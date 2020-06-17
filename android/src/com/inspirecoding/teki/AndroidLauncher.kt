package com.inspirecoding.teki

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView


class AndroidLauncher : AndroidApplication()
{
    private val TAG = "AndroidLauncher"
    private var adView: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val layout = RelativeLayout(this)
        val config = AndroidApplicationConfiguration()
        val gameView: View = initializeForView(TekiClass(), config)
        layout.addView(gameView)

        adView = AdView(this)
        adView!!.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.i(TAG, "Ad Loaded...")
            }
        }

        adView!!.adSize = AdSize.SMART_BANNER
        adView!!.adUnitId = "ca-app-pub-2148248148880486/5624415866"

        val builder = AdRequest.Builder()

        val adParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        adParams.addRule(
                RelativeLayout.ALIGN_PARENT_BOTTOM,
                1)
        layout.addView(adView, adParams)
        adView!!.loadAd(builder.build())

        setContentView(layout)
    }
}