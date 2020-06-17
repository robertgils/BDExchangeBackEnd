package backend.dao;

import backend.domain.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    public User get(int id) {
        return em.find(User.class, id);
    }

    public User insert(User newUser) {
        boolean checkEmail = isEmailUnique(newUser.getEmailaddress());
        System.out.println("Check email  =  " + checkEmail);
        if(checkEmail) {
            em.persist(newUser);
            return newUser;
        } else {
            return null;
        }
    }

    public User getUserByEmailAndPassword(String emailaddress, String password) {
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.emailaddress = :firstarg AND u.password = :secondarg", User.class
        );

        query.setParameter("firstarg", emailaddress);
        query.setParameter("secondarg", password);

        return query.getSingleResult();
    }

    public User getUserByEmail(String emailaddress) {
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.emailaddress = :firstarg", User.class
            );

            query.setParameter("firstarg", emailaddress);

            System.out.println("test");
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getAll() {
        TypedQuery<User> query = em.createQuery("select u from User u", User.class);
        return query.getResultList();
    }

    public boolean isEmailUnique(String emailaddress) {
        System.out.println("is null? - " + getUserByEmail(emailaddress));
        return getUserByEmail(emailaddress) == null;
    }

}
