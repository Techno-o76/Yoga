package com.google.mlkit.vision.demo.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.demo.R;
import com.google.mlkit.vision.demo.java.LivePreviewActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);



        int imageid = getIntent().getIntExtra("image",R.drawable.chair);
        String asana = getIntent().getStringExtra("asana");
        String time = getIntent().getStringExtra("time");
        String description = getIntent().getStringExtra("description");


        CircleImageView YogaImg = findViewById(R.id.yoga_image);
        YogaImg.setImageResource(imageid);

        TextView AsanName = findViewById(R.id.asan_name);
        AsanName.setText(asana);

        TextView Time = findViewById(R.id.time);
        Time.setText(time);

        TextView AsanDescription = findViewById(R.id.asan_description);
        AsanDescription.setText(description);

        Button back = findViewById(R.id.back);
        Button start = findViewById(R.id.start);

        back.setOnClickListener(
                    v -> {
                        Intent Back_intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(Back_intent);
                        System.exit(0);
                    });

        start.setOnClickListener(
                    v -> {
                        Intent Start_intent = new Intent(getApplicationContext(), LivePreviewActivity.class);
                        Start_intent.putExtra("asan", asana);
                        startActivity(Start_intent);
                        System.exit(0);
                    }
            );

    }
}