package jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import model.UsuarioSistema;

@ApplicationScoped
public class JwtRepository {

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	private static Map<String, Object> claims = new HashMap<>();
	private static final String JWT_SECRET = "DA+66U4Hmhfl55Y7kViL594MhHzeuafP6XxUQcUbaYRJ2ez3jXeCf6V9CuQpnBoqHb4/qSrm7lvTk0yUO0wqtKiYYuA0PGU196vdGJtzkpZl0NDImtd8Kw==";
	private static final String TOKEN_PREFIX = "Bearer ";
	private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
	private static final Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(JWT_SECRET),
			signatureAlgorithm.getJcaName());

	public OAuth2Token generateJwt(UsuarioSistema usuario) {
		List<String> papeis = usuario.getPapeis().stream().map(u -> u.getCodigo()).distinct()
				.collect(Collectors.toList());

		claims.put("usuario", usuario.getEmail());
		claims.put("id", usuario.getId());
		claims.put("nome", usuario.getNome());
		claims.put("roles", papeis);

		Date expira = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000);
		String accessToken = Jwts.builder().setClaims(claims).setSubject(usuario.getEmail())
				.setIssuedAt(new Date(System.currentTimeMillis())).setId(UUID.randomUUID().toString())
				.setExpiration(expira).signWith(hmacKey).compact();

		OAuth2Token result = new OAuth2Token();
		result.setAccessToken(accessToken);
		result.setTokenType("bearer");
		result.setExpiresIn(expira.getTime());
		result.setScope(String.join(" ", papeis));

		return result;
	}

	public boolean validateToken(String authToken) {
		String token = authToken.replace(TOKEN_PREFIX, "");
		Jws<Claims> jwt = Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(token);

		boolean valido = jwt != null && jwt.getBody() != null;

		if (valido) {
			// conferir expiração
			Claims claims = jwt.getBody();
			if (claims.containsKey("exp")) {
				Long valor = ((Integer) claims.get("exp")).longValue();
				Date dataExpiracao = new Date(TimeUnit.SECONDS.toMillis(valor));
				if (dataExpiracao.compareTo(new Date()) < 0) {
					valido = false;
				}
			}
		}

		return valido;
	}
	
	public Claims getClaims(String authToken) {
		String token = authToken.replace(TOKEN_PREFIX, "");
		Jws<Claims> jwt = Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(token);
		return jwt.getBody();
	}

}