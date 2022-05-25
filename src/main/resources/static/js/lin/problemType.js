$(function () {
    $("#updateProblemTypeSubmit").click(function () {
        modify();
    });

    $("#addProblemType").click(function () {
        $('#addptName').val("");
        $('#addptRemark').val("");
    });




    //执行添加方法
    $("#addProblemTypeSubmit").click(function () {
        addmodify();

    });


    $.jgrid.defaults.styleUI = 'Bootstrap';


    $("#loading").show();

    buildgrid();

    $("#loading").hide();

});

function deldialog(ptId) {
    deletealert("", "确定要删除此角色吗？", function () {
        del(ptId)
    });
}

function del(ptId) {
    console.log("!!:" + ptId);
    $.ajax({
        url: '/getDeleteById',
        type: "POST",
        data: 'ptId=' + ptId,
        success: function (data) {
            console.log(data);
            if (data.state == 2000) {
                $("#table_list_2").trigger("reloadGrid");
                successalert("", "删除完成");
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
    console.log("???" + $("#updateProblemTypeForm").serialize());
    $.ajax({
        url: "/getUpdateProblemTypeById",
        type: "POST",
        data: $("#updateProblemTypeForm").serialize(),
        success: function (data) {
            console.log(data);
            if (data.state == 2000) {
                successalert("", "修改完成");
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




//----------------------------------------------------------------------------------------------------------------------------

//编辑角色

function updateProblemTypeShow(id) {  // 修改信息的方法
    var datas = $("#table_list_2").jqGrid("getRowData", id);//获取单行数据
    console.log("?????->:{}", datas);
    $("#ptIdStr").val(datas.ptId);
    $("#ptName").val(datas.ptName);
    $("#ptRemark").val(datas.ptRemark);
}

//----------------------------------------------------------------------------------------------------------------------------

function buildgrid() {
    $("#table_list_2").jqGrid(
        {
            url: "/getAllProblemTypeJson",
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
            colNames: ['编号', '名称', '备注', '时间','操作'],
            colModel: [
                {
                    name: 'ptId',
                    index: 'ptId',
                    editable: true,

                },
                {
                    name: 'ptName',
                    index: 'ptName',
                    editable: true,
                },
                {
                    name: 'ptRemark',
                    index: 'ptRemark',
                }
                ,
                {
                    name: 'ptTime',
                    index: 'ptTime',
                },
                {
                    name: 'handle',
                    index: 'handle',
                    sortable: false
                }
            ],
            pager: "#pager_list_2",
            viewrecords: true,
            caption: "问题件类型",
            add: false,
            edit: false,
            hidegrid: false,
            gridComplete: function () {
                console.log("grid Complete");
                var ids = $("#table_list_2").jqGrid("getDataIDs");
                console.log("ids--->{}", ids)
                for (var int = 0; int < ids.length; int++) {
                    var id = ids[int];
                    var ptId = $("#table_list_2").jqGrid("getCell", id, "ptId");  //获取单元格中的数据
                    var modify = "<button type='button' class='layui-btn layui-btn-warm layui-btn-xs' data-toggle='modal' data-target='#myModal' onclick='updateProblemTypeShow(" + id  + ")'>编辑</button>";  //这里的onclick就是调用了上面的javascript函数 Modify(id)
                    var del = "<button  type='button' class='layui-btn layui-btn-danger layui-btn-xs' onclick='deldialog(" + ptId + ")' >删除</button>";
                    var result = $("#table_list_2").jqGrid("setRowData", id, {handle: del + "&nbsp;" + modify});
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

    console.log("!!!!新的内容：" + $("#addProblemTypeForm").serialize());

    $.ajax({
        url: "/getAddProblemType",
        type: "POST",
        data: $("#addProblemTypeForm").serialize(),
        success: function (data) {
            console.log(data);
            if (data.state == 2000) {
                successalert("", "添加完成");
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



