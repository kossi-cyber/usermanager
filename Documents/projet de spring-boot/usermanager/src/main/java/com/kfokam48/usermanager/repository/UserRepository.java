package com.kfokam48.usermanager.repository;

import com.kfokam48.usermanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Cette méthode va nous servir plus tard pour vérifier l'email lors de la connexion
    Optional<User> findByEmail(String email);
}