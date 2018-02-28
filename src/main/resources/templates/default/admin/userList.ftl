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
    <el-table :data="listData" border style="width: 100%" size="small">
        <el-table-column prop="id" label="编号" width="180"></el-table-column>
        <el-table-column prop="username" label="姓名" width="180"></el-table-column>
        <el-table-column prop="nickname" label="昵称"></el-table-column>
        <el-table-column label="操作" width="180">
            <template slot-scope="scope">
                <el-button @click="handleEdit(scope.$index)" type="primary" size="mini">编辑</el-button>
                <el-button @click="handleDelete(scope.$index)" type="danger" size="mini">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-dialog :title="addUserDlg.title" :visible.sync="addUserDlg.visible" top="30px" width="400px">
        <el-form ref="addUserForm" :model="userForm" label-width="80px" size="small">
             <@mf.Hform items=user.fields formData='userForm' formError='userFormError' />
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="addUserDlg.visible = false" size="small">取 消</el-button>
            <el-button :loading="addUserDlg.okBtnLoading" type="primary" @click="addUser()" size="small">确 定</el-button>
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
                { id: '${user.id}', username: '${user.username}', nickname: '${user.nickname}' }<#sep>,</#sep>
            </#list>
            ],
            addUserDlg: {
                visible: false,
                title: '添加用户',
                okBtnLoading: false
            },
            userModel: {id:'', username:'', password: '', rePassword: '', nickname: ''},
            userFormError: {},
            userForm: {}
        },
        methods: {
            initAddUserDlg: function(index, title) {
                if(index<0) {
                    this.userForm = JSON.parse(JSON.stringify(this.userModel));
                }else{
                    this.userForm = JSON.parse(JSON.stringify(this.listData[index]));
                }
                this.addUserDlg.visible = true;
                this.addUserDlg.title = title;
                this.addUserDlg.okBtnLoading = false;
                //this.$refs.addUserForm.clearValidate();
            },
            handleEdit: function(index){
                this.initAddUserDlg(index, '编辑用户');
            },
            handleDelete: function(index){
                this.listData.splice(index, 1);
            },
            handleAdd: function() {
                this.initAddUserDlg(-1, '添加用户');
            },
            addUser: function(){
                var $this = this;
                //this.$refs.addUserForm.clearValidate();
                this.$refs.addUserForm.validate(function(valid) {
                    if (valid) {
                        $this.addUserDlg.okBtnLoading = true;
                        myPost('/user/add', $this.userForm,
                                function(data){
                                    if(data.flag){
                                        $this.$message.success(data.msg);
                                        this.addUserDlg.visible = false;
                                    }else{
                                        $this.$message.error(data.msg);
                                        var users = data.items.user.fields;
                                        for(var key in users) {
                                            $this.$refs['userFormError_' + key].error = null;
                                            //$this.$refs['userFormError_' + key].error = users[key]['error'];
                                            $this.$nextTick(function() {
                                                $this.$refs['userFormError_' + key].error = users[key]['error'];
                                            });
                                        }
                                    }
                                },
                                function(req, textStatus){
                                    $this.$message.error(textStatus);
                                },
                                function(req, textStatus){
                                    $this.addUserDlg.okBtnLoading = false;
                                }
                        );
                    }
                });
            }
        }
    });
</script>
</body>
</html>