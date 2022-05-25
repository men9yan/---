package com.wukong.logisticsproject.controller;

import com.wukong.logisticsproject.model.Staff;
import com.wukong.logisticsproject.model.User;
import com.wukong.logisticsproject.service.IStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class PageController {
    @Autowired
    private IStaffService staffService;

    /**
     * 登录页面
     *
     * @return
     */
    @RequestMapping(value = "index")
    public String toLogin() {

        return "login";
    }

    @GetMapping(value = "/welcome")
    public String welcome() {
        System.err.println("进来了！！！！！！！！！！！！");

        return "welcome";
    }

    @RequestMapping(value = "/setPassword", method = {RequestMethod.GET})
    public String setPassword(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        request.setAttribute("loginUser", loginUser);
        return "changepassword";
    }
    @RequestMapping(value = "/registered", method = {RequestMethod.GET})
    public String registered(HttpServletRequest request) {
        System.err.println("注册用户！！！！！！！！！！！！");

        return "registered";
    }


    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    public String test(HttpServletRequest request) {
        return "Test";
    }

    @RequestMapping(value = "/userPage", method = {RequestMethod.GET})
    public String userPage() {
        System.err.println("进来了！！！！！！！！！！！！");

        return "user";
    }

    @RequestMapping(value = "/rolePage", method = {RequestMethod.GET})
    public String rolePage() {
        System.err.println("进来了！！！！！！！！！！！！");

        return "role";
    }

    @RequestMapping(value = "/branchAudit", method = {RequestMethod.GET})
    public String branchAudit() {
        System.err.println("进来了！！！！！！！！！！！！");

        return "branch/branchAudit";
    }

    @RequestMapping(value = "/branchPage", method = {RequestMethod.GET})
    public String branchPage() {
        System.err.println("进来了！！！！！！！！！！！！");

        return "branch/branch";
    }

    @RequestMapping(value = "/branchHead", method = {RequestMethod.GET})
    public String branchHead() {
        System.err.println("进来了！！！！！！！！！！！！");

        return "branch/branchHead";
    }

    @RequestMapping(value = "/waybillPage", method = {RequestMethod.GET})
    public String waybillPage(HttpServletRequest request) {
        System.err.println("进来了！！！！！！！！！！！！");
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        System.out.println(loginUser);
        return "waybill/waybill";
    }

    @GetMapping(value = "/receiptPage")
    public String receiptPage(HttpServletRequest request) {
        System.err.println("进来了！！！！！！！！！！！！");
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        request.getSession().setAttribute("date", sdf.format(date));
        if (loginUser.getUsername().equals("admin")) {
            Staff AdminInfo = new Staff();
            AdminInfo.setBranch("总公司");
            AdminInfo.setStaffName("admin");
            AdminInfo.setPhone(loginUser.getPhone());
            request.setAttribute("staffInfo", AdminInfo);
        } else {
            String staffId = loginUser.getUsername().substring(1);
            Staff staffInfo = staffService.getReceiptBaseInfo(staffId);
            request.setAttribute("staffInfo", staffInfo);
        }
        return "transport/receipt";
    }

    @RequestMapping(value = "/indentSearch", method = {RequestMethod.GET})
    public String indentPage() {
        System.err.println("进来了！！！！！！！！！！！！");

        return "indent/indentSearch";
    }


    @RequestMapping(value = "/indentAllocation", method = {RequestMethod.GET})
    public String indentAllocation() {
        System.err.println("进来了！！！！！！！！！！！！");

        return "indent/indentAllocation";
    }


    @RequestMapping(value = "/signWaybillSearch", method = {RequestMethod.GET})
    public String signWaybillSearch() {
        System.err.println("进来了！！！！！！！！！！！！");

        return "signWaybill/signWaybillSearch";
    }

    @RequestMapping(value = "/signWaybillEntry", method = {RequestMethod.GET})
    public String signWaybillEntry() {
        System.err.println("进来了！！！！！！！！！！！！");

        return "signWaybill/signWaybillEntry";
    }

    @RequestMapping(value = "/sendScanPage", method = {RequestMethod.GET})
    public String sendScanPage(HttpServletRequest request) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        request.getSession().setAttribute("date", sdf.format(date));

        System.err.println("进来了！！！！！！！！！！！！");

        return "transport/sendScan";
    }

    @RequestMapping(value = "/loadingScanPage", method = {RequestMethod.GET})
    public String loadingScanPage(HttpServletRequest request) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        request.getSession().setAttribute("date", sdf.format(date));
        System.err.println("进来了！！！！！！！！！！！！");

        return "transport/loadingScan";
    }

    @RequestMapping(value = "/vehicleSearchPage", method = {RequestMethod.GET})
    public String staffSearchPage() {
        System.err.println("进来了！！！！！！！！！！！！");
        return "vehicle/vehicleSearch";
    }

    @RequestMapping(value = "/vehicle", method = {RequestMethod.GET})
    public String vehicle() {
        System.err.println("进来了！！！！！！！！！！！！");
        return "vehicle/vehicle";
    }


    @RequestMapping(value = "/departPage", method = {RequestMethod.GET})
    public String departPage(HttpServletRequest request) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        request.getSession().setAttribute("date", sdf.format(date));
        System.err.println("进来了！！！！！！！！！！！！");
        return "transport/depart";
    }

    @RequestMapping(value = "/unLoadingScanPage", method = {RequestMethod.GET})
    public String unLoadingScanPage(HttpServletRequest request) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        request.getSession().setAttribute("date", sdf.format(date));
        System.err.println("进来了！！！！！！！！！！！！");
        return "transport/unLoadingScan";
    }


    @RequestMapping(value = "/deliveryScanPage", method = {RequestMethod.GET})
    public String deliveryScanPage() {
        System.err.println("进来了！！！！！！！！！！！！");
        return "transport/deliveryScan";




    }


    @RequestMapping(value = "/branchSearchPage", method = {RequestMethod.GET})
    public String branchSearchPage() {
        System.err.println("进来了！！！！！！！！！！！！");
        return "branch/branchSearch";
    }

    @RequestMapping(value = "/arrivePieceScanPage", method = {RequestMethod.GET})
    public String arrivePieceScanPage(HttpServletRequest request) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        request.getSession().setAttribute("date", sdf.format(date));
        System.err.println("进来了！！！！！！！！！！！！");
        return "transport/arrivePieceScan";
    }

    @RequestMapping(value = "/arriveCarScanPage", method = {RequestMethod.GET})
    public String arriveCarScanPage(HttpServletRequest request) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        request.getSession().setAttribute("date", sdf.format(date));
        System.err.println("进来了！！！！！！！！！！！！");
        return "transport/arriveCarScan";
    }

    @RequestMapping(value = "/waybillTrackingPage", method = {RequestMethod.GET})
    public String waybillTrackingPage() {
        System.err.println("进来了！！！！！！！！！！！！");
        return "services/waybillTracking";
    }


    @RequestMapping(value = "/problemTypePage", method = {RequestMethod.GET})
    public String problemTypePage() {
        System.err.println("进来了！！！！！！！！！！！！");
        return "problemType";
    }

    @RequestMapping(value = "/problemHandingPage", method = {RequestMethod.GET})
    public String problemHandingPage() {
        System.err.println("进来了！！！！！！！！！！！！");
        return "services/problemHanding";
    }

    @RequestMapping(value = "/depository", method = {RequestMethod.GET})
    public String depositoryPage() {
        System.err.println("进来了！！！！！！！！！！！！");
        return "depository/depository";
    }


}
