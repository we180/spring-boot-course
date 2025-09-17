package top.cs.boot.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.cs.boot.mp.entity.UserAccount;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserAccountMapperTest {
    @Resource
    private UserAccountMapper userAccountMapper;

    @Test
    void testSelectById(){
        UserAccount userAccount = userAccountMapper.selectById(1);
        assertEquals("admin001", userAccount.getUsername());
    }

    @Test
    void testSelectCount(){
        Wrapper<UserAccount> wrapper = new QueryWrapper<>();
        Long count = userAccountMapper.selectCount(wrapper);
        assertEquals(63, count);
    }

    @Test
    void testInsert(){
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername("testcs5");
        userAccount.setNickname("testcs5");
        userAccount.setEmail("15974154866@qq.com");
        userAccount.setPhone("1234567890");
        userAccount.setStatus(true);
        userAccount.setDeleted(false);
        int insert = userAccountMapper.insert(userAccount);
        assertEquals(1, insert);
    }
}