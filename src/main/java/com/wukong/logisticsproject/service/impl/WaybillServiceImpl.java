package com.wukong.logisticsproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wukong.logisticsproject.ex.DeleteException;
import com.wukong.logisticsproject.ex.InsertException;
import com.wukong.logisticsproject.ex.UpdateException;
import com.wukong.logisticsproject.ex.loadingCarException;
import com.wukong.logisticsproject.mapper.*;
import com.wukong.logisticsproject.model.*;
import com.wukong.logisticsproject.service.IWaybillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wukong.logisticsproject.utils.JqgridUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * 运单表 服务实现类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Service
public class WaybillServiceImpl extends ServiceImpl<WaybillMapper, Waybill> implements IWaybillService {

    @Autowired
    private WaybillMapper waybillMapper;

    @Autowired
    private IndentMapper indentMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private OrderWaybillMapper orderWaybillMapper;

    @Autowired
    private WaybillRecordMapper waybillRecordMapper;

    @Autowired
    private JqgridUtil jqgridUtil;


    /**
     * 获取运单所有信息
     *
     * @return
     */
    @Override
    public List<Waybill> getWaybillAllInfo(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        String roleName = (String) request.getSession().getAttribute("roleName");
        System.out.println(roleName);
        if (roleName.equals("superadmin") || roleName.equals("网点负责人")) {
            return waybillMapper.selectList(null);

        } else if (roleName.equals("Transporter")) {
            String staffId = loginUser.getUsername().substring(1);
            QueryWrapper<Staff> staffQueryWrapper = new QueryWrapper<>();
            staffQueryWrapper.eq("staff_id", staffId);//通过staffId，通过staffId查询员工负责运单
            Staff staffInfo = staffMapper.selectOne(staffQueryWrapper);
            String staffName = staffInfo.getStaffName();
            QueryWrapper<Waybill> waybillQueryWrapper = new QueryWrapper<>();
            waybillQueryWrapper.eq("vehicle_driver", staffName);

            return waybillMapper.selectList(waybillQueryWrapper);
        } else {
            String staffId = loginUser.getUsername().substring(1);
//            QueryWrapper<Staff> staffQueryWrapper = new QueryWrapper<>();
//            staffQueryWrapper.eq("staff_id", staffId);//通过staffId，通过staffId查询员工负责运单
//            Staff staffInfo = staffMapper.selectOne(staffQueryWrapper);
//            String staffName = staffInfo.getStaffName();
//            String staffBranch = staffInfo.getBranch();
            QueryWrapper<Waybill> waybillQueryWrapper = new QueryWrapper<>();
            waybillQueryWrapper.eq("receipt_staff", staffId).or().eq("dispatch_staff", staffId);
//                    .and(wrapper -> {
//                        wrapper.eq("operate_branch", staffBranch);
//                    });
            return waybillMapper.selectList(waybillQueryWrapper);
        }

    }

    /**
     * 运单信息转JSON
     *
     * @param page
     * @param count
     * @return
     */
    @Override
    public String getWaybillAllInfoJson(Integer page, Integer count, HttpServletRequest request) {
        List<Waybill> waybillList = getWaybillAllInfo(request);
        int toIndex = count * page;
        if (waybillList.size() < toIndex) {
            toIndex = waybillList.size();
        }
        List<Waybill> list = waybillList.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", waybillList.size(), count);
    }


    /**
     * 获取运单签收所有信息
     *
     * @return
     */
    @Override
    public List<Waybill> getWaybillReceipt(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");

        String staffId = loginUser.getUsername().substring(1);
        QueryWrapper<Staff> staffQueryWrapper = new QueryWrapper<>();
        staffQueryWrapper.eq("staff_id", staffId);//通过staffId，通过staffId查询员工负责运单
        Staff staffInfo = staffMapper.selectOne(staffQueryWrapper);
        String staffName = staffInfo.getStaffName();
        String staffBranch = staffInfo.getBranch();
        QueryWrapper<Waybill> waybillQueryWrapper = new QueryWrapper<>();
        waybillQueryWrapper.eq("receipt_staff", staffName).or().eq("dispatch_staff", staffName)
                .and(wrapper -> {
                    wrapper.eq("operate_branch", staffBranch);
                });
        return waybillMapper.selectList(waybillQueryWrapper);


    }

    /**
     * 查询功能
     *
     * @param waybillId
     * @param sendBranch
     * @param destination
     * @param customer
     * @param phone
     * @return
     */
    @Override
    public List<Waybill> findWayBillByParam(String waybillId, String sendBranch, String destination, String customer, String phone) {
        QueryWrapper<Waybill> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(waybillId), "waybill_id", waybillId)
                .like(!StringUtils.isEmpty(sendBranch), "send_branch", sendBranch)
                .like(!StringUtils.isEmpty(destination), "destination", destination)
                .like(!StringUtils.isEmpty(customer), "sender", customer).or().like(!StringUtils.isEmpty(customer), "consignor", customer)
                .like(!StringUtils.isEmpty(phone), "consignor_phone", phone).or().like(!StringUtils.isEmpty(phone), "sender_phone", phone);

