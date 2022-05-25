package com.wukong.logisticsproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wukong.logisticsproject.ex.InsertException;
import com.wukong.logisticsproject.ex.NameExistException;
import com.wukong.logisticsproject.ex.UpdateException;
import com.wukong.logisticsproject.mapper.DepositoryMapper;
import com.wukong.logisticsproject.mapper.StaffMapper;
import com.wukong.logisticsproject.model.*;
import com.wukong.logisticsproject.mapper.BranchMapper;
import com.wukong.logisticsproject.service.IBranchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wukong.logisticsproject.utils.JqgridUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Service
public class BranchServiceImpl extends ServiceImpl<BranchMapper, Branch> implements IBranchService {

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private JqgridUtil jqgridUtil;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private DepositoryMapper depositoryMapper;

    /**
     * 获取网点审核信息
     *
     * @return
     */
    @Override
    public List<Branch> getBranchAuditAllList() {
        QueryWrapper<Branch> queryWrapper = new QueryWrapper<>();
        List<Branch> newBranchList = new ArrayList<>();
        queryWrapper.select("branch_id", "branch_name", "suosu_branch",
                "branch_manager", "branch_phone", "remark", "branch_time", "examine").eq("examine", 1);
        List<Branch> branchList = branchMapper.selectList(queryWrapper);
        branchList.forEach(branch -> {
            if (branch.getExamine().equals("1")) {
                branch.setExamineStr("待审核");
            } else if (branch.getExamine().equals("2")) {
                branch.setExamineStr("审核成功");
            } else if (branch.getExamine().equals("3")) {
                branch.setExamineStr("审核失败");
            }
            newBranchList.add(branch);
        });


        return newBranchList;
    }

    /**
     * 获取网点审核信息转Json
     *
     * @param page
     * @param count
     * @return
     */
    @Override
    public String getBranchAuditJson(Integer page, Integer count) {
        List<Branch> BranchAudit = getBranchAuditAllList();

        int toIndex = count * page;
        if (BranchAudit.size() < toIndex) {
            toIndex = BranchAudit.size();
        }
        List<Branch> list = BranchAudit.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", BranchAudit.size(), count);
    }


    @Override
    public List<Branch> getBranchAuditAllById(Integer branchId) {
        QueryWrapper<Branch> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("branch_id", "branch_name", "suosu_branch",
                "branch_manager", "branch_phone", "remark", "branch_time", "examine")
                .eq("examine", 1).like(null != branchId, "branch_id", branchId);
        List<Branch> branchList = branchMapper.selectList(queryWrapper);
        return branchList;
    }

    @Override
    public String getBranchAuditAllByIdJson(Integer branchId, Integer page, Integer count) {
        List<Branch> BranchAudit = getBranchAuditAllById(branchId);

        int toIndex = count * page;
        if (BranchAudit.size() < toIndex) {
            toIndex = BranchAudit.size();
        }
        List<Branch> list = BranchAudit.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", BranchAudit.size(), count);
    }

    @Override
    public List<Branch> getBranchAllList() {
        List<Branch> newBranchList = new ArrayList<>();
        List<Branch> branchList = branchMapper.findBranchAll();
        branchList.forEach(branch -> {
            if (branch.getExamine().equals("1")) {
                branch.setExamineStr("待审核");
            } else if (branch.getExamine().equals("2")) {
                branch.setExamineStr("审核成功");
            } else if (branch.getExamine().equals("3")) {
                branch.setExamineStr("审核失败");
            }

            newBranchList.add(branch);
        });
        return newBranchList;
    }

    @Override
    public String getAllBranchJson(Integer page, Integer count) {
        List<Branch> branches = getBranchAllList();
        int toIndex = count * page;
        if (branches.size() < toIndex) {
            toIndex = branches.size();
        }
        List<Branch> list = branches.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", branches.size(), count);
    }

