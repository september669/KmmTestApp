package org.dda.testwork.shared.di

import org.dda.ankoLogger.logDebug
import org.dda.testwork.shared.view_model.restaurant_list.RestaurantListViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

const val moduleViewModelName = "ModuleViewModelName"


val moduleViewModel = DI.Module(name = moduleViewModelName) {

    importOnce(moduleRepo)

    bind<RestaurantListViewModel>() with provider {
        diLogger.logDebug("bind<RestaurantListPresenter>()")
        RestaurantListViewModel(instance())
    }


}