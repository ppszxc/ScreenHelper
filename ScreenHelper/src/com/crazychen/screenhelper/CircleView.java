package com.crazychen.screenhelper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleView extends ImageView{
	private int width;
	private int height;
	private int innerRadius;
	private int gap;
	private Paint paint;
	private MainActivity context;		
	private Canvas canvas;
	private boolean flag = true;
	
	public CircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = (MainActivity) context;
		init();
	}
	
	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = (MainActivity) context;
		init();		
	}
	
	public CircleView(Context context) {
		super(context);
		this.context = (MainActivity) context;
		init();
	}
	
	private void init(){				
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);	
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.canvas = canvas;
		width = canvas.getWidth();
		innerRadius = width*2/8;
		gap = width*1/8;
		drawInnerCircle(canvas);
		drawCircleBorder(canvas);
	}		
	
	//画内部圆
	private void drawInnerCircle(Canvas canvas){
		paint.setColor(Color.rgb(18, 93, 81));
		canvas.drawCircle(width/2,width/2, innerRadius, paint);
		paint.setColor(Color.WHITE);
		paint.setTextSize(width*1/16);	
		String info = "电量"+context.level+"%";//电量	
		float length = 0;
		switch(String.valueOf(context.level).length()){
			case 4:
				length = 2.5f;
				break;
			case 5:
				length = 3.5f;
				break;
			case 6:
				length = 4;
				break;
			default:
				length = 4;
		}
		//写出电量
		canvas.drawText(info, width/2-width*length*1/32, width/2+width*1/32, paint);
	}
	
	//根据电量多少画外围扇形
	private void drawCircleBorder(final Canvas canvas){					
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(context.btteryColor);
		paint.setStrokeWidth(gap);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawArc(new RectF(width/8,width/8,7*width/8,7*width/8), 0, 360 *context.level/100, false, paint);
	}
	
	
}
