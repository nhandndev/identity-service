package com.nhan.identity_service.config;

import com.nhan.identity_service.exception.AppException;
import com.nhan.identity_service.service.AuthService;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {

  private final AuthService authService;

  @Value("${jwt.signerKey}")
  private String SIGN_KEY;

  private NimbusJwtDecoder nimbusJwtDecoder;

  @Override
  public Jwt decode(String token) throws JwtException {
    try {
      authService.verifyToken(token);
    } catch (JOSEException | ParseException | AppException e) {
      throw new JwtException("Token invalid");
    }

    if (Objects.isNull(nimbusJwtDecoder)) {
      SecretKeySpec secretKeySpec = new SecretKeySpec(SIGN_KEY.getBytes(), "HmacSHA256");

      nimbusJwtDecoder =
          NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS256).build();
    }

    return nimbusJwtDecoder.decode(token);
  }
}
