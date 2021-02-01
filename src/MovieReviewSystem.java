import exception.MovieReviewException;
import helper.CommonUtils;
import enums.Genre;
import entities.Movie;
import entities.Review;
import entities.User;
import service.MovieService;
import service.ReviewService;
import service.UserService;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Review System is managed by this class
 */
public class MovieReviewSystem {

    UserService userService;
    MovieService movieService;
    ReviewService reviewService;
    Scanner scanner;

    public void displayChoices(){

        System.out.println("-1) Init Data");
        System.out.println("1) Add a user");
        System.out.println("2) Add a Movie");
        System.out.println("3) Review a Movie");
        System.out.println("4) List top n movies by total review score by ‘viewer’ in year of release");
        System.out.println("5) List top n movies by total review score by ‘viewer’ by genre.");
        System.out.println("6) List top n movies by total review score by ‘critics’ in year of release.");
        System.out.println("7) List top n movies by total review score by ‘critics’ in a particular genre.");
        System.out.println("8) Average review score in a particular year of release");
        System.out.println("9) Average review score in a particular genre");
        System.out.println("10) Average review score for a particular movie.");
        System.out.println("11) List All Users");
        System.out.println("12) List All Movies");
        System.out.println("13) List All Reviews");
        System.out.println("0 TO EXIT");
        CommonUtils.printSeperationLine();
        System.out.println("ENTER OPTION:");
    }

    public void selectChoice(int choice) throws MovieReviewException {
        switch (choice){
            case -1:
                initData();
                break;
            case 1:
                userService.addUser();
                break;
            case 2:
                movieService.addMovie();
                break;
            case 3:
                reviewService.addReview();
                break;
            case 4:
                movieService.findTopNMoviesByYearOfReleaseByViewers();
                break;
            case 5:
                movieService.findTopNMoviesByGenreByViewers();
                break;
            case 6:
                movieService.findTopNMoviesByYearOfReleaseReviewedByCritics();
                break;
            case 7:
                movieService.findTopNMoviesByGenreByViewersReviewedByCritics();
                break;
            case 8:
                reviewService.findAverageRatingForYear();
                break;
            case 9:
                reviewService.findAverageRatingForGenre();
                break;
            case 10:
                reviewService.findAverageRatingForMovie();
                break;
            case 11:
                userService.listAll();
                break;
            case 12:
                movieService.listAll();
                break;
            case 13:
                reviewService.listAll();
                break;
            default:
                throw new MovieReviewException("Please Enter a Valid Choice");
        }
    }

    private void initData() {
        userService.addUser(new User("SRK"));
        userService.addUser(new User("Salman"));
        userService.addUser(new User("Deepika"));

        movieService.addMovie(new Movie("Don", Genre.ACTION, LocalDate.of(2006, 1,1)));
        movieService.addMovie(new Movie("Tiger",Genre.DRAMA, LocalDate.of(2008,01,01)));
        movieService.addMovie(new Movie("Padmaavat",Genre.COMEDY, LocalDate.of(2006,01,01)));
        movieService.addMovie(new Movie("Lunchbox",Genre.DRAMA, LocalDate.of(2022,01,01)));
        movieService.addMovie(new Movie("Guru",Genre.DRAMA, LocalDate.of(2006,01,01)));
        movieService.addMovie(new Movie("Metro",Genre.ROMANCE, LocalDate.of(2006,01,01)));


        Review review = new Review("SRK", "Don", 2d);
        reviewService.addReview(review);
        Review review2 = new Review("SRK", "Padmavaat", 8d);
        reviewService.addReview(review2);
        Review review3 = new Review("Salman", "Don", 5d);
        reviewService.addReview(review3);
        Review review4 = new Review("Deepika", "Don", 9d);
        reviewService.addReview(review4);
        Review review5 = new Review("Deepika", "Guru", 6d);
        reviewService.addReview(review5);
        Review review6 = new Review("SRK", "Tiger", 5d);
        reviewService.addReview(review6);
        Review review7 = new Review("SRK", "Metro", 7d);
        review7.setComputedRating(14d);
        reviewService.addReview(review7);
    }

    public MovieReviewSystem() {
        userService = new UserService();
        movieService = new MovieService(userService);
        reviewService = new ReviewService(userService, movieService);
        movieService.setReviewService(reviewService);
        scanner = new Scanner(System.in);
    }
}
