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

package com.google.mlkit.vision.demo.java.posedetector.classification;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.nfc.TagLostException;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.WorkerThread;
import com.google.common.base.Preconditions;
import com.google.mlkit.vision.pose.Pose;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Accepts a stream of {@link Pose} for classification and Rep counting.
 */
public class PoseClassifierProcessor  {
  private static final String TAG = "PoseClassifierProcessor";


  // input karo different csv as per the user choice.
  private static String POSE_SAMPLES_FILE;
  private final boolean isStreamMode;
  private EMASmoothing emaSmoothing;
  private List<RepetitionCounter> repCounters;
  private PoseClassifier poseClassifier;
  private String lastRepResult;
  private String whichAsan ;
  private MediaPlayer correct,incorrect;
  private Context context;
  boolean crt = false;





  @WorkerThread
  public PoseClassifierProcessor(Context context, boolean isStreamMode,String whichAsan,MediaPlayer correct,MediaPlayer incorrect) {
    Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
    this.isStreamMode = isStreamMode;
    this.whichAsan = whichAsan;
    this.correct = correct;
    this.incorrect = incorrect;
    this.POSE_SAMPLES_FILE = "pose/" + whichAsan + ".csv";
    if (isStreamMode) {
      repCounters = new ArrayList<>();
      repCounters.add(new RepetitionCounter(whichAsan));
      emaSmoothing = new EMASmoothing();
    }
    this.context = context;


    loadPoseSamples(context);
  }


  private void loadPoseSamples(Context context) {
    // creating a list of posesamples and loaded from the csv file
    List<PoseSample> poseSamples = new ArrayList<>();

    try {
      BufferedReader reader = new BufferedReader(
          new InputStreamReader(context.getAssets().open(POSE_SAMPLES_FILE)));
      String csvLine = reader.readLine();
      while (csvLine != null) {
        // If line is not a valid {@link PoseSample}, we'll get null and skip adding to the list.
        PoseSample poseSample = PoseSample.getPoseSample(csvLine, ",");
        if (poseSample != null) {
            poseSamples.add(poseSample);
        }
        csvLine = reader.readLine();
      }
    } catch (IOException e) {
      Log.e(TAG, "Error when loading pose samples.\n" + e);
    }
    poseClassifier = new PoseClassifier(poseSamples);
  }


  /**
   * Given a new {@link Pose} input, returns a list of formatted {@link String}s with Pose
   * classification results.
   *
   * <p>Currently it returns up to 2 strings as following:
   * 0: PoseClass : X reps
   * 1: PoseClass : [0.0-1.0] confidence
   */
  @WorkerThread
  public List<String> getPoseResult(Pose pose) {

    Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
    List<String> result = new ArrayList<>();
    ClassificationResult classification = poseClassifier.classify(pose);



    // Update {@link RepetitionCounter}s if {@code isStreamMode}.
    if (isStreamMode) {
      // Feed pose to smoothing even if no pose found.
      classification = emaSmoothing.getSmoothedResult(classification);

      // Return early without updating repCounter if no pose found.
      if (pose.getAllPoseLandmarks().isEmpty()) {
        result.add(lastRepResult);
        return result;
      }



      for (RepetitionCounter repCounter : repCounters) {
        int repsBefore = repCounter.getNumRepeats();
        int repsAfter = repCounter.addClassificationResult(classification);

        try{
        if (repsAfter > repsBefore) {

          int sec = (repsAfter/10)%60;
          int min = repsAfter/600;

          // Play a fun beep when rep counter updates.

          if(!crt){
//            incorrect.stop();
            correct.start();
          }

          if(repsAfter%10==0) {
            ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
          }

          crt = true;
          lastRepResult = String.format(Locale.US, "Time : %d min %d  sec", min,sec);


          break;
        }
        else{
          incorrect.start();
          crt = false;
        }}
        catch (Exception e){
          Log.e(TAG, "getPoseResult:% "+e.getMessage());
        }
     }
      result.add(lastRepResult);
    }

    // Add maxConfidence class of current frame to result if pose is found.
    if (!pose.getAllPoseLandmarks().isEmpty()) {
      String maxConfidenceClass = classification.getMaxConfidenceClass();
      String maxConfidenceClassResult = String.format(
          Locale.US,
          "%s : %.2f confidence",
          maxConfidenceClass,
          classification.getClassConfidence(maxConfidenceClass)
              / poseClassifier.confidenceRange());
      result.add(maxConfidenceClassResult);
    }

    return result;
  }


}
