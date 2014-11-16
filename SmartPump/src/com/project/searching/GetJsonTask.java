package com.project.searching;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import android.os.AsyncTask;

/**
 * 
 * Subclass of AsyncTask which can be used to execute a HTTP request in the background.
 * Intended use is for retrieving JSON responses from myGasFeed.
 * 
 * @author SmartPump Team
 * @version 1.0
 *  
 * 
 */
public class GetJsonTask extends AsyncTask<URL, Void, String> 
{
	/**
	 * Overrides the AsyncTask doInBackground() method to build the response string
	 * as the response is received. It is required that the method accept multiple URLs, 
	 * but for the current version of SmartPump, only one URL at a time is expected
	 * 
	 * @param urls a list of URLs to be opened
	 * @return response string
	 * @throws IOException if there is an error with the URL stream
	 */
    protected String doInBackground(URL... urls)
    {
        InputStream dataStream;
        StringBuilder response = new StringBuilder();
        try 
        {
            dataStream = urls[0].openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(dataStream));
            int cp;
            while ((cp = rd.read()) != -1) 
            {
                response.append((char) cp);
            }
            return response.toString();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            return null;
        }
    }
}
