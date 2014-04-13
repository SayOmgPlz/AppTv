package com.lytcho.apptv.api;

import android.os.AsyncTask;

import static com.lytcho.apptv.api.StalkerApi.httpGet;

/**
 * Created by valentin on 4/13/14.
 */
class Status extends AsyncTask<String, Void, Void> {
    Status() {
        super();
    }

    /**
     * @param params userId token
     * @return
     */
    @Override
    protected Void doInBackground(String... params) {
        do {
            httpGet(StalkerApi.API_V2_URL + "users/" + params[0] + "/ping", params[1] );
            try {
                Thread.sleep(1000 * 120);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while(true);
    }
}
