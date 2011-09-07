
package net.pikanji.camerapreviewsample;

/////////////////////////////////////////////////////////////////////////////
//Sample driver class to demonstrate the use of CameraPreview class.
/////////////////////////////////////////////////////////////////////////////

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
        
        Button b1 = (Button) findViewById(R.id.button_sample);
        b1.setOnClickListener(this);
        Button b2 = (Button) findViewById(R.id.button_test);
        b2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_sample:
                intent = new Intent(this, CameraPreviewSampleActivity.class);
                startActivity(intent);
                break;
            case R.id.button_test:
                intent = new Intent(this, CameraPreviewTestActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
