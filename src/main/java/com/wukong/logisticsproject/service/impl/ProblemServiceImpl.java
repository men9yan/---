package com.wukong.logisticsproject.service.impl;

import com.wukong.logisticsproject.ex.InsertException;
import com.wukong.logisticsproject.ex.UpdateException;
import com.wukong.logisticsproject.mapper.*;
import com.wukong.logisticsproject.model.*;
import com.wukong.logisticsproject.model.Waybill;
import com.wukong.logisticsproject.service.IProblemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wukong.logisticsproject.utils.JqgridUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * 问题件表 服务实现类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements IProblemService {

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private WaybillMapper waybillMapper;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private ProblemTypeMapper problemTypeMapper;

    @Autowired
    private WaybillRecordMapper waybillRecordMapper ;

    @Autowired
    private JqgridUtil jqgridUtil;

    @Override
    public Integer getAddProblem(Problem problem) {



        //添加电话和登记时间
        Branch branch=branchMapper.findBranchByName(problem.getRegisterBranch());
        String Phone=branch.getBranchPhone();

        problem.setPhone(Phone).setRegisterTime(LocalDateTime.now());

        System.out.println("数据为---："+problem);
        Integer row=problemMapper.addProblem(problem);

        //修改运单状态
        waybillMapper.updateWaybillById(new Waybill().setWaybillId(problem.getWaybillId()).setWaybillState("10"));

        //插入运单记录
        WaybillRecord waybillRecord=new WaybillRecord()
                .setWaybillId(problem.getWaybillId())
                .setOperateBranch(problem.getRegisterBranch())
                .setOperator(problem.getRegistrant())
                .setWaybillState("10")
                .setOperateTime(problem.getRegisterTime())
                .setPhone(problem.getPhone());
        Integer WaybillRow =waybillRecordMapper.insertWaybillRecord(waybillRecord);


        if (row!=1){
            throw new InsertException("上传问题件失败！！");
        }

        return row;
    }

    /**
     * 查询问题件的所有信息
     * @param page
     * @param count
     * @return
     */
    @Override
    public String getAllProblemJson(Integer page, Integer count) {
        List<Problem> problem = problemMapper.findAll();
        int toIndex = count * page;
        if (problem.size() < toIndex) {
            toIndex = problem.size();
        }
        List<Problem> list = problem.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", problem.size(), count);
    }

    /**
     * 处理问题件
     * @param problem
     * @param staffInfo
     * @return
     */
    @Override
    public Integer updateProblem(Problem problem, Staff staffInfo) {
        problem.setHandler(staffInfo.getStaffName()).setHandleBranch(staffInfo.getBranch()).setHandleTime(LocalDateTime.now());



        Integer row=problemMapper.updateProblem(problem);
        if(row!=1){
            throw new UpdateException("处理问题件失败！！");
        }
        //修改运单状态为11
        Integer waybillRow=waybillMapper.updateWaybillById(new Waybill().setWaybillState("11").setWaybillId(problem.getWaybillId()));

        return waybillRow;
    }


    @Override
    public String getAllProblemByIdJson(Integer page, Integer count, String waybillId) {
        List<Problem> problem = problemMapper.findAllById(waybillId);
        int toIndex = count * page;
        if (problem.size() < toIndex) {
            toIndex = problem.size();
        }
        List<Problem> list = problem.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", problem.size(), count);
    }

    @Override
    public String getAllProblemTypeJson(Integer page, Integer count) {
        List<ProblemType> problemType = problemTypeMapper.findProblemTypeAll();
        int toIndex = count * page;
        if (problemType.size() < toIndex) {
            toIndex = problemType.size();
        }
        List<ProblemType> list = problemType.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", problemType.size(), count);
    }

    @Override
    public List<ProblemType> getFindAll() {
        List<ProblemType> problemType = problemTypeMapper.findProblemTypeAll();
        return problemType;
    }

    /**
     * 问题件类型删除
     * @param ptId
     * @return
     */
    @Override
    public Integer getDeleteById(Integer ptId) {
        Integer row=problemTypeMapper.deleteById(ptId);
        if (row!=1){
            throw new UpdateException("删除失败！！！");
        }
        return row;
    }

    /**
     * 问题件类型修改
     * @param problemType
     * @return
     */
    @Override
    public Integer getUpdateProblemTypeById(ProblemType problemType) {
        Integer rows=problemTypeMapper.updateProblemTypeById(problemType);
        if (rows!=1){
            throw new UpdateException("修改失败！！！");
        }
        return rows;
    }

    /**
     * 问题件类型添加
     * @param problemType
     * @return
     */
    @Override
    public Integer getAddProblemType(ProblemType problemType) {
        problemType.setPtTime(LocalDateTime.now());
        Integer rows=problemTypeMapper.addProblemType(problemType);
        if (rows!=1){
            throw new InsertException("添加失败！！");
        }
        return rows;
    }


}
