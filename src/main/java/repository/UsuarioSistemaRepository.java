package repository;

import java.util.List;

import javax.ejb.Stateless;

import model.UsuarioSistema;
import repository.base.AbstractCrudRepository;

@Stateless
public class UsuarioSistemaRepository extends AbstractCrudRepository<UsuarioSistema>{

	@SuppressWarnings("unchecked")
	public UsuarioSistema consultarEmail(String email) {
		List<UsuarioSistema> lista = super.em.createQuery("from UsuarioSistema where email = :email").setParameter("email", email).getResultList();
		return lista != null && !lista.isEmpty() ? lista.get(0) : null;
	}
	
	
}
