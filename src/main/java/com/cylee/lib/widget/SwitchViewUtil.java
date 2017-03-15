package com.cylee.lib.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cylee.androidlib.util.ViewUtils;
import com.cylee.lib.R;

/**
 * 用于切换显示“主页面”、“正在加载中……”、“加载出错”
 * 支持替换所有的View，只需要传入需要被替换的View的id即可替换
 */
public class SwitchViewUtil {
	
	public static final String TAG = "SwitchListViewUtil";

    private View mMainView;
    private Context mContext;
    private int mCurrentViewIndex;
    private ViewGroup.LayoutParams mLayoutParams;
    private View mLastView;
    private View.OnClickListener mOnClickListener;
    private int loadingBackgroundColor = -1;

    public enum ViewType{
        MAIN_VIEW, //主页面
        ERROR_VIEW, //“加载出错”页面
        LOADING_VIEW, //"正在加载"页面
        LOADING_ERROR_RETRY, //"资源加载失败点击重试"页面
        EMPTY_VIEW, //"加载为空"页面
        NO_NETWORK_VIEW, //"无网"页面
        NO_LOGIN_VIEW,//“未登录”页面
        CONTENT_DELETED,//被删除
        GET_QB_EMPTY, // 答题获取QB活动空白页
        EXERCISE_LOADING_VIEW,//同步练习智能出题中
    }
    
    /**
     * @param context
     * @param mainView(能够attach到当前Windows的View,即为getParent的View)
     */
    public SwitchViewUtil(Context context, View mainView) {
        this(context, mainView, null);
    }
    /**
     * @param context
     * @param mainView(能够attach到当前Windows的View,即为getParent的View)
     */
    public SwitchViewUtil(Context context, View mainView, View.OnClickListener clickListener){
    	mContext = context;
    	mMainView = mainView;
        mOnClickListener = clickListener;
        if(mMainView == null){
            throw new RuntimeException();
        }
        mCurrentViewIndex = getParentView(mMainView).indexOfChild(mMainView);
        mLayoutParams = mMainView.getLayoutParams();
    }

    public void showCustomView(int viewId){
        showCustomView(LayoutInflater.from(mContext).inflate(viewId, null));
    }

    public void showView(ViewType viewType){
        showView(viewType, null);
    }

    /**
     * 自定义空白页时调用的方法
     * @param viewType
     * @param newView
     */
    public void showView(ViewType viewType, View newView){
        View view = null;
        if(viewType.equals(ViewType.MAIN_VIEW)){
            showMainView();
        }else if(viewType.equals(ViewType.ERROR_VIEW)){
            view = LayoutInflater.from(mContext).inflate(R.layout.common_layout_listview_error, null);
            if (mOnClickListener != null) {
                view.setOnClickListener(mOnClickListener);
            }
        }else if(viewType.equals(ViewType.LOADING_VIEW)){
            if (newView == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.common_layout_listview_loading, null);
            } else {
                view = newView;
            }
            if ((loadingBackgroundColor & 0xFF000000) != 0) {
                view.setBackgroundColor(loadingBackgroundColor);
            }
        }else if(viewType.equals(ViewType.EMPTY_VIEW)){
            if (newView == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.common_layout_listview_empty, null);
            } else {
                view = newView;
            }
            if (mOnClickListener != null) {
                view.setOnClickListener(mOnClickListener);
            }
        }else if(viewType.equals(ViewType.NO_NETWORK_VIEW)){
            view = LayoutInflater.from(mContext).inflate(R.layout.common_layout_listview_no_network, null);
            if (mOnClickListener != null) {
                view.setOnClickListener(mOnClickListener);
            }
        } else if(viewType.equals(ViewType.LOADING_ERROR_RETRY)){
            if (newView == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.common_layout_load_error_retry, null);
            } else {
                view = newView;
            }
            if(mOnClickListener != null){
                view.setOnClickListener(mOnClickListener);
            }
        }
        showCustomView(view);
    }

    public void setLoadingBackgroundColor(int loadingBackgroundColor) {
        this.loadingBackgroundColor = loadingBackgroundColor;
    }

    /**
     * 显示自定义View，调用dismissCustomView()取消显示自定义View
     * @param view
     */
    public void showCustomView(View view){
    	if(view == mLastView){
    		return;
    	}
		if(mLastView != null && mLastView != mMainView){
            ViewUtils.removeView(mLastView);
            mLastView = null;
		}
    	if(view!= null && view != mMainView ){
    		mMainView.setVisibility(View.GONE);
            ViewGroup parent = getParentView(mMainView);
            if (parent != null) {
                parent.addView(view, mCurrentViewIndex, mLayoutParams);
            }
    	}else{
    		mMainView.setVisibility(View.VISIBLE);
    	}
    	mLastView = view;
    }

    public void showMainView(){
        if(mLastView != null){
            ViewUtils.removeView(mLastView);
            mLastView = null;
        }
        mMainView.setVisibility(View.VISIBLE);
    }

    private ViewGroup getParentView(View mainView){
        return (ViewGroup)mainView.getParent();
    }

}
