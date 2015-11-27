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
	 
    private xTabHeader mTabHeader;
	private ViewPager mTabPager;
    private xListView mChatterList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_weixin);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //启动activity时不自动弹出软键盘
        instance = this;
        
        createTabHeaders();
        createPageViewer();
        createChatterList();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus) {
            mTabHeader.init(findViewById(SRC_HEADER_CURSOR).getWidth());
            mChatterList.update();
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
    private void createTabHeaders() {
        mTabHeader = new xTabHeader(this) {
			protected void onTabHeaderClick(int tabId) {
                mTabPager.setCurrentItem(tabId);
                
                switch(tabId) {
                    case 0:
                        mChatterList.update();
                        break;
                }
            }
        };

        for(int i=0; i<SRC_HEADER.length; i++) {
            mTabHeader.add(findViewById(SRC_HEADER[i][0]), SRC_HEADER[i][1], SRC_HEADER[i][2]);
        }

        mTabHeader.addCursor(findViewById(SRC_HEADER_CURSOR));
    }

	/*******************************************
        page viewer actions
    *******************************************/
    private void createPageViewer() {
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
	
	/*******************************************
        list view
    *******************************************/
    private void createChatterList() {
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
}
    
    

