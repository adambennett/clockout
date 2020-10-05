package com.limswizards.dev.clockout.repositories;

import com.limswizards.dev.clockout.models.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface Users extends JpaRepository<User, Long> {

    @Query
    Optional<User> findDistinctByUsername(String username);

    @Query(value = "SELECT pass FROM user WHERE username = :username", nativeQuery = true)
    List<String> getLoginInfo(String username);

}
