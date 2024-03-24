package com.turtlechat.server.repositories;

import com.turtlechat.server.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.userEmail = :email")
    User findByEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.userName = :newUsername WHERE u.userEmail = :email")
    int updateUsernameByEmail(@Param("email") String email, @Param("newUsername") String newUsername);
}
