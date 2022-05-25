$(function () {  //页面加载完后执行
    $("#loading").show();  //如果被选元素已被隐藏，则显示这些元素：（显示图片）


    $("#changeSubmit").click(function () {
        editVehicleModify();  //修改信息的方法
    });

    //执行添加方法
    $("#updateExamineSubmit").click(function () {
        updateExamineModify();

    });


    buildgrid();
    $.jgrid.defaults.styleUI = 'Bootstrap';  //制定使用bootstrap的版本的样式

    $("#loading").hide();


});


//--------------------------------------------------------------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------------------------------------------------------------

//制作表格
function buildgrid() {
    $("#table_list_2").jqGrid(
        {
            url: "/getBranchAuditAllList",//获取数据的地址
            datatype: "json",//返回的数据类型
            mtype: "POST", //提交方式
            autowidth: true, //如果为ture时，则当表格在首次被创建时会根据父元素比例重新调整
            // 表格宽度。

            rownumbers: true, //如果为ture则会在表格左边新增一列，显示行顺序号，从1开始递增。
            jsonReader: {  //	描述json 数据格式的数组
                root: "dataList",
                page: "currentPage",
                total: "totalPage",          //   很重要 定义了 后台分页参数的名字。
                records: "totalCount"
            },
            width: "100%",
            autowidth: true,
            height: "100%",
            rowNum: 15,  //在grid上显示记录条数，这个参数是要被传递到后台
            rowList: [15, 25, 35], //	一个下拉选择框，用来改变显示记录数
            colNames: ['网点编号', '网点名称', '所属网点',  '联系电话', '申请时间', '审核情况', '备注', '操作'],//显示字段名
            colModel: [  //这个参数指定了jqGrid各列的具体格式
                {
                    name: 'branchId',  //指定对应数据中属性名
                    index: 'branchId',  //用于列排序
                },
                {
                    name: 'branchName',
                    index: 'branchName',
                    editable: true,

                },
                {
                    name: 'suosuBranch',
                    index: 'suosuBranch',
                    editable: true,

                },

                {
                    name: 'branchPhone',
                    index: 'branchPhone',
                    editable: true,
                },
                {
                    name: 'branchTime',
                    index: 'branchTime',
                    editable: true,
                },

                {
                    name: 'examineStr',
                    index: 'examineStr',
                    editable: true,
                },
                {
                    name: 'remark',
                    index: 'remark',
                    editable: true,
                },
                {
                    name: 'handle',
                    index: 'handle',
                    sortable: false,
                }
            ],
            pager: "#pager_list_2",  //这个参数指定了jqGrid页脚显示位置。
            viewrecords: true,  //这个参数设置了是否显示所有记录的总数。
            caption: "车辆列表",  //这个参数制订了jqGrid的标题
            add: false,   //开启添加功能
            edit: false,
            //addtext: 'Add',
            //editurl: "/addVehicle",  //添加路径
            hidegrid: false, //启用或者禁用控制表格显示、隐藏的按钮
            gridComplete: function () {
                console.log("grid Complete");
                // var id=$('#table_list_2').jqGrid('getGridParam','selrow');
                var ids = $("#table_list_2").jqGrid("getDataIDs");
                console.log("ids--->{}", ids)
                for (var int = 0; int < ids.length; int++) {
                    var id = ids[int];
                    var branchId = parseInt($("#table_list_2").jqGrid("getCell", id, "branchId"));  //获取单元格中的数据
                    console.log("branchId--->{}", branchId)
                    var modify = "<button type='button' style='margin: 0 5px 0 15px;'class='layui-btn layui-btn-warm layui-btn-xs' data-toggle='modal' data-target='#myModal' onclick='changedialogshow(" + branchId + ")'>审核</button>";  //这里的onclick就是调用了上面的javascript函数 Modify(id)
                    var result = $("#table_list_2").jqGrid("setRowData", id, {handle: modify});
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


//--------------------------------------------------------------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------------------------------------------------------------


function changedialogshow(branchId) {  // 修改信息的方法
    var examines = document.getElementsByName("examine");
    $.ajax({
        url: '/showExamineId',
        type: 'post',
        data: "branchId=" + branchId,
        success: function (data) {
            console.log(data);
            let list = data.data;
            // let roles=rolesApp.roles;
            for (var j = 0; j < examines.length; j++) {
                // console.log("roles:"+rols[j].roleId);
                if (examines[j].value == list.examine) {
                    $(examines[j]).next().click();
                    $("#branchId").val(branchId);
                    $("#remark").val(list.remark);
                }

            }
        }
    });

}


//--------------------------------------------------------------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------------------------------------------------------------


//审核信息功能
function updateExamineModify() {  //添加信息的方法

    $.ajax({
        url: "/updateExamine",
        type: "POST",
        data: $("#updateExamineForm").serialize(),
        success: function (data) {
            console.log(data);
            if (data.state == 2000) {
                successalert("", "审核信息完成");
                $("#table_list_2").trigger("reloadGrid");
                $('#myModal').modal('hide');

            } else {
                errorsalert("", data.message);
            }

        },
        error: function () {
            errorsalert("", data.message);
        }
    });
}


//--------------------------------------------------------------------------------------------------------------------------------------------------


// 创建Vue对象
let app = new Vue({
    el: '#app',
    data: {
        branchId: null,
    },
    methods: {
        addMethod: function () {
            let data = {
                'branchId': app.branchId,
            };
            console.log(data);
            $("#table_list_2").jqGrid('clearGridData');
            $("#table_list_2").setGridParam(
                {
                    url: "/findBranchAuditAllById",
                    postData: data
                }
            ).trigger("reloadGrid");

        },

    }
})

//显示审核情况
layui.use(['form'], function () {
    var form = layui.form
        , layer = layui.layer;
});

let examinesApp = new Vue({
    el: '#examinesApp',
    data: {
        examines: [
            {examine: 1, examineStr: '待审核'},
            {examine: 2, examineStr: '审核成功'},
            {examine: 3, examineStr: '审核失败'},
        ]
    },
    methods: {

        loadExamines: function () {
            $.ajax({
                url: 'showExamine',
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    console.log(data.data);
                    examinesApp.examines = data.data;

                }
            });
        }
    },
    created: function () {
        this.loadExamines();
    }
});



