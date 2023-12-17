package repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

import model.PapelSistema;
import repository.base.AbstractCrudRepository;

@Stateless
public class PapelSistemaRepository extends AbstractCrudRepository<PapelSistema>{
	
	@Transactional
	public void cadastrar(PapelSistema papel) throws Exception {
		if(this.consultar(papel.getCodigo()) == null) {
			super.inserir(papel);
		} else {
			throw new Exception("Papel já cadastrado com este código");
		}
	}	
	
	@Transactional
	public void editar(PapelSistema papel) throws Exception {
		if(this.consultar(papel.getCodigo()) != null) {
			super.atualizar(papel);
		} else {
			throw new Exception("Papel não encontrado com este código");
		}
	}
	
	@SuppressWarnings("unchecked")
	public PapelSistema consultar(String codigo) {
		List<PapelSistema> lista = super.em.createQuery("from PapelSistema where codigo = :codigo").setParameter("codigo", codigo).getResultList();
		return lista != null && !lista.isEmpty() ? lista.get(0) : null;
	}
	
}
