package udacity.flash.discovermovies;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

/**
 * Created by Prasanna Lakkur Subramanyam on 12/13/2015.
 */
public class SortDialog extends DialogFragment {
    View v;
    RadioButton popularity, rating;

    public static final String INTENT_IS_POPULAR = "udacity.flash.discovermovies_INTENT_IS_POPULAR";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        v = inflater.inflate(R.layout.sort_dialog, null);

        popularity = (RadioButton)v.findViewById(R.id.sortByPopularity);
        rating = (RadioButton)v.findViewById(R.id.sortByRating);

        //when one is selected, the other should automatically be deselected
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popularity.setChecked(false);
            }
        });
        popularity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating.setChecked(false);
            }
        });

        //get the argument which contains the current sort selection, and set it
        if( getArguments().getBoolean(MainActivityFragment.IS_POPULAR_SORT)  ){
            popularity.setChecked(true);
        }else{
            rating.setChecked(true);
        }


        //get the args and set the correct radioButton on

        return new AlertDialog.Builder(getContext()).setTitle(R.string.sort_title).setView(v).setPositiveButton(R.string.positive_button,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();

                        if (popularity.isChecked()) {
                            intent.putExtra(INTENT_IS_POPULAR, true);
                        } else {
                            intent.putExtra(INTENT_IS_POPULAR, false);
                        }

                        getTargetFragment().onActivityResult(MainActivityFragment.SORT_DIALOG_REQ_CODE, 1, intent);

                    }
                }).create();
    }
}
