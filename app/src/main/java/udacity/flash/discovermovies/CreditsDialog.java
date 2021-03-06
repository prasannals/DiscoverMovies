package udacity.flash.discovermovies;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Prasanna Lakkur Subramanyam on 12/14/2015.
 * CreditsDialog class will be used to display the credits for the Discover Movies
 */
public class CreditsDialog extends DialogFragment {

    /**
     *
     * @param savedInstanceState - bundle containing the state info of the application
     * @return a dialog instance configured to contain all the views required to show credits
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getContext()).inflate(R.layout.credits_layout, null);
        ((TextView)v.findViewById(R.id.creditTextView) ).setText(R.string.credits);
        return new AlertDialog.Builder(getContext()).setTitle(R.string.credits_title).setView(v).setPositiveButton(R.string.positive_button, null).create();
    }
}
