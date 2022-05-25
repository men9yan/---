$(function () {  //页面加载完后执行
    $("#loading").show();  //如果被选元素已被隐藏，则显示这些元素：（显示图片）

    buildgrid();
    // $("#table_list_2").setSelection(1)
    $.jgrid.defaults.styleUI = 'Bootstrap';  //制定使用bootstrap的版本的样式

    $("#loading").hide();

});


//--------------------------------------------------------------------------------------------------------------------------------------------------


//制作表格
function buildgrid() {
    $("#table_list_2").jqGrid(
        {
            url: "/getFindUnLoadingWaybillByBranchJson",//获取数据的地址
            datatype: "json",//返回的数据类型
            mtype: "POST", //提交方式
            autowidth: true, //如果为ture时，则当表格在首次被创建时会根据父元素比例重新调整
            // 表格宽度。
            width: "100%",
            height: "100%",
            multiselect:true,
            multiboxonly:true,
            shrinkToFit: false,
            autoScroll: true,//滚动条
            rownumbers: true,//如果为ture则会在表格左边新增一列，显示行顺序号，从1开始递增。
            jsonReader: {  //	描述json 数据格式的数组
                root: "dataList",
                page: "currentPage",
                total: "totalPage",          //   很重要 定义了 后台分页参数的名字。
                records: "totalCount"
            },

            rowNum: 10,  //在grid上显示记录条数，这个参数是要被传递到后台
            rowList: [10, 20, 30], //	一个下拉选择框，用来改变显示记录数

            colNames: ['运单号', '目的地', '目的网点', '收件员','收件员', '寄件人', '寄件人电话', '寄件网点', '寄件时间', '运单状态', '收件人电话', '收件人', '操作人', '操作网点', '操作时间', '货物数量', '重量','下一站','车牌号','运输员','操作'],//显示字段名
            colModel: [
                //这个参数指定了jqGrid各列的具体格式
                {
                    name: 'waybillId',  //指定对应数据中属性名
                    index: 'waybillId',  //用于列排序

                },
                {
                    name: 'destination',
                    index: 'destination',

                },


                {
                    name: 'destinationBranch',
                    index: 'destinationBranch',
                },

                {
                    name: 'receiptStaffStr',
                    index: 'receiptStaffStr',
                },
                {
                    name: 'receiptStaff',
                    index: 'receiptStaff',
                    hidden:true
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
                    name: 'sendBranch',
                    index: 'sendBranch',
                },
                {
                    name: 'sendTime',
                    index: 'sendTime',
                },
                {
                    name: 'waybillStateStr',
                    index: 'waybillStateStr',
                    hidden:true,
                },
                {
                    name: 'consignor',
                    index: 'consignor',
                },
                {
                    name: 'consignorPhone',
                    index: 'consignorPhone',
                },
                {
                    name: 'operator',
                    index: 'operator',
                },
                {
                    name: 'operateBranch',
                    index: 'operateBranch',
                },
                {
                    name: 'operateTime',
                    index: 'operateTime',
                },
                {
                    name: 'number',
                    index: 'number',
                },
                {
                    name: 'weight',
                    index: 'weight',
                },
                {
                    name: 'nextBranch',
                    index: 'nextBranch',
                    hidden:true
                },
                {
                    name: 'vehicleNum',
                    index: 'vehicleNum',
                    hidden:true
                }
                ,
                {
                    name: 'vehicleDriver',
                    index: 'vehicleDriver',
                    hidden:true
                },
                {
                    name: 'handle',
                    index: 'handle',
                    sortable: false,
                }


            ],
            pager: "#pager_list_2",  //这个参数指定了jqGrid页脚显示位置。
            viewrecords: true,  //这个参数设置了是否显示所有记录的总数。
            caption: "用户列表",  //这个参数制订了jqGrid的标题
            edit: false,
            hidegrid: false, //启用或者禁用控制表格显示、隐藏的按钮
            onSelectRow: function (rowId, status, e) {
                var rowIds = jQuery("#table_list_2").jqGrid('getGridParam', 'selarrrow');
                console.log("多选内容：")
                console.log(rowIds)
                var datas= $("#table_list_2").jqGrid("getRowData",rowIds);

                WaybillApp.showWaybill(datas);

            },
            gridComplete: function () {
                console.log("grid Complete");
                var ids = $("#table_list_2").jqGrid("getDataIDs");
                console.log("ids--->{}", ids)
                for (var int = 0; int < ids.length; int++) {
                    var id = ids[int];
                    var problemModify = "<button   class='layui-btn layui-btn-warm layui-btn-xs' data-toggle='modal' data-target='#problemMyModal' onclick='changedialogshow(" + id + ")'>问题件</button>";
                    var result = $("#table_list_2").jqGrid("setRowData", id, {handle: problemModify });
                }
            },
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







Vue.component('v-select',VueSelect.VueSelect);
let WaybillApp = new Vue({
    el: '#f1',

    methods: {
        showWaybill:function(datas){
            console.log("数据为")
            console.log(datas)

            $("#waybillId").val(datas.waybillId);
            $("#nextBranch").val(datas.nextBranch);
            $("#vehicleNum").val(datas.vehicleNum);
            $("#weight").val(datas.weight);
            $("#vehicleDriver").val(datas.vehicleDriver);



        },


        addSendScan: function () {

            if($("#waybillId").val()==""){
                swal("错误", "请输入完整信息!", "error");
                return;
            }

            console.log($("#f1").serialize());

           $.ajax({
                url: "/getUpdateunloadingWaybillById",
                type: "POST",
                data: $("#f1").serialize(),
                success: function (data) {
                    console.log(data);
                    if (data.state == 2000) {
                        successalert("", "卸车扫描完成");
                        $("#table_list_2").trigger("reloadGrid");
                        $('#f1')[0].reset();

                    } else {
                        errorsalert("", data.message);
                        $('#f1')[0].reset();
                    }

                },
                error: function () {
                    errorsalert("", data.message);
                }
            });

        },
    }
});



//-----------------------------------------------------------------------------------------------------------------------------------------
function changedialogshow(id) {  // 修改信息的方法
    var datas = $("#table_list_2").jqGrid("getRowData", id);//获取单行数据
    console.log(datas);
    console.log("运单号:"+datas.waybillId);
    showSendScanApp.showWaybillId(datas);
}

//-----------------------------------------------------------------------------------------------------------------------------------------


//创建Vue对象
Vue.component('v-select',VueSelect.VueSelect);
let showSendScanApp = new Vue({
    el:'#problemform',
    data:{

        problemWaybillId:null,
        responsibleParty:null,
        problemDesc:null,
        datas:null,
        ptName:null,
        ptNamelist:[]

    },

    methods:{
        showWaybillId:function(datas){
            showSendScanApp.problemWaybillId=datas.waybillId;
            showSendScanApp.datas=datas;
            $.ajax({
                url:'/getFindAll',
                type:'post',
                dataType:'json',
                success:function(data){
                    console.log("111111111");
                    let list=data.data;
                    console.log(data.data)
                    let ptNamelist = [];
                    for(let i=0;i<list.length;i++){
                        let  op ={};
                        op.label = list[i].ptName;
                        op.value = list[i].ptName;
                        ptNamelist[i] = op;
                    }
                    showSendScanApp.ptNamelist=ptNamelist;
                }
            });
        },

        addProblem:function(){
            let datas=showSendScanApp.datas;
            let data =
                {
                    'waybillId':showSendScanApp.problemWaybillId,
                    'receiveBranch':datas.sendBranch,
                    'registerBranch':datas.operateBranch,
                    'registrant':datas.operator,
                    'responsibleParty':showSendScanApp.responsibleParty,
                    'problemDesc':showSendScanApp.problemDesc,
                    'ptName':showSendScanApp.ptName,
                };
            console.log(data);

            $.ajax({
                url: "/addProblem",
                type: "POST",
                data: data,
                success: function (data) {
                    console.log(data);
                    if (data.state == 2000) {
                        successalert("", "问题件上传完成！！");
                        $("#table_list_2").trigger("reloadGrid");
                        $('#problemMyModal').modal('hide');
                    } else {
                        errorsalert("", data.message);
                    }

                },
                error: function () {
                    errorsalert("", "出现错误，请重试");
                }
            });
        }



    }

});


