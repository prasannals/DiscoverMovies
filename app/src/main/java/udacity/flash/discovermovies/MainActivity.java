package udacity.flash.discovermovies;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * The Launcher activity of the Discover Movies application. It checks if the device is connected to the internet. If yes, it starts the MainActivityFragment
 * instance. In no, it displays a Toast to the user.
 */
public class MainActivity extends AppCompatActivity {


    MainActivityFragment fragment;

    public final String FRAG_TAG = "MAIN_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isConnectedToInternet()) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            //if savedInstanceState isnt null, we don't have to create a new instance of the Fragment(will be a waste of memory). Hence, we
            //get the previous instance using it's String tag
            if (savedInstanceState != null) {
                fragment = (MainActivityFragment) manager.findFragmentByTag(FRAG_TAG);
            } else if (fragment == null) {
                fragment = new MainActivityFragment();
            }

            if (!fragment.isInLayout()) {
                transaction.replace(R.id.mainActivityLinearLayout, fragment, FRAG_TAG).commit();
            }

        } else {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_LONG).show();
        }
    }

    /**
     *
     * @return true if the device is connected to the internet, false otherwise.
     */
    private boolean isConnectedToInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            return info != null && info.isConnectedOrConnecting();
        }

        return false;
    }


}
