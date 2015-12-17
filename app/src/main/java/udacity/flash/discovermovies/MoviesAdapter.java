package udacity.flash.discovermovies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Flash on 12/13/2015.
 */
public class MoviesAdapter extends ArrayAdapter<Result> {

    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE = "w185/";

    public MoviesAdapter(Context context, List<Result> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //get the link to the corresponding image
        Result currentResult = getItem(position);

        final String requestString = IMAGE_BASE_URL + IMAGE_SIZE + currentResult.getPosterPath();


        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.element_grid, parent, false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.gridImage);
        //fetch the image
        Picasso.with(getContext()).load(requestString).into( imageView );

//        //set an onClick listener on the ImageView to go to the details activity
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //put the position of the result element as an extra and start the details activity
//                Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
//                intent.putExtra(MainActivity.INDEX_KEY, position);
//                getContext().startActivity(intent);
//            }
//        });


        return convertView;
    }




}
