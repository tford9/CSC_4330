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

public class XMLAsyncDownloader extends AsyncTask<Object, String, Integer> {

	String addressString;
	private String TAG;
	XMLParser parser;

	public XMLAsyncDownloader(String URL_String, String tag,
			ArrayList<String> SpinnerList, ArrayList<String> VehicleList,
			StringBuilder mPG, int parseType) {
		addressString = URL_String;
		TAG = tag;
		parser = new XMLParser(this, TAG, SpinnerList, VehicleList, parseType,
				mPG);
	}

	@Override
	protected Integer doInBackground(Object... params) {
		Log.i(TAG, "In doInBackground task");

		XmlPullParser downloadData = tryDownloadingXmlData();
		int recordsFound = parser.tryParsingXmlData(downloadData);
		return null;
	}

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
