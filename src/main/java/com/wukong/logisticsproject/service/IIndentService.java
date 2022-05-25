package com.wukong.logisticsproject.service;

import com.wukong.logisticsproject.model.Indent;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wukong.logisticsproject.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
public interface IIndentService extends IService<Indent> {
    List<Indent> getFindIndentAll(String currentBranch);


    public String getIndentAllJson(@Param("page") Integer page,
                                 @Param("count") Integer count,
                                   @Param("loginUser")  User loginUser);



    List<Indent> getFindByIndentId(Integer indentId,String currentBranch);

    public String getFindByIndentIdJson(@Param("page") Integer page,
                                            @Param("count") Integer count,
                                            @Param("indentId") Integer indentId,
                                            @Param("loginUser")  User loginUser
                                            );



    List<Indent> getFindIndentAny(String currentBranch);

    public String getIndentAnyJson(@Param("page") Integer page,
                                   @Param("count") Integer count,
                                   @Param("loginUser")  User loginUser );



    List<Indent> getFindAllByIndentId(Integer indentId,String currentBranch,String sender,String senderPhone);

    public String getFindAllByIndentIdJson(@Param("page") Integer page,
                                            @Param("count") Integer count,
                                            @Param("indentId") Integer indentId,
                                           @Param("loginUser")  User loginUser,
                                           @Param("sender")  String sender,
                                           @Param("senderPhone")  String senderPhone
    );

    Integer insertIndent(User user,Indent indent);


}
