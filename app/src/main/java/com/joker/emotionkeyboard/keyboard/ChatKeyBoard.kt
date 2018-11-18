@file:Suppress("MemberVisibilityCanBePrivate")

package com.joker.emotionkeyboard.keyboard

import android.annotation.TargetApi
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.EditText
import com.joker.emotionkeyboard.R
import com.joker.emotionkeyboard.keyboard.data.EmotionGroupType
import com.joker.emotionkeyboard.keyboard.util.EmotionUtil
import com.joker.emotionkeyboard.keyboard.util.Util
import kotlinx.android.synthetic.main.widget_chat_keyboard_layout.view.*


/**
 * ChatKeyBoard
 *
 * @author  joker
 * @date    2018/5/25
 * @since   1.0
 */
class ChatKeyBoard : LinearLayoutCompat, CompoundButton.OnCheckedChangeListener, TextWatcher, View.OnTouchListener, View.OnKeyListener {

    companion object {
        const val TAG = "ChatKeyBoard"
        const val SHARE_PREFERENCE_NAME = "chatKeyBoard"
        const val SHARE_PREFERENCE_SOFT_INPUT_HEIGHT = "share_preference_soft_input_height"
    }

    private var bindContentView: View? = null

    private val activity by lazy {
        context as AppCompatActivity
    }

