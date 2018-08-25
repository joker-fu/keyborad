package com.joker.emotionkeyboard.keyboard

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import com.joker.emotionkeyboard.R
import com.joker.emotionkeyboard.keyboard.adapter.FunctionGroupAdapter
import com.joker.emotionkeyboard.keyboard.data.Emotion
import com.joker.emotionkeyboard.keyboard.fragment.SpacesItemDecoration
import com.joker.emotionkeyboard.keyboard.util.Util
import kotlinx.android.synthetic.main.widget_function_view_board_layout.view.*

/**
 * FunctionGroupView
 *
 * @author  joker
 * @date    2018/5/28
 * @since   1.0
 */
class FunctionGroupView : LinearLayoutCompat {

    private var onFunctionItemClick: ((Emotion, Int) -> Unit)? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private val data by lazy {
        val list = mutableListOf<Emotion>()
        list.add(Emotion("", R.drawable.ic_photo_album_key_board, "相册"))
        list.add(Emotion("", R.drawable.ic_camera_key_board, "拍照"))
        list
    }

    private fun init(context: Context) {

        inflate(context, R.layout.widget_function_view_board_layout, this)

        recyclerViewContent.apply {
            adapter = FunctionGroupAdapter(data) { emotion, position ->
                onFunctionItemClick?.invoke(emotion, position)
            }
            layoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, true)
            val space = Util.dp2px(context, 20F)
            addItemDecoration(SpacesItemDecoration(0, space, 0, space))
        }
    }

    fun setOnFunctionItemClickListener(listener: (Emotion, Int) -> Unit) {
        onFunctionItemClick = listener
    }
}