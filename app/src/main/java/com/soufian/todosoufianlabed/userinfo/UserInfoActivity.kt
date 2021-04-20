package com.soufian.todosoufianlabed.userinfo

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.soufian.todosoufianlabed.R
import com.soufian.todosoufianlabed.network.Api
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*
import java.util.jar.Manifest
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class UserInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userinfo)
        val picture_button  =  findViewById(R.id.take_picture_button) as Button

        picture_button.setOnClickListener({

            askCameraPermissionAndOpenCamera()

        })



    }




    private val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) openCamera()
                else showExplanationDialog()
            }

    private fun requestCameraPermission() =
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)

    private fun askCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> openCamera()
            shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> showExplanationDialog()
            else -> requestCameraPermission()
        }
    }

    private fun showExplanationDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("On a besoin de la caméra sivouplé ! 🥺")
            setPositiveButton("Bon, ok") { _, _ ->
                requestCameraPermission()
            }
            setCancelable(true)
            show()
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        val tmpFile = File.createTempFile("avatar", "jpeg")
        tmpFile.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        handleImage(tmpFile.toUri())
    }

    private fun convert(uri: Uri) =
            MultipartBody.Part.createFormData(
                    name = "avatar",
                    filename = "temp.jpeg",
                    body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
            )

    private fun handleImage(uri: Uri) {
        lifecycleScope.launch{
            val userService = Api.userService.updateAvatar(convert(uri))
        }



    }

    // use
    private fun openCamera() = takePicture.launch()

}