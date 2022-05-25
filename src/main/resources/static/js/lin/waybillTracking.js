$(function () {



    $.jgrid.defaults.styleUI = 'Bootstrap';


    $("#loading").show();

    buildgrid();

    $("#loading").hide();

});



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
                // $('#myModal').modal('hide');
            } else {
                errorsalert("", data.message);
            }

        },
        error: function () {
            errorsalert("", "出现错误，请重试");
        }
    });
}




//----------------------------------------------------------------------------------------------------------------------------



//----------------------------------------------------------------------------------------------------------------------------

function buildgrid() {
    $("#table_list_2").jqGrid(
        {
            url: "/getFindWaybillRecordJson",
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
            colNames: ['运单编号', '寄件人', '寄件人电话', '操作'],
            colModel: [
                {
                    name: 'waybillId',
                    index: 'waybillId',

                },
                {
                    name: 'sender',
                    index: 'sender',
                },
                {
                    name: 'senderPhone',
                    index: 'senderPhone',
                },
                {
                    name: 'handle',
                    index: 'handle',
                    sortable: false
                }
            ],
            pager: "#pager_list_2",
            viewrecords: true,
            caption: "运单列表",
            add: false,
            edit: false,
            hidegrid: false,
            gridComplete: function () {
                console.log("grid Complete");
                var ids = $("#table_list_2").jqGrid("getDataIDs");
                console.log("ids--->{}", ids)
                for (var int = 0; int < ids.length; int++) {
                    var id = ids[int];
                    var modify = "<button type='button' class='layui-btn layui-btn-warm layui-btn-xs' data-toggle='modal' data-target='#myModal' onclick='waybillShow(" + id + ")'>查看物流信息</button>";  //这里的onclick就是调用了上面的javascript函数 Modify(id)
                    var result = $("#table_list_2").jqGrid("setRowData", id, {handle:  modify});
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






//--------------------------------------------------------------------------------------------------------------------------
//搜索数据

//创建Vue对象
let app = new Vue({
    el: '#app',
    data: {
        waybillId: null,
        sender: null,

    },
    methods: {

        selectMethod: function () {
            let data = {
                'waybillId': app.waybillId,
                'sender': app.sender
            };
            console.log(data);
            $("#table_list_2").jqGrid('clearGridData');
            $("#table_list_2").setGridParam(
                {
                    url: "/findWaybillRecordByParamJson",
                    postData: data
                }
            ).trigger("reloadGrid");
            app.branch=null;

        },


    }
})


//-------------------------------------------------------------------------------------------------------------



function waybillShow(id) {  // 修改信息的方法
    var datas = $("#table_list_2").jqGrid("getRowData", id);//获取单行数据
    console.log("waybillId" + datas.waybillId);
    showWaybillapp.showWaybillRecord(datas.waybillId);
}

//-------------------------------------------------------------------------------------------------------------

let showWaybillapp = new Vue({
    el: '#showWaybill',
    data: {
        WaybillRecords:[]
    },
    methods: {
        showWaybillRecord: function (waybillId) {
            let data =
                {
                    'waybillId':waybillId,
                };
            console.log(data);
            $.ajax({
                url: "/getFindWaybillRecordById",
                type: "POST",
                data: data,
                success: function (data) {
                    console.log("111111111");
                    let list = data.data;
                    console.log(data.data)
                    showWaybillapp.WaybillRecords = list;

                },
                error: function () {
                    errorsalert("", data.message);
                }
            });
        },

    }
});




problemHanding.html