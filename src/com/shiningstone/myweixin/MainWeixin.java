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
         //����activityʱ���Զ����������
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
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  //��ȡ back��
    		
        	if(menu_display){         //��� Menu�Ѿ��� ���ȹر�Menu
        		menuWindow.dismiss();
        		menu_display = false;
        		}
        	else {
        		//Intent intent = new Intent();
            	//intent.setClass(MainWeixin.this,Exit.class);
            	//startActivity(intent);
        	}
    	}
    	
    	else if(keyCode == KeyEvent.KEYCODE_MENU){   //��ȡ Menu��			
			if(!menu_display){
				//��ȡLayoutInflaterʵ��
				inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
				//�����main��������inflate�м����Ŷ����ǰ����ֱ��this.setContentView()�İɣ��Ǻ�
				//�÷������ص���һ��View�Ķ����ǲ����еĸ�
				layout = inflater.inflate(R.layout.main_menu, null);
				
				//��������Ҫ�����ˣ����������ҵ�layout���뵽PopupWindow���أ������ܼ�
				menuWindow = new PopupWindow(layout,LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); //������������width��height
				//menuWindow.showAsDropDown(layout); //���õ���Ч��
				//menuWindow.showAsDropDown(null, 0, layout.getHeight());
				menuWindow.showAtLocation(this.findViewById(R.id.mainweixin), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //����layout��PopupWindow����ʾ��λ��
				//��λ�ȡ����main�еĿؼ��أ�Ҳ�ܼ�
				mClose = (LinearLayout)layout.findViewById(R.id.menu_close);
				mCloseBtn = (LinearLayout)layout.findViewById(R.id.menu_close_btn);
				
				
				//�����ÿһ��Layout���е����¼���ע��ɡ�����
				//���絥��ĳ��MenuItem��ʱ�����ı���ɫ�ı�
				//����׼����һЩ����ͼƬ������ɫ
				mCloseBtn.setOnClickListener (new View.OnClickListener() {					
					@Override
					public void onClick(View arg0) {						
						//Intent intent = new Intent();
			        	//intent.setClass(MainWeixin.this,Exit.class);
			        	//startActivity(intent);
			        	menuWindow.dismiss(); //��Ӧ����¼�֮��ر�Menu
					}
				});				
				menu_display = true;				
			}else{
				//�����ǰ�Ѿ�Ϊ��ʾ״̬������������
				menuWindow.dismiss();
				menu_display = false;
				}
			
			return false;
		}
    	return false;
    }
	//���ñ������Ҳఴť������
	public void btnmainright(View v) {  
		//Intent intent = new Intent (MainWeixin.this,MainTopRightDialog.class);			
		//startActivity(intent);	
      }  	
	public void startchat(View v) {      //С��  �Ի�����
		//Intent intent = new Intent (MainWeixin.this,ChatActivity.class);			
		//startActivity(intent);	
      }  
	public void exit_settings(View v) {                           //�˳�  α���Ի��򡱣���ʵ��һ��activity
		//Intent intent = new Intent (MainWeixin.this,ExitFromSettings.class);			
		//startActivity(intent);	
	 }
	public void btn_shake(View v) {                                   //�ֻ�ҡһҡ
		//Intent intent = new Intent (MainWeixin.this,ShakeActivity.class);			
		//startActivity(intent);	
	}
}
    
    

