package org.dda.testwork.shared.di

import org.dda.ankoLogger.logDebug
import org.dda.testwork.shared.repo.RepoRestaurants
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton


const val moduleRepoName = "ModuleRepo"

val moduleRepo = DI.Module(name = moduleRepoName) {

    importOnce(moduleApi)

    bind<RepoRestaurants>() with singleton {
        diLogger.logDebug("bind<RepoRestaurants>()")
        RepoRestaurants(
            service = instance()
        )
    }


}