package com.example.moviesgreenbeansoup

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.moviesgreenbeansoup.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL


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

/**
 * API串接應用
 * 13-3 註冊電影API,建立專案
 * 1. 實作電影資料的API(TMDB,https://www.themoviedb.org/?language=zh-TW) , 註冊帳號,填寫使用者名稱,密碼,確認密碼,電子郵件
 * 2. TMDB ACC & PW :  KotlinMaxTsai20220412 / Max800320 ,進到個人檔案與設定 > 編輯個人檔案 > 左邊點選API (https://www.themoviedb.org/settings/api) > 點選請求一個API金鑰 > 點選Developer開發者腳色 > 填寫必要資訊
 * 3. 進入API 文件說明https://developers.themoviedb.org/3/getting-started/introduction
 * 4. 建立Android 專案 > 選Basic Activity 命名為moviesgreenbeansoup ,語言記得是Kotlin ,移除不必要的程式碼 , 像是content_main.xml 的nav_host_fragment元件刪除 ,而MainActivity.kt相對應的nav程式碼也要刪除
 * 5. 程式規範該專案必須連線網路 ,去manifests ,添加 <uses-permission android:name="android.permission.INTERNET"/>
 */

/**
 * API串接應用
 * 13-4 閱讀API文件,網路連線取得資料
 * API串接流程圖(最終目的放到RecyclerView) :
 *     網路上API(Json字串格式)資料 > 解析成json.ort Or gson > 設計data class > 將解析後的json資料轉變成List集合資料(一包資料) > 設計Adapter > 設計Adapter前,先設計ViewHolder > 設計Layout > 最後將資料給Adapter > Adapter再交給RecyclerView
 * 1.  從網頁上讀取資料,是字串的格式,要經過Json的解析,可以使用json.org,或是gson,又或是jackson的類別庫,
 * 1-1.在實務上,會先設計data clas,他的原因是如果資料的欄位有很多欄位,都在處理變數,則程式碼會非常的亂,而且不容易維護,如果可以利用data class,把這些複雜的資料,封裝在類別產生的物件當中,再處理的時候,工作也減少,未來,他人打開程式碼的時候也會輕鬆,
 * 1-2.接下來Json解析完後要得到一個List集合,所謂的一包資料,
 * 1-3.這包List集合資料,要交給RecyclerView之前,必須先設計Adapter類別,Adapter類別要使用之前,先設計ViewHolder類別,然後再針對一列資料,單列資料來設計Layout,最後再將Adapter來交給RecyclerView。
 *
 * 2. 先進到TMDB URL (https://www.themoviedb.org/) > 登入 ( KotlinMaxTsai20220412 / Max800320 ) > 進到個人檔案與設定 > 編輯個人檔案 > 左邊點選API > 複製API Key ( 8bf9f7ac5da357c4c0d5f04b41504e76 ) > 點選文件的developers.themoviedb.org.連結
 *    切換到Try it out (TMDB內建測試API連線處)頁標簽 > 在api_key貼上 API Key ( 8bf9f7ac5da357c4c0d5f04b41504e76 ) > 並點選 SEND REQUEST 按鈕 ,上述步驟測試API有無通,
 *    可直接在POSTMAN上,貼上https://api.themoviedb.org/3/movie/popular?api_key=8bf9f7ac5da357c4c0d5f04b41504e76&language=zh-TW&page=1 ,使用GET ,就可以了
 * 3. MainActivity ) 做連線 , 並且使用協程 ,不使用協程會報錯,因為不能在主執行緒(UI)上做網路連線,
 *    使用協程時,必須先在build.gradle(Module)加Coroutines 協程的 Libs EX :
 *     def coroutines_version = "1.3.0"
 *     implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version"
 *     implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"
 *     之後,打上協程程式碼 EX : CoroutineScope(Dispatchers.IO).launch { } ,
 *     在協程裡,先確認有無連線到TMDB API, EX : val data = URL("https://api.themoviedb.org/3/movie/popular?api_key=8bf9f7ac5da357c4c0d5f04b41504e76&language=zh-TW&page=1").readText() ,
 *     並且插Log EX : Log.d(TAG, "onCreate: $data"); , 插LOG的TAG,必須創一個TAG屬性欄位 , 用companion object{}包起來 , EX : val TAG = MainActivity::class.java.simpleName
 */

/**
 * API串接應用
 * 13-5 JSON資料與解析,使用外掛建立data class (1.讀取API Json 2.設計data class 3.用Gson解析Json資料)
 * 補充說明 : 先安裝 data class的JSON To Kotlin Class 外掛 > 雙擊兩下Shift > 搜尋Plugins > 並在Marketplace標籤下搜尋 JSON To Kotlin Class (圖示是一個藍色有J K 英文的圖示),並且安裝它
 * 1. 安裝完外掛後 , 在Package(moviesgreenbeansoup)上按右鍵 > New > Kotlin File/Class , 新建一個Kotlin Class檔案,
 *    在Data.kt檔案下,使用快捷鍵Alt + Insert > 點選 Kotlin data classes from JSON ,
 *    並將TMDB Raw格式 (Postman也有Raw標籤), 貼到中間大白框裡面, 並命名 MovieResult ,這樣就是快速產生data class的方法
 * 2. 利用Gson比較好的工具,來整批解析變成是一個集合,添加 Gson Libs類別庫 , 在build.gradle(Module)添加:implementation 'com.google.code.gson:gson:2.9.0'
 * 3. MainActivity.kt ) 使用Gson的類別庫,先用建構子()產生Gson的物件,在呼叫fromJson的方法,fromJson方法第"一"個參數給"字串資料" , 第"二"個參數給目標的結果類別,這樣就可以產生出一個集合 EX : val result = Gson().fromJson(data,MovieResult::class.java)
 *    接下來從result裡面,可以得到很多個結果,再用forEach{}迴圈,把一個一個電影的標題(電影名稱),LOGD出來, EX :
 *    result.results.forEach {
 *      Log.d(TAG, "onCreate: ${it.title}");
 *    }
 */
class MainActivity : AppCompatActivity() {
    companion object{
        val TAG = MainActivity::class.java.simpleName
    }
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
        CoroutineScope(Dispatchers.IO).launch {
            val data = URL("https://api.themoviedb.org/3/movie/popular?api_key=8bf9f7ac5da357c4c0d5f04b41504e76&language=zh-TW&page=1").readText() // 讀取API Json
            Log.d(TAG, "onCreate: $data")
            // 使用Gson的類別庫,先用建構子()產生Gson的物件,在呼叫fromJson的方法,fromJson方法第"一"個參數給"字串資料" , 第"二"個參數給目標的結果類別,這樣就可以產生出一個集合 EX : val result = Gson().fromJson(data,MovieResult::class.java)
            val result = Gson().fromJson(data,MovieResult::class.java)
            result.results.forEach {
                Log.d(TAG, "onCreate: ${it.title}");
            }
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