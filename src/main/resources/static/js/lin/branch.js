$(function () {  //页面加载完后执行
    $("#loading").show();  //如果被选元素已被隐藏，则显示这些元素：（显示图片）


    showBranchApp.showBranch();


    buildgrid();
    $.jgrid.defaults.styleUI = 'Bootstrap';  //制定使用bootstrap的版本的样式

    $("#loading").hide();


});


//--------------------------------------------------------------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------------------------------------------------------------

//制作表格
function buildgrid() {
    $("#table_list_2").jqGrid(
        {
            url: "/getAllBranchJson",//获取数据的地址
            datatype: "json",//返回的数据类型
            mtype: "POST", //提交方式
            autowidth: true, //如果为ture时，则当表格在首次被创建时会根据父元素比例重新调整
            // 表格宽度。

            rownumbers: true, //如果为ture则会在表格左边新增一列，显示行顺序号，从1开始递增。
            jsonReader: {  //	描述json 数据格式的数组
                root: "dataList",
                page: "currentPage",
                total: "totalPage",          //   很重要 定义了 后台分页参数的名字。
                records: "totalCount"
            },
            width: "100%",
            height: "100%",
            rowNum: 15,  //在grid上显示记录条数，这个参数是要被传递到后台
            // rowList: [10, 20, 30], //	一个下拉选择框，用来改变显示记录数
            colNames: ['网点编号', '网点名称', '审核情况', '操作'],//显示字段名
            colModel: [  //这个参数指定了jqGrid各列的具体格式
                {
                    name: 'branchId',  //指定对应数据中属性名
                    index: 'branchId',  //用于列排序
                },
                {
                    name: 'branchName',
                    index: 'branchName',
                    editable: true,


                },
                {
                    name: 'examineStr',
                    index: 'examineStr',
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
            caption: "网点列表",  //这个参数制订了jqGrid的标题
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
                    var branchId = parseInt($("#table_list_2").jqGrid("getCell", id, "branchId"));  //获取单元格中的数据
                    var modify = "<button type='button' class='layui-btn layui-btn-warm layui-btn-xs' data-toggle='modal' data-target='#myModal' onclick='changedialogshow(" + branchId + ")'>编辑</button>";  //这里的onclick就是调用了上面的javascript函数 Modify(id)
                    var select = "<button type='button' class='layui-btn  layui-btn-xs' data-toggle='modal' data-target='#showMyModal' onclick='branchAllShow(" + branchId + ")'>详情</button>";
                    var result = $("#table_list_2").jqGrid("setRowData", id, {handle: modify + "&nbsp;" + select});
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

//省级联动
//--------------------------------------------------------------------------------------------------------------------------------------------------

Vue.component('v-select', VueSelect.VueSelect);
let showBranchApp = new Vue({
    el: '#branchForm',
    data: {
        branchId: null,
        provincial: null,
        city: null,
        town:null,
        district: null,
        branchAddress: null,
        branchName: null,
        branchPhone: null,
        remark: null,
        branchLevel: null,
        suosuBranch: null,
        provinciallist: [
            {label: '福建省福州市台江区xxxx', value: 1},
            {label: '福建省莆田市荔城区xxxx', value: 2},
            {label: '福建省泉州市丰泽区xxxx', value: 3},
            {label: '福建省龙岩市漳平市xxxx', value: 4}
        ],
        townlist:[],
        citylist: [],
        districtlist: [],
        branchLevelList: [
            {label: '一级网点', value: 1},
            {label: '二级网点', value: 2},
            {label: '三级网点', value: 3}
        ],
        suosuBranchList: [],

    },
    methods: {
        showBranch: function () {
            $.ajax({
                url: '/showProvincialAll',
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    console.log("111111111");
                    let list = data.data;
                    console.log(data.data)
                    let provinciallist = [];
                    for (let i = 0; i < list.length; i++) {
                        let op = {};
                        op.label = list[i].criName;
                        op.value = list[i].criCode;
                        provinciallist[i] = op;
                    }

                    showBranchApp.provinciallist = provinciallist;
                }
            });
        },
        provincialChange: function () {
            showBranchApp.city = null;
            showBranchApp.town = null;
            showBranchApp.district = null;
            showBranchApp.branchLevel = null;
            showBranchApp.suosuBranch = null;
            console.log("省级内容为：" + showBranchApp.provincial)
            $.ajax({
                url: '/showCityAll',
                type: 'post',
                data: "provincial=" + showBranchApp.provincial,
                dataType: 'json',
                success: function (data) {
                    console.log("111111111");
                    let list = data.data;
                    console.log(data.data)
                    let citylist = [];
                    for (let i = 0; i < list.length; i++) {
                        let op = {};
                        op.label = list[i].criName;
                        op.value = list[i].criCode;
                        citylist[i] = op;
                    }

                    showBranchApp.citylist = citylist;
                }
            });
        },
        cityChange: function () {
            showBranchApp.town = null;
            showBranchApp.district = null;
            showBranchApp.branchLevel = null;
            showBranchApp.suosuBranch = null;
            console.log("市级内容为：" + showBranchApp.city)
            $.ajax({
                url: '/showDistrictAll',
                type: 'post',
                data: "city=" + showBranchApp.city,
                dataType: 'json',
                success: function (data) {
                    console.log("111111111");
                    let list = data.data;
                    console.log(data.data)
                    let districtlist = [];
                    for (let i = 0; i < list.length; i++) {
                        let op = {};
                        op.label = list[i].criName;
                        op.value = list[i].criCode;
                        districtlist[i] = op;
                    }

                    showBranchApp.districtlist = districtlist;
                }
            });
        },
        districtChange: function () {
            showBranchApp.town = null;
            console.log("市级内容为：" + showBranchApp.district)
            $.ajax({
                url: '/showTownAll',
                type: 'post',
                data: "district=" + showBranchApp.district,
                dataType: 'json',
                success: function (data) {
                    console.log("111111111");
                    let list = data.data;
                    console.log(data.data)
                    let townlist = [];
                    for (let i = 0; i < list.length; i++) {
                        let op = {};
                        op.label = list[i].criName;
                        op.value = list[i].criCode;
                        townlist[i] = op;
                    }

                    showBranchApp.townlist = townlist;
                }
            });
        },
        branchLevelChange: function () {
            showBranchApp.suosuBranch = null;
            if (showBranchApp.provincial==null||showBranchApp.city==null||showBranchApp.district==null){
                swal("错误", "请先选择省、市、县!", "error");
                return false;
            }
            console.log("网点等级：" + showBranchApp.branchLevel)
            let data={
                "branchLevel":showBranchApp.branchLevel,
                'branchProvince': method(showBranchApp.provinciallist, showBranchApp.provincial),
                'branchCity': method(showBranchApp.citylist, showBranchApp.city),
                'branchCounty': method(showBranchApp.districtlist, showBranchApp.district),
            }
            $.ajax({
                url: '/showBranchByLevel',
                type: 'post',
                data: data,
                dataType: 'json',
                success: function (data) {
                    console.log("111111111");
                    let list = data.data;
                    console.log(data.data)
                    let suosuBranchList = [];
                    for (let i = 0; i < list.length; i++) {
                        let op = {};
                        op.label = list[i].branchName;
                        op.value = list[i].branchId;
                        suosuBranchList[i] = op;
                    }

                    showBranchApp.suosuBranchList = suosuBranchList;
                }
            });

        },
        //添加网点
        addBranch: function () {
            if (showBranchApp.provincial==null||showBranchApp.city==null||showBranchApp.district==null){
            swal("错误", "请先选择省、市!", "error");
            return false;
        }
            if(showBranchApp.branchLevel!=1&&showBranchApp.suosuBranch==null){
                swal("错误", "所属网点不能为空！", "error");
                return false;
            }
            let data =
                {
                    'branchId': null,
                    'branchProvince': method(showBranchApp.provinciallist, showBranchApp.provincial),
                    'branchCity': method(showBranchApp.citylist, showBranchApp.city),
                    'branchCounty': method(showBranchApp.districtlist, showBranchApp.district),
                    'branchTown': method(showBranchApp.townlist, showBranchApp.town),
                    'branchAddress': showBranchApp.branchAddress,
                    'branchName': showBranchApp.branchName,
                    'branchPhone': showBranchApp.branchPhone,
                    'remark': showBranchApp.remark,
                    'branchLevel': showBranchApp.branchLevel,
                    'suosuBranch': method(showBranchApp.suosuBranchList, showBranchApp.suosuBranch)
                };
            console.log(data);

            $.ajax({
                url: "/addBranch",
                type: "POST",
                data: data,
                success: function (data) {
                    console.log(data);
                    if (data.state == 2000) {
                        successalert("", "网点添加完成");
                        $("#table_list_2").trigger("reloadGrid");
                    } else {
                        errorsalert("", data.message);
                    }

                },
                error: function () {
                    errorsalert("", data.message);
                }
            });

        },
    },

});

function method(list, value) {
    var label;
    for (var i = 0; i < list.length; i++) {
        if (list[i].value == value) {
            label = list[i].label;
        }
    }
    return label;

}



//--------------------------------------------------------------------------------------------------------------------------------------------------

let showUpdateBranchApp = new Vue({
    el: '#updateBranchForm',
    data: {
        branchId: null,
        provincial: null,
        city: null,
        district: null,
        town:null,
        branchAddress: null,
        branchName: null,
        branchPhone: null,
        remark: null,
        branchLevel: null,
        suosuBranch: null,

        provinciallist: [
            {label: '福建省福州市台江区xxxx', value: 1},
            {label: '福建省莆田市荔城区xxxx', value: 2},
            {label: '福建省泉州市丰泽区xxxx', value: 3},
            {label: '福建省龙岩市漳平市xxxx', value: 4}
        ],
        citylist: [],
        townlist:[],
        districtlist: [],
        branchLevelList: [
            {label: '一级网点', value: 1},
            {label: '二级网点', value: 2},
            {label: '三级网点', value: 3}
        ],
        suosuBranchList: [],

    },
    methods: {

        showBranch: function () {
            showUpdateBranchApp.suosuBranchList = [];
            console.log("？？？？？");
            $.ajax({
                url: '/showProvincialAll',
                type: 'post',
                dataType: 'json',
                success: function (data) {

                    let list = data.data;
                    console.log(data.data)
                    let provinciallist = [];
                    for (let i = 0; i < list.length; i++) {
                        let op = {};
                        op.label = list[i].criName;
                        op.value = list[i].criCode;
                        provinciallist[i] = op;
                    }

                    showUpdateBranchApp.provinciallist = provinciallist;
                }
            });
        },
        provincialChange: function () {
            showUpdateBranchApp.city = null;
            showUpdateBranchApp.town = null;
            showUpdateBranchApp.district = null;
            showUpdateBranchApp.branchLevel = null;
            showUpdateBranchApp.suosuBranch = null;
            console.log("省级内容为：" + showUpdateBranchApp.provincial)
            $.ajax({
                url: '/showCityAll',
                type: 'post',
                data: "provincial=" + showUpdateBranchApp.provincial,
                dataType: 'json',
                success: function (data) {
                    console.log("111111111");
                    let list = data.data;
                    console.log(data.data)
                    let citylist = [];
                    for (let i = 0; i < list.length; i++) {
                        let op = {};
                        op.label = list[i].criName;
                        op.value = list[i].criCode;
                        citylist[i] = op;
                    }

                    showUpdateBranchApp.citylist = citylist;
                }
            });
        },
        cityChange: function () {
            showUpdateBranchApp.town = null;
            showUpdateBranchApp.district = null;
            showUpdateBranchApp.branchLevel = null;
            showUpdateBranchApp.suosuBranch = null;
            console.log("市级内容为：" + showUpdateBranchApp.city)
            $.ajax({
                url: '/showDistrictAll',
                type: 'post',
                data: "city=" + showUpdateBranchApp.city,
                dataType: 'json',
                success: function (data) {
                    console.log("111111111");
                    let list = data.data;
                    console.log(data.data)
                    let districtlist = [];
                    for (let i = 0; i < list.length; i++) {
                        let op = {};
                        op.label = list[i].criName;
                        op.value = list[i].criCode;
                        districtlist[i] = op;
                    }

                    showUpdateBranchApp.districtlist = districtlist;
                }
            });
        },
        districtChange: function () {
            showUpdateBranchApp.town = null;
            console.log("市级内容为：" + showUpdateBranchApp.district)
            $.ajax({
                url: '/showTownAll',
                type: 'post',
                data: "district=" + showUpdateBranchApp.district,
                dataType: 'json',
                success: function (data) {
                    console.log("111111111");
                    let list = data.data;
                    console.log(data.data)
                    let townlist = [];
                    for (let i = 0; i < list.length; i++) {
                        let op = {};
                        op.label = list[i].criName;
                        op.value = list[i].criCode;
                        townlist[i] = op;
                    }

                    showUpdateBranchApp.townlist = townlist;
                }
            });
        },
        branchLevelChange: function () {
            showUpdateBranchApp.suosuBranch = null;
            if (showUpdateBranchApp.provincial==null||showUpdateBranchApp.city==null||showUpdateBranchApp.district==null){
                swal("错误", "请先选择省、市!", "error");
                return false;
            }
            var branchProvince="";
            var branchCity="";
            var branchCounty="";

            if(isNaN(parseInt(showUpdateBranchApp.provincial))){
                branchProvince=showUpdateBranchApp.provincial;
                branchCity=showUpdateBranchApp.city;
                branchCounty=showUpdateBranchApp.district;
            }else{
                branchProvince=method(showUpdateBranchApp.provinciallist, showUpdateBranchApp.provincial);
                branchCity=method(showUpdateBranchApp.citylist, showUpdateBranchApp.city);
                branchCounty=method(showUpdateBranchApp.districtlist, showUpdateBranchApp.district);
            }
            console.log(branchProvince+":"+branchCity+":"+branchCounty);
            let data={
                "branchLevel":showUpdateBranchApp.branchLevel,
                'branchProvince': branchProvince,
                'branchCity': branchCity,
                'branchCounty': branchCounty,
                "branchId":showUpdateBranchApp.branchId,
            }
            console.log(data);
            $.ajax({
                url: '/showBranchByLevel',
                type: 'post',
                data: data,
                dataType: 'json',
                success: function (data) {
                    console.log("111111111");
                    let list = data.data;
                    console.log(data.data)
                    let suosuBranchList = [];
                    for (let i = 0; i < list.length; i++) {
                        let op = {};
                        op.label = list[i].branchName;
                        op.value = list[i].branchId;
                        suosuBranchList[i] = op;
                    }

                    showUpdateBranchApp.suosuBranchList = suosuBranchList;
                }
            });

        },
        //编辑回显
        showUpdateBranch: function (branchId) {

            $.ajax({

                url: '/showAnyBranch',
                type: 'post',
                dataType: 'json',
                data: "branchId=" + branchId,
                success: function (data) {
                    console.log("！！！！");
                    let list = data.data;
                    console.log(data.data)
                    let branchLevelList = ['一级网点', '二级网点', '三级网点'];
                    for (let i = 0; i < list.length; i++) {
                        showUpdateBranchApp.branchId = list[i].branchId;
                        showUpdateBranchApp.provincial =list[i].branchProvince;
                        showUpdateBranchApp.city = list[i].branchCity;
                        showUpdateBranchApp.district = list[i].branchCounty;
                        showUpdateBranchApp.town = list[i].branchTown;
                        showUpdateBranchApp.branchAddress = list[i].branchAddress;
                        showUpdateBranchApp.branchName = list[i].branchName;
                        showUpdateBranchApp.branchPhone = list[i].branchPhone;
                        showUpdateBranchApp.branchLevel = branchLevelList[list[i].branchLevel-1];
                        showUpdateBranchApp.suosuBranch = list[i].suosuBranch;
                        showUpdateBranchApp.remark = list[i].remark;



                    }

                }
            });
        },
        updateBranch: function () {
            if (showUpdateBranchApp.provincial==null||showUpdateBranchApp.city==null||showUpdateBranchApp.district==null){
                swal("错误", "请先选择省、市!", "error");
                return false;
            }
            let branchLevelList = ['一级网点', '二级网点', '三级网点'];
            console.log("网点等级");
            console.log(showUpdateBranchApp.branchLevel);
            var  branchLevel ;
            if(isNaN(parseInt(showUpdateBranchApp.branchLevel))){
                branchLevel=(branchLevelList.indexOf(showUpdateBranchApp.branchLevel))+1;
            }else{
                branchLevel=showUpdateBranchApp.branchLevel;
            }


            if(branchLevel!=1&&showUpdateBranchApp.suosuBranch==null){
                swal("错误", "所属网点不能为空！", "error");
                return false;
            }

            var branchProvince="";
            var branchCity="";
            var branchCounty="";
            var branchTown="";
            var suosuBranch="";
            console.log("修改后网点数据");
            console.log(showUpdateBranchApp.provincial);
            console.log(showUpdateBranchApp.suosuBranch);

            if(isNaN(parseInt(showUpdateBranchApp.provincial))){
                 branchProvince=showUpdateBranchApp.provincial;
                 branchCity=showUpdateBranchApp.city;
                 branchCounty=showUpdateBranchApp.district;
                 branchTown=showUpdateBranchApp.town;
            }else{
                branchProvince=method(showUpdateBranchApp.provinciallist, showUpdateBranchApp.provincial);
                branchCity=method(showUpdateBranchApp.citylist, showUpdateBranchApp.city);
                branchCounty=method(showUpdateBranchApp.districtlist, showUpdateBranchApp.district);
                branchTown=method(showUpdateBranchApp.townlist, showUpdateBranchApp.town);
            }
            if(isNaN(parseInt(showUpdateBranchApp.suosuBranch))){
                suosuBranch=showUpdateBranchApp.suosuBranch;
            }else{
                suosuBranch=method(showUpdateBranchApp.suosuBranchList, showUpdateBranchApp.suosuBranch);
            }



            let data =
                {
                    'branchId': showUpdateBranchApp.branchId,
                    'branchName': showUpdateBranchApp.branchName,
                    'branchProvince': branchProvince,
                    'branchCity': branchCity,
                    'branchCounty': branchCounty,
                    'branchTown': branchTown,
                    'branchAddress': showUpdateBranchApp.branchAddress,
                    'branchPhone': showUpdateBranchApp.branchPhone,
                    'branchLevel': branchLevel,
                    'suosuBranch': suosuBranch,
                    'examine': 1,
                    'remark': showUpdateBranchApp.remark
                };
            console.log(data);

            $.ajax({
                url: "/updateBranch",
                type: "POST",
                data: data,
                success: function (data) {
                    console.log(data);
                    if (data.state == 2000) {
                        successalert("", "网点信息修改完成");
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


//--------------------------------------------------------------------------------------------------------------------------------------------------
//
function changedialogshow(branchId) {  // 修改信息的方法

    showUpdateBranchApp.showBranch();
    showUpdateBranchApp.showUpdateBranch(branchId);



}


//--------------------------------------------------------------------------------------------------------------------------------------------------


function branchAllShow(branchId) {

    showAllBranchApp.showAllBranch(branchId);

}


//-------------------------------------------------------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------------------------------------------------------


// //----------------------------------------------------------------------------------------------------
//创建Vue对象
let showAllBranchApp = new Vue({
    el: '#showAllBranchForm',
    data: {

        showBranchId: null,
        showBranchName: null,
        showBranchAddress: null,
        showExamine: null,
        showBranchManager: null,
        showBranchPhone: null,
        showBranchLevel: null,
        showBranchTime: null,
        showSuosuBranch: null,
        showRemark: null,

    },

    methods: {
        showAllBranch: function (branchId) {
            $.ajax({

                url: '/showAnyBranch',
                type: 'post',
                dataType: 'json',
                data: "branchId=" + branchId,
                success: function (data) {
                    console.log("！！！！");
                    let list = data.data;
                    console.log(data.data)
                    let branchLevelList = ['一级网点', '二级网点', '三级网点'];
                    let examineList = ['待审核', '审核成功', '审核失败'];
                    for (let i = 0; i < list.length; i++) {
                        showAllBranchApp.showBranchId = list[i].branchId;
                        showAllBranchApp.showBranchName = list[i].branchName;
                        showAllBranchApp.showBranchAddress = list[i].branchAddress;
                        showAllBranchApp.showExamine = examineList[list[i].examine - 1];
                        showAllBranchApp.showBranchManager = list[i].branchManager;
                        showAllBranchApp.showBranchPhone = list[i].branchPhone;
                        showAllBranchApp.showBranchLevel = branchLevelList[list[i].branchLevel - 1];
                        showAllBranchApp.showBranchTime = list[i].branchTime;
                        showAllBranchApp.showSuosuBranch = list[i].suosuBranch;
                        showAllBranchApp.showRemark = list[i].remark;

                    }

                }
            });
        }


    }

});





