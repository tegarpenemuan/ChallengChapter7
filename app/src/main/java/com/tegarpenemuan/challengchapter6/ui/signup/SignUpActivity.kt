package com.tegarpenemuan.challengchapter6.ui.signup

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.github.drjacky.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.tegarpenemuan.challengchapter6.databinding.ActivitySignUpBinding
import com.tegarpenemuan.challengchapter6.common.ConvertToMultipart.toMultipartBody
import com.tegarpenemuan.challengchapter6.data.api.auth.SignUpResponse
import com.tegarpenemuan.challengchapter6.network.AuthApiClient
import com.tegarpenemuan.challengchapter6.ui.signin.SignInActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private var fileImage: File? = null
    private val progressDialog: ProgressDialog by lazy { ProgressDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivLogin.setOnClickListener {
            picImage()
        }

        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    private fun register() {
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        val file = fileImage.toMultipartBody("image")
        val name = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            binding.etName.text.toString()
        )
        val email = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            binding.etEmail.text.toString()
        )
        val job = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            binding.etJob.text.toString()
        )
        val password = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            binding.etPassword.text.toString()
        )

        AuthApiClient.instance.registrasiUpload(
            name = name,
            email = email,
            job = job,
            password = password,
            file
        ).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                if (response.isSuccessful) {
                    progressDialog.hide()
                    Toast.makeText(applicationContext, "Register Berhasil", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                    finish()
                } else {
                    progressDialog.hide()
                    val snackbar =
                        Snackbar.make(binding.root, "Request Tidak Failed", Snackbar.LENGTH_LONG)
                    snackbar.view.setBackgroundColor(Color.RED)
                    snackbar.show()
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                progressDialog.hide()
                val snackbar = Snackbar.make(binding.root, t.toString(), Snackbar.LENGTH_LONG)
                snackbar.view.setBackgroundColor(Color.RED)
                snackbar.show()
            }

        })
    }

    private fun picImage() {
        ImagePicker.with(this)
            .crop()
            .maxResultSize(1080, 1080, true)
            .createIntentFromDialog { launcher.launch(it) }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                fileImage = File(uri.path ?: "")

                Glide.with(applicationContext)
                    .load(uri)
                    .into(binding.ivLogin)
            }
        }
}