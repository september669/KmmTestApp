package org.dda.testwork.androidApp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import dev.icerock.moko.mvvm.createViewModelFactory
import org.dda.ankoLogger.AnkoLogger
import org.dda.ankoLogger.logDebug
import org.dda.testwork.androidApp.R
import org.dda.testwork.androidApp.ui.restaurant_list.RestaurantListFragment
import org.dda.testwork.shared.redux.VMEvents
import org.dda.testwork.shared.utils.checkWhen
import org.dda.testwork.shared.view_model.main_screen.MainScreenViewModel
import org.dda.testwork.shared.view_model.main_screen.MainState
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.direct
import org.kodein.di.instance


class ActivityMain : AppCompatActivity(), DIAware, AnkoLogger {

    private val fragmentContainerId = R.id.activityMainFragmentLayout

    override val di by closestDI()

    lateinit var viewModel: MainScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        logDebug { "onCreate()" }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this,
            createViewModelFactory {
                direct.instance<MainScreenViewModel>()
            }
        )[MainScreenViewModel::class.java]

        viewModel.liveDataState.ld().observe(this) { state ->
            when (state) {
                is VMEvents.ViewState.ShowContent -> renderContent(state.content)
                is VMEvents.ViewState.ShowError -> TODO()
            }.checkWhen()
        }
    }

    private fun renderContent(content: MainState.State) {
        logDebug { "renderContent($content)" }
        when (content) {
            MainState.State.RestaurantList -> {
                if (supportFragmentManager.lastBackStackEntry?.name != RestaurantListFragment.screenKey) {
                    fragmentReplace(RestaurantListFragment(), RestaurantListFragment.screenKey)
                }
                Unit
            }
        }.checkWhen()
    }

    private fun fragmentReplace(fragment: Fragment, screenKey: String) {
        logDebug { "fragmentReplace(${fragment::class.simpleName}, $screenKey)" }

        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction
            .replace(fragmentContainerId, fragment, screenKey)
            .addToBackStack(screenKey)
            .commit()
    }
}


val FragmentManager.lastBackStackEntry: FragmentManager.BackStackEntry?
    get() {
        return if (backStackEntryCount > 0) {
            getBackStackEntryAt(backStackEntryCount - 1)
        } else {
            null
        }
    }