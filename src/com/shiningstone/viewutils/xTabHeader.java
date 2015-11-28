package com.shiningstone.viewutils;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/*****************************************************
    类似微信的单选面板，需要提供的信息包括:
        Activity
        选中标识的ImageView控件ID
        每个按钮的ImageView控件ID、平常状态的资源ID、选中状态的资源ID
*****************************************************/
public abstract class xTabHeader {
    /***************************
        单个按钮 
    ***************************/
    private class Icon {
        private ImageView idInParent; /* Activity的layout上的控件ID : R.id.xxx */
        private int normalRes;        /* 平常状态对应的资源ID :  R.drawable.xxx */
        private int pressedRes;       /* 选中状态对应的资源ID :  R.drawable.xxx */
        private int _No;              /* 按钮在面板上的顺序 */
        
        public Icon(int no, View view, int normalRes, int pressedRes) {
            this._No = no;
            this.idInParent = (ImageView)view;
            this.normalRes = normalRes;
            this.pressedRes = pressedRes;

            idInParent.setOnClickListener(new HeaderClickListener());
        }

        public void setChoose(boolean flag) {
            int resId = flag==true ? pressedRes : normalRes;
            idInParent.setImageDrawable( mParent.getResources().getDrawable(resId) );
        }
    };

    private Activity mParent;           /* Activity */
    private ImageView mIvIndicator;     /* 选中标识的ImageView控件 */
    private ArrayList<Icon> mIcons = new ArrayList<Icon>();
    
    private int TAB_WIDTH = 0;
    private int CURSOR_OFFSET = 0;
    private int _currentNo = 0;

    /***************************
        初始化接口函数 ( 需注意调用时机 )
    ***************************/
    public xTabHeader(Activity parent) {
        mParent = parent;
    }

    public void add(View view, int normalRes, int pressedRes) {
        mIcons.add(new Icon(mIcons.size(), view, normalRes, pressedRes));
    }

    public void addIndicator(View view) {
        mIvIndicator = (ImageView)view;
    }

    public void init(int cursorWidth) {
        TAB_WIDTH = mParent.getWindowManager().getDefaultDisplay().getWidth() / mIcons.size();
        CURSOR_OFFSET = (TAB_WIDTH-cursorWidth)/2;

        choose(0);
    }
    
    /***************************
        动作接口函数
    ***************************/
    public boolean choose(int target) {
        moveIndicator(_currentNo, target);
        
        if(_currentNo!=target) {
            for(Icon header : mIcons) {
                if(header._No==target) {
                    header.setChoose(true);
                } else {
                    header.setChoose(false);
                }
            }

            _currentNo = target;
            return true;
        } else {
            return false;
        }
    }

    /***************************
        回调函数，主要用于切换ViewPager 
    ***************************/
    abstract protected void onTabHeaderClick(int tabId);

    private void moveIndicator(int from, int to) {
        Animation animation = new TranslateAnimation(TAB_WIDTH*from+CURSOR_OFFSET, TAB_WIDTH*to+CURSOR_OFFSET, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(150);
        mIvIndicator.startAnimation(animation);
    }

    private class HeaderClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            for(Icon header : mIcons) {
                if(header.idInParent==v) {
                    onTabHeaderClick(header._No);
                }
            }      
        }
    };
};
