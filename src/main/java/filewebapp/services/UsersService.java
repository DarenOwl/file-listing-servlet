package filewebapp.services;

import filewebapp.daos.UserDAO;
import filewebapp.entities.User;
import filewebapp.utils.HashUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class UsersService {
    private static final String url = "jdbc:mysql://localhost:3306/filelisting_db?user=filewebapp&password=files";

    private final SessionFactory sessionFactory;

    public UsersService() {
        Configuration configuration = getMySqlConfiguration();
        sessionFactory = createSessionFactory(configuration);
    }

    public User get(String login){
        Session session = sessionFactory.openSession();
        User dataSet = new UserDAO(session).get(login);
        session.close();
        return dataSet;
    }

    public User get(String login, String password){
        Session session = sessionFactory.openSession();
        User dataSet = new UserDAO(session).get(login, password);
        session.close();
        return dataSet;
    }

    public void add(String login, String email, String password) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserDAO userDAO = new UserDAO(session);
        userDAO.add(login, email, password);
        transaction.commit();
        session.close();
    }

    private Configuration getMySqlConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/filelisting_db");
        configuration.setProperty("hibernate.connection.username", "filewebapp");
        configuration.setProperty("hibernate.connection.password", "files");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        return configuration;
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}