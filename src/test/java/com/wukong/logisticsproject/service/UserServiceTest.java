package com.wukong.logisticsproject.service;

import com.wukong.logisticsproject.mapper.BranchMapper;
import com.wukong.logisticsproject.mapper.DepositoryMapper;
import com.wukong.logisticsproject.model.*;
import com.wukong.logisticsproject.service.impl.WaybillServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Slf4j
public class UserServiceTest {
    @Autowired
    private  IUserService service;

    @Autowired
    private WaybillServiceImpl waybillService;

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private DepositoryMapper depositoryMapper;

    @Autowired
    private IDepositoryService depositoryService;

    @Test
    void getUserAllList(){
        List<User> users=service.getUserAllList();
        log.debug(">>>>  {}",users.size());
        for (User user : users) {
            log.debug("user >>{}",user);
        }
    }

    @Test
    void test(){
       /* Waybill waybill = new Waybill();
        Staff staffInfo = new Staff();
        waybill.setDestinationBranch("七台河市新兴分网点");
        waybill.setNextBranch("黑龙江省七台河市分网点");
        staffInfo.setBranch("黑龙江省七台河市分网点");
        System.out.println(waybillService.getFindSendScanData(waybill, staffInfo).getNextBranch());*/

        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(day));

    }

    @Test
    void sqlShell(){
        List<Branch> branches = branchMapper.selectList(null);
        for (Branch branch: branches) {
            Depository depository = new Depository();
            depository.setDepositoryName(branch.getBranchName()+"仓库");
            depository.setAddress(branch.getBranchProvince()+branch.getBranchCity()+branch.getBranchCounty()+branch.getBranchAddress());
            depository.setBranch(branch.getBranchName());
            depository.setDepositoryManager(branch.getBranchManager());
            depository.setCapacity("1000");
            depository.setTemperature("22");
            depository.setHumidity("45%");
            depository.setPhone(branch.getBranchPhone());
            depositoryMapper.insert(depository);
        }
    }

    @Test
    void test1(){


        // System.out.println(depositoryService.getDepository(1, 10, new User().setUserId(1)));
    }
}
