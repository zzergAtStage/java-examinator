package org.zergatstage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zergatstage.model.User;

/**
 * @author father
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

