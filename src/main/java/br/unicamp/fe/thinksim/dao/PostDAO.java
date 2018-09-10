package br.unicamp.fe.thinksim.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.unicamp.fe.thinksim.entity.Comment;
import br.unicamp.fe.thinksim.entity.Post;

public class PostDAO extends DAOImpl<Post, Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(PostDAO.class);

	private static PostDAO INSTANCE;

	private PostDAO() {

	}

	public synchronized static PostDAO getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PostDAO();
		}
		return INSTANCE;
	}

	public List<Comment> getComments(Long idPost) {
		TypedQuery<Comment> query = this.getEntityManager().createQuery("select c from Comment c ", Comment.class);
		// query.setParameter("id", idPost);
		return query.getResultList();
	}

	public List<Post> getAllByCategoria(String category) {
		TypedQuery<Post> query = this.getEntityManager()
				.createQuery("select p from Post p where p.category like :category", Post.class);
		query.setParameter("category", category + "%");
		List<Post> posts = query.getResultList();
		for (int i = 0; i > posts.size(); i++) {
			Post post = posts.get(i);
			post.setComments(this.getComments(post.getId()));
			posts.set(i, post);
		}

		return posts;

	}

	public List<Post> getAll() {
		List<Post> posts = this.getEntityManager().createQuery("select p from Post p", Post.class).getResultList();
		for (int i = 0; i > posts.size(); i++) {
			Post post = posts.get(i);
			post.setComments(this.getComments(post.getId()));
			posts.set(i, post);
		}

		return posts;

	}

	public List<String> getCategory(String query) {
		TypedQuery<String> q = this.getEntityManager().createQuery(
				"select p.category from Post p where p.category like :query group by p.category", String.class);

		q.setParameter("query", query + "%");
		List<String> categories = q.getResultList();
		return categories;
	}

	public List<String> getCategory() {
		TypedQuery<String> q = this.getEntityManager().createQuery("select p.category from Post p group by p.category",
				String.class);

		List<String> categories = q.getResultList();
		return categories;
	}

}
