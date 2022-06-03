package com.tegarpenemuan.challengchapter6.ui.signin

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.snackbar.Snackbar
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import com.tegarpenemuan.challengchapter6.databinding.ActivitySignInBinding
import com.tegarpenemuan.challengchapter6.datastore.DataStoreManager
import com.tegarpenemuan.challengchapter6.datastore.pref
import com.tegarpenemuan.challengchapter6.network.AuthApiClient
import com.tegarpenemuan.challengchapter6.repository.AuthRepository
import com.tegarpenemuan.challengchapter6.ui.MainActivity
import com.tegarpenemuan.challengchapter6.ui.signup.SignUpActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val progressDialog: ProgressDialog by lazy { ProgressDialog(this) }
    private val viewModel: SignInViewModel by viewModels {
        SignInViewModel.Factory(
            AuthRepository(
                DataStoreManager(applicationContext),
                AuthApiClient.instance,
                MyDatabase.getInstance(applicationContext)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (applicationContext.pref().getPrefLogin() == true) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        bindView()
        bindViewModel()
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

        viewModel.shouldShowSuccess.observe(this) {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
            finish()
        }

        viewModel.shouldShowError.observe(this) {
            val snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(Color.RED)
            snackbar.show()
        }
    }

    private fun bindView() {
        binding.etEmail.doAfterTextChanged {
            viewModel.onChangeEmail(it.toString())
        }

        binding.etPassword.doAfterTextChanged {
            viewModel.onChangePassword(it.toString())
        }

        binding.btnLogin.setOnClickListener {
            viewModel.onClickSignIn()
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
        }
    }
}