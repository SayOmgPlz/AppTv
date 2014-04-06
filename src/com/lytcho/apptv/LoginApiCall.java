package com.lytcho.apptv;

import static com.lytcho.apptv.StalkerApi.AUTH_TOKEN_URL;

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

import android.os.AsyncTask;
import android.widget.EditText;

public class LoginApiCall extends AsyncTask<LoginActivity, String, Boolean> {
	private LoginActivity currentActivity;

	@Override
	protected Boolean doInBackground(LoginActivity... params) {
		// TODO Auto-generated method stub
		currentActivity = params[0];
		EditText username = (EditText) currentActivity
				.findViewById(R.id.username);
		EditText password = (EditText) currentActivity
				.findViewById(R.id.password);
		return login(username.getText().toString(), password.getText()
				.toString());
	}

	@Override
	protected void onPostExecute(Boolean response) {
		if(response) {
			currentActivity.goToMainActivity();
		} else {
			currentActivity.showError();
		}
	}

	public Boolean login(String username, String password) {
		Map<String, String> tokenInfo = getAuthToken(username, password);
		
		String token = tokenInfo.get("token");
		
		if( token != null && !token.isEmpty()){
			return true;
		}
		
		return false;
	}


	private Map<String, String> getAuthToken(String username, String password) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(AUTH_TOKEN_URL);
		httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		
		Map<String, String> result = new HashMap<String, String>();
		BufferedReader reader = null;
		String responseString = "";
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = entity.getContent();

				// Get the server response
				reader = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder sb = new StringBuilder();
				String line = null;

				// Read Server Response
				while ((line = reader.readLine()) != null) {
					// Append server response in string
					sb.append(line);
				}

				// Append Server Response To Content String
				responseString = sb.toString();
			}
			//
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
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}
