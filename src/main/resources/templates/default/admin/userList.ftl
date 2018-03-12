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
        #userList {
            margin: 10px;
        }
        .el-dialog__body {
            padding: 5px 20px;
        }
    </style>
</head>
<body>
<#import "/default/lib/form.ftl" as mf>
<div id="userList" v-cloak>
    <el-button @click="handleAdd()" type="primary" size="mini">添加新用户</el-button>
    <el-table :data="listData" border style="width: 100%" size="small"
              v-loading="tableLoading"
              element-loading-text="数据处理中..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)">
        <el-table-column prop="id" label="编号" width="180"></el-table-column>
        <el-table-column prop="username" label="姓名" width="180"></el-table-column>
        <el-table-column prop="nickname" label="昵称"></el-table-column>
        <el-table-column label="操作" width="220">
            <template slot-scope="scope">
                <el-button @click="handleEdit(scope.$index)" type="primary" size="mini">编辑</el-button>
                <el-button @click="handleRole(scope.$index)" type="primary" size="mini">角色</el-button>
                <el-button @click="handleDelete(scope.$index)" type="danger" size="mini">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-dialog :title="userDlg.title" :visible.sync="userDlg.visible" top="30px" width="400px">
        <el-form ref="userForm" :model="userDlg.userForm" label-width="80px" size="small">
             <@mf.Hform items=user.fields formData='userDlg.userForm' refName='userForm' />
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="userDlg.visible = false" size="small">取 消</el-button>
            <el-button :loading="userDlg.okBtnLoading" type="primary" @click="addUser()" size="small">确 定</el-button>
        </div>
    </el-dialog>
    <el-dialog :title="roleDlg.title" :visible.sync="roleDlg.visible" top="30px" width="400px">
        <el-checkbox-group v-model="roleDlg.myRoles">
            <el-checkbox v-for="role in roleSet" :label="role.id" :key="role.id">{{role.name}}</el-checkbox>
        </el-checkbox-group>
        <div slot="footer" class="dialog-footer">
            <el-button @click="roleDlg.visible = false" size="small">取 消</el-button>
            <el-button :loading="roleDlg.okBtnLoading" type="primary" @click="setRoles()" size="small">确 定</el-button>
        </div>
    </el-dialog>
</div>
<#--<script src="//unpkg.com/vue/dist/vue.js"></script>-->
<#--<script src="//unpkg.com/element-ui@2.2.0/lib/index.js"></script>-->
<script type="text/javascript" src="/js/jquery.ajax.js"></script>
<script type="text/javascript" src="/js/vue.min.js"></script>
<script type="text/javascript" src="/element/element-ui.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript">
    var userList = new Vue({
        el: '#userList',
        data: {
            listData: [
                <#list users as user>
                    { id: '${user.id?c}', username: '${user.username?js_string}', nickname: '${user.nickname?js_string}', myRoles: [<#list myRoles[user.id?c]![] as role_id>'${role_id?c}'<#sep>,</#sep></#list>] }<#sep>,</#sep>
                </#list>
            ],
            tableLoading: false,
            userDlg: {
                visible: false,
                title: '添加用户',
                okBtnLoading: false,
                userForm: {}
            },
            userModel: {id:'', username:'', password: '', rePassword: '', nickname: ''},

            roleDlg: {
                visible: false,
                index: -1,
                title: '选择角色',
                okBtnLoading: false,
                myRoles: []
            },
            roleSet: [
                <#list roleList as role>
                    {id: '${role.id?c}', code: '${role.code?js_string}', name: '${role.name?js_string}'}<#sep>,</#sep>
                </#list>
            ]
        },
        methods: {
            inituserDlg: function(index, title) {
                if(index<0) {
                    this.userDlg.userForm = JSON.parse(JSON.stringify(this.userModel));
                }else{
                    this.userDlg.userForm = JSON.parse(JSON.stringify(this.listData[index]));
                }
                this.userDlg.visible = true;
                this.userDlg.title = title;
                this.userDlg.okBtnLoading = false;
                if(this.$refs.userForm){
                    this.$refs.userForm.clearValidate();
                }
            },
            handleEdit: function(index){
                this.inituserDlg(index, '编辑用户');
            },
            handleDelete: function(index){
                var $this = this;
                this.tableLoading = true;
                myPost('/user/delete/'+this.listData[index].id, {},
                    function(data){
                        if(data.flag) {
                            $this.listData.splice(index, 1);
                            $this.$message.success(data.msg);
                        }else{
                            $this.$message.error(data.msg);
                        }
                        $this.tableLoading = false;
                    }
                );
            },
            handleAdd: function() {
                this.inituserDlg(-1, '添加用户');
            },
            addUser: function(){
                var $this = this;
                this.$refs.userForm.validate(function(valid) {
                    if (valid) {
                        $this.userDlg.okBtnLoading = true;
                        myPost('/user/save', $this.userDlg.userForm,
                            function(data){
                                if(data.flag){
                                    $this.$message.success(data.msg);
                                    $this.userDlg.visible = false;
                                    $this.listData.unshift(data.items.newObj);
                                }else{
                                    $this.$message.error(data.msg);
                                    var users = data.items.user.fields;
                                    $this.$nextTick(function() {
                                        for(var key in users) {
                                            $this.$refs['userForm_' + key].error = users[key]['error'];
                                        }
                                    });
                                }
                            },
                            function(req, textStatus){
                                $this.$message.error(textStatus);
                            },
                            function(req, textStatus){
                                $this.userDlg.okBtnLoading = false;
                            }
                        );
                    }
                });
            },
            handleRole: function(index){
                this.roleDlg.visible = true;
                this.roleDlg.index = index;
                this.roleDlg.myRoles = JSON.parse(JSON.stringify(this.listData[index].myRoles));
            },
            setRoles: function(){
                var $this = this;
                myPost('/user/setRole/'+$this.listData[$this.roleDlg.index].id, {roles: $this.roleDlg.myRoles.join('|')},
                    function(data){
                        if(data.flag) {
                            $this.$message.success(data.msg);
                            $this.listData[$this.roleDlg.index].myRoles = JSON.parse(JSON.stringify($this.roleDlg.myRoles));
                            $this.roleDlg.visible = false;
                        }else{
                            $this.$message.error(data.msg);
                        }
                    }
                );
            }
        }
    });
</script>
</body>
</html>