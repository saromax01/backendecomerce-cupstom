package rosarioscilipoti.backendecomerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rosarioscilipoti.backendecomerce.DTO.LoginResponseDTO;
import rosarioscilipoti.backendecomerce.DTO.NewUserDTO;
import rosarioscilipoti.backendecomerce.DTO.UserLoginDTO;
import rosarioscilipoti.backendecomerce.Exception.NotFoundException;
import rosarioscilipoti.backendecomerce.entites.User;
import rosarioscilipoti.backendecomerce.repositories.UserRepository;
import rosarioscilipoti.backendecomerce.security.JWTTools;
import rosarioscilipoti.backendecomerce.services.AuthService;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTTools jwtTools;


    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody UserLoginDTO payload) {
        User user = userRepository.findByEmail(payload.email())
                .orElseThrow(() -> new NotFoundException("Email " + payload.email() + " non trovata"));
        String token = authService.GenerateToken(user);
        String userId = jwtTools.extractIdFromToken(token);
        return new LoginResponseDTO(token, userId);
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User saveUser(@RequestBody NewUserDTO newUser) {

        return this.authService.registerUser(newUser);
    }
}
