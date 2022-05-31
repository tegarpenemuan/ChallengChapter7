package com.tegarpenemuan.challengchapter6.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.tegarpenemuan.challengchapter6.common.GetInisial.getInitial
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import com.tegarpenemuan.challengchapter6.databinding.FragmentProfileBinding
import com.tegarpenemuan.challengchapter6.datastore.pref
import com.tegarpenemuan.challengchapter6.ui.signin.SignInActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    val viewModel: ProfileViewModel by viewModels()

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

        binding.btnLogout.setOnClickListener {
            requireContext().pref().clearPref()
            startActivity(Intent(requireContext(),SignInActivity::class.java))
        }

        bindViewModel()

        return root
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
                    .load("https://tegarpenemuan.xyz/storage/user/" + it.image)
                    .into(binding.ivProfile)
            }
        }
        viewModel.shouldShowUserError.observe(viewLifecycleOwner) {
            binding.tvName.text = it.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}