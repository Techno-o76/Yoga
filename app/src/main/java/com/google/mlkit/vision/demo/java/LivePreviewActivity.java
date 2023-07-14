/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.mlkit.vision.demo.java;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.gms.common.annotation.KeepName;
import com.google.mlkit.vision.demo.AsanCompleted;
import com.google.mlkit.vision.demo.CameraSource;
import com.google.mlkit.vision.demo.CameraSourcePreview;
import com.google.mlkit.vision.demo.GraphicOverlay;
import com.google.mlkit.vision.demo.R;
import com.google.mlkit.vision.demo.VisionImageProcessor;
import com.google.mlkit.vision.demo.java.posedetector.PoseDetectorProcessor;
import com.google.mlkit.vision.demo.preference.PreferenceUtils;
import com.google.mlkit.vision.demo.preference.SettingsActivity;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;
import java.io.IOException;

/** Live preview demo for ML Kit APIs. */
@KeepName
public class LivePreviewActivity extends AppCompatActivity
        implements  CompoundButton.OnCheckedChangeListener {


  private static final String TAG = "LivePreviewActivity";

  private CameraSource cameraSource = null;
  private CameraSourcePreview preview;
  private GraphicOverlay graphicOverlay;
  private String whichAsan = "warrior_1";
  public MediaPlayer correct , incorrect;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");

    this.whichAsan = getIntent().getStringExtra("asan");

    Toast.makeText(getApplicationContext(), whichAsan, Toast.LENGTH_SHORT).show();

    setContentView(R.layout.activity_vision_live_preview);

    // media player
    correct = MediaPlayer.create(getApplicationContext(), R.raw.correct_posture);
    incorrect = MediaPlayer.create(getApplicationContext(), R.raw.incorrect_posture);

    preview = findViewById(R.id.preview_view);
    if (preview == null) {
      Log.d(TAG, "Preview is null");
    }
    graphicOverlay = findViewById(R.id.graphic_overlay);
    if (graphicOverlay == null) {
      Log.d(TAG, "graphicOverlay is null");
    }

    ToggleButton facingSwitch = findViewById(R.id.facing_switch);
    facingSwitch.setOnCheckedChangeListener(this);

    ImageView settingsButton = findViewById(R.id.settings_button);

    settingsButton.setOnClickListener(
            v -> {
              Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
              intent.putExtra(
                      SettingsActivity.EXTRA_LAUNCH_SOURCE, SettingsActivity.LaunchSource.LIVE_PREVIEW);
              startActivity(intent);
            });

    Button Done = findViewById(R.id.done);
    Done.setOnClickListener(
            v -> {
              Intent ReturnIntent = new Intent(getApplicationContext(), AsanCompleted.class);
              ReturnIntent.putExtra("asan",whichAsan);
              startActivity(ReturnIntent);
            });
  }

  public void createCameraSource() {
    // If there's no existing cameraSource, create one.
    if (cameraSource == null) {
      cameraSource = new CameraSource(this, graphicOverlay);
    }

    try {
      PoseDetectorOptionsBase poseDetectorOptions =
              PreferenceUtils.getPoseDetectorOptionsForLivePreview(this);
      Log.i(TAG, "Using Pose Detector with options " + poseDetectorOptions);
      boolean shouldShowInFrameLikelihood =
              PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this);
      boolean visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this);
      boolean rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this);
      boolean runClassification = PreferenceUtils.shouldPoseDetectionRunClassification(this);

      cameraSource.setMachineLearningFrameProcessor(
              (VisionImageProcessor) new PoseDetectorProcessor(
                      this,
                      poseDetectorOptions,
                      shouldShowInFrameLikelihood,
                      visualizeZ,
                      rescaleZ,
                      runClassification,
                      /* isStreamMode = */ true,
                      whichAsan,
                      correct,
                      incorrect

              ));

    } catch (RuntimeException e) {
      Toast.makeText(
                      getApplicationContext()
                      , "Can not create image processor: " + e.getMessage(),
                      Toast.LENGTH_LONG)
              .show();
    }
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    Log.d(TAG, "Set facing");
    if (cameraSource != null) {
      if (isChecked) {
        cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
      } else {
        cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
      }
    }
    preview.stop();
    startCameraSource();
  }



  /**
   * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
   * (e.g., because onResume was called before the camera source was created), this will be called
   * again when the camera source is created.
   */
  private void startCameraSource() {
    if (cameraSource != null) {
      try {
        if (preview == null) {
          Log.d(TAG, "resume: Preview is null");
        }
        if (graphicOverlay == null) {
          Log.d(TAG, "resume: graphOverlay is null");
        }
        preview.start(cameraSource, graphicOverlay);
      } catch (IOException e) {
        Log.e(TAG, "Unable to start camera source.", e);
        cameraSource.release();
        cameraSource = null;
      }
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.d(TAG, "onResume");
    createCameraSource();
    startCameraSource();
  }

  /** Stops the camera. */
  @Override
  protected void onPause() {
    super.onPause();
    preview.stop();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (cameraSource != null) {
      cameraSource.release();
    }
  }
}
