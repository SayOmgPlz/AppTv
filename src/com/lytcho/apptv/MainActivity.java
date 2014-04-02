package com.lytcho.apptv;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends Activity {
	private TvAdapter arrayOfChannelsAdapter;
	private VideoView videoView;
	//private User user;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//user = User();
		
		setChannelList();
		
		initVideoView();
	}
	
	public void updateTvsListView(Collection<Tv> tvs) {
		arrayOfChannelsAdapter.clear();
		arrayOfChannelsAdapter.addAll(tvs);
		arrayOfChannelsAdapter.notifyDataSetChanged();
	}
	
	public void updateUserData() {
		//user.setProperties();
	}
	
	public void playVideo(String url) {
		videoView.stopPlayback();
		videoView.setVideoPath(url);
		videoView.start();
	}
	
	public void alert(String message) {
		new AlertDialog.Builder(this)
	    .setTitle("Warning")
	    .setMessage(message)
	    .show();
	}
	
	private void setChannelList() {
		arrayOfChannelsAdapter = new TvAdapter(this, R.layout.channel_item, new ArrayList<Tv>(), this);
		
		ListView channelList = (ListView)findViewById(R.id.listOfChannels);
		
		ListView listOfChannels = channelList;
		listOfChannels.setAdapter(arrayOfChannelsAdapter);		
		
		new ListTvsApiCall().execute(this);
	}
	
	private void initVideoView() {
		videoView = (VideoView) this.findViewById(R.id.focusedTv);
		videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		
		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);
	}
}