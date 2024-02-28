package com.project.user.service.controllers;

import com.project.user.service.entities.User;
import com.project.user.service.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
    int retryCount = 0;
    @GetMapping("/{id}")
    @RateLimiter(name = "UserRateLimitter", fallbackMethod = "ratingHotelFallback")
//    @Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallback")
//    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> findById(@PathVariable String id){
        LOGGER.info("Retry count: {} ", retryCount);
        retryCount++;
        User user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public  ResponseEntity<User> ratingHotelFallback(String id, Exception exception){
        LOGGER.info("Fallback is executed because service is down: ", exception.getMessage());

        User user = User.builder()
                .email("dummy@gmail.com")
                .about("This user is created dummy, because some service is down")
                .userId("123")
                .build();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }
}
