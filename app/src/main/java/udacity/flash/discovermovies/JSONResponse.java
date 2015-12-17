package udacity.flash.discovermovies;

/**
 * Created by Prasanna Lakkur Subramanyam on 12/13/2015.
 * This class is a singleton which contains the Movie Model class ( the class that maps to the response that we get when we query for
 * most popular/highest rated movies).
 *
 * Reasons for using a Singleton over Parcellable models -
 * 1. All the info contained here will be needed throughout the application. Hence, it doesn't make sense to spend extra time packaging and
 * extracting this data in between activities.
 * 2. All the fields are mapped because it'll be convenient if I decide to add some extra info in later versions. And the extra data isn't very large
 * and hence won't be a big issue.
 *
 */
public class JSONResponse {
    //instance of the singleton
    private static JSONResponse ourInstance = new JSONResponse();

    //instance of the Model for this application. This is the class that actually maps to the JSON data received.
    private MovieModel movieModel;


    /**
     * Use this method to get/modify existing singleton and set it's private MovieModel instance to the instance passed in.
     * @param movieModel - The movie model that the singleton should hold
     * @return instance of the singleton
     */
    public static JSONResponse getInstance(MovieModel movieModel) {
        ourInstance.movieModel = movieModel;

        return ourInstance;
    }

    /**
     * Use this method if you just want to set the movie model and get the singleton ready.
     * @param movieModel - the MovieModel object which the singleton should hold.
     */
    public static void setModel(MovieModel movieModel){
        ourInstance.movieModel = movieModel;
    }

    /**
     * Use this method to return the instance of the singleton. It is not recommended to use it before setting a movieModel using
     * the overloaded version of this method.
     * @return instance of the singleton
     */
    public static JSONResponse getInstance(){
        return ourInstance;
    }

    private JSONResponse() {
    }

    /**
     * Used to get the instance of the MovieModel object that the singleton has a reference to
     * @return the MovieModel object that the singleton has a reference to
     */
    public MovieModel getMovieModel(){
        return movieModel;
    }
}
