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
            
            // fetch join 대상에는 별칭을 주지마!!
            // 조회 결과가 누락될 수 있다.
            // 패치 조인이 의도한 설계가 아니다
            // 패치 조인은 싹 다 가져오는게 뽀인트
            // 거르면서 조회하는게 좋을 수 있지만 패치 조인의 사상과 맞지 않는다.
            String query = "select t from Team t join fetch t.members";

            List<Team> resultList = em.createQuery(query, Team.class).getResultList();

            for (Team s : resultList) {
                System.out.println("s = " + s.getName() + ", " + s.getMembers().size());
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
