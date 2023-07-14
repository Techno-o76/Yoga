package com.google.mlkit.vision.demo.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.demo.R;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


        @Override
        protected void onCreate (Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            Toast.makeText(getApplicationContext(), "MainAcitvity", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_main);
                int[] imageId = {R.drawable.chair, R.drawable.tree, R.drawable.warrior_1, R.drawable.warrior_2, R.drawable.warrior_3};
                String[] Asanas = {"chair", "tree", "warrior_1", "warrior_2", "warrior_3"};
                String[] TimeRequired = {"10 min", "10 min", "10 min", "10 min", "10 min"};
//                String[] Description = {"a","b","c","d","e"};
                String[] Description =
                    {       "Chair Pose, or Utkatasana, is a fairly challenging standing yoga pose which tests many different parts of your body at once. Chair pose requires you to balance while building strength in your entire lower half of your body. At the same time, this standing pose builds core strength by the nature of the balance position involved."
                            , "Tree Pose, or Vrikshasana, is a one-legged standing balance. It’s suitable for practitioners of all levels due to its many available variations, and is often the first balance to be taught to complete beginners."
                            ,"While yoga is founded in peace, the name of this pose commemorates a spiritual warrior – someone strong, brave and not stepping down in the face of adversity. Warrior I is recognised as one of the foundational poses, practiced regularly in most yoga disciplines."
                            , "Warrior II, or Virabhadrasana II, is a natural continuation in the Warrior series, and can be used in a sequence as well as practised in isolation. It involves the majority of muscle groups and requires a lot of focus to get all the pose elements right."
                            , "Warrior II, or Virabhadrasana II, is a natural continuation in the Warrior series, and can be used in a sequence as well as practised in isolation. It involves the majority of muscle groups and requires a lot of focus to get all the pose elements right."
                    };

                ArrayList<User> userArrayList = new ArrayList<>();

                for (int i = 0; i < imageId.length; i++) {
                    User user = new User(imageId[i], Asanas[i], TimeRequired[i], Description[i]);
                    userArrayList.add(user);
                }

                ListAdapter listAdapter = new ListAdapter(getApplicationContext(), userArrayList);

                ListView listview = findViewById(R.id.listview);

                listview.setAdapter(listAdapter);
                listview.setClickable(true);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent i = new Intent(getApplicationContext(), UserActivity.class);
                        i.putExtra("image", imageId[position]);
                        i.putExtra("asana", Asanas[position]);
                        i.putExtra("time", TimeRequired[position]);
                        i.putExtra("description", Description[position]);
                        startActivity(i);

                    }
                });

        }
}