
package net.pikanji.camerapreviewsample;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private Activity mActivity;
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Activity activity) {
        super(activity); // Always necessary
        mActivity = activity;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (null == mCamera) {
            mCamera = Camera.open();
        }

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();

        // Set orientation
        boolean portrait = isPortrait();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            // 2.1 and before
            Parameters mParam = mCamera.getParameters();
            if (portrait) {
                mParam.set("orientation", "portrait");
            } else {
                mParam.set("orientation", "landscape");
            }
            mCamera.setParameters(mParam);
        } else {
            // 2.2 and later
            if (portrait) {
                mCamera.setDisplayOrientation(90);
            } else {
                mCamera.setDisplayOrientation(0);
            }
        }

        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (null == mCamera) {
            return;
        }
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    protected boolean isPortrait() {
        return (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    }
}
