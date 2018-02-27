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
    <el-button @click="addUserDlgVisible = true" type="primary" size="mini">添加新用户</el-button>
    <template>
        <el-table :data="listData" border style="width: 100%" size="small">
            <el-table-column prop="id" label="日期" width="180"></el-table-column>
            <el-table-column prop="username" label="姓名" width="180"></el-table-column>
            <el-table-column prop="nickname" label="地址"></el-table-column>
            <el-table-column label="操作" width="180">
                <template slot-scope="scope">
                    <el-button @click="handleEdit(scope.$index)" type="primary" size="mini">编辑</el-button>
                    <el-button @click="handleDelete(scope.$index)" type="danger" size="mini">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </template>
    <el-dialog :title="addUserDlgTitle" :visible.sync="addUserDlgVisible" top="30px" width="400px">
        <el-form ref="addUserForm" :model="userData" label-width="80px" size="small">
             <@mf.Hform items=user.fields formData='userData'/>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="addUserDlgVisible = false" size="small">取 消</el-button>
            <el-button type="primary" @click="addUserDlgVisible = false" size="small">确 定</el-button>
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
            addUserDlgVisible: false,
            addUserDlgTitle: '添加用户',
            userData: {}
        },
        methods: {
            handleEdit: function(index){
                this.userData = this.listData[index];
                this.addUserDlgVisible = true;
            },
            handleDelete: function(index){
                this.listData.splice(index, 1);
            },
            addUser: function(){

            }
        }
    });
</script>
</body>
</html>