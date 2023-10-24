package com.example.healthcarecomp.ui.user.login

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.databinding.FragmentLoginBinding


class LoginFragment : BaseFragment(R.layout.fragment_login), View.OnClickListener {
    private lateinit var binding: FragmentLoginBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        setupUI()
    }

    private fun setupUI() {

        //set up display out line when user select role login
        binding.ivLoginPatient.setOnClickListener(this)

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

            }

            R.id.ibLoginWithGoogle -> {

            }

            R.id.tvLoginSignUpBtn -> {

            }
        }
    }


}