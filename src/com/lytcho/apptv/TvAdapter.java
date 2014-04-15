package com.lytcho.apptv;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lytcho.apptv.api.FavoritesCRUD;
import com.lytcho.apptv.models.Tv;

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

        final Tv tv = getItem(position);
        if (tv!= null) {
            
        	view.findViewById(R.id.add_favorites).setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View view) {
                	// TODO:: make it with custom attributes(state active/inactive) instead of testing thsi way
            		ImageView view1 = (ImageView)view;
            		if( view1.getDrawable().getConstantState().equals(
            				activity.getResources().getDrawable(R.drawable.grey_circle).getConstantState()) ) {
            			
            			view1.setImageResource(R.drawable.yellow_circle);
            			if(!activity.favChannels.contains(tv))
            				activity.favChannels.add(tv);
//            			/tv.status = Tv.Status.CREATE_FAVOURITE;
            			
            			//new FavoritesCRUD().execute(tv);
            		} else {
            			view1.setImageResource(R.drawable.grey_circle);
            			if(activity.favChannels.contains(tv))
            					activity.favChannels.remove(tv);

            		}
                }});
        	
        	TextView itemView = (TextView) view.findViewById(R.id.TV_articleItem);
            if (itemView != null) {                
                itemView.setText(tv.name);
            }
        }

        return view;
    }
}
