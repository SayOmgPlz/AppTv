package com.lytcho.apptv.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.lytcho.apptv.MainActivity;
import com.lytcho.apptv.NetworkDevice;
import com.lytcho.apptv.models.Tv;
import com.lytcho.apptv.models.User;

public class ListTvsApiCall extends AsyncTask<MainActivity, String, User> {
	private MainActivity currentActivity;
	

	@Override
	protected User doInBackground(MainActivity... params) {
		currentActivity = params[0]; // URL to call TODO
		
		final Map<String, String> reqeustInfo = currentActivity.userInfo;
		
		Thread asyncPingUser = new Thread() {
			public void run() {
		        try {
		    		do {
		    			httpGet(StalkerApi.API_V2_URL + "users/" + reqeustInfo.get("userId") + "/ping", reqeustInfo.get("token") );
		    			Thread.sleep(1000 * 120);
		    		} while(true);
		        } catch(InterruptedException v) {
		        	
		        }		
			}
		};
		
		asyncPingUser.start();
		
		if(true) {
			return getViaLogin();
		}
		
		return getUsingThisDeviceMac();
	}
	
	private User getViaLogin() {
		User currentUser = new User();
		
		currentUser.setSubscriptions(getChannels(currentActivity.userInfo));
	
		return currentUser;
	}
	
	private User getUsingThisDeviceMac() {
		NetworkDevice device = new NetworkDevice(currentActivity);
		String mac = device.getMac();
//		currentActivity.alert(mac); // Alerts in doInBackground do not on a real device
		
		User currentUser = new User();
		
		//mac = "EE:22:33:44:55:FF";
		
		if(mac != null && !mac.isEmpty()) {		
			currentUser = getUserData(mac);
			
			// if we have found the userMac in the backed from the reponse
			if(!currentUser.isEmpty()) {
				currentUser.setSubscriptions(_getChannels(getSubscribedChannelIds(mac)));
			}			
		} 
		
		return currentUser;
	}
	
	private User getUserData(String mac) {
		return parseUserData(httpGet(StalkerApi.USER_INFO_URL + mac));
	}
	
	private String getSubscribedChannelIds(String mac) {
		return parseSubscribedChannelIds(httpGet(StalkerApi.USER_SUBSCRIPTION_URL + mac));
	}
	
	private List<Tv> getChannels(Map<String, String> reqeustInfo) {
		return parseTvData(httpGet(StalkerApi.API_V2_URL + "users/" + reqeustInfo.get("userId") + "/tv-channels", reqeustInfo.get("token") ));
	}
	
	// deprecated
	private List<Tv> _getChannels(String channelIds) {
		return parseTvData(httpGet(StalkerApi.TVS_URL + channelIds));
	}
	
	private String httpGet(String url) {
		return httpGet(url, "");
	}
	
	// TODO:: MAKE IT UTILITY WITTH CUSTOM HEADERS AS A HASH PARAM
	private String httpGet(String url,String token) {
		BufferedReader reader = null;
		String response = "";
		
        try {
        	HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            
            if(token != null && !token.isEmpty()) {
            	httpGet.addHeader("Authorization", "Bearer " + token);
            	httpGet.addHeader("Accept", "application/json,application/json;q=0.9,image/webp,*/*;q=0.8");
            }
            
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
			
		    JSONArray jsonSubscriptions = jsonResponse.optJSONArray("results");
		    
		    int lengthJsonArr = jsonSubscriptions.length();
			
		    for(int i=0; i < lengthJsonArr; ++i) {
		        JSONObject jsonChildNode = jsonSubscriptions.getJSONObject(i);
		          
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
		        //String type = jsonChildNode.optString("type").toString();
		        String type = "";
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
		List<Tv> tvs = user.getSubscriptions();
		
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