    private val inputMethodManager by lazy {
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private val sharedPreference by lazy {
        activity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {

        View.inflate(context, R.layout.widget_chat_keyboard_layout, this)

        activity.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                if (flContentLayout.isShown) {
                    lockContentHeight()
                    hideChatKeyBoardContent(false)
                    hideEmotionGroupView()
                    hideAddGroupView()
                    unlockContentHeightDelayed()
                }
            }
        })
        emotionGroupView.bindEditText(etMessageKeyBoard)
        etMessageKeyBoard.setOnTouchListener(this)
        etMessageKeyBoard.setOnKeyListener(this)
        etMessageKeyBoard.addTextChangedListener(this)
        cbAddKeyBoard.setOnCheckedChangeListener(this)
        cbEmotionKeyBoard.setOnCheckedChangeListener(this)
    }

    /**
     * 编辑框获取焦点，并显示软件盘
     */
    private fun showSoftInput() {
        etMessageKeyBoard.requestFocus()
        etMessageKeyBoard.post({
            inputMethodManager.showSoftInput(etMessageKeyBoard, 0)
            getSupportSoftInputHeight()
        })
    }

    /**
     * 隐藏软件盘
     */
    private fun hideSoftInput() {
        inputMethodManager.hideSoftInputFromWindow(etMessageKeyBoard.windowToken, 0)
    }

    /**
     * 获取软件盘的高度
     */
    private fun getSupportSoftInputHeight(): Int {

        val r = Rect()
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        activity.window.decorView.getWindowVisibleDisplayFrame(r)
        //获取屏幕的高度
        val screenHeight = activity.window.decorView.rootView.height
        //计算软件盘的高度
        var softInputHeight = screenHeight - r.bottom
        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (softInputHeight == 144 && Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight -= getSoftButtonsBarHeight()
        }
        if (softInputHeight < 0) {
            Log.w(TAG, "EmotionKeyboard--Warning: value of softInputHeight is below zero!")
        }
        //存一份到本地
        if (softInputHeight > 0) {
            sharedPreference.edit().putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, softInputHeight).apply()
        }
        return softInputHeight
    }

    /**
     * 底部虚拟按键栏的高度
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun getSoftButtonsBarHeight(): Int {
        val metrics = DisplayMetrics()
        //这个方法获取可能不是真实屏幕的高度
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        val usableHeight = metrics.heightPixels
        //获取当前屏幕的真实高度
        activity.windowManager.defaultDisplay.getRealMetrics(metrics)
        val realHeight = metrics.heightPixels
        return if (realHeight > usableHeight) {
            realHeight - usableHeight
        } else {
            0
        }
    }

    /**
     * 是否显示软件盘
     */
    private fun isSoftInputShown(): Boolean {
        return getSupportSoftInputHeight() != 0
    }

    /**
     * 获取软键盘高度
     */
    fun getSharedKeyBoardHeight(): Int {
        return sharedPreference.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, Util.dp2px(context, 282F))
    }

    /**
     * 绑定布局防止跳闪
     */
    fun bindContentView(view: View): ChatKeyBoard {
        bindContentView = view
        return this
    }

    /**
     * 锁定内容高度，防止跳闪
     */
    private fun lockContentHeight() {
        bindContentView?.let {
            val layoutParams = it.layoutParams as LinearLayoutCompat.LayoutParams
            layoutParams.height = it.height
            layoutParams.weight = 0f
        }
    }

    private fun unlockContentHeightDelayed() {
        if (bindContentView != null) {
            handler.postDelayed({ (bindContentView?.layoutParams as LinearLayoutCompat.LayoutParams).weight = 1f }, 200L)
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (v == etMessageKeyBoard && event.action == MotionEvent.ACTION_UP && flContentLayout.isShown) {
            lockContentHeight()//显示软件盘时，锁定内容高度，防止跳闪。
            hideChatKeyBoardContent(true)//隐藏表情布局，显示软件盘
            hideEmotionGroupView()
            hideAddGroupView()
            //软件盘显示后，释放内容高度
            etMessageKeyBoard.postDelayed({
                unlockContentHeightDelayed()
            }, 200L)
        }
        return false
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView) {
            cbEmotionKeyBoard -> {
                if (isChecked) {
                    if (isSoftInputShown()) {
                        lockContentHeight()
                        showEmotionGroupView()
                        unlockContentHeightDelayed()
                    } else {
                        showEmotionGroupView()
                    }
                } else {
                    if (flContentLayout.isShown) {
                        lockContentHeight()
                        hideChatKeyBoardContent(true)
                        hideEmotionGroupView()
                        unlockContentHeightDelayed()
                    }
                }
            }
            cbAddKeyBoard -> {
                if (isChecked) {
                    if (isSoftInputShown()) {
                        lockContentHeight()
                        showAddGroupView()
                        unlockContentHeightDelayed()
                    } else {
                        showAddGroupView()
                    }
                } else {
                    if (flContentLayout.isShown) {
                        lockContentHeight()
                        hideChatKeyBoardContent(true)
                        hideAddGroupView()
                        unlockContentHeightDelayed()
                    }
                }
            }
        }
    }

    /**
     * 显示聊天键盘内容
     */
    private fun showChatKeyBoardContent() {
        var softInputHeight = getSupportSoftInputHeight()
        if (softInputHeight <= 0) {
            softInputHeight = getSharedKeyBoardHeight()
        } else {
            hideSoftInput()
        }
        flContentLayout.layoutParams.height = softInputHeight
        //解决出现表情键盘 软键盘还没来得及隐藏问题 导致表情键盘在软键盘之上 然后落下问题
        flContentLayout.postDelayed({
            flContentLayout.visibility = View.VISIBLE
        }, 200L)

    }

    /**
     * 隐藏聊天键盘内容
     * @param showSoft 是否显示软件盘
     */
    private fun hideChatKeyBoardContent(showSoft: Boolean) {
        if (flContentLayout.isShown) {
            if (!cbEmotionKeyBoard.isChecked && !cbAddKeyBoard.isChecked) {
                flContentLayout.visibility = View.GONE
                if (showSoft) {
                    showSoftInput()
                }
            }
        }
    }

    /**
     * 显示表情键盘
     */
    private fun showEmotionGroupView() {
        showChatKeyBoardContent()
        cbEmotionKeyBoard.isChecked = true
        emotionGroupView.visibility = View.VISIBLE
        hideAddGroupView()
    }

    /**
     * 隐藏表情键盘
     */
    private fun hideEmotionGroupView() {
        cbEmotionKeyBoard.isChecked = false
        emotionGroupView.visibility = View.GONE
    }

    fun hideKeyBoard() {
        inputMethodManager.hideSoftInputFromWindow(emotionGroupView.windowToken, 0)
        cbEmotionKeyBoard.isChecked = false
        flContentLayout.visibility = View.GONE
    }

    /**
     * 显示增加键盘
     */
    private fun showAddGroupView() {
        showChatKeyBoardContent()
        cbAddKeyBoard.isChecked = true
        functionGroupView.visibility = View.VISIBLE
        hideEmotionGroupView()
    }

    private fun hideAddGroupView() {
        cbAddKeyBoard.isChecked = false
        functionGroupView.visibility = View.GONE
    }

    override fun afterTextChanged(s: Editable) {
        if (s.isNotEmpty()) {
            tvSendKeyBoard.visibility = View.VISIBLE
            cbAddKeyBoard.visibility = View.INVISIBLE
        } else {
            tvSendKeyBoard.visibility = View.INVISIBLE
            cbAddKeyBoard.visibility = View.VISIBLE
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL && event?.action == KeyEvent.ACTION_DOWN) {
            (v as? EditText)?.let {
                if (it.text?.length == 0) {
                    clearReferenceLabel()
                }
            }
        }
        return false
    }

    private var onChatKeyBoardListener: OnChatKeyBoardListener? = null

    fun setOnChatKeyBoardListener(listener: OnChatKeyBoardListener) {
        onChatKeyBoardListener = listener
        initListener()
    }

    fun initListener() {
        onChatKeyBoardListener?.apply {
            tvSendKeyBoard.setOnClickListener {
                onSendClick(etMessageKeyBoard)
            }
            emotionGroupView.setOnEmotionItemClickListener { emotion, position ->
                onEmotionItemClick(emotion, position)
            }
            emotionGroupView.setOnTabItemSelectedListener {
                onEmotionTabSelected(it)
            }
            functionGroupView.setOnFunctionItemClickListener { emotion, position ->
                onFunctionItemClick(emotion, position)
            }
        }
    }


    /**
     * 设置编辑框引用消息
     */
    fun setRefrenceText(charSequence: CharSequence?) {
        tvRefrenceText.visibility = View.VISIBLE
        val str = EmotionUtil.getEmotionContent(context, EmotionGroupType.EMOTION_TYPE_CLASSIC,
                charSequence?.toString() ?: "")
        tvRefrenceText.text = str
    }

    /**
     * 获取引用消息
     */
    fun getRefrenceText(): CharSequence? {
        return tvRefrenceText.text
    }

    /**
     * 是否含有引用消息
     */
    fun isShowReferenceText(): Boolean {
        return tvRefrenceText.visibility == View.VISIBLE
    }

    /**
     * 清除编辑框输入数据
     */
    fun clearEditText() {
        clearReferenceLabel()
        etMessageKeyBoard.text.clear()
    }

    private fun clearReferenceLabel() {
        tvRefrenceText.text = ""
        tvRefrenceText.visibility = View.GONE
    }
}
