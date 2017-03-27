package com.example.curveanimation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private ImageView mDeleteImage;
    private Button mAddBtn;
    private RelativeLayout mRootLayout;
    private CartAdapter mCartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.activity_main_lv);
        mDeleteImage = (ImageView) findViewById(R.id.activity_main_iv);
        mRootLayout = (RelativeLayout) findViewById(R.id.activity_main);
        mCartAdapter = new CartAdapter();
        mListView.setAdapter(mCartAdapter);
    }

    class CartAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item,parent,false);
                holder = new ViewHolder(convertView);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }

        class ViewHolder{
            Button mAddBtn;
            float startX;
            float startY;
            float endX;
            float endY;

            public ViewHolder(View view){
                view.setTag(this);
                mAddBtn = (Button) view.findViewById(R.id.list_item_btn);
                mAddBtn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN :
                                startX = event.getRawX();
                                startY = event.getRawY()-200;
                                endX = mDeleteImage.getX();
                                endY = mDeleteImage.getY();
                                break;
                            case MotionEvent.ACTION_UP:
                                final ImageView ball = new ImageView(MainActivity.this);
                                ball.setImageResource(R.drawable.icon_favorite_batch_red);
                                mRootLayout.addView(ball);
                                //ball.setVisibility(View.VISIBLE);
                                ObjectAnimator animatorX =  ObjectAnimator.ofFloat(ball,"x",startX,endX);
                                ObjectAnimator animatorY =  ObjectAnimator.ofFloat(ball,"y",startY,endY);
                                animatorX.setDuration(500);
                                animatorY.setDuration(500);
                                animatorY.setInterpolator(new AccelerateInterpolator());
                                AnimatorSet animationSet = new AnimatorSet();
                                animationSet.play(animatorX).with(animatorY);
                                animationSet.start();
                                animationSet.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        mRootLayout.removeView(ball);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });
                                break;
                        }
                        return true;
                    }
                });
            }
        }
    }
}
