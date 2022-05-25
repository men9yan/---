package com.wukong.logisticsproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wukong.logisticsproject.model.Problem;
import com.wukong.logisticsproject.model.ProblemType;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Repository
public interface ProblemTypeMapper extends BaseMapper<Problem> {
    List<ProblemType> findProblemTypeAll();

    Integer deleteById(Integer ptId);

    Integer updateProblemTypeById(ProblemType problemType);

    Integer addProblemType(ProblemType problemType);

}
