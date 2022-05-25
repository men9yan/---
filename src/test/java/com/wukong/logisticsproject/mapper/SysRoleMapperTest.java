package com.wukong.logisticsproject.mapper;

import com.wukong.logisticsproject.model.SysRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class SysRoleMapperTest {

    @Autowired
    private SysRoleMapper mapper;

    @Test
    void findRoleName(){
        List<SysRole> roles = mapper.findRoleName();
        log.debug(">> {}",roles.size());
        for (SysRole role : roles) {
            log.debug(">>> {}",role);
        }
    }
}
