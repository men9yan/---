$(function () {  //页面加载完后执行
    $("#loading").show();  //如果被选元素已被隐藏，则显示这些元素：（显示图片）


    $("#table_list_2").trigger("reloadGrid");



    buildgrid();

    showBranchApp.showStaff();



    $.jgrid.defaults.styleUI = 'Bootstrap';  //制定使用bootstrap的版本的样式

    $("#loading").hide();


});


//--------------------------------------------------------------------------------------------------------------------------------------------------


//制作表格
function buildgrid() {
    $("#table_list_2").jqGrid(
        {
            url: "/getFindDeliveryWaybillALl",//获取数据的地址
            datatype: "json",//返回的数据类型
            mtype: "POST", //提交方式
            autowidth: true, //如果为ture时，则当表格在首次被创建时会根据父元素比例重新调整
            // 表格宽度。

            rownumbers: true, //如果为ture则会在表格左边新增一列，显示行顺序号，从1开始递增。
            jsonReader: {  //  描述json 数据格式的数组
                root: "dataList",
                page: "currentPage",
                total: "totalPage",          //   很重要 定义了 后台分页参数的名字。
                records: "totalCount"
            },
            width:"100%",
            height:"100%",
            rowNum: 10,  //在grid上显示记录条数，这个参数是要被传递到后台
            // rowList: [10, 20, 30], //  一个下拉选择框，用来改变显示记录数
            colNames: ['运单编号', '派件员','派件员', '扫描时间', '签收状态',  '操作'],//显示字段名
            colModel: [  //这个参数指定了jqGrid各列的具体格式
                {
                    name: 'waybillId',  //指定对应数据中属性名
                    index: 'waybillId',  //用于列排序
                },
                {
                    name: 'dispatchStaff',
                    index: 'dispatchStaff',
                    hidden:true

                },
                {
                    name: 'dispatchStaffStr',
                    index: 'dispatchStaffStr',


                },
                {
                    name: 'operateTime',
                    index: 'operateTime',

                },

                {
                    name: 'waybillStateStr',
                    index: 'waybillStateStr',
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
            edit: false,
            hidegrid: false, //启用或者禁用控制表格显示、隐藏的按钮
            gridComplete: function () {
                console.log("grid Complete");
                var ids = $("#table_list_2").jqGrid("getDataIDs");
                console.log("ids--->{}", ids)
                for (var int = 0; int < ids.length; int++) {
                    var id = ids[int];
                    var modify = "<button type='button' class='layui-btn layui-btn-xs' data-toggle='modal' data-target='#myModal'  onclick='staffAnyShow(" + id+")'>签收确认</button>";  //这里的onclick就是调用了上面的javascript函数 Modify(id)
                    var result = $("#table_list_2").jqGrid("setRowData", id, {handle: modify  });
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

//创建Vue对象
Vue.component('v-select',VueSelect.VueSelect);
let showBranchApp = new Vue({
    el:'#app',
    data:{

        waybillId:null,
        branchManager:null,
        branchManagerlist:[
            {  label: '福建省福州市台江区xxxx',value:1 },
            {  label: '福建省莆田市荔城区xxxx',value:2 },
            {  label: '福建省泉州市丰泽区xxxx',value:3 },
            {  label: '福建省龙岩市漳平市xxxx',value:4 }
        ],


    },

    methods:{
        showStaff:function(){

            $.ajax({
                url:'getStaffAll',
                type:'post',
                dataType:'json',
                success:function(data){
                    console.log("111111111");
                    let list=data.data;
                    console.log(data.data)
                    let branchManagerlist = [];
                    for(let i=0;i<list.length;i++){
                        let  op ={};
                        op.label = list[i].staffName;
                        op.value = list[i].staffId;
                        branchManagerlist[i] = op;
                    }
                    showBranchApp.branchManagerlist=branchManagerlist;
                    showBranchApp.branchManager=null;
                }
            });
        },



        findMethod : function(){
            let data = {
                'waybillId':showBranchApp.waybillId,
                'dispatchStaff':showBranchApp.branchManager,

            };
            console.log(data);
            $("#table_list_2").jqGrid('clearGridData');
            $("#table_list_2").setGridParam(
                {
                    url:"/FindAssignWaybillALl",
                    postData:data
                }
            ).trigger("reloadGrid");


        },




    }

});



//--------------------------------------------------------------------------------------------------------------------------------------------------

function staffAnyShow(id) {  // 修改信息的方法
    var datas = $("#table_list_2").jqGrid("getRowData", id);//获取单行数据
    showWaybillApp.showWaybillId(datas);
}


//*************************************************************************************************************************************************
//创建Vue对象
Vue.component('v-select',VueSelect.VueSelect);
let showWaybillApp = new Vue({
    el:'#changeform',
    data:{

        waybillId:null,
        signer:null,
        dispatchStaff:null

    },

    methods:{
        showWaybillId:function(datas){
            showWaybillApp.waybillId=datas.waybillId;
            showWaybillApp.dispatchStaff=datas.dispatchStaff;
            showWaybillApp.signer=null;
        },
        updateWaybill:function(){
            let data={
                'waybillId':showWaybillApp.waybillId,
                'signer':showWaybillApp.signer,
                'dispatchStaff':showWaybillApp.dispatchStaff
            }

            $.ajax({
                url: "/getUpdateWayBillById",
                type: "POST",
                data: data,
                success: function (data) {
                    console.log(data);
                    if (data.state == 2000) {
                        successalert("", "签收完成！！");
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



//--------------------------------------------------------------------------------------------------------------------------------

