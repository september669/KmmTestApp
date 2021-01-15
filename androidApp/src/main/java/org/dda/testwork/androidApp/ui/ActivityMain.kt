package org.dda.testwork.androidApp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.dda.testwork.androidApp.R
import org.dda.testwork.androidApp.ui.restaurant_list.RestaurantListFragment


class ActivityMain : AppCompatActivity() {

    private val fragmentContainerId = R.id.activityMainFragmentLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentReplace(
            RestaurantListFragment(), RestaurantListFragment.screenKey
        )
    }


    fun fragmentReplace(fragment: Fragment, screenKey: String) {

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
