package com.crazychen.screenhelper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

public class MyService extends Service {
	NotificationManager mNotificationManager;
	NotificationCompat.Builder mBuilder;
	PowerManager.WakeLock mWakeLock;
	private NotificationBinder mBinder = new NotificationBinder();
	class NotificationBinder extends Binder{
		public void MyNotify(){	
			if(mWakeLock != null)
				mWakeLock.acquire();
		}
		
		public void MyCancel(){
			//释放资源
			if(mWakeLock!=null){
				if(mWakeLock.isHeld()){
					mWakeLock.release();				
				}			
			}				
			mNotificationManager.cancel(83512);
			stopSelf();
		}
	}
	@Override
	public void onCreate() {
		super.onCreate();				
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//通知管理员
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		//发送通知
		PendingIntent pi = PendingIntent.getActivity(this , 0, 
				new Intent(this,MainActivity.class), 0);
		mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("屏幕常亮已经开启")
				.setTicker("开启屏幕常亮").setContentIntent(pi);
		mBuilder.setAutoCancel(false);		
		startForeground(83512, mBuilder.build());

		//开启屏幕常亮
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Tag");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) {		
		return mBinder;
	}

}
