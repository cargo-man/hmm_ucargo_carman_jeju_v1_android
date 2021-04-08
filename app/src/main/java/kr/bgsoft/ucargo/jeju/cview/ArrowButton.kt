package kr.bgsoft.ucargo.jeju.cview

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import kr.bgsoft.ucargo.jeju.R

class ArrowButton : RelativeLayout {
    var nBg: RelativeLayout?    = null
    var nText: TextView?        = null
    var nIcon: ImageView?       = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
        getAttr(attrs, defStyleAttr)
    }
    fun initView() {
        val lif = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = lif.inflate(R.layout.button_arrow, this, false)
        addView(view)

        nBg     = findViewById(R.id.btnarr_bg)
        nText   = findViewById(R.id.btnarr_text)
        nIcon   = findViewById(R.id.btnarr_img)
    }

    fun getAttr(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArrowButton)
        setAttr(typedArray)
    }

    fun getAttr(attrs: AttributeSet?, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArrowButton, defStyle, 0)
        setAttr(typedArray)
    }

    fun setAttr(typedArray: TypedArray) {
        val bg = typedArray.getResourceId(R.styleable.ArrowButton_bg, R.drawable.box_type3_4)
        nBg?.setBackgroundResource(bg)
        val icon = typedArray.getResourceId(R.styleable.ArrowButton_abicon, R.drawable.ico_arrow)
        nIcon?.setImageResource(icon)

        val txtcolorid = typedArray.getResourceId(R.styleable.ArrowButton_textColor, R.color.colorUser6)
        val txtcolor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(txtcolorid, null)
        } else {
            resources.getColor(txtcolorid)
        }
        nText?.setTextColor(txtcolor)

        var txtvalue = typedArray.getNonResourceString(R.styleable.ArrowButton_text)
        if(txtvalue.isNullOrBlank()) {
            var txtvalueid = typedArray.getResourceId(R.styleable.ArrowButton_textres, 0)
            if(txtvalueid > 0) {
                txtvalue = resources.getString(txtvalueid)
            }
        }
        nText?.text = txtvalue

        val hintcolorid = typedArray.getResourceId(R.styleable.ArrowButton_hintColor, R.color.colorUser11)
        val hintcolor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(hintcolorid, null)
        } else {
            resources.getColor(hintcolorid)
        }
        nText?.setHintTextColor(hintcolor)

        var hintvalue = typedArray.getNonResourceString(R.styleable.ArrowButton_hint)
        if(txtvalue.isNullOrBlank()) {
            var txtvalueid2 = typedArray.getResourceId(R.styleable.ArrowButton_hintres, 0)
            if(txtvalueid2 > 0) {
                hintvalue = resources.getString(txtvalueid2)
            }
        }
        nText?.hint = hintvalue

        typedArray.recycle()
    }

    fun setBg(resId: Int) {
        nBg?.setBackgroundResource(resId)
    }

    fun setIcon(resId: Int) {
        nIcon?.setImageResource(resId)
    }

    fun setTextColor(resId: Int) {
        val txtcolor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(resId, null)
        } else {
            resources.getColor(resId)
        }
        nText?.setTextColor(txtcolor)
    }

    fun setText(value: String) {
        nText?.text = value
    }

    fun setText(resId: Int) {
        nText?.text = resources.getString(resId)
    }

    fun setHintColor(resId: Int) {
        val txtcolor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(resId, null)
        } else {
            resources.getColor(resId)
        }
        nText?.setHintTextColor(txtcolor)
    }

    fun setHinttText(value: String) {
        nText?.text = value
    }

    fun setHintText(resId: Int) {
        nText?.text = resources.getString(resId)
    }

    fun getText() : String? {
        return nText?.text.toString()
    }

}