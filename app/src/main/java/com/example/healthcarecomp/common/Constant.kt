package com.example.healthcarecomp.common

import android.os.Bundle
import com.example.healthcarecomp.R
import com.example.healthcarecomp.data.model.Schedule
import java.util.Calendar

class Constant {
    sealed class MEDICAL() {
        enum class INT(val range: IntRange, val dimension: String){
            HEARTH_RATE(40..200, "bpm"),
            BLOOD_SUGAR(40..200, "mg/dL")
        }
        enum class FLOAT(val range: ClosedFloatingPointRange<Float>, val dimension: String){
            WEIGHT(0f..300f, "kg"),
            HEIGHT(0f..300f, "cm"),
            BODY_TEMPERATURE(34f..40f, "C")
        }

        enum class STRING(val regex: Regex, val dimension: String){
            BLOOD_PRESSURE("""^\d{2,3}/\d{2,3}$""".toRegex(), "mg Hg")
        }

    }


    companion object{
        const val DEFAULT_ERROR_MESSAGE: String = "An error occurred"
        const val BASE_URL: String = "https://mockapi.io/projects/648fd2c81e6aa71680ca1f63"
        const val APP_DATABASE_NAME = "app_db"
        const val FIREBASE_DATABASE_URL = "https://healtcarecomp-default-rtdb.asia-southeast1.firebasedatabase.app/"
        const val MEDICAL_HISTORY_TBL = "medical_history"
        const val DOCTOR_SECURITY_DOCTOR = "123"
        const val USER_SHARE_PREF_KEY: String = "user"
        const val DOCTOR_SHARE_PREF_KEY: String = "doctor"
        const val PATIENT_SHARE_PREF_KEY: String = "patient"
        const val PATIENT_MEDICAL_HISTORY_KEY = "PatientMedicalId"


        const val SCHEDULE_TBL = "schedule"



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

        fun getScheduleToday(): ArrayList<Schedule> {
            val scheduleList = ArrayList<Schedule>()

            val schedule1 = Schedule(
                doctorId = 1,
                patientID = 2,
                date_medical_examinaton = Calendar.getInstance().timeInMillis,
                status_medical_schedule = "Đã hẹn"
            )
            scheduleList.add(schedule1)
            scheduleList.add(schedule1)

            return  scheduleList
        }

        fun getScheduleUpComing(): ArrayList<Schedule> {
            val scheduleUpComingList = ArrayList<Schedule>()

            val schedule1 = Schedule(
                doctorId = 1,
                patientID = 2,
                date_medical_examinaton = Calendar.getInstance().timeInMillis,
                status_medical_schedule = "Đã hẹn"
            )


            val schedule2 = Schedule(
                doctorId = 2,
                patientID = 3,
                date_medical_examinaton = Calendar.getInstance().timeInMillis,
                status_medical_schedule = "Chưa hẹn"
            )

            scheduleUpComingList.add(schedule1)
            scheduleUpComingList.add(schedule1)
            scheduleUpComingList.add(schedule1)
            scheduleUpComingList.add(schedule2)

            return  scheduleUpComingList
        }
    }

    enum class DoctorQuery(val queryField: String){
        PATH("Doctor"),
        EMAIL("email"),
        SPECIALTY("specialty"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        GENDER("gender"),
        PHONE("phone"),
        ID("id"),
        AVATAR("avatar")
    }

    enum class PatientQuery(val queryField: String){
        PATH("Patient"),
        EMAIL("email"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        GENDER("gender"),
        PHONE("phone"),
        ID("id"),
        AVATAR("avatar")
    }

    enum class ChatRoomQuery(val queryField: String){
        PATH("chat_room"),
        FIRST_USER_ID("firstUserId"),
        SECOND_USER_ID("secondUserId"),
        LAST_ACTIVE_TIME("lastActiveTime"),
        CHAT_SEEN("chatSeen"),
        ID("id")

    }

    class Item_recycleView(
        val idIcon: Int,
        val imageIcon: Int,
        val nameIcon: String,
        val actionId:Int? = null
    )






}