package com.joker.emotionkeyboard.keyboard

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import com.joker.emotionkeyboard.R
import com.joker.emotionkeyboard.keyboard.adapter.EmotionPagerAdapter
import com.joker.emotionkeyboard.keyboard.adapter.EmotionTabAdapter
import com.joker.emotionkeyboard.keyboard.data.Emotion
import com.joker.emotionkeyboard.keyboard.data.EmotionDataHelper
import com.joker.emotionkeyboard.keyboard.data.EmotionGroupType
import com.joker.emotionkeyboard.keyboard.fragment.BaseEmotionFragment
import com.joker.emotionkeyboard.keyboard.util.EmotionUtil
import kotlinx.android.synthetic.main.widget_emotion_view_board_layout.view.*


/**
 * EmotionGroupView
 *
 * @author  joker
 * @date    2018/5/25
 * @since   1.0
 */
class EmotionGroupView : LinearLayoutCompat, View.OnClickListener, ViewPager.OnPageChangeListener {

    private lateinit var editText: EditText

    private var onTabItemSelected: ((Int) -> Unit)? = null

    private var onEmotionItemClick: ((Emotion, Int) -> Unit)? = null

    /**
     * 表情页数据
     */
    private val emotionFragments: MutableList<BaseEmotionFragment> = mutableListOf()

    /**
     * 表情页底部的表情类型Tab数据
     */
    private var emotionTabList: MutableList<EmotionGroupType> = mutableListOf()

    private val emotionTabAdapter by lazy {
        EmotionTabAdapter(emotionTabList) { _, position ->
            vpEmotionGroup.currentItem = position
            onTabItemSelected?.invoke(position)
        }
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {

        context as AppCompatActivity
        inflate(context, R.layout.widget_emotion_view_board_layout, this)
        initEmotionData()

        emotionFragments.forEach {
            it.setOnEmotionItemClick { emotion, position ->
                if (it.emotionType == EmotionGroupType.EMOTION_TYPE_CLASSIC) {
                    // 获取输入框当前光标位置,在指定位置上添加表情图片文本
                    editText.apply {
                        val currentSelectionStart = selectionStart
                        val sb = StringBuilder(text.toString())
                        sb.insert(currentSelectionStart, (emotion.key))
                        // 特殊文字处理,将表情等转换一下
                        setText(EmotionUtil.getInputEmotionContent(context, it.emotionType, this, sb.toString()))
                        setSelection(currentSelectionStart + emotion.key.length)
                    }
                }
                onEmotionItemClick?.invoke(emotion, position)
            }
        }
        vpEmotionGroup.adapter = EmotionPagerAdapter(context.supportFragmentManager, emotionFragments)
        rvEmotionTab.apply {
            adapter = emotionTabAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        vpEmotionGroup.addOnPageChangeListener(this)
        ivDeleteEmotion.setOnClickListener(this)
    }

    private fun initEmotionData() {
//        val classicEmotionFragment = ClassicEmotionFragment()
//        emotionTabList.add(classicEmotionFragment.emotionType)
//        emotionFragments.add(classicEmotionFragment)
        val emotionsMap = EmotionDataHelper.getEmotionsMap()
        emotionsMap.keys.forEach {
            (it.FragmentClass.newInstance() as BaseEmotionFragment).apply {
                emotionTabList.add(emotionType)
                emotionFragments.add(this)
            }
        }
    }

    fun bindEditText(et: EditText) {
        editText = et
    }

    override fun onPageSelected(position: Int) {
        rvEmotionTab.scrollToPosition(position)
        emotionTabAdapter.setCurrentSelected(position)
        onTabItemSelected?.invoke(position)
    }

    override fun onPageScrollStateChanged(state: Int) = Unit

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

    override fun onClick(v: View) {
        //发送一个删除事件交给系统进行删除操作
        editText.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
    }

    fun setOnTabItemSelectedListener(listener: (Int) -> Unit) {
        onTabItemSelected = listener
    }

    fun setOnEmotionItemClickListener(action: (Emotion, Int) -> Unit) {
        onEmotionItemClick = action
    }
}