package com.example.proteinpro.util.ViewPager2Adapter
import android.util.Log

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.proteinpro.util.Class.food.FoodInformationItem
import com.example.proteinpro.view.main.search.FragmentMaterial
import com.example.proteinpro.view.main.search.FragmentNutrient
import com.example.proteinpro.view.main.search.FragmentProteinInformation

class ViewPager2Adapter(fragmentActivity: FragmentActivity? , private val foodItem: FoodInformationItem) :
    FragmentStateAdapter(fragmentActivity!!) {

    private val mSetItemCount = 3 // 프래그먼트 갯수 지정
    override fun createFragment(position: Int): Fragment {
        val iViewIdx = getRealPosition(position)
        return when (iViewIdx) {
            0 -> {
                Log.i ("ViewPager2Adapter", "foodItem"+foodItem)
                FragmentNutrient(foodItem)
            }

            1 -> {
                Log.i ("ViewPager2Adapter", "foodItem"+foodItem)
                FragmentProteinInformation(foodItem)

            }

            2-> {
                Log.i ("ViewPager2Adapter", "foodItem"+foodItem)
                FragmentMaterial(foodItem)
            }

            else -> {
                FragmentNutrient(foodItem)
            }
        }
    }

    fun getRealPosition(_iPosition: Int): Int {
        return _iPosition % mSetItemCount
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemCount(): Int {
        return 3
    }

}