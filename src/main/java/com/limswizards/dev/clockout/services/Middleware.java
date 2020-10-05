package com.limswizards.dev.clockout.services;

import com.limswizards.dev.clockout.models.*;
import com.limswizards.dev.clockout.repositories.*;
import com.limswizards.dev.clockout.repositories.Time;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class Middleware {

    private final Users users;
    private final Time time;

    @Autowired
    public Middleware(Users users, Time time) { this.users = users; this.time = time; }

    public User createUser(User user) { return this.users.save(user); }

    public List<User> getUsers() { return this.users.findAll(); }

    public Optional<User> getUser(String username) { return this.users.findDistinctByUsername(username); }

}
