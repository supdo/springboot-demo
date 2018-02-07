<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>注册</title>
    <link rel="stylesheet" href="/iview/styles/iview.css">
    <link ref="stylesheet" href="/css/style.css">
    <style scoped>
        body {
            background-color: #F9F9F9;
        }
        [v-cloak] {
            display: none;
        }
        #logonApp {
            width: 400px;
            margin: 150px auto 0 auto;
        }
        .card-title-text {
            font-size: 16px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<#import "/default/lib/form.ftl" as mf>
<div id="logonApp" v-cloak>
    <card class="loginCard">
        <div slot="title"><span class="card-title-text">注册</span><span style="float:right;">已有账号请<a href="/login">登录</a>。</span></div>
        <i-form ref="logonForm" :model="formData" :label-width="80">
            <@mf.Hform items=user.fields/>
            <form-item>
                <i-button type="primary" @click="handleSubmit('logonForm')">注册</i-button>
                <i-button type="ghost" @click="handleReset('logonForm')" style="margin-left: 8px">重置</i-button>
            </form-item>
        </i-form>
    </card>
</div>
<script type="text/javascript" src="/js/vue.js"></script>
<script type="text/javascript" src="/iview/iview.js"></script>
<script type="text/javascript">
    var app = new Vue({
        el: "#logonApp",
        data: {
            formData: {
                name: ""
            }
        },
        methods: {
            handleSubmit: function(name){
                var self = this;
                this.$refs[name].validate(function(valid){
                    if (valid) {
                        self.$Message.success('校验成功!');
                    } else {
                        self.$Message.error('校验失败!');
                    }
                });
            },
            handleReset: function(name){
                this.$refs[name].resetFields();
            }
        }
    });
</script>
</body>
</html>