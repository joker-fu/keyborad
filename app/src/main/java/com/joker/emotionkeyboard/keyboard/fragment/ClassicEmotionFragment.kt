package com.joker.emotionkeyboard.keyboard.fragment

import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joker.emotionkeyboard.R
import com.joker.emotionkeyboard.keyboard.data.Emotion
import com.joker.emotionkeyboard.keyboard.data.EmotionDataHelper
import com.joker.emotionkeyboard.keyboard.data.EmotionGroupType

/**
 * ClassicEmotionFragment
 *
 * @author  joker
 * @date    2018/5/25
 * @since   1.0
 */
class ClassicEmotionFragment : BaseEmotionFragment() {

    override val emotionType: EmotionGroupType
        get() = EmotionGroupType.EMOTION_TYPE_CLASSIC

    private val data by lazy {
        EmotionDataHelper.getEmotionsByType(emotionType)
    }

    override fun getEmotions(): MutableList<Emotion> {
        return data
    }

    override fun getEmotionForName(emotionKey: String): Int {
        data.forEach {
            if (it.key == emotionKey) {
                return it.value
            }
        }
        return 0
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(activity, 7, GridLayoutManager.VERTICAL, false)
    }

    override fun getItemView(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.item_image_emotion, parent, false)
    }

    override fun onBindItem(imageView: AppCompatImageView, item: Emotion) {
        imageView.setImageResource(item.value)
    }

}