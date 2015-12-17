package udacity.flash.discovermovies;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String INT_ARGS_KEY = "udacity.flash.discovermovies_INT_ARGS_KEY";
    public static final String RESTORE_POSITION = "udacity.flash.discovermovies_RESTORE_POSITION";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        //gets the index of the movie whose details have to be displayed
        int index = getIntent().getExtras().getInt(MainActivityFragment.INDEX_KEY);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add( R.id.movieDetailsLinearParent, MovieDetailsFragment.getInstance(index) ).commit();


        //sending the index back so that the grid view is set to the correct position
        Intent intent = new Intent();
        intent.putExtra(RESTORE_POSITION, index);
        setResult(RESULT_OK, intent);
    }


}
