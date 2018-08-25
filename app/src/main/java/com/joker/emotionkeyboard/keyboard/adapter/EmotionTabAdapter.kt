package com.joker.emotionkeyboard.keyboard.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joker.emotionkeyboard.keyboard.data.EmotionGroupType
import com.joker.emotionkeyboard.R
import kotlinx.android.synthetic.main.item_image_emotion.view.*

/**
 * EmotionTabAdapter
 *
 * @author  joker
 * @date    2018/5/25
 * @since   1.0
 */
class EmotionTabAdapter(private val data: MutableList<EmotionGroupType>, private val onItemClick: (View, Int) -> Unit) : RecyclerView.Adapter<EmotionTabAdapter.EmotionTabViewHolder>() {

    private var currentSelectedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmotionTabViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_emotion_tab, parent, false)
        return EmotionTabViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: EmotionTabViewHolder, position: Int) {
        holder.itemView.apply {
            ivEmotionIcon.setImageResource(data[position].emotionTypeIcon)
            setOnClickListener {
                onItemClick(it, position)
            }
            if (currentSelectedPosition == position) {
                setBackgroundColor(ContextCompat.getColor(context, R.color.chat_keyboard_emotion_tab_item_checked_color))
            } else {
                setBackgroundColor(ContextCompat.getColor(context, R.color.chat_keyboard_emotion_tab_item_unchecked_color))
            }
        }
    }

    fun setCurrentSelected(position: Int) {
        currentSelectedPosition = position
        notifyDataSetChanged()
    }

    class EmotionTabViewHolder(itemView: View) : ViewHolder(itemView)
}
