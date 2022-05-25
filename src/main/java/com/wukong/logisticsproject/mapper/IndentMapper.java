package com.wukong.logisticsproject.mapper;

import com.wukong.logisticsproject.model.Indent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Repository
public interface  IndentMapper extends BaseMapper<Indent> {
    //查询所有


    List<Indent> findIndentAllByCurrentBranch(String currentBranch);

    //查询未分配的订单
    List<Indent> findIndentAny(String currentBranch);

    List<Indent> findByIndentId(@Param("indentId") Integer indentId,@Param("currentBranch") String currentBranch);

    //通过订单编号查询数据
    List<Indent> findAllByIndentId(@Param("indentId") Integer indentId,@Param("currentBranch") String currentBranch,
                                   @Param("sender") String sender,@Param("senderPhone") String senderPhone);

    //修改订单状态
    Integer updateStateById(Long indentId);




}
