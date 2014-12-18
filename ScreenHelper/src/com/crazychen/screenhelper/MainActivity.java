package com.crazychen.screenhelper;

import com.crazychen.screenhelper.MyService.NotificationBinder;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.domob.android.ads.AdEventListener;
import cn.domob.android.ads.AdManager.ErrorCode;
import cn.domob.android.ads.AdView;
import cn.domob.android.ads.InterstitialAd;
import cn.domob.android.ads.InterstitialAdListener;

public class MainActivity extends Activity {
	
	Button btn_open;
	Boolean isopen = false;
	ImageView infoOperatingIV;
	public BroadcastReceiver batteryReceiver;
	public int btteryColor;
	public int level;
	private MyService.NotificationBinder mBinder; 
	InterstitialAd mInterstitialAd;
	boolean flag = false;
	
	RelativeLayout mAdContainer;
	AdView mAdview;
	private ServiceConnection connection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			System.out.println("nubind connectio");
			mBinder.MyCancel();
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBinder = (NotificationBinder) service;
			mBinder.MyNotify();
		}
	};
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mAdContainer = (RelativeLayout) findViewById(R.id.adcontainer);
		// Create ad view
		mAdview = new AdView(this, "56OJxyu4uN9s+yu9+l", "16TLexalApDO2NUIAUaztcoz");
		mAdview.setKeyword("game");
		mAdview.setUserGender("male");
		mAdview.setUserBirthdayStr("2000-08-08");
		mAdview.setUserPostcode("123456");
		mAdview.setAdEventListener(new AdEventListener() {
			@Override
			public void onAdOverlayPresented(AdView adView) {
				Log.i("DomobSDKDemo", "overlayPresented");
				System.out.println("overlayPresented");
			}
			@Override
			public void onAdOverlayDismissed(AdView adView) {
				Log.i("DomobSDKDemo", "Overrided be dismissed");
				System.out.println("Overrided be dismissed");
			}
			@Override
			public void onAdClicked(AdView arg0) {
				Log.i("DomobSDKDemo", "onDomobAdClicked");
				System.out.println("onDomobAdClicked");
			}
			@Override
			public void onLeaveApplication(AdView arg0) {
				Log.i("DomobSDKDemo", "onDomobLeaveApplication");
				System.out.println("onDomobLeaveApplication");
			}
			@Override
			public Context onAdRequiresCurrentContext() {
				return MainActivity.this;
			}
			@Override
			public void onAdFailed(AdView arg0, ErrorCode arg1) {
				Log.i("DomobSDKDemo", "onDomobAdFailed");
				System.out.println("onDomobAdFailed");
			}
			@Override
			public void onEventAdReturned(AdView arg0) {
				Log.i("DomobSDKDemo", "onDomobAdReturned");
				System.out.println("onDomobAdReturned");
			}
		});
		RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mAdview.setLayoutParams(layout);
		mAdContainer.addView(mAdview);
		
		//电池广播监听
		batteryReceiver = new BroadcastReceiver() {									
			@Override
			public void onReceive(Context context, Intent intent) {
				level = intent.getIntExtra("level", 0);
				if (level > 45) {
					btteryColor = Color.rgb(74, 241, 225);
				} else if (level > 15) {
					btteryColor = Color.rgb(251, 209, 63);
				} else {
					btteryColor = Color.rgb(248, 16, 17);
				}
			}
		};
		registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		
		btn_open = (Button) findViewById(R.id.open);		

		btn_open.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isopen){
					Intent bindIntent = new Intent(MainActivity.this,MyService.class);
					bindService(bindIntent, connection, BIND_AUTO_CREATE);	
					startService(bindIntent);
					isopen = true;
					btn_open.setText("关闭常亮");
				}else{
					mBinder.MyCancel();
					unbindService(connection);
					stopService(new Intent(MainActivity.this,MyService.class));
			        isopen = false;
					btn_open.setText("开启常亮");			            					
				}
			}
		}); 
		
		
		infoOperatingIV = (ImageView) findViewById(R.id.infoOperating);
		//加载动画
		Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		if (operatingAnim != null) {
			infoOperatingIV.startAnimation(operatingAnim);
		}					
		
		infoOperatingIV.setOnClickListener(new View.OnClickListener() {
									
			@Override
			public void onClick(View v) {
				Animation operatingAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.big);
				LinearInterpolator lin = new LinearInterpolator();
				operatingAnim.setInterpolator(lin);
				operatingAnim.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						
					}
				});
				if (operatingAnim != null) {
					infoOperatingIV.startAnimation(operatingAnim);
				}		
			}
		});								
	}	
	
	
	private long lastTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		long currentTime = System.currentTimeMillis(); 		
		if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			if((currentTime-lastTime)>2000){					
				lastTime = currentTime;
				Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();			
			}else{
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume() {		
		super.onResume();		
	}		
	
	@Override
	protected void onDestroy() {		
		if(isopen){//这里要判断server是否被绑定，如果没有，则不需要解除绑定
			mBinder.MyCancel();
			unbindService(connection);
			stopService(new Intent(MainActivity.this,MyService.class));
		}
		unregisterReceiver(batteryReceiver);
		super.onDestroy();
	}
}
