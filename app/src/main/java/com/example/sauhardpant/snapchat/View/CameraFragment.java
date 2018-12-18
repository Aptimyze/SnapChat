package com.example.sauhardpant.snapchat.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sauhardpant.snapchat.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback {

  private static final int CAMERA_REQUEST_CODE = 123;

  Camera camera;
  SurfaceHolder surfaceHolder;
  @BindView(R.id.fragment_camera_surfaceView)
  SurfaceView surfaceView;
  @BindView(R.id.fragment_camera_capture_btn)
  Button captureBtn;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
    ButterKnife.bind(this, rootView);

    surfaceHolder = surfaceView.getHolder();

    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA },
          CAMERA_REQUEST_CODE);
    } else {
      surfaceHolder.addCallback(this);
      surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    captureBtn.setOnClickListener(click -> {
      takePicture();
    });

    return rootView;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == CAMERA_REQUEST_CODE) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
      } else {
        Toast.makeText(getContext(), "Need Camera Permission", Toast.LENGTH_SHORT).show();
      }
    }
  }

  public void takePicture() {
    camera.takePicture(null, null, (data, camera) -> {
      Intent pictureActivity = new Intent(getContext(), PictureActivity.class);
      // Unfortunately Android N gives an OS Exception: TransactionTooLargeException when you
      // try to pass the data array using bundle, so I am just passing the image through another
      // class
      ImageHelper.data = data;
      startActivity(pictureActivity);
    });
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    camera = Camera.open();

    // need to change the default way the camera gives your the image
    camera.setDisplayOrientation(90);
    Camera.Parameters parameters = camera.getParameters();
    parameters.setPreviewFrameRate(30);
    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    // get the best picture size
    List<Camera.Size> size = parameters.getSupportedPreviewSizes();
    int currentSize = size.get(0).height * size.get(0).width;
    Camera.Size bestSize = null;
    for (Camera.Size index : size) {
      if ((index.width * index.height) > currentSize) {
        currentSize = index.width * index.height;
        bestSize = index;
      }
    }
    if (bestSize != null) {
      parameters.setPreviewSize(bestSize.width, bestSize.height);
    }
    camera.setParameters(parameters);
    try {
      camera.setPreviewDisplay(holder);
    } catch (IOException e) {
      e.printStackTrace();
    }
    camera.startPreview();
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
  }
}
