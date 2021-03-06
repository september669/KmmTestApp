package org.dda.testwork.shared.di

import org.dda.ankoLogger.logDebug
import org.dda.testwork.shared.repo.RepoDishes
import org.dda.testwork.shared.repo.RepoRestaurantReview
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

    bind<RepoDishes>() with singleton {
        diLogger.logDebug("bind<RepoDishes>()")
        RepoDishes(
            service = instance()
        )
    }

    bind<RepoRestaurantReview>() with singleton {
        diLogger.logDebug("bind<RepoRestaurantReview>()")
        RepoRestaurantReview(
            service = instance()
        )
    }


}