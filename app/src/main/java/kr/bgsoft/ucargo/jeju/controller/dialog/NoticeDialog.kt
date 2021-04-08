package kr.bgsoft.ucargo.jeju.controller.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.cview.DefaultDialog

class NoticeDialog  (context: Context?, type: TYPE, contents: String, daycut: Boolean, callback: Callback?) : DefaultDialog(context), View.OnClickListener {

    val ncallback                       = callback
    val ncontents                       = contents
    val ntype                           = type
    val ndaycut                        = daycut
    var nwebview: WebView?              = null
    var ntextview: TextView?            = null
    var nimgview: ImageView?            = null
    // var nanimation: AnimationDrawable?  = null
    var nloadingbox: RelativeLayout?    = null

    enum class TYPE {
        TEXT, WEB, IMAGE, NOTSHOW
    }

    interface Callback {
        fun onNoShow()
        fun onClose()
        fun onClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.dialog_notice)

        nwebview        = findViewById(R.id.noticealert_webview)
        ntextview       = findViewById(R.id.noticealert_text)
        nimgview        = findViewById(R.id.noticealert_image)
        val view_close  = findViewById<Button>(R.id.noticealert_close)
        val view_wclose = findViewById<Button>(R.id.noticealert_noshow)
        val view_img    = findViewById<ImageView>(R.id.loading_image)
        val view_goto   = findViewById<Button>(R.id.noticealert_goto)
        nloadingbox     = findViewById(R.id.loading_box)
        //nanimation      = view_img.background as AnimationDrawable

        view_close.setOnClickListener(this)
        view_wclose.setOnClickListener(this)
        view_goto.setOnClickListener(this)

        when(ntype) {
            TYPE.TEXT -> {
                ntextview?.visibility   = View.VISIBLE
                nwebview?.visibility    = View.GONE
                nimgview?.visibility    = View.GONE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ntextview?.text = Html.fromHtml(ncontents.toString(), Html.FROM_HTML_MODE_COMPACT)
                } else {
                    ntextview?.text = Html.fromHtml(ncontents.toString())
                }
            }
            TYPE.WEB, TYPE.NOTSHOW -> {
                ntextview?.visibility   = View.GONE
                nwebview?.visibility    = View.VISIBLE
                nimgview?.visibility    = View.GONE

                val ws = nwebview?.getSettings()
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

                nwebview?.setNetworkAvailable(true)
                nwebview?.isScrollbarFadingEnabled = true
                nwebview?.isVerticalScrollBarEnabled = false
                nwebview?.isHorizontalScrollBarEnabled = false

                nwebview?.webChromeClient = object : WebChromeClient() {

                }

                nwebview?.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                        logd("tmddbs:: " + url)

                        var split = if (url.contains("|")) {
                            "|"
                        } else {
                            "%7C"
                        }
                        val url1 = url.split(split)
                        try {
                            return if (url1.size > 1) {
                                if (url1[1] == "1") {
                                    view.loadUrl(url1[0])
                                    false
                                } else {
                                    view.context.startActivity(
                                            Intent(Intent.ACTION_VIEW, Uri.parse(url1[0]))
                                    )
                                    true
                                }
                            } else {
                                view.loadUrl(url)
                                false
                            }

                        } catch (e: Exception) {
                            logd("no | can't split")
                            return false
                        }
                        /* try {
                             logd(url.toString())
                             view.loadUrl(url)
                         } catch (ex: Exception) {
                         }

                         return true*/
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

                nwebview?.settings!!.builtInZoomControls = true
                nwebview?.settings!!.setSupportZoom(true)

                nwebview?.settings!!.useWideViewPort = true
                nwebview?.settings!!.loadWithOverviewMode = true

                if (ncontents.isNotBlank()) {
                    nwebview?.loadUrl(ncontents)
                }
            }
            TYPE.IMAGE -> {
                ntextview?.visibility   = View.GONE
                nwebview?.visibility    = View.GONE
                nimgview?.visibility    = View.VISIBLE

                loading(true)

                Glide.with(context).load(ncontents).listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        loading(false)
                        dismiss()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        loading(false)
                        return false
                    }

                }).into(nimgview!!)
            }
        }

        /* val param = view_close.layoutParams as LinearLayout.LayoutParams

         if(ndaycut) {
             view_wclose.visibility = View.VISIBLE
             param.weight            = 4f
         } else {
             view_wclose.visibility = View.GONE
             param.weight            = 10f
         }

         view_close.layoutParams = param*/
    }

    fun loading(isShow : Boolean) {
        if(isShow) {
            // nanimation?.start()
            nloadingbox?.visibility = View.VISIBLE
        } else {
            //  nanimation?.stop()
            nloadingbox?.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        val id = v?.id

        when(id) {
            R.id.noticealert_close -> {
                ncallback?.onClose()
                dismiss()
            }
            R.id.noticealert_noshow -> {
                ncallback?.onNoShow()
                dismiss()
            }
            R.id.noticealert_goto -> {
                ncallback?.onClick()
            }
        }
    }

    override fun dismiss() {
        loading(false)
        super.dismiss()
    }

}