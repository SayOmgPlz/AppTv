package com.lytcho.apptv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
		
		alert("Latest App Version");
		
		alert(new DeviceUtility(this).getMac());
		alert(new DeviceUtility(this).getWifiMac());
		alert(new DeviceUtility(this).hasWifi() ? "true" : "false");
	}
	
	public void updateTvsListView(List<Tv> tvs) {
//		Collections.sort(tvs, new Comparator<Tv>() {
//			@Override
//			public int compare(Tv lhs, Tv rhs) {
//				return lhs.number.compareTo(rhs.number);
//			}});
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