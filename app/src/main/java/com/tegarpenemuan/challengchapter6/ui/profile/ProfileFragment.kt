package com.tegarpenemuan.challengchapter6.ui.profile

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.drjacky.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.tegarpenemuan.challengchapter6.Constant
import com.tegarpenemuan.challengchapter6.R
import com.tegarpenemuan.challengchapter6.common.GetInisial.getInitial
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import com.tegarpenemuan.challengchapter6.databinding.FragmentProfileBinding
import com.tegarpenemuan.challengchapter6.datastore.DataStoreManager
import com.tegarpenemuan.challengchapter6.datastore.pref
import com.tegarpenemuan.challengchapter6.network.AuthApiClient
import com.tegarpenemuan.challengchapter6.repository.AuthRepository
import com.tegarpenemuan.challengchapter6.ui.signin.SignInActivity
import com.tegarpenemuan.challengchapter6.ui.signup.SignUpViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val progressDialog: ProgressDialog by lazy { ProgressDialog(requireContext()) }
    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModel.Factory(
            AuthRepository(
                DataStoreManager(requireContext()),
                AuthApiClient.instance,
                MyDatabase.getInstance(requireContext())
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val db = MyDatabase.getInstance(this.requireContext())
        viewModel.getDataUser(requireActivity().pref().getPrefEmail().toString())

        viewModel.onViewLoaded(db)
        viewModel.getId(requireActivity().pref().getPrefId().toString())

        bindViewModel()
        bindView()

        return root
    }

    private fun bindView() {
        binding.btnLogout.setOnClickListener {
            requireContext().pref().clearPref()
            startActivity(Intent(requireContext(),SignInActivity::class.java))
        }
        binding.etEmail.isEnabled = false

        binding.ivProfile.setOnClickListener {
            picImage()
        }
        binding.etUsername.doAfterTextChanged {
            viewModel.onChangeUsername(it.toString())
        }
        binding.etJob.doAfterTextChanged {
            viewModel.onChangeJob(it.toString())
        }
        binding.btnUpdateProfile.setOnClickListener {
            viewModel.onValidate()
        }
        binding.ubahPassword.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setContentView(R.layout.dialog_lupa_password)

            val passwordLama = dialog.findViewById<EditText>(R.id.etPasswordLama)
            val passwordBaru = dialog.findViewById<EditText>(R.id.etPasswordBaru)

            passwordLama.doAfterTextChanged {
                viewModel.onChangePasswordLama(it.toString())
            }
            passwordBaru.doAfterTextChanged {
                viewModel.onChangePasswordBaru(it.toString())
            }

            val btnAdd = dialog.findViewById(R.id.btnGantiPassword) as Button
            btnAdd.setOnClickListener {
                viewModel.GantiPassword()
//                CoroutineScope(Dispatchers.IO).launch {
//                    db?.noteDAO()?.insertNote(
//                        NoteEntity(
//                            id = 0,
//                            date = time.toString(),
//                            titleNote = title.text.toString(),
//                            descNote = note.text.toString()
//                        )
//                    )
//                }

                dialog.dismiss()
                //loadDataDatabase()
                //Toast(applicationContext).showCustomToast("Data Berhasil Ditambahkan", this@HomeActivity)
            }

            dialog.show()
        }

    }

    private fun picImage() {
        ImagePicker.with(requireActivity())
            .crop()
            .maxResultSize(1080, 1080, true)
            .createIntentFromDialog { launcher.launch(it) }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                viewModel.getUriPath(uri)

                Glide.with(this)
                    .load(uri)
                    .circleCrop()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(binding.ivProfile)
            }
        }

    private fun bindViewModel() {
        viewModel.shouldShowUser.observe(viewLifecycleOwner) {
            binding.tvName.text = it.name
            binding.etUsername.setText(it.name)
            binding.etEmail.setText(it.email)
            binding.etJob.setText(it.job)

            if (it.image == null) {
                binding.tvInisial.text = it.name.getInitial()
            } else {
                Glide.with(requireContext())
                    .load(Constant.ImageUrl.IMAGE_URL + it.image)
                    .into(binding.ivProfile)
            }
        }
        viewModel.showResponseError.observe(viewLifecycleOwner) {
            val snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(Color.RED)
            snackbar.show()
        }
        viewModel.showResponseSuccess.observe(viewLifecycleOwner) {
            val snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(Color.BLUE)
            snackbar.show()
        }

        viewModel.shouldShowLoading.observe(viewLifecycleOwner) {
            if (it) {
                progressDialog.setMessage("Loading...")
                progressDialog.show()
            } else {
                progressDialog.hide()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}