package rosarioscilipoti.backendecomerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import rosarioscilipoti.backendecomerce.Exception.UnauthorizedException;
import rosarioscilipoti.backendecomerce.entites.User;
import rosarioscilipoti.backendecomerce.services.UserService;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTFilter extends OncePerRequestFilter {

	@Autowired
	private JWTTools jwtTools;

	@Autowired
	private UserService usersService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer "))
			throw new UnauthorizedException("Per favore metti il token nell'header");

		String accessToken = authHeader.substring(7);

		System.out.println("ACCESS TOKEN " + accessToken);

		jwtTools.verifyToken(accessToken);

		String id = jwtTools.extractIdFromToken(accessToken);
		User user = usersService.findById(UUID.fromString(id));

		Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return new AntPathMatcher().match("/auth/**", request.getServletPath());
	}
}
