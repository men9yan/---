$(function () {
    $("#updateRoleSubmit").click(function () {
        modify();
    });

    $("#addRole").click(function () {
        $('#addRoleName').val("");
    });




    //执行添加方法
    $("#addRoleSubmit").click(function () {
        addmodify();

    });


    $.jgrid.defaults.styleUI = 'Bootstrap';


    $("#loading").show();

    buildgrid();

    $("#loading").hide();

});

function deldialog(roleId) {
    deletealert("", "确定要删除此角色吗？", function () {
        del(roleId)
    });
}

function del(roleId) {
    console.log("!!:" + roleId);
    $.ajax({
        url: '/deleteRole',
        type: "POST",
        data: 'roleId=' + roleId,
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

//-----------------------------------------------------------------------------------------------------------------

//编辑角色信息
function modify() {
    console.log("???" + $("#updateRoleForm").serialize());
    $.ajax({
        url: "/updateRoleById",
        type: "POST",
        data: $("#updateRoleForm").serialize(),
        success: function (data) {
            console.log(data);
            if (data.state == 2000) {
                successalert("", "修改角色完成");
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

//分配角色框
function showtreedialog(id) {
    var roleId = parseInt($("#table_list_2").jqGrid("getCell", id, "roleId"));  //获取单元格中的数据
    roleidforterr = roleId;
    showtree();


}

//----------------------------------------------------------------------------------------------------------------------------

//编辑角色

function updateRoleShow(id, roleId) {  // 修改信息的方法
    console.log("updateRoleShow" + roleId);
    var datas = $("#table_list_2").jqGrid("getRowData", id);//获取单行数据
    console.log("?????->:{}", datas);
    $("#roleId").val(roleId);
    $("#roleName").val(datas.roleName);
    $("#createBranch").val(datas.createBranch);
}

//----------------------------------------------------------------------------------------------------------------------------

function buildgrid() {
    $("#table_list_2").jqGrid(
        {
            url: "/showrolelist",
            datatype: "json",
            mtype: "POST",
            height: "100%",
            autowidth: true,
//				shrinkToFit: true,
            rownumbers: true,
            jsonReader: {
                root: "dataList",
                page: "currentPage",
                total: "totalPage",          //   很重要 定义了 后台分页参数的名字。
                records: "totalCount"
            },
            rowNum: 10,
            rowList: [10, 20, 30],
            colNames: ['角色编号', '角色名称', '所属网点', '操作'],
            colModel: [
                {
                    name: 'roleId',
                    index: 'roleId',
                    editable: true,

                },
                {
                    name: 'roleName',
                    index: 'roleName',
                    editable: true,
                },
                {
                    name: 'createBranch',
                    index: 'createBranch',
                },
                {
                    name: 'handle',
                    index: 'handle',
                    sortable: false
                }
            ],
            pager: "#pager_list_2",
            viewrecords: true,
            caption: "角色列表",
            add: false,
            edit: false,
            // addtext: 'Add',
            // editurl: "/addrole",
            hidegrid: false,
            gridComplete: function () {
                console.log("grid Complete");
                var ids = $("#table_list_2").jqGrid("getDataIDs");
                console.log("ids--->{}", ids)
                for (var int = 0; int < ids.length; int++) {
                    var id = ids[int];
                    var roleId = parseInt($("#table_list_2").jqGrid("getCell", id, "roleId"));  //获取单元格中的数据
                    console.log("userId--->{}", roleId)
                    var modify = "<button type='button' class='layui-btn layui-btn-warm layui-btn-xs' data-toggle='modal' data-target='#myModal' onclick='updateRoleShow(" + id + "," + roleId + ")'>编辑</button>";  //这里的onclick就是调用了上面的javascript函数 Modify(id)
                    var del = "<button  type='button' class='layui-btn layui-btn-danger layui-btn-xs' onclick='deldialog(" + roleId + ")' >删除</button>";
                    var upd = "<button type='button' class='layui-btn layui-btn-xs' onclick='showtreedialog(" + id + ")'>分配权限</button>";
                    var result = $("#table_list_2").jqGrid("setRowData", id, {handle: upd + "&nbsp;" + del + "&nbsp;" + modify});
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

//---------------------------------------------------------------------------------------------------------
//添加信息的方法


function addmodify() {

    console.log("!!!!新的内容：" + $("#addRoleForm").serialize());

    $.ajax({
        url: "/addRole",
        type: "POST",
        data: $("#addRoleForm").serialize(),
        success: function (data) {
            console.log(data);
            if (data.state == 2000) {
                successalert("", "角色信息添加完成");
                $("#table_list_2").trigger("reloadGrid");
                $('#myModal2').modal('hide');
            } else {
                errorsalert("", data.message);
            }

        },
        error: function () {
            errorsalert("", "错误！！");
        }
    });
}


//--------------------------------------------------------------------------------------------------------------------------
//搜索数据

//创建Vue对象
Vue.component('v-select',VueSelect.VueSelect);
let app = new Vue({
    el: '#app',
    data: {
        roleId: null,
        roleName: null,

    },
    methods: {

        selectMethod: function () {
            console.log(app.roleId);
            if(isNaN(parseInt(app.roleId))&&app.roleId!=null&&app.roleId!=""){
                swal("错误", "用户编号只能为数字!", "error");
                return false;
            }
                let data = {
                'roleId': app.roleId,
                'roleName': app.roleName
            };
            console.log(data);
            $("#table_list_2").jqGrid('clearGridData');
            $("#table_list_2").setGridParam(
                {
                    url: "/findRoleByParam",
                    postData: data
                }
            ).trigger("reloadGrid");
            app.branch=null;

        },


    }
})


function getChecked() {
    var nodes = $('#tt').tree('getChecked');
    var s = '';
    for (var i = 0; i < nodes.length; i++) {
        if (s != '') s += ',';
        s += nodes[i].id;

    }
    return s;
}


function showtree() {
    $("#menutree").show("slow");
}

function hidetree() {
    $('#tt').tree('reload');
    $("#menutree").hide("slow");
}

function submittree() {
    if (getChecked() == "") {
        alert("未选择菜单！");
        return;
    }
    data = {"roleId": roleidforterr, "permissionId": getChecked()};
    $.ajax({
        url: "/addRolePermission",
        type: "post",
        data: data,
        success: function (data) {
            console.log(data);
            if (data.state == 2000) {
                $("#table_list_2").trigger("reloadGrid");
                successalert("", "菜单配置完成");
                $('#tt').tree('reload');
                hidetree();


            } else {
                errorsalert("", data.message);
            }
        },
        error: function () {
            errorsalert("", "出现错误，请重试");
        }
    })
}
