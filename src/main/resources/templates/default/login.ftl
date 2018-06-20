<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Login</title>
    <link rel="stylesheet" charset="utf-8" href="${request.contextPath}/element/style/style.css"/>
    <link rel="stylesheet" charset="utf-8" href="${request.contextPath}/css/style.css" />
    <style type="text/css">
        #loginApp {
            width: 400px;
            margin: 150px auto 0 auto;
        }
    </style>
</head>
<body>
<div id="loginApp" v-cloak>
    <el-card class="loginCard">
        <div slot="header" ><span class="card-title-text">欢迎登陆</span> <span style="float:right;"><a href="${request.contextPath}/logon">注册</a></span></div>
        <el-form ref="loginForm"  :model="formData" label-width="80px">
            <el-form-item ref="username" prop="username" label="用户名" :error="formError.username" :rules="{required: true, message: '用户名不能为空', trigger: 'blur'}">
                <el-input type="text" v-model="formData.username" placeholder="用户名"></el-input>
            </el-form-item>
            <el-form-item ref="password"  prop="password" label="密码" :error="formError.password" :rules="{required: true, message: '密码不能为空', trigger: 'blur'}">
                <el-input type="password" v-model="formData.password" placeholder="密码"></el-input>
            </el-form-item>
            <el-row>
                <el-col :span="12">
                    <el-form-item ref="verifycode" prop="verifycode" label="验证码" :error="formError.verifycode" :rules="{required: true, message: '验证码不能为空', trigger: 'blur'}">
                        <el-input type="text" v-model="formData.verifycode" placeholder="验证码" ></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12"><img id="VCImg" style="height:38px; margin:1px 0 0 5px;cursor:pointer;" @click="reloadSC" :src="scUrl" title="点击刷新验证码！"></el-col>
            </el-row>
            <el-form-item class="last-form-item">
                <el-button type="primary" style="width:100%" @click="handleSubmit('loginForm')">登录</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</div>
<script type="text/javascript" src="${request.contextPath}/js/jquery.ajax.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/vue.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/element/element-ui.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/common.js"></script>
<script type="text/javascript">
    var contextPath = '${request.contextPath}';
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
            scUrl: '${request.contextPath}/VerifyCode'
        },
        methods: {
            handleSubmit: function(name){
                var $this = this;
                this.$refs[name].clearValidate();
                this.$refs[name].validate(function(valid){
                   if (valid) {
                        //$this.$Spin.show();
                        myPost('${request.contextPath}/login.api', $this.formData,
                                function(data){
                                    if(data.flag){
                                        $this.$message.success(data.msg);
                                        window.location.href="${request.contextPath}/default";
                                    }else{
                                        $this.$message.error(data.msg);
                                        var users = data.items.user.fields;
                                        for(var key in users) {
                                            $this.$refs[key].error = null;
                                            $this.$nextTick(function() {
                                                $this.$refs[key].error = users[key]['error'];
                                            });
                                        }
                                        $this.$refs['verifycode'].error = null;
                                        $this.$nextTick(function() {
                                            $this.$refs['verifycode'].error = data.items.verifycodeError;
                                        });

                                        if(!data.items.verifycodeError){
                                            $this.reloadSC();
                                        }
                                    }
                                },
                                function(req,textStatus){
                                    $this.$message.error(textStatus);
                                },
                                function(req,textStatus){
                                    //$this.$Spin.hide();
                                    //$this.$Message.info(textStatus);
                                }
                        );
                    } else {
                        $this.$message.error('校验失败!');
                    }
                });
            },
            reloadSC: function(){
                this.scUrl = contextPath+"/VerifyCode.gif?rm="+Math.round(Math.random()*10000);
            }
        }
    });
</script>
</body>
</html>