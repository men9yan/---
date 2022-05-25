$(function () {
    $("input").focus(function () {
        // 清除（隐藏）原本可能存在的错误
        $("#error").css('visibility', 'hidden');
    });
});

//创建Vue对象
let app = new Vue({
    el: '#app',
    data: {
        username: null,
        password: null,
        imageCode: null,
        hasError: false,
        errorMessage: null
    },
    methods: {

        login: function () {
            let _this = this;
            app.hasError = false;
            let data = {
                'username': app.username,
                'password': app.password,
                'imageCode': app.imageCode,

            };
            $.ajax({
                url: "/login",
                type: "POST",
                data: data,
                success: function (data) {
                    console.log(data);
                    if (data.state === 2000) {
                        window.location.href = 'backoffice';
                    } else {
                        // 清除（隐藏）原本可能存在的错误
                        $("#error").removeAttr('style');
                        _this.username = "";
                        _this.password = "";
                        _this.imageCode = "";
                        $("#randImage").click();
                        app.errorMessage = data.message;
                        app.hasError = true;
                    }

                },

            });
        },

    },
})