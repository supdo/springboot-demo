<#import "../lib/layout.ftl" as layout>
<#assign style>
    <style>
        #main {
            margin: 10px;
        }
        .btn-add-site {
            font-size: 20px; cursor: pointer; height: 24px; margin: 0 5px; vertical-align: middle;
        }
        .btn-add-site:hover{
            color: #2b85e4;
        }
        .btn-delete-site{
            font-size: 20px; cursor: pointer; height: 20px; margin: 0 5px; vertical-align: middle; color: #999999;
        }
        .btn-delete-site:hover {
            color: #990000;
        }
        .el-dialog__header {
            padding: 10px 20px;
        }
        .el-dialog__headerbtn {
            top: 10px;
        }
        .el-dialog__body {
            padding: 10px 15px 0 15px;
        }
        .log-body {
            height:500px;
            overflow-y: auto;
        }
        .log-body ul {
            padding: 2px;
            margin: 0px;
        }
        .log-body ul li {
            list-style: none;
        }
        .el-dialog__footer {
            padding: 10px 20px;
        }
    </style>
</#assign>
<@layout.default title="Zootopia" style=style>
Zootopia
<div id="main" v-cloak>
    <el-form ref="TopForm" :inline="true" :model="TopForm" size="small">
        <el-form-item label="页数" prop="page" :rules="{ required: true, message: '页数不能为空'}">
            <el-input v-model="TopForm.page" placeholder="页数"></el-input>
        </el-form-item>
        <el-button @click="getMyshare()" type="primary" size="small">获取我的分享</el-button>
        <el-button @click="logDlg.visible = true" size="small">日志</el-button>
    </el-form>
    <el-dialog :title="logDlg.title" :visible.sync="logDlg.visible" top="30px" width="700px">
        <div class="log-body">
            <ul>
                <li v-for="log in logDlg.data">{{log}}</li>
            </ul>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button @click="logDlg.visible = false" size="mini">取 消</el-button>
        </div>
    </el-dialog>

    <el-form ref="loginForm" :inline="true" :model="loginForm" size="small">
        <el-form-item label="用户名" prop="username" :rules="{ required: true, message: '用户名不能为空'}">
            <el-input v-model="loginForm.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" :rules="{ required: true, message: '密码不能为空'}">
            <el-input type="password" v-model="loginForm.password" placeholder="密码"></el-input>
        </el-form-item>
        <el-button @click="voteShare()" type="primary" size="small">分享点赞</el-button>
    </el-form>
</div>
<@layout.defaultjs />
<script type="text/javascript" src="/js/sockjs.min.js"></script>
<script type="text/javascript" src="/js/stomp.min.js"></script>
<script type="text/javascript">
    var main = new Vue({
        el: '#main',
        data: {
            TopForm: {page: ''},
            logDlg: {
                visible: false,
                title: '处理日志',
                data: []
            },
            loginForm: {username: '', password: ''}
        },
        mounted: function () {
            var $this = this;
            $this.initWebSocket();
            this.$nextTick(function () {
            });
        },
        methods: {
            initWebSocket: function() {
                var $this = this;
                var socket = new SockJS('/endpointSpider');
                if($this.stompClient == null) {
                    $this.stompClient = Stomp.over(socket);
                    $this.stompClient.connect({}, function (frame) {
                                $this.connected = true;
                                console.log('Connected: ' + frame);
                                $this.stompClient.subscribe('/user/oto/notifications', function (data) {
                                    var dataBody = JSON.parse(data.body);
                                    $this.$notify.info({
                                        title: '来自服务器的消息', message: dataBody.msg, position: 'bottom-right', showClose:true
                                    });
                                    var newDate = new Date();
                                    $this.logDlg.data.unshift(newDate.format('yyyy-MM-dd h:m:s')+" -- "+dataBody.msg);
                                });
                            },
                            function(frame){
                                $this.$notify.error({
                                    title: '来自服务器的消息', message: frame, showClose:true
                                });
                            });
                }
            },
            getMyshare: function(){
                var $this = this;
                $this.tableLoading = true;
                $this.logDlg.visible = true;
                myPost('/spider/getMyShare/', $this.TopForm,
                        function(data){
                            if(data.flag){
                                $this.$message.success({message:data.msg, showClose:true});
                                $this.listData = data.items.lpl;
                            }else{
                                $this.$message.error({message:data.msg, showClose:true});
                            }
                        },
                        function(req, textStatus){
                            $this.$message.error(textStatus);
                        },
                        function(req, textStatus){
                            $this.tableLoading = false;
                        }
                );
            },
            voteShare: function(){
                var $this = this;
                $this.tableLoading = true;
                $this.logDlg.visible = true;
                myPost('/spider/voteMyShare/', $this.loginForm,
                        function(data){
                            if(data.flag){
                                $this.$message.success({message:data.msg, showClose:true});
                            }else{
                                $this.$message.error({message:data.msg, showClose:true});
                            }
                        },
                        function(req, textStatus){
                            $this.$message.error(textStatus);
                        },
                        function(req, textStatus){
                            $this.tableLoading = false;
                        }
                );
            }
        }
    });
</script>
</@layout.default>