
package net.pikanji.camerapreviewsample;
/*
import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Hide status-bar
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide title-bar, must be before setContentView
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(new CameraPreview(this));
    }
}
*/

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
        
        mPreview = new ResizableCameraPreview(this, false);
        LayoutParams previewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mPreview.setCallback(this);

        Spinner spinner = new Spinner(this);
        LayoutParams spinnerLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mAdapter);
        spinner.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());
        
        mLayout = new FrameLayout(this);
        mLayout.addView(mPreview, previewLayoutParams);
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
}
