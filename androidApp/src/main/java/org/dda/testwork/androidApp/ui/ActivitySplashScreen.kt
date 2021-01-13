package org.dda.testwork.androidApp.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.dda.ankoLogger.AnkoLogger
import org.dda.ankoLogger.logDebug
import org.dda.testwork.androidApp.R


class ActivitySplashScreen : AppCompatActivity(), AnkoLogger{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        logDebug { "onCreate" }
    }
}