package com.lytcho.apptv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

class ListTvsApiCall extends AsyncTask<MainActivity, String, List<Tv>> {
	private static final String API_URL = "http://46.47.81.78/stalker_portal/api/";
	private static final String TVS_URL = API_URL + "itv/";
	private static final String USER_SUBSCRIPTION_URL = API_URL + "itv_subscription/";
	private static final String USER_INFO_URL = API_URL + "accounts/";
	
	private MainActivity currentActivity;
	

	@Override
	protected List<Tv> doInBackground(MainActivity... params) {
		currentActivity = params[0]; // URL to call TODO
		DeviceUtility device = new DeviceUtility(currentActivity);
		String mac = device.getMac();
		
		if(mac != null && !mac.isEmpty()) {		
			//User currentUser = getUserData(mac);
			//currentUser.setSubscribtions(getChannels(getSubscribedChannelIds(mac)));
			return getChannels(getSubscribedChannelIds(mac));
		} else { 
			return Collections.emptyList();
		}
	}
	
//	private User getUserData(String mac) {
//		return parseUserData(httpRequest(USER_INFO_URL + mac));
//	}
	
	private String getSubscribedChannelIds(String mac) {
		return parseSubscribedChannelIds(httpRequest(USER_SUBSCRIPTION_URL + mac));
	}
	
	private List<Tv> getChannels(String channelIds) {
		return parseTvData(httpRequest(TVS_URL + channelIds));
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

	private List<Tv> parseTvData(String response) {
		List<Tv> tvs = new ArrayList<Tv>();
		
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
		        Integer number = jsonChildNode.optInt("number");
		        Tv tv = new Tv(name, cmd, type, number);
		        
		        tv.id   = jsonChildNode.optString("id").toString();
		        
		        tvs.add(tv);
		    }
		} catch (JSONException e) {
		    e.printStackTrace();
		}

		return tvs;
	}
	
	@Override
	protected void onPostExecute(List<Tv> tvs) {
		currentActivity.updateTvsListView(tvs);
		if(tvs.isEmpty())
			currentActivity.alert("The user does not exist in database or is not subscribed for any channel");
		else {
			currentActivity.setVideoUrl(tvs.toArray(new Tv[0])[0].url);
			currentActivity.playVideo();
		}
		
	}
	
	private void log(String message) {
		Log.i("customLog", "LOG:" + message);
	}
}
