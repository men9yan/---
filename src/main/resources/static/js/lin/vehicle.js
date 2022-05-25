$(function () {  //页面加载完后执行
    $("#loading").show();  //如果被选元素已被隐藏，则显示这些元素：（显示图片）


    $("#addVehicle").click(function () {
        addCleanInput();
    })
    $("#changeSubmit").click(function () {
        editVehicleModify();  //修改信息的方法
    });

    //执行添加方法
    $("#addSubmit").click(function () {
        addVehicleModify();

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
            url: "/getVehicleAllList",//获取数据的地址
            datatype: "json",//返回的数据类型
            mtype: "POST", //提交方式
            shrinkToFit:false,
            autoScroll: true,
            autowidth: true, //如果为ture时，则当表格在首次被创建时会根据父元素比例重新调整
            // 表格宽度。
            height: "100%",
            rownumbers: true, //如果为ture则会在表格左边新增一列，显示行顺序号，从1开始递增。
            jsonReader: {  //	描述json 数据格式的数组
                root: "dataList",
                page: "currentPage",
                total: "totalPage",          //   很重要 定义了 后台分页参数的名字。
                records: "totalCount"
            },

            rowNum: 10,  //在grid上显示记录条数，这个参数是要被传递到后台
            rowList: [10, 20, 30], //	一个下拉选择框，用来改变显示记录数
            colNames: ['车辆编号', '车牌号', '车辆类型', '创建时间', '车辆状况', '车辆状态', '车辆载重','所属网点', '转运员', '转运员电话', '车主', '车辆净重', '车俩年限', '购车时间', '操作'],//显示字段名
            colModel: [  //这个参数指定了jqGrid各列的具体格式
                {
                    name: 'vehicleId',  //指定对应数据中属性名
                    index: 'vehicleId',  //用于列排序
                    width: 150,
                },
                {
                    name: 'vehicleNum',
                    index: 'vehicleNum',
                    width: 150,

                },
                {
                    name: 'vehicleType',
                    index: 'vehicleType',
                    width: 150,

                },

                {
                    name: 'createTime',
                    index: 'createTime',
                    width: 150,
                },

                {
                    name: 'vehicleStatus',
                    index: 'vehicleStatus',
                    width: 150,
                },

                {
                    name: 'vehicleState',
                    index: 'vehicleState',
                    width: 150,
                },

                {
                    name: 'vehicleLoad',
                    index: 'vehicleLoad',
                    width: 150,
                },

                {
                    name: 'branchSuoshu',
                    index: 'branchSuoshu',
                    width: 150,
                },

                {
                    name: 'vehicleDriver',
                    index: 'vehicleDriver',
                    width: 150,
                },
                {
                    name: 'vehiclePhone',
                    index: 'vehiclePhone',
                    width: 150,
                },
                {
                    name: 'owner',
                    index: 'owner',
                    width: 150,
                },
                {
                    name: 'vehicleWeight',
                    index: 'vehicleWeight',
                    width: 150,
                },
                {
                    name: 'vehicleAgeLimit',
                    index: 'vehicleAgeLimit',
                    width: 150,
                },
                {
                    name: 'buyCarTime',
                    index: 'buyCarTime',
                    width: 150,
                },
                {
                    name: 'handle',
                    index: 'handle',
                    width: 150,
                }
            ],
            pager: "#pager_list_2",  //这个参数指定了jqGrid页脚显示位置。
            viewrecords: true,  //这个参数设置了是否显示所有记录的总数。
            caption: "车辆列表",  //这个参数制订了jqGrid的标题
            add: false,   //开启添加功能
            edit: false,
            hidegrid: true, //启用或者禁用控制表格显示、隐藏的按钮
            gridComplete: function () {
                console.log("grid Complete");
                // var id=$('#table_list_2').jqGrid('getGridParam','selrow');
                var ids = $("#table_list_2").jqGrid("getDataIDs");
                console.log("ids--->{}", ids)
                for (var int = 0; int < ids.length; int++) {
                    var id = ids[int];
                    var VehicleId = parseInt($("#table_list_2").jqGrid("getCell", id, "vehicleId"));  //获取单元格中的数据
                    console.log("VehicleId--->{}", VehicleId)
                    var modify = "<button type='button' style='margin: 0 5px 0 15px;'class='layui-btn layui-btn-warm layui-btn-xs' data-toggle='modal' data-target='#myModal' onclick='changedialogshow(" + id + "," + VehicleId + ")'>编辑</button>";  //这里的onclick就是调用了上面的javascript函数 Modify(id)
                    var del = "<button type='button' style='margin: 0 5px 0 -10px;' class='layui-btn layui-btn-danger layui-btn-xs' onclick='deldialog(" + VehicleId + ")'>删除</button>";
                    var result = $("#table_list_2").jqGrid("setRowData", id, {handle: modify + "&nbsp &nbsp" + del});
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

//删除信息的方法
//--------------------------------------------------------------------------------------------------------------------------------------------------

function deldialog(VehicleId) {
    deletealert("", "确定要删除吗？", function () {
        console.log("VehicleId-->{}", VehicleId)
        del(VehicleId)
    });
}


function del(vehicleId) {
    $.ajax({
        url: '/deleteVehicle',
        type: "POST",
        data: 'vehicleId=' + vehicleId,
        success: function (data) {
            console.log(data);
            if (data.state == 2000) {
                $("#table_list_2").trigger("reloadGrid");
                successalert("", "删除车辆完成");
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

//修改信息的方法
function editVehicleModify() {

    console.log("???"+$("#changeform").serialize());

    $.ajax({
        url: "/updateVehicle",
        type: "POST",
        data: $("#changeform").serialize(),
        success: function (data) {
            console.log(data);
            if (data.state == 2000) {
                successalert("", "用户信息修改完成");
                $("#table_list_2").trigger("reloadGrid");
                $('#myModal').modal('hide');
            } else {
                errorsalert("", data.message);
            }

        },
        error: function () {
            errorsalert("", "出现错误，请重试");
        }
    });
}



function changedialogshow(id, vehicleId) {  // 修改信息的方法
    var datas = $("#table_list_2").jqGrid("getRowData", id);//获取单行数据
    console.log("?????->:{}", datas);
    console.log("?????->:{}", vehicleId);
    $("#vehicleId").attr("value", vehicleId);
    $("#vehicleNum").val(datas.vehicleNum);
    $("#vehicleType").val(datas.vehicleType);
    $("#editVehicleState").val(datas.vehicleState);
    $("#editVehicleStatus").val(datas.vehicleStatus);
    $("#vehicleLoad").val(datas.vehicleLoad);
    $("#branchSuoshu").val(datas.branchSuoshu);
    $("#editVehicleInput").val(datas.vehicleDriver);
    $("#editVehiclePhone").val(datas.vehiclePhone);
    $("#owner").val(datas.owner);
    $("#createTime").val(datas.createTime);
    $("#editStaffId").val(datas.staffId);
    $("#vehicleAgeLimit").val(datas.vehicleAgeLimit.toString());
    $("#buyCarTime").val(datas.buyCarTime.toString());

    console.log("车俩年限->:{}", typeof (datas.vehicleAgeLimit));
    console.log("?????->:{}", datas.buyCarTime);
}


//--------------------------------------------------------------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------------------------------------------------------------


//添加信息功能
function addVehicleModify() {  //添加信息的方法

    $.ajax({
        url: "/addVehicle",
        type: "POST",
        data: $("#addform").serialize(),
        success: function (data) {
            console.log(data);
            if (data.state == 2000) {
                successalert("", "用户信息添加完成");
                $("#table_list_2").trigger("reloadGrid");
                $('#myModal2').modal('hide');

            } else {
                errorsalert("", data.message);
            }

        },
        error: function () {
            errorsalert("", data.message);
        }
    });
}

//点开添加清空input
function addCleanInput(){
    console.log("test成功");
    $('#addform')[0].reset();

}


// 创建Vue对象
Vue.component('v-select',VueSelect.VueSelect);
let app = new Vue({
    el: '#app',
    data: {
        vehicleNum: null,
        vehicleDriver: null,
        vehicleState: '',
        stateList: [
            {label: '空闲'},
            {label: '运输'},
            {label: '维护'},
        ]

    },
    methods: {

        addMethod: function () {
            let data = {
                'vehicleNum': app.vehicleNum,
                'vehicleDriver': app.vehicleDriver,
                'vehicleState': app.vehicleState,
                'branch': app.branch
            };
            console.log(data);
            $("#table_list_2").jqGrid('clearGridData');
            $("#table_list_2").setGridParam(
                {
                    url: "/findVehicleByParam",
                    postData: data
                }
            ).trigger("reloadGrid");
            app.branch=null;
        },

    }
})






