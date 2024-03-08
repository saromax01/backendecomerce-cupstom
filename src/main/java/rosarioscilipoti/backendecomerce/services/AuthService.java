package rosarioscilipoti.backendecomerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rosarioscilipoti.backendecomerce.DTO.LoginResponseDTO;
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
        User user = usersService.findByEmail(payload.email());
        if (bcrypt.matches(payload.password(), user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Credenziali sbagliate!");
        }
    }

    public User registerUser(NewUserDTO payload) {
        // Check for existing user with the same email:
        boolean emailExists = userRepository.existsByEmail(payload.email());
        if (emailExists) {
            throw new BadRequestException("L'email " + payload.email() + " è già in uso!"); // Use payload.email() for error message
        }

        // Create a new user:
        User newUser = new User(payload.username(), payload.email(), bcrypt.encode(payload.password()), payload.name(), payload.surname(), payload.avatar());

        // Save the user:
        return userRepository.save(newUser);
    }






}
