package com.example.healthcarecomp.ui.user.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combineTransform
@AndroidEntryPoint

class RegisterFragment : BaseFragment(R.layout.fragment_register) {

    val viewModel: RegisterViewModel by viewModels<RegisterViewModel>()
    lateinit var _binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)

        setEvent()

    }

    private fun setEvent() {
        _binding.btnSignUp.setOnClickListener {
            viewModel.register(_binding.etSignUpEmail.text.toString(),
                _binding.etSignUpPassword.text.toString(),
                _binding.etSignUpPasswordConfirm.text.toString())
        }
    }
}