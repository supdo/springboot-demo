<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Spider Test</title>
    <link rel="stylesheet" charset="utf-8" href="${request.contextPath}/element/style/style.css"/>
    <link rel="stylesheet" charset="utf-8" href="${request.contextPath}/css/style.css"/>
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
</head>
<body>
<#import "/default/lib/form.ftl" as mf>
<div id="main" v-cloak>
    <el-form ref="TopForm" :model="TopForm" :inline="true" :model="formInline" size="small">
        <el-form-item label="网站列表" prop="site" :rules="{ required: true, message: '网站不能为空'}">
            <el-select v-model="TopForm.site" filterable placeholder="请选择网站" style="width: 360px;">
                <el-option  v-for="(item, index) in sites" :key="item.value" :label="item.label" :value="item.value">
                    <span style="float: left">{{ item.label }}</span>
                    <span style="float: right; margin-right:-16px;"><i class="el-icon-circle-close btn-delete-site" @click="deleteSite(index);"></i></span>
                </el-option>
            </el-select>
            <i class="el-icon-circle-plus btn-add-site" @click="siteDlg.visible = true;"></i>
        </el-form-item>
        <el-checkbox v-model="TopForm.force">强制更新</el-checkbox>
        <el-button @click="handleGetPL()" type="primary" size="small">抓取页面列表</el-button>
        <el-button @click="handlePostAll()" type="primary" size="small">发送整个列表</el-button>
        <el-button @click="logDlg.visible = true" size="small">发送日志</el-button>
    </el-form>
    <el-dialog :title="siteDlg.title" :visible.sync="siteDlg.visible" top="30px" width="500px">
        <el-form ref="siteForm" :model="siteDlg.siteForm" label-width="80px" size="small">
            <@mf.Hform items=siteForm.fields formData='siteDlg.siteForm' refName='siteForm' itemOptions='siteOptions' />
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="siteDlg.visible = false" size="small">取 消</el-button>
            <el-button :loading="siteDlg.okBtnLoading" type="primary" @click="handleAddSite()" size="small">保 存</el-button>
        </div>
    </el-dialog>
    <el-table :data="listData" border style="width: 100%" size="small"
              v-loading="tableLoading"
              element-loading-text="数据处理中..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)">
        <el-table-column prop="id" label="编号" width="60"></el-table-column>
        <el-table-column prop="title" label="标题" width="300"></el-table-column>
        <el-table-column prop="url" label="地址"></el-table-column>
        <el-table-column prop="grab" label="抓取" width="60">
            <template slot-scope="scope">
                <p v-if="scope.row.grab">是</p><p v-else>否</p>
            </template>
        </el-table-column>
        <el-table-column prop="post" label="发送" width="60">
            <template slot-scope="scope">
                <p v-if="scope.row.post">是</p><p v-else>否</p>
            </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
            <template slot-scope="scope">
                <el-button @click="handleGetPC(scope.$index)" type="primary" size="mini">抓取</el-button>
                <el-button @click="handlePostContent(scope.$index)" type="primary" size="mini">发送</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-dialog :title="logDlg.title" :visible.sync="logDlg.visible" top="30px" width="700px">
        <div class="log-body">
            <ul>
                <li v-for="log in logDlg.data">{{log}}</li>
            </ul>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button @click="siteDlg.visible = false" size="mini">取 消</el-button>
        </div>
    </el-dialog>
