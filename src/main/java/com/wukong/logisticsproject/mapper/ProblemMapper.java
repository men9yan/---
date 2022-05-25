package com.wukong.logisticsproject.mapper;

import com.wukong.logisticsproject.model.Problem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 问题件表 Mapper 接口
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Repository
public interface ProblemMapper extends BaseMapper<Problem> {

    Integer addProblem(Problem problem);

    Integer updateProblem(Problem problem);

    List<Problem> findAllById(String waybillId );

    List<Problem> findAll();

}
