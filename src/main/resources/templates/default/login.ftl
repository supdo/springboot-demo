<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>首页</title>
    <link rel="stylesheet" type="text/css" href="/iview/styles/iview.css">
    <link ref="stylesheet" type="text/css" href="/style/style.css">
    <style scoped>
        body {
            background-color: #F9F9F9;
        }
        [v-cloak] {
            display: none;
        }
        #loginApp {
            margin-top: 200px;
        }
        .loginCard {

        }
        .title {
            font-size: 16px;
            font-weight: bold;
        }
        .last-form-item {
            margin-bottom: 0px;
        }
    </style>
</head>
<body>
<div id="loginApp" v-cloak>
    <row>
        <i-col span="4" offset="10">
            <card class="loginCard">
                <div slot="title" ><span class="title">欢迎登陆</span> <span style="float:right;"><a href="/logon">注册</a></span></div>
                <i-form ref="loginForm"  :model="formData">
                    <form-item prop="name" :rules="{required: true, message: '用户名不能为空', trigger: 'blur'}">
                        <i-input type="text" v-model="formData.name" placeholder="用户名">
                            <icon type="person" size="18" slot="prepend"></icon>
                        </i-input>
                    </form-item>
                    <form-item  prop="password" :rules="{required: true, message: '密码不能为空', trigger: 'blur'}">
                        <i-input type="password" v-model="formData.password" placeholder="密码">
                            <icon type="locked" size="18" slot="prepend"></icon>
                        </i-input>
                    </form-item>
                    <form-item class="last-form-item">
                        <i-button type="primary" long @click="handleSubmit('loginForm')">登录</i-button>
                    </form-item>
                </i-form>
            </card>
        </i-col>
    </row>
</div>
<script type="text/javascript" src="/js/vue.js"></script>
<script type="text/javascript" src="/iview/iview.js"></script>
<script type="text/javascript">
    var loginApp = new Vue({
        el: "#loginApp",
        data: {
            formData: {
                name: '',
                password: ''
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