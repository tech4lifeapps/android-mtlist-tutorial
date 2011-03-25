package com.mamlambo.tutorial.tutlist;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TutListFragment extends ListFragment {
    private OnTutSelectedListener tutSelectedListener;

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String[] links = getResources().getStringArray(R.array.tut_links);

        String content = links[position];
        tutSelectedListener.onTutSelected(content);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(ArrayAdapter.createFromResource(getActivity()
                .getApplicationContext(), R.array.tut_titles,
                R.layout.list_item));
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
