$(function () {  //页面加载完后执行
	$("#loading").show();  //如果被选元素已被隐藏，则显示这些元素：（显示图片）


	showRoleApp.showbranch();

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
			url: "/getAllStaffJsonByBranch",//获取数据的地址
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
			width:"100%",
			height:"100%",
			rowNum: 10,  //在grid上显示记录条数，这个参数是要被传递到后台
			rowList: [10, 20, 30], //	一个下拉选择框，用来改变显示记录数
			colNames: ['员工编号', '姓名', '性别', '电话', '身份证', '家庭地址', '是否离职', '职位', '所属网点', '操作'],//显示字段名
			colModel: [  //这个参数指定了jqGrid各列的具体格式
				{
					name: 'staffId',  //指定对应数据中属性名
					index: 'staffId',  //用于列排序
				},
				{
					name: 'staffName',
					index: 'staffName',
					editable: true,


				},
				{
					name: 'gender',
					index: 'gender',
					editable: true,

				},

				{
					name: 'phone',
					index: 'phone',
					editable: true
				},

				{
					name: 'idCard',
					index: 'idCard',
					editable: true,
				},

				{
					name: 'address',
					index: 'address',
					editable: true,
					sortable: false  //指定是否支持排序
				},

				{
					name: 'isdelete',
					index: 'isdelete',
					editable: true,
				},

				{
					name: 'position',
					index: 'position',
					editable: true
				},

				{
					name: 'branch',
					index: 'branch',
					editable: true
				},
				{
					name: 'handle',
					index: 'handle',
					sortable: false
				}
			],
			pager: "#pager_list_2",  //这个参数指定了jqGrid页脚显示位置。
			viewrecords: true,  //这个参数设置了是否显示所有记录的总数。
			caption: "员工列表",  //这个参数制订了jqGrid的标题
			add: false,   //开启添加功能
			edit: false,
			// addtext: 'Add',
			// editurl: "/addStaff",  //添加路径
			hidegrid: false, //启用或者禁用控制表格显示、隐藏的按钮
			gridComplete: function () {
				console.log("grid Complete");
				// var id=$('#table_list_2').jqGrid('getGridParam','selrow');
				var ids = $("#table_list_2").jqGrid("getDataIDs");
				console.log("ids--->{}", ids)
				for (var int = 0; int < ids.length; int++) {
					var id = ids[int];
					var StaffId = parseInt($("#table_list_2").jqGrid("getCell", id, "staffId"));  //获取单元格中的数据
					var modify = "<button type='button' class='layui-btn layui-btn-warm layui-btn-xs' data-toggle='modal' data-target='#myModal' onclick='changedialogshow(" + id + ","+StaffId+")'>编辑</button>";  //这里的onclick就是调用了上面的javascript函数 Modify(id)
					var del = "<button  type='button' class='layui-btn layui-btn-danger layui-btn-xs' onclick='deldialog(" + StaffId + ")' >删除</button>";
					var result = $("#table_list_2").jqGrid("setRowData", id, {handle: modify + "&nbsp" + del });
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

//删除信息的方法
//--------------------------------------------------------------------------------------------------------------------------------------------------

function deldialog(StaffId) {
	deletealert("", "确定要删除吗？", function () {
		console.log("StaffId-->{}", StaffId)
		del(StaffId)
	});
}


function del(StaffId) {
	console.log("删除的id："+StaffId);
	$.ajax({
		url: '/deleteStaff',
		type: "POST",
		data: 'StaffId=' + StaffId,
		success: function (data) {
			console.log(data);
			if (data.state == 2000) {
				$("#table_list_2").trigger("reloadGrid");
				successalert("", "删除用户完成");
			} else {
				errorsalert("", data.message);
			}
		},
		error: function () {
			errorsalert("", "出现错误，请重试");
		}
	});


}


//--------------------------------------------------------------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------------------------------------------------------------





function changedialogshow(id,StaffId) {  // 修改信息的方法
	showStaffApp.showbranch();
	showStaffApp.showStaff(id,StaffId);
}


//--------------------------------------------------------------------------------------------------------------------------------------------------






//--------------------------------------------------------------------------------------------------------------------------------------------------


//创建Vue对象
//搜索数据
Vue.component('v-select',VueSelect.VueSelect);
let app = new Vue({
	el:'#app',
	data:{
		staffid:null,
		staffname:null
	},
	methods:{
		addMethod : function(){
			console.log(isNaN(parseInt(app.staffid)));
			if(isNaN(parseInt(app.staffid))&&app.staffid!=null&&app.staffid!=""){
				swal("错误", "员工编号只能为数字!", "error");
				return false;
			}
			let data = {
				'staffid':app.staffid,
				'staffname':app.staffname,
			};
			console.log(data);
			$("#table_list_2").jqGrid('clearGridData');
			$("#table_list_2").setGridParam(
				{
					url:"/findStaffByParam",
					postData:data
				}
			).trigger("reloadGrid");
			app.branch=null;

		},


	}
})



//--------------------------------------------------------------------------------------------------------------------------------------------------

//创建Vue对象
//添加员工数据
Vue.component('v-select',VueSelect.VueSelect);
let showRoleApp = new Vue({
	el:'#addfrom',
	data:{
		staffName:null,
		gender:null,
		phone:null,
		idCard:null,
		address:null,
		isDelete:null,
		branch:null,
		position:null,
		genderlist:[
			{  label: '男',value:'男' },
			{  label: '女',value:'女' }
		],
		isDeleteList:[
			{  label: '是',value:'是' },
			{  label: '否',value:'否' }
		]

	},
	methods:{
		showbranch:function(){
			$.ajax({
				url:'showBranchName',
				type:'post',
				dataType:'json',
				success:function(data){
					console.log("111111111");
					let list=data.data;
					console.log(data.data)
					let branchList = [];
					for(let i=0;i<list.length;i++){
						let  op ={};
						op.label = list[i].branchName;
						op.value = list[i].branchName;
						branchList[i] = op;
					}
					showRoleApp.branchList=branchList;
				}
			});
		},

		addStaff:function(){
			if(showRoleApp.branch==null||showRoleApp.branch==""){
				swal("错误", "所属网点不能为空！", "error");
				return false;
			}
			let data =
				{
					'staffId':null,
					'staffName':showRoleApp.staffName,
					'gender':showRoleApp.gender,
					'phone':showRoleApp.phone,
					'idCard':showRoleApp.idCard,
					'address':showRoleApp.address,
					'isdelete':showRoleApp.isDelete,
					'position':showRoleApp.position	,
					'branch':showRoleApp.branch
				};
			console.log(data);

			$.ajax({
				url: "/addStaff",
				type: "POST",
				data: data,
				success: function (data) {
					console.log(data);
					if (data.state == 2000) {
						successalert("", "用户信息添加完成");
						$("#table_list_2").trigger("reloadGrid");
						showRoleApp.gender=null;
						showRoleApp.isDelete=null;
						showRoleApp.branch=null;
						showRoleApp.phone="";
						showRoleApp.idCard="";
						showRoleApp.address="";
						showRoleApp.position="";
						showRoleApp.staffName="";

						$('#myModal2').modal('hide');

					} else {
						errorsalert("", data.message);
						showRoleApp.gender=null;
						showRoleApp.isDelete=null;
						showRoleApp.branch=null;
						showRoleApp.phone="";
						showRoleApp.idCard="";
						showRoleApp.address="";
						showRoleApp.position="";
						showRoleApp.staffName="";
					}

				},
				error: function () {
					errorsalert("", data.message);
				}
			});

		},



	},


});

//----------------------------------------------------------------------------------------------------
//创建Vue对象
Vue.component('v-select',VueSelect.VueSelect);
let showStaffApp = new Vue({
	el:'#changeform',
	data:{
		staffName:null,
		gender:null,
		phone:null,
		idCard:null,
		address:null,
		isDelete:null,
		branch:null,
		position:null,
		staffId:null,

		genderlist:[
			{  label: '男',value:'男' },
			{  label: '女',value:'女' }
		],
		isDeleteList:[
			{  label: '是',value:'是' },
			{  label: '否',value:'否' }
		],
		branchList:[
			{  label: '福建省福州市台江区xxxx' },
			{  label: '福建省莆田市荔城区xxxx' },
			{  label: '福建省泉州市丰泽区xxxx' },
			{  label: '福建省龙岩市漳平市xxxx' }
		]
	},

	methods:{
		showbranch:function(){

			$.ajax({
				url:'showBranchName',
				type:'post',
				dataType:'json',
				success:function(data){
					console.log("111111111");
					let list=data.data;
					console.log(data.data)
					let branchList = [];
					for(let i=0;i<list.length;i++){
						let  op ={};
						op.label = list[i].branchName;
						op.value = list[i].branchName;
						branchList[i] = op;
					}
					showStaffApp.branchList=branchList;
				}
			});
		},
		showStaff:function(id,StaffId){
			console.log("changedialogshow" + id);
			var datas = $("#table_list_2").jqGrid("getRowData", id);//获取单行数据
			console.log("?????->:{}", datas);
			showStaffApp.staffName=datas.staffName;
			showStaffApp.gender=datas.gender;
			showStaffApp.phone=datas.phone;
			showStaffApp.idCard=datas.idCard;
			showStaffApp.address=datas.address;
			showStaffApp.isDelete=datas.isdelete;
			showStaffApp.branch=datas.branch;
			showStaffApp.position=datas.position;
			showStaffApp.staffId=StaffId;

		},

		updateStaff:function(){
			let data =
				{
					'staffId':showStaffApp.staffId,
					'staffName':showStaffApp.staffName,
					'gender':showStaffApp.gender,
					'phone':showStaffApp.phone,
					'idCard':showStaffApp.idCard,
					'address':showStaffApp.address,
					'isdelete':showStaffApp.isDelete,
					'position':showStaffApp.position	,
					'branch':showStaffApp.branch
				};
			console.log(data);

			$.ajax({
				url: "/updateStaff",
				type: "POST",
				data: data,
				success: function (data) {
					console.log(data);
					if (data.state == 2000) {
						successalert("", "用户信息修改完成");
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





