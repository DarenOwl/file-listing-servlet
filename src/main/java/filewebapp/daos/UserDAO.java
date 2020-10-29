package filewebapp.daos;

import filewebapp.entities.User;
import filewebapp.utils.HashUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;

public class UserDAO {

    private final Session session;
    private HashUtil hashUtil;

    public UserDAO(Session session) {
        this.session = session;
        hashUtil = new HashUtil();
    }

    public User get(String login) throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root = criteria.from(User.class);
        criteria.select(root).where(builder.equal(root.get("login"),login));

        Query<User> query = session.createQuery(criteria);
        query.setMaxResults(1);

        return query.uniqueResult();
    }

    public User get(String login, String password) throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root = criteria.from(User.class);
        criteria.select(root)
                .where(builder.and(
                        builder.equal(root.get("login"),login),
                        builder.equal(root.get("passwordHash"),Arrays.toString(hashUtil.generateHash(password)))));

        Query<User> query = session.createQuery(criteria);
        query.setMaxResults(1);

        return query.uniqueResult();
    }

    public void add(String login, String email, String password) throws HibernateException {
        session.save(new User(login, email, Arrays.toString(hashUtil.generateHash(password))));
    }
}
