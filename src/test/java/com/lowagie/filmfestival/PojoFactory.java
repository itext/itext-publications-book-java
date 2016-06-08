/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package com.lowagie.filmfestival;

import com.lowagie.database.DatabaseConnection;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A factory that makes it easy to query the database using
 * a series of static methods.
 */
public class PojoFactory {
    /** SQL statement to get all the movies of the festival. */
    public static final String MOVIES =
        "SELECT m.id, m.title, m.original_title, m.imdb, m.year, m.duration, "
        + "e.year, c.name, c.keyword, c.color "
        + "FROM film_movietitle m, festival_entry e, festival_category c "
        + "WHERE m.id = e.film_id AND e.category_id = c.id "
        + "ORDER BY m.title";
    /** SQL statement to get the directors of a specific movie. */
    public static final String DIRECTORS =
        "SELECT d.name, d.given_name "
        + "FROM film_director d, film_movie_director md "
        + "WHERE md.film_id = ? AND md.director_id = d.id";
    /** SQL statement to get the movies of a specific director. */
    public static final String MOVIEDIRECTORS =
        "SELECT m.id, m.title, m.original_title, m.imdb, m.year, m.duration "
        + "FROM film_movietitle m, film_movie_director md "
        + "WHERE md.director_id = ? AND md.film_id = m.id "
        + "ORDER BY m.title";
    /** SQL statement to get the countries of a specific movie. */
    public static final String COUNTRIES =
        "SELECT c.country "
        + "FROM film_country c, film_movie_country mc "
        + "WHERE mc.film_id = ? AND mc.country_id = c.id";
    /** SQL statement to get the movies from a specific country. */
    public static final String MOVIECOUNTRIES =
        "SELECT m.id, m.title, m.original_title, m.imdb, m.year, m.duration "
        + "FROM film_movietitle m, film_movie_country mc "
        + "WHERE mc.country_id = ? AND mc.film_id = m.id "
        + "ORDER BY m.title";
    /** SQL statement to get all the days of the festival. */
    public static final String DAYS =
        "SELECT DISTINCT day FROM festival_screening ORDER BY day";
    /** SQL statament to get all the locations at the festival */
    public static final String LOCATIONS =
        "SELECT DISTINCT location FROM festival_screening ORDER by location";
    /** SQL statement to get screenings. */
    public static final String SCREENINGS =
        "SELECT m.title, m.original_title, m.imdb, m.year, m.duration,"
        + "s.day, s.time, s.location, s.press, "
        + "e.year, c.name, c.keyword, c.color, m.id "
        + "FROM festival_screening s, film_movietitle m, "
        + "festival_entry e, festival_category c "
        + "WHERE day = ? AND s.film_id = m.id "
        + "AND m.id = e.film_id AND e.category_id = c.id";
    /** SQL statement to get screenings. */
    public static final String MOVIESCREENINGS =
        "SELECT s.day, s.time, s.location, s.press "
        + "FROM festival_screening s "
        + "WHERE s.film_id = ?";
    /** SQL statement to get screenings. */
    public static final String PRESS =
        "SELECT m.title, m.original_title, m.imdb, m.year, m.duration,"
        + "s.day, s.time, s.location, s.press, "
        + "e.year, c.name, c.keyword, c.color, m.id "
        + "FROM festival_screening s, film_movietitle m, "
        + "festival_entry e, festival_category c "
        + "WHERE s.press=1 AND s.film_id = m.id "
        + "AND m.id = e.film_id AND e.category_id = c.id "
        + "ORDER BY day, time ASC";
    
    /**
     * Fills a Movie POJO using a ResultSet.
     * @param rs a ResultSet with records from table film_movietitle
     * @return a Movie POJO
     * @throws UnsupportedEncodingException 
     */
    public static Movie getMovie(ResultSet rs)
        throws SQLException, UnsupportedEncodingException {
        Movie movie = new Movie();
        movie.setTitle(rs.getString("title"));
        if (rs.getObject("original_title") != null)
            movie.setOriginalTitle(
               rs.getString("original_title"));
        movie.setImdb(rs.getString("imdb"));
        movie.setYear(rs.getInt("year"));
        movie.setDuration(rs.getInt("duration"));
        return movie;
    }
    
    /**
     * Fills a Director POJO using a ResultSet.
     * @param rs a ResultSet with records from table file_director
     * @return a Director POJO
     */
    public static Director getDirector(ResultSet rs)
        throws SQLException, UnsupportedEncodingException {
        Director director = new Director();
        director.setName(rs.getString("name"));
        director.setGivenName(rs.getString("given_name"));
        return director;
    }
    
