package com.joker.emotionkeyboard

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_text.view.*

class TestAdapter(private val list: MutableList<CharSequence?>) : RecyclerView.Adapter<TestAdapter.TestViewHolder>() {

    fun insertData(content: CharSequence?) {
        val index = list.size
        list.add(index, content)
        notifyItemInserted(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false)
        return TestViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.itemView.tvContent.text = list[position]
    }

    class TestViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
