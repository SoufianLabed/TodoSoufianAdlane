package com.soufian.todosoufianlabed.userinfo

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.soufian.todosoufianlabed.R
import com.soufian.todosoufianlabed.network.Api
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*
import java.util.jar.Manifest
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.soufian.todosoufianlabed.BuildConfig
import com.soufian.todosoufianlabed.MainActivity
import com.soufian.todosoufianlabed.TaskListViewModel
import com.soufian.todosoufianlabed.network.TasksRepository
import com.soufian.todosoufianlabed.network.UserInfo
import com.soufian.todosoufianlabed.task.TaskActivity
import com.soufian.todosoufianlabed.task.TaskActivity.Companion.ADD_TASK_REQUEST_CODE
import com.soufian.todosoufianlabed.tasklist.TaskListFragment
import kotlinx.coroutines.launch

class UserInfoActivity : AppCompatActivity() {

    private val viewModel: UserInfoViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userinfo)
        val picture_button  =  findViewById(R.id.take_picture_button) as Button

        val upload_image_button  =  findViewById(R.id.upload_image_button) as Button






        val email = findViewById(R.id.email) as EditText
        val lastname = findViewById(R.id.lastname) as EditText
        val firstname = findViewById(R.id.firstname) as EditText
        val editUser = findViewById(R.id.editUser) as ImageButton



        viewModel.userInfo.observe(this) { userInfo ->
            // utliser la liste
            email.setText(userInfo.email)
            lastname.setText(userInfo.lastName)
            firstname.setText(userInfo.firstName)

        }



        editUser.setOnClickListener({

            val user = UserInfo(email.text.toString(),firstname.text.toString(),lastname.text.toString(),)
            Log.d("email,firstname",email.text.toString())
            viewModel.editUser(user)

            val intent = Intent(this, MainActivity::class.java)
            startActivityForResult(intent,0)

        })



        picture_button.setOnClickListener({
            askCameraPermissionAndOpenCamera()
        })

        upload_image_button.setOnClickListener({
            pickInGallery.launch("image/*")
        })

        email.setText(viewModel.userInfo.value?.email)

        viewModel.loadinfo()
    }


    override fun onResume() {
        super.onResume()



        viewModel.loadinfo()




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

/* V1
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        val tmpFile = File.createTempFile("avatar", "jpeg")
        tmpFile.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        handleImage(tmpFile.toUri())
    }*/









        private fun convert(uri: Uri) =
                MultipartBody.Part.createFormData(
                        name = "avatar",
                        filename = "temp.jpeg",
                        body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
                )

        private fun handleImage(uri: Uri) {
            lifecycleScope.launch {
                val userService = Api.userService.updateAvatar(convert(uri))
            }


        }

    // register
    private val pickInGallery =
            registerForActivityResult(ActivityResultContracts.GetContent()) {

                lifecycleScope.launch {
                    val userService = Api.userService.updateAvatar(convert(it))
                }
            }




    private val photoUri by lazy {
        FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID +".fileprovider",
                File.createTempFile("avatar", ".jpeg", externalCacheDir)

        )
    }


    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) handleImage(photoUri)
        else Toast.makeText(this, "Erreur ! 😢", Toast.LENGTH_LONG).show()
    }



    // use
    private fun openCamera() = takePicture.launch(photoUri)

    // V1 private fun openCamera() = takePicture.launch()


}
