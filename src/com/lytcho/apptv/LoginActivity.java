package com.lytcho.apptv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	EditText usernameField;
	EditText passwordField;		
	String SERVER_URL = "http://v2.api.valentinaitken.com/stalker_portal";
	String AUTH_TOKEN_URL = SERVER_URL + "/auth/token";
	String API_V2_URL = "http://v2.api.valentinaitken.com/stalker_portal/api/api_v2/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		usernameField = (EditText)findViewById(R.id.username);
		passwordField = (EditText)findViewById(R.id.password);
		Button loginBUtton   =  (Button)findViewById(R.id.post_login);
		
		loginBUtton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				login(v);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_login,
					container, false);
			return rootView;
		}
	}

	public void login(View buttonLogin){
	   String username = usernameField.getText().toString();
	   String password = passwordField.getText().toString();
	   
	   Map<String, String> tokenInfo = getAuthToken("l", "lol");
	   tryLogIn(tokenInfo.get("token"), tokenInfo.get("userId"));
	}
	
	private boolean tryLogIn(String token, String userId) {
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(AUTH_TOKEN_URL);
	    
	    
	    
	    Map<String, String> result = new HashMap<String, String>();

	        
	    
	    return true;
	}
	
	private Map<String, String> getAuthToken(String username, String password) {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(AUTH_TOKEN_URL);
	    
	    Map<String, String> result = new HashMap<String, String>();
	    BufferedReader reader = null;
	    String responseString = "";
	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("login", username));
	        nameValuePairs.add(new BasicNameValuePair("password", password));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        
	        HttpEntity entity = response.getEntity();
            if(entity != null){
                InputStream inputStream = entity.getContent();

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
				responseString = sb.toString();
            }
            
			JSONObject jsonResponse;
			try {
				jsonResponse = new JSONObject(responseString);
			
				result.put("token", jsonResponse.optString("access_token"));
				result.put("userId", jsonResponse.optString("user_id"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        

	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	    
	    return result;
	} 


}
