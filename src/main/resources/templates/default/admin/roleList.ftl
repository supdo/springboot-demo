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
        #roleList {
            margin: 10px;
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
              v-loading="tableLoading"
              element-loading-text="数据处理中..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)">
        <el-table-column prop="id" label="编号" width="180"></el-table-column>
        <el-table-column prop="code" label="编码" width="180"></el-table-column>
        <el-table-column prop="name" label="名称" width="180"></el-table-column>
        <el-table-column prop="description" label="描述"></el-table-column>
        <el-table-column label="操作" width="220">
            <template slot-scope="scope">
                <el-button @click="handleEdit(scope.$index)" type="primary" size="mini">编辑</el-button>
                <el-button @click="handleRole(scope.$index)" type="primary" size="mini">权限</el-button>
                <el-button @click="handleDelete(scope.$index)" type="danger" size="mini">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
</div>
<script type="text/javascript" src="/js/jquery.ajax.js"></script>
<script type="text/javascript" src="/js/vue.min.js"></script>
<script type="text/javascript" src="/element/element-ui.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript">
    var roleList = new Vue({
        el: '#roleList',
        data: {
            listData: [
                <#list roles as role>
                    { id: '${role.id}', code: '${role.code?js_string}', name: '${role.name?js_string}', description: '${role.description?js_string}', myPermission: [<#list role.permissionSet![] as permission>'${permission.id}'<#sep>,</#sep></#list>] }<#sep>,</#sep>
                </#list>
            ],
            tableLoading: false
        },
        methods: {
            handleAdd: function() {

            }
        }
    });
</script>
</body>
</html>