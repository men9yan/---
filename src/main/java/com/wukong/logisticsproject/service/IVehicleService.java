package com.wukong.logisticsproject.service;

import com.wukong.logisticsproject.model.Staff;
import com.wukong.logisticsproject.model.Vehicle;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 车辆信息表 服务类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
public interface IVehicleService extends IService<Vehicle> {
    List<Vehicle> getVehicleAllList(Staff staffInfo);

    /**
     * 将所有车辆信息转成JSON
     */
    String getAllVehicleJson(@Param("page") Integer page,
                             @Param("count") Integer count,@Param("staffInfo") Staff staffInfo);


    List<Vehicle> getVehicleByParamList(String branchSuoshu,String nextBranch);

    /**
     * 将所有车辆信息转成JSON
     */
    String getVehicleByParamJson(@Param("page") Integer page,
                                 @Param("count") Integer count, Staff staffInfo,String nextBranch);

    //装车扫描中查询车辆信息
    String getVehicleSearchByParamJson(@Param("page") Integer page,
                                 @Param("count") Integer count,
                                 @Param("staffInfo")  Staff staffInfo,
                                 @Param("nextBranch")   String nextBranch,
                                 @Param("vehicleId")    Integer vehicleId   );

    /**
     * 添加车辆
     */
    Integer getInsertVehicle(Vehicle vehicle);

    Integer deleteVehicleById(Integer vehicleId);

    Integer updateVehicle(Vehicle vehicle);

    String getVehicleJsonByParam(@Param("vehicleNum") String vehicleNum,
                                 @Param("vehicleDriver") String vehicleDriver,
                                 @Param("vehicleState") String vehicleState,
                                 @Param("page") Integer page,
                                 @Param("count") Integer count);

    List<Vehicle> getFindVehicleByParam(
            @Param("vehicleNum") String vehicleNum,
            @Param("vehicleDriver") String vehicleDriver,
            @Param("vehicleState") String vehicleState);

    Integer UpdateVehicleState(Vehicle vehicleNum);
}
