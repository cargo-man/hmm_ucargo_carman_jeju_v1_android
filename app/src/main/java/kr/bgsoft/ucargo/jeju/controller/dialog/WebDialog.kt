package kr.bgsoft.ucargo.jeju.controller.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.cview.DefaultDialog

class WebDialog (context: Context?, type: TYPE, title: String, contents: String = "", url: String = "", callback: Callback?) : DefaultDialog(context), View.OnClickListener {

    val ntitle                          = title
    val ncallback                       = callback
    val ncontents                       = contents
    val nurl                            = url
    val ntype                           = type
    var view_webview: WebView?          = null
    var nanimation: AnimationDrawable?  = null
    var nloadingbox: RelativeLayout?    = null

    enum class TYPE {
        CLOSE, JOIN
    }

    interface Callback {
        fun onNo()
        fun onYes()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.dialog_web)

        val view_title  = findViewById<TextView>(R.id.webalert_title)
        view_webview    = findViewById(R.id.webalert_webview)
        val view_ntext  = findViewById<TextView>(R.id.webalert_text)
        val view_no     = findViewById<Button>(R.id.webalert_no)
        val view_yes    = findViewById<Button>(R.id.webalert_yes)
        val view_img    = findViewById<ImageView>(R.id.loading_image)
        nloadingbox     = findViewById(R.id.loading_box)
        //nanimation      = view_img.background as AnimationDrawable

        logd("ntitle :: $ntitle / nurl :: $nurl / ncontents :: $ncontents")

        view_title?.text = ntitle
        view_no.setOnClickListener(this)
        view_yes.setOnClickListener(this)

        when(ntype) {
            TYPE.CLOSE -> {
                view_no.visibility = View.GONE
                val params = TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 5f)
                view_yes.setBackgroundResource(R.drawable.cbutton_type6f)
                view_yes.layoutParams = params
                view_yes.text = context?.resources?.getString(R.string.alert_ok)
            }
            TYPE.JOIN -> {
                view_no.text = context?.resources?.getString(R.string.alert_close)
                view_yes.text = context?.resources?.getString(R.string.alert_agree)
            }
        }

        if(!nurl.isNullOrBlank()) {
            view_ntext.visibility       = View.GONE
            view_webview?.visibility    = View.VISIBLE

            val ws = view_webview?.getSettings()
            ws?.setJavaScriptEnabled(true)
            //ws.setPluginsEnabled(true);
            ws?.cacheMode = WebSettings.LOAD_NO_CACHE
            ws?.databaseEnabled = true
            val databasePath = context?.applicationContext?.getDir("database", Context.MODE_PRIVATE)?.path
            ws?.setDatabasePath(databasePath)
            val cachePath = context?.applicationContext?.getDir("cache", Context.MODE_PRIVATE)?.path
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
                        loading(true)
                    } catch (ex: Exception) {
                    }
                }

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)

                    try {
                        loading(false)
                    } catch (ex: Exception) {
                    }

                }

                override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                    AlertDialog.Builder(context).setTitle("onReceivedError").setMessage("onReceivedError:::").setCancelable(false)
                            .setNeutralButton(android.R.string.ok) { dialog, whichButton -> dismiss() }.show()
                }
            }

            if (nurl.isNotBlank()) {
                view_webview?.loadUrl(nurl)
            }
        } else {
            view_ntext.visibility       = View.VISIBLE
            view_webview?.visibility    = View.GONE

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view_ntext.text = Html.fromHtml(ncontents, Html.FROM_HTML_MODE_COMPACT)
            } else {
                view_ntext.text = Html.fromHtml(ncontents)
            }
        }
    }

    fun loading(isShow : Boolean) {
        if(isShow) {
            if(nanimation != null) {
                nanimation?.start()
            }
            nloadingbox?.visibility = View.VISIBLE
        } else {
            if(nanimation != null) {
                nanimation?.stop()
            }
            nloadingbox?.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        val id = v?.id

        when(id) {
            R.id.webalert_yes -> {
                ncallback?.onYes()
                dismiss()
            }
            R.id.webalert_no -> {
                ncallback?.onNo()
                dismiss()
            }
        }
    }

    override fun dismiss() {
        loading(false)
        super.dismiss()
    }

}