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

    @PostMapping("/changePassword")
    @CrossOrigin(origins = {"https://nmi-clockout.herokuapp.com", "http://localhost:4200"})
    public ResponseEntity<?> updateUserPassword(@RequestBody LinkedHashMap<String, ?> request) {
        String username = null;
        String newPass = null;
        boolean setPassData = false;
        for (Map.Entry<String, ?> entry : request.entrySet()) {
            switch (entry.getKey()) {
                case "pass" -> {
                    newPass = entry.getValue().toString();
                    setPassData = true;
                }
                case "username" ->  username = entry.getValue().toString();
            }
        }
        if (username != null && !username.equals("") && setPassData) {
            Optional<User> user = service.getUser(username);
            if (user.isPresent()) {
                User dbUser = user.get();
                dbUser.setPass(newPass);
                User newUser = service.updateUser(dbUser);
                return new ResponseEntity<>(newUser, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Could not find username, or for some reason the new password sent was corrupted somehow.", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/changeDisplayName/{username}/{newName}")
    @CrossOrigin(origins = {"https://nmi-clockout.herokuapp.com", "http://localhost:4200"})
    public ResponseEntity<?> updateUserDisplayName(@PathVariable String username, @PathVariable String newName) {
        Optional<User> user = service.getUser(username);
        if (user.isPresent()) {
            User dbUser = user.get();
            dbUser.setEmployee(newName);
            service.createUser(dbUser);
            return new ResponseEntity<>(dbUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/updateUserTime")
    @CrossOrigin(origins = {"https://nmi-clockout.herokuapp.com", "http://localhost:4200"})
    public ResponseEntity<?> updateUserTime(@RequestBody LinkedHashMap<String, ?> request) {
        String username = null;
        VacationTime newTimeData = new VacationTime();
        boolean setTimeData = false;
        for (Map.Entry<String, ?> entry : request.entrySet()) {
            switch (entry.getKey()) {
                case "time" -> {
                    LinkedHashMap<String, ?> newTime = (LinkedHashMap<String, ?>) entry.getValue();
                    for (Map.Entry<String, ?> innerEntry : newTime.entrySet()) {
                        switch (innerEntry.getKey()) {
                            case "vacation" -> {
                                newTimeData.setVacation((Integer) innerEntry.getValue());
                                setTimeData = true;
                            }
                            case "personal" -> {
                                newTimeData.setPersonal((Integer) innerEntry.getValue());
                                setTimeData = true;
                            }
                            case "floating" -> {
                                newTimeData.setFloating((Integer) innerEntry.getValue());
                                setTimeData = true;
                            }
                        }
                    }
                }
                case "username" -> username = entry.getValue().toString();
            }
        }
        if (username != null && !username.equals("") && setTimeData) {
            Optional<User> user = service.getUser(username);
            if (user.isPresent()) {
                User dbUser = user.get();
                VacationTime oldTimeData = dbUser.getTime();
                oldTimeData.setPersonal(newTimeData.getPersonal());
                oldTimeData.setVacation(newTimeData.getVacation());
                oldTimeData.setFloating(newTimeData.getFloating());
                oldTimeData.setUsername(username);
                User newUser = service.updateUser(dbUser);
                return new ResponseEntity<>(newUser, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Could not find username, or for some reason the updated time data sent was corrupted somehow.", HttpStatus.NOT_FOUND);
    }
}
