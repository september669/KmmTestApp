package org.dda.testwork.shared.di

import org.dda.testwork.shared.repo.RepoRestaurants
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton


const val moduleModuleRepo = "ModuleRepo"

val moduleRepo = DI.Module(name = moduleModuleRepo) {

    importOnce(moduleApi)

    bind<RepoRestaurants>() with singleton {
        RepoRestaurants(
            service = instance()
        )
    }


}