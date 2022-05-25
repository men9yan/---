package com.wukong.logisticsproject.service.impl;

import com.wukong.logisticsproject.model.Region;
import com.wukong.logisticsproject.mapper.RegionMapper;
import com.wukong.logisticsproject.service.IRegionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地区表 服务实现类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements IRegionService {

}
