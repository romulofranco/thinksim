
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.Before;

import br.unicamp.fe.thinksim.entity.Post;
import br.unicamp.fe.thinksim.entity.User;

/**
 *
 */

/**
 * @author romulofranco
 *
 */
public class TestJPA {

	private static final String PERSISTENCE_UNIT_NAME = "THINKJPA";
	private EntityManagerFactory factory;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		this.factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = this.factory.createEntityManager();

		// Begin a new local transaction so that we can persist a new entity
		em.getTransaction().begin();
		TypedQuery<Post> query = em.createQuery("SELECT t from Post t", Post.class);
		boolean createNewEntries = (query.getResultList().size() == 0);

		if (createNewEntries) {
			org.junit.Assert.assertTrue(query.getResultList().size() == 0);
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
			user.setUsername("romulo");
			em.persist(user);
			em.flush();
			em.getTransaction().commit();

		}

	}

}
