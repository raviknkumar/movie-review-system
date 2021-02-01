package service;

import enums.Genre;
import enums.Role;
import exception.MovieReviewException;
import entities.*;
import models.MovieFilter;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * service
 * used to handle business logic regarding reviews
 */
public class ReviewService {

    private List<Review> reviews= new ArrayList<>();
    Scanner scanner;
    private UserService userService;
    private MovieService movieService;

    public ReviewService(UserService userService, MovieService movieService) {
        scanner = new Scanner(System.in);
        this.userService = userService;
        this.movieService = movieService;
    }

    public Review addReview() throws MovieReviewException {

        System.out.println("To Add Review, please enter the User Name:");
        String userName = scanner.nextLine();
        User user = userService.findByUserName(userName);

        System.out.println("Enter Movie Name");
        String movieName = scanner.nextLine();

        Optional<Movie> movieOptional = movieService.findByName(movieName);
        if (!movieOptional.isPresent()){
            throw new MovieReviewException("No Movie found with name: "+ movieName);
        }

        Movie movie =movieOptional.get();
        LocalDate currentDate = LocalDate.now();
        if(movie.getReleaseDate().isAfter(currentDate)){
            throw new MovieReviewException("Movie yet to be released, you can't review now");
        }

        Optional<Review> reviewOptional = findByUserNameAndMovieName(userName, movieName);
        if(reviewOptional.isPresent()){
            throw new MovieReviewException("User "+userName+" already reviewed the movie:" +movieName);
        }

        System.out.println("Enter Rating");
        double rating = scanner.nextDouble();
        Role role = user.getRole();
        Integer ratingMultipler = Role.findRatingMultipler(role);

        Review review = new Review(movieName, userName, rating, rating*ratingMultipler);
        reviews.add(review);

        promoteUserIfApplicable(user);

        return review;
    }

    public Review addReview(Review review){
        reviews.add(review);
        return review;
    }

    private void promoteUserIfApplicable(User user) {
        List<Review> reviews = this.reviews.stream().filter(review -> review.getUserName().equalsIgnoreCase(user.getName()))
                .collect(Collectors.toList());
        userService.promoteUserIfApplicable(user, reviews);
    }

    public Optional<Review> findByUserNameAndMovieName(String userName, String movieName){
        return reviews.stream()
                .filter(r -> r.getUserName().equalsIgnoreCase(userName)
                 && r.getMovieName().equalsIgnoreCase(movieName))
                .findFirst();
    }

    public void listAll() {
        for (Review review : reviews)
            System.out.println(review);
    }

    public void findAverageRatingForYear(){
        System.out.println("Enter Year");
        int year = scanner.nextInt();
        System.out.println(averageRatingForYear(year));
    }

    public Double averageRatingForYear(int year){
        MovieFilter movieFilter = new MovieFilter();
        movieFilter.setYear(year);
       return getAverageRatingByMovieFilter(movieFilter);
    }

    public Double getAverageRatingByMovieFilter(MovieFilter movieFilter){

        List<Movie> movies = movieService.getMoviesByFilter(movieFilter);
        if(movies.isEmpty()){
            return 0d;
        }

        Map<String, List<Review>> movieNameToReviewsMap = reviews.stream()
                .collect(Collectors.groupingBy(Review::getMovieName));

        double totalRating=0d;
        int totalReviews = 0;

        for (Movie movie : movies){
            List<Review> reviews = movieNameToReviewsMap.getOrDefault(movie.getName(), new ArrayList<>());
            totalReviews += reviews.size();
            totalRating += reviews.stream().mapToDouble(Review::getComputedRating).sum();
        }
        return totalRating / totalReviews;
    }

    public void findAverageRatingForGenre() {
        System.out.println("Enter genre");
        String genreStr = scanner.next();
        Genre genre = Genre.findByDescription(genreStr);
        MovieFilter movieFilter = new MovieFilter();
        movieFilter.setGenre(genre);
        Double averageRating = getAverageRatingByMovieFilter(movieFilter);
        System.out.println("Average Rating For Genre "+ genreStr + " is "+ averageRating);
    }

    public void findAverageRatingForMovie() throws MovieReviewException {
        System.out.println("Enter Movie Name");
        String movieName = scanner.next();
        Optional<Movie> movieOptional = movieService.findByName(movieName);
        if(!movieOptional.isPresent()){
            throw new MovieReviewException("No Movie found with name: "+ movieName);
        }

        Double averageRatingForMovie = findAverageRatingForMovieAndRole(movieName, null);
        System.out.println(averageRatingForMovie);
    }

    public Double findAverageRatingForMovieAndRole(String movieName, Role role) {

        List<Review> reviewsForMovie = reviews.stream()
                .filter(review -> review.getMovieName().equalsIgnoreCase(movieName))
                .collect(Collectors.toList());

        if(role != null){
            List<User> users = userService.findByRole(role);
            Set<String> userNames = users.stream().map(User::getName).collect(Collectors.toSet());

            reviewsForMovie = reviewsForMovie.stream()
                    .filter(review -> userNames.contains(review.getUserName()))
                    .collect(Collectors.toList());
        }

        double rating = reviewsForMovie.stream().mapToDouble(Review::getRating).sum();
        return rating / reviewsForMovie.size();
    }

    /**
     * if no criteria applies, send list as null
     * @param movies
     * @param users
     * @return
     */
    public List<Review> findByMoviesAndUsers(List<Movie> movies, List<User> users) {

        List<Review> filteredReviews = new ArrayList<>(reviews);

        if(movies != null && !movies.isEmpty()){
            Set<String> movieNames = movies.stream()
                    .map(Movie::getName)
                    .collect(Collectors.toSet());
            filteredReviews = filteredReviews.stream()
                    .filter(r -> movieNames.contains(r.getMovieName()))
                    .collect(Collectors.toList());

        }

        if(users != null && !users.isEmpty()){
            List<String> userNames = users.stream()
                    .map(User::getName)
                    .collect(Collectors.toList());
            filteredReviews = filteredReviews.stream()
                    .filter(r -> userNames.contains(r.getUserName()))
                    .collect(Collectors.toList());
        }

        return filteredReviews;
    }
}
