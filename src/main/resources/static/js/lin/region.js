/*$(function () {
    $("input").focus(function () {
        // 清除（隐藏）原本可能存在的错误
        $("#error").css('visibility', 'hidden');
    });
});

//创建Vue对象
let app = new Vue({
    el: '#app',
    data: {
        trueName: null,
        username: null,
        password: null,
        phone: null,
        hasError: false,
        errorMessage: null
    },
    methods: {

        region: function () {
            let _this = this;
            app.hasError = false;
            let data = {
                'trueName' : app.trueName,
                'username': app.username,
                'password': app.password,
                'phone': app.phone,

            };
            $.ajax({
                url: "/region",
                type: "POST",
                data: data,
                success: function (data) {
                    console.log(data);
                    if (data.state === 2000) {
                        window.location.href = 'backoffice';
                    }

                },

            });
        },

    },
})*/


function region1(){

    trueName = $("#trueName").val();
    password = $("#password").val();
    username = $("#username").val();
    phone = $("#phone").val();


    console.log("submit");
    $.ajax({
        url: "/region",
        type: "POST",
        data: $("#regist").serialize(),
        success:function(data){
            console.log(data);
            if (data.state === 2000) {
                window.location.href = 'backoffice';
            }else{
                swal("失败",data.message, "error");
            }

        },
        error:function(){
            swal("失败","系统错误，请重试", "warning");
        }
    });
}