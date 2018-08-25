package com.joker.emotionkeyboard.keyboard.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joker.emotionkeyboard.R
import com.joker.emotionkeyboard.keyboard.data.Emotion
import kotlinx.android.synthetic.main.item_add_image_text_emotion.view.*

/**
 * EmotionTabAdapter
 *
 * @author  joker
 * @date    2018/5/25
 * @since   1.0
 */
class FunctionGroupAdapter(private val data: MutableList<Emotion>, private val onItemClick: (Emotion, Int) -> Unit) : RecyclerView.Adapter<FunctionGroupAdapter.FunctionGroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionGroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_image_text_emotion, parent, false)
        return FunctionGroupViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FunctionGroupViewHolder, position: Int) {
        holder.itemView.apply {
            val emotion = data[position]
            this.setOnClickListener {
                onItemClick(emotion, position)
            }
            ivAddImage.setImageResource(emotion.value)
            tvAddDesc.text = emotion.desc
        }
    }

    class FunctionGroupViewHolder(itemView: View) : ViewHolder(itemView)
}
