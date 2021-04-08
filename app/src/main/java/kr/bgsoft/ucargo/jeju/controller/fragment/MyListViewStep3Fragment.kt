package kr.bgsoft.ucargo.jeju.controller.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kr.bgsoft.ucargo.jeju.App
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.controller.conf.Settings
import kr.bgsoft.ucargo.jeju.controller.dialog.AlertDialog
import kr.bgsoft.ucargo.jeju.controller.dialog.ProcessDialog
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment
import kr.bgsoft.ucargo.jeju.data.model.MyCargo
import kr.bgsoft.ucargo.jeju.data.model.Photo
import kr.bgsoft.ucargo.jeju.utils.Etc
import kr.bgsoft.ucargo.jeju.utils.RealPathUtil
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MyListViewStep3Fragment: DefaultFragment(), View.OnClickListener {
    var nactivity: SubActivity?         = null
    var nmydata: MyCargo?               = null
    var nmyimageview: ImageView?        = null
    val TYPE_GALLERY                    = 9090
    val TYPE_PHOTO                      = 9091
    var nimagepath                      = ""
    var nsendcount                      = 0
    var nmobilenum: TextView?           = null
    var arrayData                       = ArrayList<Photo>()
    var naddList                        = ArrayList<Photo>()
    var nupfile: File?                  = null
    var ndialog: ProcessDialog?         = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_mylistview_step3, container, false)

        nactivity       = activity as SubActivity
        nmydata         = MyListViewFragment.mydata

        val view_photo  = layout.findViewById<Button>(R.id.mylistview_photo)
        nmobilenum      = layout.findViewById(R.id.mylistview_gomobile)
        nmyimageview    = layout.findViewById(R.id.mylistview_imageview)

        nmobilenum?.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            when(actionId) {
                EditorInfo.IME_ACTION_DONE ->{
                    Etc.hideKeyboard(nactivity!!)
                }
                else -> {
                    return@OnEditorActionListener false
                }
            }

            true
        })

        view_photo.setOnClickListener(this)
        nmyimageview?.setOnClickListener(this)

        loadData()

        ndialog = ProcessDialog(nactivity)
        ndialog?.setText(resources.getString(R.string.dialog_processing))

        return layout
    }

    fun callGallery() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"

        if (photoPickerIntent.resolveActivity(nactivity?.packageManager!!) != null) {
            startActivityForResult(photoPickerIntent, TYPE_GALLERY)
        }
    }

    fun callPhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(nactivity?.packageManager!!) != null) {

            var photoFile : File? = null

            try {
                photoFile = createImageFile()
            } catch (e: Exception) {

            }

            if (photoFile != null) {
                val photoUri = FileProvider.getUriForFile(nactivity!!, nactivity?.packageName!!, photoFile)

                val resolvedIntentActivities = nactivity?.packageManager?.queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY)

                for (resolvedIntentInfo in resolvedIntentActivities!!) {
                    val packageName = resolvedIntentInfo.activityInfo.packageName
                    nactivity?.grantUriPermission(packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(takePictureIntent, TYPE_PHOTO)
            }
        }
    }

    fun loadData() {
        /*CargoHttp().getReceiptimg(nmydata!!.nOrderNum, Http, object : DefaultHttp.Callback {
            override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                logd(message)
                try {
                    toasts(resources.getString(R.string.toast_error_server))
                } catch (e: Exception) {

                }
            }

            override fun onSuccess(json: Any) {
                val data = json as JSONArray

                if(data.length() > 0) {
                    arrayData = ArrayList<Photo>()

                    var count = data.length()

                    for(i in 0..(count - 1)) {
                        val field = data.getJSONObject(i)
                        arrayData.add(Photo(field.getString("nIdx"), field.getString("sUrl") + field.getString("sFileName"), "", field.getString("sRegdate")))

                        val path = field.getString("sUrl") + field.getString("sFileName")

                        Glide.with(activity!!).load(path).listener(object: RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                logd(e.toString())
                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                nmyimageview?.setImageResource(R.drawable.ico_noimage)
                                return false
                            }

                        }).into(nmyimageview!!)
                    }

                }
            }
        })*/
    }

    fun resetData() {
        if(arrayData.size > 0) {
            for (i in 0..(arrayData.size - 1)) {

                val data = arrayData.get(i)

                /*CargoHttp().setReceiptimgDelete(nmydata!!.nOrderNum, data.nIdx, Http, object : DefaultHttp.Callback {
                    override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                        logd("delete error : " + i.toString())
                    }

                    override fun onSuccess(json: Any) {
                        logd("delete ok : " + i.toString())
                    }
                })*/
            }

            nmyimageview?.setImageResource(R.drawable.ico_noimage)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "TEST_" + timeStamp + "_"
        val storageDir = nactivity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir          /* directory */
        )

        nimagepath = image.absolutePath

        return image
    }

    fun gotoSend() {
        var error   = false
        var message = ""
        nsendcount  = 0

        Etc.hideKeyboard(nactivity!!)

        if(nmobilenum?.text.isNullOrBlank()) {
            error   = true
            message = resources.getString(R.string.toast_mylistview_nomobile)
        }

        if(nupfile == null && arrayData.size <= 0) {
            error   = true
            message = resources.getString(R.string.toast_mylistview_noupdate)
        }

        if(error) {
            toasts(message)
        } else {
            ndialog?.show()

            if(nupfile == null) {
                sendSMS()
            } else {
                /*CargoHttp().setReceiptimg(nmydata!!.nOrderNum, nupfile!!, Http, object : DefaultHttp.Callback {
                    override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                        try {
                            toasts(resources.getString(R.string.toast_error_server))
                        } catch (e: Exception) {

                        }
                        logd(message)
                        ndialog?.stop()
                    }

                    override fun onSuccess(json: Any) {
                        val data2 = json as JSONObject
                        arrayData.add(Photo(data2.getString("nIdx"), data2.getString("sUrl") + data2.getString("sFileName"), ""))
                        sendSMS()
                    }
                })*/
            }
        }

    }

    fun sendSMS() {
        var isSend      = false
        var toimage     = ""

        toimage     = App.host + "p/" + nmydata?.nOrderNum.toString()

        logd("toimage : $toimage")

        if(!toimage.isNullOrBlank()) {
            var sendtext = resources.getString(R.string.mylistview_mmstext)
            var sendtext2 = resources.getString(R.string.mylistview_mmstext2)

            sendtext = sendtext.replace("{loadcity}", nmydata?.sLoadLoc.toString())
            sendtext = sendtext.replace("{downcity}", nmydata?.sDownLoc.toString())
            sendtext = sendtext.replace("{loadday}", nmydata?.sLoadDay.toString().take(16))
            sendtext = sendtext.replace("{name}", App.userInfo?.name.toString())
            sendtext = sendtext.replace("{mobile}", App.userInfo?.sHP.toString())
            sendtext2 = sendtext2.replace("{urls}", toimage)

            val cwnum = Settings.getID(activity!!)

            /*CargoHttp().setReceiptSMS(cwnum, nmobilenum?.text.toString(), App.userInfo?.sHP.toString(), sendtext, Http, object : DefaultHttp.Callback {
                override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                    try {
                        toasts(resources.getString(R.string.toast_error_server))
                    } catch (e: Exception) {

                    }
                    logd(message)
                    ndialog?.stop()
                }

                override fun onSuccess(json: Any) {
                    ndialog?.stop()

                    CargoHttp().setReceiptSMS(cwnum, nmobilenum?.text.toString(), App.userInfo?.sHP.toString(), sendtext2, Http, object : DefaultHttp.Callback {
                        override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                            try {
                                toasts(resources.getString(R.string.toast_error_server))
                            } catch (e: Exception) {

                            }
                            logd(message)
                            ndialog?.stop()
                        }

                        override fun onSuccess(json: Any) {
                            ndialog?.stop()
                            toasts(resources.getString(R.string.toast_mylistview_sendok))
                        }
                    })
                }
            })*/

            /**
             * SMS SEND CHANGE
             *
            val smsManager = SmsManager.getDefault()
            val parts = smsManager.divideMessage(sendtext)
            smsManager.sendMultipartTextMessage(nmobilenum?.text.toString(),
            null, parts, null, null)


            Handler().postDelayed({
            ndialog?.stop()
            toasts(resources.getString(R.string.toast_mylistview_sendok))
            }, 1000)
             */
        } else {
            toasts(resources.getString(R.string.toast_mylistview_noupdate))
        }
    }

    override fun onDestroy() {

        if(naddList.size > 0) {
            for(i in 0..(naddList.size - 1)) {
                val data = naddList[i]
                val file = File(data.sPicture)
                if(file != null && file.exists()) {
                    file.delete()
                }
            }
        }

        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            TYPE_GALLERY -> {
                if(resultCode == Activity.RESULT_OK) {
                    try {
                        val url = data?.data as Uri
                        val file = File(RealPathUtil.getRealPath(nactivity!!, url))
                        nupfile = file

                        Glide.with(this).load(url).listener(object: RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                logd(e.toString())
                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                nmyimageview?.setImageResource(R.drawable.ico_noimage)
                                return false
                            }

                        }).into(nmyimageview!!)

                    } catch (e: Exception) {
                        toasts(resources.getString(R.string.toast_getnoimage))
                    }
                } else {
                    toasts(resources.getString(R.string.toast_getnoimage))
                }
            }
            TYPE_PHOTO -> {
                if(resultCode == Activity.RESULT_OK) {
                    val file = File(nimagepath)

                    naddList.add(Photo("", nimagepath, "", ""))
                    nupfile = file

                    Glide.with(this).load(file).listener(object: RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            logd(e.toString())
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            nmyimageview?.setImageResource(R.drawable.ico_noimage)
                            return false
                        }

                    }).into(nmyimageview!!)

                } else {
                    toasts(resources.getString(R.string.toast_getnoimage))
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.mylistview_photo -> {
                callPhoto()
                Etc.hideKeyboard(nactivity!!)
            }
            R.id.mylistview_imageview -> {
                if(arrayData.size <= 0) {
                    callGallery()
                } else {
                    val dialog = AlertDialog(nactivity, AlertDialog.TYPE.YESNO, resources.getString(R.string.dialog_photoreselet_title), resources.getString(R.string.dialog_photoreselet_info), object : AlertDialog.Callback{
                        override fun onNo() {

                        }

                        override fun onYes() {
                            callGallery()
                        }
                    })
                    dialog.show()
                }
            }
        }
    }
}