</div>
<#--<script src="//unpkg.com/vue/dist/vue.js"></script>-->
<#--<script src="//unpkg.com/element-ui@2.2.0/lib/index.js"></script>-->
<script type="text/javascript" src="${request.contextPath}/js/jquery.ajax.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/vue.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/element/element-ui.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/sockjs.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/stomp.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/common.js"></script>
<script type="text/javascript">
    var main = new Vue({
        el: '#main',
        data: {
            stompClient: null,
            TopForm: {site: '', force: false},
            sites: [
                <#list siteList![] as site>
                    {id: '${site.id}', value: '${site.id}', label: '${site.name}(${site.url})'}<#sep>,</#sep>
                </#list>
            ],
            siteDlg: {
                visible: false,
                index: -1,
                title: '添加网站',
                okBtnLoading: false,
                siteForm: {}
            },
            siteModel: {id: '', <#list siteForm.fields?keys as key>${key}: ''<#sep>, </#sep></#list>},
            siteOptions:{ <#list siteForm.fields?keys as key><#if siteForm.fields[key].type == 'select'>
                ${key}: {<#list siteForm.fields[key].options?keys as val>${val}: '${siteForm.fields[key].options[val]}'<#sep>, </#sep></#list>}
                <#sep>, </#sep></#if></#list> },
            listData: [
                <#list apl![] as pl>
                    { id: '${pl['id']!"#"}', title: '${pl.title?js_string}', url: '${pl.url?js_string}', grab: ${pl.isGrab?js_string}, post: ${pl.isPost?js_string}}<#sep>,</#sep>
                </#list>
            ],
            tableLoading: false,
            logDlg: {
                visible: false,
                title: '处理日志',
                data: []
            }
        },
        mounted: function() {
            var $this = this;
            this.$nextTick(function () {
                $this.initWebSocket();
            });
        },
        methods: {
            initWebSocket: function() {
                var $this = this;
                var socket = new SockJS('${request.contextPath}/endpointSpider');
                if($this.stompClient == null) {
                    $this.stompClient = Stomp.over(socket);
                    $this.stompClient.connect({}, function (frame) {
                                $this.connected = true;
                                console.log('Connected: ' + frame);
                                $this.stompClient.subscribe('${request.contextPath}/user/oto/notifications', function (data) {
                                    var dataBody = JSON.parse(data.body);
                                    $this.$notify.info({
                                        title: '来自服务器的消息', message: dataBody.msg, position: 'bottom-right', showClose:true
                                    });
                                    //$this.logDlg.data.unshift(dataBody.msg);
                                    var newDate = new Date();
                                    $this.logDlg.data.unshift(newDate.format('yyyy-MM-dd h:m:s')+" -- "+dataBody.msg);
                                });
                            },
                            function(frame){
                                $this.$notify.error({
                                    title: '来自服务器的消息', message: frame, showClose:true
                                });
                                var newDate = new Date();
                                $this.logDlg.data.unshift(newDate.format('yyyy-MM-dd h:m:s')+" -- 失去连接，"+frame);
                            });
                }
            },
            handleAddSite:function() {
                var $this = this;
                this.$refs.siteForm.validate(function(valid) {
                    if (valid) {
                        $this.siteDlg.okBtnLoading = true;
                        myPost('/spider/addSite', $this.siteDlg.siteForm,
                                function(data){
                                    if(data.flag){
                                        $this.$message.success(data.msg);
                                        $this.siteDlg.visible = false;
                                        $this.siteDlg.siteForm = {};
                                        var site = data.items.site;
                                        $this.sites.push({value: site.id, label: site.name+"("+site.url+")"});
                                    }else{
                                        $this.$message.error(data.msg);
                                        var rules = data.items.site.fields;
                                        $this.$nextTick(function() {
                                            for(var key in rules) {
                                                $this.$refs['siteDlg_' + key].error = rules[key]['error'];
                                            }
                                        });
                                    }
                                },
                                function(req, textStatus){
                                    $this.$message.error(textStatus);
                                },
                                function(req, textStatus){
                                    $this.siteDlg.okBtnLoading = false;
                                }
                        );
                    }
                });
            },
            deleteSite: function(index) {
                var $this = this;
                $this.$confirm('确认要删除“'+$this.sites[index].label+'”么？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                    callback: function (action, instance) {
                        if (action == 'confirm') {
                            myPost('${request.contextPath}/spider/deleteSite/' + $this.sites[index].id, {},
                                    function (data) {
                                        if (data.flag) {
                                            $this.$message.success({message:data.msg, showClose:true});
                                            if($this.TopForm.site == $this.sites[index].value){
                                                $this.TopForm.site = "";
                                            }
                                            $this.sites.splice(index, 1);
                                        } else {
                                            $this.$message.error({message:data.msg, showClose:true});
                                        }
                                    },
                                    function (req, textStatus) {
                                        $this.$message.error(textStatus);
                                    },
                                    function (req, textStatus) {
                                        $this.tableLoading = false;
                                    }
                            );
                        }
                    }
                });
            },
            handleGetPL: function(){
                var $this = this;
                this.$refs.TopForm.validate(function(valid) {
                    if (valid) {
                        $this.tableLoading = true;
                        myPost('${request.contextPath}/spider/GetPL', $this.TopForm,
                                function(data){
                                    if(data.flag){
                                        $this.$message.success({message:data.msg, showClose:true});
                                        //$this.listData = $this.listData.concat(data.items.lpl);
                                        $this.listData = data.items.lpl;
                                        // $this.pUrls = [];
                                        // for(pUrl in data.items.pUrls) {
                                        //     $this.pUrls.push({value: pUrl, label: pUrl});
                                        // }
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
                });
            },
            handleGetPC: function(index){
                var $this = this;
                $this.tableLoading = true;
                myPost('${request.contextPath}/spider/GetPC/'+$this.listData[index].id, {},
                        function(data){
                            if(data.flag){
                                $this.$message.success({message:data.msg, showClose:true});
                                $this.listData[index].grab = true;
                                //$this.$alert(data.html);
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
            handlePostContent: function(index) {
                var $this = this;
                $this.tableLoading = true;
                myPost('${request.contextPath}/spider/PostZootopia/'+$this.listData[index].id, {},
                        function(data){
                            if(data.flag){
                                $this.$message.success({message:data.msg, showClose:true});
                                $this.listData[index].post = true;
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
            handlePostAll: function() {
                var $this = this;
                $this.tableLoading = true;
                $this.logDlg.visible = true;
                myPost('${request.contextPath}/spider/PostZootopia/all/'+$this.TopForm.site, {},
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
            }
        }
    });
</script>
</body>
</html>