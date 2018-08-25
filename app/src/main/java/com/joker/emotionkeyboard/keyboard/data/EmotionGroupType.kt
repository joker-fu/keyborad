package com.joker.emotionkeyboard.keyboard.data

import com.joker.emotionkeyboard.R
import com.joker.emotionkeyboard.keyboard.fragment.ClassicEmotionFragment

/**
 * EmotionGroupType
 *
 * @author  joker
 * @date    2018/5/25
 * @since   1.0
 */

enum class EmotionGroupType(val emotionTypeIcon: Int,
                            val FragmentClass: Class<*>) {

    //TODO 这里添加表情类型
    EMOTION_TYPE_CLASSIC(R.drawable.vector_keyboard_emotion, ClassicEmotionFragment::class.java) //经典表情
}