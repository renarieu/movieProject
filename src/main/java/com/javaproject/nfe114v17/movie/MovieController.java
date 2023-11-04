package com.javaproject.nfe114v17.movie;

import com.javaproject.nfe114v17.tmdbApi.NotFoundException;
import com.javaproject.nfe114v17.tmdbApi.TmdbApiClient;
import com.javaproject.nfe114v17.user.User;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class MovieController {

    private final MovieService movieService;
    private final TmdbApiClient tmdbApiClient;

    public MovieController(MovieService movieService, TmdbApiClient tmdbApiClient) {
        this.movieService = movieService;
        this.tmdbApiClient = tmdbApiClient;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchMovie(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        model.addAttribute("currentUser", currentUser);


        return "searchform";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String searchMovie(HttpServletRequest request, Model model) throws IOException, InterruptedException, NotFoundException, JSONException {
        String query = request.getParameter("query");
        model.addAttribute("research", tmdbApiClient.searchMovie(query));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        model.addAttribute("currentUser", currentUser);


        return "searchform";
    }

    @RequestMapping(value = "movie/{movieId}", method = RequestMethod.GET)
    public String getMovieById(@PathVariable String movieId, Model model) throws IOException, InterruptedException, NotFoundException {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUser = authentication.getName();
            model.addAttribute("currentUser", currentUser);
            int parsedMovieId = Integer.parseInt(movieId);
            Movie movieById = tmdbApiClient.getMovieById(parsedMovieId);
            movieService.addNewMovie(movieById);
            model.addAttribute("movie", movieById);
        } catch (NotFoundException e) {
            throw e;
        }
        return "movie";
    }

    @RequestMapping("/")
    public String home(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        model.addAttribute("currentUser", currentUser);

        return "index";
    }

}
