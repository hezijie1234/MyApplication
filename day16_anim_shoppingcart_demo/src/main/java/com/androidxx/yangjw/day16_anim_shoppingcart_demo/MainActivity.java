package com.androidxx.yangjw.day16_anim_shoppingcart_demo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private MyCartAdapter myCartAdapter;
    private ImageView mCartIv;
    private RelativeLayout mRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.main_shopping_list_lv);
        mCartIv = (ImageView) findViewById(R.id.main_shopping_cart_iv);
        mRootLayout = (RelativeLayout) findViewById(R.id.root_layout);
        myCartAdapter = new MyCartAdapter();
        mListView.setAdapter(myCartAdapter);
    }

    class MyCartAdapter extends BaseAdapter {

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
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_view,parent,false);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }

        class ViewHolder implements View.OnTouchListener{
            public Button mAddCartBtn;
            float endX ;
            float endY ;
            float startX ;
            float startY ;

            public ViewHolder(View view) {
                view.setTag(this);
                mAddCartBtn = (Button) view.findViewById(R.id.item_add_btn);
                mAddCartBtn.setOnTouchListener(this);
            }


            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //点下去时，获取点击的坐标值
                        endX = mCartIv.getX();
                        endY = mCartIv.getY();

                        startX = event.getRawX();
                        startY = event.getRawY()-200;
                        break;
                    case MotionEvent.ACTION_UP:
                        final ImageView ballImageView = new ImageView(MainActivity.this);
                        ballImageView.setImageResource(R.drawable.icon_favorite_batch_red);
                        mRootLayout.addView(ballImageView);
                        ballImageView.setVisibility(View.VISIBLE);
                        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(ballImageView,"x",startX,endX);
                        ObjectAnimator yAnimator = ObjectAnimator.ofFloat(ballImageView,"y",startY,endY);
                        xAnimator.setDuration(500);
                        yAnimator.setDuration(500);
                        yAnimator.setInterpolator(new AccelerateInterpolator());
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.play(xAnimator).with(yAnimator);
                        animatorSet.start();
                        animatorSet.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mRootLayout.removeView(ballImageView);
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
        }
    }
}
