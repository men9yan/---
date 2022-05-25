package com.wukong.logisticsproject.mapper;

import com.wukong.logisticsproject.model.Indent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class IndentMapperTest {

    @Autowired
    private IndentMapper indentMapper;

    @Test
    void findIndentAny(){
        List<Indent> indents=indentMapper.findIndentAny("文山里小区网点");
        log.debug("size >> {}",indents.size());
        for (Indent indent : indents) {
            log.debug(">>> {}",indent);
        }
    }


    @Test
    void selectOne(){
        List<Indent> indents= indentMapper.selectList(null);
        for (Indent indent : indents) {
            log.debug(">>{}",indent);
        }
    }


    @Test
    void a(){
        String[] arr="2020000000,2020000001,".split(",");
        log.debug(">>>  {}",arr.length);
        for (String s : arr) {
            log.debug(">> {}",s);
        }
    }




}
