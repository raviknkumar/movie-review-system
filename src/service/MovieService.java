package service;

import enums.Genre;
import enums.Role;
import exception.MovieReviewException;
import entities.*;
import models.MovieFilter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * service
 * used to handle business logic regarding movies
 */
public class MovieService {

    ArrayList<Movie> movies;
    Scanner scanner;
    DateTimeFormatter dateTimeFormatter ;
    private ReviewService reviewService;
    private UserService userService;

    public MovieService(UserService userService) {
        movies = new ArrayList<>();
        scanner = new Scanner(System.in);
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.userService = userService;
    }

    public void setReviewService(ReviewService reviewService) {
        this.reviewService =reviewService;
    }

    /**
     * enter name and date of release
     * to enter full date enter DD-MM-YYYY
     * else for only year, enter YYYY alone, date and month defaulted to 01, 01
     * @return
     * @throws MovieReviewException
     */
    public Movie addMovie() throws MovieReviewException {

        System.out.println("To Add Movie, please enter the Name");
        Movie movie = new Movie();
        String name = scanner.nextLine();
        movie.setName(name);

        checkIfAlreadyExists(name);

        System.out.println("Please Enter Date Of Release (DD-MM-YYYY)/ (YYYY)");
        String date = scanner.nextLine();

        LocalDate movieDate;
        try {
            if(date.length() == 4){
                date = "01-01-"+date;
            }
            movieDate = LocalDate.parse(date, dateTimeFormatter);
        } catch (Exception exception){
            throw new MovieReviewException("Date Format is invalid, please check again");
        }
        movie.setReleaseDate(movieDate);

        System.out.println("Please Enter Genre");
        Genre.printAvailableGenres();
        String genreStr = scanner.nextLine();
        Genre genre = Genre.findByDescription(genreStr);
        if(genre == null){
            throw new MovieReviewException("Please Enter a valid genre");
        }
        movie.setGenre(genre);

        movies.add(movie);
        return movie;
    }

    public Movie addMovie(Movie movie){
        movies.add(movie);
        return movie;
    }

    private void checkIfAlreadyExists(String name) throws MovieReviewException {
        Optional<Movie> movieOptional = findByName(name);
        if(movieOptional.isPresent()){
            throw new MovieReviewException("Movie with name: "+ name + " already exists");
        }
    }

    public void listAll() {
        for (Movie movie: movies){
            System.out.println(movie);
        }
    }

    public Optional<Movie> findByName(String movieName){
        return movies.stream()
                .filter(movie -> movie.getName().equalsIgnoreCase(movieName))
                .findFirst();
    }

    public List<Movie> getMoviesByFilter(MovieFilter movieFilter){

        if(movieFilter == null)
            return Collections.emptyList();

        if(movieFilter.getYear() != null)
            return getMoviesByReleaseYear(movieFilter.getYear());

        if(movieFilter.getGenre() != null)
            return getMoviesByGenre(movieFilter.getGenre());

        if(movieFilter.getName() != null){
            Optional<Movie> movieOptional = findByName(movieFilter.getName());
            if (movieOptional.isPresent())
                return Collections.singletonList(movieOptional.get());
        }

        return Collections.emptyList();
    }

    private List<Movie> getMoviesByGenre(Genre genre) {
        return movies.stream()
                .filter(movie -> movie.getGenre() == genre)
                .collect(Collectors.toList());
    }

    public List<Movie> getMoviesByReleaseYear(int year){
        return movies.stream()
                .filter(m -> m.getReleaseDate().getYear() == year)
                .collect(Collectors.toList());
    }

    public void findTopNMoviesByYearOfReleaseByViewers() {

        System.out.println("Enter Year");
        int year = scanner.nextInt();
        System.out.println("Enter N number of movies: ");
        int n = scanner.nextInt();

        MovieFilter movieFilter = new MovieFilter();
        movieFilter.setYear(year);

        findByMovieFilterAndRoleAndOrderAndN(movieFilter, Role.VIEWER,
                Comparator.reverseOrder(), n);
    }

