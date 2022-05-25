function submit1(){
	
	oldpassword = $("#oldpassword").val();
	password = $("#password").val();
	repassword = $("#repassword").val();
	console.log(oldpassword+password+repassword);
	if(oldpassword==""||password==""||repassword==""){
		swal("错误", "请输入完整信息！！", "error");
		return;
	}

	if(password != repassword){
		swal("错误", "新密码两次输入不一样!", "error");
		return;
	}
	
	if(oldpassword == password){
		swal("错误", "新旧密码不能相同!", "error");
		return;
	}
	
	
	console.log("submit");
	$.ajax({
		url: "/rsetpassword",
		type: "POST",
		data: $("#changefrom").serialize(),
		success:function(data){
			console.log(data);
			if(data.state == 2000){
				swal({
					title:"成功",
					text:"修改密码成功！",
					type:"success",
					showCancelButton:false,//是否显示取消按钮

					showConfirmButton:true,
					confirmButtonText:'确 认',
					confirmButtonColor:"#28a745",

					closeOnConfirm:false,//点击返回上一步操作
					closeOnCancel:false
				},function(){//正确按钮进行的操作点
					window.location="/logout"
				});
			}else{
				swal("失败",data.message, "error");
			}

		},
		error:function(){
			swal("失败","系统错误，请重试", "warning");
		}
	});
}