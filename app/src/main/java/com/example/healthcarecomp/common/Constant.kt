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
        const val APP_DATABASE_NAME = "app_db"
        const val FIREBASE_DATABASE_URL = "https://healtcarecomp-default-rtdb.asia-southeast1.firebasedatabase.app/"
        const val MEDICAL_HISTORY_TBL = "medical_history"
        const val DOCTOR_SECURITY_DOCTOR = "123"
        const val USER_SHARE_PREF_KEY: String = "user"
        const val DOCTOR_SHARE_PREF_KEY: String = "doctor"
        const val PATIENT_SHARE_PREF_KEY: String = "patient"
        const val PATIENT_MEDICAL_HISTORY_KEY = "PatientMedicalId"

        const val BASE_URL = "https://fcm.googleapis.com"
        const val DEFAULT_ACCESS_TOKEN = "835e9ebec19b393f1a561302ccfc73dc55308747"
        const val CONTENT_TYPE = "application/json"
        const val NOTIFICATION_HOST = "fcm.googleapis.com"
        const val SCHEDULE_TBL = "schedule"



        // Hàm này chạy tron MainActiity khi user là người dùng phổ thông
        fun getItemListForRecycleView_UserHome():  ArrayList<ItemRecycleView>  {
            val itemList = ArrayList<ItemRecycleView>()

            val it1 = ItemRecycleView(1, R.drawable.baseline_incomplete_circle_24 , "Medical History", R.id.action_navigation_home_to_medicalHistoryFragment)
            itemList.add(it1)
            val it2 = ItemRecycleView(2, R.drawable.baseline_incomplete_circle_24 , "QR Scan")
            itemList.add(it1)
            val it3 = ItemRecycleView(3, R.drawable.baseline_incomplete_circle_24 , "Schedule")
            itemList.add(it1)
            val it4 = ItemRecycleView(4, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it5 = ItemRecycleView(5, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it6 = ItemRecycleView(6, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            return itemList
        }


        // Hàm này chạy tron MainActiity khi user là Bác Sĩ

        fun getItemListForRecycleView_DoctorHome():  ArrayList<ItemRecycleView>  {
            val itemList = ArrayList<ItemRecycleView>()
            val it1 = ItemRecycleView(1, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it2 = ItemRecycleView(2, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it3 = ItemRecycleView(3, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it4 = ItemRecycleView(4, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it5 = ItemRecycleView(5, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it6 = ItemRecycleView(6, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it7 = ItemRecycleView(7, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it8 = ItemRecycleView(8, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it9 = ItemRecycleView(9, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it10 = ItemRecycleView(10, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it11 = ItemRecycleView(11, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            val it12 = ItemRecycleView(12, R.drawable.baseline_incomplete_circle_24 , "Chỉ Số IBM")
            itemList.add(it1)
            return itemList
        }

        fun convertTimestampToCalendar(timestamp: Long): Calendar {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp
            return calendar
        }


    }

    @Retention(AnnotationRetention.SOURCE)
    annotation class BottomNav() {
        companion object {
            val HOME_NAV = listOf(R.id.navigation_home, R.id.medicalHistoryFragment, R.id.medicalRecordFragment)
            val SCHEDULE_NAV = listOf(R.id.navigation_schedule)
            val CHAT_NAV = listOf(R.id.navigation_chat, R.id.chatMessageFragment)
            val INFO_NAV = listOf(R.id.navigation_info, R.id.viewProfileFragment)
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

    enum class ChatMessageQuery(val queryField: String){
        PATH("chat_message"),
        TIME_STAMP("timeStamp"),
        CHAT_ROOM_ID("chatRoomId"),
        CONTENT("content"),
        SENDER_ID("senderId"),
        RECEIVER_ID("receiverId")
    }

    class ItemRecycleView(
        val idIcon: Int,
        val imageIcon: Int,
        val nameIcon: String,
        val actionId:Int? = null
    )







}