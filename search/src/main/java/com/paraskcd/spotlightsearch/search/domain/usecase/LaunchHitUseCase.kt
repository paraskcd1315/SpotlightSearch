package com.paraskcd.spotlightsearch.search.domain.usecase

import com.paraskcd.spotlightsearch.search.domain.ports.UsagePort
import com.paraskcd.spotlightsearch.sources.domain.model.actions.HitAction
import com.paraskcd.spotlightsearch.sources.domain.model.actions.LaunchApp
import com.paraskcd.spotlightsearch.sources.domain.repository.ActionRunner
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LaunchHitUseCase @Inject constructor(
    private val usage: UsagePort,
    private val runner: ActionRunner
) {
    suspend operator fun invoke(action: HitAction) {
        runner.run(action)
        if (action is LaunchApp) withContext(NonCancellable) { usage.recordLaunch(action.packageName) }
    }
}
