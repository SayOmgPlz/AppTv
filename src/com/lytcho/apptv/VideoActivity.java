package com.lytcho.apptv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity {
	private VideoView videoView;
	private String videoUrl;
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.video);
		
		Intent intent = getIntent();
		videoUrl = intent.getStringExtra("videoUrl");
		
		videoView = (VideoView)findViewById(R.id.fullScreenVideo);
		videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		
		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);
	}

	@Override
	public void onResume() {
		super.onResume();
		playVideo();
	}

	@Override
	public void onPause() {
		super.onPause();
		videoView.stopPlayback();		
	}

	public void playVideo() {
		videoView.stopPlayback();
		if(videoUrl != null) {
			videoView.setVideoPath(videoUrl);
		}
	}
}
