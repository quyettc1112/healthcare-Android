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
        


        // Hàm này chạy tron MainActiity khi user là người dùng phổ thông
        fun getItemListForRecycleView_UserHome():  ArrayList<Item_recycleView>  {
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
                date_medical_examinaton = Date(2023,10,25),
                status_medical_schedule = "Đã hẹn"
            )
            scheduleList.add(schedule1)

//            val schedule2 = MedicalSchedule(
//                doctorId = 2,
//                patientID = 3,
//                date_medical_examinaton = Calendar.getInstance().time,
//                status_medical_schedule = "Chưa hẹn"
//            )
//            scheduleList.add(schedule2)

            return  scheduleList
        }



    }

    class Item_recycleView(
        val idIcon: Int,
        val imageIcon: Int,
        val nameIcon: String
    )




}