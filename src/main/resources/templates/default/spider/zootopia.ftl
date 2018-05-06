<#import "../lib/layout.ftl" as layout>
<#assign style>
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
        .el-dialog__body {
            padding: 10px 15px 0 15px;
        }
    </style>
</#assign>
<@layout.default title="Zootopia" style=style>
Zootopia
<div id="main" v-cloak>
    <el-form ref="TopForm" :inline="true" size="small">
        <el-button type="primary" size="small">抓取页面列表</el-button>
    </el-form>
</div>
<@layout.defaultjs />
<script type="text/javascript">
    var main = new Vue({
        el: '#main',
        data: {},
        mounted: function () {

        },
        methods: {}
    });
</script>
</@layout.default>