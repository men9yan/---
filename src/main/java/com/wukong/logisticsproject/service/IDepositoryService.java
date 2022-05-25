package com.wukong.logisticsproject.service;

import com.wukong.logisticsproject.model.Depository;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wukong.logisticsproject.model.User;

/**
 * <p>
 * 仓库表 服务类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
public interface IDepositoryService extends IService<Depository> {

    String getDepository(Integer page, Integer count, User loginUser);

    Integer addData(Depository depository);
}
