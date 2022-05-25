package com.wukong.logisticsproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wukong.logisticsproject.model.CnRegionInfo;

import java.util.List;

public interface ICnRegionInfoService extends IService<CnRegionInfo> {

    List<CnRegionInfo> getfindProvincialAll();

    List<CnRegionInfo> getfindCityByProvincial(String criCode);

    List<CnRegionInfo> getfindDistrictByCity(String criCode);

    List<CnRegionInfo> getfindTownByDistrict(String criCode);
}
