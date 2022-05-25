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
            url: "/getIndentAnyJson",//获取数据的地址
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

            rowNum: 15,  //在grid上显示记录条数，这个参数是要被传递到后台
            rowList: [15, 25, 35], //	一个下拉选择框，用来改变显示记录数
            colNames: ['订单编号', '寄件人姓名', '寄件人电话', '寄件人省份','寄件地级市','寄件县级市', '寄件乡镇', '寄件人住址', '收件人姓名','收件人电话','收件省份','收件地级市','收件县级市', '收件乡镇', '收件住址', '当前所在网点','规模大小','状态','创建时间','当前所在仓库','操作'],//显示字段名
            colModel: [
                {
                    name: 'indentId',  //指定对应数据中属性名
                    index: 'indentId',  //用于列排序
                    width: 150,

                },
                {
                    name: 'sender',
                    index: 'sender',
                    width: 150,

                },
                {
                    name: 'senderPhone',
                    index: 'senderPhone',
                    width: 150,
                },

                {
                    name: 'senderProvince',
                    index: 'senderProvince',
                    width: 150,
                },
                {
                    name: 'senderCity',
                    index: 'senderCity',
                    width: 150,
                },
                {
                    name: 'senderCounty',  //指定对应数据中属性名
                    index: 'senderCounty',  //用于列排序
                    width: 150,

                },
                {
                    name: 'senderTown',
                    index: 'senderTown',
                    width: 150,

                },


                {
                    name: 'senderAddress',
                    index: 'senderAddress',
                    width: 150,
                },

                {
                    name: 'consignor',
                    index: 'consignor',
                    width: 150,
                },
                {
                    name: 'consignorPhone',
                    index: 'consignorPhone',
                    width: 150,
                },
                {
                    name: 'consignorProvince',
                    index: 'consignorProvince',
                    width: 150,

                },
                {
                    name: 'consignorCity',
                    index: 'consignorCity',
                    width: 150,

                },


                {
                    name: 'consignorCounty',
                    index: 'consignorCounty',
                    width: 150,
                },

                {
                    name: 'consignorTown',
                    index: 'consignorTown',
                    width: 150,
                },
                {
                    name: 'consignorAddress',
                    index: 'consignorAddress',
                    width: 150,
                },
                {
                    name: 'currentBranch',  //指定对应数据中属性名
                    index: 'currentBranch',  //用于列排序
                    width: 150,

                },
                {
                    name: 'size',
                    index: 'size',
                    width: 150,

                },


                {
                    name: 'stateStr',
                    index: 'stateStr',
                    width: 150,
                },

                {
                    name: 'createTime',
                    index: 'createTime',
                    width: 150,
                },
                {
                    name: 'currentDepository',
                    index: 'currentDepository',
                    width: 150,
                },
                {
                    name: 'handle',
                    index: 'handle',
                    sortable: false,
                    width: 150,

                }


            ],
            pager: "#pager_list_2",  //这个参数指定了jqGrid页脚显示位置。
            viewrecords: true,  //这个参数设置了是否显示所有记录的总数。
            caption: "订单分配列表",  //这个参数制订了jqGrid的标题
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
                    var result = $("#table_list_2").jqGrid("setRowData", id, {handle: modify });
                }
            },
            onSelectRow: function (rowId, status, e) {
                var rowIds = jQuery("#table_list_2").jqGrid('getGridParam', 'selarrrow');
                console.log("多选内容：")
                console.log(rowIds)
                var dataList=[];
                for(var i=0;i<rowIds.length;i++){
                    var datas = $("#table_list_2").jqGrid("getRowData", rowIds[i]);//获取单行数据
                    console.log(datas)
                    dataList.push(datas);
                    console.log(dataList)
                }
                showAllocationStaffApp.showStaffName(dataList);
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
        indentId:null,
        sender:null,
        senderPhone:null
    },
    methods:{
        selectMethod : function(){
            console.log(isNaN(parseInt(app.indentId)));
            if(isNaN(parseInt(app.indentId))&&app.indentId!=null&&app.indentId!=""){
                swal("错误", "订单编号只能为数字!", "error");
                return false;
            }
            let data = {
                'indentId':app.indentId,
                'sender':app.sender,
                'senderPhone':app.senderPhone,
            };
            console.log(data);
            $("#table_list_2").jqGrid('clearGridData');
            $("#table_list_2").setGridParam(
                {
                    url:"/getFindAllByIndentIdJson",
                    postData:data
                }
            ).trigger("reloadGrid");


        },


    }
})

//--------------------------------------------------------------------------------------------------------------------------------------------------

function staffAnyShow(id) {  // 修改信息的方法
    var datas = $("#table_list_2").jqGrid("getRowData", id);//获取单行数据
    console.log("所在网点为:"+datas.currentBranch+",订单号:"+datas.indentId);
    showStaffByBranchApp.showStaffName(datas);
}


