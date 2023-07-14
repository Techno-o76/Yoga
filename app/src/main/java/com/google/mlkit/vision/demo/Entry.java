package com.google.mlkit.vision.demo;



import static android.app.PendingIntent.getActivity;

import static androidx.camera.core.CameraX.getContext;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.mlkit.vision.demo.frontend.MainActivity;
import com.google.mlkit.vision.demo.java.LivePreviewActivity;
import com.google.mlkit.vision.demo.preference.SettingsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entry extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vision_entry_choice);
        Toast.makeText(getApplicationContext(), "app started", Toast.LENGTH_SHORT).show();
        TextView start = findViewById(R.id.java_entry_point);

        start.setOnClickListener(
                v -> {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    Toast.makeText(getApplicationContext(), "intent made", Toast.LENGTH_SHORT).show();
                    startActivity(intent);

                });

        if(!checkPermission()){
            requestPermission();
        };
    }

    private boolean checkPermission() {
        int camera = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA);
        if (camera != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestPermission(){
        String camera = Manifest.permission.CAMERA;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[]{camera},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (String p : permissions) {
                    String msg = "";
                    if (this.checkSelfPermission(p) == PackageManager.PERMISSION_GRANTED)
                        msg = "Permission Granted for " + p;
                    else
                        msg = "Permission not Granted for " + p;
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
