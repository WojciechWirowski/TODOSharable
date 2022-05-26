package com.easv.dt.ww.todosharable

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Telephony
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.easv.dt.ww.todosharable.data.listItem.ListItemRepository
import kotlinx.android.synthetic.main.camera.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Camera : AppCompatActivity(){

    private var uri: Uri? = null
    val TAG = "xyz"

    private var mFile: File? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera)
        btnByFile.setOnClickListener { takeImage() }
        ListItemRepository.initialize(this)
    }

    private fun takeImage() {
        mFile = getOutputMediaFile("CameraFiles")

        if(mFile == null) {
            Toast.makeText(this, "Could not create file", Toast.LENGTH_LONG).show()
            return
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val applicationId = "com.easv.dt.ww.todosharable"
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
            this,
            "${applicationId}.provider",
            mFile!!))

        uri = FileProvider.getUriForFile(
            this,
            "${applicationId}.provider",
            mFile!!)

        fileCallback.launch(intent)
    }

    private val fileCallback = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ activityResult ->
        val mImage = findViewById<ImageView>(R.id.imgView)
        if (activityResult.resultCode == RESULT_OK)
            showImageFromFile(mImage, mFile!!)
        else handleOther(activityResult.resultCode)
    }

    @SuppressLint("SetTextI18n")
    private fun showImageFromFile(imgView: ImageView, f: File) {
        imgView.setImageURI(Uri.fromFile(f))
        imgView.setBackgroundColor(Color.RED)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getOutputMediaFile(folder: String): File? {
        val mediaStorageDir = File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), folder)
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory")
                return null
            }
        }

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val postfix = "jpg"
        val prefix = "IMG"
        return File(mediaStorageDir.path +
                File.separator + prefix +
                "_" + timeStamp + "." + postfix)
    }

    private fun handleOther(resultCode: Int) {
        if (resultCode == RESULT_CANCELED)
            Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show()
    }
}