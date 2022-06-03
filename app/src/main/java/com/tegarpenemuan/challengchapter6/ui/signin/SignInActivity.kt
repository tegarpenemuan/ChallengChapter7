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
import com.tegarpenemuan.challengchapter6.R
import com.tegarpenemuan.challengchapter6.data.api.auth.SignInResponse
import com.tegarpenemuan.challengchapter6.data.api.auth.SignUpResponse
import com.tegarpenemuan.challengchapter6.data.local.UserEntity
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import com.tegarpenemuan.challengchapter6.databinding.ActivitySignInBinding
import com.tegarpenemuan.challengchapter6.datastore.pref
import com.tegarpenemuan.challengchapter6.network.AuthApiClient
import com.tegarpenemuan.challengchapter6.ui.MainActivity
import com.tegarpenemuan.challengchapter6.ui.signup.SignUpActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val progressDialog: ProgressDialog by lazy { ProgressDialog(this) }
    private var db: MyDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MyDatabase.getInstance(this)

        if (applicationContext.pref().getPrefLogin() == false) {
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        AuthApiClient.instance.login(
            email = binding.etEmail.text.toString(),
            password = binding.etPassword.text.toString()
        ).enqueue(object : Callback<SignInResponse> {
            override fun onResponse(
                call: Call<SignInResponse>,
                response: Response<SignInResponse>
            ) {
                if (response.isSuccessful) {
                    progressDialog.hide()

                    applicationContext.pref().setPrefLogin(true)
                    applicationContext.pref().setPrefEmail(response.body()!!.user.email)

                    insertToLocal(
                        UserEntity(
                            id = response.body()!!.user.id.toString(),
                            name = response.body()!!.user.name,
                            job = response.body()!!.user.job,
                            email = response.body()!!.user.email,
                            image = response.body()!!.user.image
                        )
                    )
                } else {
                    progressDialog.hide()
                    val snackbar =
                        Snackbar.make(binding.root, "Request Tidak Failed", Snackbar.LENGTH_LONG)
                    snackbar.view.setBackgroundColor(Color.RED)
                    snackbar.show()
                }
            }

            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                progressDialog.hide()
                val snackbar =
                    Snackbar.make(binding.root, "Request Tidak Failed", Snackbar.LENGTH_LONG)
                snackbar.view.setBackgroundColor(Color.RED)
                snackbar.show()
            }
        })
    }

    private fun insertToLocal(userEntity: UserEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = db?.userDAO()?.insertUser(userEntity)
            withContext(Dispatchers.Main) {
                if (user != 0L) {
                    Toast.makeText(applicationContext, "Login Berhasil",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                    finish()
                } else {
                    val snackbar =
                        Snackbar.make(binding.root, "Login Gagal", Snackbar.LENGTH_LONG)
                    snackbar.view.setBackgroundColor(Color.RED)
                    snackbar.show()
                }
            }
        }

    }
}