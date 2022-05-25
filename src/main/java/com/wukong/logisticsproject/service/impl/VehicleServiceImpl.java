package com.wukong.logisticsproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wukong.logisticsproject.ex.InsertException;
import com.wukong.logisticsproject.ex.UpdateException;
import com.wukong.logisticsproject.mapper.StaffMapper;
import com.wukong.logisticsproject.mapper.WaybillMapper;
import com.wukong.logisticsproject.model.*;
import com.wukong.logisticsproject.mapper.VehicleMapper;
import com.wukong.logisticsproject.service.IVehicleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wukong.logisticsproject.utils.JqgridUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 车辆信息表 服务实现类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Service
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle> implements IVehicleService {
    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private WaybillMapper  waybillMapper;

    @Autowired
    private JqgridUtil jqgridUtil;

    /**
     * 获取车辆信息
     *
     * @return
     */
    @Override
    public List<Vehicle> getVehicleAllList(Staff staffInfo) {
        if (staffInfo.getStaffName().equals("admin")){
            List<Vehicle> vehicleList = vehicleMapper.selectList(null);
            return vehicleList;
        }
        if (staffInfo.getStaffId() != null) {
            QueryWrapper<Staff> staffQueryWrapper = new QueryWrapper<>();
            staffQueryWrapper.eq("staff_id", staffInfo.getStaffId());
            Staff staffFindInfo = staffMapper.selectOne(staffQueryWrapper);
            if (staffFindInfo.getPosition().equals("转运员")) {
                QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("branch_suoshu", staffInfo.getBranch()).eq("staff_id", staffInfo.getStaffId());
                List<Vehicle> vehicleList = vehicleMapper.selectList(queryWrapper);
                return vehicleList;
            } else {
                QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("branch_suoshu", staffInfo.getBranch());
                List<Vehicle> vehicleList = vehicleMapper.selectList(queryWrapper);
                return vehicleList;
            }
        }
        return null;
    }


    /**
     * 车辆信息转JSON
     *
     * @param page
     * @param count
     * @return
     */
    @Override
    public String getAllVehicleJson(Integer page, Integer count, Staff staffInfo) {
        List<Vehicle> vehicles = getVehicleAllList(staffInfo);
        if(null == vehicles){
            return jqgridUtil.getJson(null, page + "", 0, count);
        }
        int toIndex = count * page;
        if (vehicles.size() < toIndex) {
            toIndex = vehicles.size();
        }
        List<Vehicle> list = vehicles.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", vehicles.size(), count);
    }

    /**
     * 查询空闲车辆
     *
     * @param branchSuoshu
     * @return
     */
    @Override
    public List<Vehicle> getVehicleByParamList(String branchSuoshu,String nextBranch) {
        System.err.println("数据为3：" + branchSuoshu);
        System.err.println("数据为4：" + nextBranch);

        //先查询该所属网点中所有空闲的车辆
        QueryWrapper<Vehicle> vehicleQueryWrapper = new QueryWrapper<>();
        vehicleQueryWrapper.eq("branch_suoshu", branchSuoshu)
                           .eq("vehicle_state", "空闲") ;
        List<Vehicle> vehicles = vehicleMapper.selectList(vehicleQueryWrapper);

        //遍历获取车牌号
        HashSet<String> vehicleNums = new HashSet<String>();

        for (Vehicle vehicle : vehicles) {
            vehicleNums.add(vehicle.getVehicleNum());
        }



        // 查询车牌号不为空、所属网点是当前网点、下一站不同的运单
        QueryWrapper<Waybill> waybillByParamQueryWrapper = new QueryWrapper<>();
        waybillByParamQueryWrapper.isNotNull("vehicle_num")
                .eq("operate_branch",branchSuoshu)
                .ne("next_branch",nextBranch);
        List<Waybill> waybillList = waybillMapper.selectList(waybillByParamQueryWrapper);

        //获取需要排除的车牌号
        HashSet<String> vehicleByParamNums = new HashSet<String>();

        for (Waybill waybill : waybillList) {
            vehicleByParamNums.add(waybill.getVehicleNum());
        }

        //筛选出可以使用的车辆
        vehicleNums.removeAll(vehicleByParamNums);




        List<Vehicle> vehicleList=null;
        if(vehicleNums.size()!=0){
            vehicleList=vehicleMapper.getAllVehicleByBranch(branchSuoshu,"空闲",null,vehicleNums);
        }


        return vehicleList;
    }

    @Override
    public String getVehicleByParamJson(Integer page, Integer count, Staff staffInfo,String nextBranch) {

        List<Vehicle> vehicles = getVehicleByParamList(staffInfo.getBranch(),nextBranch);
        int toIndex = count * page;
        if (vehicles.size() < toIndex) {
            toIndex = vehicles.size();
        }
        List<Vehicle> list = vehicles.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", vehicles.size(), count);
    }

    @Override
    public String getVehicleSearchByParamJson(Integer page, Integer count, Staff staffInfo, String nextBranch, Integer vehicleId) {

        //先通过下一站网点查询运单表
        QueryWrapper<Waybill> waybillQueryWrapper = new QueryWrapper<>();
        waybillQueryWrapper.eq("next_branch", nextBranch);
        List<Waybill> waybills = waybillMapper.selectList(waybillQueryWrapper);

        //遍历获取车牌号
        HashSet<String> vehicleNums = new HashSet<String>();

        for (Waybill waybill : waybills) {
            if(waybill.getVehicleNum()!=null){
                vehicleNums.add(waybill.getVehicleNum());
                System.out.println(waybill.getVehicleNum());
            }
        }

        // 查询车牌号不为空、所属网点是当前网点、下一站不同的运单
        QueryWrapper<Waybill> waybillByParamQueryWrapper = new QueryWrapper<>();
        waybillByParamQueryWrapper.ne("vehicle_num", null)
                .eq("operate_branch",staffInfo.getBranch())
                .ne("next_branch",nextBranch);
        List<Waybill> waybillList = waybillMapper.selectList(waybillByParamQueryWrapper);

        //获取需要排除的车牌号
        HashSet<String> vehicleByParamNums = new HashSet<String>();

        for (Waybill waybill : waybillList) {
            vehicleByParamNums.add(waybill.getVehicleNum());
        }
        //排除
        vehicleNums.removeAll(vehicleByParamNums);



        //通过车牌号、操作网点查询车俩信息
        List<Vehicle> vehicleList=vehicleMapper.getAllVehicleByBranch(staffInfo.getBranch(),"空闲",vehicleId,vehicleNums);
        System.out.println("vehicleList=="+vehicleList);
        if(vehicleList.size()==0){
            vehicleList=vehicleMapper.getAllVehicleByBranch(staffInfo.getBranch(),"空闲",vehicleId,null);
        }


        int toIndex = count * page;
        if (vehicleList.size() < toIndex) {
            toIndex = vehicleList.size();
        }
        List<Vehicle> list = vehicleList.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", vehicleList.size(), count);
    }


    /**
     * 添加车辆信息
     *
     * @param vehicle
     * @return
     */
    @Override
    public Integer getInsertVehicle(Vehicle vehicle) {
        Integer rows = vehicleMapper.insertVehicle(vehicle);
        if (rows != 1) {
            throw new InsertException("插入车辆数据失败！！");
        }
        return rows;
    }

    /**
     * 删除车辆信息
     *
     * @param vehicleId
     * @return
     */
    @Override
    public Integer deleteVehicleById(Integer vehicleId) {
        System.out.println("vehicleId:" + vehicleId);
        return vehicleMapper.deleteById(vehicleId);
    }

    /**
     * 修改车辆信息
     *
     * @param vehicle
     * @return
     */
    @Override
    public Integer updateVehicle(Vehicle vehicle) {
        System.out.println(vehicle);
        Integer rows = vehicleMapper.updateById(vehicle);
        if (rows != 1) {
            throw new UpdateException("修改数据失败！！");
        }
        return rows;
    }

    /**
     * 查询JSON
     *
     * @param vehicleNum
     * @param vehicleDriver
     * @param vehicleState
     * @param
     * @param page
     * @param count
     * @return
     */
    @Override
    public String getVehicleJsonByParam(String vehicleNum, String vehicleDriver, String vehicleState,  Integer page, Integer count) {
        List<Vehicle> Vehicle = getFindVehicleByParam(vehicleNum, vehicleDriver, vehicleState);
        int toIndex = count * page;
        if (Vehicle.size() < toIndex) {
            toIndex = Vehicle.size();
        }
        List<Vehicle> list = Vehicle.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", Vehicle.size(), count);
    }

    /**
     * 查询功能
     *
     * @param vehicleNum
     * @param vehicleDriver
     * @param vehicleState
     * @param
     * @return
     */
    @Override
    public List<Vehicle> getFindVehicleByParam(String vehicleNum, String vehicleDriver, String vehicleState) {



        System.err.println("!!!!!!!!!vehicleNum" + vehicleNum + ",vehicleDriver" + vehicleDriver + ",vehicleState" + vehicleState );
        QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(vehicleNum), "vehicle_num", vehicleNum)
                .like(!StringUtils.isEmpty(vehicleDriver), "vehicle_driver", vehicleDriver)
                .like(!StringUtils.isEmpty(vehicleState), "vehicle_state", vehicleState);
        List<Vehicle> vehicleList = vehicleMapper.selectList(queryWrapper);
        return vehicleList;
    }

    /*
    修改车辆状态
     */
    @Override
    public Integer UpdateVehicleState(Vehicle vehicleInfo) {
        QueryWrapper<Vehicle> vehicleQueryWrapper = new QueryWrapper<>();
        vehicleQueryWrapper.eq("vehicle_num", vehicleInfo.getVehicleNum());
//        Vehicle vehicleOneInfo = vehicleMapper.selectOne(vehicleQueryWrapper);
        return vehicleMapper.update(vehicleInfo, vehicleQueryWrapper);
    }


}
