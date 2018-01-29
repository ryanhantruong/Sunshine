/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;

    private TextView mErrorTextView;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);
        mErrorTextView = (TextView) findViewById(R.id.tv_error);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);

        loadWeatherData();
    }

    /**
     * This method will get the user's preferred location for weather, and then tell some
     * background method to get the weather data in the background.
     */
    private void loadWeatherData() {
        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        showWeatherDataView();
        //new FetchWeatherTask().execute(location);

        // build URL
        String url = NetworkUtils.buildUrl(location).toString();

        // Instantiate request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Build response listeners
        Response.Listener<String> listener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                String[] weatherData = null;

                try {
                    weatherData = OpenWeatherJsonUtils
                            .getSimpleWeatherStringsFromJson(MainActivity.this, response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    mWeatherTextView.setText(R.string.error_msg);
                }

                // hide loading bar
                mProgressBar.setVisibility(View.INVISIBLE);
                // display info
                for (String weatherString : weatherData) {
                    mWeatherTextView.append((weatherString) + "\n\n\n");
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                showErrorMessage();
            }
        };

        // Build request
        StringRequest request = new StringRequest(Request.Method.GET,
                url,listener,errorListener);

        // Add request to queue
        queue.add(request);

    }

    private void showWeatherDataView(){
        mWeatherTextView.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage(){
        mWeatherTextView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.forecast, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            mWeatherTextView.setText("");
            // display loading bar
            mProgressBar.setVisibility(View.VISIBLE);
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}