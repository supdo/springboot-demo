<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Login</title>
    <link rel="stylesheet" charset="utf-8" href="/element/style/style.css"/>
    <style type="text/css">
        body {
            margin: 0px;
        }
        [v-cloak] {
            display: none;
        }
    </style>
</head>
<body>
<div id="app" v-cloak>

</div>
<script type="text/javascript" src="/js/vue.js"></script>
<script type="text/javascript" src="/element/element-ui.min.js"></script>
<script type="text/javascript">
    var Main = {
        data: function() {
            return {
                isCollapse: true
            };
        },
        methods: {
            handleOpen: function(key, keyPath) {
                console.log(key, keyPath);
            },
            handleClose: function(key, keyPath) {
                console.log(key, keyPath);
            }
        }
    }
    var Ctor = Vue.extend(Main);
    new Ctor().$mount('#app');
</script>
</body>
</html>