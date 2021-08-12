//
// Created by bli on 2021/7/16.
//

package com.oal.insightface;


import android.view.Surface;

public class FaceTengine {
    public native boolean loadModel(int cpugpu);

    public native boolean openCamera(int facing);

    public native boolean closeCamera();

    public native boolean setOutputWindow(Surface surface);

    public static native void release();

    static {
        System.loadLibrary("facetengine");
    }
}
