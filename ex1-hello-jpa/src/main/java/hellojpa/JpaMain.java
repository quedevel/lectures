package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            Movie movie = new Movie();
            movie.setActor("actorA");
            movie.setDirector("directorA");
            movie.setName("Hell World");
            movie.setPrice(10000);

            em.persist(movie);
            em.flush();
            em.clear();

            Movie findMovie = em.find(Movie.class, movie.getId());

            System.out.println("findMovie = " + findMovie);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}
