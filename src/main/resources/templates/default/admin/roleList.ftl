<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>userList</title>
    <link rel="stylesheet" charset="utf-8" href="${request.contextPath}/element/style/style.css"/>
    <link rel="stylesheet" charset="utf-8" href="${request.contextPath}/css/style.css"/>
    <style>
        #roleList {
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
<div id="roleList" v-cloak>
    <el-button @click="handleAdd()" type="primary" size="mini">添加角色</el-button>
    <el-table :data="listData" border style="width: 100%" size="small"
              :row-class-name="tableRowClassName"
              v-loading="tableLoading"
              element-loading-text="数据处理中..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)">
        <el-table-column prop="id" label="编号" width="180"></el-table-column>
        <el-table-column prop="code" label="编码" width="180"></el-table-column>
        <el-table-column prop="name" label="名称" width="180"></el-table-column>
        <el-table-column prop="type" label="类型" width="180"></el-table-column>
        <el-table-column prop="description" label="描述"></el-table-column>
        <el-table-column label="操作" width="220">
            <template slot-scope="scope">
                <el-button @click="handleEdit(scope.$index)" type="primary" size="mini">编辑</el-button>
                <el-button @click="handlePermission(scope.$index)" type="primary" size="mini">权限</el-button>
                <el-button @click="handleDelete(scope.$index)" type="danger" size="mini">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-dialog :title="roleDlg.title" :visible.sync="roleDlg.visible" top="30px" width="400px">
        <el-form ref="roleForm" :model="roleDlg.roleForm" label-width="80px" size="small">
             <@mf.Hform items=role.fields formData='roleDlg.roleForm' refName="roleForm" />
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="roleDlg.visible = false" size="small">取 消</el-button>
            <el-button :loading="roleDlg.okBtnLoading" type="primary" @click="saveRole()" size="small">确 定</el-button>
        </div>
    </el-dialog>
    <el-dialog title="选择权限" :visible.sync="permissionDlg.visible" top="30px" width="400px">
        <el-checkbox-group v-model="permissionDlg.myPermissions">
            <el-checkbox v-for="permission in permissionList" :label="permission.id" :key="permission.id">{{permission.name}}({{permission.code}})</el-checkbox>
        </el-checkbox-group>
        <div slot="footer" class="dialog-footer">
            <el-button @click="permissionDlg.visible = false" size="small">取 消</el-button>
            <el-button :loading="permissionDlg.okBtnLoading" type="primary" @click="setPermission()" size="small">确 定</el-button>
        </div>
    </el-dialog>
</div>
<script type="text/javascript" src="${request.contextPath}/js/jquery.ajax.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/vue.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/element/element-ui.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/common.js"></script>
<script type="text/javascript">
    var roleList = new Vue({
        el: '#roleList',
        data: {
            listData: [
                <#list roles as role>
                    { id: '${role.id?c}', code: '${role.code?js_string}', name: '${role.name?js_string}', description: '${role.description?js_string}', myPermissions: [<#list myPermissions[role.id?c]![] as permission_id>'${permission_id?c}'<#sep>,</#sep></#list>] }<#sep>,</#sep>
                </#list>
            ],
            tableLoading: false,
            deleteRow: {index: -1, className: ''},
            roleModel: {id: '', code: '', name: '', description: ''},
            roleDlg: {
                visible: false,
                index: -1,
                title: '添加角色',
                okBtnLoading: false,
                roleForm: {}
            },
            permissionDlg: {
                visible: false,
                index: -1,
                okBtnLoading: false,
                myPermissions: {}
            },
            permissionList: [
                <#list permissionList as permission>
                    {id: '${permission.id}', code: '${permission.code?js_string}', name: '${permission.name?js_string}'}<#sep>,</#sep>
                </#list>
            ]
        },
        methods: {
            initroleDlg: function(index, title){
                var $this = this;
                if(index < 0){
                    this.roleDlg.roleForm = JSON.parse(JSON.stringify(this.roleModel));
                }else{
                    this.roleDlg.roleForm = JSON.parse(JSON.stringify(this.listData[index]));
                }
                this.roleDlg.index = index;
                this.roleDlg.visible = true;
                this.roleDlg.title = title
                this.roleDlg.okBtnLoading = false;
                //首次显示前this.$refs中没有roleForm，因为roleForm还没加载到虚拟dom中。
                if(this.$refs.roleForm) {
                    this.$refs.roleForm.clearValidate();
                }
            },
            handleAdd: function() {
                this.initroleDlg(-1, '添加角色');
            },
            handleEdit: function(index){
                this.initroleDlg(index, '修改角色');
            },
            handleDelete: function(index){
                delRow(this, index, '${request.contextPath}/role/delete/'+this.listData[index].id, '您确定要删除此角色么？');
            },
            handlePermission: function(index){
                this.permissionDlg.visible = true;
                this.permissionDlg.index = index;
                this.permissionDlg.myPermissions = this.listData[index].myPermissions;
            },
            saveRole: function(){
                var $this = this;
                this.$refs.roleForm.validate(function(valid) {
                    if (valid) {
                        $this.roleDlg.okBtnLoading = true;
                        myPost('${request.contextPath}/role/save', $this.roleDlg.roleForm,
                            function (data) {
                                if (data.flag) {
                                    $this.$message.success(data.msg);
                                    $this.roleDlg.visible = false;
                                    if($this.roleDlg.roleForm.id === '') {
                                        $this.listData.unshift(data.items.newObj);
                                    }else{
                                        $this.$set($this.listData, $this.roleDlg.index, data.items.newObj);
                                    }
                                } else {
                                    $this.$message.error(data.msg);
                                    var roles = data.items.role.fields;
                                    $this.$nextTick(function () {
                                        for (var key in roles) {
                                            $this.$refs['roleForm_' + key].error = roles[key]['error'];
                                        }
                                    });
                                }
                            },
                            function (req, textStatus) {
                                $this.$message.error(textStatus);
                            },
                            function (req, textStatus) {
                                $this.roleDlg.okBtnLoading = false;
                            }
                        );
                    }
                });
            },
            setPermission: function(){
                var $this = this;
                $this.permissionDlg.okBtnLoading = true;
                myPost('${request.contextPath}/role/setPermission/'+$this.listData[$this.permissionDlg.index].id, {permissions: $this.permissionDlg.myPermissions.join('|')},
                    function(data){
                        if(data.flag) {
                            $this.$message.success(data.msg);
                            $this.listData[$this.permissionDlg.index].myPermissions = JSON.parse(JSON.stringify($this.permissionDlg.myPermissions));
                            $this.permissionDlg.visible = false;
                        }else{
                            $this.$message.error(data.msg);
                        }
                    },
                    function(req, textStatus){
                        $this.$message.error(textStatus);
                    },
                    function(req, textStatus){
                        $this.permissionDlg.okBtnLoading = false;
                    }
                );
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