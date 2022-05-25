package com.wukong.logisticsproject.service;

import com.wukong.logisticsproject.model.Branch;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wukong.logisticsproject.model.Staff;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
public interface IBranchService extends IService<Branch> {

    /**
     * 获取所有网点审核信息
     *
     * @return
     */
    List<Branch> getBranchAuditAllList();

    String getBranchAuditJson(@Param("page") Integer page,
                              @Param("count") Integer count);

    /**
     * 通过网点编号查询所需信息
     */

    List<Branch> getBranchAuditAllById(Integer branchId);

    String getBranchAuditAllByIdJson(@Param("branchId") Integer branchId,
                                     @Param("page") Integer page,
                                     @Param("count") Integer count);


    List<Branch> getBranchAllList();

    public String getAllBranchJson(@Param("page") Integer page,
                                   @Param("count") Integer count);

    List<Branch> getfindBranchByLevel(Branch branch);

    Integer addBranch(Branch branch);

    List<Branch> getFindAnyBranch(Integer branchId);

    Integer getupdateBranch(Branch branch);

    Integer getupdateBranchByParam(Branch branch);

    Branch getExamineById(Integer id);

    List<Branch> getFindAnyBranchById(Integer branchId);

    String getFindAnyBranchJson(Integer branchId, Integer page, Integer count);

    Integer updateExamine(Integer branchId, String examine, String remark);

    List<Branch> getfindAnyBranchByBranch(Staff staffInfo);

    List<Branch> getfindAnyBranchByExamine();

    String getfindAnyBranchByExamineJson(Integer page, Integer count);

    List<Branch> getExamine();

    List<Branch> getFindBranchName();
}