package enums;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * genre enum, add other genres if required
 */
public enum Genre {
    ACTION("Action"),
    DRAMA("Drama"),
    COMEDY("Comedy"),
    ROMANCE("Romance");

    public static Genre findByDescription(String genre) {
        return Arrays.stream(Genre.values())
                .filter(g -> g.description.equalsIgnoreCase(genre))
                .findFirst().orElse(null);
    }

    public String getDescription() {
        return description;
    }

    private final String description;

    Genre(String description) {
        this.description = description;
    }

    public static void printAvailableGenres(){
        System.out.println("Available Genres are: ");
        String genresList = Arrays.stream(Genre.values()).map(g -> g.description).collect(Collectors.joining(", "));
        System.out.println(genresList);
    }
}
