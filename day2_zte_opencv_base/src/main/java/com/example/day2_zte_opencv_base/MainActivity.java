package com.example.day2_zte_opencv_base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{
    private static final String TAG = "111";
    private JavaCameraView cameraView;
    private Mat mRgba;
    //相对的脸的大小。
    private float mRelativeFaceSize = 0.2f;
    //绝对的脸的大小。
    private int mAbsoluteFaceSize = 0;
    private int num = 0;
    private boolean isFinished = false;
    /**持续获取到人脸数量后，就使用当前截图**/
    public static final int FACE_SUCCESS_COUNT = 10;
    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);
    /**项目目录*/
    public static final String PROJECT_FOLDER_PATH = getSDCardRootPath() + File.separator + "day2zteopencvbase" + File.separator;
    /**缓存路径，用于存放临时的图片*/
    public static final String CASH = PROJECT_FOLDER_PATH + "cashes" + File.separator;
    /**缓存用的单张图片**/
    public static final String CASH_IMG = CASH+"cash.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //保持屏幕亮着。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        initViews();
    }

    /**
     * 在启动activity的时候查看openCV的库文件是否存在。
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG,"OpenCV library not found!");
            //没有库文件久启动下载
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.e(TAG, "OpenCV library found inside package. Using it!");
            //有库文件久使用它。
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    private CascadeClassifier mJavaDetector;
    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        /**
         * 只有当OpenCVManager连接成功，我们才能使用openCV的一些功能。
         * @param status
         */
        @Override
        public void onManagerConnected(int status){
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.e(TAG, "OpenCV loaded successfully" );
                    try {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (mJavaDetector.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier" );
                            mJavaDetector = null;
                        } else {
                            Log.e(TAG, "Loaded cascade classifier from"+ mCascadeFile.getAbsolutePath() );
                        }
                        cascadeDir.delete();
                        //getLocalHeadMat();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown:" );
                    }
                    //当放回值是成功的时候，就显示
                    cameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    private void initViews() {
        cameraView = (JavaCameraView) findViewById(R.id.main_camera);
        cameraView.setCvCameraViewListener(this);
        //使用前置摄像头来获取图像。
        cameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);
    }

    public void openClick(View view) {

    }

    /**相机开启时久会调用该方法，一般在该函数内部新建Mat用于存储图片。
     * @param width  -  the width of the frames that will be delivered被传递进来的视屏的宽度
     * @param height - the height of the frames that will be delivered 高度
     */
    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat();
    }

    /**
     * 相机关闭的时候在函数累不释放Mat对象
     */
    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    /**可以将处理图片的算法写在里面
     * @param inputFrame 视屏帧
     * @return
     */
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        //使用前置摄像头时旋转90度
        Mat rotateMat = Imgproc.getRotationMatrix2D(new Point(mRgba.cols() / 2, mRgba.rows() / 2), 90, 1);
        Imgproc.warpAffine(mRgba, mRgba, rotateMat, mRgba.size());

        if (mAbsoluteFaceSize == 0) {
            int height = mRgba.rows();
            Log.e(TAG, "onCameraFrame: "+height );
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }
        MatOfRect faces = new MatOfRect();
        if (mJavaDetector != null) {
            mJavaDetector.detectMultiScale(mRgba, faces, 1.1, 2, 2,
                    new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        }
        //检测并且显示。
        Rect[] facesArray = faces.toArray();
        if (facesArray.length > 0 && !isFinished ) {
            num++;
            if (num % FACE_SUCCESS_COUNT == 0) {

                Mat mat = getDefaultCompareSize(mRgba.submat(facesArray[0]));
//                compare(mat,headMat);
                Imgcodecs.imwrite(CASH_IMG, mat);
                Bitmap bitmap = BitmapFactory.decodeFile(CASH_IMG);
                //bitmap = ImageUtils.compressImage(bitmap);
                //ImageUtils.saveBitmapLocal(bitmap,Constants.CASH_IMG,80);
            }
            //在这里画矩形
            Imgproc.rectangle(mRgba, facesArray[0].tl(), facesArray[0].br(), FACE_RECT_COLOR, 3);
        } else {
            num = 0;
        }
        return mRgba;
    }
    /**
     * 获取相同尺寸的头像
     *
     * @param mat 传入的mat
     * @return Mat
     */
    /**上传到后台对比图片的尺寸**/
    public static final int COMPARE_SIZE = 320;
    private Mat getDefaultCompareSize(Mat mat) {
        Mat result = new Mat();
        Size size = new Size(COMPARE_SIZE, COMPARE_SIZE);
        Imgproc.resize(mat, result, size);
        return result;
    }

    public static String getSDCardRootPath() {
        if (avaiableSDCard()) {
            return getSDCardRoot().getPath();
        }
        return null;
    }
    public static boolean avaiableSDCard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    public static File getSDCardRoot() {
        if (avaiableSDCard()) {
            return Environment.getExternalStorageDirectory();
        }
        return null;
    }
}
