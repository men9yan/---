$(function () {  //页面加载完后执行
    $("#loading").show();  //如果被选元素已被隐藏，则显示这些元素：（显示图片）


    $("#updateRoleSubmit").click(function () {
        modify();  //修改信息的方法
    });


    $("#updateWayBillSubmit").click(function () {
        updateWayBillModify();  //修改信息的方法
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
            url: "/getWaybillAllInfo",//获取数据的地址
            datatype: "json",//返回的数据类型
            mtype: "POST", //提交方式
            autowidth: true, //如果为ture时，则当表格在首次被创建时会根据父元素比例重新调整
            // 表格宽度。
            width: "100%",
            height: "100%",
            shrinkToFit: false,
            autoScroll: true,//滚动条
            multiselect: true, //复选框
            rownumbers: true,//如果为ture则会在表格左边新增一列，显示行顺序号，从1开始递增。
            jsonReader: {  //	描述json 数据格式的数组
                root: "dataList",
                page: "currentPage",
                total: "totalPage",          //   很重要 定义了 后台分页参数的名字。
                records: "totalCount"
            },

            rowNum: 10,  //在grid上显示记录条数，这个参数是要被传递到后台
            rowList: [10, 20, 30], //	一个下拉选择框，用来改变显示记录数

            colNames: ['运单号', '寄件网点', '目的地', '收件人', '收件人电话', '寄件时间', '寄件人', '寄件人电话', '运单状态', '收件员', '派件员', '货物数量', '重量', '转运员', '操作人员', '操作网点', '操作'],//显示字段名
            colModel: [
                //这个参数指定了jqGrid各列的具体格式
                {
                    name: 'waybillId',  //指定对应数据中属性名
                    index: 'waybillId',  //用于列排序

                },
                {
                    name: 'sendBranch',
                    index: 'sendBranch',
                    editable: true,

                },


                {
                    name: 'destination',
                    index: 'destination',
                    editable: true,
                },

                {
                    name: 'consignor',
                    index: 'consignor',
                    editable: true,
                },
                {
                    name: 'consignorPhone',
                    index: 'consignorPhone',
                    editable: true,
                },
                {
                    name: 'sendTime',
                    index: 'sendTime',
                    editable: true,
                },
                {
                    name: 'sender',
                    index: 'sender',
                    editable: true,
                },
                {
                    name: 'senderPhone',
                    index: 'senderPhone',
                    editable: true,
                },
                {
                    name: 'waybillState',
                    index: 'waybillState',
                    editable: true,
                },
                {
                    name: 'receiptStaff',
                    index: 'receiptStaff',
                    editable: true,
                },
                {
                    name: 'dispatchStaff',
                    index: 'dispatchStaff',
                    editable: true,
                },
                {
                    name: 'number',
                    index: 'number',
                    editable: true,
                },
                {
                    name: 'weight',
                    index: 'weight',
                    editable: true,
                },

                {
                    name: 'vehicleDriver',
                    index: 'vehicleDriver',
                    editable: true,
                },
                {
                    name: 'operator',
                    index: 'operator',
                    editable: true,
                },
                {
                    name: 'operateBranch',
                    index: 'operateBranch',
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
            caption: "运单列表",  //这个参数制订了jqGrid的标题
            edit: false,
            hidegrid: false, //启用或者禁用控制表格显示、隐藏的按钮
            gridComplete: function () {
                console.log("grid Complete");
                var ids = $("#table_list_2").jqGrid("getDataIDs");
                console.log("ids--->{}", ids)
                for (var int = 0; int < ids.length; int++) {
                    var id = ids[int];
                    var waybillId = $("#table_list_2").jqGrid("getCell", id, "waybillId").toString();  //获取单元格中的数据
                    console.log("waybillId--->{}", waybillId)
                    var modify = "<button type='button' class='layui-btn layui-btn-warm layui-btn-xs' data-toggle='modal' data-target='#myModal2'  onclick='updateWayBillShow(" + id + ")'>编辑</button>";  //这里的onclick就是调用了上面的javascript函数 Modify(id)
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


//创建Vue对象
let app = new Vue({
    el: '#app',
    data: {
        waybillId: null,
        sendBranch: null,
        destination: null,
        customer: null,
        phone: null,
    },
    methods: {
        selectMethod: function () {
            let data = {
                'waybillId': app.waybillId,
                'sendBranch': app.sendBranch,
                'destination': app.destination,
                'customer': app.customer,
                'phone': app.phone,
            };
            console.log(data);
            $("#table_list_2").jqGrid('clearGridData');
            $("#table_list_2").setGridParam(
                {
                    url: "/findWayBillByParam",
                    postData: data
                }
            ).trigger("reloadGrid");


        },


    }
})

//---------------------------------------------------------------------------------------------bh-------------------------------

function updateWayBillShow(id) {  // 修改信息的方法
    //console.log("updateWayBillShow" + waybillId);
    var datas = $("#table_list_2").jqGrid("getRowData", id);//获取单行数据
    console.log("?????->:{}", datas);
    $("#updateWayBillId").val(datas.waybillId);
    $("#sendBranch").val(datas.sendBranch);
    $("#destination").val(datas.destination);
    $("#consignor").val(datas.consignor);
    $("#consignorPhone").val(datas.consignorPhone);
    $("#sender").val(datas.sender);
    $("#senderPhone").val(datas.senderPhone);
    $("#dispatchStaff").val(datas.receiptStaff);
    $("#number").val(datas.number);
}

//-------------------------------------------------------------------------------------------------------

//修改运单

function updateWayBillModify() {

    console.log($("#updateWayBillForm").serialize());
    console.log("你在哪里？？？？？？")

    $.ajax({
        url: "/updateWayBillById",
        type: "POST",
        data: $("#updateWayBillForm").serialize(),
        success: function (data) {
            console.log(data);
            if (data.state == 2000) {
                successalert("", "订单修改成功");
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





