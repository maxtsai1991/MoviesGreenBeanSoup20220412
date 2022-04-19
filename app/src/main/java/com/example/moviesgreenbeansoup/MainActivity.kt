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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesgreenbeansoup.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.content_main.*
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

/**
 * API串接應用
 * 13-6 展示清單資料-進階RecyclerView好評電影瀏覽 (該章節是使用匿名內部類別的ViewHolder)
 * 1.   content_main.xml ) 先拉RecyclerView元件,並設定ID : recycler
 * 2.   build.gradle(Module) ) 添加必要外掛(才有辦法快速利用此外掛,得到Layout上的元件) :
 *          id 'kotlin-kapt' // Android Jetpack
 *          id 'kotlin-android-extensions'
 * 2-1. 設定是否為固定大小 EX : recycler.setHasFixedSize(true)
 * 2-2. 設定Layout管理器 EX : recycler.layoutManager = LinearLayoutManager(this)
 * 3.   MovieAdapter ) 接下來創建Adapter > 在Package(moviesgreenbeansoup)上按右鍵 > New > Kotlin File/Class >命名MovieAdapter(Class) EX : class MovieAdapter { }
 * 4.   MovieAdapter ) 並且在Adapter創建匿名內部類別的ViewHolder , 並繼承ViewHolder,參數放view , 而MovieViewHolder傳入一個View類別 EX : inner class MovieViewHolder(view : View) : RecyclerView.ViewHolder(view)
 * 5.   movie_row.xml ) 設計一個Layout ,一列資料(單列資料)的Layout > layout > New > Layout Resource File > 命名movie_row.xml ,
 *      拉一個ImageView,設置ID:movie_poster , 再拉一個TextView(電影名稱),設置ID:movie_title , 再拉一個TextView(評分),設置ID:movie_pop
 * 6.   MovieAdapter ) 在MovieViewHolder把剛剛單列資料的item放入後 , 就可以回頭設計Adapter EX :
 *      val poster = itemView.movie_poster
 *      val title = itemView.movie_title
 *      val popularity = itemView.movie_pop
 * 7.   MovieAdapter ) MovieAdapter 繼承Adapter(androidx.recyclerview.widget.RecyclerView), 裡面一定要有一個ViewHolder , 之後在呼叫空建構子() EX : class MovieAdapter  : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){ }
 * 8.   接著實做Adapter身上的三個方法 (燈泡熱鍵>覆寫 1.onCreateViewHolder 2.onBindViewHolder 3.getItemCount)
 * 9.   onCreateViewHolder ) 在onCreateViewHolder方法裡要產生一個view , 並且return MovieViewHolder,把view交給它 EX :
 *      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
 *          val view = LayoutInflater.from(parent.context) .inflate(R.layout.movie_row, parent , false)
 *          return MovieViewHolder(view)
 *      }
 * 10.  onBindViewHolder ) 資料來的時候要怎麼辦,就onBindViewHolder,holder身上的title,等於得到的資料,那個電影名稱 EX : holder.title.setText(movie.title)
 * 10-1.在MovieAdapter設計一個建構子,讓它把所有資料放進來 , 要用var讓資料可以改變 EX : (var movies : List<Movie>)
 * 10-2.所有的電影(movies)如果不是null,在執行run裡面的工作 EX :  movies?.run { }
 * 10-3.先得到一個電影,就從movies裡面的List集合去get這個position資料 EX : val movie = movies.get(position)
 * 10-4.接下來在設定電影的熱門度(電影評分) , 它是一個Double型態, 再toString就可以轉型 EX : holder.popularity.setText(movie.popularity.toString())
 * 11.  getItemCount ) 接下來是資料數量 , 如果你的movies,給它size,是null的話要給0
 *      override fun getItemCount(): Int {
 *          return movies.size ?: 0
 *      }
 * 12. MainActivity.kt ) 生出adapter , 將adapter 改成全域屬性 EX :
 *     private lateinit var adapter: MovieAdapter
 *     adapter = MovieAdapter(result.results) // 生出adapter , 要在CoroutineScope協程裡面,因為協程已經抓到TMDB API資料了
 * 13. MainActivity.kt ) 存取UI元件,不能在其他的UI執行緒上,為了避免,所以要使用runOnUiThread,讓設定adapter的工作,在UI執行緒執行 EX :  runOnUiThread { }
 * 14. MainActivity.kt ) 將adapter設定到recycler的adapter EX : recycler.adapter = adapter
 * 14. MainActivity.kt ) 當拿到資料後,要重整(刷新) EX : adapter.notifyDataSetChanged()
 * 15. movie_row.xml ) 嚴重BUG記得檢查 , ConstraintLayout元件的layout_height(高度)改成wrap_content(符合內容物高度就好)
 */

