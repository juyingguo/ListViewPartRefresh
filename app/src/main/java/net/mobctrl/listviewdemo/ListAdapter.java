/*
 * $filename: ListAdapter.java,v $
 * $Date: 2014-9-19  $
 * Copyright (C) ZhengHaibo, Inc. All rights reserved.
 * This software is Made by Zhenghaibo.
 */
package net.mobctrl.listviewdemo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;

/*
 *@author: ZhengHaibo  
 *blog:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *web:     http://www.mobctrl.net
 *2014-9-19  Nanjing,njupt,China
 */
public class ListAdapter extends BaseAdapter {
	private  String TAG = ListAdapter.class.getSimpleName();
	private List<Model> datas;
	private Context context;
	
	private UpdateCallback updateCallback;
	
	public UpdateCallback getUpdateCallback() {
		return updateCallback;
	}

	public void setUpdateCallback(UpdateCallback updateCallback) {
		this.updateCallback = updateCallback;
	}

	public ListAdapter(Context context) {
		datas = new ArrayList<Model>();
		this.context = context;
	}

	public void addData(Model model) {
		datas.add(model);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return datas.get(pos);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(final int pos, View convertView, ViewGroup viewGroup) {
		final Model model = datas.get(pos);
		ViewHolder viewHolder;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item_layout, null);
			viewHolder.pb = (ProgressBar) convertView
					.findViewById(R.id.pb_show);
			viewHolder.btn = (Button) convertView.findViewById(R.id.btn);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			convertView.setTag(viewHolder);
		}
		Log.i(TAG, ">>getView>>pos:" + pos + ",convertView.hashCode():" + convertView.hashCode());
		viewHolder.btn.setText(model.getName() + ":" + model.getId());
		viewHolder.pb.setProgress(model.getProgress());
		viewHolder.btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (model.getProgress()>0){ //有进度时直接返回
					return;
				}

				if(null != updateCallback){
					updateCallback.startProgress(model,pos);
				}
			}
		});
		// cache the view
		return convertView;
	}
	
	public static class ViewHolder {
		ProgressBar pb;
		Button btn;
	}

}
