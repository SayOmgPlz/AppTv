package com.lytcho.apptv;

import java.io.IOException;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainActivity extends Activity implements SurfaceHolder.Callback, OnPreparedListener{
 
 MediaPlayer mediaPlayer;
 SurfaceHolder surfaceHolder;
 SurfaceView playerSurfaceView;
 String videoSrc = "http://valentinaitken.com/YouTube.mp4";

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  playerSurfaceView = (SurfaceView)findViewById(R.id.playersurface);

        surfaceHolder = playerSurfaceView.getHolder();
        surfaceHolder.addCallback(this);
 }

 @Override
 public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
  // TODO Auto-generated method stub1
  
 }

 @Override
 public void surfaceCreated(SurfaceHolder arg0) {

        try {
         mediaPlayer = new MediaPlayer();
    mediaPlayer.setDisplay(surfaceHolder);
   mediaPlayer.setDataSource(videoSrc);
   mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
   mediaPlayer.prepareAsync();
   mediaPlayer.setOnPreparedListener(this);
  
  } catch (IllegalArgumentException e) {
	  String message = e.getMessage();
	  Log.i("TAG", ((message == null) ? "string null" : message));
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (SecurityException e) {
	  String message = e.getMessage();
	  Log.i("TAG", ((message == null) ? "string null" : message));
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (IllegalStateException e) {
	  String message = e.getMessage();
	  Log.i("TAG", ((message == null) ? "string null" : message));
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (IOException e) {
	  String message = e.getMessage();
	  Log.i("TAG", ((message == null) ? "string null" : message));
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
 }

 @Override
 public void surfaceDestroyed(SurfaceHolder arg0) {
  // TODO Auto-generated method stub
  
 }

 @Override
 public void onPrepared(MediaPlayer mp) {
  mediaPlayer.start();
 }

}

