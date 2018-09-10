package br.unicamp.fe.thinksim.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.unicamp.fe.thinksim.entity.User;

public class UserDAO extends DAOImpl<User, String> implements Serializable {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(UserDAO.class);

	private static UserDAO INSTANCE;

	private UserDAO() {

	}

	public synchronized static UserDAO getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new UserDAO();
		}
		return INSTANCE;
	}

	public List<User> getAll() {
		List<User> users = this.getEntityManager().createQuery("select p from User p", User.class).getResultList();
		return users;
	}

	public User getUserByEmail(String email) {
		TypedQuery<User> query = this.getEntityManager().createQuery("select u from User u where u.email = :email",
				User.class);
		query.setParameter("email", email);

		List<User> result = query.getResultList();
		User user = null;

		if (!result.isEmpty()) {
			if (result.get(0) != null) {
				user = result.get(0);
			}
		}

		return user;

	}

}
