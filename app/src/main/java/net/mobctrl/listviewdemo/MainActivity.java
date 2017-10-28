package net.mobctrl.listviewdemo;

import net.mobctrl.listviewdemo.ListAdapter.ViewHolder;
import net.mobctrl.listviewdemo.service.MyService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
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
	private String TAG = MainActivity.class.getSimpleName();
//	@ViewById
	ListView listview;
	
	private ListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listview = (ListView) findViewById(R.id.listview);
		afterViews();
		showMemory();

		// 绑定服务端
		Intent intent = new Intent(this, MyService.class);
		bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
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
			if(i ==0 ){
				model.setTts("深圳,2017年10月23日星期一,天气晴,最高气温26摄氏度,最低气温13摄氏度,当前气温24摄氏度,风向北风,最大风速5到6级,空气质量指数95,良好,适合外出,不过少数异常敏感人群应减少外出\n");
			}else  if (i ==1){
				model.setTts("小爱是笑话大王，给你讲一个吧。小红和爷爷在一起看电视。这时，屏幕上出现了一架直升飞机。“直升飞机上一定很热。”小红对爷爷说。“你怎么知道？”爷爷问。小红说：“如果不热，这飞机上怎么装着那么大的电风扇呢？\n");

			}
			else  if (i ==2){
				model.setTts("是的，动物有自己的语言。所有的生物，都有自己的交流方式，有看得到的，有看不到的，有听得到的，有听不到的。生命活动本身就伴随着各种各样的信息交流，这些信息就是语言\n");

			}
			else  if (i ==3){
				model.setTts("蘑菇是菌类,简单点说,有的菌是不能吃的!\n");
			}
			else  if (i == 4){
				model.setTts("众所周知，由于水中存在杂质和微物生，不能被人们所饮用。但自来水厂通过在水中投加混凝剂和消毒剂，以达到去除水中杂质和杀灭细菌等微生物的目的，使提供给人们生活饮用的水，经过这此工艺的处理后较之原来的水要安全卫生得多，但由于以下原因，这些水仍然不能保证绝对安全。 （1）水厂水处理工艺不完善。主要是指一些农村水厂，尚未建立一套完善的消毒工艺设备，常用人工投氯的方法进行消毒。这种方法，往往消毒剂量不准确，消毒效果难以保证，常使饮水消毒不彻底。 （2）可能受到二次污染。在城镇，供水管网密布千整个城区，并且许多管网很陈旧，经常发生破裂和渗漏，可以造成水质的二次污染。另外，一些高楼的水箱，长期得不到清洗消毒，能够积累大量的污染物和微物生，也会造成水质的二次污染。 （3）水源水质可以影响消毒效果。水的温度、浊度、酸碱度、水中有机物污染程度及卤素的含量、不同致病微生物的污染量及其抗药性等因素都会影响氯消毒效果，常规消毒往往不能彻底杀灭水中的微生物。如50年代中期，印度新德里发生一次大规模水型暴发性甲型肝炎流行、感染者约占新德里总人员的68%，就是由于水源遭受生活污水的严重污染，特别是肝炎病毒污染量大、抵抗力强，常规加氯量不足以全部灭活甲肝病毒而造成的。 （4）很多农家常用明矾将河水澄清后即饮用，但明矾虽有澄清水的作用，而无杀菌作用，致病微生物仍然存活于水中。 除此以外，还有不少因素都可以降低饮水的安全卫生性能。由于高温可以灭菌。所以，为了防止介水传染病的发生和流行，保护人们身体健康，应提倡喝开水，而不喝生水\n");
			}else  if (i == 5){
				model.setTts("3.平时说的空腹不能吃西红柿、香蕉等是指每顿饭前，也就是饭后四、五个小时");
			}

			adapter.addData(model);
		}

	}

	private void showMemory() {
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / (1024 * 1024));
		Log.d(TAG ,"Max memory is " + maxMemory + "MB");
		final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
		Log.i(TAG,"系统剩余内存:"+(info.availMem /(1024 * 1024))+"m");
		Log.i(TAG,"系统是否处于低内存运行："+info.lowMemory);
		Log.i(TAG,"当系统剩余内存低于"+info.threshold/(1024 * 1024)+"m 时就看成低内存运行");
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
		startTtsSpeaker(this,model.getTts(),0);
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
	/** 外部广播事件启动tts */
	public  static final String TTS_EXT_REQUEST = "com.ibotn.ibotnvoice.TTS_EXT_REQUEST";
	/**
	 * 外部广播事件启动tts <br/>
	 * @param context 上下文
	 * @param content 语音合成内容
	 * @param weight 权重
	 * @return success true;otherwise false
	 */
	public static boolean startTtsSpeaker(Context context, String content, int weight) {
		if (content == null || content.length() == 0){
			return false;
		}
		Intent intent = new Intent(TTS_EXT_REQUEST);
		intent.putExtra("content", content);
		intent.putExtra("weight", weight);
		context.sendBroadcast(intent);
		return true;
	}

	///////////
	public void testAidl(View view){
		if (isBound) {
			try {
				// 调用服务端方法
				String result = mService.getMessage("123");
				Log.e(TAG, "客户端：" + result);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Log.e(TAG, "还没有绑定成功");
		}
	}
	private IMyAidlInterface mService;
	private boolean isBound;

	// 绑定回调
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// 获取AIDL接口对象，这样就可以用来通信了
			mService = IMyAidlInterface.Stub.asInterface(service);
			isBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
			isBound = false;
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isBound) {
			unbindService(mServiceConnection);
		}
	}
}
