package android.example.com.movieapiappp2;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Abbie on 16/02/2018.
 */

public class MovieApi {
    public interface MoviesApi {
        @GET("/movie/popular")
        void getPopularMovies(Callback<Movie.MovieResult> callback);

        @GET("/movie/top_rated")
        void getTopRatedMovies(Callback<Movie.MovieResult> callback);

        @GET("/movie/{id}/videos")
        void getMovieTrailer(@Path("id") String id, Callback<Movie.MovieResult> callback);

        @GET("/movie/{id}/reviews")
        void getMovieReviews(@Path("id") String id, Callback<Movie.MovieResult> callback);
    }
}
