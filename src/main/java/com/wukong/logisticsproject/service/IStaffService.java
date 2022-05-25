package com.wukong.logisticsproject.service;

import com.wukong.logisticsproject.model.Staff;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface IStaffService extends IService<Staff> {
    List<Staff> getStaffAllList();

    List<Staff> getStaffAll();

    Integer deleteStaffById(Integer staffId);

    public String getAllStaffJson(@Param("page") Integer page,
                                  @Param("count") Integer count);

    public String getAllStaffJsonByBranch(@Param("page") Integer page,
                                  @Param("count") Integer count,
                                          @Param("staffInfo") Staff staffInfo);



    public String getAllStaffByDriverJson(@Param("page") Integer page,
                                 @Param("count") Integer count,@Param("staffInfo") Staff staffInfo);

    Integer getinsertStaff(Staff staff);

    Integer updateStaff(Staff staff);

    List<Staff> getfindStaffByParam(
            @Param("staffId") Integer staffId,
            @Param("staffName") String staffName,
            @Param("branch") String branch
    );

    public String getStaffJsonByParam(@Param("staffId") Integer staffId,
                                      @Param("staffName") String staffName,
                                      @Param("branch") String branch,
                                      @Param("page") Integer page,
                                      @Param("count") Integer count);

    Staff getFindStaffByName(String username);

    List<Staff> getFindAllByBranch(String branch);

    List<Staff> getFindAllByBranchAndRole(String branch);

    Staff getReceiptBaseInfo(String staffId);
}
