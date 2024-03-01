package rosarioscilipoti.backendecomerce.DTO;

public record NewUserDTO(
        String username,
        String email,
        String password,
        String name,
        String surname,
        String avatar

) {
}
