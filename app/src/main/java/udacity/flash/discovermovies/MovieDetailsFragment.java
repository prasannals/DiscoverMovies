package udacity.flash.discovermovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Flash on 12/13/2015.
 */
public class MovieDetailsFragment extends Fragment {
    ImageView posterPic;
    TextView movieTitle, movieDescription, movieDate, movieRating;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.movie_details_fragment, container, false);

        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        int index = bundle.getInt(MovieDetailsActivity.INT_ARGS_KEY);

        JSONResponse response = JSONResponse.getInstance();
        Result currentResult = response.getMovieModel().getResults().get(index);

        posterPic = (ImageView)v.findViewById(R.id.detailsMoviePoster);

        movieTitle = (TextView)v.findViewById(R.id.detailsMovieNameText);
        movieDescription = (TextView)v.findViewById(R.id.detailsMovieDescriptionText);
        movieDate = (TextView)v.findViewById(R.id.detailsReleaseDateText);
        movieRating = (TextView)v.findViewById(R.id.detailsRatingText);

        Picasso.with(getContext()).load( MoviesAdapter.IMAGE_BASE_URL + MoviesAdapter.IMAGE_SIZE + currentResult.getPosterPath()  ).into(posterPic);

        movieTitle.setText(currentResult.getOriginalTitle());
        movieDescription.setText(currentResult.getOverview());
        movieDate.setText(currentResult.getReleaseDate());
        movieRating.setText(currentResult.getVoteAverage() + "/10.0");


        return v;
    }


    /**
     *
     * @param index the index of the movie whose details have to be displayed
     * @return an instance of the MovieDetailsFragment configured to work with the Movie at the parameter provided
     */
    public static MovieDetailsFragment getInstance(int index){
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MovieDetailsActivity.INT_ARGS_KEY, index);
        fragment.setArguments(bundle);

        return fragment;
    }

}
