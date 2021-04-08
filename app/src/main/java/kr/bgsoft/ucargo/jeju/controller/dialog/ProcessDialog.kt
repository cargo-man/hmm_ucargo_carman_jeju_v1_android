package kr.bgsoft.ucargo.jeju.controller.dialog

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.cview.DefaultDialog

class ProcessDialog (context: Context?) : DefaultDialog(context) {

    var isFinish                        = false
    var nanimation: AnimationDrawable?  = null
    var ntext                           = ""
    var ntextview: TextView?            = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.dialog_loading)

        isFinish = false

        val view_img    = findViewById<ImageView>(R.id.loading_image)
        ntextview       = findViewById(R.id.loading_text)

        //nanimation      = view_img.background as AnimationDrawable
    }

    override fun show() {
        super.show()

        if(!ntext.isNullOrBlank()) {
            ntextview?.text = ntext
        }

        if(nanimation != null) {
            nanimation?.start()
        }
    }

    override fun dismiss() {
        if (isFinish) {
            if(nanimation != null) {
                nanimation?.stop()
            }
            super.dismiss()
        }
    }

    fun setText(title: String) {
        ntext = title
    }

    fun stop() {
        isFinish = true
        dismiss()
    }
}