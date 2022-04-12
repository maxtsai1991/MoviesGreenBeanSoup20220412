package com.example.moviesgreenbeansoup

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.moviesgreenbeansoup.databinding.ActivityMainBinding


/**
 * API串接應用
 * 13-1 什麼是API ? 有哪些種類
 * 該章節,主要是說明如何連接別人的API, API 全名Application Interface ,API一定有文件,讓你知道怎樣可以讀到你想要的資料,API泛指外部資料的介面
 */

/**
 * API串接應用
 * 13-2 常見的API架構與應用
 * 搜尋open data ,
 * 不需經過認證,或是帳號申請 EX : 1. 政府資料開放平臺https://data.gov.tw/  2. 臺北市資料大平臺https://data.taipei/  3. 內政資料開放平臺https://data.moi.gov.tw/MoiOD/default/Index.aspx
 * 需經過認證,或是帳號申請 EX : 藍新https://www.newebpay.com/website/Page/content/download_api
 */
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}