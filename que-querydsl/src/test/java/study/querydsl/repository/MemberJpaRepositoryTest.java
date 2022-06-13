package study.querydsl.repository;

import static org.assertj.core.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void basicTest() {
        Member member1 = new Member("member1", 10);
        memberJpaRepository.save(member1);

        Member findMember = memberJpaRepository.findById(member1.getId()).get();
        assertThat(findMember).isEqualTo(member1);

        List<Member> result1 = memberJpaRepository.findAll_Querydsl();
        assertThat(result1).containsExactly(member1);

        List<Member> result2 = memberJpaRepository.findByUsername_Querydsl(member1.getUsername());
        assertThat(result2).containsExactly(member1);
    }

    @Test
    void searchTest() {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // when
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(20);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");
        List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(condition);

        // then
        assertThat(result).extracting("username").containsExactly("member3","member4");
    }
}