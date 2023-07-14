package com.google.mlkit.vision.demo.frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.mlkit.vision.demo.R;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<User> {
    public ListAdapter(Context context, ArrayList<User> userArrayList){
        super(context, R.layout.list_item, userArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        User user = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        ImageView imageView = convertView.findViewById(R.id.yoga_image);
        TextView asan_name = convertView.findViewById(R.id.asan_name);
        TextView time = convertView.findViewById(R.id.time);

        imageView.setImageResource(user.imageId);
        asan_name.setText(user.Asanas);
        time.setText(user.TimeRequired);


        return convertView;
    }
}
