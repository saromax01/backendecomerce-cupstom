package rosarioscilipoti.backendecomerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rosarioscilipoti.backendecomerce.DTO.LoginResponseDTO;
import rosarioscilipoti.backendecomerce.DTO.NewUserDTO;
import rosarioscilipoti.backendecomerce.DTO.UserLoginDTO;
import rosarioscilipoti.backendecomerce.entites.User;
import rosarioscilipoti.backendecomerce.services.AuthService;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody UserLoginDTO payload) {
        return new LoginResponseDTO(authService.GenerateToken(payload));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User saveUser(@RequestBody NewUserDTO newUser) {

        return this.authService.registerUser(newUser);
    }
}
