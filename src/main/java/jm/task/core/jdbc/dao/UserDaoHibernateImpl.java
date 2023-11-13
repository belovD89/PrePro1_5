package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getConnection();

    public UserDaoHibernateImpl() {

    }

    // создание таблицы
    @Override
    public void createUsersTable() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.createNativeQuery("CREATE TABLE IF NOT EXISTS basekata.users" +
                    " (id mediumint not null auto_increment, name VARCHAR(50), " +
                    "lastname VARCHAR(50), " +
                    "age tinyint, " +
                    "PRIMARY KEY (id))").executeUpdate();
            transaction.commit();
            System.out.println("Создана Таблица");
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    //удаление таблицы
    @Override
    public void dropUsersTable() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.createNativeQuery("DROP TABLE IF EXISTS users").executeUpdate();
            transaction.commit();
            System.out.println("Таблица удалена");
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    //сохранение пользователя в БД
        @Override
        public void saveUser(String name, String lastName, byte age) {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            try {
                session.save(new User(name, lastName, age));
                transaction.commit();
                System.out.println("Пользователь" + name + " добавлен в базу данных");
            } catch (HibernateException e) {
                e.printStackTrace();
                session.getTransaction().rollback();
            } finally {
                session.close();
            }
        }

    //удаление пользователя
    @Override
    public void removeUserById(long id) {
//        Session session = sessionFactory.openSession();
//        Transaction transaction = session.beginTransaction();
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.delete(id);
            session.getTransaction().commit();
            System.out.println("Удален пользователь");
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        } finally {
            session.close();
        }
    }

    // извлечение всех пользователей из БД в виде List(а)
    @Override
    public List<User> getAllUsers() {
//        Session session = sessionFactory.openSession();
//        CriteriaQuery<User> criteriaQuery = session.getCriteriaBuilder().createQuery(User.class);
//        criteriaQuery.from(User.class);
//        Transaction transaction = session.beginTransaction();
//        List<User> userList = session.createQuery(criteriaQuery).getResultList();
        Session session = null;
        List<User> list = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            list = session.createQuery("from User", User.class).list();
            System.out.println("Пользователи извлечены из базы данных");
        } catch (HibernateException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return list;
    }

    // стирание содержимого в таблице
    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.createNativeQuery("TRUNCATE TABLE basekata.users;").executeUpdate();
            transaction.commit();
            System.out.println("Таблица теперь пустая");
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }
}
