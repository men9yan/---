$(function () {  //页面加载完后执行
    $("#loading").show();  //如果被选元素已被隐藏，则显示这些元素：（显示图片）


    $("#table_list_2").trigger("reloadGrid");





    buildgrid();
    $.jgrid.defaults.styleUI = 'Bootstrap';  //制定使用bootstrap的版本的样式

    $("#loading").hide();


    //执行添加方法
    $("#addSubmit").click(function () {
        addIndentModify();

    });

});


//--------------------------------------------------------------------------------------------------------------------------------------------------

//添加信息功能
function addIndentModify() {  //添加信息的方法

    $.ajax({
        url: "/addIndent",
        type: "POST",
        data: $("#addform").serialize(),
        success: function (data) {
            console.log(data);
            if (data.state == 2000) {
                successalert("", "订单创建成功");
                $("#table_list_2").trigger("reloadGrid");
                $('#myModal2').modal('hide');

            } else {
                errorsalert("", data.message);
            }

        },
        error: function () {
            errorsalert("", data.message);
        }
    });
}


//--------------------------------------------------------------------------------------------------------------------------------------------------


//制作表格
function buildgrid() {
    $("#table_list_2").jqGrid(
        {
            url: "/getAllIndentJson",//获取数据的地址
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
            colNames: ['订单编号', '寄件人姓名', '寄件人电话', '寄件人省份','寄件地级市','寄件县级市', '寄件乡镇', '寄件人住址', '收件人姓名','收件人电话','收件省份','收件地级市','收件县级市', '收件乡镇', '收件住址', '当前所在网点','规模大小','状态','创建时间','当前所在仓库'],//显示字段名
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
                }


            ],
            pager: "#pager_list_2",  //这个参数指定了jqGrid页脚显示位置。
            viewrecords: true,  //这个参数设置了是否显示所有记录的总数。
            caption: "订单查询列表",  //这个参数制订了jqGrid的标题
            add: false,   //开启添加功能
            edit: false,
            hidegrid: false, //启用或者禁用控制表格显示、隐藏的按钮

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
            };
            console.log(data);
            $("#table_list_2").jqGrid('clearGridData');
            $("#table_list_2").setGridParam(
                {
                    url:"/getFindAllByWaybillIdJson",
                    postData:data
                }
            ).trigger("reloadGrid");


        },


    }
})
