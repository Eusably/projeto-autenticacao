package services;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.PapelSistema;
import repository.PapelSistemaRepository;

@Path("/papel-sistema")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PapelSistemaService {

	@Inject
	private PapelSistemaRepository papelSistemaRepository;

	@GET
	public Response listar() {
		return Response.ok().entity(this.papelSistemaRepository.pesquisarTodos()).build();
	}

	@GET
	@Path("/consultar")
	public Response consultar(@QueryParam("codigo") String codigo) {
		return Response.ok().entity(this.papelSistemaRepository.consultar(codigo)).build();
	}

	@POST
	public Response cadastrar(PapelSistema papel) {
		try {
			this.papelSistemaRepository.cadastrar(papel);
			return Response.ok().build();
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

	@PUT
	public Response atualizar(PapelSistema papel) {
		try {
			this.papelSistemaRepository.editar(papel);
			return Response.ok().build();
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

}
