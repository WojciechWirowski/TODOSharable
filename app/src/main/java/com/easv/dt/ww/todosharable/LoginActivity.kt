package com.easv.dt.ww.todosharable

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import com.easv.dt.ww.todosharable.data.login.BEUser
import com.easv.dt.ww.todosharable.data.login.LoginRepo
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var loggedInUser = BEUser(0,"", "")

    private var username = ""
    private var password = ""
    private val PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener{login()}
        checkPermissions()
    }

    private fun login(){

        val repo = LoginRepo()

        username = etUsername.text.toString()
        password = etPassword.text.toString()

        if(repo.login(username, password) == null){
            val exception = "Couldn't find user with this name or it has different password"
            tvLoginMessage.text = exception
        }else {
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("id", loggedInUser.id)
            i.putExtra("username", loggedInUser.username)
            startActivity(i)
        }
    }

    //Only Permissions and Error Messages

    /**
     * Diverse
     */

    //Checks if the app has the required permissions, and prompts the user with the ones missing.
    private fun checkPermissions() {
        val permissions = mutableListOf<String>()
        if ( ! isGranted(Manifest.permission.SEND_SMS) ) permissions.add(Manifest.permission.SEND_SMS)
        if ( ! isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) ) permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if ( ! isGranted(Manifest.permission.CAMERA) ) permissions.add(Manifest.permission.CAMERA)
        if (permissions.size > 0)
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var cameraGranted = true
        var fileGranted = true
        var smsGranted = true
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.CAMERA && grantResults[i] == PackageManager.PERMISSION_DENIED)
                    cameraGranted = false
                if (permissions[i] == Manifest.permission.WRITE_EXTERNAL_STORAGE && grantResults[i] == PackageManager.PERMISSION_DENIED)
                    fileGranted = false
                if (permissions[i] == Manifest.permission.SEND_SMS && grantResults[i] == PackageManager.PERMISSION_DENIED)
                    smsGranted = false
            }
        }
        if (!cameraGranted) {
            val btnLogin = findViewById<Button>(R.id.btnLogin)
            btnLogin.isEnabled = false
        }
        if (!fileGranted) {
            val btnLogin = findViewById<Button>(R.id.btnLogin)
            btnLogin.isEnabled = false
        }
        if (!smsGranted) {
            val btnLogin = findViewById<Button>(R.id.btnLogin)
            btnLogin.isEnabled = false
        }
    }

    private fun isGranted(permission: String): Boolean =
        ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}


