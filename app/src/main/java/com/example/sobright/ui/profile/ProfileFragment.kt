package com.example.sobright.ui.profile

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.sobright.auth.data.AuthViewModel
import com.example.sobright.auth.data.ViewModelFactory
import com.example.sobright.auth.register.RegisterActivity
import com.example.sobright.auth.start.StartActivity
import com.example.sobright.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory(activity?.application ?: Application())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.email.text = viewModel.getUser()?.email
        binding.userName.text = viewModel.getUser()?.displayName
        Glide.with(requireContext())
            .load(viewModel.getUser()?.photoUrl)
            .into(binding.profileImage)

        val loginButton: Button = binding.loginButton
        loginButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), StartActivity::class.java))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
