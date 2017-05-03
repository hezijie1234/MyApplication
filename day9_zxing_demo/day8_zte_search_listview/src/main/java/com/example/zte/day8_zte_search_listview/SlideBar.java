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

	//slide11bar����ʾ����ĸ��#
	private static final String[] CHARACTERS = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
		"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	private static final String TAG = "111";

	//slidebar�Ŀ��
	private int width;
	//slidebar�ĸ߶�
	private int height;
	//SideBar��ÿ����ĸ����ʾ����ĸ߶�
	private float cellHeight;
	//����ĸ�Ļ���
	private Paint characterPaint;
	//SideBar����ĸ���Ƶľ�������
	private Rect textRect;
	//��ָ������SideBar�ϵĺ�������
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
	
	//��ʼ������
	private void init(Context context){
		textRect = new Rect();
		characterPaint = new Paint();
		characterPaint.setColor(Color.parseColor("#6699ff"));
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		//��view���µ�λ�û�ߴ�ʱ���Ͱ�changed�ͻ�ı䡣
		 //���������SideBar�ĸ߶ȺͿ��
		if(changed){
			width = getWidth();
			height = getHeight();
			//SideBar�ĸ߶ȳ�����Ҫ��ʾ����ĸ�ĸ���������ÿ����ĸ��ʾ����ĸ߶�
			cellHeight = height * 1.0f / CHARACTERS.length;
			//����SideBar�Ŀ�Ⱥ�ÿ����ĸ��ʾ�ĸ߶ȣ�ȷ��������ĸ�����ִ�С����������ĺô��ǣ����ڲ�ͬ�ֱ��ʵ���Ļ�����ִ�С�ǿɱ��
			int textSize = (int) ((width > cellHeight ? cellHeight : width) * (3.0f / 4));
			characterPaint.setTextSize(textSize);
		}
	}
	
	//����SideBar�ϵ���ĸ
	private void drawCharacters(Canvas canvas){
		Log.e(TAG, "drawCharacters: "+"ondraw" );
		for(int i = 0; i < CHARACTERS.length; i++){
			String s = CHARACTERS[i];
			Log.e(TAG, "drawCharacters: "+"����ĸѭ���ڲ�" );
			//��ȡ����ĸ�ľ�������,ϵͳ�������ĸ�Ĵ�С������һ����������������պÿ��Է��������ĸ
			//���� s���ַ�����0��s.length()��Ҫ�����ַ�������textRect������������ַ����ľ��Ρ�
			characterPaint.getTextBounds(s, 0, s.length(), textRect);
			if(position == i){
				characterPaint.setColor(Color.parseColor("#ff0000"));
				characterPaint.setFakeBoldText(true);
				Log.e(TAG, "drawCharacters: "+"�޸���ɫ" );
			}
			//������һ����õľ������򣬻�����ĸ
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
	
	//������ָ���������꣬��ȡ��ǰѡ�����ĸ
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
			//��ȡ��ָ����������
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
	
	//��������������ָ��SideBar�ϰ��º�̧��Ķ���
	public interface OnSelectListener{
		void onSelect(String s);
		void onMoveUp(String s);
	}
	
	//���ü�����
	public void setOnSelectListener(OnSelectListener listener){
		this.listener = listener;
	}

}
