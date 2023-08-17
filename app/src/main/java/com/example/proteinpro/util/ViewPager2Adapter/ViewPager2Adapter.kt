package com.example.proteinpro.util.ViewPager2Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.proteinpro.view.main.search.FragmentMaterial
import com.example.proteinpro.view.main.search.FragmentNutrient
import com.example.proteinpro.view.main.search.FragmentProteinInformation

class ViewPager2Adapter(fragmentActivity: FragmentActivity?) :
    FragmentStateAdapter(fragmentActivity!!) {
    private val mSetItemCount = 3 // 프래그먼트 갯수 지정
    override fun createFragment(position: Int): Fragment {
        val iViewIdx = getRealPosition(position)
        return when (iViewIdx) {
            0 -> {
                FragmentNutrient()
            }

            1 -> {
                FragmentProteinInformation()

            }

            2-> {
                FragmentMaterial()
            }

            else -> {
                FragmentNutrient()
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