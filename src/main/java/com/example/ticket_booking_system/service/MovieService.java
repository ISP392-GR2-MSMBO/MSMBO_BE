package com.example.ticket_booking_system.service;


import com.example.ticket_booking_system.entity.Movie;
import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
public class MovieService {
    private final MovieRepository movieRepository;
    @Autowired
    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    /*public Optional<Movie> getMovieById(Long id){
        return movieRepository.findById(id);
    }*/
    public Movie getMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
    }
    public Movie getMovieByName(String movieName) {
        return movieRepository.findByMovieNameIgnoreCase(movieName)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
    }

    // Tìm kiếm gần đúng
    public List<Movie> searchMoviesByKeyword(String keyword) {
        return movieRepository.findByMovieNameContainingIgnoreCase(keyword);
    }

    // Thêm movie mới
    public Movie saveMovie(Movie movie) {
        if(movieRepository.existsByMovieNameIgnoreCase(movie.getMovieName())){
            throw new AppException(ErrorCode.MOVIE_EXISTED);
        }
        return movieRepository.save(movie);
    }

    // Cập nhật movie
    public Movie updateMovie(Long id, Movie movieDetails) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

        movie.setMovieName(movieDetails.getMovieName());
        movie.setGenre(movieDetails.getGenre());
        movie.setDuration(movieDetails.getDuration());
        movie.setAge(movieDetails.getAge());
        movie.setDirector(movieDetails.getDirector());
        movie.setActress(movieDetails.getActress());
        movie.setReleaseDate(movieDetails.getReleaseDate());
        movie.setLanguage(movieDetails.getLanguage());
        movie.setDescription(movieDetails.getDescription());

        return movieRepository.save(movie);
    }

    // Xóa movie
    public void deleteMovie(Long id){
        if (!movieRepository.existsById(id)) {
            throw new AppException(ErrorCode.MOVIE_NOT_FOUND);
        }
        movieRepository.deleteById(id);
    }
}
