package com.q2ve.schedappv2

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.q2ve.schedappv2.databinding.MainActivityBinding
import com.q2ve.schedappv2.helpers.Frames
import com.q2ve.schedappv2.helpers.VKAuthCallbackSetter
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.exceptions.VKAuthException

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class MainActivity : AppCompatActivity() {
	var allowBackButtonAction: Boolean = true
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		//AppCenter analytics
		/*AppCenter.start( TODO()
			application, Constants.appCenterId,
			Analytics::class.java, Crashes::class.java
		)*/
		
		//View and window setting
		val binding = MainActivityBinding.inflate(layoutInflater)
		setContentView(binding.root)
		this.window.apply {
			addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
			statusBarColor = Color.TRANSPARENT
			decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		}
		//this.window.apply { decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR }
		Frames.registerActivityFrame(R.id.main_activity_frame)
		
		//Setting up helpers
		val launchManager = LaunchManager(this, binding.appLogo)
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
		if (allowBackButtonAction) {
			super.onBackPressed()
		}
	}
	
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		//VK
		val callback = object: VKAuthCallback {
			override fun onLogin(token: VKAccessToken) {
				VKAuthCallbackSetter.callback?.let { it(token.accessToken) }
			}
			
			override fun onLoginFailed(authException: VKAuthException) {
				VKAuthCallbackSetter.callback?.let { it(null) }
			}
		}
		Log.e("REMOVE1337", BuildConfig.APPLICATION_ID)
		Log.e("REMOVE1337", applicationContext.packageName)
		if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
			VKAuthCallbackSetter.callback?.let { it(null) }
			super.onActivityResult(requestCode, resultCode, data)
		}
		super.onActivityResult(requestCode, resultCode, data)
	}
	
	override fun onDestroy() {
		//TODO("Занулить все постоянные ссылки на активити")
		super.onDestroy()
		Frames.unregisterActivityFrame()
	}
}