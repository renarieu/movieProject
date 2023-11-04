package com.javaproject.nfe114v17.user;

import com.javaproject.nfe114v17.movie.Movie;
import com.javaproject.nfe114v17.tmdbApi.NotFoundException;
import com.javaproject.nfe114v17.tmdbApi.TmdbApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TmdbApiClient tmdbApiClient;

    @Autowired
    public UserService(UserRepository userRepository, TmdbApiClient tmdbApiClient) {
        this.userRepository = userRepository;
        this.tmdbApiClient = tmdbApiClient;
    }

    public User getUserById(int userId) {
        Optional<User> optUser = userRepository.findByUserId(userId);
        boolean exists = userRepository.existsById(userId);
        if (!exists) {
            throw new IllegalStateException("User with id +" + userId + " does not exists");
        }
        return optUser.get();
    }

    public User getUserByUserName(String userName) {
        Optional<User> optUser = userRepository.findByUserName(userName);
        boolean exists = optUser.isPresent();
        if (!exists) {
            throw new IllegalStateException("User with id +" + userName + " does not exists");
        }
        return optUser.get();
    }


    public void addNewUser(User user) {
        Optional<User> optUser = userRepository.findByUserName(user.getUserName());
        if (optUser.isPresent()) {
            throw new IllegalStateException("User with login +" + user.getUserName() + " Already exists");
        }
        userRepository.save(user);
    }

    public void addSeenMovie(String userName, int movieId) throws NotFoundException, IOException, InterruptedException {
        User user = userRepository.findByUserName(userName).get();
        Movie movie = tmdbApiClient.getMovieById(movieId);
        user.addSeenMovie(movie);
        userRepository.save(user);
    }

    public int getTimeSpentWatching(User user) {
        int timeSpentWatching = 0;

        for (Movie movie : user.getSeenMovies()) {
            timeSpentWatching +=movie.getRuntime();
        }
        return timeSpentWatching/60;
    }

    public int getFavoriteRealeasedYear(User user) {
        ArrayList<Integer> releasedYear = new ArrayList();
        for (Movie movie : user.getSeenMovies()) {
            releasedYear.add(movie.getRelease_date().getYear());
        }
        return mostCommonElement(releasedYear);
    }

    private static int mostCommonElement(ArrayList<Integer> list) {
        Map<Integer, Integer> map = new HashMap<>();
        for (Integer t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }
        Map.Entry<Integer, Integer> max = null;
        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }
        return max.getKey();

    }

    public void processRegistration(User user) {
        User tempUser = new User(user.getUserName(), user.getPassword());
        userRepository.save(tempUser);
    }

    public int findNumberOfMovies(User user) {
        ArrayList<Movie> seenMovies = new ArrayList();
        for (Movie movie : user.getSeenMovies()) {
            seenMovies.add(movie);
        }
        return seenMovies.size();
    }
}
