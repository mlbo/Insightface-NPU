package com.oal.insightface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageProxy;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class FaceCameraxActivity extends AppCompatActivity {

    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA"};
    ImageView faceBitmap;         // 叠加在textureview上面的bitmap
    ImageAnalysis imageAnalysis;

    // 创建一个facetengine
    private FaceTengine facetengine = new FaceTengine();

    // 查询是否满足当前所有权限需求
    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 将app最上面那个带名字的title隐藏掉
        setContentView(R.layout.activity_face_camerax);
        faceBitmap = findViewById(R.id.faceBitmap);

        // 检查权限并启动摄像头
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        // 这里就不做选择了，直接默认模型0和CPU
        boolean ret_init = facetengine.loadModel(1);
        if (!ret_init) {
            Log.e("MainActivity", "facetengine loadModel failed");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 权限请求回调函数
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void startCamera() {
        CameraX.unbindAll(); // 解绑CameraX
        imageAnalysis = setImageAnalysis(); // 设置图像分析
        CameraX.bindToLifecycle(this, imageAnalysis);
    }

    private ImageAnalysis setImageAnalysis() {
        // 设置用来做图像分析的线程
        HandlerThread analyzerThread = new HandlerThread("OpenCVAnalysis");
        analyzerThread.start();

        // 配置图像分析并生成imageanalysis
        ImageAnalysisConfig imageAnalysisConfig = new ImageAnalysisConfig.Builder()
                .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE) // 接收最新的图
                .setCallbackHandler(new Handler(analyzerThread.getLooper())) // 设置回调
                .setImageQueueDepth(1).build(); // 设置图像队列深度为1
        ImageAnalysis imageAnalysis = new ImageAnalysis(imageAnalysisConfig);

        // 设置分析器
        imageAnalysis.setAnalyzer(
                new ImageAnalysis.Analyzer() {
                    @Override
                    public void analyze(ImageProxy image, int rotationDegrees) {

                        // 从CameraX提供的ImageProxy拉取图像数据
                        Image img = image.getImage();
                        final Bitmap bitmap = onImageAvailable(img);
                        if (bitmap == null) return;
                        Matrix matrix = new Matrix();
                        matrix.setRotate(90);
                        final Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                        // 在这里跑Tengine
                        int width = result.getWidth();
                        int height = result.getHeight();
                        int[] pixArr = new int[width * height];
                        // bitmap转数组
                        result.getPixels(pixArr, 0, width, 0, 0, width, height);
                        // 推理
                        facetengine.detectDraw(width, height, pixArr);
                        // 数组转回去bitmap
                        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                        newBitmap.setPixels(pixArr, 0, width, 0, 0, width, height);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                faceBitmap.setImageBitmap(newBitmap); // 将推理后的bitmao喂回去
                            }
                        });
                    }
                });
        return imageAnalysis;
    }

    public Bitmap onImageAvailable(Image image) {
        ByteArrayOutputStream outputbytes = new ByteArrayOutputStream();
        ByteBuffer bufferY = image.getPlanes()[0].getBuffer();
        byte[] data0 = new byte[bufferY.remaining()];
        bufferY.get(data0);
        ByteBuffer bufferU = image.getPlanes()[1].getBuffer();
        byte[] data1 = new byte[bufferU.remaining()];
        bufferU.get(data1);
        ByteBuffer bufferV = image.getPlanes()[2].getBuffer();
        byte[] data2 = new byte[bufferV.remaining()];
        bufferV.get(data2);
        try {
            outputbytes.write(data0);
            outputbytes.write(data2);
            outputbytes.write(data1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final YuvImage yuvImage = new YuvImage(outputbytes.toByteArray(), ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream outBitmap = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 95, outBitmap);
        Bitmap bitmap = BitmapFactory.decodeByteArray(outBitmap.toByteArray(), 0, outBitmap.size());
        image.close();
        return bitmap;
    }

}