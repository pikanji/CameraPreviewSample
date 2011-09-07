
package net.pikanji.camerapreviewsample;

import java.util.List;

import android.app.Activity;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;

public class CameraPreviewTestActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private ResizableCameraPreview mPreview;
    private ArrayAdapter<String> mAdapter;
    private RelativeLayout mLayout;
    private int mCameraId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide status-bar
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide title-bar, must be before setContentView
        // requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.test);
        
        // Spinner for preview sizes
        Spinner spinnerSize = (Spinner) findViewById(R.id.spinner_size);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(mAdapter);
        spinnerSize.setOnItemSelectedListener(this);

        // Spinner for camera ID
        Spinner spinnerCamera = (Spinner) findViewById(R.id.spinner_camera);
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCamera.setAdapter(adapter);
        spinnerCamera.setOnItemSelectedListener(this);
        adapter.add("0");
        adapter.add("1");
        adapter.add("2");
        
        mLayout = (RelativeLayout) findViewById(R.id.layout);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.w("CameraPreviewTestActivity", "onItemSelected invoked");
        Log.w("CameraPreviewTestActivity", "position: " + position);
        Log.w("CameraPreviewTestActivity", "parent.getId(): " + parent.getId());
        switch (parent.getId()) {
            case R.id.spinner_size:
            Rect rect = new Rect();
            mLayout.getDrawingRect(rect);

            if (0 == position) { // "Auto" selected
                mPreview.surfaceChanged(null, 0, rect.width(), rect.height());
            } else {
                mPreview.setPreviewSize(position - 1, rect.width(), rect.height());
            }
        break;
            case R.id.spinner_camera:
            mPreview.stop();
            mLayout.removeView(mPreview);
            mCameraId = position;
            createCameraPreview();
            break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

    @Override
    protected void onResume() {
        super.onResume();
        createCameraPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
        mLayout.removeView(mPreview);
        mPreview = null;
    }
    
    private void createCameraPreview() {
        // Set the second argument by your choice.
        // Usually, 0 for back-facing camera, 1 for front-facing camera.
        // If the OS is pre-gingerbreak, this does not have any effect.
        mPreview = new ResizableCameraPreview(this, mCameraId, CameraPreview.LayoutMode.FitToParent, false);
        LayoutParams previewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayout.addView(mPreview, 0, previewLayoutParams);

        mAdapter.clear();
        mAdapter.add("Auto");
        List<Camera.Size> sizes = mPreview.getSupportedPreivewSizes();
        for (Camera.Size size : sizes) {
            mAdapter.add(size.width + " x " + size.height);
        }
    }
}
