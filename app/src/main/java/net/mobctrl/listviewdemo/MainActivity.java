package net.mobctrl.listviewdemo;

import net.mobctrl.listviewdemo.ListAdapter.ViewHolder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * @author 郑海波
 * @webset http://www.mobctrl.net
 * ListView的局部刷新
 */
//@EActivity(R.layout.activity_main)
public class MainActivity extends Activity implements UpdateCallback{

//	@ViewById
	ListView listview;
	
	private ListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listview = (ListView) findViewById(R.id.listview);
		afterViews();
	}

//	@AfterViews
	void afterViews() {
		adapter = new ListAdapter(this);
		adapter.setUpdateCallback(this);
		listview.setAdapter(adapter);
		initDatas();
	}

	private void initDatas() {
		for(int i = 0;i<30;i++){
			Model model = new Model(i, "点击");
			adapter.addData(model);
		}
	}

	@Override
	public void startProgress(final Model model,final int position) {
		/** start the Thread to update the Progress*/
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i = 0;i<=100;i+=20){
					model.setProgress(i);
					updateProgressInUiThread(model, i,position);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
//	@UiThread
	void updateProgressInUiThread(Model model, final int progress, final int position){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
//				updateProgressPartly(progress,position);
		//		updateProgressPartly2(model,progress,position);
				updateSingleRow(listview,position);

			}
		});
	}
	
	
	private void updateProgressPartly(int progress,int position){
		int firstVisiblePosition = listview.getFirstVisiblePosition();
		int lastVisiblePosition = listview.getLastVisiblePosition();
		if(position>=firstVisiblePosition && position<=lastVisiblePosition){
			View view = listview.getChildAt(position - firstVisiblePosition);
			if(view.getTag() instanceof ViewHolder){
				ViewHolder vh = (ViewHolder)view.getTag();
				vh.pb.setProgress(progress);
			}
		}
	}
	private void updateProgressPartly2(Model model, int progress, int position){
		int firstVisiblePosition = listview.getFirstVisiblePosition();
		View view = listview.getChildAt(position - firstVisiblePosition);
		if (view != null){
			ProgressBar pb = (ProgressBar) view.findViewById(R.id.pb_show);
			Model modelTag = (Model) pb.getTag();
			if (modelTag!= null && modelTag.getId()  == model.getId() ){
				pb.setProgress(progress);
			}

		}
	}

	/**
	 * listview局部刷新
	 * @param listView
	 * @param id
	 */
	private void updateSingleRow(ListView listView, long id) {

		if (listView != null) {
			int start = listView.getFirstVisiblePosition();
			for (int i = start, j = listView.getLastVisiblePosition(); i <= j; i++)
				if (id == ((Model) listView.getItemAtPosition(i)).getId()) {
					View view = listView.getChildAt(i - start);
					adapter.getView(i, view, listView);
					break;
				}
		}
	}

}
