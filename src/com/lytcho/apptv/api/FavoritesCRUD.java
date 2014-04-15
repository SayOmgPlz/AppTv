package com.lytcho.apptv.api;

import android.os.AsyncTask;

import com.lytcho.apptv.models.Tv;


public class FavoritesCRUD extends AsyncTask<Tv, Void, Void >{

	@Override
	protected Void doInBackground(Tv... params) {
		for(Tv tv : params) {
			if(tv.status != null && tv.status == Tv.Status.CREATE_FAVOURITE) {
				add(tv);
			}
		}
		return null;
	}
	
	private void add(Tv tv) {
		
		
	}
}
