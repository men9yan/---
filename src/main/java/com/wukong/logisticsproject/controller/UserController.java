package com.wukong.logisticsproject.controller;


import com.wukong.logisticsproject.ex.*;
import com.wukong.logisticsproject.mapper.UserMapper;
import com.wukong.logisticsproject.model.Staff;
import com.wukong.logisticsproject.model.User;
import com.wukong.logisticsproject.model.VerifySeq;
import com.wukong.logisticsproject.service.IStaffService;
import com.wukong.logisticsproject.service.IUserService;
import com.wukong.logisticsproject.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Controller
@Slf4j
public class UserController {



    //设置保存路径
    @Value("${project.upload.base-dir}")
    private String uploadBaseDir;
    //设置文件大小
    @Value("${project.upload.question-image.max-size}")
    private long imageMaxSize;
    //设置文件的类型
    @Value("${project.upload.question-image.types}")
    private List<String> imageTypes;

    @Autowired
    IUserService service;


    @ResponseBody
    @RequestMapping(value = "/getUserAllList", method = {RequestMethod.POST})
    public String list(HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return service.getAllUserJson(Integer.parseInt(page), Integer.parseInt(rows));
    }

    @ResponseBody
    @RequestMapping(value = "/getUserAllListByBranch", method = {RequestMethod.POST})
    public String getUserAllListByBranch(HttpServletRequest request) {
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return service.getAllUserByBranchJson(Integer.parseInt(page), Integer.parseInt(rows),staffInfo);
    }


    /**
     * 修改密码
     *
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/rsetpassword")
    public R setPassword(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        String newPassword = request.getParameter("password");
        String oldPassword = request.getParameter("oldpassword");
        Integer userId = loginUser.getUserId();
        return R.ok(service.updatePassword(userId, oldPassword, newPassword));
    }

    /**
     * 重置密码
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/resetPassword")
    public R resetPassword(Integer userId) {

        return R.ok(service.resetPassword(userId));
    }


    //搜索数据
    @ResponseBody
    @RequestMapping(value = "/findUserByParam", method = {RequestMethod.POST})
    public String findUserByParam(HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        String userId = request.getParameter("userId");
        String username = request.getParameter("username");
        String trueName = request.getParameter("trueName");
        Integer userId_int;
        if (!userId.isEmpty()) {
            userId_int = Integer.parseInt(userId);
        } else {
            userId_int = null;
        }
        System.err.println("搜索数据：" + page + "," + rows + "," + userId + "," + username + "," + trueName);
        return service.getUserJsonByParam(Integer.parseInt(page), Integer.parseInt(rows), userId_int, username, trueName);

    }

    //删除数据
    //删除数据
    @ResponseBody
    @RequestMapping(value = "/deleteUser", method = {RequestMethod.POST})
    public R<User> deleteUser(Integer userId,String username) {
        System.err.println("username:" + username);
        return R.ok(service.deleteUserByid(userId,username));
    }


    //修改用户信息
    @ResponseBody
    @RequestMapping(value = "/updateUserById", method = {RequestMethod.POST})
    public R updateUserById(@Validated(VerifySeq.class) User user, BindingResult bindingResult,HttpServletRequest request) {
        System.err.println("修改用户接收的数据-->" + user);
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            log.debug("验证参数格式失败！{}", errorMessage);
            throw new ParameterValidationException(errorMessage);
        }
        return R.ok(service.getUpdateUserById(user,request));
    }

    /**
     * 设置上传图片的位置
     * @param imageFile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upload-image", method = {RequestMethod.POST})
    public R<String> uploadImage(MultipartFile imageFile){

        System.err.println("!!!!imageFile:"+imageFile);
        //调用参数imageFile.isEmpty（）判断上传的文件是否为空

        if(imageFile==null){
            //是 抛出异常FileEmptyException
            throw new FileEmptyException("上传图片失败！请选择有效的图片文件！");
        }

        //调用参数imageFile.getSize() 判断上传的文件大小
        //判断文件大小是够超出了imageMaxSize值的限制
        long size=imageFile.getSize();
        if(size>imageMaxSize){
            //是 抛出异常FileSizeException
            throw new FIleSizeException("上传图片失败！！不允许上传超过"+imageMaxSize/1024+"KB的图片");
        }


        //调用参数imageFile.getContentType() 判断上传的文件类型
        //判断文件类型是否不被imageTypes（集合）包含（调用contains（）方法）
        if(!imageTypes.contains(imageFile.getContentType())){
            //是， 抛出异常FileTYypeException
            throw new FileTypeException("上传文件失败！仅支持以下格式的图片"+imageTypes);
        }



        //--------------------------------------------------
        //  开始创建文件的保存路径
        // -------------------------------------------------

        //保存上传文件的文件夹：D:/IntelliJ IDEA/straw-upload/2020/08
        //通过方法
        //DataTimeFormatter.ofPattern("yyyy/MM").format(LocalDate.now())得到 2020/08
        String subDir = DateTimeFormatter.ofPattern("yyyy/MM").format(LocalDate.now());

        //创建上传文件夹的FIle对象：File parent = newFile(uploadBaseDir,以上得到的2020/80);
        File parent = new File(uploadBaseDir,subDir);
        //调用parent.exists()方法判断文件夹是否不存在
        if(!parent.exists()){
            //是， 调用parent.mkdirs()创建缺少的文件夹
            parent.mkdirs();
        }

        //文件名filename:自定义策略，保证文件名不会冲突
        String filename= System.currentTimeMillis()+"-"+System.nanoTime();

        //--------------------------------------------------
        //  开始提取上传文件的全名
        // -------------------------------------------------

        //调用参数imageFile.getOriginalFilename() 获取原始文件名
        String originalFilename = imageFile.getOriginalFilename();
        //基于原始文件名进行截取，得到扩展名suffix
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //基于filename和suffix得到文件全名child
        String child =filename+suffix;

        //基于parent和child创建文件对象，表示将上传的文件保存在哪里
        try{
            // -- 调用imageFile.transferTo()方法执行保存
            imageFile.transferTo(new File(parent,child));
        } catch(IOException e){
            //  -- 抛出FIleIOException
            throw  new FileIOException("上传图片失败！上传时出现读写错误，请稍后再次尝试！");
        }catch(IllegalStateException e){
            //  -- 抛出fileStateException
            throw new FileStateException("上传图片失败！原图片状态异常，请检查原图片文件是否存在");
        }


        //基于 2020/08 和文件全名child得到上传的文件的路径：、2020/08/xxx.jpg
        String imageUrl = "/"+subDir+"/"+child;
        //返回文件路径
        return R.ok(imageUrl);
//        http://localhost:8081/2020/11/1605597905097-503535982223000.jpg
//        http://localhost:8081/img/boy.jpg
    }

    /**
     * 修改头像
     * @param request
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateImgById", method = {RequestMethod.POST})
    public R updateImgById(HttpServletRequest request,User user) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        user.setUserId(loginUser.getUserId());
        return R.ok(service.getUpdateUserById(user,request));
    }

}
