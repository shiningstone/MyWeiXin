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
    int      controlId;        /* Activity��layout�ϵ�ListView�ؼ� */
    int      layout;         /* ListView��layout */
    int[]    elements;       /* ListView layout�ϵ�ÿ��Ԫ��id */
	
	String[] xElements;

    /***************************
        ��ʼ���ӿں���
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
        ���ƺ���
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
        �ص��������ṩ����list�������Դ
    ***************************/
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
