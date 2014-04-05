package com.lytcho.apptv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

class ListTvsApiCall extends AsyncTask<MainActivity, String, User> {
	private static final String API_URL = "http://46.47.81.78/stalker_portal/api/";
	private static final String TVS_URL = API_URL + "itv/";
	private static final String USER_SUBSCRIPTION_URL = API_URL + "itv_subscription/";
	private static final String USER_INFO_URL = API_URL + "accounts/";
	
	private MainActivity currentActivity;
	

	@Override
	protected User doInBackground(MainActivity... params) {
		currentActivity = params[0]; // URL to call TODO
		DeviceUtility device = new DeviceUtility(currentActivity);
		String mac = device.getMac();
//		currentActivity.alert(mac); // Alerts in doInBackground do not on a real device
		
		User currentUser = new User();
		
		//mac = "EE:22:33:44:55:FF";
		
		if(mac != null && !mac.isEmpty()) {		
			currentUser = getUserData(mac);
			
			// if we have found the userMac in the backed from the reponse
			if(!currentUser.isEmpty()) {
				currentUser.setSubscribtions(getChannels(getSubscribedChannelIds(mac)));
			}			
		} 
		
		return currentUser;
	}
	
	private User getUserData(String mac) {
		return parseUserData(httpGet(USER_INFO_URL + mac));
	}
	
	private String getSubscribedChannelIds(String mac) {
		return parseSubscribedChannelIds(httpGet(USER_SUBSCRIPTION_URL + mac));
	}
	
	private List<Tv> getChannels(String channelIds) {
		return parseTvData(httpGet(TVS_URL + channelIds));
	}
	
	
	// TODO:: MAKE IT UTILITY WITTH CUSTOM HEADERS AS A HASH PARAM
	private String httpGet(String url) {
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
	
	private User parseUserData(String response) {
		User resultUser = new User();
		try {
			/****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
			JSONObject jsonResponse = new JSONObject(response);
			
			if(!jsonResponse.optString("status").equals("OK")) {
				return resultUser;
			}
			
		    JSONArray jsonUserData = jsonResponse.optJSONArray("results");
		    
		    int lengthJsonArr = jsonUserData.length();  
			
		    for(int i=0; i < lengthJsonArr; ++i) {
		        JSONObject jsonChildNode = jsonUserData.getJSONObject(i);
		          
		        String userAccountNumber = jsonChildNode.optString("account_number");
		        String userName = jsonChildNode.optString("full_name");
		        
		        resultUser.setAccountNumber(Integer.parseInt(userAccountNumber));
		        resultUser.setUsername(userName);
		    }
		    
	    } catch (JSONException e) {
		    e.printStackTrace();
		}
		
		return resultUser;
	}
	
	@Override
	protected void onPostExecute(User user) {
		List<Tv> tvs = user.getSubscribtions();
		
		currentActivity.updateTvsListView(tvs);
		if(tvs.isEmpty()) {
			currentActivity.alert("The user does not exist in database or is not subscribed for any channel");
		} else {
			currentActivity.setVideoUrl(tvs.toArray(new Tv[0])[0].url);
			currentActivity.playVideo();
		}
		
	}
	
	
	private void log(String message) {
		Log.i("customLog", "LOG:" + message);
	}
}
