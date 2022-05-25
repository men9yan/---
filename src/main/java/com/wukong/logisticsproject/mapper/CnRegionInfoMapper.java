package com.wukong.logisticsproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wukong.logisticsproject.model.CnRegionInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CnRegionInfoMapper extends BaseMapper<CnRegionInfo> {
    List<CnRegionInfo> findProvincialAll();

    List<CnRegionInfo> findCityByProvincial(String criCode);

    List<CnRegionInfo> findDistrictByCity(String criCode);

    List<CnRegionInfo> getfindTownByDistrict(String criCode);
}
