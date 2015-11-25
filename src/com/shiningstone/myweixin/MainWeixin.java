package com.shiningstone.myweixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.shiningstone.viewutils.*;

public class MainWeixin extends Activity {
	/*******************************************
        resource configuration
    *******************************************/
    private static int SRC_SUB_LAYOUT[] = {
    	R.layout.main_tab_weixin,
    	R.layout.main_tab_address,
    	R.layout.main_tab_friends,
    	R.layout.main_tab_settings,
    };
    
    private static int SRC_HEADER[][] = {
        {R.id.img_weixin, R.drawable.tab_weixin_normal, R.drawable.tab_weixin_pressed},
        {R.id.img_address, R.drawable.tab_address_normal, R.drawable.tab_address_pressed},
        {R.id.img_friends, R.drawable.tab_friends_normal, R.drawable.tab_friends_pressed},
        {R.id.img_settings, R.drawable.tab_settings_normal, R.drawable.tab_settings_pressed},
    };

    private static int SRC_HEADER_CURSOR = R.id.img_tab_now;

	/*******************************************
        local variables
    *******************************************/
	public static MainWeixin instance = null;
	 
    private Headers mTabHeader;
    private int TAB_WIDTH = 0;
    private int CURSOR_OFFSET = 0;
	private ViewPager mTabPager;
    private xListView mChatterList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_weixin);
         //启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        instance = this;
        
        initHeaders();
        initPageViewer();
        
        int listItems[] = {R.id.head, R.id.name, R.id.time, R.id.content};
        mChatterList = new xListView(this, R.id.chatter_list, R.layout.chatter_list_item, listItems) {
			@Override
			protected ArrayList<ArrayList<Object>> getItems() {
	            ArrayList<ArrayList<Object>> list = new ArrayList<ArrayList<Object>>();  
	            for (int i = 0; i < 10; i++) {  
	            	ArrayList<Object> elements = new ArrayList<Object>();  
	                
	            	elements.add(R.drawable.ic_launcher);  
	            	elements.add("名字"+i);  
	            	elements.add("时间"+i);  
	            	elements.add("内容"+i);  
	                
	                list.add(elements);  
	            }  
	            
	            return list;
			}
        };
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus) {
            TAB_WIDTH = getWindowManager().getDefaultDisplay().getWidth() / SRC_HEADER.length;
            CURSOR_OFFSET = (TAB_WIDTH - findViewById(SRC_HEADER_CURSOR).getWidth())/2;

            mTabHeader.choose(0);
        }
    }

	//设置标题栏右侧按钮的作用
	public void btnmainright(View v) {  
		//Intent intent = new Intent (MainWeixin.this,MainTopRightDialog.class);			
		//startActivity(intent);	
      }  	
	public void startchat(View v) {      //小黑  对话界面
		//Intent intent = new Intent (MainWeixin.this,ChatActivity.class);			
		//startActivity(intent);	
      }  
	public void exit_settings(View v) {                           //退出  伪“对话框”，其实是一个activity
		//Intent intent = new Intent (MainWeixin.this,ExitFromSettings.class);			
		//startActivity(intent);	
	 }
	public void btn_shake(View v) {                                   //手机摇一摇
		//Intent intent = new Intent (MainWeixin.this,ShakeActivity.class);			
		//startActivity(intent);	
	}
    
	/*******************************************
        key actions
    *******************************************/
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return _onBackPressed();
    	} else if(keyCode == KeyEvent.KEYCODE_MENU){
            return false;
		}
        
    	return false;
    }

    protected boolean _onBackPressed() {
        //Intent intent = new Intent();
        //intent.setClass(MainWeixin.this,Exit.class);
        //startActivity(intent);

        return true;
    }

	/*******************************************
        header actions
    *******************************************/
    private void initHeaders() {
        mTabHeader = new Headers();

        for(int i=0; i<SRC_HEADER.length; i++) {
            mTabHeader.add(findViewById(SRC_HEADER[i][0]), SRC_HEADER[i][1], SRC_HEADER[i][2]);
        }

        mTabHeader.addCursor(findViewById(SRC_HEADER_CURSOR));
    }

    private class Headers {
        private class Header {
            private ImageView mIvView;
            private int mId;
            private int mNormalRes;
            private int mPressedRes;
            
            public Header(int idx, View view, int normalRes, int pressedRes) {
                mId = idx;
                mIvView = (ImageView)view;
                mNormalRes = normalRes;
                mPressedRes = pressedRes;

                mIvView.setOnClickListener(new HeaderClickListener());
            }

            public void setChoose(boolean flag) {
                int resId = flag==true ? mPressedRes : mNormalRes;
                mIvView.setImageDrawable( getResources().getDrawable(resId) );
            }
        };
        
        private ArrayList<Header> mList = new ArrayList<Header>();
        private ImageView mIvCursor;
        private int mChosen = 0;
        
        public void add(View view, int normalRes, int pressedRes) {
            mList.add(new Header(mList.size(), view, normalRes, pressedRes));
        }

        public void addCursor(View view) {
            mIvCursor = (ImageView)view;
        }

        public boolean choose(int target) {
            moveCursor(mChosen, target);
            
            if(mChosen!=target) {
                for(Header header : mList) {
                    if(header.mId==target) {
                        header.setChoose(true);
                    } else {
                        header.setChoose(false);
                    }
                }

                mChosen = target;
                return true;
            } else {
                return false;
            }
        }

        private void moveCursor(int from, int to) {
            Animation animation = new TranslateAnimation(TAB_WIDTH*from+CURSOR_OFFSET, TAB_WIDTH*to+CURSOR_OFFSET, 0, 0);
            animation.setFillAfter(true);
            animation.setDuration(150);
            mIvCursor.startAnimation(animation);
        }
        
    	private class HeaderClickListener implements View.OnClickListener {
    		@Override
    		public void onClick(View v) {
    		    for(Header header : mList) {
                    if(header.mIvView==v) {
                        mTabPager.setCurrentItem(header.mId);
                        
                        switch(header.mId) {
	                        case 0:
	                        	mChatterList.update();
	                        	break;
                        }
                    }
                }      
    		}
    	};
    };

	/*******************************************
        page viewer actions
    *******************************************/
    private void initPageViewer() {
        mTabPager = (ViewPager)findViewById(R.id.tabpager);
        mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
        
        final ArrayList<View> views = new ArrayList<View>();
        LayoutInflater mLi = LayoutInflater.from(this);
        for(int i=0; i<SRC_SUB_LAYOUT.length; i++) {
            views.add(mLi.inflate(SRC_SUB_LAYOUT[i], null));
        }

        PagerAdapter mPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		
		mTabPager.setAdapter(mPagerAdapter);
    }
	private class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
            mTabHeader.choose(arg0);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
}
    
    

