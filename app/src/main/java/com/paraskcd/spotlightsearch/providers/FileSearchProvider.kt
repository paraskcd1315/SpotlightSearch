package com.paraskcd.spotlightsearch.providers

import android.content.Context
import android.os.Environment
import com.paraskcd.spotlightsearch.enums.SearchResultType
import com.paraskcd.spotlightsearch.utils.FileUtils
import com.paraskcd.spotlightsearch.icons.Document
import com.paraskcd.spotlightsearch.types.SearchResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileSearchProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val cache = mutableMapOf<String, List<SearchResult>>()

    suspend fun searchFiles(query: String): List<SearchResult> = withContext(Dispatchers.IO) {
        cache[query]?.let { return@withContext it }

        val root = Environment.getExternalStorageDirectory()
        // Soporte para comodines tipo shell
        val regexPattern = query
            .replace(".", "\\.")
            .replace("?", ".")
            .replace("*", ".*")
            .toRegex(RegexOption.IGNORE_CASE)

        fun searchRecursive(dir: File): List<File> {
            if (!dir.isDirectory || dir.isHidden) return emptyList()
            return dir.walkTopDown()
                .filter { it.isFile && it.name.matches(regexPattern) }
                .toList()
        }

        val results = searchRecursive(root).map { file ->
            SearchResult(
                title = file.name,
                subtitle = file.absolutePath,
                iconVector = Document,
                onClick = {
                    val uri = FileUtils.getUriForFile(context, file)
                    FileUtils.openFile(context, uri, file)
                },
                searchResultType = SearchResultType.FILE
            )
        }

        cache[query] = results
        return@withContext results
    }
}