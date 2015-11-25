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
	Activity mParent;         /* the activity use ListView */
	int      mParentsList;    /* R.id.xxx,     the resource id of the ListView in the layout of activity */
	int      mLayout;         /* R.layout.xxx  the layout of the ListView */
	int[]    mElements;       /* R.id.xxx      each element need to be draw in the layout of the ListView */
	
	String[] xElements;       /* local variable */
	
	public xListView(Activity parent, int list, int layout, int[] elements) {
		mParent = parent;
		mParentsList = list;
		mLayout = layout;
		mElements = new int[elements.length];
		xElements = new String[elements.length];
		
		for(int i=0;i<elements.length;i++) {
			mElements[i] = elements[i];
			xElements[i] = "tag" + i;
		}
	}
	
	public void update() {
		ArrayList<ArrayList<Object>> list = getItems();
		
		ArrayList<HashMap<String,Object>> listItems = new ArrayList<HashMap<String,Object>>();
		for(int i=0;i<list.size();i++) {
			HashMap<String,Object> map = new HashMap<String,Object>();
			
			for(int j=0;j<mElements.length;j++) {
				map.put(xElements[j], list.get(i).get(j));
			}
			
			listItems.add(map);
		}
		
        ListView lv = (ListView) mParent.findViewById(mParentsList);
        lv.setAdapter(new SimpleAdapter(mParent, listItems, mLayout, xElements, mElements));
        lv.setOnItemClickListener(new onClickListener());
	}
	
    protected abstract ArrayList<ArrayList<Object>> getItems();

    private class onClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
        /*
         * ����б���ʱ����onItemClick�������ĸ���������ֱ�Ϊ
         * arg0�����������¼���AdapterView
         * arg1��AdapterView�б������View 
         * position����ǰ���������adapter���±�
         * id����ǰ������е�id
         */
        	Log.v("tag","list item clicked");
        }
    };
}