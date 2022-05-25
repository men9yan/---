package com.wukong.logisticsproject.mapper;

import com.wukong.logisticsproject.model.User;
import com.wukong.logisticsproject.model.Vehicle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 车辆信息表 Mapper 接口
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Repository
public interface VehicleMapper extends BaseMapper<Vehicle> {

    /**
     * 添加车辆信息
     * @param vehicle
     * @return
     */
    Integer insertVehicle(Vehicle vehicle);

    Integer updateVehicleByNum(Vehicle vehicle);

    Vehicle findAllVehicleByNum(String vehicleNum);

    List<Vehicle> getAllVehicleByBranch(@Param("branchSuoshu") String branchSuoshu,
                                        @Param("vehicleState") String vehicleState,
                                        @Param("vehicleId") Integer vehicleId,
                                        @Param("list") HashSet<String> vehicleNums);


    List<Vehicle> getVehicleNotInVehicleNum(@Param("branchSuoshu") String branchSuoshu,
                                        @Param("vehicleState") String vehicleState,
                                        @Param("list") HashSet<String> vehicleByParamNums);
}