    @Override
    public List<Branch> getfindBranchByLevel(Branch branch) {
        System.out.println("？？网点等级："+branch.getBranchLevel()+",省："+branch.getBranchProvince()+",市："+branch.getBranchCity());
        List<Branch> list=null;
        if (branch.getBranchLevel().equals("1")){
            list=branchMapper.findBranchByLevel(branch.getBranchLevel(),branch.getBranchProvince(),null,null,branch.getBranchId());
            System.out.println("1list:"+list);
        }else {
            list=branchMapper.findBranchByLevel(branch.getBranchLevel(),branch.getBranchProvince(),branch.getBranchCity(),null,branch.getBranchId());
            System.out.println("2list:"+list);
        }
        System.out.println("总list:"+list);
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer addBranch(Branch branch) {
        //查询网点名称唯一
        QueryWrapper<Branch> BranchQueryWrapper = new QueryWrapper<>();
        BranchQueryWrapper.eq("branch_name", branch.getBranchName()).eq("branch_province", branch.getBranchProvince());
        List<Branch> BranchfList = branchMapper.selectList(BranchQueryWrapper);
        if (BranchfList.size()!=0){
            throw new NameExistException("该网点名称已存在！！");
        }
        branch.setBranchTime(LocalDateTime.now());
        Integer rows = branchMapper.addBranch(branch);
        Depository depository = new Depository();
        depository.setDepositoryName(branch.getBranchName()+"仓库");
        depository.setAddress(branch.getBranchProvince()+branch.getBranchCity()+branch.getBranchCounty()+branch.getBranchAddress());
        depository.setBranch(branch.getBranchName());
        depository.setDepositoryManager(branch.getBranchManager());
        depository.setCapacity("1000");
        // 添加仓库时添加温湿度  默认生成阈值范围内的温湿度。  30 65
        depository.setTemperature(String.valueOf((int)(Math.random() * 31 + 10)));
        depository.setHumidity(String.valueOf((int)(Math.random() * 36 + 30)));
        depository.setPhone(branch.getBranchPhone());
        depositoryMapper.insert(depository);

        if (rows != 1) {
            throw new InsertException("插入网点失败！！");
        }


        return rows;
    }

    @Override
    public List<Branch> getFindAnyBranchById(Integer branchId) {
        if(null == branchId){
            return branchMapper.selectList(null);
        }
        return branchMapper.findAnyBranch(branchId);
    }

    @Override
    public List<Branch> getFindAnyBranch(Integer branchId) {
        return branchMapper.findBranchByExamine(branchId);
    }




    @Override
    public String getFindAnyBranchJson(Integer branchId, Integer page, Integer count) {
        List<Branch> branches = getFindAnyBranchById(branchId);
        int toIndex = count * page;
        if (branches.size() < toIndex) {
            toIndex = branches.size();
        }
        List<Branch> list = branches.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", branches.size(), count);
    }

    /**
     * 修改网点负责人
     * @param branch
     * @return
     */
    @Override
    public Integer getupdateBranch(Branch branch) {
        //判断员工是否已经是负责人
        Integer staffrows;
        Branch branch1=branchMapper.findBranchByStaffId(Integer.parseInt(branch.getStaffId()));
        if(branch1!=null){
          branchMapper.updateNullBranch(Integer.parseInt(branch.getStaffId()));
        }
        System.out.println("网点负责人网点数据："+branch);
        //获取原来的网点负责人并修改岗位
        QueryWrapper<Branch> BranchQueryWrapper = new QueryWrapper<>();
        BranchQueryWrapper.eq("branch_id", branch.getBranchId());
        Branch Branch = branchMapper.selectOne(BranchQueryWrapper);
        if(Branch.getStaffId()!=null){
            staffrows=staffMapper.updateStaffById(new Staff().setStaffId(Integer.parseInt(Branch.getStaffId())).setPosition("业务员"));
        }


        //修改员工的所属网点
        Staff staff=new Staff().setBranch(branch.getBranchName()).setStaffId(Integer.parseInt(branch.getStaffId())).setPosition("网点负责人");
        staffrows=staffMapper.updateStaffById(staff);

        // 修改仓库负责人
        Depository depository = depositoryMapper.selectOne(new QueryWrapper<Depository>().eq("branch", branch.getBranchName()));
        depository.setDepositoryManager(branch.getBranchManager());
        depository.setPhone(branch.getBranchPhone());
        depositoryMapper.updateById(depository);

        Integer row = branchMapper.updateBranch(branch);
        if (row != 1&&staffrows!=1) {
            throw new UpdateException("修改网点失败！！");
        }
        return row;
    }

    /**
     * 修改网点信息
     * @param branch
     * @return
     */
    @Override
    public Integer getupdateBranchByParam(Branch branch) {
        Integer row = branchMapper.updateBranch(branch);
        if (row != 1) {
            throw new UpdateException("修改网点失败！！");
        }
        return row;
    }

    @Override
    public List<Branch> getExamine() {
        List<Branch> newExamines = new ArrayList<>();
        Branch branch1 = new Branch("1", "待审核");
        Branch branch2 = new Branch("2", "审核成功");
        Branch branch3 = new Branch("3", "审核失败");

        newExamines.add(branch1);
        newExamines.add(branch2);
        newExamines.add(branch3);
        return newExamines;
    }

    @Override
    public List<Branch> getFindBranchName() {
        return branchMapper.findBranchAll();
    }

    @Override
    public Branch getExamineById(Integer id) {
        QueryWrapper<Branch> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("branch_id", id);
        return branchMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer updateExamine(Integer branchId, String examine, String remark) {
        QueryWrapper<Branch> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("branch_id", branchId);
        Branch branch = new Branch();
        branch.setExamine(examine);
        branch.setRemark(remark);
        Integer row = branchMapper.update(branch, queryWrapper);
        if (row != 1) {
            throw new UpdateException("修改网点失败！！");
        }
        return row;
    }

    @Override
    public List<Branch> getfindAnyBranchByBranch(Staff staffInfo) {
        QueryWrapper<Branch> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("branch_name", staffInfo.getBranch()).eq("examine", 2);
        return branchMapper.selectList(queryWrapper);
    }

    @Override
    public List<Branch> getfindAnyBranchByExamine() {
        return branchMapper.findAnyBranchByExamine();
    }

    @Override
    public String getfindAnyBranchByExamineJson(Integer page, Integer count) {
        List<Branch> branches = getfindAnyBranchByExamine();
        int toIndex = count * page;
        if (branches.size() < toIndex) {
            toIndex = branches.size();
        }
        List<Branch> list = branches.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", branches.size(), count);
    }


}
