package com.wukong.logisticsproject.service;

import com.wukong.logisticsproject.model.Problem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wukong.logisticsproject.model.ProblemType;
import com.wukong.logisticsproject.model.Staff;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 问题件表 服务类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
public interface IProblemService extends IService<Problem> {

    Integer getAddProblem(Problem problem);

    public String getAllProblemJson(@Param("page") Integer page,
                                        @Param("count") Integer count);

    Integer updateProblem(@Param("problem") Problem problem,
                          @Param("staffInfo") Staff staffInfo);

    public String getAllProblemByIdJson(@Param("page") Integer page,
                                    @Param("count") Integer count,
                                        @Param("waybillId") String waybillId);


    public String getAllProblemTypeJson(@Param("page") Integer page,
                                  @Param("count") Integer count);

    List<ProblemType> getFindAll();

    Integer getDeleteById(Integer ptId);

    Integer getUpdateProblemTypeById(ProblemType problemType);

    Integer getAddProblemType(ProblemType problemType);
}
