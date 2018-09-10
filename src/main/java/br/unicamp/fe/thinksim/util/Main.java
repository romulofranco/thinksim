package br.unicamp.fe.thinksim.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.unicamp.fe.thinksim.entity.Post;
import br.unicamp.fe.thinksim.entity.User;

public class Main {
	private static final String PERSISTENCE_UNIT_NAME = "THINKJPA";
	private static EntityManagerFactory factory;
	public final static Logger logger = Logger.getLogger(Main.class);

	public static void main(String[] args) {

		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();

		// Begin a new local transaction so that we can persist a new entity
		em.getTransaction().begin();
		TypedQuery<Post> query = em.createQuery("SELECT t from Post t", Post.class);
		boolean createNewEntries = (query.getResultList().size() == 0);

		Post at = new Post();
		at.setText("Teste");
		em.persist(at);

		at = new Post();
		at.setText("Teste1");
		em.persist(at);

		at = new Post();
		at.setText("Teste2");
		em.persist(at);

		User user = new User();
		// user.setName("Romulo Franco1");
		// user.setPassword("YWRtMTIz");
		user.setUsername("admin1");
		em.persist(user);
		em.flush();
		em.getTransaction().commit();

		query = em.createQuery("SELECT t from Post t", Post.class);
		List<Post> result = query.getResultList();
		for (Post post : result) {
			logger.debug("Veja: " + post.toString());
		}

	}
}
