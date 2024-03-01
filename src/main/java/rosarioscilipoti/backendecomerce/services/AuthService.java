package rosarioscilipoti.backendecomerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rosarioscilipoti.backendecomerce.DTO.NewUserDTO;
import rosarioscilipoti.backendecomerce.DTO.UserLoginDTO;
import rosarioscilipoti.backendecomerce.Exception.BadRequestException;
import rosarioscilipoti.backendecomerce.Exception.UnauthorizedException;
import rosarioscilipoti.backendecomerce.entites.User;
import rosarioscilipoti.backendecomerce.repositories.UserRepository;
import rosarioscilipoti.backendecomerce.security.JWTTools;


@Service
public class AuthService {
    @Autowired
    private UserService usersService;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTTools jwtTools;

    public String GenerateToken(UserLoginDTO payload) {
        User user = UserService.findByEmail(payload.email());
        if (bcrypt.matches(payload.password(), user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Credenziali sbagliate!");
        }
    }

    public User registerUser(NewUserDTO payload) {
        userRepository.findByEmail(payload.email()).ifPresent(user -> {
            throw new BadRequestException("L'email " + user.getEmail() + " è già in uso!");
        });
        User newUser = new User(payload.username(), payload.email(), bcrypt.encode(payload.password()), payload.name(), payload.surname(), payload.avatar());

        return userRepository.save(newUser);
    }



}