
package net.pikanji.camerapreviewsample;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.Toast;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String LOG_TAG = "CameraPreviewSample";
    private static final String CAMERA_PARAM_ORIENTATION = "orientation";
    private static final String CAMERA_PARAM_LANDSCAPE = "landscape";
    private static final String CAMERA_PARAM_PORTRAIT = "portrait";
    protected Activity mActivity;
    private boolean mAdjustByAspectRatio = false;
    private SurfaceHolder mHolder;
    protected Camera mCamera;
    protected List<Camera.Size> mPreviewSizes;
    protected int mPreviewWidth;
    protected int mPreviewHeight;

    /**
     * State flag: true when surface's layout size is set and surfaceChanged()
     * process has not been completed.
     */
    protected boolean mSurfaceConfiguring = false;

    public CameraPreview(Activity activity, boolean adjustByAspectRatio) {
        super(activity); // Always necessary
        mActivity = activity;
        mAdjustByAspectRatio = adjustByAspectRatio;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (null == mCamera) {
            mCamera = Camera.open();
            mPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
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

        Camera.Parameters cameraParams = mCamera.getParameters();
        boolean portrait = isPortrait();
        
        if (!mSurfaceConfiguring) {
            Camera.Size previewSize = determinePreviewSize(portrait, width, height);
            Log.v(LOG_TAG, "Desired Preview Size - w: " + width + ", h: " + height);
            mPreviewWidth = previewSize.width;
            mPreviewHeight = previewSize.height;
            boolean layoutChanged = adjustSurfaceLayoutSize(previewSize, portrait, width, height);
            if (layoutChanged) {
                mSurfaceConfiguring = true;
                return;
            }
        }

        configureCameraParameters(cameraParams, portrait);
        try {
            mCamera.startPreview();
        } catch (Exception e) {
            Toast.makeText(mActivity, "Failed to satart preview: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        mSurfaceConfiguring = false;
    }
    
    /**
     * @param cameraParams
     * @param portrait
     * @param reqWidth must be the value of the parameter passed in onSurfaceChanged
     * @param reqHeight must be the value of the parameter passed in onSurfaceChanged
     * @return true if layout parameter is newly set, false otherwise.
     */
    private Camera.Size determinePreviewSize(boolean portrait, int reqWidth, int reqHeight) {
        // Meaning of width and height is switched for preview when portrait,
        // while it is the same as user's view for surface and metrics.
        // That is, width must always be larger than height for setPreviewSize.
        int reqPreviewWidth; // requested width in terms of camera hardware
        int reqPreviewHeight; // requested height in terms of camera hardware
        if (portrait) {
            reqPreviewWidth = reqHeight;
            reqPreviewHeight = reqWidth;
        } else {
            reqPreviewWidth = reqWidth;
            reqPreviewHeight = reqHeight;
        }

        // Actual preview size will be one of the sizes obtained by getSupportedPreviewSize.
        // It is the one that has the largest width among the sizes of which both width and
        // height no larger than given size in setPreviewSize.
        
        // Only for debugging
        Log.v(LOG_TAG, "Listing all supported preview sizes");
        for (Camera.Size size : mPreviewSizes) {
            Log.v(LOG_TAG, "  w: " + size.width + ", h: " + size.height);
        }
        
        int tmpWidth = 0;
        int tmpHeight = 0;

        if (mAdjustByAspectRatio) { // Adjust surface size with the closest aspect-ratio
            float reqRatio = ((float)reqPreviewWidth) / reqPreviewHeight;
            float curRatio, deltaRatio;
            float deltaRatioMin = Float.MAX_VALUE;
            for (Camera.Size size : mPreviewSizes) {
                if ((size.width > reqPreviewWidth) || (size.height > reqPreviewHeight)) {
                    continue;
                }
                curRatio = ((float)size.width) / size.height;
                deltaRatio = Math.abs(reqRatio - curRatio);
                if (deltaRatio < deltaRatioMin) {
                    deltaRatioMin = deltaRatio;
                    tmpWidth = size.width;
                    tmpHeight = size.height;
                }
            }
        } else { // Adjust surface size with the largest picture size
            for (Camera.Size size : mPreviewSizes) {
                if ((size.width > reqPreviewWidth) || (size.height > reqPreviewHeight)) {
                    continue;
                }
                // When scale up the preview to fit to the screen,
                // it is more likely to have a margin on a short side.
                // So select a size based on the short side length, which
                // is height in terms of camera hardware.
                if (tmpHeight < size.height) {
                    tmpWidth = size.width;
                    tmpHeight = size.height;
                }
            }
        }

        return mCamera.new Size(tmpWidth, tmpHeight);
    }
    
    protected boolean adjustSurfaceLayoutSize(Camera.Size previewSize, boolean portrait, int availableWidth, int availableHeight) {
        float tmpLayoutHeight, tmpLayoutWidth;
        if (portrait) {
            tmpLayoutHeight = previewSize.width;
            tmpLayoutWidth = previewSize.height;
        } else {
            tmpLayoutHeight = previewSize.height;
            tmpLayoutWidth = previewSize.width;
        }
        
        float factH, factW, fact;
        factH = availableHeight / tmpLayoutHeight;
        factW = availableWidth / tmpLayoutWidth;
        // Select smaller factor, because the surface cannot be set to the size larger than display metrics.
        if (factH < factW) {
            fact = factH;
        } else {
            fact = factW;
        }
        
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        Log.v(LOG_TAG, "Current Preview Layout Size - w: " + this.getWidth() + ", h: " + this.getHeight());
        
        int layoutHeight = (int)(tmpLayoutHeight * fact);
        int layoutWidth = (int)(tmpLayoutWidth * fact);
        Log.v(LOG_TAG, "Preview Layout Size - w: " + layoutWidth + ", h: " + layoutHeight);
        
        boolean layoutChanged;
        if ((layoutWidth != this.getWidth()) || (layoutHeight != this.getHeight())) {
            layoutParams.height = layoutHeight;
            layoutParams.width = layoutWidth;
            this.setLayoutParams(layoutParams); // this will trigger another onSurfaceChanged invocation.
            layoutChanged = true;
        } else {
            layoutChanged = false;
        }
        
        return layoutChanged;
    }
    
    protected void configureCameraParameters(Camera.Parameters cameraParams, boolean portrait) {
        String orientation; // for 2.1 and before
        int angle; // for 2.2 and later
        
        if (portrait) {
            orientation = CAMERA_PARAM_PORTRAIT;
            angle = 90;
        } else {
            orientation = CAMERA_PARAM_LANDSCAPE;
            angle = 0;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) { // for 2.1 and before
            cameraParams.set(CAMERA_PARAM_ORIENTATION, orientation);
        } else { // for 2.2 and later
            mCamera.setDisplayOrientation(angle);
        }

        cameraParams.setPreviewSize(mPreviewWidth, mPreviewHeight);
        Log.v(LOG_TAG, "Preview Actual Size - w: " + mPreviewWidth + ", h: " + mPreviewHeight);
        
        mCamera.setParameters(cameraParams);
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
