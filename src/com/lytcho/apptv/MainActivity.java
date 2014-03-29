package com.lytcho.apptv;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends Activity {
 String videoSrc = "http://hd3.lsops.net/live/smil:aljazeer_ar_hls/playlist.m3u8";

@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// TODO: ASYNC FILL WITH API CALL
		fillChannelList();
		
		startVideView();
	}
 
	private void fillChannelList() {
		String[] channels = {"teveliziq 1", "televiziq 2", "aljazira"};
		ListView listOfChannels = (ListView)findViewById(R.id.listOfChannels);
		ArrayAdapter<String> arrayOfChannelsAdapter = new ArrayAdapter<String>(getBaseContext(),
				  R.layout.channel_item,
				  channels);
		listOfChannels.setAdapter(arrayOfChannelsAdapter);
		 
	}
	
	private void startVideView() {
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