package udacity.flash.discovermovies;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Prasanna Lakkur Subramanyam on 12/13/2015.
 *
 */
public class MainActivityFragment extends Fragment {


    public static final String IS_POPULAR_SORT = "udacity.flash.discovermovies_IS_POPULAR_SORT";
    public static final int SORT_DIALOG_REQ_CODE = 12345;
    public static final int DETAILS_REQUEST_CODE = 12;
    public static final String BUNDLE_POPULAR_SORT = "POPULAR_SORT_BUNDLE";
    public static final String INDEX_KEY = "udacity.flash.discovermovies_INDEX_KEY";

    private String REQUEST_POPULARITY;
    private String REQUEST_RATING;

    String responseFromServer;

    GridView gridView;
    MoviesAdapter gridAdapter;

    private boolean popularSort = true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the entire layout of the fragment

        View v = inflater.inflate(R.layout.fragment_activity_main, container, false);

        //to handle situations where the fragment is recreated again when it's resumed(because of low memory perhaps)
        if(isConnectedToInternet()) {
            //get a reference to the grid view
            gridView = (GridView) v.findViewById(R.id.mainFragmentGrid);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                    intent.putExtra(MainActivityFragment.INDEX_KEY, position);
                    startActivityForResult(intent, DETAILS_REQUEST_CODE);
                }
            });
            startGridView();
        }else{
            Toast.makeText(getContext(), R.string.not_connected, Toast.LENGTH_LONG).show();
        }

        return v;
    }

    /**
     * Instantiates a MoviesAdapter instance which corresponds to the data in the MovieModel instance in the Singleton currently.
     * Then sets the MoviesAdapter to the gridView instance
     */
    public void startGridView() {
        gridAdapter = new MoviesAdapter(getContext(), JSONResponse.getInstance().getMovieModel().getResults());
        //set an adapter to the grid view to populate it's elements
        gridView.setAdapter(gridAdapter);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        //on confugration change, save the value of "popularSort"
        outState.putBoolean( BUNDLE_POPULAR_SORT , popularSort);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isConnectedToInternet()) {
            Resources res = getActivity().getResources();

            //generating the required string to get the most popular movies from themoviedb
            REQUEST_POPULARITY = res.getString(R.string.popular_sort_request) + res.getString(R.string.api_key);
            //generating the required string to get the highest rated movies from themoviedb
            REQUEST_RATING = res.getString(R.string.rating_sort_request) + res.getString(R.string.api_key);

            //to inform that the fragment has an options menu(so that we get a callback to onOptionsItemSelected
            setHasOptionsMenu(true);
            //to retain the current instance of the fragment whenever possible
            setRetainInstance(true);

            //restoring the previous state of "popularSort" variable
            if (savedInstanceState != null)
                popularSort = savedInstanceState.getBoolean(BUNDLE_POPULAR_SORT);

            //if "popularSort" is true then the user wants a sort by popularity, else by top rating
            if (popularSort)
                getJSONFromLink(REQUEST_POPULARITY);
            else
                getJSONFromLink(REQUEST_RATING);
        }else{
            Toast.makeText(getContext(), R.string.not_connected, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Gets the JSON string from themoviedb for the string provided in the param and sets it to the responseFromServer String. Also initializes the
     * singleton with the correct model
     * @param link the String which represents the request to be made to the server
     */
    private void getJSONFromLink(final String link) {
        try {
            GetJSONResponse task = new GetJSONResponse();
            task.execute(link);
            responseFromServer = task.get();
            setModel(responseFromServer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_activity, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sortMenuOption:
                SortDialog dialog = new SortDialog();
                //setting fragment arguments so that the dialog knows which radiobutton to "turn on" at the moment
                Bundle bundle = new Bundle();
                bundle.putBoolean(IS_POPULAR_SORT, popularSort);
                dialog.setArguments(bundle);
                //to receive data back from the DialogFragment
                dialog.setTargetFragment(this, SORT_DIALOG_REQ_CODE);
                dialog.show(getFragmentManager(), "SortDialogFragment");
                break;

            case R.id.creditsMenuOption:
                CreditsDialog creditsDialog = new CreditsDialog();
                creditsDialog.show(getFragmentManager(), "CreditsDialogFragment");
                break;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case SORT_DIALOG_REQ_CODE:
                boolean popularSelected = data.getBooleanExtra(SortDialog.INTENT_IS_POPULAR, true);

                //only if the user has changed the sort mechanism, do the following
                if (popularSort != popularSelected) {
                    if(isConnectedToInternet()) {
                        if (popularSelected) {
                            //get the JSON file for high rated movies and set the JSONResponse singleton
                            getJSONFromLink(REQUEST_POPULARITY);
                            startGridView();
                        } else {
                            getJSONFromLink(REQUEST_RATING);
                            startGridView();
                        }

                        popularSort = !popularSort;
                    }else{
                        Toast.makeText(getContext(), R.string.not_connected, Toast.LENGTH_LONG).show();
                    }
                }

                break;
            case DETAILS_REQUEST_CODE:
                int index = data.getExtras().getInt(MovieDetailsActivity.RESTORE_POSITION);
                gridView.setSelection(index);
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * Uses Gson JSON deserialization library to map the data in the JSON string to the MovieModel instance and then initializes the JSONResponse
     * singleton with this instance
     * @param toSet the JSON data got from themoviedb
     */
    private void setModel(String toSet) {
        Gson gson = new GsonBuilder().create();
        MovieModel movieModel = gson.fromJson(toSet, MovieModel.class);
        JSONResponse.setModel(movieModel);
        //Unit test done. Fetches GSON and loads it onto the singleton.
    }


    /**
     * A helper class to fetch the JSON file from movieDB in the background thread.
     * The String parameter(1st argument) passed in will be the URL from which it will try to fetch the data from.
     */
    private class GetJSONResponse extends AsyncTask<String, Void, String> {

        URL url;
        HttpURLConnection connection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                result = receiveData(connection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        private String receiveData(InputStream inputStream) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer result = new StringBuffer();

            try {
                String read = null;
                while ((read = reader.readLine()) != null) {
                    result.append(read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result.toString();
        }


    }



    private boolean isConnectedToInternet() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);

        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            return info != null && info.isConnectedOrConnecting();
        }

        return false;
    }

}
