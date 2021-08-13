//
// Created by bli on 2021/7/16.
//
package com.oal.insightface;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.oal.insightface.utils.FileUtils;

import java.io.File;

public class FaceActivity extends Activity implements SurfaceHolder.Callback {
    public static final int REQUEST_CAMERA = 100;
    private int facing = 1;  //For Khadas VIM3 Camera
    private int currentCpuNpu = 1; //CPU 0 NPU 1
    private SurfaceView cameraView;
    private FaceTengine faceTengine = new FaceTengine();
    private String modelPathString;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);

        //Copy model from assets to device
        File modelPath = getExternalFilesDir("Models");
        modelPathString = modelPath.toString();
//        Log.d("modelPath",modelPathString);
        FileUtils.copyAllAssets(this,modelPathString);

        String Device = getIntent().getStringExtra("currentCpuNpu");
        currentCpuNpu = Integer.parseInt(Device);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        cameraView = (SurfaceView) findViewById(R.id.cameraview);
        cameraView.getHolder().setFormat(PixelFormat.RGBA_8888);
        cameraView.getHolder().addCallback(this);

        reload();
    }

    private void reload() {
        Log.e("",modelPathString);
        if(currentCpuNpu==1){
            modelPathString = modelPathString + "/scrfd_2.5g_bnkps_uint8.tmfile";
        }
        else modelPathString = modelPathString + "/scrfd_2.5g_bnkps_sim.tmfile";
        Log.e("model",modelPathString);
        boolean ret_init = faceTengine.loadModel(currentCpuNpu,modelPathString);
        if (!ret_init) {
            Log.e("MainActivity", "Tengine loadModel failed");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        faceTengine.setOutputWindow(holder.getSurface());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        cameraView.setVisibility(View.GONE);
        faceTengine.closeCamera();
    }

    @Override
    public void onResume() {
        cameraView.setVisibility(View.VISIBLE);
        super.onResume();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }

        faceTengine.openCamera(facing);
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraView.setVisibility(View.GONE);
        faceTengine.closeCamera();
    }


}
