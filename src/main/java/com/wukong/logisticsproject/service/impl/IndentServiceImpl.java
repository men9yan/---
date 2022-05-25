package com.wukong.logisticsproject.service.impl;

import com.wukong.logisticsproject.ex.UpdateException;
import com.wukong.logisticsproject.mapper.StaffMapper;
import com.wukong.logisticsproject.mapper.SysUserRoleMapper;
import com.wukong.logisticsproject.model.*;
import com.wukong.logisticsproject.mapper.IndentMapper;
import com.wukong.logisticsproject.service.IIndentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wukong.logisticsproject.utils.JqgridUtil;
import com.wukong.logisticsproject.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Service
public class IndentServiceImpl extends ServiceImpl<IndentMapper, Indent> implements IIndentService {

    SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);

    @Autowired
    private JqgridUtil jqgridUtil;

    @Autowired
    private IndentMapper indentMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public List<Indent> getFindIndentAll(String currentBranch) {
        List<Indent> indents=indentMapper.findIndentAllByCurrentBranch(currentBranch);
        for (Indent indent : indents) {
            if(indent.getState().equals("0")){
                indent.setStateStr("未分配");
            }else if(indent.getState().equals("1")){
                indent.setStateStr("已分配");
            }
        }
        return indents;
    }

    @Override
    public String getIndentAllJson(Integer page, Integer count,User loginUser) {

        //判断是否是管理员
        SysUserRole sysUserRole=sysUserRoleMapper.findRoleIdByUserId(loginUser.getUserId());
        List<Indent> indents=null;
        if (sysUserRole.getRoleId().equals(1)){
            indents= indentMapper.selectList(null);
            for (Indent indent : indents) {
                if(indent.getState().equals("0")){
                    indent.setStateStr("未分配");
                }else if(indent.getState().equals("1")){
                    indent.setStateStr("已分配");
                }
            }
        }else{
            System.err.println("账号为："+loginUser.getUsername().substring(1));
            Staff staff=staffMapper.findAllByName(Integer.parseInt(loginUser.getUsername().substring(1)));

            indents=getFindIndentAll(staff.getBranch());
        }


        int toIndex = count * page;
        if (indents.size() < toIndex) {
            toIndex = indents.size();
        }
        List<Indent> list = indents.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", indents.size(), count);
    }

    @Override
    public List<Indent> getFindByIndentId(Integer indentId,String currentBranch) {
        List<Indent> indents=indentMapper.findByIndentId(indentId,currentBranch);
        for (Indent indent : indents) {
            if(indent.getState().equals("0")){
                indent.setStateStr("未分配");
            }else if(indent.getState().equals("1")){
                indent.setStateStr("已分配");
            }
        }
        return indents;
    }

    @Override
    public String getFindByIndentIdJson(Integer page, Integer count, Integer indentId,User loginUser) {
        //判断是否是管理员
        SysUserRole sysUserRole=sysUserRoleMapper.findRoleIdByUserId(loginUser.getUserId());
        List<Indent> indents=null;
        if (sysUserRole.getRoleId().equals(1)){
            indents= indentMapper.findByIndentId(indentId,null);

            for (Indent indent : indents) {
                if(indent.getState().equals("0")){
                    indent.setStateStr("未分配");
                }else if(indent.getState().equals("1")){
                    indent.setStateStr("已分配");
                }
            }

        }else {

            System.err.println("账号为：" + loginUser.getUsername().substring(1));
            Staff staff = staffMapper.findAllByName(Integer.parseInt(loginUser.getUsername().substring(1)));
            indents = getFindByIndentId(indentId, staff.getBranch());
        }
        int toIndex = count * page;
        if (indents.size() < toIndex) {
            toIndex = indents.size();
        }
        List<Indent> list = indents.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", indents.size(), count);
    }

    @Override
    public List<Indent> getFindIndentAny(String currentBranch) {
        List<Indent> indents=indentMapper.findIndentAny(currentBranch);
        for (Indent indent : indents) {
            if(indent.getState().equals("0")){
                indent.setStateStr("未分配");
            }else if(indent.getState().equals("1")){
                indent.setStateStr("已分配");
            }
        }
        return indents;
    }

    @Override
    public String getIndentAnyJson(Integer page, Integer count, User loginUser) {
        //判断是否是管理员
        SysUserRole sysUserRole=sysUserRoleMapper.findRoleIdByUserId(loginUser.getUserId());
        List<Indent> indents=null;
        if (sysUserRole.getRoleId().equals(1)){
            indents= indentMapper.findIndentAny(null);

            for (Indent indent : indents) {
                if(indent.getState().equals("0")){
                    indent.setStateStr("未分配");
                }else if(indent.getState().equals("1")){
                    indent.setStateStr("已分配");
                }
            }

        }else {
            System.err.println("账号为：" + loginUser.getUsername().substring(1));
            Staff staff = staffMapper.findAllByName(Integer.parseInt(loginUser.getUsername().substring(1)));
            indents = getFindIndentAny(staff.getBranch());
        }
        int toIndex = count * page;
        if (indents.size() < toIndex) {
            toIndex = indents.size();
        }
        List<Indent> list = indents.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", indents.size(), count);
    }

    @Override
    public List<Indent> getFindAllByIndentId(Integer indentId, String currentBranch,String sender,String senderPhone) {
        List<Indent> indents=indentMapper.findAllByIndentId(indentId,currentBranch,sender,senderPhone);

        for (Indent indent : indents) {
            if(indent.getState().equals("0")){
                indent.setStateStr("未分配");
            }else if(indent.getState().equals("1")){
                indent.setStateStr("已分配");
            }
        }
        return indents;
    }

    @Override
    public String getFindAllByIndentIdJson(Integer page, Integer count, Integer indentId,  User loginUser,String sender,String senderPhone ) {
        //判断是否是管理员
        SysUserRole sysUserRole=sysUserRoleMapper.findRoleIdByUserId(loginUser.getUserId());
        List<Indent> indents=null;
        if (sysUserRole.getRoleId().equals(1)){
            indents=indentMapper.findAllByIndentId(indentId,null,sender,senderPhone);

            for (Indent indent : indents) {
                if(indent.getState().equals("0")){
                    indent.setStateStr("未分配");
                }else if(indent.getState().equals("1")){
                    indent.setStateStr("已分配");
                }
            }

        }else {
            System.err.println("账号为：" + loginUser.getUsername().substring(1));
            Staff staff = staffMapper.findAllByName(Integer.parseInt(loginUser.getUsername().substring(1)));
            indents = getFindAllByIndentId(indentId, staff.getBranch(),sender,senderPhone);
        }
        int toIndex = count * page;
        if (indents.size() < toIndex) {
            toIndex = indents.size();
        }
        List<Indent> list = indents.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", indents.size(), count);
    }

    @Override
    public Integer insertIndent(User user, Indent indent) {
        Date date=new Date();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //通过随机数生成运单编号
        indent.setIndentId(idWorker.nextId() / 1000);
        indent.setCreateTime(formatter.format(date));
        indent.setState("0");
        Staff staff=staffMapper.findAllByName(Integer.parseInt(user.getUsername().substring(1)));
        indent.setCurrentBranch(staff.getBranch());
        return indentMapper.insert(indent);
    }


}
