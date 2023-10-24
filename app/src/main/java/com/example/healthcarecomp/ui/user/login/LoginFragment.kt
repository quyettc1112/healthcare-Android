package com.example.healthcarecomp.ui.user.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.databinding.FragmentLoginBinding
import com.example.healthcarecomp.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.zip.Inflater

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login), View.OnClickListener{
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {


        //set up display out line when user select role login
        binding.ivLoginPatient.setOnClickListener(this)
        binding.ivLoginDoctor.setOnClickListener(this)

        //setup login btn
        binding.ibLoginWithGoogle.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)

        binding.tvLoginSignUpBtn.setOnClickListener(this)
        binding.tvLoginForgotBnt.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){

            //set up display out line when user select role login
            R.id.ivLoginPatient -> {
                binding.llLoginPatientBorder.setBackgroundResource(R.drawable.img_high_light)
                binding.llLoginDoctorBorder.setBackgroundColor(Color.TRANSPARENT)
            }

            R.id.ivLoginDoctor -> {
                binding.llLoginDoctorBorder.setBackgroundResource(R.drawable.img_high_light)
                binding.llLoginPatientBorder.setBackgroundColor(Color.TRANSPARENT)
            }

            //
            R.id.btnLogin -> {
                openMainActivity()
            }

            R.id.ibLoginWithGoogle -> {
                openMainActivity()
            }

            R.id.tvLoginSignUpBtn -> {

            }
        }
    }

    private fun openMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }


}