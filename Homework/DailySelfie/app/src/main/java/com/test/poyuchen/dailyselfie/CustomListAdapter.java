package com.test.poyuchen.dailyselfie;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by poyuchen on 17/4/12.
 */
public class CustomListAdapter extends ArrayAdapter<Map<String, Object>> {

    private final Activity context;
    private List<Map<String, Object>> dataList;

    public CustomListAdapter(Activity context, List<Map<String, Object>> dataList) {
        super(context, R.layout.listview_main, dataList);

        this.context = context;
        this.dataList = dataList;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_main, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(dataList.get(position).get("name").toString());
        Bitmap bmp = BitmapFactory.decodeFile(dataList.get(position).get("image").toString());
        imageView.setImageBitmap(bmp);
        return rowView;
    }
}
