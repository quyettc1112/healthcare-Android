package com.example.healthcarecomp.ui.schedule

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.base.dialog.ConfirmDialog
import com.example.healthcarecomp.common.Adapter.ListDoctorAdapter
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.common.Screen
import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.databinding.FragmentScheduleBinding
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class ScheduleFragment : BaseFragment(R.layout.fragment_schedule) {

    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var _recyclerViewAdapter: ScheduleAdapter
    private lateinit var _recyclerViewAdapter_UpComing: ScheduleAdapter

    private lateinit var _recyclerViewAdapter_DoctorList: ListDoctorAdapter

    private lateinit var currentList_UpComing: List<Schedule>
    private lateinit var currentList_Today: List<Schedule>


    private var calendar = Calendar.getInstance()

    val dateFormat = SimpleDateFormat("MMM dd yyyy HH:mm")

    private var isCanceled: Boolean = false
    private var isTimeCanceled: Boolean = false
    var currentTime = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentScheduleBinding.inflate(inflater, container, false)


        binding.tvDayChoose.text = Calendar.getInstance().time.toString()
        scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]


        if (scheduleViewModel.getListToday()?.size == 0 && scheduleViewModel.getListTodayUPComing()?.size == 0) {
            Toast.makeText(requireContext(), "You have no Schedule", Toast.LENGTH_SHORT).show()
        }

        scheduleViewModel.getAllDoctor()
        _recyclerViewAdapter_DoctorList = ListDoctorAdapter()
        scheduleViewModel.doctorLIst.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success  -> {
                    _recyclerViewAdapter_DoctorList.differ.submitList(it?.data?.toList())
                }
                else -> {}
            }
        })
        setDate(binding)
        setUpUI(binding, scheduleViewModel)
        setUpUI_UpComing(binding, scheduleViewModel)
        return binding.root
    }

    private fun planSchedule(calendar: Calendar?, doctor: Doctor) {
        scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]
        scheduleViewModel.upsertSchedule(
            Schedule(
                doctorId = doctor.id,
                patientID = scheduleViewModel.patientID.toString(),
                date_medical_examinaton = calendar?.timeInMillis,
                status_medical_schedule = "Đã Đặt Lịch",
                note = "Tôi bị đau bụng",
            )
        )
    }

    private fun setUpUI(binding: FragmentScheduleBinding, scheduleViewModel: ScheduleViewModel) {
        _recyclerViewAdapter = ScheduleAdapter(scheduleViewModel)
        _recyclerViewAdapter.onItemClick = {
            setUpDialogPopup(it)
        }

        binding.rvListTodaySchedule.apply {
            adapter = _recyclerViewAdapter
        }
        scheduleViewModel.scheduleListToday.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    _recyclerViewAdapter.differ.submitList(it.data?.toList())
                    currentList_Today = _recyclerViewAdapter.differ.currentList?.toList()!!
                }
                else -> {}
            }
        })
        val itemTouchHelper = ItemTouchHelper(_recyclerViewAdapter.getSimpleCallBack())
        itemTouchHelper.attachToRecyclerView(binding.rvListTodaySchedule)
    }

    private fun setUpUI_UpComing(
        binding: FragmentScheduleBinding,
        scheduleViewModel: ScheduleViewModel
    ) {
        _recyclerViewAdapter_UpComing = ScheduleAdapter(scheduleViewModel)
        binding.rvListUpcomingSchedule.apply {
            adapter = _recyclerViewAdapter_UpComing
        }
        scheduleViewModel.scheduleListUpComing.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    _recyclerViewAdapter_UpComing.differ.submitList(it.data?.toList())
                    currentList_UpComing =
                        _recyclerViewAdapter_UpComing.differ.currentList?.toList()!!
                }
                else -> {}
            }
        })

        val itemTouchHelper = ItemTouchHelper(_recyclerViewAdapter_UpComing.getSimpleCallBack())
        itemTouchHelper.attachToRecyclerView(binding.rvListUpcomingSchedule)

    }
    fun setUpDoctorDialogPopUp(calendar: Calendar?, binding: FragmentScheduleBinding) {
        val dialogBinding = layoutInflater.inflate(R.layout.dialog_doctor_list, null)
        val rv = dialogBinding.findViewById<RecyclerView>(R.id.rv_doclist)
        val button = dialogBinding.findViewById<Button>(R.id.btn_close)
        rv.apply {
            adapter = _recyclerViewAdapter_DoctorList
        }
        val myDialog = Dialog(requireContext())
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(false)
        //myDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,   ViewGroup.LayoutParams.WRAP_CONTENT)
        myDialog.window?.setLayout(Screen.width, Screen.height)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(requireContext().getColor(R.color.zxing_transparent)))
        myDialog.show()
        button.setOnClickListener {
            myDialog.dismiss()
        }
        _recyclerViewAdapter_DoctorList.onItemClick = {
            ConfirmDialog(calendar,binding, it, "Meeting with ${it.lastName} at ${dateFormat.format(calendar?.time)} ", myDialog)
        }
    }

    fun setUpDialogPopup (schedule: Schedule) {

        _recyclerViewAdapter_UpComing.onItemClick = {
            setUpDialogPopup(it)
        }

        val dialogBinding = layoutInflater.inflate(R.layout.activity_pop_up, null)
        val title = dialogBinding.findViewById<TextView>(R.id.title_activity_popup)
        val deadline_title = dialogBinding.findViewById<TextView>(R.id.deadline_title_popup)
        val iconPopUp_activity = dialogBinding.findViewById<ImageView>(R.id.icon_doctor)
        val btn_finish = dialogBinding.findViewById<Button>(R.id.btn_statusButton)
        val btn_phone = dialogBinding.findViewById<Button>(R.id.phoneDoctor)

        title.text = "Meeting with"
        val dateSchedule = dateFormat.format(Constant.convertTimestampToCalendar(schedule.date_medical_examinaton!!).time)
        deadline_title.text = dateSchedule.toString()
        iconPopUp_activity.setImageResource(R.drawable.default_user_avt)


        val myDialog = Dialog(requireContext())
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        //myDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,   ViewGroup.LayoutParams.WRAP_CONTENT)
        myDialog.window?.setLayout(Screen.width,   Screen.height)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(requireContext().getColor(R.color.zxing_transparent)))
        myDialog.show()

        btn_finish.setOnClickListener {
            myDialog.dismiss()
        }
        btn_phone.setOnClickListener {
            myDialog.dismiss()
            val packageManager = requireContext().packageManager
            val appInstalled = packageManager.getPackageInfo("com.android.phone", 0) != null
            if (appInstalled) {
                // Create an intent to open the phone app
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:0356970696")
                // Start the intent
                startActivity(intent)
            } else {
                // Show an error message
                Toast.makeText(requireContext(), "Ứng dụng điện thoại không được cài đặt", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setDate(binding: FragmentScheduleBinding) {
        // Lấy ngày giờ hiện tại
        binding.btnShowDatePicker.setOnClickListener {
            // Tạo đối tượng DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            var setTime: Calendar?
            datePickerDialog.setOnCancelListener { isCanceled = true }
            datePickerDialog.setOnDismissListener {
                if (isCanceled == false) {
                    setTime = calendar
                    setTime(setTime, binding)
                } else isCanceled = false
            }
            datePickerDialog.datePicker.minDate = currentTime.timeInMillis
            datePickerDialog.show()
        }
    }

    private fun setTime(calendar: Calendar?, binding: FragmentScheduleBinding) {
        var validationHour = 0
        var validationMin = 0
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                validationHour = hourOfDay
                validationMin = minute
                calendar?.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar?.set(Calendar.MINUTE, minute)
            },
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            Calendar.getInstance().get(Calendar.MINUTE),
            true
        )
        timePickerDialog.setOnCancelListener { isTimeCanceled = true }
        timePickerDialog.setOnDismissListener {
            currentTime = Calendar.getInstance()
            if (checkDayPicker(calendar)) {
                if (checkMinTimeValidation(validationHour, validationMin) ) {
                    errorDialog(timePickerDialog,  "Cannot Choose Time In The Past")
                } else {
                    if (checkDuplicate(scheduleViewModel.getListToday()!!, calendar!!) == true && checkTimeDiff(calendar, currentTime)) {
                        errorDialog(timePickerDialog, "You have an appointment scheduled for that time")
                    } else {
                        setUpDoctorDialogPopUp(calendar, binding)
                    }
                }
            } else {
                when(isTimeCanceled) {
                    true -> { isTimeCanceled = false }
                    false -> {
                        if (checkDuplicate(scheduleViewModel.getListTodayUPComing()!!, calendar!!) == true) {
                            errorDialog(timePickerDialog, "You have an appointment scheduled for that time")
                        } else {
                     }
                    }
                }
            }
        }
        timePickerDialog.show()
    }



    fun ConfirmDialog(calendar: Calendar?, binding: FragmentScheduleBinding, doctor: Doctor, message: String, dialog: Dialog) {
        val confirmDialog = ConfirmDialog(
            requireContext(),
            object : ConfirmDialog.ConfirmCallback {
                override fun negativeAction() {}
                override fun positiveAction() {
                    binding.tvDayChoose.text = calendar?.time.toString()
                    planSchedule(calendar, doctor)
                    dialog.dismiss()
                }
            },
            title = "Confirm",
            message = message,
            positiveButtonTitle = "Yes",
            negativeButtonTitle = "No"
        )
        confirmDialog.show()
    }

    fun checkDuplicate(list: List<Schedule>, date: Calendar): Boolean {
        for (item in list) {
            val checkday = Constant.convertTimestampToCalendar(item.date_medical_examinaton!!)
            if (checkday.get(Calendar.HOUR_OF_DAY) == date.get(Calendar.HOUR_OF_DAY)  &&
                checkday.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR) &&
                checkday.get(Calendar.YEAR) == date.get(Calendar.YEAR)) {
                return true
            }
        }
        return false
    }
    fun checkTimeDiff(date1: Calendar, date2: Calendar): Boolean {
        val diff = date1.timeInMillis - date2.timeInMillis
        val diffInMinutes = diff / (1000 * 60)
        Log.d("Checl",  diffInMinutes.toString())
        return diffInMinutes <= 60L
    }


    private fun checkMinTimeValidation(validationHour: Int, validationMin: Int) =
        validationHour.toInt() < currentTime.get(Calendar.HOUR_OF_DAY) ||
                (validationHour == currentTime.get(Calendar.HOUR_OF_DAY)
                        && validationMin < currentTime.get(Calendar.MINUTE))

    private fun checkDayPicker(calendar: Calendar?) =
        isTimeCanceled == false &&
                (calendar?.get(Calendar.DAY_OF_YEAR) == currentTime.get(Calendar.DAY_OF_YEAR)) &&
                (calendar?.get(Calendar.YEAR) == currentTime.get(Calendar.YEAR))

    private fun errorDialog(timePickerDialog: TimePickerDialog, message: String) {
        val confirmDialog = ConfirmDialog(
            requireContext(),
            object : ConfirmDialog.ConfirmCallback {
                override fun negativeAction() {}
                override fun positiveAction() {
                    timePickerDialog.updateTime(
                        currentTime.get(Calendar.HOUR_OF_DAY),
                        currentTime.get(Calendar.MINUTE)
                    )
                    timePickerDialog.show()
                }
            },
            title = "Error",
            message = message,
            positiveButtonTitle = "Again",
            negativeButtonTitle = "Cancel"
        )
        // Show the ConfirmDialog
        confirmDialog.show()
    }


}

