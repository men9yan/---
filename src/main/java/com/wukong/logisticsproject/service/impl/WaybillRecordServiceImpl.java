package com.wukong.logisticsproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wukong.logisticsproject.ex.InsertException;
import com.wukong.logisticsproject.mapper.VehicleMapper;
import com.wukong.logisticsproject.mapper.WaybillMapper;
import com.wukong.logisticsproject.model.*;
import com.wukong.logisticsproject.mapper.WaybillRecordMapper;
import com.wukong.logisticsproject.service.IWaybillRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wukong.logisticsproject.utils.JqgridUtil;
import com.wukong.logisticsproject.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 运单记录表 服务实现类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Service
public class WaybillRecordServiceImpl extends ServiceImpl<WaybillRecordMapper, WaybillRecord> implements IWaybillRecordService {

    @Autowired
    private WaybillRecordMapper waybillRecordMapper;

    @Autowired
    private WaybillMapper waybillMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private JqgridUtil jqgridUtil;

    /**
     * 获取运单签收所有信息
     *
     * @return
     */
    @Override
    public List<Waybill> getWaybillReceipt(HttpServletRequest request, String waybillState) {
        QueryWrapper<Waybill> waybillQueryWrapper = new QueryWrapper<>();
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        waybillQueryWrapper.eq("waybill_state", waybillState);
        if (staffInfo.getBranch().equals("总公司")) {
            return waybillMapper.selectList(waybillQueryWrapper);
        } else {

            waybillQueryWrapper.and(wrapper -> {
                wrapper.eq("operate_branch", staffInfo.getBranch());
            });
            return waybillMapper.selectList(waybillQueryWrapper);
        }
    }

    /*
    获取车辆是否装满
     */
    @Override
    public Map<String, Integer> getVehicleFullInfo(String vehicleNum) {
        int state;
        Map<String, Integer> dataMap = new HashMap<>();
        QueryWrapper<Vehicle> vehicleQueryWrapper = new QueryWrapper<>();
        vehicleQueryWrapper.eq("vehicle_num", vehicleNum);
        Vehicle vehicleInfo = vehicleMapper.selectOne(vehicleQueryWrapper);
        double load = Double.parseDouble(vehicleInfo.getVehicleLoad()) * 1000;//车辆载重
        double weight = Double.parseDouble(vehicleInfo.getVehicleWeight());//车辆货重
        if ((load / 3 * 2) <= weight) {
            state = 2001;
            dataMap.put("state", state);
        } else if ((load * 0.95) <= weight && weight <= load) {
            state = 2000;
            dataMap.put("state", state);
        } else {
            state = 4012;
            dataMap.put("state", state);
        }
        return dataMap;
    }

    /**
     * 查询不重复的运单记录
     * @param page
     * @param count
     * @return
     */
    @Override
    public String getFindWaybillRecordJson(Integer page, Integer count) {
        List<WaybillRecord> waybillList = waybillRecordMapper.findWaybillRecord();
        int toIndex = count * page;
        if (waybillList.size() < toIndex) {
            toIndex = waybillList.size();
        }
        List<WaybillRecord> list = waybillList.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", waybillList.size(), count);
    }

    /**
     * 查询对应运单的所有记录
     * @param waybillId
     * @return
     */
    @Override
    public List<WaybillRecord> getFindWaybillRecordById(String waybillId) {
        return waybillRecordMapper.findWaybillRecordById(waybillId);
    }

    /**
     *
     * 搜索
     * @param page
     * @param count
     * @param waybillRecord
     * @return
     */
    @Override
    public String findWaybillRecordByParamJson(Integer page, Integer count,WaybillRecord waybillRecord) {
        List<WaybillRecord> waybillList = waybillRecordMapper.findWaybillRecordByParam(waybillRecord);
        int toIndex = count * page;
        if (waybillList.size() < toIndex) {
            toIndex = waybillList.size();
        }
        List<WaybillRecord> list = waybillList.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", waybillList.size(), count);
    }

    @Override
    public Integer addWayBillRecordInfo(WaybillRecord waybillRecord) {
        QueryWrapper<Waybill> waybillQueryWrapper = new QueryWrapper<>();
        waybillQueryWrapper.eq("waybill_id", waybillRecord.getWaybillId());
        Waybill waybillInfo = waybillMapper.selectOne(waybillQueryWrapper);
        System.out.println(waybillInfo.toString());
        if (!waybillInfo.getOperateBranch().equals(waybillRecord.getOperateBranch())) {
            throw new InsertException("运单不在当前网点！！");
        }
        int rows = waybillRecordMapper.insert(waybillRecord);
        if (rows != 1) {
            throw new InsertException("插入数据失败！！");
        }
        return rows;

    }

    /**
     * 运单信息转JSON
     *
     * @param page
     * @param count
     * @return
     */
    @Override
    public String getWaybillReceiptInfoJson(Integer page, Integer count, HttpServletRequest request, String waybillState) {
        List<Waybill> waybillList = getWaybillReceipt(request, waybillState);
        int toIndex = count * page;
        if (waybillList.size() < toIndex) {
            toIndex = waybillList.size();
        }
        List<Waybill> list = waybillList.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", waybillList.size(), count);
    }





}
