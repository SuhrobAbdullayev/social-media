package com.example.demo.jwt_utils;

import com.example.demo.domain.dto.response_dto.LoginResponseDTO;
import com.example.demo.domain.dto.response_dto.ResponseDTO;
import com.example.demo.domain.entity.User;
import com.example.demo.exceptions.UserException;
import com.example.demo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    @Value("${jwt.token.secret}")
    private String secret;
    @Value("${jwt.token.validity}")
    private long validityMilliSecond;

    public JwtTokenProvider(UserDetailsService userDetailsService, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public ResponseDTO<LoginResponseDTO> createToken(String username, Boolean rememberMe) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserException("Bunday foydalanuvch mavjud emas");
        }
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", user.getRoles());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityMilliSecond);
        ResponseDTO<LoginResponseDTO> responseDto = new ResponseDTO<>();
        responseDto.setSuccess(true);
        responseDto.setMessage("Token yaratildi");
        responseDto.setRecordsTotal(1L);
        LoginResponseDTO loginResponseDto = new LoginResponseDTO();
        loginResponseDto.setId(user.getId());
        loginResponseDto.setFirstName(user.getFirstName());
        loginResponseDto.setLastName(user.getLastName());
        loginResponseDto.setUsername(user.getUsername());
        loginResponseDto.setRoles(user.getRoles());
        loginResponseDto.setToken(Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret).compact());
        responseDto.setData(loginResponseDto);
        if (rememberMe) {
            user.setRefreshToken(Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(validity)
                    .signWith(SignatureAlgorithm.HS384, secret).compact());
            loginResponseDto.setRefreshToken(user.getRefreshToken());
        } else {
            user.setRefreshToken(null);
        }
        userRepository.save(user);
        return responseDto;
    }

    public ResponseDTO<LoginResponseDTO> refreshToken(String refreshToken) {
        User user = userRepository.findByToken(refreshToken);
        if (user == null) {
            throw new UserException("Ushbu foydalanuvchi eslab qolish funksiyasidan foydalanmagan");
        }
        return createToken(user.getUsername(), true);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUser(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUser(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }


}