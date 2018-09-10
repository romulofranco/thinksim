package br.unicamp.fe.thinksim.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.unicamp.fe.thinksim.entity.User;

public class GenericDAO extends DAOImpl<User, Integer> implements Serializable {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(GenericDAO.class);

	private static GenericDAO INSTANCE;

	private GenericDAO() {

	}

	public synchronized static GenericDAO getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GenericDAO();
		}
		return INSTANCE;
	}

	public User isUsuarioReadyToLogin(String username, String password) {
		try {

			EntityManager em = this.getEntityManager();
			TypedQuery<User> query = em.createQuery(
					"SELECT u FROM User u WHERE u.username = :name and u.password = :pwd and u.active = '1'",
					User.class);
			query.setParameter("name", username);
			query.setParameter("pwd", password);
			if (query.getResultList() != null) {
				// user = query.getResultList().get(0);
				if (query.getResultList().size() > 0) {
					logger.debug("User " + query.getSingleResult());
					return query.getSingleResult();
				} else {
					return null;
				}
			} else {
				return null;
			}

		} catch (NoResultException e) {
			return null;
		}
	}

	public boolean updateUser(User user) {

		try {
			EntityManager em = this.getEntityManager();

			Query q = em.createQuery("UPDATE User u SET u.dateTime = :dateTime,"
					+ "u.active = :active, u.password = :pwd, u.email = :email, "
					+ "u.theme = :theme  where u.username = :username");

			q.setParameter("email", user.getEmail());
			q.setParameter("active", user.getActive());
			q.setParameter("dateTime", user.getDateTime());
			q.setParameter("theme", user.getTheme());
			q.setParameter("username", user.getUsername());

			em.getTransaction().begin();
			q.executeUpdate();
			em.getTransaction().commit();

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

}
