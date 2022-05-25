package com.wukong.logisticsproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wukong.logisticsproject.ex.DeleteException;
import com.wukong.logisticsproject.ex.InsertException;
import com.wukong.logisticsproject.ex.NameExistException;
import com.wukong.logisticsproject.ex.UpdateException;
import com.wukong.logisticsproject.mapper.BranchMapper;
import com.wukong.logisticsproject.mapper.UserMapper;
import com.wukong.logisticsproject.model.Branch;
import com.wukong.logisticsproject.model.Staff;
import com.wukong.logisticsproject.mapper.StaffMapper;
import com.wukong.logisticsproject.model.User;
import com.wukong.logisticsproject.service.IStaffService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wukong.logisticsproject.utils.JqgridUtil;
import com.wukong.logisticsproject.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
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
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements IStaffService {
    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private JqgridUtil jqgridUtil;

    @Override
    public List<Staff> getStaffAllList() {
        QueryWrapper<Staff> StaffQueryWrapper = new QueryWrapper<>();
        StaffQueryWrapper.ne("isdelete", "是");
        List<Staff> BranchfList = staffMapper.selectList(StaffQueryWrapper);
        return BranchfList;
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    @Override
    public List<Staff> getStaffAll() {
        return staffMapper.selectList(null);
    }

    /**
     * 删除员工Id
     *
     * @param staffId
     * @return
     */
    @Override
    public Integer deleteStaffById(Integer staffId) {
        String username = "Y" + staffId;
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        Integer rows = staffMapper.deleteStaffById(staffId);

        if (rows != 1) {
            throw new DeleteException("刪除员工数据失败");
        }
        User userInfo = new User();
        userInfo.setIsDelete(1);
        userMapper.update(userInfo, userQueryWrapper);

        branchMapper.updateNullBranch(staffId);

        return rows;
    }

    @Override
    public String getAllStaffJson(Integer page, Integer count) {
        List<Staff> staff = getStaffAll();
        int toIndex = count * page;
        if (staff.size() < toIndex) {
            toIndex = staff.size();
        }
        List<Staff> list = staff.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", staff.size(), count);
    }


    /**
     * 根据网点查询所有员工
     *
     * @param page
     * @param count
     * @param staffInfo
     * @return
     */
    @Override
    public String getAllStaffJsonByBranch(Integer page, Integer count, Staff staffInfo) {
        QueryWrapper<Staff> staffQueryWrapper = new QueryWrapper<>();
        List<Staff> staff;
        if (staffInfo.getStaffName().equals("admin")) {
            staff = staffMapper.selectList(null);
        } else {
            staffQueryWrapper.eq("branch", staffInfo.getBranch());
            staff = staffMapper.selectList(staffQueryWrapper);
        }

        int toIndex = count * page;
        if (staff.size() < toIndex) {
            toIndex = staff.size();
        }
        List<Staff> list = staff.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", staff.size(), count);
    }

    @Override
    public String getAllStaffByDriverJson(Integer page, Integer count, Staff staffInfo) {
        QueryWrapper<Staff> StaffQueryWrapper = new QueryWrapper<>();
        StaffQueryWrapper.eq("branch", staffInfo.getBranch()).eq("position", "转运员");
        List<Staff> BranchfList = staffMapper.selectList(StaffQueryWrapper);

        int toIndex = count * page;
        if (BranchfList.size() < toIndex) {
            toIndex = BranchfList.size();
        }
        List<Staff> list = BranchfList.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", BranchfList.size(), count);
    }


    /**
     * 添加员工
     *
     * @param staff
     * @return
     */
    @Override
    public Integer getinsertStaff(Staff staff) {

        //判断身份证号唯一
        QueryWrapper<Staff> staffQueryWrapper = new QueryWrapper<>();
        staffQueryWrapper.eq("idCard", staff.getIdCard());
        List<Staff> staffList = staffMapper.selectList(staffQueryWrapper);
        if (staffList.size() != 0) {
            throw new NameExistException("身份证号已被使用！！");
        }


        Integer rowStaff = staffMapper.insertStaff(staff);

        Staff staffInfo = staffMapper.selectOne(staffQueryWrapper);

        String username = "Y" + staffInfo.getStaffId();
        String IdCard = staff.getIdCard();
        String password = MD5Utils.text2md5(IdCard.substring(IdCard.length() - 6, IdCard.length()));
        String phone = staff.getPhone();
        String trueName = staff.getStaffName();
        System.err.println("插入用户的数据：" + username + "," + IdCard.substring(IdCard.length() - 6, IdCard.length()) + "," + phone + "," + trueName);
        Integer rowUser = userMapper.addUser(username, password, phone, trueName);

        if (rowStaff != 1 && rowUser != 1) {
            throw new UpdateException("插入员工数据失败！！");
        }
        return rowUser;

    }

    @Override
    public Integer updateStaff(Staff staff) {

        //判断身份证号唯一
        QueryWrapper<Staff> staffQueryWrapper = new QueryWrapper<>();
        staffQueryWrapper.eq("idCard", staff.getIdCard()).ne("staff_id", staff.getStaffId());
        List<Staff> staffList = staffMapper.selectList(staffQueryWrapper);
        if (staffList.size() != 0) {
            throw new NameExistException("身份证号已被使用！！");
        }

        Integer rows = staffMapper.updateStaffById(staff);
        if (rows != 1) {
            throw new UpdateException("修改数据失败！！");
        }
        return rows;
    }

    @Override
    public List<Staff> getfindStaffByParam(Integer staffId, String staffName, String branch) {
        System.err.println("!!!!!!!!!staffId" + staffId + ",staffName" + staffName + ",branch" + branch);
        List<Staff> stafflist = staffMapper.findStaffByParam(staffId, staffName, branch);
        return stafflist;
    }

    @Override
    public String getStaffJsonByParam(Integer staffId, String staffName, String branch, Integer page, Integer count) {
        List<Staff> staff = getfindStaffByParam(staffId, staffName, branch);
        int toIndex = count * page;
        if (staff.size() < toIndex) {
            toIndex = staff.size();
        }
        List<Staff> list = staff.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", staff.size(), count);
    }

    @Override
    public Staff getFindStaffByName(String username) {

        return staffMapper.findAllByName(Integer.parseInt(username));
    }


    @Override
    public List<Staff> getFindAllByBranch(String branch) {
        return staffMapper.findAllByBranch(branch);
    }

    @Override
    public List<Staff> getFindAllByBranchAndRole(String branch) {
        QueryWrapper<Staff> staffQueryWrapper = new QueryWrapper<>();
        staffQueryWrapper.eq("branch", branch).eq("state", "0").ne("position", "转运员");
        List<Staff> staffList = staffMapper.selectList(staffQueryWrapper);
        return staffList;
    }


    @Override
    public Staff getReceiptBaseInfo(String staffId) {
        QueryWrapper<Staff> staffQueryWrapper = new QueryWrapper<>();
        staffQueryWrapper.eq("staff_id", staffId);
        Staff staffInfo = staffMapper.selectOne(staffQueryWrapper);
        return staffInfo;
    }

}
