package net.pikanji.camerapreviewsample;

import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class ResizableCameraPreview extends CameraPreview {
    private static final String LOG_TAG = "ResizableCameraPreviewSample";
    private Callback mCallback;
    private boolean mAddReversedSizes;

    /**
     * @param activity
     * @param adjustByAspectRatio
     * @param addReversedSizes is set to true to add reversed values of supported preview-sizes to the list.
     */
    public ResizableCameraPreview(Activity activity, boolean addReversedSizes) {
        super(activity);
        mAddReversedSizes = addReversedSizes;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
        
        if (mAddReversedSizes) {
            List<Camera.Size> sizes = mPreviewSizes;
            int length = sizes.size();
            for (int i = 0; i < length; i++) {
                Camera.Size size = sizes.get(i);
                Camera.Size revSize = mCamera.new Size(size.height, size.width);
                sizes.add(revSize);
            }
        }
        
        if (null != mCallback) {
            mCallback.cameraOpened();
        }
    }

    /**
     * 
     * @param index selects preview size from the list returned by CameraPreview.getSupportedPreivewSizes().
     * @param width is the width of the available area for this view
     * @param height is the height of the available area for this view
     */
    public void setPreviewSize(int index, int width, int height) {
        mCamera.stopPreview();
        
        Camera.Parameters cameraParams = mCamera.getParameters();
        boolean portrait = isPortrait();
        
        Camera.Size previewSize = mPreviewSizes.get(index);
        Log.v(LOG_TAG, "Requested Preview Size - w: " + previewSize.width + ", h: " + previewSize.height);
        mPreviewWidth = previewSize.width;
        mPreviewHeight = previewSize.height;
        boolean layoutChanged = adjustSurfaceLayoutSize(previewSize, portrait, width, height);
        if (layoutChanged) {
            mSurfaceConfiguring = true;
            return;
        }

        configureCameraParameters(cameraParams, portrait);
        try {
            mCamera.startPreview();
        } catch (Exception e) {
            Toast.makeText(mActivity, "Failed to satart preview: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        mSurfaceConfiguring = false;
    }

    public List<Camera.Size> getSupportedPreivewSizes() {
        return mPreviewSizes;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        public void cameraOpened();
    }
}
