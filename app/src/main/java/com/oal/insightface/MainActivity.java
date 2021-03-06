//
// Created by bli on 2021/7/16.
//
package com.oal.insightface;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.oal.insightface.utils.PermissionUtils;

public class MainActivity extends Activity {

    private int currentCpuNpu = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonNPU = (Button) findViewById(R.id.detct_NPU);
        buttonNPU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                currentCpuNpu = 1;
                startVideoWithFaceDetected();
            }
        });

        Button buttonCpu = (Button) findViewById(R.id.detct_CPU);
        buttonCpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                currentCpuNpu = 0;
                startVideoWithFaceDetected();
            }
        });

    }

    private void startVideoWithFaceDetected() {
        PermissionUtils.checkPermission(this, new Runnable() {
            @Override
            public void run() {
                jumpToCameraActivity();
            }
        });
    }

    public void jumpToCameraActivity() {
        Intent intent = new Intent(MainActivity.this, FaceActivity.class);
        intent.putExtra("currentCpuNpu", String.valueOf(currentCpuNpu));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                jumpToCameraActivity();
            } else {
                startVideoWithFaceDetected();
            }
        }
    }

}