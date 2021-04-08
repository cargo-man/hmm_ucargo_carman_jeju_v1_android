package kr.bgsoft.ucargo.jeju.controller.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.controller.dialog.ProcessDialog
import kr.bgsoft.ucargo.jeju.controller.http.bridge.bgsoftjs
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment

class GamengFragment: DefaultFragment() {

    var nactivity: SubActivity? = null
    var view_webview: WebView? = null
    var exType: SubActivity.TYPE? = null
    var exData = ""
    var nloading: ProcessDialog? = null

    private var filePathCallbackNormal: ValueCallback<Uri>? = null
    private var filePathCallbackLollipop: ValueCallback<Array<Uri>>? = null
    private val FILECHOOSER_NORMAL_REQ_CODE = 1
    private val FILECHOOSER_LOLLIPOP_REQ_CODE = 2

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        nactivity = activity as SubActivity
        nloading = ProcessDialog(nactivity)

        var layout: View? = null
        val set_url = when (MyListViewFragment.mydata?.sOwnerNum) {
            "skolo" -> {
                "http://samsung.15880424.net/Approval/Approval_App.aspx?ktc_ordernum=" + MyListViewFragment.mydata?.nOrderNum
            }
            "shinhan" -> {
                "http://ktc.mycargoman.co.kr/shinhan.aspx?ordernum=" + MyListViewFragment.mydata?.nOrderNum
            }
            else -> {
                "https://korea.15880424.net/hanjin/default.aspx?ordernum=" + MyListViewFragment.mydata?.nOrderNum
            }
        }

        layout = inflater.inflate(R.layout.fragment_webviewfull, container, false)

        view_webview = layout.findViewById(R.id.web_view)

        val ws = view_webview?.settings
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
                if (url.toString().indexOf(" http://ktcmap.15880424.com") > -1) {
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

                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        return false
                    } else if (url.startsWith("tel:")) {
                        val tel = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                        startActivity(tel)
                    } else if (url.startsWith("mailto:")) {
                        val body = "Enter your Question, Enquiry or Feedback below:\n\n"
                        val mail = Intent(Intent.ACTION_SEND)
                        mail.type = "application/octet-stream"
                        mail.putExtra(Intent.EXTRA_EMAIL, arrayOf("email address"))
                        mail.putExtra(Intent.EXTRA_SUBJECT, "Subject")
                        mail.putExtra(Intent.EXTRA_TEXT, body)
                        startActivity(mail)
                    } else {
                        view.loadUrl(url)
                    }
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

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    CookieSyncManager.getInstance().sync()
                } else {
                    CookieManager.getInstance().flush()
                }

                try {
                    nloading?.stop()
                } catch (ex: Exception) {

                }

            }

            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                AlertDialog.Builder(context).setTitle("onReceivedError").setMessage("onReceivedError:::").setCancelable(false)
                        .setNeutralButton(android.R.string.ok) { dialog, whichButton -> nactivity?.finish() }.show()
            }
        }

        if (set_url.isNotBlank()) {
            if (exData.isNullOrBlank()) {
                view_webview?.loadUrl(set_url)
            } else {
                view_webview?.postUrl(set_url, exData.toByteArray())
            }
        }

        view_webview?.addJavascriptInterface(bgsoftjs(nactivity), "bgsoftjs")

        return layout
    }
}