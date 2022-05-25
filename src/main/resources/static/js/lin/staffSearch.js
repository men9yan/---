$(function () {  //页面加载完后执行
    $("#loading").show();  //如果被选元素已被隐藏，则显示这些元素：（显示图片）
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
            url: "/getAllStaffByDriverJson",//获取数据的地址
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
            rowNum: 10,  //在grid上显示记录条数，这个参数是要被传递到后台
            rowList: [10, 20, 30], //	一个下拉选择框，用来改变显示记录数
            colNames: ['员工编号', '姓名', '性别', '电话', '在职', '职位', '所属网点', '操作'],//显示字段名
            colModel: [  //这个参数指定了jqGrid各列的具体格式
                {
                    name: 'staffId',  //指定对应数据中属性名
                    index: 'staffId',  //用于列排序
                    width: 100

                },
                {
                    name: 'staffName',
                    index: 'staffName',
                    editable: true,
                    width: 80


                },
                {
                    name: 'gender',
                    index: 'gender',
                    editable: true,
                    width: 60

                },

                {
                    name: 'phone',
                    index: 'phone',
                    editable: true
                },

                {
                    name: 'state',
                    index: 'state',
                    editable: true,
                    width: 60
                },

                {
                    name: 'position',
                    index: 'position',
                    width: 60
                },

                {
                    name: 'branch',
                    index: 'branch',
                    editable: true
                },
                {
                    name: 'handle',
                    index: 'handle',
                    sortable: false
                }
            ],
            pager: "#pager_list_2",  //这个参数指定了jqGrid页脚显示位置。
            viewrecords: true,  //这个参数设置了是否显示所有记录的总数。
            caption: "员工列表",  //这个参数制订了jqGrid的标题
            add: false,   //开启添加功能
            edit: false,
            // addtext: 'Add',
            // editurl: "/addStaff",  //添加路径
            hidegrid: false, //启用或者禁用控制表格显示、隐藏的按钮
            gridComplete: function () {
                console.log("grid Complete");
                // var id=$('#table_list_2').jqGrid('getGridParam','selrow');
                var ids = $("#table_list_2").jqGrid("getDataIDs");
                console.log("ids--->{}", ids)
                for (var int = 0; int < ids.length; int++) {
                    var id = ids[int];
                    var StaffId = parseInt($("#table_list_2").jqGrid("getCell", id, "staffId"));  //获取单元格中的数据
                    console.log("StaffId--->{}", StaffId)
                    var modify = "<button type='button' style='margin: 0 5px 0 15px;' class='layui-btn layui-btn-warm layui-btn-xs' onclick='changedialogshow(" + id + "," + StaffId + ")'>选择</button>";  //这里的onclick就是调用了上面的javascript函数 Modify(id)
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

function changedialogshow(id, StaffId) {  // 修改信息的方法
    console.log("changedialogshow" + id);
    var datas = $("#table_list_2").jqGrid("getRowData", id);//获取单行数据
    console.log("?????->:{}", datas);
    parent.$('#addStaffId').val(StaffId);
    parent.$('#addVehicleInput').val(datas.staffName);
    parent.$('#addBranchSuoshu').val(datas.branch);
    parent.$('#addVehiclePhone').val(datas.phone);


    parent.$('.staffId').val(StaffId);
    parent.$('#editVehicleInput').val(datas.staffName);
    parent.$('.vehiclePhone').val(datas.phone);
    parent.$('#branchSuoshu').val(datas.branch);

    //隐藏iframe
    parent.$('#layui-layer4').hide();


}




