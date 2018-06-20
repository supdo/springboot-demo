<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>规则管理</title>
    <link rel="stylesheet" charset="utf-8" href="${request.contextPath}/element/style/style.css"/>
    <link rel="stylesheet" charset="utf-8" href="${request.contextPath}/css/style.css"/>
    <style>
        #main {
            margin: 10px;
        }
        .el-table--small td{
            padding: 2px 0;
        }
        .el-dialog__body {
            padding: 10px 20px 0 20px;
        }
    </style>
</head>
<body>
<#import "/default/lib/form.ftl" as mf>
<div id="main" v-cloak>
    <el-button @click="handleAdd()" type="primary" size="mini">新增规则</el-button>
    <el-dialog :title="itemDlg.title" :visible.sync="itemDlg.visible" top="30px" width="500px">
        <el-form ref="itemForm" :model="itemDlg.itemForm" label-width="80px" size="small">
            <@mf.Hform items=itemForm.fields formData='itemDlg.itemForm' refName='itemForm' itemOptions='itemOptions' />
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="itemDlg.visible = false" size="small">取 消</el-button>
            <el-button :loading="itemDlg.okBtnLoading" type="primary" @click="saveItem()" size="small">保 存</el-button>
        </div>
    </el-dialog>
    <el-table :data="listData" border style="width: 100%" size="small"
              :row-class-name="tableRowClassName"
              v-loading="tableLoading"
              element-loading-text="数据处理中..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)">
        <el-table-column prop="id" label="编号" width="60"></el-table-column>
        <el-table-column prop="code" label="编码" width="60"></el-table-column>
        <el-table-column prop="name" label="名称"></el-table-column>
        <el-table-column prop="pages" label="分页"></el-table-column>
        <el-table-column prop="list" label="列表"></el-table-column>
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="url" label="Url"></el-table-column>
        <el-table-column prop="content" label="内容"></el-table-column>
        <el-table-column prop="valid" label="生效" width="60">
            <template slot-scope="scope">
                <p v-if="scope.row.valid">是</p><p v-else>否</p>
            </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
            <template slot-scope="scope">
                <el-button @click="handleEdit(scope.$index);" type="primary" size="mini">编辑</el-button>
                <el-button @click="handleDelete(scope.$index);" type="danger" size="mini">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
</div>
<#--<script src="//unpkg.com/vue/dist/vue.js"></script>-->
<#--<script src="//unpkg.com/element-ui@2.2.0/lib/index.js"></script>-->
<script type="text/javascript" src="${request.contextPath}/js/jquery.ajax.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/vue.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/element/element-ui.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/common.js"></script>
<script type="text/javascript">
    var main = new Vue({
        el: '#main',
        data: {
            itemDlg: {
                visible: false,
                index: -1,
                title: '添加规则',
                okBtnLoading: false,
                itemForm: {}
            },
            itemModel: {id: '', <#list itemForm.fields?keys as key>${key}: ''<#sep>, </#sep></#list>},
            itemOptions:{ <#list itemForm.fields?keys as key><#if itemForm.fields[key].type == 'select'>
                ${key}: {<#list itemForm.fields[key].options?keys as val>${val}: '${itemForm.fields[key].options[val]}' <#sep>, </#sep></#list>}
                <#sep>, </#sep></#if></#list> },
            listData: [
                <#list items![] as item>
                    {id: '${item.id}', <#list itemForm.fields?keys as key>${key}: <#if item[key]?is_boolean>${item[key]?string}<#else>'${item[key]?js_string}'</#if><#sep>, </#sep></#list>}<#sep>, </#sep>
                </#list>
            ],
            tableLoading: false,
            deleteRow: {index: -1, className: ''},
        },
        methods: {
            initDlg: function(index, title) {
                if(index < 0) {
                    this.itemDlg.itemForm = JSON.parse(JSON.stringify(this.itemModel));
                }else{
                    this.itemDlg.itemForm = JSON.parse(JSON.stringify(this.listData[index]));
                }
                this.itemDlg.index = index;
                this.itemDlg.visible = true;
                this.itemDlg.title = title;
                this.itemDlg.okBtnLoading = false;
                if(this.$refs.itemForm){
                    this.$refs.itemForm.clearValidate();
                }
            },
            handleAdd: function(){
                this.initDlg(-1, "添加新规则");
            },
            saveItem: function() {
                var $this = this;
                this.$refs.itemForm.validate(function (valid) {
                    if (valid) {
                        $this.itemDlg.okBtnLoading = true;
                        myPost('${request.contextPath}/spiderRule/save', $this.itemDlg.itemForm,
                                function (data) {
                                    if (data.flag) {
                                        $this.$message.success(data.msg);
                                        $this.itemDlg.visible = false;
                                        if($this.itemDlg.itemForm.id === '') {
                                            $this.listData.unshift(data.items.newItem);
                                        }else{
                                            $this.$set($this.listData, $this.itemDlg.index, data.items.newItem);
                                        }
                                    } else {
                                        $this.$message.error(data.msg);
                                        var items = data.items.items.fields;
                                        $this.$nextTick(function () {
                                            for (var key in items) {
                                                $this.$refs['itemForm_' + key].error = items[key]['error'];
                                            }
                                        });
                                    }
                                },
                                function (req, textStatus) {
                                    $this.$message.error(textStatus);
                                },
                                function (req, textStatus) {
                                    $this.itemDlg.okBtnLoading = false;
                                }
                        );
                    }
                });
            },
            handleEdit: function(index) {
                this.initDlg(index, "添加新规则");
            },
            handleDelete: function(index) {
                delRow(this, index, '${request.contextPath}/spiderRule/delete/'+this.listData[index].id, '您确定要删除此规则么？');
            },
            tableRowClassName: function(row) {
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