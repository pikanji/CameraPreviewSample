
package net.pikanji.camerapreviewsample;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String LOG_TAG = "CameraPreviewSample";
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

        Log.v(LOG_TAG, "Desired Preview Size - w: " + width + ", h: " + height);

        Parameters mParam = mCamera.getParameters();

        // Set orientation
        boolean portrait = isPortrait();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            // 2.1 and before
            if (portrait) {
                mParam.set("orientation", "portrait");
            } else {
                mParam.set("orientation", "landscape");
            }
        } else {
            // 2.2 and later
            if (portrait) {
                mCamera.setDisplayOrientation(90);
            } else {
                mCamera.setDisplayOrientation(0);
            }
        }

        // Set width & height
        int previewWidth = width;
        int previewHeight = height;
        // Meaning of width and height is switched for preview when portrait,
        // while it is the same as user's view for surface and metrics.
        // That is, width must always be larger than height for setPreviewSize.
        if (portrait) {
            previewWidth = height;
            previewHeight = width;
        }

        // Actual preview size will be one of the sizes obtained by getSupportedPreviewSize.
        // It is the one that is the largest in both width and height no larger than given size in setPreviewSize.
        List<Size> sizes = mParam.getSupportedPreviewSizes();
        for (Size size : sizes) {
            if ((size.width <= previewWidth) && (size.height <= previewHeight)) {
                previewWidth = size.width;
                previewHeight = size.height;
                break;
            }
        }

        for (Size size : sizes) {
            if ((size.width <= previewWidth) && (size.height <= previewHeight)) {
                Log.v(LOG_TAG, "w: " + size.width + ", h: " + size.height);
            }
        }
        
        mParam.setPreviewSize(previewWidth, previewHeight);

        Log.v(LOG_TAG, "Preview Actual Size - w: " + previewWidth + ", h: " + previewHeight);

        // Adjust SurfaceView size
//        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
//        if(portrait) {
//            layoutParams.height = previewWidth;
//            layoutParams.width = previewHeight;
//        } else {
//            layoutParams.height = previewHeight;
//            layoutParams.width = previewWidth;
//        }

        float layoutHeight, layoutWidth;
        if (portrait) {
            layoutHeight = previewWidth;
            layoutWidth = previewHeight;
        } else {
            layoutHeight = previewHeight;
            layoutWidth = previewWidth;
        }

        float factH, factW, fact;
        factH = height / layoutHeight;
        factW = width / layoutWidth;
        // Select smaller factor, because the surface cannot be set to the size larger than display metrics.
        if (factH < factW) {
            fact = factH;
        } else {
            fact = factW;
        }
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.height = (int)(layoutHeight * fact);
        layoutParams.width = (int)(layoutWidth * fact);
        this.setLayoutParams(layoutParams);

        Log.v(LOG_TAG, "Preview Layout Size - w: " + layoutParams.width + ", h: " + layoutParams.height);
        
        mCamera.setParameters(mParam);
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
