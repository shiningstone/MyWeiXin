package com.shiningstone.myweixin;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainWeixin extends Activity {
	/*******************************************
        resource configruation
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
	private ViewPager mTabPager;	

	private LinearLayout mClose;
    private LinearLayout mCloseBtn;
    private View layout;	
	private boolean menu_display = false;
	private PopupWindow menuWindow;
	private LayoutInflater inflater;
	//private Button mRightBtn;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_weixin);
         //启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        instance = this;
        
        initHeaders();
        initPageViewer();
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
            private ImageView mView;
            private int mId;
            private int mNormalRes;
            private int mPressedRes;
            
            public Header(int idx, View view, int normalRes, int pressedRes) {
                mId = idx;
                mView = (ImageView)view;
                mNormalRes = normalRes;
                mPressedRes = pressedRes;

                mView.setOnClickListener(new HeaderClickListener());
            }

            public void setChoose(boolean flag) {
                int resId = flag==true ? mPressedRes : mNormalRes;
                mView.setImageDrawable( getResources().getDrawable(resId) );
            }
        };
        
        private ArrayList<Header> mList = new ArrayList<Header>();
        private ImageView mCursor;
        private int TAB_WIDTH = 0;
        private int mChosen = 0;
        
        public void add(View view, int normalRes, int pressedRes) {
            mList.add(new Header(mList.size(), view, normalRes, pressedRes));
        }

        public void addCursor(View view) {
            mCursor = (ImageView)view;

            TAB_WIDTH = getWindowManager().getDefaultDisplay().getWidth() / mList.size();
        }

        public boolean choose(int target) {
            if(mChosen!=target) {
                moveCursor(mChosen, target);

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
            int offset = (TAB_WIDTH - mCursor.getWidth())/2;

            Animation animation = new TranslateAnimation(TAB_WIDTH*from+offset, TAB_WIDTH*to+offset, 0, 0);
            animation.setFillAfter(true);
            animation.setDuration(150);
            mCursor.startAnimation(animation);
        }
        
    	private class HeaderClickListener implements View.OnClickListener {
    		@Override
    		public void onClick(View v) {
    		    for(Header header : mList) {
                    if(header.mView==v) {
                        mTabPager.setCurrentItem(header.mId);
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
	



	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  //获取 back键
    		
        	if(menu_display){         //如果 Menu已经打开 ，先关闭Menu
        		menuWindow.dismiss();
        		menu_display = false;
        		}
        	else {
        		//Intent intent = new Intent();
            	//intent.setClass(MainWeixin.this,Exit.class);
            	//startActivity(intent);
        	}
    	}
    	
    	else if(keyCode == KeyEvent.KEYCODE_MENU){   //获取 Menu键			
			if(!menu_display){
				//获取LayoutInflater实例
				inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
				//这里的main布局是在inflate中加入的哦，以前都是直接this.setContentView()的吧？呵呵
				//该方法返回的是一个View的对象，是布局中的根
				layout = inflater.inflate(R.layout.main_menu, null);
				
				//下面我们要考虑了，我怎样将我的layout加入到PopupWindow中呢？？？很简单
				menuWindow = new PopupWindow(layout,LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); //后两个参数是width和height
				//menuWindow.showAsDropDown(layout); //设置弹出效果
				//menuWindow.showAsDropDown(null, 0, layout.getHeight());
				menuWindow.showAtLocation(this.findViewById(R.id.mainweixin), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
				//如何获取我们main中的控件呢？也很简单
				mClose = (LinearLayout)layout.findViewById(R.id.menu_close);
				mCloseBtn = (LinearLayout)layout.findViewById(R.id.menu_close_btn);
				
				
				//下面对每一个Layout进行单击事件的注册吧。。。
				//比如单击某个MenuItem的时候，他的背景色改变
				//事先准备好一些背景图片或者颜色
				mCloseBtn.setOnClickListener (new View.OnClickListener() {					
					@Override
					public void onClick(View arg0) {						
						//Intent intent = new Intent();
			        	//intent.setClass(MainWeixin.this,Exit.class);
			        	//startActivity(intent);
			        	menuWindow.dismiss(); //响应点击事件之后关闭Menu
					}
				});				
				menu_display = true;				
			}else{
				//如果当前已经为显示状态，则隐藏起来
				menuWindow.dismiss();
				menu_display = false;
				}
			
			return false;
		}
    	return false;
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
}
    
    

