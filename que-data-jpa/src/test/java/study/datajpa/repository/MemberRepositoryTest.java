package study.datajpa.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;
import study.datajpa.entity.Member;

import java.util.List;

@SpringBootTest
@Transactional
class MemberRepositoryTest {


    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("Spring Data JPA 테스트")
    void testMember() throws Exception {
        //given
        Member memberA = new Member("MemberA");
        Member savedMember = memberRepository.save(memberA);
        //when
        Member findMember = memberRepository.findById(savedMember.getId()).get();
        //then
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void basicCRUD() throws Exception {
        //given
        Member member = new Member("member1");
        Member member1 = new Member("member2");
        memberRepository.save(member);
        memberRepository.save(member1);

        //when
        Member findMember1 = memberRepository.findById(member.getId()).get();
        Member findMember2 = memberRepository.findById(member1.getId()).get();

        //then
        assertThat(member).isEqualTo(findMember1);
        assertThat(member1).isEqualTo(findMember2);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        memberRepository.delete(member);
        memberRepository.delete(member1);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }
}