package kr.bgsoft.ucargo.jeju.controller.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.*
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.UCargoActivity
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.controller.http.bridge.bgsoftjs
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment
import kr.bgsoft.ucargo.jeju.data.model.Board
import kr.bgsoft.ucargo.jeju.utils.Etc

class BoardViewFragment : DefaultFragment(), View.OnClickListener {

    var nactivity: SubActivity? = null
    private var filePathCallbackNormal: ValueCallback<Uri>? = null
    private var filePathCallbackLollipop: ValueCallback<Array<Uri>>? = null
    private val FILECHOOSER_NORMAL_REQ_CODE = 1
    private val FILECHOOSER_LOLLIPOP_REQ_CODE = 2
    var mydata = Board()

    var view_title: TextView? = null
    var view_back: ImageButton? = null
    var view_intitle: TextView? = null
    var view_intitlebox: LinearLayout? = null
    var view_indate: TextView? = null
    var view_contents_box: ScrollView? = null
    var view_contents: TextView? = null
    var view_contents_html: WebView? = null
    var view_type: TextView? = null
    var view_typebox: RelativeLayout? = null
    var view_golist: Button? = null
    var view_type2box: RelativeLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_boardview, container, false)

        nactivity = activity as SubActivity

        logd("start NoticeView")

        val data = nactivity?.intent?.getSerializableExtra(Config.EXTRA_ORDER) as Board
        mydata = data

        view_title          = layout.findViewById(R.id.header_title)
        view_back           = layout.findViewById(R.id.header_back)
        view_intitle        = layout.findViewById(R.id.board_view_title)
        view_indate         = layout.findViewById(R.id.board_view_date)
        view_contents_box   = layout.findViewById(R.id.board_view_contents_box)
        view_contents       = layout.findViewById(R.id.board_view_contents)
        view_contents_html  = layout.findViewById(R.id.board_view_contents_html)
        view_type           = layout.findViewById(R.id.board_view_type)
        view_typebox        = layout.findViewById(R.id.board_view_typebox)
        view_golist         = layout.findViewById(R.id.board_view_golist)
        view_type2box       = layout.findViewById(R.id.board_view_type2box)
        view_intitlebox     = layout.findViewById(R.id.board_view_titlebox)

        view_back?.setOnClickListener(this)
        view_golist?.setOnClickListener(this)

        val ws = view_contents_html?.settings
        ws?.javaScriptEnabled = true
        //ws.setPluginsEnabled(true);
        ws?.cacheMode = WebSettings.LOAD_NO_CACHE
        ws?.databaseEnabled = true
        val databasePath = nactivity?.applicationContext?.getDir("database", Context.MODE_PRIVATE)?.path
        ws?.databasePath = databasePath
        val cachePath = nactivity?.applicationContext?.getDir("cache", Context.MODE_PRIVATE)?.path
        ws?.setAppCachePath(cachePath)
        ws?.allowFileAccess = true
        ws?.setAppCacheEnabled(false)
        ws?.domStorageEnabled = true
        ws?.setAppCacheMaxSize((1024 * 1024 * 8).toLong())

        view_contents_html?.setNetworkAvailable(true)
        view_contents_html?.isScrollbarFadingEnabled = true
        view_contents_html?.isVerticalScrollBarEnabled = false
        view_contents_html?.isHorizontalScrollBarEnabled = false

        view_contents_html?.webChromeClient = object : WebChromeClient() {

            // For Android 3.0+
            fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String = "") {
                filePathCallbackNormal = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "image/*"
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_NORMAL_REQ_CODE)
            }

            // For Android 4.1+
            fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
                openFileChooser(uploadMsg, acceptType)
            }


            // For Android 5.0+
            override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: WebChromeClient.FileChooserParams): Boolean {
                //Log.d("MainActivity", "5.0+");
                if (filePathCallbackLollipop != null) {
                    filePathCallbackLollipop!!.onReceiveValue(null)
                    filePathCallbackLollipop = null
                }

                filePathCallbackLollipop = filePathCallback
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "image/*"
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_LOLLIPOP_REQ_CODE)
                return true
            }

            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                logd("onJsAlert call : " + url)
                if(url.toString().indexOf(" http://ktcmap.15880424.com") > -1) {
                    return true
                } else {
                    return super.onJsAlert(view, url, message, result)
                }
            }
        }

        view_contents_html?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                try {
                    logd(url.toString())
                    view.loadUrl(url)
                } catch (ex: Exception) {
                }

                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                try {

                } catch (ex: Exception) {
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

                try {

                } catch (ex: Exception) {
                }

            }

            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                AlertDialog.Builder(context).setTitle("onReceivedError").setMessage("onReceivedError:::").setCancelable(false)
                        .setNeutralButton(android.R.string.ok) { dialog, whichButton -> nactivity?.finish() }.show()
            }
        }

        view_contents_html?.setOnKeyListener { v, keyCode, event ->
            val value = true

            if(keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_UP) {
                if(view_contents_html?.url == resources.getString(R.string.url_cardjoin)) {
                    nactivity?.isFinish = true
                    nactivity?.finish()
                } else {
                    if (view_contents_html!!.canGoBack()) {
                        view_contents_html?.goBack()
                    } else {
                        nactivity?.isFinish = true
                        nactivity?.finish()
                    }
                }
            }

            value
        }

        view_contents_html?.addJavascriptInterface(bgsoftjs(nactivity), "bgsoftjs")

        if(data.idx == 0) {
            nactivity?.finish()
        } else {
            changeView(data)
        }

        return layout
    }

    fun changeView(data: Board) {
        when (data.type) {
            Config.BOARD_HNOTICE -> {

                view_title?.text = resources.getString(R.string.title_nhh)
                view_contents_box?.visibility    = View.GONE
                view_contents_html?.visibility   = View.VISIBLE
                view_type2box?.visibility        = View.GONE
                data.contents = "<html><head><meta name=\"viewport\" content=\"initial-scale=1, maximum-scale=1\"></head><body style=\"margin: 0; padding: 15px 10px; font-size: 14px; \">" + data.contents + "</body></html>"
                view_contents_html?.loadData(data.contents, "text/html; charset=utf-8", "utf-8")
                view_contents_html?.setBackgroundColor(Color.TRANSPARENT)
            }
            Config.BOARD_JIBU -> {
                view_title?.text = resources.getString(R.string.title_jibu)
                view_contents_box?.visibility    = View.GONE
                view_contents_html?.visibility   = View.VISIBLE
                view_type2box?.visibility        = View.GONE
                data.contents = "<html><head><meta name=\"viewport\" content=\"initial-scale=1, maximum-scale=1\"></head><body style=\"margin: 0; padding: 15px 10px; font-size: 14px; \">" + data.contents + "</body></html>"
                view_contents_html?.loadData(data.contents, "text/html; charset=utf-8", "utf-8")
                view_contents_html?.setBackgroundColor(Color.TRANSPARENT)
            }
            Config.BOARD_NOTICE -> {
                view_title?.text = resources.getString(R.string.title_notice)
                view_typebox?.visibility        = View.GONE
                view_contents_box?.visibility    = View.VISIBLE
                view_contents_html?.visibility   = View.GONE
                view_type2box?.visibility        = View.GONE
                view_contents?.setText(data.contents)

                val params = view_intitlebox?.layoutParams as LinearLayout.LayoutParams
                val default = if(Etc.convertDpToPixel(15f, activity!!) != null) { Etc.convertDpToPixel(15f, activity!!)!!.toInt() } else { 0 }
                params.setMargins(default, 0, default, 0)
                view_intitlebox?.layoutParams = params
            }
            Config.BOARD_NEWS -> {
                view_title?.text = resources.getString(R.string.title_news)
                view_contents_box?.visibility    = View.GONE
                view_contents_html?.visibility   = View.VISIBLE
                view_type2box?.visibility        = View.GONE
                data.contents = "<html><head><meta name=\"viewport\" content=\"initial-scale=1, maximum-scale=1\"></head><body style=\"margin: 0; padding: 15px 10px; font-size: 14px; \">" + data.contents + "</body></html>"
                view_contents_html?.loadData(data.contents, "text/html; charset=utf-8", "utf-8")
                view_contents_html?.setBackgroundColor(Color.TRANSPARENT)
            }
            Config.BOARD_FAQ -> {
                view_title?.text = resources.getString(R.string.title_faq)
                view_contents_box?.visibility    = View.GONE
                view_type2box?.visibility        = View.VISIBLE
                view_contents_html?.visibility   = View.VISIBLE
                data.contents = "<html><head><meta name=\"viewport\" content=\"initial-scale=1, maximum-scale=1\"></head><body style=\"margin: 0; padding: 15px 10px 15px 0; font-size: 14px; \">" + data.contents + "</body></html>"
                view_contents_html?.loadData(data.contents, "text/html; charset=utf-8", "utf-8")
                view_contents_html?.setBackgroundColor(Color.TRANSPARENT)
                view_indate?.visibility          = View.GONE
            }
        }

        view_intitle?.text = data.title
        view_indate?.text = resources.getString(R.string.board_view_regdate) + " " + data.date.take(10).replace("-", ".")


        if(view_typebox?.visibility == View.VISIBLE) {
            when(data.ttype) {
                resources.getString(R.string.boardk_type1) -> {
                    view_type?.text = resources.getString(R.string.boardk_type1)
                    view_type?.setBackgroundResource(R.drawable.rbox_type2_2)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        view_type?.setTextColor(resources.getColor(R.color.colorListStatus2, null))
                    } else {
                        view_type?.setTextColor(resources.getColor(R.color.colorListStatus2))
                    }
                }
                resources.getString(R.string.boardk_type2) -> {
                    view_type?.text = resources.getString(R.string.boardk_type2)
                    view_type?.setBackgroundResource(R.drawable.rbox_type2_4)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        view_type?.setTextColor(resources.getColor(R.color.colorListMixup, null))
                    } else {
                        view_type?.setTextColor(resources.getColor(R.color.colorListMixup))
                    }
                }
                resources.getString(R.string.boardk_type3) -> {
                    view_type?.text = resources.getString(R.string.boardk_type3)
                    view_type?.setBackgroundResource(R.drawable.rbox_type2_1)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        view_type?.setTextColor(resources.getColor(R.color.colorListStatus1, null))
                    } else {
                        view_type?.setTextColor(resources.getColor(R.color.colorListStatus1))
                    }
                }
                resources.getString(R.string.boardk_type4) -> {
                    view_type?.text = resources.getString(R.string.boardk_type4)
                    view_type?.setBackgroundResource(R.drawable.rbox_type2_2)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        view_type?.setTextColor(resources.getColor(R.color.colorListStatus2, null))
                    } else {
                        view_type?.setTextColor(resources.getColor(R.color.colorListStatus2))
                    }
                }
            }
        }
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.header_back -> {
                nactivity?.finish()
            }
            R.id.board_view_golist -> {
                nactivity?.finish()

                if(mydata.check) {
                    when (mydata.type) {
                        Config.BOARD_NOTICE -> {
                            UCargoActivity.showBoard(SubActivity.TYPE.NOTICE)
                        }
                        Config.BOARD_JIBU -> {
                            UCargoActivity.showBoard(SubActivity.TYPE.HH5, 2)
                        }
                        Config.BOARD_HNOTICE -> {
                            UCargoActivity.showBoard(SubActivity.TYPE.HH5, 1)
                        }
                        Config.BOARD_NEWS -> {
                            UCargoActivity.showBoard(SubActivity.TYPE.NEWS)
                        }
                        Config.BOARD_FAQ -> {
                            UCargoActivity.showBoard(SubActivity.TYPE.FAQ)
                        }
                    }
                }
            }
        }
    }
}