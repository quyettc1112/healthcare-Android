package com.example.healthcarecomp.common

import com.example.healthcarecomp.R

class Constant {
    companion object{
        const val BASE_URL: String = "https://mockapi.io/projects/648fd2c81e6aa71680ca1f63"
        const val APP_DATABASE_NAME = "app_db"
        const val FIREBASE_DATABASE_URL = "https://healtcarecomp-default-rtdb.asia-southeast1.firebasedatabase.app/"
        const val DOCTOR_TBL = "doctors"
        const val PATIENT_TBL = "patients"


        fun getItemListForRecycleView():  ArrayList<Item_recycleView>  {
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

    }

    class Item_recycleView(
        val idIcon: Int,
        val imageIcon: Int,
        val nameIcon: String
    )




}