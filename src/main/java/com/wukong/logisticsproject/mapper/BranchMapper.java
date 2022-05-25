package com.wukong.logisticsproject.mapper;

import com.wukong.logisticsproject.model.Branch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.beans.beancontext.BeanContext;
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
public interface BranchMapper extends BaseMapper<Branch> {
    List<Branch> findBranchAll();

    Integer addBranch(Branch branch);

    List<Branch> findBranchByLevel(String branchLevel, String branchProvince, String branchCity, String branchCounty, Integer branchId);

    Integer updateBranch(Branch branch);

    Integer updateNullBranch(Integer staffId);

    List<Branch> findBranchById(Integer branchId);

    List<Branch> findAnyBranch(Integer branchId);

    List<Branch> findBranchByExamine(Integer branchId);

    List<Branch> findAnyBranchByExamine();

    Branch findSuosuBranchByBranchName(@Param("branchName") String branchName, @Param("branchCity") String branchCity);

    Branch getFindBranchByParam(@Param("branchTown") String branchTown, @Param("branchLevel") String branchLevel, @Param("suosuBranch") String suosuBranch);

    List<Branch> findBranchNameByProvince(
            @Param("branchProvince") String branchProvince,
            @Param("branchCity") String branchCity,
            @Param("branchCounty") String branchCounty,
            @Param("branchTown") String branchTown
    );

    Branch findBranchByStaffId(Integer staffId);

    Branch findBranchByName(String branchName);

    Branch findBranchByParam(@Param("branchCity") String branchCity, @Param("branchCounty") String branchCounty, @Param("branchLevel") String branchLevel, @Param("suosuBranch") String suosuBranch);

    List<Branch> findBranchListByParam(String branchCity, String branchCounty, String branchLevel, String suosuBranch);

}
