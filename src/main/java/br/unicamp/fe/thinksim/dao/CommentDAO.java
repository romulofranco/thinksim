package br.unicamp.fe.thinksim.dao;

import java.io.Serializable;

import javax.persistence.Query;

import org.apache.log4j.Logger;

import br.unicamp.fe.thinksim.entity.Comment;

public class CommentDAO extends DAOImpl<Comment, Integer> implements Serializable {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(CommentDAO.class);

	private static CommentDAO INSTANCE;

	private CommentDAO() {

	}

	public synchronized static CommentDAO getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CommentDAO();
		}
		return INSTANCE;
	}

	public void updateComment(Comment comment) {
		try {

			StringBuilder str = new StringBuilder();
			str.append("UPDATE Comment c SET c.text = :text");
			str.append(" where c.id = :id");

			Query q = this.getEntityManager().createQuery(str.toString());

			q.setParameter("text", comment.getText());

			this.getEntityManager().getTransaction().begin();
			q.executeUpdate();
			this.getEntityManager().getTransaction().commit();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
