package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Team t = new Team();
            t.setName("testA");
            em.persist(t);

            Member m = new Member();
            m.setUsername("mA");
            m.setTeam(t);
            em.persist(m);

            em.flush();
            em.clear();
            
            Member findMember = em.find(Member.class, m.getId());
            List<Member> members = findMember.getTeam().getMembers();

            for (Member member : members) {
                System.out.println("member = " + member.getUsername());
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}
