package study.datajpa.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;

@SpringBootTest
//@Transactional
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    void save() throws Exception {
        Item item = new Item("A");
        itemRepository.save(item);
    }

}