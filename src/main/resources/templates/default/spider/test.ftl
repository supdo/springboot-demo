<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Spider Test</title>
    <link rel="stylesheet" charset="utf-8" href="/element/style/style.css"/>
    <link rel="stylesheet" charset="utf-8" href="/css/style.css"/>
    <style>
        #main {
            margin: 10px;
        }
    </style>
</head>
<body>
<#list apl![] as pl>
${pl.title}:${pl.url}<br>
</#list>
<div id="main" v-cloak>

</div>
<#--<script src="//unpkg.com/vue/dist/vue.js"></script>-->
<#--<script src="//unpkg.com/element-ui@2.2.0/lib/index.js"></script>-->
<script type="text/javascript" src="/js/jquery.ajax.js"></script>
<script type="text/javascript" src="/js/vue.min.js"></script>
<script type="text/javascript" src="/element/element-ui.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript">
    var main = new Vue({
        el: '#main',
        data: {

        },
        methods: {

        }
    });
</script>
</body>
</html>