package com.javaproject.nfe114v17.user;

import com.javaproject.nfe114v17.tmdbApi.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "user/{userName}", method = RequestMethod.GET)
    public String getUser(@PathVariable String userName, Model model) {
        User user = userService.getUserByUserName(userName);
        model.addAttribute(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           String currentUser = authentication.getName();
        model.addAttribute("currentUser", currentUser);
        return "user";
    }

    @RequestMapping(value = "user/{userName}/statistics", method = RequestMethod.GET)
    public String getStatistics(@PathVariable String userName, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        model.addAttribute("currentUser", currentUser);


        User user = userService.getUserByUserName(userName);
        int timeSpentWatching = userService.getTimeSpentWatching(user);
        int favoriteRealeasedYear = userService.getFavoriteRealeasedYear(user);
        int numberOfMovies = userService.findNumberOfMovies(user);
        model.addAttribute("timeSpentWatching", timeSpentWatching);
        model.addAttribute("favoriteRealeasedYear", favoriteRealeasedYear);
        model.addAttribute("numberOfMovies", numberOfMovies);
        return "statistics";
    }

    @PostMapping(path = "user/{userName}/movie/{movieId}")
    public String addSeenMovie(@PathVariable String userName, @PathVariable int movieId) throws NotFoundException, IOException, InterruptedException {
        userService.addSeenMovie(userName, movieId);

        return "redirect:/";
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        model.addAttribute("currentUser", currentUser);


        return "register";
    }

    @RequestMapping(path = "/registration_complete", method = RequestMethod.POST)
    public String processRegistration(User user){
        userService.processRegistration(user);
        return "redirect:/";
    }


}

