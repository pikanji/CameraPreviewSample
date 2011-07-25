CameraPreviewSample
===================

Over View
---------

GitHub URL: [https://github.com/pikanji/CameraPreviewSample](https://github.com/pikanji/CameraPreviewSample)

### Git Tags
* minimum -- Minimum code implementing camera preview.
* orientation -- Display in correct orientation.
* aspectratio -- Display in correct aspect ratio.
* aspectratio_refine -- Fixed a non-critical issue that onSurfaceChanged() is called twice.
* test_driver -- Includes UI to arbitrarily set camera preview size.

minimum
-------
This release is a minimum implementation of camera preview, which may display the preview in wrong orientation and broken aspect ratio.

### Files
* CameraPreview.java -- Primary file implementing camera preview feature.
* MainActivity.java -- Driver Activity to demonstrate the usage of CameraPreview.java.

orientation
-----------
This release handles the orientation change to dispaly in the correct orientation.

### Files
The same as the ones in "minimum."

aspectratio
-----------
This release displays camera preview in correct aspect ratio.
Whatever the actual size of the preview is, it keeps the width & height ratio while it scale the preview to fit to the display.
This although has a non-critical issue that might configure the camera parameters twice for one configuration change of the display.
It is caused by the multiple invocation of onSurfaceChanged for one orientation change because layout parameter might be updated at the first call of onSurfaceChanged.
This will be fixed in the next release.

### Files
The same as the ones in "orientation."

aspectratio_refine
------------------
This release fixed the known issue explained in the previous section.
It was also changed the algorithm to select the preview size, and now picks the size of which the ratio is closest to the one of the display.
There is a known issue that the camera preview might fail to start even with the supported preview size on some devices.
It is observed on Japanese device, IS03, so far.

### Files
The same as the ones in "aspectratio."

arbitrary_size
--------------
This release allows user to arbitrarily set the size of camera preview from the supported sizes.
It still keeps the aspect ratio and scales it to fit to the display.

### Files
* CameraPreview.java -- Primary file implementing camera preview feature.
* MainActivity.java -- Driver Activity to demonstrate the usage of CameraPreview.java.
    This file includes both code that utilizes CameraPreview and ResizeableCameraPreview, one of them commented out.
* ResizableCameraPreview.java -- Extended class of CameraPreview allowing user to specify the preview size.
