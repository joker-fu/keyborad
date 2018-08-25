package com.joker.emotionkeyboard.keyboard.fragment

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * SpacesItemDecoration
 *
 * @author  joker
 * @date    2018/5/26
 * @since   1.0
 */
class SpacesItemDecoration(var l: Int = 0,
                           var t: Int = 0,
                           var r: Int = 0,
                           var b: Int = 0) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.apply {
            left = l
            top = t
            right = r
            bottom = b
        }
    }

}