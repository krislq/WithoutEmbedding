package com.krislq.we;

import android.app.Activity;
import android.app.Application;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 2016/11/7 11:32.
 * Packaged in WithoutEmbedding:com.krislq.we.
 */

public class WeLifecycleCallBacks implements Application.ActivityLifecycleCallbacks {
    private static WeLifecycleCallBacks mWeLifecycleCallBacks;
    private final String TAG = this.getClass().getSimpleName();

    private final List<View> mViews = new ArrayList<>();
    private WeakReference activityRefer = null;

    public static WeLifecycleCallBacks getInstance(){
        if(mWeLifecycleCallBacks==null) {
            mWeLifecycleCallBacks = new WeLifecycleCallBacks();
        }
        return mWeLifecycleCallBacks;
    }

    private WeLifecycleCallBacks() {

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.e(TAG,"onActivityCreated ->"+activity.getClass().getSimpleName());

    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.e(TAG,"onActivityStarted ->"+activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.e(TAG,"onActivityResumed ->"+activity.getClass().getSimpleName());
        this.activityRefer = new WeakReference(activity);
        View decorView = activity.getWindow().getDecorView();
        mViews.clear();
        //找出两个页面中的view
        iteratorView(decorView);

        if(decorView instanceof  ViewGroup ) {
            ViewGroup decorViewGroup = (ViewGroup)decorView;
            View fistView = decorViewGroup.getChildAt(0);
            if(!(fistView instanceof CoverFrameLayout)) {
                creatCoverFrameLayout(decorViewGroup);
            }
        }
    }

    private CoverFrameLayout creatCoverFrameLayout(ViewGroup decorView) {
        if(activityRefer.get()==null) return null;
        //TODO 在这里可能需要不根据不同的情况返回不同的这个Layout.比如frgment ? popWIndows 等等
        return new CoverFrameLayout((Activity)activityRefer.get(), decorView);
    }

    private void iteratorView(View view) {
        Log.e(TAG,"iteratorView "+view.getClass().getSimpleName());
        if(view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup)view;
            int childCount = vg.getChildCount();
            for(int i=0;i<childCount;i++) {
                iteratorView(vg.getChildAt(i));
            }
        } else {
            //TODO 在这里如果需要项目化功能,还需要结合自身项目的功能和统计规则对一些特殊的控件的保存方式。
            //TODO 比如:adapterView itemview上面的点击? 还得考虑weview的事件?对纯绘制View的事件?
            //TODO 自己项目统计规则 是怎么样的? 可能保存view的不只是Set ,甚至可能需要扩充至Map等
            mViews.add(view);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e(TAG,"onActivityPaused ->"+activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.e(TAG,"onActivityStopped ->"+activity.getClass().getSimpleName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Log.e(TAG,"onActivitySaveInstanceState ->"+activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e(TAG,"onActivityDestroyed ->"+activity.getClass().getSimpleName());
    }

    public View touchAnyView(MotionEvent motionEvent){
        if(activityRefer.get()==null) return null;
        View touchView = findTouchView(motionEvent);
        if(touchView==null) {
            Toast.makeText((Activity)activityRefer.get(),"点击到空白区域",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText((Activity)activityRefer.get(),findTouchViewTag(touchView),Toast.LENGTH_SHORT).show();
        }

        return touchView;
    }

    private View findTouchView(MotionEvent motionEvent) {
        int[] location = new int[2];
        for (View v:mViews) {
            if(v.isShown()) {
                v.getLocationOnScreen(location);
                Rect r = new Rect();
                v.getGlobalVisibleRect(r);
                boolean contains = r.contains((int)motionEvent.getX(),(int)motionEvent.getY());
                if(contains) {
                    //TODO 在这里如果需要项目化功能,还需要对此view是否可见?是否可点进行一些判断,再确定用户点击的是哪个View
                    return v;
                }
            }
        }
        return null;
    }

    private String findTouchViewTag(View v) {
        if(v==null) return "View is null";
        if(v instanceof TextView) {
            if(v instanceof CheckBox) {
                CheckBox cb = (CheckBox) v;
                return "CheckBox, text is:"+cb.getText();
            } else if(v instanceof Button){
                Button btn = (Button) v;
                return "Button, text is:" + btn.getText();
            } else if(v instanceof EditText) {
                EditText et = (EditText) v;
                return "EditText, text is:"+et.getText();
            }else {
                TextView tv = (TextView) v;
                return "TextView, text is:" + tv.getText().toString();
            }
        } else if(v instanceof ImageView) {
            ImageView im = (ImageView) v;
            return "ImageView, contentDrscription is:"+im.getContentDescription();
        }else if(v instanceof Spinner) {
            Spinner spinner = (Spinner) v;
            return "Spinner, item is:"+spinner.getCount();
        }
        return  "No Identity";
    }
}
