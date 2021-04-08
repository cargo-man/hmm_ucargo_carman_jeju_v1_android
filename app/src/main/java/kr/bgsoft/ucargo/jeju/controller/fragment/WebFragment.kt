package kr.bgsoft.ucargo.jeju.controller.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ImageButton
import android.widget.TextView
import kr.bgsoft.ucargo.jeju.App
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.controller.dialog.ProcessDialog
import kr.bgsoft.ucargo.jeju.controller.http.bridge.bgsoftjs
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment

class WebFragment: DefaultFragment(), View.OnClickListener, View.OnKeyListener {

    var nactivity: SubActivity?     = null
    var view_webview: WebView?      = null
    var exType: SubActivity.TYPE?   = null
    var exOrder                     = ""
    var nloading: ProcessDialog?    = null

    private var filePathCallbackNormal: ValueCallback<Uri>?             = null
    private var filePathCallbackLollipop: ValueCallback<Array<Uri>>?    = null
    private val FILECHOOSER_NORMAL_REQ_CODE = 1
    private val FILECHOOSER_LOLLIPOP_REQ_CODE = 2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        nactivity   = activity as SubActivity
        nloading    = ProcessDialog(nactivity)

        exType  = activity!!.intent.getSerializableExtra(Config.EXTRA_TYPE) as SubActivity.TYPE
        exOrder = if(activity!!.intent.getSerializableExtra(Config.EXTRA_ORDER) != null) {
            activity!!.intent.getSerializableExtra(Config.EXTRA_ORDER) as String
        } else {
            ""
        }

        var layout: View?   = null
        var set_url         = ""
        var hp              = App.userInfo.sHP

        /*if(hp.isNullOrBlank()) {
            hp = thisActivity?.thisDeviceInfo?.getNumber()!!
        }*/

        layout = inflater!!.inflate(R.layout.fragment_webview, container, false)

        val view_title      = layout!!.findViewById<TextView>(R.id.header_title)
        val view_back       = layout!!.findViewById<ImageButton>(R.id.header_back)
        view_back.setOnClickListener(this)


        when(exType) {
            SubActivity.TYPE.RULEPAY ->{
                set_url         = resources.getString(R.string.url_rulepay).replace("{host}", App.host)
                view_title.text = resources.getString(R.string.title_mylistrule)
            }
            SubActivity.TYPE.EVENT -> {
                set_url         = resources.getString(R.string.url_event).replace("{host}", App.host)
                view_title.text = resources.getString(R.string.title_event)
            }
            else -> {
                logd("exOrder : " + exOrder)

                val array       = exOrder.split("|")
                set_url         = array[0]
                view_title.text = array[1]
            }
        }

        logd("url : " + set_url)

        nactivity = activity as SubActivity
        view_webview        = layout!!.findViewById(R.id.web_view)

        val ws = view_webview?.getSettings()
        ws?.setJavaScriptEnabled(true)
        //ws.setPluginsEnabled(true);
        ws?.cacheMode = WebSettings.LOAD_NO_CACHE
        ws?.databaseEnabled = true
        val databasePath = nactivity?.applicationContext?.getDir("database", Context.MODE_PRIVATE)?.path
        ws?.setDatabasePath(databasePath)
        val cachePath = nactivity?.applicationContext?.getDir("cache", Context.MODE_PRIVATE)?.path
        ws?.setAppCachePath(cachePath)
        ws?.allowFileAccess = true
        ws?.setAppCacheEnabled(false)
        ws?.domStorageEnabled = true
        ws?.setAppCacheMaxSize((1024 * 1024 * 8).toLong())

        view_webview?.setNetworkAvailable(true)
        view_webview?.isScrollbarFadingEnabled = true
        view_webview?.isVerticalScrollBarEnabled = false
        view_webview?.isHorizontalScrollBarEnabled = false

        view_webview?.webChromeClient = object : WebChromeClient() {

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

        view_webview?.webViewClient = object : WebViewClient() {
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
                    nloading?.show()
                } catch (ex: Exception) {
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

                try {
                    nloading?.stop()

                    if(exType == SubActivity.TYPE.EVENT && (App.userInfo.bzNum.isNullOrBlank() || App.userInfo.bzNum == "_")) {
                        view_webview?.loadUrl("javascript:showButton()")
                    }
                } catch (ex: Exception) {
                }

            }

            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                AlertDialog.Builder(context).setTitle("onReceivedError").setMessage("onReceivedError:::").setCancelable(false)
                        .setNeutralButton(android.R.string.ok) { dialog, whichButton -> nactivity?.finish() }.show()
            }
        }

        if(set_url.isNotBlank()) {
            view_webview?.loadUrl(set_url)
        }

        view_webview?.setOnKeyListener(this)
        view_webview?.addJavascriptInterface(bgsoftjs(nactivity), "bgsoftjs")

        return layout
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        val value = true

        if(keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_UP) {
            if(view_webview?.url == resources.getString(R.string.url_cardjoin)) {
                nactivity?.isFinish = true
                nactivity?.finish()
            } else {
                if (view_webview?.canGoBack()!!) {
                    view_webview?.goBack()
                } else {
                    nactivity?.isFinish = true
                    nactivity?.finish()
                }
            }
        }

        return value
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.header_back -> {
                if(view_webview?.url == resources.getString(R.string.url_cardjoin)) {
                    nactivity?.isFinish = true
                    nactivity?.finish()
                } else {
                    if (view_webview?.canGoBack()!!) {
                        view_webview?.goBack()
                    } else {
                        nactivity?.isFinish = true
                        nactivity?.finish()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == FILECHOOSER_NORMAL_REQ_CODE) {
            if (filePathCallbackNormal == null) return
            val result = if (data == null || resultCode != Activity.RESULT_OK) null else data.data
            filePathCallbackNormal!!.onReceiveValue(result)
            filePathCallbackNormal = null
        } else if (requestCode == FILECHOOSER_LOLLIPOP_REQ_CODE) {
            if (filePathCallbackLollipop == null) return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                filePathCallbackLollipop!!.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data))
            }
            filePathCallbackLollipop = null
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

}