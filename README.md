CameraPreviewSample
===================

Over View
---------
This project demonstrates how to implement a camera preview step-by-step.

GitHub URL: [https://github.com/pikanji/CameraPreviewSample](https://github.com/pikanji/CameraPreviewSample)

### Git Tags
* minimum -- Minimum code implementing camera preview.
* orientation -- Display in correct orientation.
* aspectratio -- Display in correct aspect ratio.
* aspectratio_refine -- Fixed a non-critical issue that onSurfaceChanged() is called twice.
* resizable -- Includes UI to arbitrarily set camera preview size.
* previewsize_fix -- Fixed a problem that preview couldn't start even with supported preview size.

minimum
-------
This release is a minimum implementation of camera preview, which may display the preview in wrong orientation and broken aspect ratio.

### Files
* CameraPreview.java -- Primary file implementing camera preview feature.
* MainActivity.java -- Driver Activity to demonstrate the usage of CameraPreview.java.

orientation
-----------
This release handles the orientation change to display in the correct orientation.

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

resizable
---------
This release allows user to arbitrarily set the size of camera preview from the supported sizes.
It still keeps the aspect ratio and scales it to fit to the display.

### Files
* CameraPreview.java -- Primary file implementing camera preview feature.
* MainActivity.java -- Driver Activity to demonstrate the usage of CameraPreview.java.
    This file includes both code that utilizes CameraPreview and ResizeableCameraPreview, one of them commented out.
* ResizableCameraPreview.java -- Extended class of CameraPreview allowing user to specify the preview size.

picturesize_fix
---------------
This release includes a fix that solves the issue that the preview can't be started even with supported preview size.
The solution was to set picture size with supported picture size, as well as preview size.
This release also supported more display rotation, Surface.ROTATION_270 in addition to Surface.ROTATION_90. This affects only Android 2.2 and later.

### Files
The same as "resizable."

### Known Issue
* If the display rotation is changed from Surface.ROTATION_90 to Surface.ROTATION_270 or vice versa, the rotation of the camera preview is not updated.
* Orientation is not correct on an optimus pad (or all Honeycomb).

multicamera
-----------
This release supports multiple camera.
If the device has more than one camera on it, the user can choose a camera to display the camera preview.
CameraPreview (and ResizableCameraPreview) takes a camera ID, which is usually 0 for back-facing camera and 1 for front-facing camera if exists.
To switch between cameras, call stop() and instantiate another camera preview object with different camera ID.
This release also shows how to stop the camera preview when it is not on the foreground.
Stopping camera preview prevents from wasting the battery power and blocking other applications to use camera device.

### Files
The same as "picturesize_fix."

practical
---------
This version allows user class to specify the size and position of the camera preview.
Also, implemented setter for a callback called on preview-ready, and wrapper for Camera.set*Callback.

### Files
The same as "multicamera."

