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
package com.example.android.newsapp2;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {


    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /** Sample JSON response for a USGS query */
    private static final String SAMPLE_JSON_RESPONSE = "{\"response\":{\"status\":\"ok\",\"userTier\":\"developer\",\"total\":25549,\"startIndex\":1,\"pageSize\":10,\"currentPage\":1,\"pages\":2555,\"orderBy\":\"relevance\",\"results\":[{\"id\":\"us-news/2019/jun/26/democratic-debate-2019-watch-2020-election-when-where-who\",\"type\":\"article\",\"sectionId\":\"us-news\",\"sectionName\":\"US news\",\"webPublicationDate\":\"2019-06-27T20:23:02Z\",\"webTitle\":\"Democratic debates 2019: everything you need to know\",\"webUrl\":\"https://www.theguardian.com/us-news/2019/jun/26/democratic-debate-2019-watch-2020-election-when-where-who\",\"apiUrl\":\"https://content.guardianapis.com/us-news/2019/jun/26/democratic-debate-2019-watch-2020-election-when-where-who\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"us-news/2019/jul/31/healthcare-democrats-2020-debates-detroit\",\"type\":\"article\",\"sectionId\":\"us-news\",\"sectionName\":\"US news\",\"webPublicationDate\":\"2019-07-31T19:39:21Z\",\"webTitle\":\"Democratic debates: how healthcare is defining and dividing 2020 candidates\",\"webUrl\":\"https://www.theguardian.com/us-news/2019/jul/31/healthcare-democrats-2020-debates-detroit\",\"apiUrl\":\"https://content.guardianapis.com/us-news/2019/jul/31/healthcare-democrats-2020-debates-detroit\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"commentisfree/2019/may/11/missing-ingredient-debates-generosity-labour-antisemitism\",\"type\":\"article\",\"sectionId\":\"commentisfree\",\"sectionName\":\"Opinion\",\"webPublicationDate\":\"2019-05-11T05:00:12Z\",\"webTitle\":\"The missing ingredient in today\u2019s debates? Generosity | Gary Younge\",\"webUrl\":\"https://www.theguardian.com/commentisfree/2019/may/11/missing-ingredient-debates-generosity-labour-antisemitism\",\"apiUrl\":\"https://content.guardianapis.com/commentisfree/2019/may/11/missing-ingredient-debates-generosity-labour-antisemitism\",\"isHosted\":false,\"pillarId\":\"pillar/opinion\",\"pillarName\":\"Opinion\"},{\"id\":\"us-news/2019/jun/26/us-briefing-border-patrol-chief-quits-democrat-debates-and-mueller\",\"type\":\"article\",\"sectionId\":\"us-news\",\"sectionName\":\"US news\",\"webPublicationDate\":\"2019-06-26T10:27:06Z\",\"webTitle\":\"US briefing: border patrol chief quits, Democrat debates and Mueller\",\"webUrl\":\"https://www.theguardian.com/us-news/2019/jun/26/us-briefing-border-patrol-chief-quits-democrat-debates-and-mueller\",\"apiUrl\":\"https://content.guardianapis.com/us-news/2019/jun/26/us-briefing-border-patrol-chief-quits-democrat-debates-and-mueller\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"us-news/2019/jul/18/democratic-debates-biden-harris-bernie-warren-2020\",\"type\":\"article\",\"sectionId\":\"us-news\",\"sectionName\":\"US news\",\"webPublicationDate\":\"2019-07-19T01:45:58Z\",\"webTitle\":\"Democratic debates: Biden and Harris will face off again in second round\",\"webUrl\":\"https://www.theguardian.com/us-news/2019/jul/18/democratic-debates-biden-harris-bernie-warren-2020\",\"apiUrl\":\"https://content.guardianapis.com/us-news/2019/jul/18/democratic-debates-biden-harris-bernie-warren-2020\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"commentisfree/2019/jun/29/democratic-debate-kamala-harris-elizabeth-warren-trump\",\"type\":\"article\",\"sectionId\":\"commentisfree\",\"sectionName\":\"Opinion\",\"webPublicationDate\":\"2019-06-29T10:00:46Z\",\"webTitle\":\"Who won the Democratic debates? Kamala Harris, Elizabeth Warren \u2013 and Trump | Cas Mudde\",\"webUrl\":\"https://www.theguardian.com/commentisfree/2019/jun/29/democratic-debate-kamala-harris-elizabeth-warren-trump\",\"apiUrl\":\"https://content.guardianapis.com/commentisfree/2019/jun/29/democratic-debate-kamala-harris-elizabeth-warren-trump\",\"isHosted\":false,\"pillarId\":\"pillar/opinion\",\"pillarName\":\"Opinion\"},{\"id\":\"us-news/live/2019/jul/30/democratic-debates-2019-live-news-tonight-bernie-sanders-elizabeth-warren-buttigieg-o-rourke-latest\",\"type\":\"liveblog\",\"sectionId\":\"us-news\",\"sectionName\":\"US news\",\"webPublicationDate\":\"2019-07-31T04:40:19Z\",\"webTitle\":\"Democratic debates: Sanders and Warren team up against moderate attacks \u2013 as it happened\",\"webUrl\":\"https://www.theguardian.com/us-news/live/2019/jul/30/democratic-debates-2019-live-news-tonight-bernie-sanders-elizabeth-warren-buttigieg-o-rourke-latest\",\"apiUrl\":\"https://content.guardianapis.com/us-news/live/2019/jul/30/democratic-debates-2019-live-news-tonight-bernie-sanders-elizabeth-warren-buttigieg-o-rourke-latest\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"politics/2019/jun/21/jeremy-hunt-calls-for-live-tv-debates-before-tory-voting-begins\",\"type\":\"article\",\"sectionId\":\"politics\",\"sectionName\":\"Politics\",\"webPublicationDate\":\"2019-06-21T21:30:54Z\",\"webTitle\":\"Jeremy Hunt calls for live TV debates before Tory voting begins\",\"webUrl\":\"https://www.theguardian.com/politics/2019/jun/21/jeremy-hunt-calls-for-live-tv-debates-before-tory-voting-begins\",\"apiUrl\":\"https://content.guardianapis.com/politics/2019/jun/21/jeremy-hunt-calls-for-live-tv-debates-before-tory-voting-begins\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"commentisfree/2019/aug/03/democratic-debates-control-trump-campaign\",\"type\":\"article\",\"sectionId\":\"commentisfree\",\"sectionName\":\"Opinion\",\"webPublicationDate\":\"2019-08-03T10:00:46Z\",\"webTitle\":\"The Democratic party must control the debates \u2013 or they play into Trump's hands | Cas Mudde\",\"webUrl\":\"https://www.theguardian.com/commentisfree/2019/aug/03/democratic-debates-control-trump-campaign\",\"apiUrl\":\"https://content.guardianapis.com/commentisfree/2019/aug/03/democratic-debates-control-trump-campaign\",\"isHosted\":false,\"pillarId\":\"pillar/opinion\",\"pillarName\":\"Opinion\"},{\"id\":\"world/2019/jun/19/reparations-ta-nehisi-coates-cory-booker-congress-debate\",\"type\":\"article\",\"sectionId\":\"us-news\",\"sectionName\":\"US news\",\"webPublicationDate\":\"2019-06-19T17:56:00Z\",\"webTitle\":\"\u2018Stain of slavery\u2019: Congress debates reparations to atone for America's original sin\",\"webUrl\":\"https://www.theguardian.com/world/2019/jun/19/reparations-ta-nehisi-coates-cory-booker-congress-debate\",\"apiUrl\":\"https://content.guardianapis.com/world/2019/jun/19/reparations-ta-nehisi-coates-cory-booker-congress-debate\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"}]}}";
            /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Publication} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Publication> extractFeatureFromJson() {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Publication> publications = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

           // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(SAMPLE_JSON_RESPONSE);

            // Extract the JSONObject associated with the key called "response",
            JSONObject publicationResponse = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results"
            JSONArray publicationArray = publicationResponse.getJSONArray("results");

            // For each publication in the publicationArray, create an {@link Publication} object
            for (int publicationIndex = 0; publicationIndex < publicationArray.length();
                 publicationIndex++) {

                // Get a single publication at position i within the list of publications
                JSONObject currentPublication = publicationArray.getJSONObject(publicationIndex);

                // Extract the value for the key called "webTitle"
                String title = currentPublication.getString("webTitle");

                // Extract the value for the key called "sectionName"
                String section = currentPublication.getString("sectionName");

                // Extract the value for the key called "webUrl"
                String url = currentPublication.getString("webUrl");

                // Create a new {@link Earthquake} object with the magnitude, location, time,
                // and url from the JSON response.
                Publication publication = new Publication(title, section, url);

                // Add the new {@link Publication} to the list of publications.
                publications.add(publication);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return publications;
    }

}