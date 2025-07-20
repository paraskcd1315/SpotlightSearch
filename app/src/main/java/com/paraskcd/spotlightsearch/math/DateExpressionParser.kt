package com.paraskcd.spotlightsearch.math

import com.paraskcd.spotlightsearch.enums.SearchResultType
import com.paraskcd.spotlightsearch.icons.Calculate
import com.paraskcd.spotlightsearch.types.SearchResult
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateExpressionParser @Inject constructor() {
    fun parse(raw: String): SearchResult? {
        val input = raw.lowercase(Locale.getDefault()).trim()
        val weekdaysEs = listOf("domingo", "lunes", "martes", "miércoles", "jueves", "viernes", "sábado")
        val weekdaysEn = listOf("sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday")
        val monthsEs = listOf("enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre")
        val monthsEn = listOf("january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december")

        // Helper to format date in a localized way depending on locale
        fun formatDate(date: Date, locale: Locale): String {
            val fmt = SimpleDateFormat("EEEE, d MMMM yyyy, HH:mm", locale)
            return fmt.format(date).replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
        }

        fun buildResult(date: Date, isEn: Boolean = false): SearchResult {
            val locale = if (isEn) Locale.ENGLISH else Locale.getDefault()
            return SearchResult(
                title = formatDate(date, locale),
                subtitle = if (isEn) "Detected date" else "Fecha detectada",
                iconVector = Calculate,
                searchResultType = SearchResultType.CALCULATOR,
                onClick = {}
            )
        }

        // --- Named day Spanish: "el lunes", "martes pasado", "jueves que viene"
        val namedDayEs = Regex("""^(el\s*)?(lunes|martes|miércoles|jueves|viernes|sábado|domingo)(\s+que\s+viene|\s+pasado)?$""").matchEntire(input)
        if (namedDayEs != null) {
            val day = weekdaysEs.indexOf(namedDayEs.groupValues[2])
            val offset = namedDayEs.groupValues[3].trim()
            val today = Calendar.getInstance()
            val currentDay = today.get(Calendar.DAY_OF_WEEK) - 1 // Calendar SUNDAY=1, so 0-based
            var delta = day - currentDay
            when (offset) {
                "pasado" -> delta -= if (delta >= 0) 7 else 0
                "que viene" -> delta += if (delta <= 0) 7 else 0
            }
            today.add(Calendar.DATE, delta)
            return buildResult(today.time, false)
        }

        // --- Named day English: "next monday", "last friday"
        val namedDayEn = Regex("""^(next|last)?\s*(monday|tuesday|wednesday|thursday|friday|saturday|sunday)$""").matchEntire(input)
        if (namedDayEn != null) {
            val offsetType = namedDayEn.groupValues[1]
            val day = weekdaysEn.indexOf(namedDayEn.groupValues[2])
            val today = Calendar.getInstance()
            val currentDay = today.get(Calendar.DAY_OF_WEEK) - 1
            var delta = day - currentDay
            when (offsetType) {
                "last" -> delta -= if (delta >= 0) 7 else 0
                "next" -> delta += if (delta <= 0) 7 else 0
            }
            today.add(Calendar.DATE, delta)
            return buildResult(today.time, true)
        }

        // --- Ordinal weekday of month English: "first monday of january"
        val ordinalDayEn = Regex("""^(first|second|third|fourth|last)\s+(monday|tuesday|wednesday|thursday|friday|saturday|sunday)\s+of\s+(january|february|march|april|may|june|july|august|september|october|november|december)$""")
        val ordinalDayMatchEn = ordinalDayEn.matchEntire(input)
        if (ordinalDayMatchEn != null) {
            val pos = ordinalDayMatchEn.groupValues[1]
            val weekday = ordinalDayMatchEn.groupValues[2]
            val monthName = ordinalDayMatchEn.groupValues[3]
            val targetDay = weekdaysEn.indexOf(weekday)
            val targetMonth = monthsEn.indexOf(monthName)
            val now = Calendar.getInstance()
            val year = now.get(Calendar.YEAR)
            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, targetMonth)
            if (pos == "last") {
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                while (cal.get(Calendar.DAY_OF_WEEK) - 1 != targetDay) {
                    cal.add(Calendar.DAY_OF_MONTH, -1)
                }
            } else {
                val countMap = mapOf("first" to 1, "second" to 2, "third" to 3, "fourth" to 4)
                val nth = countMap[pos] ?: 1
                cal.set(Calendar.DAY_OF_MONTH, 1)
                var matches = 0
                while (matches < nth) {
                    if (cal.get(Calendar.DAY_OF_WEEK) - 1 == targetDay) matches++
                    if (matches < nth) cal.add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            return buildResult(cal.time, true)
        }

        // --- Ordinal weekday of month Spanish: "primer lunes de agosto"
        val ordinalDayEs = Regex("""^(primer|primero|segundo|tercero|cuarto|último)\s+(lunes|martes|miércoles|jueves|viernes|sábado|domingo)\s+de\s+(enero|febrero|marzo|abril|mayo|junio|julio|agosto|septiembre|octubre|noviembre|diciembre)$""")
        val ordinalDayMatchEs = ordinalDayEs.matchEntire(input)
        if (ordinalDayMatchEs != null) {
            val pos = ordinalDayMatchEs.groupValues[1]
            val weekday = ordinalDayMatchEs.groupValues[2]
            val monthName = ordinalDayMatchEs.groupValues[3]
            val targetDay = weekdaysEs.indexOf(weekday)
            val targetMonth = monthsEs.indexOf(monthName)
            val now = Calendar.getInstance()
            val year = now.get(Calendar.YEAR)
            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, targetMonth)
            if (pos.contains("último")) {
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                while (cal.get(Calendar.DAY_OF_WEEK) - 1 != targetDay) {
                    cal.add(Calendar.DAY_OF_MONTH, -1)
                }
            } else {
                val countMap = mapOf("primer" to 1, "primero" to 1, "segundo" to 2, "tercero" to 3, "cuarto" to 4)
                val nth = countMap[pos] ?: 1
                cal.set(Calendar.DAY_OF_MONTH, 1)
                var matches = 0
                while (matches < nth) {
                    if (cal.get(Calendar.DAY_OF_WEEK) - 1 == targetDay) matches++
                    if (matches < nth) cal.add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            return buildResult(cal.time, false)
        }

        // --- Date literal English: "march 15"
        val dateLiteralEn = Regex("""^(january|february|march|april|may|june|july|august|september|october|november|december)\s*(\d{1,2})$""")
        val dateLiteralMatchEn = dateLiteralEn.matchEntire(input)
        if (dateLiteralMatchEn != null) {
            val month = monthsEn.indexOf(dateLiteralMatchEn.groupValues[1])
            val day = dateLiteralMatchEn.groupValues[2].toInt()
            val today = Calendar.getInstance()
            var year = today.get(Calendar.YEAR)
            if (today.get(Calendar.MONTH) > month || (today.get(Calendar.MONTH) == month && today.get(Calendar.DAY_OF_MONTH) > day)) {
                year += 1
            }
            val cal = Calendar.getInstance()
            cal.set(year, month, day, 12, 0, 0)
            return buildResult(cal.time, true)
        }

        // --- Date literal Spanish: "el 3 de marzo"
        val dateLiteralEs = Regex("""^el\s*(\d{1,2})\s*de\s*(enero|febrero|marzo|abril|mayo|junio|julio|agosto|septiembre|octubre|noviembre|diciembre)$""")
        val dateLiteralMatchEs = dateLiteralEs.matchEntire(input)
        if (dateLiteralMatchEs != null) {
            val day = dateLiteralMatchEs.groupValues[1].toInt()
            val month = monthsEs.indexOf(dateLiteralMatchEs.groupValues[2])
            val today = Calendar.getInstance()
            var year = today.get(Calendar.YEAR)
            if (today.get(Calendar.MONTH) > month || (today.get(Calendar.MONTH) == month && today.get(Calendar.DAY_OF_MONTH) > day)) {
                year += 1
            }
            val cal = Calendar.getInstance()
            cal.set(year, month, day, 12, 0, 0)
            return buildResult(cal.time, false)
        }

        // --- Relative expressions: "mañana", "tomorrow", "ayer", "yesterday", "hoy", "today"
        val relative = mapOf(
            "mañana" to 1, "tomorrow" to 1,
            "ayer" to -1, "yesterday" to -1,
            "hoy" to 0, "today" to 0
        )
        if (relative.containsKey(input)) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, relative[input]!!)
            val isEn = input in listOf("tomorrow", "yesterday", "today")
            return buildResult(cal.time, isEn)
        }

        // --- Relative math: "today + 3 months", "ayer - 2 días", etc.
        val dateMath = Regex("""^(today|hoy|tomorrow|mañana|yesterday|ayer|yesteryear|añopasado)\s*([\+\-])\s*(\d+)\s*(days?|días?|weeks?|semanas?|months?|meses?|years?|años?)$""")
        val dateMathMatch = dateMath.matchEntire(input)
        if (dateMathMatch != null) {
            val baseStr = dateMathMatch.groupValues[1]
            val op = dateMathMatch.groupValues[2]
            val amount = dateMathMatch.groupValues[3].toInt()
            val unit = dateMathMatch.groupValues[4]
            val baseDate = Calendar.getInstance()
            when {
                baseStr in listOf("tomorrow", "mañana") -> baseDate.add(Calendar.DATE, 1)
                baseStr in listOf("yesterday", "ayer") -> baseDate.add(Calendar.DATE, -1)
                baseStr in listOf("yesteryear", "añopasado") -> baseDate.add(Calendar.YEAR, -1)
            }
            val sign = if (op == "+") 1 else -1
            when {
                unit.startsWith("day") || unit.startsWith("día") -> baseDate.add(Calendar.DATE, sign * amount)
                unit.startsWith("week") || unit.startsWith("semana") -> baseDate.add(Calendar.DATE, sign * amount * 7)
                unit.startsWith("month") || unit.startsWith("mes") -> baseDate.add(Calendar.MONTH, sign * amount)
                unit.startsWith("year") || unit.startsWith("año") -> baseDate.add(Calendar.YEAR, sign * amount)
            }
            val isEn = baseStr in listOf("today", "tomorrow", "yesterday", "yesteryear")
            return buildResult(baseDate.time, isEn)
        }

        return null
    }
}