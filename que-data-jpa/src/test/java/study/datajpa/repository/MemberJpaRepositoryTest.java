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
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    @DisplayName("맴버 저장 및 찾기 테스트")
    void testMember() throws Exception {
        //given
        Member memberA = new Member("MemberA");
        Member savedMember = memberJpaRepository.save(memberA);
        //when
        Member findMember = memberJpaRepository.find(savedMember.getId());
        //then
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(savedMember);
    }
}