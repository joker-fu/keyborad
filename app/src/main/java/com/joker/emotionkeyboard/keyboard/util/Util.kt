package com.joker.emotionkeyboard.keyboard.util

import android.content.Context

/**
 * Util
 *
 * @author  joker
 * @date    2018/5/26
 * @since   1.0
 */
object Util {

    /**
     * 将dp值转换为px值
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

}
