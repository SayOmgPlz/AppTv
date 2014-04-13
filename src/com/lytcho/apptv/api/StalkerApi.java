package com.lytcho.apptv.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StalkerApi {
	// TODO remove trailing slashes
	public static final String SERVER_URL = "http://v2.api.valentinaitken.com/";
	public static final String BASE_URL = SERVER_URL + "stalker_portal/";
	public static final String API_URL = BASE_URL + "api/";
    public static final String API_V2_URL =  API_URL + "api_v2/";

	public static final String TVS_URL = API_URL + "itv/";
	public static final String USER_SUBSCRIPTION_URL = API_URL + "itv_subscription/";
	public static final String USER_INFO_URL = API_URL + "accounts/";
	public static final String AUTH_TOKEN_URL = BASE_URL + "auth/token";

    static String httpGet(String url) {
        return httpGet(url, "");
    }

    // TODO:: MAKE IT UTILITY WITTH CUSTOM HEADERS AS A HASH PARAM
    static String httpGet(String url,String token) {
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
}
