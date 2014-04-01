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

class ListTvsApiCall extends AsyncTask<MainActivity, String, Collection<Tv>> {
	private MainActivity currentActivity;
	
	private static final String TVS_URL = "http://46.47.81.78/stalker_portal/api/itv";

	@Override
	protected Collection<Tv> doInBackground(MainActivity... params) {
		currentActivity = params[0]; // URL to call TODO
		return getChannels();
	}
	
	private Collection<Tv> getChannels() {
		return parseResponse(httpRequest(TVS_URL));
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

	private Collection<Tv> parseResponse(String response) {
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
}
