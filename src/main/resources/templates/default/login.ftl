<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Login</title>
    <link rel="stylesheet" charset="utf-8" href="/element/style/style.css"/>
    <link rel="stylesheet" charset="utf-8" href="/css/style.css" />
    <style type="text/css">
        body {
            margin: 0px;
        }
        [v-cloak] {
            display: none;
        }
        #loginApp {
            width: 400px;
            margin: 150px auto 0 auto;
        }
    </style>
</head>
<body>
<div id="loginApp" v-cloak>
    <el-card class="loginCard">
        <div slot="header" ><span class="card-title-text">欢迎登陆</span> <span style="float:right;"><a href="/logon">注册</a></span></div>
        <el-form ref="loginForm"  :model="formData">
            <el-form-item ref="username" prop="username" :error="formError.username" :rules="{required: true, message: '用户名不能为空', trigger: 'blur'}">
                <el-input type="text" v-model="formData.username" placeholder="用户名">
                    <i class="el-icon-document" size="18" slot="prepend"></i>
                </el-input>
            </el-form-item>
            <el-form-item ref="password"  prop="password" :error="formError.password" :rules="{required: true, message: '密码不能为空', trigger: 'blur'}">
                <el-input type="password" v-model="formData.password" placeholder="密码">
                    <i class="el-icon-document" size="18" slot="prepend"></i>
                </el-input>
            </el-form-item>
            <el-row>
                <el-col :span="12">
                    <el-form-item ref="verifycode" prop="verifycode" :error="formError.verifycode" :rules="{required: true, message: '验证码不能为空', trigger: 'blur'}">
                        <el-input type="text" v-model="formData.verifycode" placeholder="验证码" >
                            <i class="el-icon-document" size="18" slot="prepend"></i>
                        </el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12"><img id="VCImg" style="height:38px; margin:2px 0 0 5px;cursor:pointer;" @click="reloadSC" :src="scUrl" title="点击刷新验证码！"></el-col>
            </el-row>
            <el-form-item class="last-form-item">
                <el-button type="primary" style="width:100%" @click="handleSubmit('loginForm')">登录</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</div>
<script type="text/javascript" src="/js/vue.min.js"></script>
<script type="text/javascript" src="/element/element-ui.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript">
    var loginApp = new Vue({
        el: '#loginApp',
        data: {
            formData: {
                username: '',
                password: '',
                verifycode: '',
            },
            formError: {
                username: '',
                password: '',
                verifycode: '',
            },
            scUrl: '/VerifyCode'
        },
        methods: {
            handleSubmit: function(name){
                var self = this;
                this.$refs[name].clearValidate();
                this.$refs[name].validate(function(valid){
                   if (valid) {
                        //self.$Spin.show();
                        myPost('/login.api', self.formData,
                                function(data){
                                    if(data.flag){
                                        self.$message.success(data.msg);
                                        window.location.href="/default";
                                    }else{
                                        self.$message.error(data.msg);
                                        var users = data.items.user.fields;
                                        for(var key in users) {
                                            self.$refs[key].error = null;
                                            self.$nextTick(function() {
                                                self.$refs[key].error = users[key]['error'];
                                            });
                                        }
                                        self.$refs['verifycode'].error = null;
                                        self.$nextTick(function() {
                                            self.$refs['verifycode'].error = data.items.verifycodeError;
                                        });

                                        if(!data.items.verifycodeError){
                                            self.reloadSC();
                                        }
                                    }
                                },
                                function(req,textStatus){
                                    self.$message.error(textStatus);
                                },
                                function(req,textStatus){
                                    //self.$Spin.hide();
                                    //self.$Message.info(textStatus);
                                }
                        );
                    } else {
                        self.$message.error('校验失败!');
                    }
                });
            },
            reloadSC: function(){
                this.scUrl = "/VerifyCode.gif?rm="+Math.round(Math.random()*10000);
            }
        }
    });
</script>
</body>
</html>