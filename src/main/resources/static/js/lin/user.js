$(function () {  //页面加载完后执行
    $("#loading").show();  //如果被选元素已被隐藏，则显示这些元素：（显示图片）


    $("#table_list_2").trigger("reloadGrid");

    //分配角色
    $("#updateRoleSubmit").click(function () {
        modify();
    });

    //修改信息的方法
    $("#updateUserSubmit").click(function () {
        updateUserModify();
    });



    buildgrid();
    $.jgrid.defaults.styleUI = 'Bootstrap';  //制定使用bootstrap的版本的样式

    $("#loading").hide();


});


//--------------------------------------------------------------------------------------------------------------------------------------------------


//制作表格
function buildgrid() {
    $("#table_list_2").jqGrid(
        {
            url: "/getUserAllListByBranch",//获取数据的地址
            datatype: "json",//返回的数据类型
            mtype: "POST", //提交方式
            shrinkToFit:false,
            autoScroll: true,
            height: "100%",
            autowidth: true, //如果为ture时，则当表格在首次被创建时会根据父元素比例重新调整
            // 表格宽度。
            rownumbers: true,//如果为ture则会在表格左边新增一列，显示行顺序号，从1开始递增。
            jsonReader: {  //	描述json 数据格式的数组
                root: "dataList",
                page: "currentPage",
                total: "totalPage",          //   很重要 定义了 后台分页参数的名字。
                records: "totalCount"
            },

            rowNum: 10,  //在grid上显示记录条数，这个参数是要被传递到后台
            rowList: [10, 20, 30], //	一个下拉选择框，用来改变显示记录数
            colNames: ['用户编号', '用户名', '电话', '真实姓名','角色','操作'],//显示字段名
            colModel: [
                //这个参数指定了jqGrid各列的具体格式
                {
                    name: 'userId',  //指定对应数据中属性名
                    index: 'userId',  //用于列排序
                    width: 250,

                },
                {
                    name: 'username',
                    index: 'username',
                    width: 250,

                },


                {
                    name: 'phone',
                    index: 'phone',
                    width: 250,
                },

                {
                    name: 'trueName',
                    index: 'trueName',
                    width: 250,
                },
                {
                    name: 'roleName',
                    index: 'roleName',
                    width: 250,
                },

                {
                    name: 'handle',
                    index: 'handle',
                    width: 250,
                }


            ],
            pager: "#pager_list_2",  //这个参数指定了jqGrid页脚显示位置。
            viewrecords: true,  //这个参数设置了是否显示所有记录的总数。
            caption: "用户列表",  //这个参数制订了jqGrid的标题
            add: false,   //开启添加功能
            edit: false,
            hidegrid: false, //启用或者禁用控制表格显示、隐藏的按钮
            gridComplete: function () {
                console.log("grid Complete");
                var ids = $("#table_list_2").jqGrid("getDataIDs");
                console.log("ids--->{}", ids)
                for (var int = 0; int < ids.length; int++) {
                    var id = ids[int];
                    var userId = parseInt($("#table_list_2").jqGrid("getCell", id, "userId"));  //获取单元格中的数据
                    console.log("userId--->{}", userId)
                    var modify = "<button type='button' class='layui-btn layui-btn-warm layui-btn-xs' data-toggle='modal' data-target='#myModal2'  onclick='updateUserShow("+id+","+userId+")'>编辑</button>";  //这里的onclick就是调用了上面的javascript函数 Modify(id)
                    var del = "<button  type='button' class='layui-btn layui-btn-danger layui-btn-xs' onclick='deldialog(" + id + ")' >删除</button>";
                    var upd ="<button type='button' class='layui-btn layui-btn-xs' data-toggle='modal' data-target='#myModal' onclick='showRoleByUser("+userId+")'>分配角色</button>";
                    var resetPwd ="<button type='button' class='layui-btn layui-btn-xs'   onclick='resetPassword("+userId+")'>重置密码</button>";
                    var result = $("#table_list_2").jqGrid("setRowData", id, {handle: upd+"&nbsp;"+del+"&nbsp;"+modify +"&nbsp;"+resetPwd});
                }
            }
        });

    // Setup buttons
    $("#table_list_2").jqGrid('navGrid', '#pager_list_2', {
        edit: false,
        add: false,
        del: false,
        search: false
    }, {
        height: 300,
        reloadAfterSubmit: true
    });


    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#table_list_2').setGridWidth(width);
    });

}

//重置密码
function resetPassword(userId){
    $.ajax({
        url: '/resetPassword',
        type: "POST",
        data: 'userId=' + userId,
        success: function (data) {
            console.log(data);
            if (data.state == 2000) {

                successalert("", "重置密码完成");
            } else {
                errorsalert("", data.message);
            }
        },
        error: function () {
            errorsalert("", "出现错误，请重试");
        }
    });
}

//--------------------------------------------------------------------------------------------------------------------------------------------------

//显示所有角色
layui.use(['form'], function(){
    var form = layui.form
        ,layer = layui.layer;
});

