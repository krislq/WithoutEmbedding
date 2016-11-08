package com.krislq.we;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by kris on 2016/11/7 17:26.
 * Packaged in WithoutEmbedding:com.krislq.we.
 */

public class CoverFrameLayout extends FrameLayout {
    private final String TAG = this.getClass().getSimpleName();
    public CoverFrameLayout(Context context, ViewGroup decorView) {
        super(context);

        View firstView = decorView.getChildAt(0);
        //TODO 判断有没有动画
        View focusView = firstView.findFocus();
        decorView.removeViewAt(0);
        //把第一个view加到当前view之下去
        addView(firstView);
        //再把当前view加到decorview下面去
        decorView.addView(this,0);
        if(focusView!=null) {
            focusView.requestFocus();
        }
        //TODO 如果上面有去判断有动画,在这里需要去恢复
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG,"onInterceptTouchEvent "+ev.getAction());

        //在这里能拿到所有的事件,不能在这个布局中去监听touch,因为有可能底层如果处理了touch ,是不会到达这个布局来的
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                //去看落在哪个view上面了,在这里可能得根据不同的控件 作一些特殊化的处理
                //比如:如果想监听落在没有设置click事件的textView上面的点击,那么可能去ACTION_UP上面监听
                //比如Spinner整体区域,可能也得想其它的办法
                //另外还有对滚动, 滚动,当前可视区域等的复杂统计判断
                WeLifecycleCallBacks.getInstance().touchAnyView(ev);
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
