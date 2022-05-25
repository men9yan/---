var iframe=window.parent.document.getElementById("selectSubmit");
iframe.onclick = function()  {  //页面加载完后执行
    $("#loading").show();  //如果被选元素已被隐藏，则显示这些元素：（显示图片）
    console.log("进入子窗体！！！");



    buildgrid();



    $.jgrid.defaults.styleUI = 'Bootstrap';  //制定使用bootstrap的版本的样式
    $("#loading").hide();


};


//--------------------------------------------------------------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------------------------------------------------------------

//制作表格
function buildgrid() {
    console.log("进入表格！！！");
    var nextBranch = $('#nextBranch', parent.document).val();
    let data = {
        'nextBranch': nextBranch
    };
    console.log(data)
    // $('#table_list_2').jqGrid('GridUnload');
    $.jgrid.gridUnload("#table_list_2");
    // $("#table_list_2").jqGrid('clearGridData');

    $("#table_list_2").jqGrid(
        {
            url: "/getVehicleByParamJson",//获取数据的地址
            datatype: "json",//返回的数据类型
            mtype: "POST", //提交方式
            postData: data,
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
            colNames: ['车辆编号', '车牌号', '车辆类型', '创建时间', '车辆状况', '车辆状态', '车辆载重','所属网点', '转运员', '转运员电话', '车主', '车辆净重','员工id', '车俩年限', '购车时间', '操作'],//显示字段名
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
                    name: 'staffId',
                    index: 'staffId',
                    hidden:true,
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
                    var modify = "<button type='button' style='margin: 0 5px 0 15px;' class='layui-btn layui-btn-warm layui-btn-xs' onclick='changedialogshow(" + id +  ")'>选择</button>";  //这里的onclick就是调用了上面的javascript函数 Modify(id)
                    var result = $("#table_list_2").jqGrid("setRowData", id, {handle: modify});
                }


            }
        });
    // $("#table_list_2").setGridParam(
    //     {
    //         url: "/getVehicleByParamJson",
    //         postData: data
    //         }
    //     ).trigger("reloadGrid");

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




function changedialogshow(id) {  // 修改信息的方法

    console.log("changedialogshow" + id);
    var datas = $("#table_list_2").jqGrid("getRowData", id);//获取单行数据

    console.log("?????->:{}", datas);

    parent.$('#vehicleNum').val(datas.vehicleNum);
    parent.$('#vehicleLoad').val(datas.vehicleLoad);
    parent.$('#vehicleWeight').val(datas.vehicleWeight);
    parent.$('#vehicleDriver').val(datas.vehicleDriver);

    //隐藏iframe
    parent.$('#layui-layer4').hide();

}




// 创建Vue对象
let app = new Vue({
    el: '#app',
    data: {
        vehicleId: null,

    },
    methods: {

        addMethod: function () {
            var nextBranch = $('#nextBranch', parent.document).val();
            let data = {
                'vehicleId': app.vehicleId,
                'nextBranch': nextBranch
            };
            console.log(data);
            $("#table_list_2").jqGrid('clearGridData');
            $("#table_list_2").setGridParam(
                {
                    url: "/findVehicleSearchByParam",
                    postData: data
                }
            ).trigger("reloadGrid");
        },

    }
})




