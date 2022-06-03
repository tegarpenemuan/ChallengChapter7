package com.tegarpenemuan.challengchapter6.ui.signup

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import com.github.drjacky.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.tegarpenemuan.challengchapter6.R
import com.tegarpenemuan.challengchapter6.databinding.ActivitySignUpBinding
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import com.tegarpenemuan.challengchapter6.datastore.DataStoreManager
import com.tegarpenemuan.challengchapter6.network.AuthApiClient
import com.tegarpenemuan.challengchapter6.repository.AuthRepository
import com.tegarpenemuan.challengchapter6.ui.signin.SignInActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val progressDialog: ProgressDialog by lazy { ProgressDialog(this) }
    private val viewModel: SignUpViewModel by viewModels {
        SignUpViewModel.FactorySignUp(
            AuthRepository(
                DataStoreManager(applicationContext),
                AuthApiClient.instance,
                MyDatabase.getInstance(applicationContext)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindView()
        bindViewModel()
    }

    private fun bindView() {
        binding.etName.doAfterTextChanged {
            viewModel.onChangeName(it.toString())
        }
        binding.etJob.doAfterTextChanged {
            viewModel.onChangeJob(it.toString())
        }
        binding.etEmail.doAfterTextChanged {
            viewModel.onChangeEmail(it.toString())
        }
        binding.etPassword.doAfterTextChanged {
            viewModel.onChangePassword(it.toString())
        }
        binding.ivLogin.setOnClickListener {
            picImage()
        }
        binding.btnRegister.setOnClickListener {
            viewModel.onValidate()
        }
        binding.ivLogin.setOnClickListener {
            picImage()
        }
    }

    private fun bindViewModel() {
        viewModel.shouldShowLoading.observe(this) {
            if (it) {
                progressDialog.setMessage("Loading...")
                progressDialog.show()
            } else {
                progressDialog.hide()
            }
        }

        viewModel.showResponseSuccess.observe(this) {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
            finish()
        }

        viewModel.showResponseError.observe(this) {
            val snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(Color.RED)
            snackbar.show()
        }
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
                viewModel.getUriPath(uri)

                Glide.with(applicationContext)
                    .load(uri)
                    .circleCrop()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(binding.ivLogin)
            }
        }
}