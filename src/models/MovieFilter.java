package models;

import enums.Genre;

/**
 * movie filter
 * used to filter on movie data
 */
public class MovieFilter {
    private String name;
    private Integer year;
    private Genre genre;

    public MovieFilter() {
    }

    public MovieFilter(String name, Integer year, Genre genre) {
        this.name = name;
        this.year = year;
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
