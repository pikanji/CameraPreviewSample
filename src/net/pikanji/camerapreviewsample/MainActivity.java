
package net.pikanji.camerapreviewsample;

/////////////////////////////////////////////////////////////////////////////
//Sample driver class to demonstrate the use of CameraPreview class.
/////////////////////////////////////////////////////////////////////////////

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class MainActivity extends Activity {
    private CameraPreview mPreview;
    private FrameLayout mLayout;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Hide status-bar
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide title-bar, must be before setContentView
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        mLayout = new FrameLayout(this);
        setContentView(mLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set the second argument by your choice.
        // Usually, 0 for back-facing camera, 1 for front-facing camera.
        // If the OS is pre-gingerbreak, this does not have any effect.
        mPreview = new CameraPreview(this, 0);
        LayoutParams previewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayout.addView(mPreview, 0, previewLayoutParams);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
        mPreview = null;
    }
}


/////////////////////////////////////////////////////////////////////////////
//Sample driver class to demonstrate the use of ResizableCameraPreview class.
/////////////////////////////////////////////////////////////////////////////
/*
import java.util.List;

import android.app.Activity;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Spinner;

public class MainActivity extends Activity implements ResizableCameraPreview.Callback {
    private ResizableCameraPreview mPreview;
    private ArrayAdapter<String> mAdapter;
    private FrameLayout mLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Hide status-bar
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide title-bar, must be before setContentView
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        Spinner spinner = new Spinner(this);
        LayoutParams spinnerLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mAdapter);
        spinner.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());
        
        mLayout = new FrameLayout(this);
        mLayout.addView(spinner, spinnerLayoutParams);
        
        setContentView(mLayout);
    }

    @Override
    public void cameraOpened() {
        mAdapter.add("Auto");
        List<Camera.Size> sizes = mPreview.getSupportedPreivewSizes();
        for (Camera.Size size : sizes) {
            mAdapter.add(size.width + " x " + size.height);
        }
    }
    
    private class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Rect rect = new Rect();
            mLayout.getDrawingRect(rect);
            
            if (0 == position) { // "Auto" selected
                mPreview.surfaceChanged(null, 0, rect.width(), rect.height());
            } else {
                mPreview.setPreviewSize(position - 1, rect.width(), rect.height());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // do nothing
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set the second argument by your choice.
        // Usually, 0 for back-facing camera, 1 for front-facing camera.
        // If the OS is pre-gingerbreak, this does not have any effect.
        mPreview = new ResizableCameraPreview(this, 0, false);
        LayoutParams previewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mPreview.setCallback(this);
        mLayout.addView(mPreview, 0, previewLayoutParams);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
        mPreview = null;
    }
}
*/