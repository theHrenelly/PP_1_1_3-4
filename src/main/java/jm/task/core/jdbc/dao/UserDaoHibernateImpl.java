package jm.task.core.jdbc.dao;


import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = Util.getSessionFactory();
        } catch (ExceptionInInitializerError e) {
            System.err.println(e.getMessage());
        }
    }


    public UserDaoHibernateImpl() {
    }


    @Override
    public void createUsersTable() {
        Session session = sessionFactory.getCurrentSession();
        try {
            String query = new String(Files.readAllBytes(Paths.get("src/main/resources/UsersTable.sql")));
            session.beginTransaction();
            session.createNativeQuery(query, User.class).executeUpdate();
            session.getTransaction().commit();
            System.out.println("Created table of users");
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            try {
                session.getTransaction().rollback();
            } catch (HibernateException ex) {
                System.err.println(ex.getMessage());
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            session.createNativeQuery("drop table if exists users", User.class).executeUpdate();
            session.getTransaction().commit();
            System.out.println("Dropped the table of users");
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            try {
                session.getTransaction().rollback();
            } catch (HibernateException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            User user = new User(name, lastName, age);
            session.persist(user);
            session.getTransaction().commit();
            System.out.printf("User %s added to the table\n", name);
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            try {
                session.getTransaction().rollback();
            } catch (HibernateException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            session.remove(session.get(User.class, id));
            session.getTransaction().commit();
            System.out.printf("User with id %d deleted from the table\n", id);
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            try {
                session.getTransaction().rollback();
            } catch (HibernateException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.getCurrentSession();
        List<User> userList = new ArrayList<>();
        try {
            session.beginTransaction();
            userList = session.createQuery("select i from User i", User.class)
                    .getResultList();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            try {
                session.getTransaction().rollback();
            } catch (HibernateException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            session.createNativeQuery("delete from users", User.class)
                    .executeUpdate();
            session.getTransaction().commit();
            System.out.println("The table of users is cleared");
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            try {
                session.getTransaction().rollback();
            } catch (HibernateException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
