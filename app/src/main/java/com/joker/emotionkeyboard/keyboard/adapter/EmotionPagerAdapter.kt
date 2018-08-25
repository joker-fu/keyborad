package com.joker.emotionkeyboard.keyboard.adapter
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.joker.emotionkeyboard.keyboard.fragment.BaseEmotionFragment

/**
 * EmotionPagerAdapter
 *
 * @author  joker
 * @date    2018/5/25
 * @since   1.0
 */
class EmotionPagerAdapter(fm: FragmentManager, private val data: MutableList<BaseEmotionFragment>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return data[position]
    }

    override fun getCount(): Int {
        return data.size
    }

}