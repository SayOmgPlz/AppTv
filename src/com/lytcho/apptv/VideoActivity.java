package com.lytcho.apptv;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity implements MediaPlayer.OnCompletionListener {
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
		
		MediaController mediaController = new MediaController(this) {
            public boolean dispatchKeyEvent(KeyEvent event)
            {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//                    ((Activity) getContext()).finish();
                    VideoActivity.this.finish();
                }

                return super.dispatchKeyEvent(event);
            }
        };
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);
        playVideo();
        videoView.requestFocus();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                //start Previous Activity here

                VideoActivity.this.finish();
            }
        }); // video finish listener

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
            videoView.start();
		}
	}

    @Override
    public void onCompletion(MediaPlayer mp) {
        finish();
    }
}
