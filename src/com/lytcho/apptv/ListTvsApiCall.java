package com.lytcho.apptv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

class ListTvsApiCall extends AsyncTask<MainActivity, String, Collection<Tv>> {
	private MainActivity currentActivity;
	private static String baseUrl = "http://46.47.81.78/stalker_portal/api/";
	private static final String TVS_URL = baseUrl + "itv/";
	private static String USER_SUBSCRIBTION = baseUrl + "itv_subscription/";
	
	
	@Override
	protected Collection<Tv> doInBackground(MainActivity... params) {
		currentActivity = params[0]; // URL to call TODO
		DeviceUtility device = new DeviceUtility(currentActivity);
		this.USER_SUBSCRIBTION += device.getMac(); 
		return getChannels(getSubscribedChannels());
	}
	
	private Collection<Tv> getChannels(String channelIds) {
		return parseTvData(httpRequest(TVS_URL + channelIds));
	}
	
	private String getSubscribedChannels() {
		return parseSubscribedChannelIds(httpRequest(USER_SUBSCRIBTION));
	}
	
	private String httpRequest(String url) {
		BufferedReader reader = null;
		String response = "";
		
        try {
        	HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            if(httpEntity != null){
                InputStream inputStream = httpEntity.getContent();

	            // Get the server response
	            reader = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder sb = new StringBuilder();
				String line = null;
	
				// Read Server Response
				while((line = reader.readLine()) != null) {
					// Append server response in string
					sb.append(line);
				}
			
				// Append Server Response To Content String
				response = sb.toString();
            }
         } catch(IOException e) { 
        	 e.printStackTrace();
         } finally {
        	 try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }    
        return response;
	}
	
	private String parseSubscribedChannelIds(String response) {
		String subscribedAsString = "";
		try {
			/****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
			JSONObject jsonResponse = new JSONObject(response);
			
		    JSONArray jsonSubscribtions = jsonResponse.optJSONArray("results");
		    
		    int lengthJsonArr = jsonSubscribtions.length();  
			
		    for(int i=0; i < lengthJsonArr; ++i) {
		        JSONObject jsonChildNode = jsonSubscribtions.getJSONObject(i);
		          
		        JSONArray subscribedTo = jsonChildNode.getJSONArray("sub_ch");
		        
		        // subscribedTo.join was not providing the needed result
		        
		        for(int j=0; j < subscribedTo.length(); ++j) {
		        	subscribedAsString += "," + subscribedTo.getString(j);
		        }
		    }
		    
	    } catch (JSONException e) {
		    e.printStackTrace();
		}
		
		return subscribedAsString;
	}

	private Collection<Tv> parseTvData(String response) {
		Collection<Tv> tvs = new ArrayList<Tv>();
		
		try {
			/****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
			JSONObject jsonResponse = new JSONObject(response);
			
		    JSONArray jsonChannels = jsonResponse.optJSONArray("results");

		    /*********** Process each JSON Node ************/
	
		    int lengthJsonArr = jsonChannels.length();  
	
		    for(int i=0; i < lengthJsonArr; ++i) {
		        /****** Get Object for each JSON node.***********/
		        JSONObject jsonChildNode = jsonChannels.getJSONObject(i);
		          
		        /******* Fetch node values **********/
		        String name = jsonChildNode.optString("name").toString();
		        String cmd  = jsonChildNode.optString("url").toString();		      
		        String type = jsonChildNode.optString("type").toString();		        
		        Tv tv = new Tv(name, cmd, type);
		        
		        tv.id   = jsonChildNode.optString("id").toString();
		        
		        tvs.add(tv);
		    }
		} catch (JSONException e) {
		    e.printStackTrace();
		}

		return tvs;
	}
	
	@Override
	protected void onPostExecute(Collection<Tv> tvs) {
		//currentActivity.updateUserData();
		currentActivity.updateTvsListView(tvs);
	}
	
	private void log(String message) {
		Log.i("customLog", "LOG:" + message);
	}
}
