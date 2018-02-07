<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>首页</title>
    <link rel="stylesheet" href="/iview/styles/iview.css">
    <link ref="stylesheet" href="/css/style.css">
    <style scoped>
        body {
            background-color: #F9F9F9;
        }
        [v-cloak] {
            display: none;
        }
        #loginApp {
            width: 300px;
            margin: 150px auto 0 auto;
        }
        .loginCard {

        }
        .card-title-text {
            font-size: 16px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div id="loginApp" v-cloak>
    <card class="loginCard">
        <div slot="title" ><span class="card-title-text">欢迎登陆</span> <span style="float:right;"><a href="/logon">注册</a></span></div>
        <i-form ref="loginForm"  :model="formData">
            <form-item prop="name" :rules="{required: true, message: '用户名不能为空', trigger: 'blur'}">
                <i-input type="text" v-model="formData.name" placeholder="用户名" clearable>
                    <icon type="person" size="18" slot="prepend"></icon>
                </i-input>
            </form-item>
            <form-item  prop="password" :rules="{required: true, message: '密码不能为空', trigger: 'blur'}">
                <i-input type="password" v-model="formData.password" placeholder="密码" clearable>
                    <icon type="locked" size="18" slot="prepend"></icon>
                </i-input>
            </form-item>
            <Row>
                <i-col span="12">
                    <form-item prop="verifycode" :rules="{required: true, message: '验证码不能为空', trigger: 'blur'}">
                        <i-input type="text" v-model="formData.verifycode" placeholder="验证码" clearable ><icon type="grid" size="18" slot="prepend"></icon></i-input>
                    </form-item>
                </i-col>
                <i-col span="12"><img id="VCImg" style="height:30px; margin:2px 0 0 5px;cursor:pointer;" src="/VerifyCode" title="点击刷新验证码！"></i-col>
            </Row>
            <form-item class="last-form-item">
                <i-button type="primary" long @click="handleSubmit('loginForm')">登录</i-button>
            </form-item>
        </i-form>
    </card>
</div>
<script type="text/javascript" src="/js/vue.js"></script>
<script type="text/javascript" src="/iview/iview.js"></script>
<script type="text/javascript">
    var loginApp = new Vue({
        el: "#loginApp",
        data: {
            formData: {
                name: '',
                password: '',
                verifycode: '',
            }
        },
        methods: {
            handleSubmit: function(name){
                this.$refs[name].validate(function(valid){
                    if (valid) {
                        this.$Message.success('校验成功!');
                    } else {
                        this.$Message.error('校验失败!');
                    }
                });
            }
        }
    });
</script>
</body>
</html>