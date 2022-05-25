package com.wukong.logisticsproject.mapper;

import com.wukong.logisticsproject.model.Staff;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Repository
public interface StaffMapper extends BaseMapper<Staff> {
    /**
     * 查询员工表的所有字段
     *
     * @return 员工列表
     */
//    List<Staff> findStaffAll();

    Integer deleteStaffById(Integer staffId);

    Integer insertStaff(Staff staff);

    Integer updateStaffById(Staff staff);

    List<Staff> findStaffByParam(
            @Param("staffId") Integer staffId,
            @Param("staffName") String staffName,
            @Param("branch") String branch
    );

    Staff  findAllByName(Integer username);

    List<Staff> findAllByBranch(String branch);

    Integer updateStaffStateById(@Param("staffId") Integer staffId,@Param("state") String state);
}
