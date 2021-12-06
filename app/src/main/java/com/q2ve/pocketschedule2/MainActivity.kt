package com.q2ve.pocketschedule2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.q2ve.pocketschedule2.ui.Frames
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		//AppCenter analytics
		/*AppCenter.start( TODO()
			application, Constants.appCenterId,
			Analytics::class.java, Crashes::class.java
		)*/
		
		//View and window setting
		setContentView(R.layout.main_activity)
		this.window.apply { decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR }
		Frames.registerActivityFrame(R.id.main_activity_frame)
		
		//Setting up helpers
		val launchManager = LaunchManager(this)
		launchManager.initializeHelpers()
		
		//AppCenter analytics
		//Analytics.trackEvent("started")
		
		//Starting functional
		launchManager.applicationStarted()
	}
	
	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
	}
	
	override fun onBackPressed() {
		super.onBackPressed()
	}
	
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		//VK
		/*
		val callback = object: VKAuthCallback {
			override fun onLogin(token: VKAccessToken) {
				//Log.d("VK", "----------------------")
				//Log.d("VK token", token.accessToken)
				if (vkCallbackDestination != null) {
					vkCallbackDestination!!.vkAuthCallback(token.accessToken)
				}
			}
			override fun onLoginFailed(errorCode: Int) {
				//Log.d("VK", "----------------------")
				//Log.d("VK login failed", errorCode.toString())
				if (vkCallbackDestination != null) {
					vkCallbackDestination!!.vkAuthCallback(null)
				}
			}
		}
		if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
			//Log.d("VK", "----------------------")
			//Log.d("VK data == null", resultCode.toString())
			if (vkCallbackDestination != null) {
				vkCallbackDestination!!.vkAuthCallback(null)
			}
			super.onActivityResult(requestCode, resultCode, data)
		}
		*/
		super.onActivityResult(requestCode, resultCode, data)
	}
	
	override fun onDestroy() {
		//TODO("Занулить все постоянные ссылки на активити")
		super.onDestroy()
		Frames.unregisterActivityFrame()
	}
}