package com.wukong.logisticsproject.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 响应到客户端的数据的封装类
 */
@Data
@Accessors(chain = true)
public class R<T> {
    /**
     * 响应状态码
     */
    private Integer state;
    /**
     * 出错时提示的信息
     */
    private String message;
    /**
     * 成功时响应给客户端的数据
     */
    private T data;


    /**
     * 操作成功
     *
     * @return 状态码已经标记为“成功”的对象
     */
    public static R ok() {
        return new R().setState(State.SUCCESS);
    }

    /**
     * 特殊情况下操作成功
     *
     * @return 状态码已经标记为“成功”的对象
     */
    public static R Specialok(Integer SpecialState, String message) {
        return new R().setState(SpecialState).setMessage(message);
    }


    /**
     * 操作成功，并响应数据
     *
     * @param data 响应到客户端的数据
     * @param <T>  响应到客户端的数据的类型
     * @return 表示操作成功且封装了响应数据的对象
     */
    public static <T> R ok(T data) {
        return R.ok().setData(data);
    }


    /**
     * 操作失败
     *
     * @param failureState 操作失败的状态码
     * @param e            操作失败时抛出并捕获的异常对象
     * @return 已经封装了操作失败的状态码，错误描述信息的对象
     */
    public static R failure(Integer failureState, Throwable e) {
        return new R().setState(failureState).setMessage(e.getMessage());
    }


    /**
     * 状态码
     */
    public static interface State {
        /**
         * 成功
         */
        int SUCCESS = 2000;

        /**
         * 插入数据失败
         */
        int ERR_INSERT_FAIL = 4000;
        /**
         * 删除数据失败
         */
        int ERR_DELETE_FAIL = 4001;
        /**
         * 修改数据失败
         */
        int ERR_UPDATE_FAIL = 4002;
        /**
         * 请求参数验证失败
         */
        int ERR_PARAMETER_VALIDATION = 4003;

        /**
         * 修改密码失败
         */
        int ERR_RESETPASSWORD_FAIL = 4005;

        /**
         *
         */
        int ERR_VALIDATIONCODE_VALIDATION = 4004;

        /**
         *
         */
        int ERR_LOADINGCAR_VALIDATION = 4006;
        /**
         * 上传文件为空
         */
        int ERR_UPLOAD_FILE_EMPTY = 4007;
        /**
         * 上传文件大小超出了限制
         */
        int ERR_UPLOAD_FILE_SIZE = 4008;
        /**
         * 上传文件类型超出了限制
         */
        int ERR_UPLOAD_FILE_TYPE = 4009;
        /**
         * 上传文件时出现读写错误
         */
        int ERR_UPLOAD_IO = 4010;
        /**
         * 上传文件状态错误，可能源文件已经不存在
         */
        int ERR_UPLOAD_FILE_STATE = 4011;


        /**
         * 未知错误
         */
        int ERR_UNKNOWN = 9000;

        /**
         * 名称已存在
         */
        int ERR_NAMEEXIST_FAIL = 4006;
    }


}
