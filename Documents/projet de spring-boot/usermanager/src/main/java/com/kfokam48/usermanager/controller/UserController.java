package com.kfokam48.usermanager.controller;

import com.kfokam48.usermanager.dto.AuthRequest;
import com.kfokam48.usermanager.dto.AuthResponse;
import com.kfokam48.usermanager.dto.RegisterRequest;
import com.kfokam48.usermanager.model.User;
import com.kfokam48.usermanager.repository.UserRepository;
import com.kfokam48.usermanager.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    // 1. INSCRIPTION (Public) : Créer un utilisateur avec mot de passe haché par BCrypt
    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Erreur : Cet email est déjà utilisé !");
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        // Hachage sécurisé du mot de passe exigé par le sujet
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);
        return ResponseEntity.ok("Utilisateur enregistré avec succès !");
    }

    // 2. CONNEXION (Public) : Vérification des identifiants et génération du Token JWT
    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authRequest.getEmail());
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            throw new UsernameNotFoundException("Requête d'authentification invalide !");
        }
    }

    // 3. CONSULTATION (Sécurisé) : Récupérer la liste complète des utilisateurs
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // 4. MODIFICATION (Sécurisé) : Mettre à jour un utilisateur existant
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody RegisterRequest userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(userDetails.getName());
                    user.setEmail(userDetails.getEmail());
                    if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                    }
                    userRepository.save(user);
                    return ResponseEntity.ok("Utilisateur mis à jour avec succès !");
                }).orElse(ResponseEntity.notFound().build());
    }

    // 5. SUPPRESSION (Sécurisé) : Supprimer un utilisateur de la base de données
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok("Utilisateur supprimé avec succès !");
                }).orElse(ResponseEntity.notFound().build());
    }
}