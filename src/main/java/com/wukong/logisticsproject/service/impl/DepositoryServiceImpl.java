package com.wukong.logisticsproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.wukong.logisticsproject.mapper.StaffMapper;
import com.wukong.logisticsproject.mapper.SysUserRoleMapper;
import com.wukong.logisticsproject.model.*;
import com.wukong.logisticsproject.mapper.DepositoryMapper;
import com.wukong.logisticsproject.service.IDepositoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wukong.logisticsproject.utils.JqgridUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 仓库表 服务实现类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Service
public class DepositoryServiceImpl extends ServiceImpl<DepositoryMapper, Depository> implements IDepositoryService {

    @Autowired
    private DepositoryMapper depositoryMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private JqgridUtil jqgridUtil;

    @Override
    public String getDepository(Integer page, Integer count, User loginUser) {
        SysUserRole sysUserRole = sysUserRoleMapper.findRoleIdByUserId(loginUser.getUserId());
        List<Depository> depositories = null;
        if (sysUserRole.getRoleId().equals(1)) {
            depositories = depositoryMapper.selectList(null);
        } else {
            System.err.println("账号为：" + loginUser.getUsername().substring(1));
            Staff staff = staffMapper.findAllByName(Integer.parseInt(loginUser.getUsername().substring(1)));
            depositories = depositoryMapper.selectList(new QueryWrapper<Depository>().eq("branch",staff.getBranch()));
        }
        // 温湿度提醒  温度阈值范围 10 - 40   湿度阈值范围 30 65
        for (Depository depository:depositories) {
            if (Integer.parseInt(depository.getTemperature()) >= 40){
                depository.setTemperature(depository.getTemperature()+"(温度过高)");
            }else if (Integer.parseInt(depository.getTemperature()) <= 10){
                depository.setTemperature(depository.getTemperature()+"(温度过低)");
            }
            String hun = depository.getHumidity().substring(0,depository.getHumidity().length() - 1);
            if (Integer.parseInt(hun) >= 65){
                depository.setHumidity(depository.getHumidity()+"(湿度过高)");
            }else if (Integer.parseInt(hun) <= 30){
                depository.setHumidity(depository.getHumidity()+"(湿度过低)");
            }

        }
        int toIndex = count * page;
        if (depositories.size() < toIndex) {
            toIndex = depositories.size();
        }
        List<Depository> list = depositories.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", depositories.size(), count);

    }

    @Override
    public Integer addData(Depository depository) {
        Depository result = depositoryMapper.selectById(depository.getId());
        result.setTemperature(depository.getTemperature());
        result.setHumidity(depository.getHumidity()+"%");
        return depositoryMapper.updateById(result);
    }
}
