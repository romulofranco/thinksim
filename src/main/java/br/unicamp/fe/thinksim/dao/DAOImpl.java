package br.unicamp.fe.thinksim.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import br.unicamp.fe.thinksim.entity.Post;

public abstract class DAOImpl<T, I extends Serializable> implements DAO<T, I> {

	private final static Logger logger = Logger.getLogger(DAOImpl.class);

	private DAOConexao conexao;

	@Override
	public T save(T entity) {
		try {
			T saved = null;

			this.getEntityManager().getTransaction().begin();
			saved = this.getEntityManager().merge(entity);
			this.getEntityManager().flush();
			this.getEntityManager().getTransaction().commit();

			return saved;
		} catch (Exception e) {
			e.printStackTrace();
			this.conexao.getEntityManager().getTransaction().rollback();
			return null;
		}
	}

	public void insert(T entity) {
		try {
			this.getEntityManager().getTransaction().begin();
			this.getEntityManager().persist(entity);
			this.getEntityManager().flush();
			this.getEntityManager().getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			this.conexao.getEntityManager().getTransaction().rollback();
		}
	}

	public void makeFreeTransaction() {

		try {
			Post p = this.conexao.getEntityManager().find(Post.class, new Long(1));

		} catch (Exception sqle) {
			logger.debug("VAI DAR ROLLBACK" + sqle.getMessage());

			try {
				this.conexao.getEntityManager().getTransaction().rollback();
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}
		}
	}

	public void delete(Class<T> classe, Long id) {
		try {
			EntityManager em = this.getEntityManager();
			em.getTransaction().begin();
			Query query = em.createQuery("delete from " + classe.getSimpleName() + " o where o.id = :id");
			query.setParameter("id", id);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			this.conexao.getEntityManager().getTransaction().rollback();
		}
	}

	@Override
	public void remove(T entity) {
		this.getEntityManager().getTransaction().begin();
		this.getEntityManager().remove(entity);
		this.getEntityManager().getTransaction().commit();

	}

	@Override
	public T getById(Class<T> classe, I pk) {

		try {
			return this.getEntityManager().find(classe, pk);
		} catch (NoResultException e) {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAll(Class<T> classe) {
		return this.getEntityManager().createQuery("select o from " + classe.getSimpleName() + " o").getResultList();
	}

	@Override
	public synchronized EntityManager getEntityManager() {

		if (this.conexao == null) {
			this.conexao = new DAOConexao();
		}

		this.makeFreeTransaction();

		return this.conexao.getEntityManager();
	}

}