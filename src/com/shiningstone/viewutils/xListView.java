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
    public final class Desc {
        Activity parent;         /* the activity use this ListView */
        int      idInParent;     /* R.id.xxx,     the resource id of the ListView in the layout of activity */
        int      layout;         /* R.layout.xxx  the layout of the ListView */
        int[]    elements;       /* R.id.xxx      each element need to be draw in the layout of the ListView */
    };
	
    Desc     _view;
	String[] xElements;          /* local variable */
    
	public xListView(Activity parent, int listId, int layout, int[] elements) {
		_view = new Desc();
		
		_view.parent = parent;
		_view.idInParent = listId;
		_view.layout = layout;
		_view.elements = new int[elements.length];

        xElements = new String[elements.length];
		
		for(int i=0;i<elements.length;i++) {
			_view.elements[i] = elements[i];
			xElements[i] = "tag" + i;
		}
	}
	
	public void update() {
		ArrayList<ArrayList<Object>> list = getItems();
		
		ArrayList<HashMap<String,Object>> listItems = new ArrayList<HashMap<String,Object>>();
		for(int i=0;i<list.size();i++) {
			HashMap<String,Object> map = new HashMap<String,Object>();
			
			for(int j=0;j<_view.elements.length;j++) {
				map.put(xElements[j], list.get(i).get(j));
			}
			
			listItems.add(map);
		}
		
        ListView lv = (ListView) _view.parent.findViewById(_view.idInParent);
        lv.setAdapter(new SimpleAdapter(_view.parent, listItems, _view.layout, xElements, _view.elements));
        lv.setOnItemClickListener(new onClickListener());
	}
	
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
