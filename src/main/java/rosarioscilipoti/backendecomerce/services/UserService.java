package rosarioscilipoti.backendecomerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import rosarioscilipoti.backendecomerce.Exception.NotFoundException;
import rosarioscilipoti.backendecomerce.entites.User;
import rosarioscilipoti.backendecomerce.repositories.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

  @Autowired
  private  UserRepository userRepository;

  public Page<User> getUsers(int pageNumber, int size, String orderBy) {
    if (size > 100) size = 100;
    Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(orderBy));
    return userRepository.findAll(pageable);
  }

  public  User findByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email " + email + " non trovata"));
  }

  public User findById(UUID userId) {
    return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
  }

  public User findByUsername(String username) {
    Optional<User> userOptional = userRepository.findByUsername(username);
    return userOptional.orElse(null);
  }
  public User findByIdAndUpdate(UUID userId, User updateUser) {
    User found = this.findById(userId);
    found.setUsername(updateUser.getUsername());
    found.setEmail(updateUser.getEmail());
    found.setPassword(updateUser.getPassword());
    found.setName(updateUser.getName());
    found.setSurname(updateUser.getSurname());
    found.setAvatar(updateUser.getAvatar());
    return userRepository.save(found);
  }
  public void findByIdAndDelete(UUID userId) {
    User found = this.findById(userId);
    userRepository.delete(found);
  }
}
