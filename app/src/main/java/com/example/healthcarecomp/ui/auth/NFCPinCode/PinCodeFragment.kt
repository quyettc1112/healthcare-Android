package com.example.healthcarecomp.ui.auth.NFCPinCode

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.databinding.FragmentPinCodeBinding
import com.example.healthcarecomp.ui.activity.auth.AuthActivity
import com.example.healthcarecomp.ui.activity.auth.AuthViewModel
import com.example.healthcarecomp.ui.auth.login.LoginViewModel
import com.example.healthcarecomp.util.extension.afterTextChanged
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PinCodeFragment : BaseFragment(R.layout.fragment_pin_code){

    private  lateinit var _binding: FragmentPinCodeBinding
    private  lateinit var authViewModel: AuthViewModel
    private lateinit var _viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPinCodeBinding.inflate(inflater, container, false)

        _binding.customToolBarBackLogin.onStartIconClick = {
            authViewModel.ClearNFCValue()
            authViewModel.SetOnBackPressedF()
            navigateToPage(R.id.action_PinCodeFragment_to_loginFragment)
        }
        if (requireActivity() is AuthActivity) {
            authViewModel = (requireActivity() as AuthActivity).authViewModel
        }
        _viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    authViewModel.SetOnBackPressedF()
                    authViewModel.ClearNFCValue()
                    navigateToPage(R.id.action_PinCodeFragment_to_loginFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        CheckLoginPinCode()
        return _binding.root
    }

    fun CheckLoginPinCode() {
        _binding.passPinCodeNFC.afterTextChanged {
            authViewModel.onTextChangePSSValue(it)
        }
        _binding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    authViewModel.PSSValue.collectLatest {
                        val pss = authViewModel.NFCValue.value
                        if (!pss["SDT"].isNullOrEmpty() && !pss["PSD"].isNullOrEmpty() && pss["PSS"] == it) {
                            _viewModel.loginByPhone(pss["SDT"]!!, pss["PSD"]!!)
                            authViewModel.SetOnBackPressedF()
                            navigateToPage(R.id.action_PinCodeFragment_to_loginFragment)
                        }
                    }
                }
            }
        }
    }

}