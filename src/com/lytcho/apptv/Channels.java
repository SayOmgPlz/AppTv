package com.lytcho.apptv;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Channels extends Activity {
	@Override
	 protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
		  setContentView(R.layout.chanels);
		  String[] channels = {"teveliziq 1", "televiziq 2", "aljazira"};
		  ListView listOfChannels = (ListView)findViewById(R.id.listOfChannels);
		  ArrayAdapter<String> arrayOfChannelsAdapter = new ArrayAdapter<String>(getBaseContext(),
				  R.layout.channel_item,
				  channels);
//		  arrayOfChannelsAdapter.add("TestTV item");
		  listOfChannels.setAdapter(arrayOfChannelsAdapter);
	 }
}
