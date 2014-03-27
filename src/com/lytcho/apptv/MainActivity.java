package com.lytcho.apptv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
	
	Button openChannelsButton = (Button)findViewById(R.id.button1);
	openChannelsButton.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(), Channels.class);
			startActivity(intent);
		}
	});
	
	MediaController mediaController = new MediaController(this);
	mediaController.setAnchorView(videoView);
	videoView.setMediaController(mediaController);
	
	videoView.start();		
 }
}