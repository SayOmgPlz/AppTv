package com.lytcho.apptv;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TvAdapter extends ArrayAdapter<Tv> {

    Context context;
    int layoutResourceId;
    List<Tv> data;
    MainActivity activity;
    
    public TvAdapter(Context context, int layoutResourceId, List<Tv> data, MainActivity activity) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        
        // check if the layout is null, we must render it if it is
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.channel_item, null);
        }

        final Tv item = getItem(position);
        if (item!= null) {
            
        	TextView itemView = (TextView) view.findViewById(R.id.TV_articleItem);
            if (itemView != null) {                
                itemView.setText(item.name);
            }
            
            itemView.setOnClickListener(new OnClickListener() {
            	
            	@Override
            	public void onClick(View v) {
            		activity.setVideoUrl(item.url);
            		activity.playVideo();
            	}
            });
        }

        return view;
    }
}
