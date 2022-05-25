$(function () {  //页面加载完后执行
    $("#loading").show();  //如果被选元素已被隐藏，则显示这些元素：（显示图片）


    $("#table_list_2").trigger("reloadGrid");



    buildgrid();





    $.jgrid.defaults.styleUI = 'Bootstrap';  //制定使用bootstrap的版本的样式

    $("#loading").hide();


});


//--------------------------------------------------------------------------------------------------------------------------------------------------


//制作表格
function buildgrid() {
    $("#table_list_2").jqGrid(
        {
            url: "/getFindDeliveryWaybillByBranchJson",//获取数据的地址
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
            colNames: ['运单号', '目的地', '目的网点', '收件员','收件员', '寄件人', '寄件人电话', '寄件网点', '寄件时间', '运单状态', '收件人电话', '收件人', '操作人', '操作网点', '操作时间', '货物数量', '重量','下一站','操作'],//显示字段名
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
                    name: 'handle',
                    index: 'handle',
                    sortable: false

                }


            ],
            pager: "#pager_list_2",  //这个参数指定了jqGrid页脚显示位置。
            viewrecords: true,  //这个参数设置了是否显示所有记录的总数。
            caption: "运单列表",  //这个参数制订了jqGrid的标题
            add: false,   //开启添加功能
            multiselect: true,
            edit: false,
            hidegrid: false, //启用或者禁用控制表格显示、隐藏的按钮
            gridComplete: function () {
                console.log("grid Complete");
                var ids = $("#table_list_2").jqGrid("getDataIDs");
                console.log("ids--->{}", ids)
                for (var int = 0; int < ids.length; int++) {
                    var id = ids[int];
                    var modify = "<button type='button' class='layui-btn  layui-btn-xs' data-toggle='modal' data-target='#showMyModal' onclick='staffAnyShow(" + id + ")'>指定派件员</button>";
                    var problemModify = "<button   class='layui-btn layui-btn-warm layui-btn-xs' data-toggle='modal' data-target='#problemMyModal' onclick='changedialogshow(" + id + ")'>问题件</button>";
                    var result = $("#table_list_2").jqGrid("setRowData", id, {handle: modify+""+problemModify });
                }
            },
            onSelectRow: function (rowId, status, e) {
                var rowIds = jQuery("#table_list_2").jqGrid('getGridParam', 'selarrrow');
                console.log("多选内容：")
                console.log(rowIds)
                var dataList=[];
                for(var i=0;i<rowIds.length;i++){
                    var datas = $("#table_list_2").jqGrid("getRowData", rowIds[i]);//获取单行数据
                    console.log("多选内容数据：")
                    console.log(datas)
                    dataList.push(datas);
                    console.log(dataList)
                }
                showAllDeliveryStaffApp.showStaffName(dataList);
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


//--------------------------------------------------------------------------------------------------------------------------------------------------


//创建Vue对象
let app = new Vue({
    el:'#app',
    data:{
        waybillId:null,

    },
    methods:{
        selectMethod : function(){
            let data = {
                'waybillId':app.waybillId,

            };
            console.log(data);
            $("#table_list_2").jqGrid('clearGridData');
            $("#table_list_2").setGridParam(
                {
                    url:"/FindDeliveryWaybillALl",
                    postData:data
                }
            ).trigger("reloadGrid");


        },


    }
})

//--------------------------------------------------------------------------------------------------------------------------------------------------

function staffAnyShow(id) {  // 修改信息的方法
    var datas = $("#table_list_2").jqGrid("getRowData", id);//获取单行数据
    console.log(datas);
    console.log("所在网点为:"+datas.operateBranch+",运单号:"+datas.waybillId);
    showAdeliveryStaffApp.showStaffName(datas);
}


//*************************************************************************************************************************************************
//创建Vue对象
Vue.component('v-select',VueSelect.VueSelect);
let showAdeliveryStaffApp = new Vue({
    el:'#changeform',
    data:{

        waybillId:null,
        staffName:null,
        staffNameList:[
            {  label: '福建省福州市台江区xxxx',value:1 },
            {  label: '福建省莆田市荔城区xxxx',value:2 },
            {  label: '福建省泉州市丰泽区xxxx',value:3 },
            {  label: '福建省龙岩市漳平市xxxx',value:4 }
        ],
        newData:null

    },

    methods:{
        showStaffName:function(datas){
            console.log(datas.waybillId)
            let data={
                'branch':datas.destinationBranch
            }

            $.ajax({
                url:'showStaffByBranch',
                type:'post',
                dataType:'json',
                data:data,
                success:function(data){

                    console.log("111111111");
                    let list=data.data;
                    console.log(data.data)
                    let staffNameList = [];
                    for(let i=0;i<list.length;i++){
                        let  op ={};
                        op.label = list[i].staffName;
                        op.value = list[i].staffId;
                        staffNameList[i] = op;
                    }
                    showAdeliveryStaffApp.staffNameList=staffNameList;

                    showAdeliveryStaffApp.waybillId = datas.waybillId;
                    showAdeliveryStaffApp.newData=datas;
                    showAdeliveryStaffApp.staffName=null;
                }
            });
        },

        deliveryWaybill:function(){
            console.log("!!!!!!");
            console.log(showAdeliveryStaffApp.newData);
            let datas=showAdeliveryStaffApp.newData;
            let data =
                {
                    'waybillId':datas.waybillId,
                    'operator':datas.operator,
                    'operateBranch':datas.operateBranch,
                    'dispatchStaff':showAdeliveryStaffApp.staffName
                };
            console.log(data);

            $.ajax({
                url: "/getUpdateDeliveryWaybillById",
                type: "POST",
                data: data,
                success: function (data) {
                    console.log(data);
                    if (data.state == 2000) {
                        successalert("", "指定派件员完成！！");
                        $("#table_list_2").trigger("reloadGrid");
                        $('#showMyModal').modal('hide');
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

function method(list,value){
    var label;
    for (var i=0;i<list.length;i++){
        if(list[i].value==value){
            label=list[i].label;
        }
    }
    return label;

}


//--------------------------------------------------------------------------------------------------------------------------------
//批量分配员工
//创建Vue对象
Vue.component('v-select',VueSelect.VueSelect);
let showAllDeliveryStaffApp = new Vue({
    el:'#allocationStaffForm',
    data:{

        anyWaybillId:null,
        staffName:null,
        staffNameList:[ ],
        datas:null,
        number:null

    },

    methods:{
        showStaffName:function(dataList){
            console.log("该数据是：");
            console.log(dataList);
            var WaybillIds="";
            for (var i=0;i<dataList.length;i++){
                WaybillIds+=dataList[i].waybillId
                WaybillIds+=","

            }
            console.log("订单编号是：");
            console.log(WaybillIds)

            //判断数据是否相同
            if(dataList==null){
                showAllDeliveryStaffApp.anyWaybillId=null;
            }else{
                let data={
                    'branch':dataList[0].destinationBranch
                }

                $.ajax({
                    url:'showStaffByBranch',
                    type:'post',
                    dataType:'json',
                    data:data,
                    success:function(data){
                        console.log("111111111");
                        let list=data.data;
                        console.log(data.data)
                        let staffNameList = [];
                        for(let i=0;i<list.length;i++){
                            let  op ={};
                            op.label = list[i].staffName;
                            op.value = list[i].staffId;
                            staffNameList[i] = op;
                        }
                        showAllDeliveryStaffApp.staffNameList=staffNameList;
                        showAllDeliveryStaffApp.anyWaybillId=WaybillIds;
                        showAllDeliveryStaffApp.datas=dataList[0];
                        showAllDeliveryStaffApp.staffName=null;
                        showAllDeliveryStaffApp.number=dataList.length;

                    }
                });
            }

        },

        addAnyWaybill:function(){
            if(showAllDeliveryStaffApp.number<2){
                swal("失败","合并的运单数量必须大于等于2", "error");
                $("#table_list_2").trigger("reloadGrid");
                $('#myModal').modal('hide');
                return null;
            }


            console.log("!!!!!!");
            console.log(showAllDeliveryStaffApp.datas);
            let datas=showAllDeliveryStaffApp.datas;
            let data =
                {
                    'dispatchStaff':showAllDeliveryStaffApp.staffName,
                    'waybillIds':showAllDeliveryStaffApp.anyWaybillId,
                    'operator':datas.operator,
                    'operateBranch':datas.operateBranch,


                };
            console.log(data);

            $.ajax({
                url: "/getUpdateAnyDeliveryWaybillById",
                type: "POST",
                data: data,
                success: function (data) {
                    console.log(data);
                    if (data.state == 2000) {
                        successalert("", "指定派件员完成！！");
                        showAllDeliveryStaffApp.staffNameList=null;
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