/**
 * API串接應用
 * 13-7 RecyclerView圖片處理 - 使用Coil類別庫 (把TMDB API 所抓到的圖片,放到ImageView上來顯示)
 *  補充說明:
 *      TMDB API 有細項說明如何取得 API的圖片 (https://developers.themoviedb.org/3/getting-started/images)
 *      如寬度是500的照片,完整TMDB圖片URL是 : https://image.tmdb.org/t/p/w500/wwemzKWzjKYJFfCeiB57q3r4Bcm.png , w500 是型態,寬度500點的意思
 *      TMDB API 的照片 欄位是 "poster_path" (可以從Postman輸入URL後查看下面個欄位,使用GET,URL:https://api.themoviedb.org/3/movie/popular?api_key=8bf9f7ac5da357c4c0d5f04b41504e76&language=zh-TW&page=1)
 *      如Android(Java開發) , 抓取圖檔類別庫有 Glide , Picaso , Fresco , 而Kotlin開發抓取圖檔類別庫則是Coil ,該章節Coil有搭配Coroutines協程
 *  1.  build.gradle(Module) ) 導入Coil Libs EX : implementation("io.coil-kt:coil:2.0.0-rc03")
 *  2.  得到TMDB資料的時候,是跑到onBindViewHolder方法裡 , 怎麼使用 ? 使用 holder.poster在呼叫.load()方法,接著要把網址"https://image.tmdb.org/t/p/w500"放到方法裡,在w500後面加上API圖片的變數(poster_path) EX : holder.poster.load("https://image.tmdb.org/t/p/w500${movie.poster_path}"){ }
 *  3.  在讀API圖檔時,還沒讀到的話,可以用placeholder()方法,此方法會顯示還沒讀到API圖時顯示的圖片 EX : placeholder(R.drawable.movie_poster)
 *  3-1.圓形圖顯示 EX : transformations(CircleCropTransformation())
 *  3-2.讀不到API圖時顯示的圖片 EX : error(R.drawable.error_movie_poster)
 */

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: MovieAdapter //  lateinit 意思是晚一點才會生出來
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    companion object{
        val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        // RecyclerView的必要設定 , 要添加第二點必要外掛,才能找到recycler元件
        recycler.setHasFixedSize(true) // 設定是否為固定大小
        recycler.layoutManager = LinearLayoutManager(this) // Layout管理器

        CoroutineScope(Dispatchers.IO).launch {
            val data = URL("https://api.themoviedb.org/3/movie/popular?api_key=8bf9f7ac5da357c4c0d5f04b41504e76&language=zh-TW&page=1").readText() // 讀取API Json
            Log.d(TAG, "onCreate: $data")

            // 使用Gson的類別庫,先用建構子()產生Gson的物件,在呼叫fromJson的方法,fromJson方法第"一"個參數給"字串資料" , 第"二"個參數給目標的結果類別,這樣就可以產生出一個集合 EX : val result = Gson().fromJson(data,MovieResult::class.java)
            val result = Gson().fromJson(data,MovieResult::class.java)

            result.results.forEach {
                Log.d(TAG, "onCreate: ${it.title}");
            }

            // 生出Adapter
            adapter = MovieAdapter(result.results)

            // 注意: 存取UI元件,不能在其他的UI執行緒上,為了避免,所以要使用runOnUiThread,讓設定adapter的工作,在UI執行緒執行
            runOnUiThread {
                recycler.adapter = adapter // 將adapter設定到recycler的adapter
                adapter.notifyDataSetChanged() // 刷新
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