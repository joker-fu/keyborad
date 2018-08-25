package com.joker.emotionkeyboard.keyboard.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joker.emotionkeyboard.R
import com.joker.emotionkeyboard.keyboard.data.Emotion
import com.joker.emotionkeyboard.keyboard.data.EmotionGroupType
import com.joker.emotionkeyboard.keyboard.util.Util
import kotlinx.android.synthetic.main.fragment_emotion_layout.*
import kotlinx.android.synthetic.main.item_image_emotion.view.*

/**
 * BaseEmotionFragment
 *
 * @author  joker
 * @date    2018/5/25
 * @since   1.0
 */
abstract class BaseEmotionFragment : Fragment() {

    abstract val emotionType: EmotionGroupType

    private var onEmotionItemClick: ((Emotion, Int) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        return inflater.inflate(R.layout.fragment_emotion_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewContent.apply {
            adapter = EmotionAdapter()
            layoutManager = getRecyclerViewLayoutManager()
            val space = Util.dp2px(context, 5F)
            addItemDecoration(SpacesItemDecoration(space, space, space, space))
        }
    }

    inner class EmotionAdapter : RecyclerView.Adapter<EmotionViewHolder>() {

        val data: MutableList<Emotion> by lazy {
            getEmotions()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmotionViewHolder {
            return EmotionViewHolder(getItemView(parent, viewType))
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: EmotionViewHolder, position: Int) {
            holder.itemView.apply {
                setOnClickListener {
                    onEmotionItemClick?.invoke(data[position], position)
                }
                onBindItem(ivEmotionIcon, data[position])
            }
        }
    }

    /**
     * 设置RecyclerView LayoutManager
     */
    abstract fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager

    /**
     * 获取表情集合
     */
    abstract fun getEmotions(): MutableList<Emotion>

    /**
     * 根据表情 emotionKey 获取id
     */
    abstract fun getEmotionForName(emotionKey: String): Int

    /**
     * 获取item view
     */
    abstract fun getItemView(parent: ViewGroup, viewType: Int): View

    /**
     * 绑定item数据
     */
    abstract fun onBindItem(imageView: AppCompatImageView, item: Emotion)

    /**
     * 表情点击监听
     */
    fun setOnEmotionItemClick(action: (Emotion, Int) -> Unit) {
        onEmotionItemClick = action
    }

    class EmotionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}