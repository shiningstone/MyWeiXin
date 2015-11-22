
package com.shiningstone.myweixin;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ControlOption extends RelativeLayout {
	private static int SRC_BG[] = {
		R.drawable.preference_single_item,
		R.drawable.preference_first_item,
		R.drawable.preference_item,
		R.drawable.preference_last_item,
	};
	
	private ImageView mImage;
	private TextView mText;
	
	public ControlOption(Context context, AttributeSet attrs) {
		super(context,attrs);
		LayoutInflater.from(context).inflate(R.layout.control_option, this, true);  
        
		mImage = (ImageView)findViewById(R.id.image);
        mImage.setVisibility(GONE);
		mText = (TextView)findViewById(R.id.text);
        
        int resourceId = -1;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ControlOption);
        
        int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = typedArray.getIndex(i);
            
            switch (attr) {
	            case R.styleable.ControlOption_Mode:
	                resourceId = typedArray.getInt(R.styleable.ControlOption_Mode, 0);
	                this.setBackgroundResource(SRC_BG[resourceId]);
	                break;
	            case R.styleable.ControlOption_Text:
	                resourceId = typedArray.getResourceId(R.styleable.ControlOption_Text, 0);
	                mText.setText(resourceId > 0 ? 
	                		typedArray.getResources().getText(resourceId) : 
		        			typedArray.getString(R.styleable.ControlOption_Text));
	                break;
	            case R.styleable.ControlOption_Img:
	                resourceId = typedArray.getResourceId(R.styleable.ControlOption_Img, 0);
	                if(resourceId>0) {
		                mImage.setVisibility(VISIBLE);
		                mImage.setImageResource(resourceId);
	                }
	                break;   
            }
        }
        
        typedArray.recycle();
    }
	
};
