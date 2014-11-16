package com.project.searching;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;
import android.util.Log;

/**
 * <p>
 * Handles background downloading of vehicle information allowing SmartPump to
 * continue running during the request. This class extends AsyncTask which gives
 * the structure for background downloads. This class uses XMLParser to obtain
 * needed data.
 * </p>
 * 
 * @extends {@link AsyncTask}
 * @author SmartPump Team
 * @version 1.0
 */
public class XMLAsyncDownloader extends AsyncTask<Object, String, Integer> {

	String addressString;
	private String TAG;
	XMLParser parser;

	/**
	 * Constructs a XMLAsyncDownloader object from the parameter
	 * 
	 * @param URL_String
	 *            URL address of download source
	 * @param tag
	 * @param SpinnerList
	 *            List of elements available for the spinner drop-down list in
	 *            XMLParser
	 * @param VehicleList
	 *            List of elements available for the vehicle spinner drop-down
	 *            list in XMLParser
	 * @param mPG
	 *            The Miles per Gallon for XMLParser
	 * @param parseType
	 *            The type of parse that XMLParser should handle
	 */
	public XMLAsyncDownloader(String URL_String, String tag,
			ArrayList<String> SpinnerList, ArrayList<String> VehicleList,
			StringBuilder mPG, int parseType) {
		addressString = URL_String;
		TAG = tag;
		parser = new XMLParser(this, TAG, SpinnerList, VehicleList, parseType,
				mPG);
	}

	/**
	 * Attempts to download data in the background
	 */
	@Override
	protected Integer doInBackground(Object... params) {
		Log.i(TAG, "In doInBackground task");

		XmlPullParser downloadData = tryDownloadingXmlData();
		int recordsFound = parser.tryParsingXmlData(downloadData);
		return null;
	}

	/**
	 * Attempts to pull XML Data from given URL
	 */
	private XmlPullParser tryDownloadingXmlData() {
		Log.i(TAG, "Trying to download XML");

		try {
			URL FedGov = new URL(addressString);
			XmlPullParser downloadData;
			downloadData = XmlPullParserFactory.newInstance().newPullParser();
			InputStream IS = FedGov.openConnection().getInputStream();
			downloadData.setInput(IS, null);
			return downloadData;
		} catch (XmlPullParserException e) {
			Log.i(TAG, "XML Pull Parser Exception");
		} catch (IOException e) {
			Log.i(TAG, "IOException +");
		}
		return null;
	}

	public void doPublishProgress() {
		publishProgress();
	}

	public void doOnProgressUpdate(String[] values) {
		super.onProgressUpdate(values);
	}
}
