package com.wukong.logisticsproject.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wukong.logisticsproject.mapper.CnRegionInfoMapper;
import com.wukong.logisticsproject.model.CnRegionInfo;
import com.wukong.logisticsproject.service.ICnRegionInfoService;
import com.wukong.logisticsproject.utils.JqgridUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Service
public class CnRegionInfoServiceImpl extends ServiceImpl<CnRegionInfoMapper, CnRegionInfo> implements ICnRegionInfoService {

    @Autowired
    private JqgridUtil jqgridUtil;

    @Autowired
    private CnRegionInfoMapper cnRegionInfoMapper;


    //查询所有省级
    @Override
    public List<CnRegionInfo> getfindProvincialAll() {
        return cnRegionInfoMapper.findProvincialAll();
    }

    @Override
    public List<CnRegionInfo> getfindCityByProvincial(String criCode) {

        return cnRegionInfoMapper.findCityByProvincial(criCode);
    }

    @Override
    public List<CnRegionInfo> getfindDistrictByCity(String criCode) {
        return cnRegionInfoMapper.findDistrictByCity(criCode);
    }

    @Override
    public List<CnRegionInfo> getfindTownByDistrict(String criCode) {
        return cnRegionInfoMapper.getfindTownByDistrict(criCode);
    }
}
