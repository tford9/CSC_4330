package com.project.searching;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

/**
 * Handles four types of parsing based on passed parseType value from 0 to 4. 0
 * = Model, 1 = Make, 2 = Options, 3 = MPG.
 * 
 * @author SmartPump Team
 * @version 1.0
 */
public class XMLParser {
	String TAG;
	public StringBuilder MPG;
	private ArrayList<String> SpinnerList;
	private ArrayList<String> VehicleIDList;
	private Boolean fallOutBool = false;
	XMLAsyncDownloader caller;
	int parseType;

	/**
	 * Constructs a XMLParser object
	 * 
	 * @param that
	 *            Gets the XMLAsyncDownloader object used for downloading the
	 *            XML Data
	 * @param tag
	 * @param S
	 *            List of elements available for the spinner drop-down list
	 * @param V
	 *            List of elements available for the vehicle spinner drop-down
	 *            list
	 * @param pType
	 *            The type of parse that should be performed
	 * @param mPG
	 *            The Miles per Gallon
	 */
	public XMLParser(XMLAsyncDownloader that, String tag, ArrayList<String> S,
			ArrayList<String> V, int pType, StringBuilder mPG) {
		caller = that;
		MPG = mPG;
		TAG = tag;
		SpinnerList = S;
		VehicleIDList = V;
		parseType = pType;
	}

	/**
	 * Attempts to parse the XML data
	 * 
	 * @param downloadData
	 *            The XML Data downloaded
	 */
	public int tryParsingXmlData(XmlPullParser downloadData) {
		Log.i(TAG, "Trying to parse Data");

		if (downloadData != null) {
			try {
				return processDownloadData(downloadData);
			} catch (XmlPullParserException e) {
				Log.i(TAG, "XmlPullParserException");
			} catch (IOException e) {
				Log.i(TAG, "IOException +");
			}
		} else {
			Log.i(TAG, "No Downloaded Data");

		}
		return 0;
	}

	/**
	 * Processes downloaded data based on parseType; pulls SmartPump required
	 * information out
	 * 
	 * @param xmlData
	 */
	private int processDownloadData(XmlPullParser xmlData)
			throws XmlPullParserException, IOException {
		Log.i(TAG, "Attempting Process");
		int recordsFound = 0; // Find values in the XML records
		int eventType = xmlData.getEventType();

		if (parseType == 0) {
			SpinnerList.clear();
			VehicleIDList.clear();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					recordsFound++;
					Log.i(TAG, "Start document");
				} else if (eventType == XmlPullParser.START_TAG) {
					recordsFound++;
					Log.i(TAG, "Start tag combo F " + xmlData.getName());
					Log.i(TAG, "Start tag combo F " + xmlData.getName());
					if (xmlData.getName().equals("text")) {
						SpinnerList.add(xmlData.nextText().toString());
					} else if (xmlData.getName().equals("value")) {
						VehicleIDList.add(xmlData.nextText().toString());
					}

					Log.i(TAG, "Start tag combo S" + xmlData.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					Log.i(TAG, "End tag " + xmlData.getName());
				} else if (eventType == XmlPullParser.TEXT) {
					recordsFound++;
					// SpinnerLists.add(xmlData.getText());
				}
				eventType = xmlData.next();
			}
		}

		if (parseType == 1) {
			SpinnerList.clear();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					recordsFound++;
					Log.i(TAG, "Start document");
				} else if (eventType == XmlPullParser.START_TAG) {
					recordsFound++;
					Log.i(TAG, "Start tag combo F " + xmlData.getName());
					Log.i(TAG, "Start tag combo F " + xmlData.getName());
					if (xmlData.getName().equals("value")) {
						SpinnerList.add(xmlData.nextText().toString());
					} else if (xmlData.getName().equals("value")) {
						VehicleIDList.add(xmlData.nextText().toString());
					}
					Log.i(TAG, "Start tag combo S" + xmlData.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					Log.i(TAG, "End tag " + xmlData.getName());
				} else if (eventType == XmlPullParser.TEXT) {
					recordsFound++;
					// SpinnerLists.add(xmlData.getText());
				}
				eventType = xmlData.next();
			}
		}

		if (parseType == 2) {
			SpinnerList.clear();
			VehicleIDList.clear();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					recordsFound++;
					Log.i(TAG, "Start document");
				} else if (eventType == XmlPullParser.START_TAG) {
					recordsFound++;
					Log.i(TAG, "Start tag combo F " + xmlData.getName());
					Log.i(TAG, "Start tag combo F " + xmlData.getName());
					if (xmlData.getName().equals("text")) {
						SpinnerList.add(xmlData.nextText().toString());
					} else if (xmlData.getName().equals("value")) {
						VehicleIDList.add(xmlData.nextText().toString());
					}

					Log.i(TAG, "Start tag combo S" + xmlData.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					Log.i(TAG, "End tag " + xmlData.getName());
				} else if (eventType == XmlPullParser.TEXT) {
					recordsFound++;
					// SpinnerLists.add(xmlData.getText());
				}
				eventType = xmlData.next();
			}
		}

		if (parseType == 3) {
			MPG.delete(0, 2);
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					recordsFound++;
					Log.i(TAG, "Start document");
				} else if (eventType == XmlPullParser.START_TAG) {
					recordsFound++;
					Log.i(TAG, "Start tag combo F " + xmlData.getName());
					Log.i(TAG, "Start tag combo F " + xmlData.getName());
					if (xmlData.getName().equals("comb08")) {
						MPG.append(xmlData.nextText().toString());
					}
					Log.i(TAG, "Start tag combo S " + xmlData.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					Log.i(TAG, "End tag " + xmlData.getName());
				} else if (eventType == XmlPullParser.TEXT) {
					recordsFound++;
					// SpinnerLists.add(xmlData.getText());
				}
				eventType = xmlData.next();
			}
		}
		fallOutBool = true;
		if (recordsFound == 0) {
			caller.doPublishProgress();
		}
		Log.i(TAG, "Finished processing " + recordsFound + " records.");
		return recordsFound;
	}

	protected void onProgressUpdate(String... values) {
		if (values.length == 0)
			fallOutBool = false;
		Log.i(TAG, "No Data Downloaded");
		if (values.length > 0) {
			fallOutBool = true;
		}
		caller.doOnProgressUpdate(values);
	}
}
