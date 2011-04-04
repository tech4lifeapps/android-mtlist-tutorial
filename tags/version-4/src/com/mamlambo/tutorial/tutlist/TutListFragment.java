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
package com.mamlambo.tutorial.tutlist;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.mamlambo.tutorial.tutlist.data.TutListDatabase;
import com.mamlambo.tutorial.tutlist.data.TutListProvider;

public class TutListFragment extends ListFragment {
    private OnTutSelectedListener tutSelectedListener;

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String projection[] = { TutListDatabase.COL_URL };
        Cursor tutorialCursor = getActivity().getContentResolver().query(
                Uri.withAppendedPath(TutListProvider.CONTENT_URI,
                        String.valueOf(id)), projection, null, null, null);
        if (tutorialCursor.moveToFirst()) {
            String tutorialUrl = tutorialCursor.getString(0);
            tutSelectedListener.onTutSelected(tutorialUrl);
        }
        tutorialCursor.close();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] projection = { TutListDatabase.ID, TutListDatabase.COL_TITLE };
        String[] uiBindFrom = { TutListDatabase.COL_TITLE };
        int[] uiBindTo = { R.id.title };

        Cursor tutorials = getActivity().managedQuery(
                TutListProvider.CONTENT_URI, projection, null, null, null);

        CursorAdapter adapter = new SimpleCursorAdapter(getActivity()
                .getApplicationContext(), R.layout.list_item, tutorials,
                uiBindFrom, uiBindTo);

        setListAdapter(adapter);
    }

    public interface OnTutSelectedListener {
        public void onTutSelected(String tutUrl);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            tutSelectedListener = (OnTutSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTutSelectedListener");
        }
    }
}