    public void findTopNMoviesByGenreByViewers() {
        System.out.println("Enter Genre");
        Genre.printAvailableGenres();
        String genreStr= scanner.nextLine();
        Genre genre = Genre.findByDescription(genreStr);

        System.out.println("Enter N number of movies: ");
        int n = scanner.nextInt();

        MovieFilter movieFilter = new MovieFilter();
        movieFilter.setGenre(genre);

        findByMovieFilterAndRoleAndOrderAndN(movieFilter, Role.VIEWER,
                Comparator.reverseOrder(), n);
    }

    public void findTopNMoviesByYearOfReleaseReviewedByCritics() {

        System.out.println("Enter Genre");
        Genre.printAvailableGenres();
        String genreStr= scanner.nextLine();
        Genre genre = Genre.findByDescription(genreStr);

        System.out.println("Enter N number of movies: ");
        int n = scanner.nextInt();

        MovieFilter movieFilter = new MovieFilter();
        movieFilter.setGenre(genre);

        findByMovieFilterAndRoleAndOrderAndN(movieFilter, Role.CRITIC, Comparator.reverseOrder(), n);
    }

    public void findTopNMoviesByGenreByViewersReviewedByCritics() {
        System.out.println("Enter Year");
        int year = scanner.nextInt();
        System.out.println("Enter N number of movies: ");
        int n = scanner.nextInt();

        MovieFilter movieFilter = new MovieFilter();
        movieFilter.setYear(year);

        findByMovieFilterAndRoleAndOrderAndN(movieFilter, Role.CRITIC, Comparator.reverseOrder(), n);
    }

    /**
     * print reviews for any kind of filter
     *
     * MovieFilter: filter on any one parameter of movies: eg: Genre / yearOfRelease
     *
     * Role: to filter by user role, if null all users are considered, else user with specific roles are considered
     *
     * comparator: to sort the results
     * if provided as naturalOrder gives bottom n results
     * if provided as reverseOrder, gives TOP n results
     *
     * n is count of results
     * if provided a valid number restricts to particular number of results
     * if null, doesnot restrict on the number of results
     *
     * @param movieFilter
     * @param role - null / CRITIC / VIEWER / any valid role
     * @param comparator
     * @param n
     */
    public void findByMovieFilterAndRoleAndOrderAndN(MovieFilter movieFilter, Role role, Comparator<Double> comparator,
                                                     Integer n) {

        // find movies if movieFilter is applicable
        List<Movie> movies = getMoviesByFilter(movieFilter);

        // find users
        List<User> users = userService.findByRole(role);

        // find ratings of all movies rated by all users
        List<Review> filteredReviews = reviewService.findByMoviesAndUsers(movies, users);
        Map<String, List<Review>> movieToReviewsMap = filteredReviews.stream()
                .collect(Collectors.groupingBy(Review::getMovieName));

        TreeMap<Double, List<Movie>> scoreToMovieMap = new TreeMap<>(comparator);

        // sort it by rating
        for (Movie movie : movies){
            List<Review> reviewsForMovie = movieToReviewsMap.getOrDefault(movie.getName(), new ArrayList<>());
            double totalRating = reviewsForMovie.stream()
                    .mapToDouble(Review::getComputedRating)
                    .sum();
            int size = reviewsForMovie.size();
            List<Movie> movieList = scoreToMovieMap.getOrDefault(totalRating, new ArrayList<>());
            movieList.add(movie);
            scoreToMovieMap.put(totalRating, movieList);
        }

        // print list
        int cnt=1;

        o: for (Map.Entry<Double, List<Movie>> ratingToMovieList : scoreToMovieMap.entrySet()) {
            List<Movie> movieList = ratingToMovieList.getValue();
            for(Movie movie : movieList){
                System.out.println("MovieName: "+ movie.getName() + ", Rating: "+ ratingToMovieList.getKey());
                cnt++;
                if(n != null && cnt > n)
                    break o;
            }
        }
    }
}
