package com.oal.insightface;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.oal.insightface.utils.FileUtils;

public class FaceActivity extends Activity implements SurfaceHolder.Callback {
    public static final int REQUEST_CAMERA = 100;

    private FaceTengine facetengine = new FaceTengine();
    //    private int facing = 0;
    private int facing = 1;  //TIM3

    private Spinner spinnerModel;
    private Spinner spinnerCPUGPU;
    private int current_model = 0;
    private int current_cpugpu = 1;

    private SurfaceView cameraView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);

        FileUtils.copyAllAssets(this, "sdcard/OAL/");

        String sID = getIntent().getStringExtra("current_cpugpu");
        current_cpugpu = Integer.parseInt(sID);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        cameraView = (SurfaceView) findViewById(R.id.cameraview);

        cameraView.getHolder().setFormat(PixelFormat.RGBA_8888);
        cameraView.getHolder().addCallback(this);
        reload();
    }

    private void reload() {

        boolean ret_init = facetengine.loadModel(current_cpugpu);
        if (!ret_init) {
            Log.e("MainActivity", "Tengine loadModel failed");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        facetengine.setOutputWindow(holder.getSurface());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }

        facetengine.openCamera(facing);
    }

    @Override
    public void onPause() {
        super.onPause();

        facetengine.closeCamera();
    }


}