    /**
     * Fills a Country POJO using a ResultSet.
     * @param rs a ResultSet with records from table file_director
     * @return a Director POJO
     */
    public static Country getCountry(ResultSet rs) throws SQLException {
        Country country = new Country();
        country.setCountry(rs.getString("country"));
        return country;
    }
    
    /**
     * Fills an Entry POJO using a ResultSet.
     * @param rs a resultSet with records from table festival_entry
     * @return an Entry POJO
     */
    public static Entry getEntry(ResultSet rs) throws SQLException {
        Entry entry = new Entry();
        entry.setYear(rs.getInt("year"));
        return entry;
    }
    
    /**
     * Fills a Category POJO using a ResultSet.
     * @param rs a resultSet with records from table festival_category
     * @return a Category POJO
     */
    public static Category getCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setName(rs.getString("name"));
        category.setKeyword(rs.getString("keyword"));
        category.setColor(rs.getString("color"));
        return category;
    }
    
    /**
     * Fills a Screening POJO using a ResultSet.
     * @param rs a ResultSet with records from table festival_screening
     * @return a Screening POJO
     */
    public static Screening getScreening(ResultSet rs) throws SQLException {
        Screening screening = new Screening();
        screening.setDate(rs.getDate("day"));
        screening.setTime(rs.getTime("time"));
        screening.setLocation(rs.getString("location"));
        screening.setPress(rs.getInt("press") == 1);
        return screening;
    }

    /**
     * Returns a list with Screening objects, if you pass
     * a DatabaseConnection and a day.
     * @param connection a connection to the film festival database
     * @param day a day (java.sql.Date)
     * @return a List of Screening POJOs
     * @throws UnsupportedEncodingException 
     */
    public static List<Screening> getScreenings(
        DatabaseConnection connection, Date day)
        throws SQLException, UnsupportedEncodingException {
        List<Screening> list = new ArrayList<>();
        PreparedStatement stm =
            connection.createPreparedStatement(SCREENINGS);
        stm.setDate(1, day);
        ResultSet rs = stm.executeQuery();
        Screening screening;
        Movie movie;
        Entry entry;
        Category category;
        while (rs.next()) {
            screening = getScreening(rs);
            movie = getMovie(rs);
            for (Director director :
                getDirectors(connection, rs.getInt("id"))) {
                movie.addDirector(director);
            }
            for (Country country :
                getCountries(connection, rs.getInt("id"))) {
                movie.addCountry(country);
            }
            entry = getEntry(rs);
            category = getCategory(rs);
            entry.setCategory(category);
            entry.setMovie(movie);
            movie.setEntry(entry);
            screening.setMovie(movie);
            list.add(screening);
        }
        stm.close();
        return list;
    }

    /**
     * Returns a list with Screening objects, if you pass
     * a DatabaseConnection and a day.
     * @param connection a connection to the film festival database
     * @param film_id a movie id
     * @return a List of Screening POJOs
     * @throws UnsupportedEncodingException 
     */
    public static List<Screening> getScreenings(
        DatabaseConnection connection, int film_id)
        throws SQLException, UnsupportedEncodingException {
        List<Screening> list = new ArrayList<>();
        PreparedStatement stm =
            connection.createPreparedStatement(MOVIESCREENINGS);
        stm.setInt(1, film_id);
        ResultSet rs = stm.executeQuery();
        Screening screening;
        while (rs.next()) {
            screening = getScreening(rs);
            list.add(screening);
        }
        stm.close();
        return list;
    }

    /**
     * Returns a list with Screening objects, if you pass
     * a DatabaseConnection and a day.
     * @param connection a connection to the film festival database
     * @param day a day (java.sql.Date)
     * @return a List of Screening POJOs
     * @throws UnsupportedEncodingException 
     */
    public static List<Screening> getPressPreviews(DatabaseConnection connection)
        throws SQLException, UnsupportedEncodingException {
        List<Screening> list = new ArrayList<>();
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(PRESS);
        Screening screening;
        Movie movie;
        Entry entry;
        Category category;
        while (rs.next()) {
            screening = getScreening(rs);
            movie = getMovie(rs);
            for (Director director : getDirectors(connection, rs.getInt("id"))) {
                movie.addDirector(director);
            }
            for (Country country : getCountries(connection, rs.getInt("id"))) {
                movie.addCountry(country);
            }
            entry = getEntry(rs);
            category = getCategory(rs);
            entry.setCategory(category);
            entry.setMovie(movie);
            movie.setEntry(entry);
            screening.setMovie(movie);
            list.add(screening);
        }
        stm.close();
        return list;
    }
    
    /**
     * Returns a list with Movie objects.
     * @param connection a connection to the filmfestival database
     * @return a List of Screening POJOs
     * @throws SQLException 
     * @throws UnsupportedEncodingException 
     */
    public static List<Movie> getMovies(DatabaseConnection connection)
        throws SQLException, UnsupportedEncodingException {
        List<Movie> list = new ArrayList<>();
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(MOVIES);
        Movie movie;
        Entry entry;
        Category category;
        while (rs.next()) {
            movie = getMovie(rs);
            entry = getEntry(rs);
            category = getCategory(rs);
            entry.setCategory(category);
            for (Screening screening : getScreenings(connection, rs.getInt("id"))) {
                entry.addScreening(screening);
            }
            movie.setEntry(entry);
            for (Director director : getDirectors(connection, rs.getInt("id"))) {
                movie.addDirector(director);
            }
            for (Country country : getCountries(connection, rs.getInt("id"))) {
                movie.addCountry(country);
            }
            list.add(movie);
        }
        return list;
    }
    
    /**
     * Returns a list with Director objects.
     * @param connection a connection to the filmfestival database
     * @param director_id the id of a movie
     * @return a List of Screening POJOs
     * @throws SQLException 
     * @throws UnsupportedEncodingException 
     */
    public static List<Director> getDirectors(
        DatabaseConnection connection, int movie_id)
        throws SQLException, UnsupportedEncodingException {
        List<Director> list = new ArrayList<>();
        PreparedStatement directors =
            connection.createPreparedStatement(DIRECTORS);
        directors.setInt(1, movie_id);
        ResultSet rs = directors.executeQuery();
        while (rs.next()) {
            list.add(getDirector(rs));
        }
        return list;
    }
    
    /**
     * Returns a list with Country objects.
     * @param connection a connection to the filmfestival database
     * @param movie_id the id of a movie
     * @return a List of Screening POJOs
     * @throws SQLException 
     * @throws UnsupportedEncodingException 
     */
    public static List<Country> getCountries(
        DatabaseConnection connection, int movie_id)
        throws SQLException, UnsupportedEncodingException {
        List<Country> list = new ArrayList<>();
        PreparedStatement countries =
            connection.createPreparedStatement(COUNTRIES);
        countries.setInt(1, movie_id);
        ResultSet rs = countries.executeQuery();
        while (rs.next()) {
            list.add(getCountry(rs));
        }
        return list;
    }
    
    /**
     * Returns a list with Movie objects.
     * @param connection a connection to the filmfestival database
     * @param director_id the id of a director
     * @return a List of Screening POJOs
     * @throws SQLException 
     * @throws UnsupportedEncodingException 
     */
    public static List<Movie> getMovies(
        DatabaseConnection connection, int director_id)
        throws SQLException, UnsupportedEncodingException {
        List<Movie> list = new ArrayList<>();
        PreparedStatement movies =
            connection.createPreparedStatement(MOVIEDIRECTORS);
        movies.setInt(1, director_id);
        ResultSet rs = movies.executeQuery();
        Movie movie;
        while (rs.next()) {
            movie = getMovie(rs);
            for (Country country : getCountries(connection, rs.getInt("id"))) {
                movie.addCountry(country);
            }
            list.add(movie);
        }
        return list;
    }
    
    /**
     * Returns a list with Movie objects.
     * @param connection a connection to the filmfestival database
     * @param country_id the id of a country
     * @return a List of Screening POJOs
     * @throws SQLException 
     * @throws UnsupportedEncodingException 
     */
    public static List<Movie> getMovies(
       DatabaseConnection connection, String country_id)
        throws SQLException, UnsupportedEncodingException {
        List<Movie> list = new ArrayList<>();
        PreparedStatement movies =
            connection.createPreparedStatement(MOVIECOUNTRIES);
        movies.setString(1, country_id);
        ResultSet rs = movies.executeQuery();
        while (rs.next()) {
            Movie movie = getMovie(rs);
            for (Director director : getDirectors(connection, rs.getInt("id"))) {
                movie.addDirector(director);
            }
            list.add(movie);
        }
        return list;
    }
    
    /**
     * Returns an ArrayList containing all the filmfestival days.
     * @param connection a connection to the database.
     * @return a list containing dates.
     */
    public static List<Date> getDays(DatabaseConnection connection)
        throws SQLException {
        List<Date> list = new ArrayList<>();
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(DAYS);
        while (rs.next()) {
            list.add(rs.getDate("day"));
        }
        stm.close();
        return list;
    }
    
    /**
     * Returns an ArrayList containing all the screening locations.
     * @param connection a connection to the database.
     * @return a list containing location codes.
     */
    public static List<String> getLocations(DatabaseConnection connection)
        throws SQLException {
        List<String> list = new ArrayList<>();
        Statement stm = connection.createStatement();
        ResultSet rs =
            stm.executeQuery(LOCATIONS);
        while (rs.next()) {
            list.add(rs.getString("location"));
        }
        stm.close();
        return list;
    }
}
