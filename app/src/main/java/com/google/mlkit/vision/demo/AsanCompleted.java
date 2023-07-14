package com.google.mlkit.vision.demo;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.mlkit.vision.demo.frontend.MainActivity;
import com.google.mlkit.vision.demo.java.LivePreviewActivity;
import com.google.mlkit.vision.demo.preference.SettingsActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class AsanCompleted extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.asan_completed);

        int asan_no = R.drawable.chair;
        // which asan was chosen
        String asan = getIntent().getStringExtra("asan");

        // find which asan was that.
        int[] imageId = {R.drawable.chair, R.drawable.tree, R.drawable.warrior_1, R.drawable.warrior_2, R.drawable.warrior_3};
        String[] Asanas = {"chair", "tree", "warrior_1", "warrior_2", "warrior_3"};

        for(int i=0;i<imageId.length;i++){

            if(Asanas[i].equals(asan)){
                asan_no = imageId[i];
                break;
            }
        }

        Button main = findViewById(R.id.main);
        Button back = findViewById(R.id.back);
        CircleImageView img = findViewById(R.id.asan_image);
        img.setImageResource(asan_no);


        main.setOnClickListener(
                v -> {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra(
                            SettingsActivity.EXTRA_LAUNCH_SOURCE, SettingsActivity.LaunchSource.LIVE_PREVIEW);
                    startActivity(intent);
                });

        back.setOnClickListener(
                v -> {
                    Intent Rintent = new Intent(getApplicationContext(), LivePreviewActivity.class);
                    Rintent.putExtra("asan",asan);
                    startActivity(Rintent);
               });

    }
}
