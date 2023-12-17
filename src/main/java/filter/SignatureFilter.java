package filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jwt.JwtRepository;

@WebFilter("/*")
public class SignatureFilter implements Filter {

	@Inject
	private JwtRepository jwtRepository;

	private List<String> excludeUrls = Arrays.asList(new String[] { "/projeto-autenticacao/api/login" });

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String url = req.getRequestURI();

		if (!this.excludeUrls.contains(url)) {
			String authorization = req.getHeader("Authorization");

			if (authorization == null) {
				resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token de autenticação inválido");
			} else {
				try {
					boolean tokenValido = this.jwtRepository.validateToken(authorization);
					if (!tokenValido) {
						resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token de autenticação inválido");
					}
				} catch (Exception e) {
					e.printStackTrace();
					resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token de autenticação inválido");
				}
			}
		}

		chain.doFilter(request, response);
	}

}
