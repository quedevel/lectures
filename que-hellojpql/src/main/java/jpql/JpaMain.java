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


//            벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리
//            벌크 연산을 먼저 실행

            // flush
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            // 벌크 연산 수행 후 영속성 컨텍스트 초기화
            em.clear();

            // 그리고 다시 조회를 해와야한다.
            Member m = em.find(Member.class, member.getId());

            System.out.println("resultCount = " + resultCount);

            System.out.println("m = " + m.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}
