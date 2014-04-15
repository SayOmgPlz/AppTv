package com.lytcho.apptv.api;

import static com.lytcho.apptv.api.StalkerApi.httpGet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		currentActivity = params[0];
		
		final Map<String, String> reqeustInfo = currentActivity.userInfo;

        new com.lytcho.apptv.api.Status().execute(reqeustInfo.get("userId"), reqeustInfo.get("token"));
		
		if(true) {
			return getViaLogin();
		}
		
		return getUsingThisDeviceMac();
	}
	
	@Override
	protected void onPostExecute(User user) {
		List<Tv> tvs = user.getSubscriptions();
		
		currentActivity.setChannelList(tvs);
		// TODO:: refactor with get method
		currentActivity.setFavoriteList(user.favorites);
		currentActivity.updateTvsListView(tvs);
		if(tvs.isEmpty()) {
			currentActivity.alert("The user does not exist in database or is not subscribed for any channel");
		} else {
			currentActivity.setVideoUrl(tvs.toArray(new Tv[0])[0].url);
            currentActivity.selectFirst(); // FIXME doesn't get the visual effect
			currentActivity.playVideo();
		}
		
	}
	
	private User getViaLogin() {
		User currentUser = new User();
		
		currentUser.setSubscriptions(getChannels(currentActivity.userInfo));
	
		return currentUser;
	}
	
	private List<Tv> getChannels(Map<String, String> reqeustInfo) {
		return parseTvData(httpGet(StalkerApi.API_V2_URL + "users/" + reqeustInfo.get("userId") + "/tv-channels", reqeustInfo.get("token") ));
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
		        int favorite = Integer.parseInt(jsonChildNode.optString("favorite").toString());
		        tv.favorite = ( favorite == 1 ? true : false);
		        
		        tvs.add(tv);
		    }
		} catch (JSONException e) {
		    e.printStackTrace();
		}

		return tvs;
	}
	
	
	
	// not used
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
	
	// not used
	private void log(String message) {
		Log.i("customLog", "LOG:" + message);
	}

	
	// deprecated, not used
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
	
	// deprecated, not used
	private User getUserData(String mac) {
		return parseUserData(httpGet(StalkerApi.USER_INFO_URL + mac));
	}
	
	// deprecated, not used
	private String getSubscribedChannelIds(String mac) {
		return parseSubscribedChannelIds(httpGet(StalkerApi.USER_SUBSCRIPTION_URL + mac));
	}
	
	
	// deprecated, not used
	private List<Tv> _getChannels(String channelIds) {
		return parseTvData(httpGet(StalkerApi.TVS_URL + channelIds));
	}
	
	// deprecated, not used
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

}
