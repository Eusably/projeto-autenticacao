package services;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.UsuarioSistema;
import repository.UsuarioSistemaRepository;

@Path("/usuario-sistema")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioSistemaService {

	@Inject
	private UsuarioSistemaRepository usuarioSistemaRepository;

	@GET
	public Response listar() {
		return Response.ok().entity(this.usuarioSistemaRepository.pesquisarTodos()).build();
	}

	@GET
	@Path("/{id}")
	public Response consultar(@PathParam("id") Integer id) {
		return Response.ok().entity(this.usuarioSistemaRepository.consultar(id)).build();
	}

	@POST
	public Response cadastrar(UsuarioSistema usuario) {
		try {
			this.usuarioSistemaRepository.inserir(usuario);
			return Response.ok().build();
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

	@PUT
	public Response atualizar(UsuarioSistema usuario) {
		try {
			this.usuarioSistemaRepository.atualizar(usuario);
			return Response.ok().build();
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

}
