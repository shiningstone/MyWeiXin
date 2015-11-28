package com.shiningstone.viewutils;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/*****************************************************
    ����΢�ŵĵ�ѡ��壬��Ҫ�ṩ����Ϣ����:
        Activity
        ѡ�б�ʶ��ImageView�ؼ�ID
        ÿ����ť��ImageView�ؼ�ID��ƽ��״̬����ԴID��ѡ��״̬����ԴID
*****************************************************/
public abstract class xTabHeader {
    /***************************
        ������ť 
    ***************************/
    private class Icon {
        private ImageView idInParent; /* Activity��layout�ϵĿؼ�ID : R.id.xxx */
        private int normalRes;        /* ƽ��״̬��Ӧ����ԴID :  R.drawable.xxx */
        private int pressedRes;       /* ѡ��״̬��Ӧ����ԴID :  R.drawable.xxx */
        private int _No;              /* ��ť������ϵ�˳�� */
        
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
    private ImageView mIvIndicator;     /* ѡ�б�ʶ��ImageView�ؼ� */
    private ArrayList<Icon> mIcons = new ArrayList<Icon>();
    
    private int TAB_WIDTH = 0;
    private int CURSOR_OFFSET = 0;
    private int _currentNo = 0;

    /***************************
        ��ʼ���ӿں��� ( ��ע�����ʱ�� )
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
        �����ӿں���
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
        �ص���������Ҫ�����л�ViewPager 
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
