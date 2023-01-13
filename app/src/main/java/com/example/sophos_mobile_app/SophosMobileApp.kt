package com.example.sophos_mobile_app

import android.app.Application
import com.example.sophos_mobile_app.data.source.local.db.SophosAppDatabase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SophosMobileApp: Application()
