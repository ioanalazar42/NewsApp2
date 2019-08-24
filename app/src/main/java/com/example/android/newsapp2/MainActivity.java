package com.example.android.newsapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
                            implements LoaderManager.LoaderCallbacks<List<Publication>> {
    /** Constant value for the publication loader ID. */
    private static final int PUBLICATION_LOADER_ID = 1;

    String url = "http://content.guardianapis.com/search?q=debates&api-key=test";

    /** Adapter for the list of publications */
    private PublicationAdapter adapter;

    private static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publication_activity);


        ListView publicationListView = (ListView) findViewById(R.id.list);

        adapter = new PublicationAdapter(this, new ArrayList<Publication>());

        publicationListView.setAdapter(adapter);

        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(PUBLICATION_LOADER_ID, null, this);

    }
    @Override
    public Loader<List<Publication>> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG,"in onCreateLoader");
        // Create a new loader for the given URL
        return new PublicationLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Publication>> loader, List<Publication> publications) {

        // Clear the adapter of previous earthquake data
        adapter.clear();

        // If there is a valid list of {@link Publication}s, then add them to the adapter's
        // data set. This will update the ListView
        if (publications != null && !publications.isEmpty()) {
            Log.v(LOG_TAG,"list is not null or empty");
            adapter.addAll(publications);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Publication>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clear();
    }
}
