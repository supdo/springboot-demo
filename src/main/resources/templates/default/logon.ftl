<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>首页</title>
    <link rel="stylesheet" type="text/css" href="/iview/styles/iview.css">
    <link ref="stylesheet" type="text/css" href="/style/style.css">
    <style scoped>
        body {
            background-color: #F9F9F9;
        }
        [v-cloak] {
            display: none;
        }
        #app {
            margin-top: 200px;
        }
    </style>
</head>
<body>
<#import "/default/lib/form.ftl" as mf>
<div id="logonApp" v-cloak>
    <i-form ref="logonForm" :model="formData" :label-width="80">
        <@mf.Hform items=user.fields/>
    </i-form>
</div>
<script type="text/javascript" src="/js/vue.js"></script>
<script type="text/javascript" src="/iview/iview.js"></script>
<script type="text/javascript">
    var app = new Vue({
        el: "#logonApp",
        data: {
            formData: {
                name: ""
            }
        },
        methods: {

        }
    });
</script>
</body>
</html>