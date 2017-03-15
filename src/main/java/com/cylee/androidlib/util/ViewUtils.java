package com.cylee.androidlib.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 提供一些常见的操作View的函数
 */
public class ViewUtils {
    public static final View.OnTouchListener EMPTY_TOUCH = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };

    public static void eatTouch(View view) {
        if (view != null) {
            view.setOnTouchListener(EMPTY_TOUCH);
        }
    }

    /**
     * 延迟Remove掉View
     * @param duration 延迟时间
     * @param view
     */
    public static void removeLater(long duration,final View view){
        AlphaAnimation animation = new AlphaAnimation(1,0);
        animation.setDuration(500);
        animation.setStartOffset(duration);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                removeView(view);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(animation);
    }
    public static void removeView(View view){
        if(view != null) {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
            }
        }
    }
    public static void setBackground(View view, Drawable drawable){
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
            view.setBackground(drawable);
        }else{
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 展示软键盘
     * @param input
     * @param context
     */
    public static void showSoftInput(final EditText input, final Context context) {
        input.post(new Runnable() {
            @Override
            public void run() {
                input.requestFocus();
                input.setFocusable(true);
                if (input.getWindowVisibility() == View.VISIBLE) {
                    input.post(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.showSoftInput(input, 0);
                        }
                    });
                }
            }
        });
    }

    /**
     * 隐藏软键盘
     * @param input
     * @param context
     */
    public static void hideSoftInput(final EditText input, final Context context) {
        input.clearFocus();
        if (input.getWindowVisibility() == View.VISIBLE) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
        }
    }
}
