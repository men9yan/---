package com.wukong.logisticsproject.mapper;

import com.wukong.logisticsproject.model.User;
import com.wukong.logisticsproject.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class UserMapperTest {

    @Autowired
    UserMapper mapper;
    @Test
    void resetPasswordById(){
        String password = MD5Utils.text2md5("admin123");
        Integer row=mapper.resetPasswordById(1,password);
        log.debug(">>> {}",row);
    }


    @Test
    void findUserById(){
        User users=mapper.findUserById(1);
        log.debug(">>> {}",users);

    }

    @Test
    void findUserByParam(){
        List<User> users =mapper.findUserByParam(1,"","");
        for (User user : users) {
            log.debug(">>>{}",user);
        }
    }
}
