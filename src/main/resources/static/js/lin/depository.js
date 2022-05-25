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
            url: "/getDepository",//获取数据的地址
            datatype: "json",//返回的数据类型
            mtype: "POST", //提交方式
            height: "100%",
            width: "100%",
            autowidth:   true, //如果为ture时，则当表格在首次被创建时会根据父元素比例重新调整
            // 表格宽度。
            rownumbers: true,//如果为ture则会在表格左边新增一列，显示行顺序号，从1开始递增。
            jsonReader: {  //	描述json 数据格式的数组
                root: "dataList",
                page: "currentPage",
                total: "totalPage",          //   很重要 定义了 后台分页参数的名字。
                records: "totalCount"
            },

            rowNum: 10,  //在grid上显示记录条数，这个参数是要被传递到后台
            // rowList: [10, 20, 30], //	一个下拉选择框，用来改变显示记录数
            colNames: ['仓库编号','仓库名字', '地址', '所属网点', '仓库负责人','电话','温度（°）','湿度','操作'],//显示字段名
            colModel: [
                {
                    name: 'id',  //指定对应数据中属性名
                    index: 'id',  //用于列排序
                    editable: true,

                },
                {
                    name: 'depositoryName',  //指定对应数据中属性名
                    index: 'depositoryName',  //用于列排序
                    editable: true,

                },
                {
                    name: 'address',
                    index: 'address',
                    editable: true,

                },
                {
                    name: 'branch',
                    index: 'branch',
                    editable: true,
                },

                {
                    name: 'depositoryManager',
                    index: 'depositoryManager',
                    editable: true,
                },
                {
                    name: 'phone',
                    index: 'phone',
                    editable: true,
                },
                {
                    name: 'temperature',
                    index: 'temperature',
                    editable: true,
                },
                {
                    name: 'humidity',  //指定对应数据中属性名
                    index: 'humidity',  //用于列排序
                    editable: true,

                },
                {
                    name: 'handle',
                    index: 'handle',
                    sortable: false
                }


            ],
            pager: "#pager_list_2",  //这个参数指定了jqGrid页脚显示位置。
            viewrecords: true,  //这个参数设置了是否显示所有记录的总数。
            caption: "仓库列表",  //这个参数制订了jqGrid的标题
            add: false,   //开启添加功能
            edit: false,
            hidegrid: false, //启用或者禁用控制表格显示、隐藏的按钮
            gridComplete: function () {
                console.log("grid Complete");
                // var id=$('#table_list_2').jqGrid('getGridParam','selrow');
                var ids = $("#table_list_2").jqGrid("getDataIDs");
                console.log("ids--->{}", ids)
                for (var int = 0; int < ids.length; int++) {
                    var id = ids[int];
                    var modify = "<button type='button' class='layui-btn layui-btn-xs' data-toggle='modal' data-target='#myModal' onclick='changeBranchshow(" +id+")'>修改温湿度</button>";  //这里的onclick就是调用了上面的javascript函数 Modify(id)
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


function changeBranchshow(id) {  // 修改信息的方法
    var idD = parseInt($("#table_list_2").jqGrid("getCell", id, "id"));  //获取单元格中的数据
    var temperature = $("#table_list_2").jqGrid("getCell", id, "temperature");
    var humidity = $("#table_list_2").jqGrid("getCell", id, "humidity");
    showDepositoryApp.addId(idD);
}

//--------------------------------------------------------------------------------------------------------------------------------------------------

//创建Vue对象
Vue.component('v-select',VueSelect.VueSelect);
let showDepositoryApp = new Vue({
    el:'#changeform',
    data:{
        id: null,
        temperature:null,
        humidity:null,

    },

    methods:{
        addId:function(id){
            showDepositoryApp.id = id;
        },



        updateData : function(){
            let data = {
                'id':showDepositoryApp.id,
                'temperature':showDepositoryApp.temperature,
                'humidity':showDepositoryApp.humidity,

            };
            console.log(data);
            $.ajax({
                url: "/updateData",
                type: "POST",
                data: data,
                success: function (data) {

                    console.log(data);
                    if (data.state == 2000) {
                        successalert("", "温湿度更新完成");
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


        },




    }

});
