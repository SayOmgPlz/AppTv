package com.lytcho.apptv;

import android.app.Activity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends Activity {
 String videoSrc = "http://m24.megafun.vn/live.vs?c=vstv042&q=high";

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main); 
	
	final VideoView videoView = (VideoView) 
                      findViewById(R.id.videoView1);

	videoView.setVideoPath(
			videoSrc);
	
	MediaController mediaController = new MediaController(this);
	mediaController.setAnchorView(videoView);
	videoView.setMediaController(mediaController);
	
	videoView.start();		
 }

}