//*************************************************************************************************************************************************
//创建Vue对象
Vue.component('v-select',VueSelect.VueSelect);
let showStaffByBranchApp = new Vue({
    el:'#changeform',
    data:{

        indentId:null,
        staffName:null,
        staffNameList:[
            {  label: '福建省福州市台江区xxxx',value:1 },
            {  label: '福建省莆田市荔城区xxxx',value:2 },
            {  label: '福建省泉州市丰泽区xxxx',value:3 },
            {  label: '福建省龙岩市漳平市xxxx',value:4 }
        ],
        datas:null

    },

    methods:{
        showStaffName:function(datas){
            let data={
                'branch':datas.currentBranch
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
                    showStaffByBranchApp.staffNameList=staffNameList;
                    showStaffByBranchApp.indentId=datas.indentId;
                    showStaffByBranchApp.datas=datas;
                    showStaffByBranchApp.staffName=null;
                }
            });
        },

        addWaybill:function(){
            console.log("!!!!!!");
            console.log(showStaffByBranchApp.datas);
            let datas=showStaffByBranchApp.datas;
            let data =
                {
                    'indentId':datas.indentId,
                    'sender':datas.sender,
                    'senderPhone':datas.senderPhone,
                    'senderProvince':datas.senderProvince,
                    'senderCity':datas.senderCity,
                    'senderCounty':datas.senderCounty,
                    'senderTown':datas.senderTown,
                    'senderAddress':datas.senderAddress,
                    'consignor':datas.consignor,
                    'consignorPhone':datas.consignorPhone,
                    'consignorProvince':datas.consignorProvince,
                    'consignorCity':datas.consignorCity,
                    'consignorCounty':datas.consignorCounty,
                    'consignorTown':datas.consignorTown,
                    'consignorAddress':datas.consignorAddress,
                    'currentBranch':datas.currentBranch,
                    'size':datas.size,
                    'stateStr':datas.stateStr,
                    'createTime':datas.createTime,
                    'currentDepository':datas.currentDepository,
                    'staffName':method(showStaffByBranchApp.staffNameList,showStaffByBranchApp.staffName),
                    'staffId':showStaffByBranchApp.staffName
                };
            console.log(data);

            $.ajax({
                url: "/insertWayBillByParam",
                type: "POST",
                data: data,
                success: function (data) {
                    console.log(data);
                    if (data.state == 2000) {
                        successalert("", "员工分配完成！！");
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
let showAllocationStaffApp = new Vue({
    el:'#allocationStaffForm',
    data:{

        anyIndentId:null,
        staffName:null,
        staffNameList:[ ],
        datas:null,
        sumWeight:null,
        number:null

    },

    methods:{
        showStaffName:function(dataList){
            console.log("该数据是：");
            console.log(dataList);
            var indentIds="";
            var newIndentData="";
            var oldIndentData="";
            var sumWeight=0;
            for (var i=0;i<dataList.length;i++){
                indentIds+=dataList[i].indentId
                indentIds+=","
                sumWeight+=parseInt(dataList[i].size)
                newIndentData=dataList[i].sender+dataList[i].senderPhone
                    +dataList[i].senderProvince+dataList[i].senderCity+dataList[i].senderCounty
                    +dataList[i].senderTown+dataList[i].senderAddress+dataList[i].consignor
                    +dataList[i].consignorPhone+dataList[i].consignorProvince+dataList[i].consignorCity
                    +dataList[i].consignorCounty+dataList[i].consignorTown+dataList[i].consignorAddress
                    +dataList[i].currentBranch;
                if (oldIndentData!=""&&oldIndentData!=newIndentData){
                    indentIds="";
                    swal("失败","该订单不允许合并", "error");
                    $("#table_list_2").trigger("reloadGrid");
                }
                oldIndentData=newIndentData;
            }
            console.log("订单编号是：");
            console.log(indentIds)

            //判断数据是否相同
            console.log("选中数据是：");
            console.log(dataList)
            if(dataList.length==0){
                showAllocationStaffApp.anyIndentId=null;
            }else{
                let data={
                    'branch':dataList[0].currentBranch
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
                        showAllocationStaffApp.staffNameList=staffNameList;
                        showAllocationStaffApp.anyIndentId=indentIds;
                        showAllocationStaffApp.datas=dataList[0];
                        showAllocationStaffApp.staffName=null;
                        showAllocationStaffApp.sumWeight=sumWeight;
                        showAllocationStaffApp.number=dataList.length;
                    }
                });
            }

        },

        addAnyWaybill:function(){
            if(showAllocationStaffApp.number<2){
                swal("失败","合并的订单数量必须大于等于2", "error");
                $("#table_list_2").trigger("reloadGrid");
                $('#myModal').modal('hide');
                return null;
            }


            console.log("!!!!!!");
            console.log(showAllocationStaffApp.datas);
            let datas=showAllocationStaffApp.datas;
            let data =
                {
                    'indentId':parseInt(datas.indentId),
                    'sender':datas.sender,
                    'senderPhone':datas.senderPhone,
                    'senderProvince':datas.senderProvince,
                    'senderCity':datas.senderCity,
                    'senderCounty':datas.senderCounty,
                    'senderTown':datas.senderTown,
                    'senderAddress':datas.senderAddress,
                    'consignor':datas.consignor,
                    'consignorPhone':datas.consignorPhone,
                    'consignorProvince':datas.consignorProvince,
                    'consignorCity':datas.consignorCity,
                    'consignorCounty':datas.consignorCounty,
                    'consignorTown':datas.consignorTown,
                    'consignorAddress':datas.consignorAddress,
                    'currentBranch':datas.currentBranch,
                    'size':datas.size,
                    'stateStr':datas.stateStr,
                    'createTime':datas.createTime,
                    'currentDepository':datas.currentDepository,
                    'staffName':method(showAllocationStaffApp.staffNameList,showAllocationStaffApp.staffName),
                    'staffId':showAllocationStaffApp.staffName,
                    'number':showAllocationStaffApp.number,
                    'weight':showAllocationStaffApp.sumWeight,
                    'indentIdString':showAllocationStaffApp.anyIndentId

                };
            console.log(data);

            $.ajax({
                url: "/insertAnyWayBillByParam",
                type: "POST",
                data: data,
                success: function (data) {
                    console.log(data);
                    if (data.state == 2000) {
                        successalert("", "员工分配完成！！");
                        showAllocationStaffApp.staffNameList=null;
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