let rolesApp =new Vue({
    el:'#rolesApp',
    data:{
        roles:[
            {roleId:1,roleName:'admin'},
            {roleId:2,roleName:'test'},
            {roleId:3,roleName:'add'},
            {roleId:4,roleName:'delete'},
        ]
    },
    methods:{
        loadRoles:function(){
            $.ajax({
                url:'/showRole',
                type:'post',
                dataType:'json',
                success:function(data){
                    console.log("111111111")
                    console.log("data: "+data.data);
                    console.log("data: "+data);
                    rolesApp.roles=data.data;
                }
            });
        }
    },
    created:function () {
        this.loadRoles();

    }
});


//--------------------------------------------------------------------------------------------------------------------------------------------------
//根据userId显示角色
function showRoleByUser(userId){

    // console.log("用户id为:"+userId);
    $('#updateRoleForm')[0].reset();
    var rols = document.getElementsByName("rol");
    $.ajax({
        url:'/showRoleByUser',
        type:'post',
        data:"userId="+userId,
        success:function (data) {
            console.log(data);
            let list=data.data;
            // let roles=rolesApp.roles;
            for(var j=0;j<rols.length;j++){
                // console.log("roles:"+rols[j].roleId);
                if(list==null){
                    $("#updateUserId").val(userId);
                    return false;
                }
                if(rols[j].value == list.roleId){
                    $(rols[j]).next().click();
                    $("#updateUserId").val(userId);
                }

            }
        }
    });
}

//--------------------------------------------------------------------------------------------------------------------------------------------------
//删除信息的方法
//--------------------------------------------------------------------------------------------------------------------------------------------------

function deldialog(id) {
    var userId = parseInt($("#table_list_2").jqGrid("getCell", id, "userId"));  //获取单元格中的数据
    var username = $("#table_list_2").jqGrid("getCell", id, "username");  //获取单元格中的数据
    deletealert("", "确定要删除吗？", function () {
        console.log("StaffId-->{}", userId)
        del(userId,username)
    });
}


function del(userId,username) {
    console.log("!!:"+userId);
    let data= {
        'userId': userId,
        'username': username

    }
    console.log("!!:"+data);
    $.ajax({
        url: '/deleteUser',
        type: "POST",
        data: data,
        success: function (data) {
            console.log(data);
            if (data.state == 2000) {
                $("#table_list_2").trigger("reloadGrid");
                successalert("", "删除用户完成");
            } else {
                errorsalert("", data.message);
            }
        },
        error: function () {
            errorsalert("", "出现错误，请重试");
        }
    });


}


//--------------------------------------------------------------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------------------------------------------------------------
//更新用户角色信息
function modify() {


    console.log("???"+$("#updateRoleForm").serialize());

    $.ajax({
        url: "/updateRoleByUser",
        type: "POST",
        data: $("#updateRoleForm").serialize(),
        success: function (data) {
            console.log(data);
            if (data.state == 2000) {
                successalert("", "分配角色完成");
                $("#table_list_2").trigger("reloadGrid");
                $('#myModal').modal('hide');
            } else {
                errorsalert("", data.message);
                $('#myModal').modal('hide');
            }

        },
        error: function () {
            errorsalert("", "出现错误，请重试");
        }
    });
}


//--------------------------------------------------------------------------------------------------------------------------------------------------




//--------------------------------------------------------------------------------------------------------------------------------------------------


//创建Vue对象
let app = new Vue({
    el:'#app',
    data:{
        userId:null,
        username:null,
        trueName:null
    },
    methods:{
        selectMethod : function(){
            console.log(isNaN(parseInt(app.userId)));
            if(isNaN(parseInt(app.userId))&&app.userId!=null&&app.userId!=""){
                swal("错误", "用户编号只能为数字!", "error");
                return false;
            }
            let data = {
                'userId':app.userId,
                'username':app.username,
                'trueName':app.trueName
            };
            console.log(data);
            $("#table_list_2").jqGrid('clearGridData');
            $("#table_list_2").setGridParam(
                {
                    url:"/findUserByParam",
                    postData:data
                }
            ).trigger("reloadGrid");


        },


    }
})
//----------------------------------------------------------------------------------------------------------------------------

function updateUserShow(id,userId) {  // 修改信息的方法
    $("#table_list_2").trigger("reloadGrid");
    console.log("updateUserShow" + userId);
    var datas = $("#table_list_2").jqGrid("getRowData", id);//获取单行数据
    console.log("?????->:{}", datas);
    $("#userId").val(userId);
    $("#phone").val(datas.phone);
    $("#trueName").val(datas.trueName);

    $('#myModal2').modal('hide');
}

//-------------------------------------------------------------------------------------------------------

//修改用户信息

function updateUserModify() {

    console.log($("#updateUserForm").serialize());
    console.log("你在哪里？？？？？？")

    $.ajax({
        url: "/updateUserById",
        type: "POST",
        data: $("#updateUserForm").serialize(),
        success: function (data) {
            console.log(data);
            if (data.state == 2000) {
                successalert("", "修改用户完成");
                $("#table_list_2").trigger("reloadGrid");
                $('#myModal2').modal('hide');
            } else {
                errorsalert("", data.message);
            }

        },
        error: function () {
            errorsalert("", "出现错误，请重试");
        }
    });
}





