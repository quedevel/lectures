package jpql;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            Team team = new Team();
            team.setName("팀A");
            em.persist(team);

            Team team2 = new Team();
            team2.setName("팀B");
            em.persist(team2);

            Member member = new Member();
            member.setUsername("회원1");
            member.setAge(10);
            member.changeTeam(team);

            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(10);
            member2.changeTeam(team);

            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(10);
            member3.changeTeam(team2);

            em.persist(member3);

            em.flush();
            em.clear();

            String query = "select m from Member m where m.username = :username";

//            List<Member> members = em.createQuery(query, Member.class)
//                    .setParameter("username", "회원1").getResultList();

            List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username","회원1").getResultList();

//            for (Member m : members) {
//                System.out.println("m = " + m.getUsername());
//            }

            for (Member m2 : resultList) {
                System.out.println("m2 = " + m2.getUsername());
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
