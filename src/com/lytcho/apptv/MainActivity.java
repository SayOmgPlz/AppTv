package com.lytcho.apptv;

import java.util.Collection;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends Activity {
	private TvAdapter arrayOfChannelsAdapter;
	String videoSrc = "http://hd3.lsops.net/live/smil:aljazeer_ar_hls/playlist.m3u8";

@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// TODO: ASYNC FILL WITH API CALL
		setChannelList();
		
		startVideoView();
	}
 
	private void setChannelList() {
		
		Tv[] channels = {new Tv("QQ", "bb","CC"), new Tv("2", "a", "3")};
		ListView listOfChannels = (ListView)findViewById(R.id.listOfChannels);
		arrayOfChannelsAdapter = new TvAdapter(this, R.layout.channel_item, channels);
		
		listOfChannels.setAdapter(arrayOfChannelsAdapter);
		 
	}
	
	public void updateTvsListView(Collection<Tv> tvs) {
		arrayOfChannelsAdapter.addAll(tvs); 
	}
	
	private void startVideoView() {
		final VideoView videoView = (VideoView) 
                findViewById(R.id.focusedTv);

		videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		videoView.setVideoPath(videoSrc);
		
		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);
		
		videoView.start();		
	}
}