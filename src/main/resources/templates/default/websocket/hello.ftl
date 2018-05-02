<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>WebSocket Hello</title>
    <link rel="stylesheet" charset="utf-8" href="/element/style/style.css"/>
    <link rel="stylesheet" charset="utf-8" href="/css/style.css" />
    <style type="text/css">
        #hello {
            width: 600px;
            margin: 10px auto 0 auto;
        }
        .hello-card {
            margin-top: 50px;
        }
    </style>
</head>
<body>
<div id="hello" v-cloak>
    <el-card class="hello-card">
        <div slot="header" ><span class="card-title-text">广播</span> </div>
        <el-form :inline="true">
            <el-form-item>
                <el-button type="primary" :disabled="connected" @click="handleConnect();">连接</el-button>
                <el-button type="primary" :disabled="!connected" @click="handleClose();">断开</el-button>
            </el-form-item>
        </el-form>
        <el-form :inline="true" :model="chatFrom">
            <el-form-item label="消息">
                <el-input v-model="chatFrom.msg" placeholder="消息"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="sendMsg();">发送</el-button>
            </el-form-item>
        </el-form>
    </el-card>
    <el-card class="hello-card">
        <div slot="header" ><span class="card-title-text">点对点</span> </div>
        <el-form :inline="true" :model="chatFrom">
            <el-form-item label="用户">
                <el-input v-model="chatFrom.toUser" placeholder="用户"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" :disabled="connected" @click="handleConnectOne();">连接</el-button>
                <el-button type="primary" :disabled="!connected" @click="handleClose();">断开</el-button>
            </el-form-item>
        </el-form>
        <el-form :inline="true" :model="chatFrom">
            <el-form-item label="消息">
                <el-input v-model="chatFrom.msg" placeholder="消息"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="sendMsgOne();">发送</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</div>
<script type="text/javascript" src="/js/jquery.ajax.js"></script>
<script type="text/javascript" src="/js/vue.min.js"></script>
<script type="text/javascript" src="/element/element-ui.min.js"></script>
<script type="text/javascript" src="/js/sockjs.min.js"></script>
<script type="text/javascript" src="/js/stomp.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript">
    var hello = new Vue({
        el: '#hello',
        data: {
            stompClient: null,
            connected: false,
            chatFrom: {
                toUser: '',
                msg: ''
            }
        },
        methods: {
            handleConnect: function() {
                var $this = this;
                var socket = new SockJS('/endpointSpider');
                if($this.stompClient == null) {
                    $this.stompClient = Stomp.over(socket);
                    $this.stompClient.connect({}, function (frame) {
                        $this.connected = true;
                        console.log('Connected: ' + frame);
                        $this.stompClient.subscribe('/topic', function (data) {
                            var dataBody = JSON.parse(data.body);
                            $this.$notify.info({
                                title: '来自服务器的消息', message: dataBody.msg, position: 'bottom-right', showClose:true
                            });
                        });
                    },
                    function(frame){
                        $this.$notify.info({
                            title: '来自服务器的消息', message: frame, showClose:true
                        });
                    });
                }
            },
            handleConnectOne: function() {
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
                        });
                    },
                    function(frame){
                        $this.$notify.info({
                            title: '来自服务器的消息', message: frame, showClose:true
                        });
                    });
                }
            },
            handleClose: function() {
                var $this = this;
                if ($this.stompClient != null) {
                    $this.stompClient.disconnect(function(){
                        $this.$message.info('连接已关闭。');
                    }, {});
                    $this.stompClient = null;
                }
                $this.connected = false;
                console.log("Disconnected");
            },
            sendMsg: function(){
                var $this = this;
                if($this.stompClient == null) {
                    $this.$message.error('请先建立连接。');
                }else{
                    $this.stompClient.send("/hello", {}, JSON.stringify($this.chatFrom));
                    $this.chatFrom.msg = '';
                }

            },
            sendMsgOne: function() {
                var $this = this;
                if($this.stompClient == null) {
                    $this.$message.error('请先建立连接。');
                }else{
                    $this.stompClient.send("/sayToUser", {}, JSON.stringify($this.chatFrom));
                    $this.chatFrom.msg = '';
                }
            }
        }
    });
</script>
</body>
</html>