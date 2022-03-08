package com.feeling.demo.permission

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
	
	private val btRequest: AppCompatButton by lazy { findViewById(R.id.button) }
	private val requestPermission = registerForActivityResult(
		ActivityResultContracts.RequestMultiplePermissions()
	) { result ->
		result.map {
			Log.e(TAG, "key:${it.key} value:${it.value}")
		}
		//授予的权限
		val granted = result.filterValues { it }
		if (result.size == granted.size) {
			//TODO 权限授权后的处理
			Log.e(TAG, "全部权限授权")
			return@registerForActivityResult
		}
		//拒绝的权限，包含可再次请求和不再请求的权限
		val denied = result.filterValues { !it }
		//可再次请求的拒绝权限
		val requestDenied =
			denied.filter { ActivityCompat.shouldShowRequestPermissionRationale(this, it.key) }
		//不再请求的拒绝权限
		val alwaysDenied = denied.filter { !requestDenied.containsKey(it.key) }
		
		Log.e(TAG, "授权的权限")
		granted.map {
			Log.e(TAG, "key:${it.key} value:${it.value}")
		}
		
		Log.e(TAG, "可再次请求的权限")
		requestDenied.map {
			Log.e(TAG, "key:${it.key} value:${it.value}")
		}
		
		Log.e(TAG, "不再询问的权限")
		alwaysDenied.map {
			Log.e(TAG, "key:${it.key} value:${it.value}")
		}
		
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		btRequest.setOnClickListener {
			requestPermission.launch(
				arrayOf(
					Manifest.permission.WRITE_EXTERNAL_STORAGE,
					Manifest.permission.READ_EXTERNAL_STORAGE,
					Manifest.permission.ACCESS_COARSE_LOCATION,
					Manifest.permission.ACCESS_FINE_LOCATION,
					Manifest.permission.READ_PHONE_STATE
				)
			)
		}
	}
}