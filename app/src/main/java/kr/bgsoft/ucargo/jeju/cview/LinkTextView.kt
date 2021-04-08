package kr.bgsoft.ucargo.jeju.cview

import android.content.Context
import android.text.util.Linkify
import android.util.AttributeSet
import java.util.regex.Pattern

class LinkTextView : androidx.appcompat.widget.AppCompatTextView {
    internal var regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    fun setText(text: String) {
        super.setText(text)
        //App.Logd("NoUnderlineTextView setText")
        val p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        //App.Logd(p.toString())
        Linkify.addLinks(this, p, null)
    }
}