$(function(){
	tologin(10);
});

function tologin(sec){
	if(sec<=0){
		window.location = "/index";
		return;
	}
	$("#second").text(sec + "秒后");
	sec = sec - 1;
	setTimeout(function(){tologin(sec)},1000);

}
