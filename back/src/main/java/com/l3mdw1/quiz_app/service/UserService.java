package com.l3mdw1.quiz_app.service;

import com.l3mdw1.quiz_app.model.User;
import com.l3mdw1.quiz_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public List<User> getAllUsers() {
        List<User> users = userRepo.findAll();
        if (users.isEmpty()) {
            throw new RuntimeException("Aucun utilisateur trouvé");
        }
        return users;
    }

    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id: " + id));
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Aucun utilisateur trouvé avec l'email: " + email));
    }

    public User createUser(User user) {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new RuntimeException("L'email est requis");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Le nom d'utilisateur est requis");
        }
        return userRepo.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);

        if (updatedUser.getUsername() != null && !updatedUser.getUsername().trim().isEmpty()) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().trim().isEmpty()) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().trim().isEmpty()) {
            existingUser.setPassword(updatedUser.getPassword());
        }

        existingUser.setAdmin(updatedUser.isAdmin());

        return userRepo.save(existingUser);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepo.delete(user);
    }
}
