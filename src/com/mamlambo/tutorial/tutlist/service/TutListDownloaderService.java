/*
 * Copyright (c) 2011, Lauren Darcey and Shane Conder
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this list of 
 *   conditions and the following disclaimer.
 *   
 * * Redistributions in binary form must reproduce the above copyright notice, this list 
 *   of conditions and the following disclaimer in the documentation and/or other 
 *   materials provided with the distribution.
 *   
 * * Neither the name of the <ORGANIZATION> nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific prior 
 *   written permission.
 *   
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR 
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF 
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * <ORGANIZATION> = Mamlambo
 */
package com.mamlambo.tutorial.tutlist.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.mamlambo.tutorial.tutlist.data.TutListDatabase;
import com.mamlambo.tutorial.tutlist.data.TutListProvider;

public class TutListDownloaderService extends Service {

    private static final String DEBUG_TAG = "TutListDownloaderService";
    private DownloaderTask tutorialDownloader;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        URL tutorialPath;
        try {
            tutorialPath = new URL(intent.getDataString());
            tutorialDownloader = new DownloaderTask();
            tutorialDownloader.execute(tutorialPath);
        } catch (MalformedURLException e) {
            Log.e(DEBUG_TAG, "Bad URL", e);
        }

        return Service.START_FLAG_REDELIVERY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class DownloaderTask extends AsyncTask<URL, Void, Boolean> {

        private static final String DEBUG_TAG = "TutListDownloaderService$DownloaderTask";

        @Override
        protected Boolean doInBackground(URL... params) {
            boolean succeeded = false;
            URL downloadPath = params[0];

            if (downloadPath != null) {
                succeeded = xmlParse(downloadPath);
            }
            return succeeded;
        }

        private boolean xmlParse(URL downloadPath) {
            boolean succeeded = false;

            XmlPullParser tutorials;

            try {
                tutorials = XmlPullParserFactory.newInstance().newPullParser();
                tutorials.setInput(downloadPath.openStream(), null);
                int eventType = -1;
                // psuedo code--
                // for each found "item" tag, find "link" and "title" tags
                // before end tag "item"

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        String tagName = tutorials.getName();
                        if (tagName.equals("item")) {

                            ContentValues tutorialData = new ContentValues();
                            // inner loop looking for link and title
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                if (eventType == XmlPullParser.START_TAG) {
                                    if (tutorials.getName().equals("link")) {
                                        tutorials.next();
                                        Log.d(DEBUG_TAG,
                                                "Link: " + tutorials.getText());
                                        tutorialData.put(
                                                TutListDatabase.COL_URL,
                                                tutorials.getText());
                                    } else if (tutorials.getName().equals(
                                            "title")) {
                                        tutorials.next();
                                        tutorialData.put(
                                                TutListDatabase.COL_TITLE,
                                                tutorials.getText());
                                    }
                                } else if (eventType == XmlPullParser.END_TAG) {
                                    if (tutorials.getName().equals("item")) {
                                        // save the data, and then continue with
                                        // the outer loop
                                        getContentResolver().insert(
                                                TutListProvider.CONTENT_URI,
                                                tutorialData);
                                        break;
                                    }
                                }
                                eventType = tutorials.next();
                            }
                        }
                    }
                    eventType = tutorials.next();
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return succeeded;
        }

    }

}
