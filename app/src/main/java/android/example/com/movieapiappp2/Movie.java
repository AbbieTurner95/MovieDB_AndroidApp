package android.example.com.movieapiappp2;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by Abbie on 16/02/2018.
 */

public class Movie{
    public final static String MOVIE_IMG_URI = "http://image.tmdb.org/t/p/w500";
    private String title;

    @SerializedName("vote_average")
    private String avgVote;
    @SerializedName("release_date")
    private String relDate;
    @SerializedName("poster_path")
    private String poster;
    @SerializedName("overview")
    private String description;
    @SerializedName("id")
    public String id;
    @SerializedName("key")
    public String key;
    @SerializedName("author")
    public String author;
    @SerializedName("content")
    public String content;

    public static class MovieResult {
        private List<Movie> results;
        public List<Movie> getResults() {
            return results;
        }
    }

    public String getAuthor() { return author; }

    public String getContent() { return content; }

    public void setContent(String content){
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey(){
        return key;
    }

    public String getID(){
        return id;
    }

    public void setID(String id){
        this.id = id;
    }


    public String getPoster() {
        return MOVIE_IMG_URI + poster;
    }

    public String getPoster_path() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRelDate() {
        return relDate;
    }

    public void setRelDate(String relDate){
        this.relDate = relDate;
    }

    public String getAvgVote() {
        return avgVote;
    }

    public void setAvgVote(String avgVote){
        this.avgVote = avgVote;
    }
}