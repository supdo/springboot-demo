<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Spider Test</title>
    <link rel="stylesheet" charset="utf-8" href="/element/style/style.css"/>
    <link rel="stylesheet" charset="utf-8" href="/css/style.css"/>
    <style>
        #main {
            margin: 10px;
        }
        .btn-add-url {
            font-size: 20px; cursor: pointer; height: 24px; margin: 0 5px; vertical-align: middle;
        }
        .btn-add-url:hover {
            color: #2b85e4;
        }
        .el-dialog__body {
            padding: 10px 15px 0 15px;
        }
    </style>
</head>
<body>
<#import "/default/lib/form.ftl" as mf>
<div id="main" v-cloak>
    <el-form ref="TopForm" :model="TopForm" :inline="true" :model="formInline" size="small">
        <el-form-item label="列表地址" prop="url" :rules="{ required: true, message: '地址不能为空'}">
            <el-select v-model="TopForm.url" filterable placeholder="请选择地址" requ style="width: 360px;">
                <el-option  v-for="item in pUrls" :key="item.value" :label="item.label" :value="item.value"></el-option>
            </el-select>
            <i class="el-icon-circle-plus btn-add-url" @click="ruleDlg.visible = true;"></i>
        </el-form-item>
        <el-checkbox v-model="TopForm.force">强制更新</el-checkbox>
        <el-button @click="handleGetPL()" type="primary" size="small">抓取页面列表</el-button>
        <el-button @click="handlePostAll()" type="primary" size="small">发送整个列表</el-button>
    </el-form>
    <el-dialog :title="ruleDlg.title" :visible.sync="ruleDlg.visible" top="30px" width="500px">
        <el-form ref="ruleForm" :model="ruleDlg.ruleForm" label-width="80px" size="small">
            <@mf.Hform items=rule.fields formData='ruleDlg.ruleForm' refName='ruleForm' />
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="ruleDlg.visible = false" size="small">取 消</el-button>
            <el-button :loading="ruleDlg.okBtnLoading" type="primary" @click="handleAddRule()" size="small">保 存</el-button>
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
</div>
<#--<script src="//unpkg.com/vue/dist/vue.js"></script>-->
<#--<script src="//unpkg.com/element-ui@2.2.0/lib/index.js"></script>-->
<script type="text/javascript" src="/js/jquery.ajax.js"></script>
<script type="text/javascript" src="/js/vue.min.js"></script>
<script type="text/javascript" src="/element/element-ui.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript">
    var main = new Vue({
        el: '#main',
        data: {
            TopForm: {url: '', force: false},
            pUrls: [
                <#list lsr![] as sr>
                    {value: '${sr.pUrl}', label: '${sr.pUrl}'}<#sep>,</#sep>
                </#list>
            ],
            ruleDlg: {
                visible: false,
                index: -1,
                title: '添加规则',
                okBtnLoading: false,
                ruleForm: {pUrl: '', list: '', title: '', url: '', content: '', tags: ''}
            },
            listData: [
                <#list apl![] as pl>
                    { id: '${pl.id!"#"}', title: '${pl.title?js_string}', url: '${pl.url?js_string}', grab: ${pl.isGrab?js_string}, post: ${pl.isPost?js_string}}<#sep>,</#sep>
                </#list>
            ],
            tableLoading: false
        },
        methods: {
            handleAddRule:function() {
                var $this = this;
                this.$refs.ruleForm.validate(function(valid) {
                    if (valid) {
                        $this.ruleDlg.okBtnLoading = true;
                        myPost('/spider/AddRule', $this.ruleDlg.ruleForm,
                                function(data){
                                    if(data.flag){
                                        $this.$message.success(data.msg);
                                        $this.ruleDlg.visible = false;
                                        $this.pUrls.push({value: $this.ruleDlg.ruleForm.pUrl, label: $this.ruleDlg.ruleForm.pUrl});
                                        $this.TopForm.url = $this.ruleDlg.ruleForm.pUrl;
                                    }else{
                                        $this.$message.error(data.msg);
                                        var rules = data.items.rule.fields;
                                        $this.$nextTick(function() {
                                            for(var key in rules) {
                                                $this.$refs['ruleForm_' + key].error = rules[key]['error'];
                                            }
                                        });
                                    }
                                    //$this.ruleDlg.okBtnLoading = false;
                                },
                                function(req, textStatus){
                                    $this.$message.error(textStatus);
                                    //$this.ruleDlg.okBtnLoading = false;
                                },
                                function(req, textStatus){
                                    $this.ruleDlg.okBtnLoading = false;
                                }
                        );
                    }
                });
            },
            handleGetPL: function(){
                var $this = this;
                this.$refs.TopForm.validate(function(valid) {
                    if (valid) {
                        $this.tableLoading = true;
                        myPost('/spider/GetPL', $this.TopForm,
                                function(data){
                                    if(data.flag){
                                        $this.$message.success(data.msg);
                                        //$this.listData = $this.listData.concat(data.items.lpl);
                                        $this.listData = data.items.lpl;
                                        // $this.pUrls = [];
                                        // for(pUrl in data.items.pUrls) {
                                        //     $this.pUrls.push({value: pUrl, label: pUrl});
                                        // }
                                    }else{
                                        $this.$message.error(data.msg);
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
                myPost('/spider/GetPC/'+$this.listData[index].id, {},
                        function(data){
                            if(data.flag){
                                $this.$message.success(data.msg);
                                $this.listData[index].grab = true;
                                //$this.$alert(data.html);
                            }else{
                                $this.$message.error(data.msg);
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
                myPost('/spider/PostZootopia/'+$this.listData[index].id, {},
                        function(data){
                            if(data.flag){
                                $this.$message.success(data.msg);
                                $this.listData[index].post = true;
                            }else{
                                $this.$message.error(data.msg);
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
                myPost('/spider/PostZootopia/all', $this.TopForm,
                        function(data){
                            if(data.flag){
                                $this.$message.success(data.msg);
                                $this.listData = data.items.lpl;
                            }else{
                                $this.$message.error(data.msg);
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