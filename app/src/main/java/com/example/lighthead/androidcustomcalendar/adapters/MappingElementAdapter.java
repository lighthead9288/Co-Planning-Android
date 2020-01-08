package com.example.lighthead.androidcustomcalendar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lighthead.androidcustomcalendar.interfaces.IMappingElementOperations;
import com.example.lighthead.androidcustomcalendar.R;

import java.util.ArrayList;

public class MappingElementAdapter extends ArrayAdapter<String> {

    private LayoutInflater inflater;
    private int layout;
    private ArrayList<String> MapElements;

    private IMappingElementOperations iMappingElementOperations;

    public MappingElementAdapter(Context context, int resource, ArrayList<String> mapElements) {
        super(context, resource, mapElements);

        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.MapElements = mapElements;
    }

    public void SetIMappingElementOperations(IMappingElementOperations ops) { iMappingElementOperations = ops;}


    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(layout, parent, false);

        final String mapElement = MapElements.get(position);

        TextView usernameTv = view.findViewById(R.id.username);
        usernameTv.setText(mapElement);

        ImageButton closeButton = view.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromMapping(v, mapElement);
            }
        });

        return view;

    }

    public void removeFromMapping(View view, String username) {

        iMappingElementOperations.RemoveMappingElement(username);
    }


}
