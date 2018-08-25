package com.joker.emotionkeyboard.keyboard.data

/**
 * Emotion
 *
 * @author  joker
 * @date    2018/5/25
 * @since   1.0
 */
/**
 * @param key 表情key 正则匹配使用
 * @param value 表情值 本地资源id
 * @param desc 表情描述
 *
 */
data class Emotion(val key: String,
                   val value: Int,
                   val desc: String = "")