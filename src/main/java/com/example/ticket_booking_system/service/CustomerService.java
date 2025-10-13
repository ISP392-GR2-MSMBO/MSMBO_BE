//package com.example.ticket_booking_system.service;
//
//import com.example.ticket_booking_system.dto.reponse.movie.MovieResponse;
//import com.example.ticket_booking_system.entity.Customer;
//import com.example.ticket_booking_system.entity.Movie;
//import com.example.ticket_booking_system.mapper.; // nếu bạn có mapper riêng, dùng mapper của bạn
//import com.example.ticket_booking_system.repository.CustomerRepository;
//import com.example.ticket_booking_system.repository.MovieRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class CustomerService {
//
//    private final CustomerRepository customerRepository;
//    private final MovieRepository movieRepository;
//
//    // Search movies by keyword (name)
//    public List<MovieResponse> searchMovies(String keyword) {
//        List<Movie> movies = movieRepository.findByMovieNameContainingIgnoreCase(keyword == null ? "" : keyword);
//        return movies.stream()
//                .map(m -> {
//                    // nếu bạn có MovieMapper, dùng nó; nếu không, map thủ công
//                    return MovieMapper.toResponse(m);
//                })
//                .collect(Collectors.toList());
//    }
//
//    // view customer profile by userId
//    public Customer getCustomerProfile(Long userId) {
//        return customerRepository.findByUser_UserID(userId);
//    }
//
//    // update customer profile
//    public Customer updateProfile(Long userId, Customer updated) {
//        Customer existing = customerRepository.findByUser_UserID(userId);
//        if (existing == null) throw new RuntimeException("Customer not found");
//        existing.setGender(updated.getGender());
//        existing.setAddress(updated.getAddress());
//        existing.setDateOfBirth(updated.getDateOfBirth());
//        return customerRepository.save(existing);
//    }
//}
