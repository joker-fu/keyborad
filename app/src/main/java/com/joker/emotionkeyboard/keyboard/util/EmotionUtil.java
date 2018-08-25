package com.joker.emotionkeyboard.keyboard.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.joker.emotionkeyboard.R;
import com.joker.emotionkeyboard.keyboard.data.EmotionDataHelper;
import com.joker.emotionkeyboard.keyboard.data.EmotionGroupType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EmotionUtil
 *
 * @author joker
 * @date 2018/5/26
 * @since 1.0
 */
public class EmotionUtil {
    /**
     * 处理输入框中的表情，将表情大小进行压缩
     */
    public static SpannableString getInputEmotionContent(Context context, EmotionGroupType emotionType, final TextView tv, String source) {
        // 表情图片
        int size = (int) tv.getTextSize() * 13 / 10;
        return getEmotionContent(context, emotionType, size, source);
    }

    /**
     * 处理聊天消息中的表情
     */
    public static SpannableString getEmotionContent(Context context, EmotionGroupType emotionType, String source) {

        // 表情图片
        int size = context.getResources().getDimensionPixelSize(R.dimen.dimen_26);
        return getEmotionContent(context, emotionType, size, source);
    }

    private static SpannableString getEmotionContent(Context context, EmotionGroupType emotionType, int emotionSize, String source) {

        SpannableString spannableString = new SpannableString(source);
        Resources res = context.getResources();

//        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";
        String regexEmotion = "\\[bq_b[0-9]\\d{2}]";
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);

        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            // 利用表情名字获取到对应的图片
            Integer emotionRes = EmotionDataHelper.getEmotionValueByKey(emotionType, key);

            if (emotionRes != null) {
                // 表情图片
                Bitmap bitmap = BitmapFactory.decodeResource(res, emotionRes);
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, emotionSize, emotionSize, true);

                ImageSpan span = new ImageSpan(context, scaleBitmap);
                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }
}
