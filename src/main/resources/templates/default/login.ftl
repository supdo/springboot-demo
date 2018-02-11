<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>登录</title>
    <link rel="stylesheet" charset="utf-8" href="/iview/styles/iview.css" />
    <link rel="stylesheet" charset="utf-8" href="/css/style.css" />
    <style scoped>
        body {
            background-color: #F9F9F9;
        }
        #loginApp {
            width: 300px;
            margin: 150px auto 0 auto;
        }
        .loginCard {

        }
    </style>
</head>
<body>
<div id="loginApp" v-cloak>
    <#--<spin>处理中...</spin>-->
    <card class="loginCard">
        <div slot="title" ><span class="card-title-text">欢迎登陆</span> <span style="float:right;"><a href="/logon">注册</a></span></div>
        <i-form ref="loginForm"  :model="formData">
            <form-item ref="username" prop="username" :error="formError.username" :rules="{required: true, message: '用户名不能为空', trigger: 'blur'}">
                <i-input type="text" v-model="formData.username" placeholder="用户名" clearable>
                    <icon type="person" size="18" slot="prepend"></icon>
                </i-input>
            </form-item>
            <form-item ref="password"  prop="password" :error="formError.password" :rules="{required: true, message: '密码不能为空', trigger: 'blur'}">
                <i-input type="password" v-model="formData.password" placeholder="密码" clearable>
                    <icon type="locked" size="18" slot="prepend"></icon>
                </i-input>
            </form-item>
            <Row>
                <i-col span="12">
                    <form-item ref="verifycode" prop="verifycode" :error="formError.verifycode" :rules="{required: true, message: '验证码不能为空', trigger: 'blur'}">
                        <i-input type="text" v-model="formData.verifycode" placeholder="验证码" clearable ><icon type="grid" size="18" slot="prepend"></icon></i-input>
                    </form-item>
                </i-col>
                <i-col span="12"><img id="VCImg" style="height:30px; margin:2px 0 0 5px;cursor:pointer;" @click="reloadSC" :src="scUrl" title="点击刷新验证码！"></i-col>
            </Row>
            <form-item class="last-form-item">
                <i-button type="primary" long @click="handleSubmit('loginForm')">登录</i-button>
            </form-item>
        </i-form>
    </card>
</div>
<script type="text/javascript" src="/js/vue.min.js"></script>
<script type="text/javascript" src="/iview/iview.js"></script>
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
            this.$refs[name].validate(function(valid){
                if (valid) {
                    self.$Spin.show();
                    myPost('/login.api', self.formData,
                        function(data){
                            if(data.flag){
                                self.$Message.success(data.msg);
                                window.location.href="/default";
                            }else{
                                self.$Message.error(data.msg);
                                var users = data.items.user.fields;
                                for(var key in users){
                                    setFormError(self.$refs[key], users[key]['error']);
                                    // self.$refs[key].validateMessage = users[key]['error'];
                                    // self.$refs[key].validateState = 'error';

                                    // self.formError[key] = '';
                                    // self.$nextTick(function() {
                                    //     self.formError[key] = users[key]['error'];
                                    // });
                                }
                                setFormError(self.$refs['verifycode'], data.items.verifycodeError);
                                if(!data.items.verifycodeError){
                                    self.reloadSC();
                                }
                            }
                        },
                        function(req,textStatus){
                            self.$Message.error(textStatus);
                        },
                        function(req,textStatus){
                            self.$Spin.hide();
                            //self.$Message.info(textStatus);
                        }
                    );
                } else {
                    self.$Message.error('校验失败!');
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