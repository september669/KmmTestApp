package org.dda.testwork.androidApp.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import dev.icerock.moko.mvvm.createViewModelFactory
import org.dda.ankoLogger.logDebug
import org.dda.ankoLogger.logError
import org.dda.testwork.androidApp.R
import org.dda.testwork.androidApp.databinding.ActivityMainBinding
import org.dda.testwork.androidApp.ui.base.BaseActivity
import org.dda.testwork.androidApp.ui.dish_hit_list.DishHitListFragment
import org.dda.testwork.androidApp.ui.restaurant_list.RestaurantListFragment
import org.dda.testwork.androidApp.ui.restaurant_review_list.RestaurantReviewListFragment
import org.dda.testwork.shared.utils.checkWhen
import org.dda.testwork.shared.view_model.main_screen.MainScreenViewModel
import org.dda.testwork.shared.view_model.main_screen.MainState.Action
import org.dda.testwork.shared.view_model.main_screen.MainState.Screen
import org.kodein.di.direct
import org.kodein.di.instance


class ActivityMain : BaseActivity<ActivityMainBinding>() {

    private val fragmentContainerId = R.id.activityMainFragmentLayout

    lateinit var viewModel: MainScreenViewModel

    override fun inflateViewBind(): ActivityMainBinding {
        logDebug("inflateViewBind()")
        return ActivityMainBinding.inflate(layoutInflater).also { binding ->
            viewModel = ViewModelProvider(this,
                createViewModelFactory {
                    direct.instance<MainScreenViewModel>()
                }
            )[MainScreenViewModel::class.java]

            logDebug { "createViewModelFactory(): $viewModel" }

            launchUi {
                viewModel.flowOneTimeAction.collectOnEach {
                    renderContent(it)
                }
            }

            binding.activityMainNavigation.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.mainMenuRestaurantList -> {
                        viewModel fire Action.ChangeScreen(Screen.RestaurantList)
                    }
                    R.id.mainMenuDishHitList -> {
                        viewModel fire Action.ChangeScreen(Screen.DishHitList)
                    }
                    R.id.mainMenuRestaurantReviewList -> {
                        viewModel fire Action.ChangeScreen(Screen.RestaurantReviewList)
                    }
                    else -> {
                        logError("Unknown menuItem: ${menuItem.title}")
                        viewModel fire Action.ChangeScreen(Screen.RestaurantList)
                    }
                }
                true
            }
        }
    }

    private fun renderContent(screen: Screen) {
        logDebug { "renderContent($screen)" }
        when (screen) {
            Screen.RestaurantList -> {
                if (supportFragmentManager.lastBackStackEntry?.name != RestaurantListFragment.screenKey) {
                    fragmentReplace(RestaurantListFragment(), RestaurantListFragment.screenKey)
                }
                Unit
            }
            Screen.DishHitList -> {
                if (supportFragmentManager.lastBackStackEntry?.name != DishHitListFragment.screenKey) {
                    fragmentReplace(DishHitListFragment(), DishHitListFragment.screenKey)
                }
                Unit
            }
            Screen.RestaurantReviewList -> {
                if (supportFragmentManager.lastBackStackEntry?.name != RestaurantReviewListFragment.screenKey) {
                    fragmentReplace(RestaurantReviewListFragment(), RestaurantReviewListFragment.screenKey)
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

    override fun onBackPressed() {
        logDebug { "onBackPressed() backStackEntryCount: ${supportFragmentManager.backStackEntryCount} " }
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
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