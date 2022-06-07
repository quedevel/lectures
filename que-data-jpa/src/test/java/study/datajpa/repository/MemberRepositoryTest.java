package study.datajpa.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;
import study.datajpa.entity.Member;

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
}