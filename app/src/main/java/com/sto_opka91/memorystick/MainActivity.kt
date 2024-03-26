package com.sto_opka91.memorystick

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sto_opka91.memorystick.databinding.ActivityMainBinding
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val banner = findViewById<BannerAdView>(R.id.bannerYa)
        banner.setAdUnitId("R-M-6979016-1")
        banner.setAdSize(BannerAdSize.stickySize(this,350))
        val adRequest = AdRequest.Builder().build()
        banner.loadAd(adRequest)
    }


}