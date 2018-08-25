package com.joker.emotionkeyboard.keyboard

import android.widget.EditText
import com.joker.emotionkeyboard.keyboard.data.Emotion

/**
 * OnChatKeyBoardListener
 *
 * @author joker
 * @date 2018/5/31.
 */
interface OnChatKeyBoardListener {

    /**
     * 发送按钮点击
     */
    fun onSendClick(editText: EditText)

    /**
     * 表情Item被点击
     */
    fun onEmotionItemClick(emotion: Emotion, position: Int)

    /**
     * 功能Item被电击
     */
    fun onFunctionItemClick(emotion: Emotion, position: Int)

    /**
     * 表情组Tab被点击
     */
    fun onEmotionTabSelected(position: Int)
}