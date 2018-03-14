<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>userList</title>
    <link rel="stylesheet" charset="utf-8" href="/element/style/style.css"/>
    <link rel="stylesheet" charset="utf-8" href="/css/style.css"/>
    <style>
        #permissionList {
            margin: 10px;
        }
        .el-message-box{
            display: block;
            margin: 100px auto 0px auto;
        }
        .el-dialog__body {
            padding: 5px 20px;
        }
    </style>
</head>
<body>
<#import "/default/lib/form.ftl" as mf>
<div id="permissionList" v-cloak>
    <el-button @click="handleAdd()" type="primary" size="mini">添加权限</el-button>
    <el-table :data="listData" border style="width: 100%" size="small"
              :row-class-name="tableRowClassName"
              v-loading="tableLoading"
              element-loading-text="数据处理中..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)">
        <el-table-column prop="id" label="编号" width="180"></el-table-column>
        <el-table-column prop="code" label="编码" width="180"></el-table-column>
        <el-table-column prop="type" label="名称" width="180"></el-table-column>
        <el-table-column prop="description" label="描述"></el-table-column>
        <el-table-column label="操作" width="220">
            <template slot-scope="scope">
                <el-button @click="handleEdit(scope.$index)" type="primary" size="mini">编辑</el-button>
                <el-button @click="handleDelete(scope.$index)" type="danger" size="mini">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-dialog :title="permissionDlg.title" :visible.sync="permissionDlg.visible" top="30px" width="400px">
        <el-form ref="permissionForm" :model="permissionDlg.permissionForm" label-width="80px" size="small">
             <@mf.Hform items=permission.fields formData='permissionDlg.permissionForm' refName="permissionForm" />
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="permissionDlg.visible = false" size="small">取 消</el-button>
            <el-button :loading="permissionDlg.okBtnLoading" type="primary" @click="savePermission()" size="small">确 定</el-button>
        </div>
    </el-dialog>
</div>
<script type="text/javascript" src="/js/jquery.ajax.js"></script>
<script type="text/javascript" src="/js/vue.min.js"></script>
<script type="text/javascript" src="/element/element-ui.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript">
    var permissionList = new Vue({
        el: '#permissionList',
        data: {
            listData: [
                <#list permissions as permission>
                    { id: '${permission.id?c}', code: '${permission.code?js_string}', name: '${permission.name?js_string}', type: '${permission.type?js_string}', description: '${permission.description?js_string}' }<#sep>,</#sep>
                </#list>
            ],
            tableLoading: false,
            deleteRow: {index: -1, className: ''},
            permissionModel: {id: '', code: '', name: '', type: '', description: ''},
            permissionDlg: {
                visible: false,
                index: -1,
                title: '添加权限',
                okBtnLoading: false,
                permissionForm: {}
            }
        },
        methods: {
            initDlg: function(index, title){
                var $this = this;
                if(index < 0){
                    this.permissionDlg.permissionForm = JSON.parse(JSON.stringify(this.permissionModel));
                }else{
                    this.permissionDlg.permissionForm = JSON.parse(JSON.stringify(this.listData[index]));
                }
                this.permissionDlg.index = index;
                this.permissionDlg.visible = true;
                this.permissionDlg.title = title
                this.permissionDlg.okBtnLoading = false;
                if(this.$refs.permissionForm) {
                    this.$refs.permissionForm.clearValidate();
                }
            },
            handleAdd: function() {
                this.initDlg(-1, '添加权限');
            },
            handleEdit: function(index){
                this.initDlg(index, '修改权限');
            },
            handleDelete: function(index){
                delRow(this, index, '/permission/delete/'+this.listData[index].id, '您确定要删除此权限么？');
            },
            savePermission: function(){
                var $this = this;
                this.$refs.permissionForm.validate(function(valid) {
                    if (valid) {
                        $this.permissionDlg.okBtnLoading = true;
                        myPost('/permission/save', $this.permissionDlg.permissionForm,
                                function(data){
                                    if(data.flag){
                                        $this.$message.success(data.msg);
                                        $this.permissionDlg.visible = false;
                                        if($this.permissionDlg.permissionForm.id === '') {
                                            $this.listData.unshift(data.items.newObj);
                                        }else{
                                            $this.$set($this.listData, $this.permissionDlg.index, data.items.newObj);
                                        }
                                    }else{
                                        $this.$message.error(data.msg);
                                        var fields = data.items.permission.fields;
                                        $this.$nextTick(function() {
                                            for(var key in fields) {
                                                $this.$refs['permissionForm_' + key].error = fields[key]['error'];
                                            }
                                        });
                                    }
                                },
                                function(req, textStatus){
                                    $this.$message.error(textStatus);
                                },
                                function(req, textStatus){
                                    $this.permissionDlg.okBtnLoading = false;
                                }
                        );
                    }
                });
            },
            tableRowClassName: function(row) {
                //{row, rowIndex}
                if (row.rowIndex === this.deleteRow.index) {
                    return this.deleteRow.className;
                } else {
                    return '';
                }
            }
        }
    });
</script>
</body>
</html>