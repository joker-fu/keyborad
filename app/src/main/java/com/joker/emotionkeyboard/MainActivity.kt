package com.joker.emotionkeyboard

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.EditText
import android.widget.Toast
import com.joker.emotionkeyboard.keyboard.OnChatKeyBoardListener
import com.joker.emotionkeyboard.keyboard.data.Emotion
import com.joker.emotionkeyboard.keyboard.data.EmotionGroupType
import com.joker.emotionkeyboard.keyboard.util.EmotionUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter by lazy {
        TestAdapter(mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        initChatKeyBoard()
    }

    private fun initChatKeyBoard() {
        chatKeyBoard.bindContentView(recyclerView)
        chatKeyBoard.setOnChatKeyBoardListener(object : OnChatKeyBoardListener {
            override fun onSendClick(editText: EditText) {
                val msg = editText.text.toString()
                if (!chatKeyBoard.isShowReferenceText()) {
                    //消息引用是否显示
                }
                val content =
                        EmotionUtil.getEmotionContent(this@MainActivity, EmotionGroupType.EMOTION_TYPE_CLASSIC, msg)
                adapter.insertData(content)
                chatKeyBoard.clearEditText()
            }

            override fun onEmotionItemClick(emotion: Emotion, position: Int) {
                toast(emotion.key)
            }

            override fun onFunctionItemClick(emotion: Emotion, position: Int) {
                when (position) {
                    0 -> toast("相册选择")//相册选择
                    1 -> toast("拍照") //拍照
                }
                chatKeyBoard.hideKeyBoard()
            }

            override fun onEmotionTabSelected(position: Int) = Unit

        })
    }

    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
