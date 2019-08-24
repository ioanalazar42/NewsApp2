package com.example.android.newsapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String url = "http://content.guardianapis.com/search?q=debates&api-key=test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publication_activity);

        // Get the list of earthquakes from {@link QueryUtils}
        List<Publication> publications = QueryUtils.extractFeatureFromJson();

        ListView publicationListView = (ListView) findViewById(R.id.list);

        PublicationAdapter adapter = new PublicationAdapter(this, publications);

        publicationListView.setAdapter(adapter);

    }
}
