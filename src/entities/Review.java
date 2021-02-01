package entities;

import java.util.Date;

/**
 * review entity
 * used to store the review details
 */
public class Review {
    private Integer id;
    private String movieName;
    private String userName;
    private Double rating;
    private Double computedRating;
    private Date reviewedOn;

    public Review(String movieName, String userName, Double rating, Double computedRating) {
        this.movieName = movieName;
        this.userName = userName;
        this.rating = rating;
        this.computedRating = computedRating;
    }

    public Review(String userName, String movieName, Double rating) {
        this.movieName = movieName;
        this.userName = userName;
        this.rating = rating;
        this.computedRating=rating;
    }

    public Review() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getComputedRating() {
        return computedRating;
    }

    public void setComputedRating(Double computedRating) {
        this.computedRating = computedRating;
    }

    public Date getReviewedOn() {
        return reviewedOn;
    }

    public void setReviewedOn(Date reviewedOn) {
        this.reviewedOn = reviewedOn;
    }

    @Override
    public String toString() {
        return "Review{" +
                "movieName='" + movieName + '\'' +
                ", userName='" + userName + '\'' +
                ", rating=" + rating +
                ", computedRating=" + computedRating +
                '}';
    }
}
