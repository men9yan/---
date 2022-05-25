package com.wukong.logisticsproject.mapper;

import com.wukong.logisticsproject.model.Waybill;
import com.wukong.logisticsproject.model.WaybillRecord;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
@Slf4j
public class WaybillMapperTest {

    @Autowired
    private WaybillMapper waybillMapper;

    @Autowired
    private WaybillRecordMapper waybillRecordMapper;



    @Test
    void findWaybillByOperateBranch(){
        List<Waybill> waybills=waybillMapper.findWaybillByOperateBranch(new Waybill().setOperateBranch("总公司").setWaybillState("1"));
        log.debug(">>> {}",waybills.size());
        for (Waybill waybill : waybills) {
            log.debug("waybill >>> {}",waybill);
        }
    }


    @Test
    void findWaybillRecordById(){
        List<WaybillRecord> waybillRecords=waybillRecordMapper.findWaybillRecordById("WK2011042321");
        for (WaybillRecord waybillRecord : waybillRecords) {
            log.debug("waybill >>> {}",waybillRecord);
        }
    }


    @Test
    void a(){
        HashSet h1 = new HashSet();
        HashSet h2 = new HashSet();
        h1.add("闽C25813");
        h1.add("闽C25814");
        h1.add("闽C25815");
        h1.add("闽C25816");

        h2.add("闽C25816");
        h2.add("闽C25817");

        h1.removeAll(h2);
        log.debug("waybill >>> {}",h1);
    }
}
