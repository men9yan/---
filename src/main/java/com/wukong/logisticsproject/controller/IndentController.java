package com.wukong.logisticsproject.controller;



import com.wukong.logisticsproject.model.Indent;
import com.wukong.logisticsproject.model.User;
import com.wukong.logisticsproject.model.VerifySeq;
import com.wukong.logisticsproject.service.IIndentService;
import com.wukong.logisticsproject.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@RestController
@Slf4j
public class IndentController {

    @Autowired
    private IIndentService service;




    /**
     * 根据订单编号搜索所有未分配的订单数据
     * @param request
     * @return
     */

    @RequestMapping(value = "/getFindAllByIndentIdJson", method = {RequestMethod.POST})
    public String getFindAllByIndentIdJson(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        String indentId = request.getParameter("indentId"); //
        String sender = request.getParameter("sender"); //
        String senderPhone = request.getParameter("senderPhone"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        Integer indentId_int ;
        if(!indentId.isEmpty()){
            indentId_int =  Integer.parseInt(indentId);
        }else{
            indentId_int = null;
        };
        return service.getFindAllByIndentIdJson(Integer.parseInt(page), Integer.parseInt(rows),indentId_int,loginUser,sender,senderPhone);
    }



    /**
     * 显示所有未分配的订单数据
     * @param request
     * @return
     */
    @RequiresRoles(value = {"网点负责人","业务员","superadmin"},logical = Logical.OR)
    @RequestMapping(value = "/getIndentAnyJson", method = {RequestMethod.POST})
    public String getIndentAnyJson(HttpServletRequest request) {

        User loginUser = (User) request.getSession().getAttribute("loginUser");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return service.getIndentAnyJson(Integer.parseInt(page), Integer.parseInt(rows),loginUser);
    }

    /**
     * 根据运单编号搜索所有订单数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/getFindAllByWaybillIdJson", method = {RequestMethod.POST})
    public String getFindAllByWaybillIdJson(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        String indentId = request.getParameter("indentId");

        Integer indentId_int ;
        if(!indentId.isEmpty()){
            indentId_int =  Integer.parseInt(indentId);
        }else{
            indentId_int = null;
        };
        System.out.println("Page:" + page + ",rows:" + rows);
        return service.getFindByIndentIdJson(Integer.parseInt(page), Integer.parseInt(rows),indentId_int,loginUser);
    }

    /**
     * 显示所有订单数据
     * @param request
     * @return
     */
    @RequiresRoles(value = {"网点负责人","业务员","superadmin"},logical = Logical.OR)
    @RequestMapping(value = "/getAllIndentJson", method = {RequestMethod.POST})
    public String getAllIndentJson(HttpServletRequest request) {

        User loginUser = (User) request.getSession().getAttribute("loginUser");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return service.getIndentAllJson(Integer.parseInt(page), Integer.parseInt(rows),loginUser);
    }

    @RequestMapping(value = "/addIndent", method = {RequestMethod.POST})
    public R addIndent(@Validated(VerifySeq.class) Indent indent, HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        return R.ok(service.insertIndent(loginUser,indent));

    }


}
