package com.lytcho.apptv.api;

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
	
}
