package com.example.zte.day8_zte_search_listview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 *
 */
public class SlideBar extends View {

	//slide11bar上显示的字母和#
	private static final String[] CHARACTERS = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
		"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	private static final String TAG = "111";

	//slidebar的宽度
	private int width;
	//slidebar的高度
	private int height;
	//SideBar中每个字母的显示区域的高度
	private float cellHeight;
	//画字母的画笔
	private Paint characterPaint;
	//SideBar上字母绘制的矩形区域
	private Rect textRect;
	//手指触摸在SideBar上的横纵坐标
	private float touchY;
	private float touchX;
	private int position;

	public void setPosition(int position) {
		this.position = position;
	}

	private OnSelectListener listener;

	public SlideBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public SlideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SlideBar(Context context) {
		super(context);
		init(context);
	}
	
	//初始化操作
	private void init(Context context){
		textRect = new Rect();
		characterPaint = new Paint();
		characterPaint.setColor(Color.parseColor("#6699ff"));
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		//当view有新的位置或尺寸时，就爱changed就会改变。
		 //在这里测量SideBar的高度和宽度
		if(changed){
			width = getWidth();
			height = getHeight();
			//SideBar的高度除以需要显示的字母的个数，就是每个字母显示区域的高度
			cellHeight = height * 1.0f / CHARACTERS.length;
			//根据SideBar的宽度和每个字母显示的高度，确定绘制字母的文字大小，这样处理的好处是，对于不同分辨率的屏幕，文字大小是可变的
			int textSize = (int) ((width > cellHeight ? cellHeight : width) * (3.0f / 4));
			characterPaint.setTextSize(textSize);
		}
	}
	
	//画出SideBar上的字母
	private void drawCharacters(Canvas canvas){
		Log.e(TAG, "drawCharacters: "+"ondraw" );
		for(int i = 0; i < CHARACTERS.length; i++){
			String s = CHARACTERS[i];
			Log.e(TAG, "drawCharacters: "+"画字母循环内部" );
			//获取画字母的矩形区域,系统会根据字母的大小来分配一个矩形区域，在里面刚好可以放下这个字母
			//参数 s是字符串。0到s.length()是要画的字符数量。textRect是来容纳这个字符串的矩形。
			characterPaint.getTextBounds(s, 0, s.length(), textRect);
			if(position == i){
				characterPaint.setColor(Color.parseColor("#ff0000"));
				characterPaint.setFakeBoldText(true);
				Log.e(TAG, "drawCharacters: "+"修改颜色" );
			}
			//根据上一步获得的矩形区域，画出字母
			canvas.drawText(s, 
					(width - textRect.width()) / 2f, 
					cellHeight * i + (cellHeight + textRect.height()) / 2f,
					characterPaint);
			characterPaint.setColor(Color.parseColor("#6699ff"));
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawCharacters      (canvas);
	}
	
	//根据手指触摸的坐标，获取当前选择的字母
	private String getHint(){
		int index = (int) (touchY / cellHeight);
		if(index >= 0 && index < CHARACTERS.length){
			return CHARACTERS[index];
		}
		return null;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			setBackgroundColor(Color.BLUE);
		case MotionEvent.ACTION_MOVE:
			//获取手指触摸的坐标
			touchX = event.getX();
			touchY = event.getY();
			if(listener != null && touchX > 0){
				listener.onSelect(getHint());
			}
			if(listener != null && touchX < 0){
				listener.onMoveUp(getHint());
			}
			return true;
		case MotionEvent.ACTION_UP:
			setBackgroundColor(Color.WHITE);
			touchY = event.getY();
			if(listener != null){
				listener.onMoveUp(getHint());
			}
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	//监听器，监听手指在SideBar上按下和抬起的动作
	public interface OnSelectListener{
		void onSelect(String s);
		void onMoveUp(String s);
	}
	
	//设置监听器
	public void setOnSelectListener(OnSelectListener listener){
		this.listener = listener;
	}

}
