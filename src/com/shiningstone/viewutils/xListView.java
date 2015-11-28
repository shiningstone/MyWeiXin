package com.shiningstone.viewutils;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public abstract class xListView {
    Activity parent;         
    int      controlId;        /* Activity的layout上的ListView控件 */
    int      layout;         /* ListView的layout */
    int[]    elements;       /* ListView layout上的每个元素id */
	
	String[] xElements;

    /***************************
        初始化接口函数
    ***************************/
	public xListView(Activity parent, int controlId, int layout, int[] elements) {
		this.parent = parent;
		this.controlId = controlId;
		this.layout = layout;
		this.elements = new int[elements.length];

        xElements = new String[elements.length];
		
		for(int i=0;i<elements.length;i++) {
			this.elements[i] = elements[i];
			xElements[i] = "tag" + i;
		}
	}
	
    /***************************
        绘制函数
    ***************************/
	public void update() {
		ArrayList<ArrayList<Object>> list = getItems();
		
		ArrayList<HashMap<String,Object>> listItems = new ArrayList<HashMap<String,Object>>();
		for(int i=0;i<list.size();i++) {
			HashMap<String,Object> map = new HashMap<String,Object>();
			
			for(int j=0;j<elements.length;j++) {
				map.put(xElements[j], list.get(i).get(j));
			}
			
			listItems.add(map);
		}

        ListView control = (ListView) parent.findViewById(controlId);
        control.setAdapter(new SimpleAdapter(parent, listItems, layout, xElements, elements));
        control.setOnItemClickListener(new onClickListener());
	}
	
    /***************************
        回调函数，提供绘制list所需的资源
    ***************************/
    protected abstract ArrayList<ArrayList<Object>> getItems();

    private class onClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
        /*
         * 点击列表项时触发onItemClick方法，四个参数含义分别为
         * arg0：发生单击事件的AdapterView
         * arg1：AdapterView中被点击的View 
         * position：当前点击的行在adapter的下标
         * id：当前点击的行的id
         */
        	Log.v("tag","list item clicked");
        }
    };
}
