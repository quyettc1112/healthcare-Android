package com.example.healthcarecomp.base.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthcarecomp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NFCBottomSheetNNotify: BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_nfc_check, container, false)

        // Trả về giao diện của bottom sheet dialog
        return view
    }
}