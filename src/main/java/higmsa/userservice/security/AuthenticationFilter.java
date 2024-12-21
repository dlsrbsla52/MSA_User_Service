package higmsa.userservice.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import higmsa.userservice.dto.UserDto;
import higmsa.userservice.service.UserService;
import higmsa.userservice.vo.RequestLogin;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserService userService;
    private final Environment environment;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                   UserService userService, Environment environment) {
        super(authenticationManager);
        this.userService = userService;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try{
            RequestLogin requestLogin = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(requestLogin.getEmail(), requestLogin.getPassword(), new ArrayList<>()));
        }catch (IOException e){
            throw new RuntimeException(e);
        }
//        try{
//            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
//
//            return getAuthenticationManager().authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            creds.getEmail(),
//                            creds.getPassword(),
//                            new ArrayList<>()
//                    )
//            );
//        } catch (IOException e){
//            log.info(e.getMessage());
//            throw new RuntimeException(e);
//        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        try{
            log.debug(((User)authResult.getPrincipal()).getUsername());

            String userName = ((User)authResult.getPrincipal()).getUsername();
            UserDto userDetails = userService.getUserDetailsByEmail(userName);

            byte[] secretKeyBytes = Base64.getEncoder().encode(environment.getProperty("token.secret").getBytes());

            SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

            Instant now = Instant.now();

            String token = Jwts.builder()
                    .setSubject(userDetails.getUserId())
                    .setExpiration(Date.from(now.plusMillis(Long.parseLong(environment.getProperty("token.expiration_time")))))
                    .setIssuedAt(Date.from(now))
                    .signWith(secretKey)
                    .compact();

            response.addHeader("token", token);
            response.addHeader("userId", userDetails.getUserId());

//            super.successfulAuthentication(request, response, chain, authResult);
        }catch (Exception e){
            log.debug(e.getMessage());
        }
    }
}
