package com.example.healthcarecomp.common

import com.example.healthcarecomp.R
import com.example.healthcarecomp.data.model.MedicalSchedule
import java.util.Calendar
import java.util.Date

class Constant {
    companion object{
        val DEFAULT_ERROR_MESSAGE: String = "An error occurred"
        const val BASE_URL: String = "https://mockapi.io/projects/648fd2c81e6aa71680ca1f63"
        const val APP_DATABASE_NAME = "app_db"
        const val FIREBASE_DATABASE_URL = "https://healtcarecomp-default-rtdb.asia-southeast1.firebasedatabase.app/"
        const val DOCTOR_TBL = "doctors"
        const val PATIENT_TBL = "patients"
        const val MEDICAL_HISTORY_TBL = "medical_history"
        


        // Hàm này chạy tron MainActiity khi user là người dùng phổ thông
        fun getItemListForRecycleView_UserHome():  ArrayList<Item_recycleView>  {
            val itemList = ArrayList<Item_recycleView>()
            val it1 = Item_recycleView(1, R.drawable.baseline_incomplete_circle_24 , "Medical History", R.id.action_navigation_home_to_medicalHistoryFragment)
            itemList.add(it1)
            val it2 = Item_recycleView(2, R.drawable.baseline_incomplete_circle_24 , "QR Scan")
            itemList.add(it1)
            val it3 = Item_recycleView(3, R.drawable.baseline_incomplete_circle_24 , "Schedule")
            itemList.add(it1)
            val it4 = Item_recycleView(4, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it5 = Item_recycleView(5, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it6 = Item_recycleView(6, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it7 = Item_recycleView(7, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it8 = Item_recycleView(8, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it9 = Item_recycleView(9, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it10 = Item_recycleView(10, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it11 = Item_recycleView(11, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it12 = Item_recycleView(12, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            return itemList
        }


        // Hàm này chạy tron MainActiity khi user là Bác Sĩ

        fun getItemListForRecycleView_DoctorHome():  ArrayList<Item_recycleView>  {
            val itemList = ArrayList<Item_recycleView>()
            val it1 = Item_recycleView(1, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it2 = Item_recycleView(2, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it3 = Item_recycleView(3, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it4 = Item_recycleView(4, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it5 = Item_recycleView(5, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it6 = Item_recycleView(6, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it7 = Item_recycleView(7, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it8 = Item_recycleView(8, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it9 = Item_recycleView(9, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it10 = Item_recycleView(10, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it11 = Item_recycleView(11, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it12 = Item_recycleView(12, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            return itemList
        }


        fun getScheduleToday(): ArrayList<MedicalSchedule> {
            val scheduleList = ArrayList<MedicalSchedule>()

            val schedule1 = MedicalSchedule(
                doctorId = 1,
                patientID = 2,
                date_medical_examinaton = Date(2023,10,20),
                status_medical_schedule = "Đã hẹn"
            )
            scheduleList.add(schedule1)
            scheduleList.add(schedule1)

          //  scheduleList.add(schedule1)

//            val schedule2 = MedicalSchedule(
//                doctorId = 2,
//                patientID = 3,
//                date_medical_examinaton = Calendar.getInstance().time,
//                status_medical_schedule = "Chưa hẹn"
//            )
//            scheduleList.add(schedule2)
            return  scheduleList
        }

        fun getScheduleUpComing(): ArrayList<MedicalSchedule> {
            val scheduleUpComingList = ArrayList<MedicalSchedule>()

            val schedule1 = MedicalSchedule(
                doctorId = 1,
                patientID = 2,
                date_medical_examinaton = Date(2023,10,26),
                status_medical_schedule = "Đã hẹn"
            )


            val schedule2 = MedicalSchedule(
                doctorId = 2,
                patientID = 3,
                date_medical_examinaton = Date(2023,10,26),
                status_medical_schedule = "Chưa hẹn"
            )

            scheduleUpComingList.add(schedule1)
            scheduleUpComingList.add(schedule1)
            scheduleUpComingList.add(schedule1)
            scheduleUpComingList.add(schedule2)

            return  scheduleUpComingList
        }



        const val DOCTOR_SECURITY_DOCTOR = "bomaylabacsi"
        const val userSPKey: String = "USER"
    }

    enum class DoctorQuery(val queryField: String){
        PATH("Doctor"),
        EMAIL("email"),
        SPECIALTY("specialty"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        GENDER("gender"),
        PHONE("phone")
    }

    enum class PatientQuery(val queryField: String){
        PATH("Patient"),
        EMAIL("email"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        GENDER("gender"),
        PHONE("phone")
    }

    class Item_recycleView(
        val idIcon: Int,
        val imageIcon: Int,
        val nameIcon: String,
        val actionId:Int? = null
    )




}