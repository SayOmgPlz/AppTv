package com.lytcho.apptv;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class DeviceUtility {
	
	private Context context;
	
	public DeviceUtility(Context context) {
		this.context = context;
	}
	
	public String getMac() {		
		if(hasWifi()) {
			return getWifiMac();
		}
		
		return getEthernetMac();
	}
	
	public boolean hasWifi() {
		return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI);
	}
	
	private String getWifiMac() {
		WifiManager wifiMan = (WifiManager) context.getSystemService(
		                Context.WIFI_SERVICE);
		WifiInfo wifiInf = wifiMan.getConnectionInfo();
		return wifiInf.getMacAddress();
	}
	
	private String getEthernetMac() {
		return "";
	}
	
}
