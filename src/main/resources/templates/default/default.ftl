<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>首页</title>
    <link rel="stylesheet" type="text/css" href="/iview/styles/iview.css">
</head>
<body>
<div id="loginApp" v-cloak>
    <i-form ref="loginForm"  :model="formData" :label-width="80">
        <form-item label="用户名" prop="name">
            <i-input v-model="formData.name" placeholder="用户名"></i-input>
        </form-item>
        <form-item label="密码" prop="password">
            <i-input v-model="formData.password" placeholder="密码"></i-input>
        </form-item>
        <form-item>
            <i-button type="primary" @click="handleSubmit('loginForm')">登录</i-button>
            <i-button type="ghost" @click="handleReset('loginForm')" style="margin-left: 8px">重置</i-button>
        </form-item>
    </i-form>
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
        handleReset: function(name){

        },
        handleSubmit: function(name){

        }
    }
});
</script>
</body>
</html>