package com.example.zte.day27_zte_glide_base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);

    }

    /**参考与：http://blog.csdn.net/xiehuimx/article/details/52349317
     * @param view
     */
    public void loadImageClick(View view) {
        //这是显示gif图片的基本设置，
//        Glide.with(this).load("http://img1.3lian.com/2015/w4/17/d/64.gif")
//                .asGif()
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .placeholder(R.mipmap.ic_launcher)
//                .crossFade(500)
//                .into(imageView);
        //下面的转换圆角，需要将imageview的高度设置成固定的，
        Glide.with(this).load("http://img2.3lian.com/2014/f6/173/d/51.jpg")
//                .centerCrop()
                .transform(new GlideGroundTranform(this,50))
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);
    }

    class GlideGroundTranform extends BitmapTransformation{
        private float radius;
        public GlideGroundTranform(Context context) {
            this(context,100);
        }

        public GlideGroundTranform(Context context,int dp){
            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            Log.e("11aa", radius + "");
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }
     class GlideRotateTransform extends BitmapTransformation {
        private float rotateAngle = 0f;

        public GlideRotateTransform(Context context) {
            this(context, 90);
        }

        public GlideRotateTransform(Context context, float rotateAngle) {
            super(context);
            this.rotateAngle = rotateAngle;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Matrix matrix=new Matrix();
            matrix.postRotate(rotateAngle);
            return Bitmap.createBitmap(toTransform,0,0,toTransform.getWidth(),toTransform.getHeight(),matrix,true);
        }
        @Override
        public String getId() {
            return getClass().getName() + rotateAngle;
        }
    }
}
