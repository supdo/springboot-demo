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
<div id="app" v-cloak>
<a href="/login">登录</a>
</div>
<script type="text/javascript" src="/js/vue.js"></script>
<script type="text/javascript" src="/iview/iview.js"></script>
<script type="text/javascript">
var loginApp = new Vue({
    el: "#app",
    data: {

    },
    methods: {

    }
});
</script>
</body>
</html>