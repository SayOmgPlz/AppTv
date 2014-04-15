package com.lytcho.apptv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.lytcho.apptv.api.ListTvsApiCall;
import com.lytcho.apptv.models.Tv;

public class MainActivity extends Activity {
	public Map<String, String> userInfo;
	
	private TvAdapter arrayOfChannelsAdapter;
	private VideoView videoView;
	private String videoUrl;
    private ListView channelList;
    private List<Tv> allChannels;
    public List<Tv> favChannels;
    
    //TODO:: refactor the class to use User private field instead of many other fields

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		Bundle params = intent.getBundleExtra("tokenInfo");
		userInfo = new HashMap<String, String>();
		userInfo.put("userId", params.getString("userId"));
		userInfo.put("token", params.getString("token"));
		setChannelList();
		
		initVideoView();
		
	}

    @Override
    public void onResume() {
        super.onResume();
        playVideo();
        findViewById(R.id.focusedTv).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                View squareView = findViewById(R.id.focusedTv);
                squareView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        stopVideo();
    }

	public void updateTvsListView(List<Tv> tvs) {
		arrayOfChannelsAdapter.clear();
		
		if(tvs.isEmpty()) {
			return;
		}
		
		Collections.sort(tvs, new Comparator<Tv>() {
			@Override
			public int compare(Tv lhs, Tv rhs) {
				return lhs.number.compareTo(rhs.number);
			}});
		
		arrayOfChannelsAdapter.addAll(tvs);
		arrayOfChannelsAdapter.notifyDataSetChanged();
	}
	
	public void updateUserData() {
		//user.setProperties();
	}
	
	public void playVideo() {
		if(videoUrl != null) {
			videoView.setVideoPath(videoUrl);
			videoView.start();
		}	
	}

	public void stopVideo() {
		videoView.stopPlayback();
	}
	
	public void alert(String message) {
		new AlertDialog.Builder(this)
	    .setTitle("Warning")
	    .setMessage(message)
	    .show();
	}

	private void setChannelList() {
		arrayOfChannelsAdapter = new TvAdapter(this, R.layout.channel_item, new ArrayList<Tv>(), this);

        channelList = (ListView)findViewById(R.id.listOfChannels);
        channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                setVideoUrl(((Tv)channelList.getItemAtPosition(position)).url);
                playVideo();
            }
        });
        channelList.setAdapter(arrayOfChannelsAdapter);
        channelList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		new ListTvsApiCall().execute(this);
	}

    public void selectFirst() {
        channelList.setSelection(0);
    }
	
	public void setVideoUrl(String url) {
		videoUrl = url;
	}

	private void initVideoView() {
		videoView = (VideoView)findViewById(R.id.focusedTv);
		videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                intent.putExtra("videoUrl", videoUrl);
                startActivity(intent);
                return true;
            }
        });
	}
	
	public void switchChannelFilters(View view) {
		switch(view.getId()) {
			case R.id.all_channels:
				updateTvsListView(allChannels);
				break;
			case R.id.favorite_channels:
				updateTvsListView(favChannels);
				break;
		}
	}
	
	public void setChannelList(List<Tv> tvs) {
		allChannels = tvs;
	}
	
	public void setFavoriteList(List<Tv> tvs) {
		favChannels = tvs;
	}
	
	public void changeFavIcon(View view) {
		// TODO:: make it with custom attributes(state active/inactive) instead of testing thsi way
		
		ImageView view1 = (ImageView)view;
		if(view1.getDrawable().getConstantState().equals
        (getResources().getDrawable(R.drawable.yellow_circle).getConstantState())){
			view1.setImageResource(R.drawable.grey_circle);
		} else {
			view1.setImageResource(R.drawable.yellow_circle);
		}
		
	
	}

}
