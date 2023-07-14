package com.google.mlkit.vision.demo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.mlkit.vision.demo.java.LivePreviewActivity;
import com.google.mlkit.vision.demo.java.posedetector.classification.PoseClassifierProcessor;


public class Yoga_Asan_Chooser extends AppCompatActivity
        implements AdapterView.OnItemClickListener {



    PoseClassifierProcessor PoseCsvChooser ;
    private static final String[] which_yoga =
                    new String[] {
                            "chair",
                            "tree",
                            "warrior_1",
                            "warrior_2",
                            "warrior_3"
                    };

    private static final int[] DESCRIPTION_IDS =
                    new int[] {
                            R.string.chair,
                            R.string.tree,
                            R.string.warrior_1,
                            R.string.warrior_2,
                            R.string.warrior_3
                    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chooser);

        ListView listView = findViewById(R.id.test_activity_list_view);
        MyArrayAdapter adapter = new MyArrayAdapter(this, android.R.layout.simple_list_item_2, which_yoga);
        adapter.setDescriptionIds(DESCRIPTION_IDS);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String clicked = which_yoga[position];
        Intent intent = new Intent(this, LivePreviewActivity.class);
        intent.putExtra("asan", clicked);
        startActivity(intent);
    }

    private static class MyArrayAdapter extends ArrayAdapter<String> {

        private final Context context;
        private final String[] which_yoga;
        private int[] descriptionIds;

        MyArrayAdapter(Context context, int resource, String[] which_yoga) {
            super(context, resource, which_yoga);

            this.context = context;
            this.which_yoga = which_yoga;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (convertView == null) {
                LayoutInflater inflater =
                        (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(android.R.layout.simple_list_item_2, null);
            }

            ((TextView) view.findViewById(android.R.id.text1)).setText(which_yoga[position]);
            ((TextView) view.findViewById(android.R.id.text2)).setText(descriptionIds[position]);

            return view;
        }

        void setDescriptionIds(int[] descriptionIds) {
            this.descriptionIds = descriptionIds;
        }
    }
}
