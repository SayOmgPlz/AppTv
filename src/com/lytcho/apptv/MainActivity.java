package com.lytcho.apptv;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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
		
		setChannelList();
		
		startVideoView();
	}
 
	private void setChannelList() {
		arrayOfChannelsAdapter = new TvAdapter(this, R.layout.channel_item, new ArrayList<Tv>());
		
		ListView listOfChannels = (ListView)findViewById(R.id.listOfChannels);
		listOfChannels.setAdapter(arrayOfChannelsAdapter);
		
		new ListTvsApiCall().execute(this);
	}
	
	public void updateTvsListView(Collection<Tv> tvs) {
		arrayOfChannelsAdapter.clear();
		arrayOfChannelsAdapter.addAll(tvs);
		arrayOfChannelsAdapter.notifyDataSetChanged();
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