        List<Waybill> waybillList = waybillMapper.selectList(queryWrapper);
        return waybillList;
    }

    @Override
    public String findWayBillByParamJson(String waybillId, String sendBranch, String destination, String customer, String phone, Integer page, Integer rows) {
        List<Waybill> waybillList = findWayBillByParam(waybillId, sendBranch, destination, customer, phone);
        int toIndex = rows * page;
        if (waybillList.size() < toIndex) {
            toIndex = waybillList.size();
        }
        List<Waybill> list = waybillList.subList(rows * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", waybillList.size(), rows);
    }

    /**
     * 修改功能
     *
     * @param waybill
     * @return
     */
    @Override
    public Integer updateWayBillById(Waybill waybill) {
        System.out.println(waybill);
        Integer rows = waybillMapper.updateById(waybill);
        if (rows != 1) {
            throw new UpdateException("修改数据失败！！");
        }
        return rows;
    }


    /**
     * 一对一生成运单
     *
     * @param indent
     * @param username
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer getInsertWayBill(Indent indent, String username) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //通过随机数生成运单编号
        String num = DateTimeFormatter.ofPattern("yyMMddHHss").format(LocalDateTime.now());
        String waybillId = "WK" + num;

        //通过收件人的省、市、县、乡镇查找网点表中网点就是目的网点

        List<Branch> branchList = branchMapper.findBranchNameByProvince(indent.getConsignorProvince(),
                indent.getConsignorCity(),
                indent.getConsignorCounty(),
                indent.getConsignorTown());
        String destinationBranch = branchList.get(0).getBranchName();


        String operator = "";
        String operateBranch = "";
        //通过员工id查询员工表的相关数据
        if (username.equals("admin")) {
            operator = username;
            operateBranch = "总公司";
        } else {
            String staffId = username.substring(1);
            Staff staff = staffMapper.findAllByName(Integer.parseInt(staffId));
            operator = staff.getStaffName();
            operateBranch = staff.getBranch();
        }


        //下一站网点就是当前网点的所属网点
        System.out.println("需要的数据为：branch_name" + indent.getCurrentBranch() + ",branch_city" + indent.getSenderCity());
        Branch branches = branchMapper.findSuosuBranchByBranchName(indent.getCurrentBranch(), indent.getSenderCity());
        System.out.println("all的数据为：" + branches);
        String nextBranch = null;
        System.out.println("23数据为：" + branches.getBranchLevel());
        if (!branches.getBranchLevel().equals("1")) {
            nextBranch = branches.getSuosuBranch();
        }

        //货物数量默认为1
        Integer number = 1;


        //重量
        Integer weight = Integer.parseInt(indent.getSize());

        //执行插入操作
        Waybill waybill = new Waybill().setWaybillId(waybillId)
                .setDestination(indent.getConsignorAddress())
                .setDestinationBranch(destinationBranch)
                .setReceiptStaff(indent.getStaffId())
                .setSender(indent.getSender())
                .setSenderPhone(indent.getSenderPhone())
                .setSendBranch(indent.getCurrentBranch())
                .setSendTime(LocalDateTime.parse(indent.getCreateTime(), formatter))
                .setConsignorPhone(indent.getConsignorPhone())
                .setConsignor(indent.getConsignor())
                .setOperator(operator)
                .setOperateBranch(operateBranch)
                .setOperateTime(LocalDateTime.now())
                .setNextBranch(nextBranch)
                .setNumber(number)
                .setWeight(weight);

        //修改订单表的状态
        Integer indentRow = indentMapper.updateStateById(indent.getIndentId());


        //修改员工状态
        Integer staffRow = staffMapper.updateStaffStateById(indent.getStaffId(), "1");

        //添加运单和订单的关系表记录
        Integer orderWaybillRow = orderWaybillMapper.insertOrderWaybill(new OrderWaybill().setIndentId(indent.getIndentId()).setWaybillId(waybillId));


        Integer waybillRow = waybillMapper.insertWaybill(waybill);

        //插入运单记录
        WaybillRecord waybillRecord = new WaybillRecord()
                .setWaybillId(waybillId)
                .setOperateBranch(operateBranch)
                .setOperator(operator)
                .setWaybillState("1")
                .setOperateTime(waybill.getOperateTime())
                .setPhone(branchMapper.findBranchByName(operateBranch).getBranchPhone());
        Integer WaybillRow = waybillRecordMapper.insertWaybillRecord(waybillRecord);

        if (waybillRow != 1 || indentRow != 1 || staffRow != 1 || orderWaybillRow != 1 || WaybillRow != 1) {
            throw new InsertException("员工分配失败！！！");
        }


        return waybillRow;
    }


    /**
     * 多对一生成运单
     *
     * @param indent
     * @param username
     * @return
     */
    @Override
    public Integer getInsertAnyWayBill(Indent indent, String username) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //通过随机数生成运单编号
        String num = DateTimeFormatter.ofPattern("yyMMddHHss").format(LocalDateTime.now());
        String waybillId = "WK" + num;

        //通过收件人的省、市、县、乡镇查找网点表中网点就是目的网点
        List<Branch> branchList = branchMapper.findBranchNameByProvince(indent.getConsignorProvince(),
                indent.getConsignorCity(),
                indent.getConsignorCounty(),
                indent.getConsignorTown());
        String destinationBranch = branchList.get(0).getBranchName();


        String operator = "";
        String operateBranch = "";
        //通过员工id查询员工表的相关数据
        if (username.equals("admin")) {
            operator = username;
            operateBranch = "总公司";
        } else {
            String staffId = username.substring(1);
            Staff staff = staffMapper.findAllByName(Integer.parseInt(staffId));
            operator = staff.getStaffName();
            operateBranch = staff.getBranch();
        }


        //下一站网点就是当前网点的所属网点
        System.out.println("需要的数据为：branch_name" + indent.getCurrentBranch() + ",branch_city" + indent.getSenderCity());
        Branch branches = branchMapper.findSuosuBranchByBranchName(indent.getCurrentBranch(), indent.getSenderCity());
        System.out.println("all的数据为：" + branches);
        String nextBranch = null;
        System.out.println("23数据为：" + branches.getBranchLevel());
        if (!branches.getBranchLevel().equals("1")) {
            nextBranch = branches.getSuosuBranch();
        }


        //执行插入操作
        Waybill waybill = new Waybill().setWaybillId(waybillId)
                .setDestination(indent.getConsignorAddress())
                .setDestinationBranch(destinationBranch)
                .setReceiptStaff(indent.getStaffId())
                .setSender(indent.getSender())
                .setSenderPhone(indent.getSenderPhone())
                .setSendBranch(indent.getCurrentBranch())
                .setSendTime(LocalDateTime.parse(indent.getCreateTime(), formatter))
                .setConsignorPhone(indent.getConsignorPhone())
                .setConsignor(indent.getConsignor())
                .setOperator(operator)
                .setOperateBranch(operateBranch)
                .setOperateTime(LocalDateTime.now())
                .setNextBranch(nextBranch)
                .setNumber(indent.getNumber())
                .setWeight(indent.getWeight());


        //改变多条订单状态
        String[] arr = indent.getIndentIdString().split(",");

        for (String indentId : arr) {
            indentMapper.updateStateById(Long.parseLong(indentId));

            //添加运单和订单的关系表记录
            Integer orderWaybillRow = orderWaybillMapper.insertOrderWaybill(new OrderWaybill().setIndentId(Long.parseLong(indentId)).setWaybillId(waybillId));
            if (orderWaybillRow != 1) {
                throw new InsertException("员工分配失败！！！");
            }
        }


        //修改员工状态
        Integer staffRow = staffMapper.updateStaffStateById(indent.getStaffId(), "1");

        Integer waybillRow = waybillMapper.insertWaybill(waybill);

        //插入运单记录
        WaybillRecord waybillRecord = new WaybillRecord()
                .setWaybillId(waybillId)
                .setOperateBranch(operateBranch)
                .setOperator(operator)
                .setWaybillState("1")
                .setOperateTime(waybill.getOperateTime())
                .setPhone(branchMapper.findBranchByName(operateBranch).getBranchPhone());
        Integer WaybillRow = waybillRecordMapper.insertWaybillRecord(waybillRecord);

        if (waybillRow != 1 || staffRow != 1 || WaybillRow != 1) {
            throw new InsertException("员工分配失败！！！");
        }


        return waybillRow;
    }


    /**
     * 查询所有已派件和已签收的运单
     *
     * @param destinationBranch
     * @return
     */
    @Override
    public List<Waybill> getFindSignWaybillALl(String destinationBranch, String waybillState) {
        List<Waybill> waybills = waybillMapper.findSignWaybillALl(destinationBranch, waybillState);
        for (Waybill waybill : waybills) {
            waybill.setWaybillStateStr(this.WaybillStateToString(waybill));
            Staff staff = staffMapper.findAllByName(waybill.getDispatchStaff());
            waybill.setDispatchStaffStr(staff.getStaffName());
        }

        return waybills;
    }

    @Override
    public String getFindSignWaybillALlJson(Integer page, Integer count, User loginUser, String waybillState) {
        //判断是否是管理员
        SysUserRole sysUserRole = sysUserRoleMapper.findRoleIdByUserId(loginUser.getUserId());
        List<Waybill> waybills = null;
        if (sysUserRole.getRoleId().equals(1)) {
            waybills = waybillMapper.findSignWaybillALl(null, waybillState);
            for (Waybill waybill : waybills) {

                waybill.setWaybillStateStr(this.WaybillStateToString(waybill));

                Staff staff = staffMapper.findAllByName(waybill.getDispatchStaff());
                waybill.setDispatchStaffStr(staff.getStaffName());
            }


        } else {
            System.err.println("账号为：" + loginUser.getUsername().substring(1));
            Staff staff = staffMapper.findAllByName(Integer.parseInt(loginUser.getUsername().substring(1)));
            waybills = getFindSignWaybillALl(staff.getBranch(), waybillState);
        }
        int toIndex = count * page;
        if (waybills.size() < toIndex) {
            toIndex = waybills.size();
        }
        List<Waybill> list = waybills.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", waybills.size(), count);
    }


    /**
     * 修改签收信息
     *
     * @param waybill
     * @param loginUser
     * @return
     */
    @Override
    public Integer getUpdateWaybillById(Waybill waybill, User loginUser) {

        //判断是否是管理员
        SysUserRole sysUserRole = sysUserRoleMapper.findRoleIdByUserId(loginUser.getUserId());
        String SignBranch = "";
        String Operator = "";
        LocalDateTime time = LocalDateTime.now();

        if (sysUserRole.getRoleId().equals(1)) {
            SignBranch = "总公司";
            Operator = "admin";

        } else {

            System.err.println("账号为：" + loginUser.getUsername().substring(1));
            Staff staff = staffMapper.findAllByName(Integer.parseInt(loginUser.getUsername().substring(1)));
            SignBranch = staff.getBranch();
            Operator = staff.getStaffName();
        }


        Integer row = waybillMapper.updateWaybillById(
                waybill.setSignBranch(SignBranch)
                        .setSignerTime(time)
                        .setOperateBranch(SignBranch)
                        .setOperateTime(time)
                        .setOperator(Operator)
                        .setWaybillState("9"));


        System.out.println("！！！数据为：" + waybill.getDispatchStaff());
        //查询网点电话
        Staff staffInfo =  staffMapper.selectById(waybill.getDispatchStaff());

        WaybillRecord waybillRecord = new WaybillRecord().setWaybillId(waybill.getWaybillId())
                .setOperateTime(time)
                .setWaybillState("9")
                .setPhone(staffInfo.getPhone())
                .setSigner(waybill.getSigner())
                .setOperator(Operator)
                .setOperateBranch(SignBranch);

        Integer waybillRow = waybillRecordMapper.insertWaybillRecord(waybillRecord);
        if (waybillRow != 1) {
            throw new UpdateException("签收失败！！");
        }

        //修改员工状态为空闲

        List<Waybill> waybills = waybillMapper.findCountByDispatchStaff(waybill.getDispatchStaff(), "8");
        if (waybills.size()==0) {
            staffMapper.updateStaffStateById(waybill.getDispatchStaff(), "0" );
        }

        if (row != 1) {
            throw new UpdateException("签收失败！！");
        }
        return row;
    }


    @Override
    public List<Waybill> getFindWaybillByParam(Waybill waybill) {
        List<Waybill> waybills = waybillMapper.findWaybillByParam(waybill);
        for (Waybill waybill1 : waybills) {
            waybill1.setWaybillStateStr(this.WaybillStateToString(waybill1));

            Staff staff = staffMapper.findAllByName(waybill1.getDispatchStaff());
            if (waybill.getWaybillState().equals("8") || waybill.getWaybillState().equals("9")) {
                waybill1.setDispatchStaffStr(staff.getStaffName());
            }
        }
        return waybills;
    }

    @Override
    public String getFindWaybillByParamJson(Integer page, Integer count, Waybill waybill, User loginUser) {
        System.out.println("收到的数据为：" + waybill);
        //判断是否是管理员
        SysUserRole sysUserRole = sysUserRoleMapper.findRoleIdByUserId(loginUser.getUserId());
        List<Waybill> waybills = null;
        if (sysUserRole.getRoleId().equals(1)) {
            waybills = waybillMapper.findWaybillByParam(waybill);
            System.out.println("11数据为：" + waybills);
            for (Waybill waybill1 : waybills) {

                waybill1.setWaybillStateStr(this.WaybillStateToString(waybill1));

                System.out.println("数据为：" + waybill1.getDispatchStaff());
                Staff staff = staffMapper.findAllByName(waybill1.getDispatchStaff());
                if (waybill.getWaybillState().equals("8") || waybill.getWaybillState().equals("9")) {
                    waybill1.setDispatchStaffStr(staff.getStaffName());
                }

            }


        } else {
            //不是管理员，只显示所属网点数据
            System.err.println("账号为：" + loginUser.getUsername().substring(1));
            Staff staff = staffMapper.findAllByName(Integer.parseInt(loginUser.getUsername().substring(1)));
            waybill.setDestinationBranch(staff.getBranch());
            waybills = getFindWaybillByParam(waybill);
        }
        int toIndex = count * page;
        if (waybills.size() < toIndex) {
            toIndex = waybills.size();
        }
        List<Waybill> list = waybills.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", waybills.size(), count);
    }


    /**
     * 根据运单状态和操作网点查询所有数据
     *
     * @param waybill
     * @return
     */
    @Override
    public List<Waybill> findWaybillByOperateBranch(Waybill waybill) {
        List<Waybill> waybills = waybillMapper.findWaybillByOperateBranch(waybill);
        for (Waybill waybill1 : waybills) {
            waybill1.setWaybillStateStr(this.WaybillStateToString(waybill1));
            Staff staff = staffMapper.findAllByName(waybill1.getDispatchStaff());
            waybill1.setReceiptStaffStr(staffMapper.findAllByName(waybill1.getReceiptStaff()).getStaffName());

        }
        return waybills;
    }

    @Override
    public String getFindWaybillByOperateBranchJson(Integer page, Integer count, Waybill waybill, Staff staffInfo) {

        System.out.println("问题排查11：" + waybill);
        //判断是否是管理员
        List<Waybill> waybills = null;
        if (!staffInfo.getStaffName().equals("admin")) {
            if (Integer.parseInt(waybill.getWaybillState()) == 1 || Integer.parseInt(waybill.getWaybillState()) == 7) {
                QueryWrapper<Waybill> waybillQueryWrapper = new QueryWrapper<>();
                //.or().eq("waybill_state", 7);
                waybillQueryWrapper.eq("waybill_state", waybill.getWaybillState()).eq("operate_branch", staffInfo.getBranch());
                waybills = waybillMapper.selectList(waybillQueryWrapper);
                for (Waybill waybill1 : waybills) {
                    waybill1.setWaybillStateStr(this.WaybillStateToString(waybill1));
                    Staff staff = staffMapper.findAllByName(waybill1.getDispatchStaff());
                    waybill1.setReceiptStaffStr(staffMapper.findAllByName(waybill1.getReceiptStaff()).getStaffName());
                }
            } else {
                waybill.setOperateBranch(staffInfo.getBranch());
                waybills = findWaybillByOperateBranch(waybill);
            }
            int toIndex = count * page;
            if (waybills.size() < toIndex) {
                toIndex = waybills.size();
            }
            List<Waybill> list = waybills.subList(count * (page - 1), toIndex);
            return jqgridUtil.getJson(list, page + "", waybills.size(), count);
        }
        return jqgridUtil.getJson(null, page + "", waybills.size(), count);
    }

    /**
     * 查询到件扫描的数据
     *
     * @param page
     * @param count
     * @param waybill
     * @param staffInfo
     * @return
     */
    @Override
    public String getFindArriveWaybillByOperateBranchJson(Integer page, Integer count, Waybill waybill, Staff staffInfo) {
        List<Waybill> waybills = null;
        if (staffInfo.getStaffName().equals("admin")) {
            waybills = waybillMapper.findWaybillByOperateBranch(waybill);
            for (Waybill waybill1 : waybills) {
                waybill1.setWaybillStateStr(this.WaybillStateToString(waybill1));
                System.out.println("发件扫描的数据为：" + waybill1.getDispatchStaff());
                waybill1.setReceiptStaffStr(staffMapper.findAllByName(waybill1.getReceiptStaff()).getStaffName());
            }
        } else {
            waybill.setNextBranch(staffInfo.getBranch());
            waybills = waybillMapper.findWaybillByOperateBranch(waybill);
            for (Waybill waybill1 : waybills) {
                waybill1.setWaybillStateStr(this.WaybillStateToString(waybill1));
                Staff staff = staffMapper.findAllByName(waybill1.getDispatchStaff());
                waybill1.setReceiptStaffStr(staffMapper.findAllByName(waybill1.getReceiptStaff()).getStaffName());

            }
        }
        int toIndex = count * page;
        if (waybills.size() < toIndex) {
            toIndex = waybills.size();
        }
        List<Waybill> list = waybills.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", waybills.size(), count);
    }


    /**
     * 回显下一站网点的数据
     *
     * @param
     * @param
     * @return
     */
    @Override
    public Waybill getFindSendScanData(Waybill waybill, Staff staffInfo) {
        Waybill newWaybill = new Waybill();

//        下一站
        //目的网点等于操作网点
        if (waybill.getDestinationBranch().equals(staffInfo.getBranch())) {
//        是，直接返回
            return newWaybill.setNextBranch(staffInfo.getBranch());
        }
        String addresseeCity = branchMapper.findBranchByName(waybill.getDestinationBranch()).getSuosuBranch();
        String addresseeProvince = branchMapper.findBranchByName(addresseeCity).getSuosuBranch();

        if (addresseeProvince.equals(waybill.getNextBranch())){
            return newWaybill.setNextBranch(addresseeCity);
        }

        if (addresseeCity.equals(waybill.getNextBranch())){
            return newWaybill.setNextBranch(waybill.getDestinationBranch());
        }

        Branch now = branchMapper.findBranchByName(waybill.getNextBranch());
        String nowLevel = now.getBranchLevel();
        String newBranch = "";

        if (nowLevel.equals("3")){
            if (addresseeCity.equals(now.getSuosuBranch())){
                return newWaybill.setNextBranch(waybill.getDestinationBranch());
            }
            newBranch = now.getSuosuBranch();
        }else if (nowLevel.equals("2")){
            if (addresseeProvince.equals(now.getSuosuBranch())){
                return newWaybill.setNextBranch(addresseeCity);
            }
            newBranch = now.getSuosuBranch();
        }else{
            return newWaybill.setNextBranch(addresseeProvince);
        }
        return newWaybill.setNextBranch(newBranch);


/*//        否，开始判断等级
        //判断下一站网点是否为空
        if (null == waybill.getNextBranch()  || waybill.getNextBranch().equals("") ) {
            String addresseeCity = branchMapper.findBranchByName(waybill.getDestinationBranch()).getBranchCity();
            String nowCity = branchMapper.findBranchByName(staffInfo.getBranch()).getBranchCity();
            String addresseeCounty = branchMapper.findBranchByName(waybill.getDestinationBranch()).getBranchCounty();
            System.out.println("addresseeCity:" + addresseeCity + ",nowCity:" + nowCity);
            if (addresseeCity.equals(nowCity)) {
                // 是，再查询自己的下级网点，满足 感3个条件：1.同市 2.二级网点3.下级网点的所属网点是当前网点
                newBranch = branchMapper.findBranchByParam(null,addresseeCounty, "2", staffInfo.getBranch()).getBranchName();

            } else {
                // 否，就根据这个市查询对应的1级网点
                newBranch = branchMapper.findBranchByParam(addresseeCity,null, "1", null).getBranchName();

            }
        } else {
//        先判断操作网点和下一站网点是否相同
//        否，直接输出下一站网点
            if (!staffInfo.getBranch().equals(waybill.getNextBranch())) {
                return newWaybill.setNextBranch(waybill.getNextBranch());
            }
//        是，根据下一站网点查询它的等级，及所属网点
//        如果是3级网点就查2级网点，如果是2级网点就查1级网点，
            Branch branch = branchMapper.findBranchByName(waybill.getNextBranch());
            if (branch.getBranchLevel().equals("3") || branch.getBranchLevel().equals("2")) {
                String addresseeCounty = branchMapper.findBranchByName(waybill.getDestinationBranch()).getBranchCounty();
                String addresseeTown = branchMapper.findBranchByName(waybill.getDestinationBranch()).getBranchTown();
                String nowCounty = branchMapper.findBranchByName(staffInfo.getBranch()).getBranchCounty();
                System.out.println("addresseeCity:" + addresseeCounty + ",nowCity:" + nowCounty);
                if (addresseeCounty.equals(nowCounty)) {
                    // 是，再查询自己的下级网点，满足感3个条件：1.同市 2.二级网点3.下级网点的所属网点是当前网点
                    newBranch = branchMapper.getFindBranchByParam(addresseeTown, "3", staffInfo.getBranch()).getBranchName();

                } else {
                    // 否，查找所属网点
                    return newWaybill.setNextBranch(branch.getSuosuBranch());

                }

            } else {
                String addresseeCity = branchMapper.findBranchByName(waybill.getDestinationBranch()).getBranchCity();
                String nowCity = branchMapper.findBranchByName(staffInfo.getBranch()).getBranchCity();
                String addresseeCounty = branchMapper.findBranchByName(waybill.getDestinationBranch()).getBranchCounty();
                System.out.println("addresseeCity:" + addresseeCity + ",nowCity:" + nowCity);
                if (addresseeCity.equals(nowCity)) {
                    // 是，再查询自己的下级网点，满足感3个条件：1.同市 2.二级网点3.下级网点的所属网点是当前网点
                    newBranch = branchMapper.findBranchByParam(null,addresseeCounty, "2", staffInfo.getBranch()).getBranchName();

                } else {
                    // 否，就根据这个市查询对应的1级网点

                    newBranch = branchMapper.findBranchByParam(addresseeCity, null,"1", null).getBranchName();

                }
            }

        }

        System.out.println("下一站" + newBranch);

        return newWaybill.setNextBranch(newBranch);*/
    }


    /**
     * 获取单挑运单数据
     *
     * @param wabillId
     * @return
     */
    @Override
    public Waybill getOneWayBillInfo(String wabillId) {
        return waybillMapper.selectById(wabillId);
    }


    /**
     * 显示下一站的所有数据
     *
     * @param
     * @param
     * @return
     */
    @Override
    public String getFindSendScanDataAllJson(Integer page, Integer count, Waybill waybill) {
        if (waybill.getNextBranch().equals("总公司")) {
            return null;
        }
        List<Branch> branches = null;
        //判断从一级出发
        if (waybill.getNextBranch().equals(null)) {
            branches = branchMapper.findBranchListByParam(null, null, "1", null);
        } else {
            System.out.println("错误排查1：" + waybill.getNextBranch());
            // 通过下一站的网点名称查询等级
            Branch branch = branchMapper.findBranchByName(waybill.getNextBranch());
            System.out.println("错误排查2：" + branch);
            String branchLevel = branchMapper.findBranchByName(waybill.getNextBranch()).getBranchLevel();
            QueryWrapper<Indent> indentQueryWrapper = new QueryWrapper<>();
            indentQueryWrapper.eq("waybill_id", waybill.getWaybillId());
            List<Indent> IndentInfo = indentMapper.selectList(indentQueryWrapper);

            // 如果是一级网点，就查询所有同等级
            if (branchLevel.equals("1")) {
                branches = branchMapper.findBranchListByParam(IndentInfo.get(0).getConsignorCity(), null, "1", null);
            } else if (branchLevel.equals("2")) {
                //如果是二级网点，就查询所有同等级并且同市
                branches = branchMapper.findBranchListByParam(branch.getBranchCity(), null, "2", null);
            } else if (branchLevel.equals("3")) {
                //如果是三级网点，就查询所有同等级并且同县
                branches = branchMapper.findBranchListByParam(branch.getBranchCity(), branch.getBranchCounty(), "3", null);
            }
        }


        int toIndex = count * page;
        if (branches.size() < toIndex) {
            toIndex = branches.size();
        }
        List<Branch> list = branches.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", branches.size(), count);

    }

    /**
     * 发件扫描中修改运单
     *
     * @param waybill
     * @return
     */
    @Override
    public Integer getUpdateSendWaybillById(Waybill waybill, Staff staffInfo) {

        Integer row;
        if (staffInfo.getBranch().equals(waybill.getNextBranch())) {
            row = waybillMapper.updateWaybillById(waybill.setWaybillState("6"));
        } else {
            row = waybillMapper.updateWaybillById(waybill.setWaybillState("2"));
        }

        WaybillRecord waybillRecord = new WaybillRecord()
                .setWaybillId(waybill.getWaybillId())
                .setOperateBranch(waybill.getOperateBranch())
                .setOperator(waybill.getOperator())
                .setWaybillState("2")
                .setOperateTime(LocalDateTime.parse(waybill.getOperateTimeStr()))
                .setPhone(branchMapper.findBranchByName(waybill.getOperateBranch()).getBranchPhone());
        Integer WaybillRow = waybillRecordMapper.insertWaybillRecord(waybillRecord);
        if (row != 1) {
            throw new UpdateException("发件扫描失败！");
        }
        // 收件员变为空闲
        staffMapper.updateStaffStateById(waybill.getReceiptStaff(), "0");
//        staffMapper.updateStaffStateById(staffInfo.getStaffId(), "0");
        return WaybillRow;
    }

    /**
     * 装车扫描中修改运单
     *
     * @param waybill
     * @return
     */
    @Override
    public Integer getUpdateloadingWaybillById(Waybill waybill, Vehicle vehicle) {


        // 修改车辆表中的净重字段
        Float weight = Float.parseFloat(vehicle.getVehicleWeight());
        Float vehicleLoad = Float.parseFloat(vehicle.getVehicleLoad());
        weight += waybill.getWeight();
        System.out.println("净重为：" + weight);
//        DecimalFormat df2   = new DecimalFormat("0.000");
//        Double newWeight = Double.parseDouble(df2.format(weight/1000));
        Float newVehicleLoad = vehicleLoad * 1000;
        if (weight == newVehicleLoad) {
            throw new loadingCarException("该车辆已装满,请另选车辆！");

        } else if (weight > newVehicleLoad) {
            throw new loadingCarException("超过该车辆的载重，请另选车辆！");
        }

        //修改车辆信息的净重
        Integer vehicleRow = vehicleMapper.updateVehicleByNum(new Vehicle().setVehicleWeight(weight.toString())
                .setVehicleNum(vehicle.getVehicleNum()));
        //修改业务员为空闲
        staffMapper.updateStaffStateById(waybill.getReceiptStaff(), "0");
        //修改司机状态为忙碌
        Integer staffRow = staffMapper.updateStaffStateById(
                vehicleMapper.findAllVehicleByNum(waybill.getVehicleNum()).getStaffId(), "1");
        if (vehicleRow != 1) {
            throw new UpdateException("发件扫描失败！");
        }

        Integer row = waybillMapper.updateWaybillById(waybill.setWaybillState("3"));
        if (row != 1) {
            throw new UpdateException("发件扫描失败！");
        }
        WaybillRecord waybillRecord = new WaybillRecord()
                .setWaybillId(waybill.getWaybillId())
                .setOperateBranch(waybill.getOperateBranch())
                .setOperator(waybill.getOperator())
                .setWaybillState("3")
                .setOperateTime(LocalDateTime.parse(waybill.getOperateTimeStr()))
                .setPhone(branchMapper.findBranchByName(waybill.getOperateBranch()).getBranchPhone());
        Integer WaybillRow = waybillRecordMapper.insertWaybillRecord(waybillRecord);

        return row;
    }

    /**
     * 卸车扫描的修改
     *
     * @param waybill
     * @return
     */
    @Override
    public Integer getUpdateunloadingWaybillById(Waybill waybill) {
        QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vehicle_num", waybill.getVehicleNum());
        Vehicle vehicle = vehicleMapper.selectOne(queryWrapper);
        // 修改车辆表中的净重字段
        Float weight = Float.parseFloat(vehicle.getVehicleWeight());
        weight -= waybill.getWeight();
        System.out.println("净重为：" + weight);

        //修改车辆信息的净重
        Integer vehicleRow = vehicleMapper.updateVehicleByNum(new Vehicle().setVehicleWeight(weight.toString())
                .setVehicleNum(vehicle.getVehicleNum()));
        if (vehicleRow != 1) {
            throw new UpdateException("卸车扫描失败！");
        }

        Integer row = waybillMapper.updateWaybillById(waybill.setWaybillState("6"));
        if (row != 1) {
            throw new UpdateException("卸车扫描失败！");
        }
        WaybillRecord waybillRecord = new WaybillRecord()
                .setWaybillId(waybill.getWaybillId())
                .setOperateBranch(waybill.getOperateBranch())
                .setOperator(waybill.getOperator())
                .setWaybillState("6")
                .setOperateTime(LocalDateTime.parse(waybill.getOperateTimeStr()))
                .setPhone(branchMapper.findBranchByName(waybill.getOperateBranch()).getBranchPhone());
        Integer WaybillRow = waybillRecordMapper.insertWaybillRecord(waybillRecord);

        //查询运单表中是否还有该车辆的运单
        QueryWrapper<Waybill> waybillQueryWrapper = new QueryWrapper<>();
        waybillQueryWrapper.eq("vehicle_num", waybill.getVehicleNum()).eq("waybill_state", "5");
        List<Waybill> waybillList=waybillMapper.selectList(waybillQueryWrapper);

        if (waybillList.size() == 0) {
            //修改司机状态为空闲
            Integer staffRow = staffMapper.updateStaffStateById(
                    vehicleMapper.findAllVehicleByNum(waybill.getVehicleNum()).getStaffId(), "0");
            //车辆的净重改为0
            vehicleMapper.updateVehicleByNum(new Vehicle().setVehicleNum(waybill.getVehicleNum()).setVehicleWeight("0"));

        }

        return row;
    }

    /**
     * 分派扫描的修改
     *
     * @param waybill
     * @return
     */
    @Override
    public Integer getUpdateDeliveryWaybillById(Waybill waybill) {
        waybill.setOperateTime(LocalDateTime.now());

        //修改员工状态
        Integer staffRow = staffMapper.updateStaffStateById(waybill.getDispatchStaff(), "1");


        Integer row = waybillMapper.updateWaybillById(waybill.setWaybillState("8"));
        if (row != 1) {
            throw new UpdateException("分派员工失败！");
        }
        WaybillRecord waybillRecord = new WaybillRecord()
                .setWaybillId(waybill.getWaybillId())
                .setOperateBranch(waybill.getOperateBranch())
                .setOperator(waybill.getOperator())
                .setWaybillState("8")
                .setOperateTime(waybill.getOperateTime())
                .setPhone(branchMapper.findBranchByName(waybill.getOperateBranch()).getBranchPhone());
        Integer WaybillRow = waybillRecordMapper.insertWaybillRecord(waybillRecord);

        return row;
    }

    /**
     * 分派扫描的批量修改
     *
     * @param waybill
     * @return
     */
    @Override
    public Integer getUpdateAnyDeliveryWaybillById(Waybill waybill) {

        LocalDateTime time = LocalDateTime.now();

        //修改员工状态
        Integer staffRow = staffMapper.updateStaffStateById(waybill.getDispatchStaff(), "1");

        System.out.println("运单号为：" + waybill.getWaybillIds());

        Integer row = 0;

        String[] arr = waybill.getWaybillIds().split(",");
        for (int i = 0; i < arr.length; i++) {
            row = waybillMapper.updateWaybillById(new Waybill().setWaybillId(arr[i])
                    .setOperator(waybill.getOperator())
                    .setOperateBranch(waybill.getOperateBranch())
                    .setOperateTime(time)
                    .setWaybillState("8")
                    .setDispatchStaff(waybill.getDispatchStaff()));
            if (row != 1) {
                throw new UpdateException("分派员工失败！");
            }

            WaybillRecord waybillRecord = new WaybillRecord()
                    .setWaybillId(arr[i])
                    .setOperateBranch(waybill.getOperateBranch())
                    .setOperator(waybill.getOperator())
                    .setWaybillState("8")
                    .setOperateTime(time)
                    .setPhone(branchMapper.findBranchByName(waybill.getOperateBranch()).getBranchPhone());
            Integer WaybillRow = waybillRecordMapper.insertWaybillRecord(waybillRecord);
        }
        return row;
    }


    /**
     * 到件扫描中修改
     *
     * @param waybill
     * @return
     */
    @Override
    public Integer getUpdateArrivePieceById(Waybill waybill, Staff staffInfo) {
        //判断目的网点是否是操作网点
        Integer row;
        String WaybillState;
        //目的网点等于操作网点
        if (waybill.getDestinationBranch().equals(waybill.getNextBranch())) {
//        是，直接返回
            WaybillState = "7";
            row = waybillMapper.updateWaybillById(waybill.setWaybillState(WaybillState));
        } else {
            WaybillState = "1";
            row = waybillMapper.updateWaybillById(waybill.setWaybillState(WaybillState));
        }


        WaybillRecord waybillRecord = new WaybillRecord()
                .setWaybillId(waybill.getWaybillId())
                .setOperateBranch(waybill.getOperateBranch())
                .setOperator(waybill.getOperator())
                .setWaybillState(WaybillState)
                .setOperateTime(LocalDateTime.parse(waybill.getOperateTimeStr()))
                .setPhone(branchMapper.findBranchByName(waybill.getOperateBranch()).getBranchPhone());
        Integer WaybillRow = waybillRecordMapper.insertWaybillRecord(waybillRecord);
        if (row != 1) {
            throw new UpdateException("到件扫描失败！");
        }
        return WaybillRow;
    }


    //运单状态转义
    public String WaybillStateToString(Waybill waybill) {
        String Str = "";
        switch (waybill.getWaybillState()) {
            case "0":
                Str = "待收件";
                break;
            case "1":
                Str = "已收件";
                break;
            case "2":
                Str = "已发件";
                break;
            case "3":
                Str = "已装车";
                break;
            case "4":
                Str = "已发车";
                break;
            case "5":
                Str = "已到车";
                break;
            case "6":
                Str = "已卸车";
                break;
            case "7":
                Str = "已到件";
                break;
            case "8":
                Str = "已派件";
                break;
            case "9":
                Str = "已签收";
                break;
        }
        return Str;
    }


}
