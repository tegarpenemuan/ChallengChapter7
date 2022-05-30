package com.tegarpenemuan.challengchapter6.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import com.tegarpenemuan.challengchapter6.databinding.FragmentProfileBinding
import com.tegarpenemuan.challengchapter6.datastore.pref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var db: MyDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        db = MyDatabase.getInstance(this.requireContext())
       getUser(requireActivity().pref().getPrefEmail().toString())

        binding.btnLogout.setOnClickListener {
            requireContext().pref().clearPref()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getUser(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = db?.userDAO()?.getUsername(email = email)
            withContext(Dispatchers.Main) {
                if (user !== null ) {
                    val username = user.name
                    val email = user.email
                    val job = user.job
                    val image =  user.image

                    binding.tvName.text = username
                    binding.etUsername.setText(username)
                    binding.etEmail.setText(email)
                    binding.etJob.setText(job)

                    Glide.with(requireContext())
                        .load("https://tegarpenemuan.xyz/storage/user/"+image)
                        .into(binding.ivProfile)
                } else {
                    binding.tvName.text = "Anonymous"
                }
            }
        }
    }
}