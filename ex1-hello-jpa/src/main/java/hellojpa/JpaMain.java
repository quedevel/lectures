package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
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

            Address address = new Address("city", "street", "1000");

            Member member = new Member();
            member.setUsername("test");
            member.setHomeAddress(address);
            em.persist(member);

            Member member1 = new Member();
            member1.setUsername("test2");
            member1.setHomeAddress(address);
            em.persist(member1);

            // address를 공유하므로 둘다 변경이 되버림... 따라서 setter 제거하여 불변 객체로 만들어주자.
            // 변경은 복제하여 해당 address만 변경되도록 만들어주자
            member.getHomeAddress().setCity("newCity");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}
