package com.ecommerce.training.config;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.ecommerce.training.exception.TokenRefreshException;
import com.ecommerce.training.models.RefreshToken;
import com.ecommerce.training.repository.RefreshTokenRepository;
import com.ecommerce.training.repository.UserRepository;
import com.ecommerce.training.service.UserDetailsImpl;
import com.ecommerce.training.utility.AppConstant;

import io.jsonwebtoken.*;


@Component
public class JwtUtils {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value(AppConstant.jwtSecret)
	private String jwtSecret;

	@Value(AppConstant.jwtExpirationMs)
	private int jwtExpirationMs;

	@Value(AppConstant.jwtRefreshExpirationDateInMs)
	private Long refreshTokenDurationMs;
	 
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		return Jwts.builder()
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
	
	public RefreshToken createRefreshToken(Long userId) {
	    RefreshToken refreshToken = new RefreshToken();

	    refreshToken.setUser(userRepository.findById(userId).get());
	    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
	    refreshToken.setToken(UUID.randomUUID().toString());

	    refreshToken = refreshTokenRepository.save(refreshToken);
	    return refreshToken;
	  }
	public RefreshToken verifyExpiration(RefreshToken token) {
	    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
	      refreshTokenRepository.delete(token);
	      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
	    }

	    return token;
	  }
	public String generateTokenFromUsername(String username) {
	    return Jwts.builder().setSubject(username).setIssuedAt(new Date())
	        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
	        .compact();
	  }

}
