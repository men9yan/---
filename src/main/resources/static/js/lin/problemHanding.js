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
            url: "/getAllProblemJson",//获取数据的地址
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
            colNames: ['问题件编号', '运单编号', '问题描述', '接收网点','责任方', '电话', '登记网点', '登记人', '登记时间', '处理网点', '处理人', '备注', '处理时间', '问题件类型','操作'],//显示字段名
            colModel: [
                //这个参数指定了jqGrid各列的具体格式
                {
                    name: 'problemId',  //指定对应数据中属性名
                    index: 'problemId',  //用于列排序

                },
                {
                    name: 'waybillId',
                    index: 'waybillId',

                },


                {
                    name: 'problemDesc',
                    index: 'problemDesc',
                },

                {
                    name: 'receiveBranch',
                    index: 'receiveBranch',
                },
                {
                    name: 'responsibleParty',
                    index: 'responsibleParty',
                    hidden:true
                },
                {
                    name: 'phone',
                    index: 'phone',
                },

                {
                    name: 'registerBranch',
                    index: 'registerBranch',
                },
                {
                    name: 'registrant',
                    index: 'registrant',
                },
                {
                    name: 'registerTime',
                    index: 'registerTime',
                },
                {
                    name: 'handleBranch',
                    index: 'handleBranch',
                },
                {
                    name: 'handler',
                    index: 'handler',
                },
                {
                    name: 'remark',
                    index: 'remark',
                },
                {
                    name: 'handleTime',
                    index: 'handleTime',
                },
                {
                    name: 'ptName',
                    index: 'ptName',
                },
                {
                    name: 'handle',
                    index: 'handle',
                    sortable: false,
                }


            ],
            pager: "#pager_list_2",  //这个参数指定了jqGrid页脚显示位置。
            viewrecords: true,  //这个参数设置了是否显示所有记录的总数。
            caption: "问题件列表",  //这个参数制订了jqGrid的标题
            add: false,   //开启添加功能
            edit: false,
            hidegrid: false, //启用或者禁用控制表格显示、隐藏的按钮
            gridComplete: function () {
                console.log("grid Complete");
                var ids = $("#table_list_2").jqGrid("getDataIDs");
                console.log("ids--->{}", ids)
                for (var int = 0; int < ids.length; int++) {
                    var id = ids[int];
                    var problemModify = "<button   class='layui-btn layui-btn-warm layui-btn-xs'  onclick='changedialogshow(" + id + ")'>问题件处理</button>";
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
                    url:"/getAllProblemByIdJson",
                    postData:data
                }
            ).trigger("reloadGrid");


        },


    }
})

//--------------------------------------------------------------------------------------------------------------------------------------------------



//--------------------------------------------------------------------------------------------------------------------------------

//-----------------------------------------------------------------------------------------------------------------------------------------
function changedialogshow(id) {  // 修改信息的方法
    $('#problemMyModal').modal('show');
    var datas = $("#table_list_2").jqGrid("getRowData", id);//获取单行数据
    console.log(datas);
    console.log("运单号:"+datas.waybillId);
    updateProblemApp.showWaybillId(datas.waybillId);
}

//-----------------------------------------------------------------------------------------------------------------------------------------


//创建Vue对象
let updateProblemApp = new Vue({
    el:'#problemform',
    data:{

        waybillId:null,
        remark:null,

    },

    methods:{
        showWaybillId:function(waybillId){
            updateProblemApp.waybillId=waybillId;
        },

        updateProblem:function(){
            let data =
                {
                    'waybillId':updateProblemApp.waybillId,
                    'remark':updateProblemApp.remark,
                };
            console.log(data);

            $.ajax({
                url: "/updateProblem",
                type: "POST",
                data: data,
                success: function (data) {
                    console.log(data);
                    if (data.state == 2000) {
                        successalert("", "问题件处理完成！！");
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



