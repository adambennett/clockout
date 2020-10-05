package com.limswizards.dev.clockout.controllers;

import com.limswizards.dev.clockout.models.*;
import com.limswizards.dev.clockout.services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class Endpoints {

    private static Middleware service;

    @Autowired
    public Endpoints(Middleware mid) { service = mid; }

    public static Middleware getMiddleware() { return service; }

    @GetMapping("/getUsers")
    @CrossOrigin(origins = {"https://nmi-clockout.herokuapp.com", "http://localhost:4200"})
    public ResponseEntity<?> getUsers() {
        return new ResponseEntity<>(service.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/getUser/{name}")
    @CrossOrigin(origins = {"https://nmi-clockout.herokuapp.com", "http://localhost:4200"})
    public ResponseEntity<?> getUser(@PathVariable String name) {
        Optional<User> user = service.getUser(name);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/newUser")
    @CrossOrigin(origins = {"https://nmi-clockout.herokuapp.com", "http://localhost:4200"})
    public ResponseEntity<?> saveUser(@RequestBody User requestUser) {
        String username = requestUser.getUsername();
        if (username != null && !username.equals("")) {
            Optional<User> user = service.getUser(username);
            if (user.isEmpty()) {
                User newUser = new User();
                VacationTime time = new VacationTime();
                time.setUser(newUser);
                newUser.setUsername(username);
                newUser.setPass(requestUser.getPass());
                newUser.setSalt(requestUser.getSalt());
                newUser.setTime(time);
                newUser = service.createUser(newUser);
                return new ResponseEntity<>(newUser, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Username already taken!", HttpStatus.FOUND);
    }

}
