package repository.base;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

public abstract class AbstractCrudRepository<T> {

	protected Class<T> persistentClass;

	@PersistenceContext(unitName = "primary")
	protected EntityManager em;

	@PostConstruct
	@SuppressWarnings("unchecked")
	public void init() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	public T consultar(Integer id) {
		return this.em.find(persistentClass, id);
	}

	public List<T> pesquisarTodos() {
		CriteriaBuilder builder = this.em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(this.persistentClass);
		Root<T> root = criteria.from(this.persistentClass);
		criteria.select(root);
		return em.createQuery(criteria).getResultList();
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {

		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	@Transactional
	public void inserir(T entity) {
		this.em.persist(entity);
	}

	@Transactional
	public void atualizar(T entity) {
		this.em.merge(entity);
	}

	@Transactional
	public void remover(T entity) {
		this.em.remove(entity);
	}

	@Transactional
	public void remover(Integer entityId) throws Exception {
		T entity = this.consultar(entityId);
		this.remover(entity);
	}

}
