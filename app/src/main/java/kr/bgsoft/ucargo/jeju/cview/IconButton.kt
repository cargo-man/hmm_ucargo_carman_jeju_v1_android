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

class IconButton: RelativeLayout {

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
        val view = lif.inflate(R.layout.button_icon, this, false)
        addView(view)

        nBg     = findViewById(R.id.btnicon_bg)
        nText   = findViewById(R.id.btnicon_text)
        nIcon   = findViewById(R.id.btnicon_img)
    }

    fun getAttr(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconButton)
        setAttr(typedArray)
    }

    fun getAttr(attrs: AttributeSet?, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconButton, defStyle, 0)
        setAttr(typedArray)
    }

    fun setAttr(typedArray: TypedArray) {
        val bg = typedArray.getResourceId(R.styleable.IconButton_bg, R.drawable.box_type3_4)

        nBg?.setBackgroundResource(bg)
        val icon = typedArray.getResourceId(R.styleable.IconButton_abicon, R.drawable.ico_arrow)
        nIcon?.setImageResource(icon)

        val txtcolorid = typedArray.getResourceId(R.styleable.IconButton_textColor, R.color.colorUser6)
        val txtcolor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(txtcolorid, null)
        } else {
            resources.getColor(txtcolorid)
        }
        nText?.setTextColor(txtcolor)

        var txtvalue = typedArray.getNonResourceString(R.styleable.IconButton_text)
        if(txtvalue.isNullOrBlank()) {
            var txtvalueid = typedArray.getResourceId(R.styleable.IconButton_textres, 0)

            if(txtvalueid > 0) {
                txtvalue = resources.getString(txtvalueid)
            }
        }

        nText?.text = txtvalue

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

}