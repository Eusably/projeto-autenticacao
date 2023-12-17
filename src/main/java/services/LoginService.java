package services;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jwt.JwtRepository;
import jwt.OAuth2Token;
import model.UsuarioSistema;
import repository.UsuarioSistemaRepository;

@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginService {

	@Inject
	private UsuarioSistemaRepository usuarioSistemaRepository;
	
	@Inject
    private JwtRepository jwtRepository;	

	@GET
	public Response login(@QueryParam("email") String email, @QueryParam("senha") String senha)
			throws Exception {
		UsuarioSistema usuario = this.usuarioSistemaRepository.consultarEmail(email);
		if (usuario == null) {
			return Response.serverError().entity("Usuário não encontrado com este e-mail").build();
		}
		if (!usuario.getSenha().equals(senha)) {
			return Response.serverError().entity("Senha incorreta").build();
		}
		
		OAuth2Token oauth = this.jwtRepository.generateJwt(usuario);
		
		return Response.ok().entity(oauth).build();
	}

}
