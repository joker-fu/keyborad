package com.joker.emotionkeyboard.keyboard.data

import com.joker.emotionkeyboard.R


/**
 * EmotionDataHelper
 *
 * @author  joker
 * @date    2018/5/28
 * @since   1.0
 */
object EmotionDataHelper {

    private val emotionsMap = mutableMapOf<EmotionGroupType, MutableList<Emotion>>()


    init {
        //TODO 这里添加表情数据集合

        //经典表情
        val emotionClassic = mutableListOf<Emotion>().apply {
            for (i in 0..118) {
                when {
                    i < 9 -> add(Emotion("[bq_b00${i + 1}]", R.drawable.b001.plus(i)))
                    i < 99 -> add(Emotion("[bq_b0${i + 1}]", R.drawable.b001.plus(i)))
                    else -> add(Emotion("[bq_b${i + 1}]", R.drawable.b001.plus(i)))
                }
            }
        }

        emotionsMap[EmotionGroupType.EMOTION_TYPE_CLASSIC] = emotionClassic
    }

    @JvmStatic
    fun getEmotionsMap(): MutableMap<EmotionGroupType, MutableList<Emotion>> {
        return emotionsMap
    }

    @JvmStatic
    fun getEmotionValueByKey(type: EmotionGroupType, key: String): Int? {
        getEmotionsByType(type).forEach {
            if (it.key == key) {
                return it.value
            }
        }
        return null
    }

    @JvmStatic
    fun getEmotionValueByKey(key: String): Int? {
        emotionsMap.values.forEach {
            it.forEach {
                if (it.key == key) {
                    return it.value
                }
            }
        }
        return null
    }

    /**
     * 根据表情类型获取表情列表
     */
    @JvmStatic
    fun getEmotionsByType(type: EmotionGroupType): MutableList<Emotion> {

        //TODO 这里返回表情数据集合
        val res = when (type) {
            EmotionGroupType.EMOTION_TYPE_CLASSIC -> emotionsMap[type]
        }

        return res ?: mutableListOf()
    }

}