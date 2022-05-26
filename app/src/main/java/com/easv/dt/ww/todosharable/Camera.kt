package com.easv.dt.ww.todosharable

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.camera.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Camera : AppCompatActivity(){

    val TAG = "xyz"
    private var mFile: File? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera)
        //set on take image button listener
        btnByFile.setOnClickListener { takeImage() }
    }

    //runs after pressing button
    //creates output file and puts it into phone storage
    //opens camera MediaStore.ACTION_IMAGE_CAPTURE to able user to take photo
    //overrides created file with made photo
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

        //for future usage we can use this uri to display our image in app

        val uri = FileProvider.getUriForFile(
            this,
            "${applicationId}.provider",
            mFile!!)

        fileCallback.launch(intent)
    }

    //starts activity and expect to receive result code RESULT_OK setting imgView image for created file
    private val fileCallback = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ activityResult ->
        val mImage = findViewById<ImageView>(R.id.imgView)
        if (activityResult.resultCode == RESULT_OK)
            showImageFromFile(mImage, mFile!!)
        else handleOther(activityResult.resultCode)
    }

    //method for displaying image
    @SuppressLint("SetTextI18n")
    private fun showImageFromFile(imgView: ImageView, f: File) {
        imgView.setImageURI(Uri.fromFile(f))
        imgView.setBackgroundColor(Color.RED)
    }

    //getting created file
    //if couldn't create file returns failing log
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

    //user information to show toast after canceling taking photo or error
    private fun handleOther(resultCode: Int) {
        if (resultCode == RESULT_CANCELED)
            Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show()
    }
}