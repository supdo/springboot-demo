<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Default</title>
    <link rel="stylesheet" charset="utf-8" href="/element/style/style.css"/>
    <link rel="stylesheet" charset="utf-8" href="/css/style.css" />
    <style type="text/css">
        #app {

        }
        .app-aside {
            background-color: rgb(84, 92, 100);
            overflow: hidden;
        }
        .app-logo {
            border-right: solid 1px #e6e6e6;
        }
        .app-logo-img {
            width: 170px;
            margin: 5px 0px 0px 15px;
        }
        .app-header {
            margin: 0px;
            padding: 0px;
        }
        .app-header-right {
            float: right;
            margin-top:20px;
            outline: none;
            padding: 0px 15px;
            cursor: pointer;
        }
        .app-main {
            padding: 10px;
        }

    </style>
</head>
<body>
<div id="app" v-cloak>
    <el-container>
        <el-aside class="app-aside" style="width:202px;">
            <div class="app-logo"><img src="/image/logo.jpg" class="app-logo-img" /></div>
            <el-menu default-active="1-4-1" class="el-menu-vertical-demo" background-color="#545c64"
                     text-color="#fff"
                     active-text-color="#ffd04b">
                <el-submenu index="1">
                    <template slot="title">
                        <i class="el-icon-location"></i>
                        <span slot="title">导航一</span>
                    </template>
                    <el-menu-item-group>
                        <span slot="title">分组一</span>
                        <el-menu-item index="1-1">选项1</el-menu-item>
                        <el-menu-item index="1-2">选项2</el-menu-item>
                    </el-menu-item-group>
                    <el-menu-item-group title="分组2">
                        <el-menu-item index="1-3">选项3</el-menu-item>
                    </el-menu-item-group>
                    <el-submenu index="1-4">
                        <span slot="title">选项4</span>
                        <el-menu-item index="1-4-1">选项1</el-menu-item>
                    </el-submenu>
                </el-submenu>
                <el-menu-item index="2">
                    <i class="el-icon-menu"></i>
                    <span slot="title">导航二</span>
                </el-menu-item>
                <el-menu-item index="3" disabled>
                    <i class="el-icon-document"></i>
                    <span slot="title">导航三</span>
                </el-menu-item>
                <el-menu-item index="4">
                    <i class="el-icon-setting"></i>
                    <span slot="title">导航四</span>
                </el-menu-item>
            </el-menu>
        </el-aside>
        <el-container>
            <el-header class="app-header">
                <el-menu default-active="1" class="el-menu-demo" mode="horizontal">
                    <el-menu-item index="1">首页</el-menu-item>
                    <el-menu-item index="3">系统管理</el-menu-item>
                    <el-menu-item index="4">业务管理</el-menu-item>
                    <div class="app-header-right">
                        <el-dropdown>
                            <span class="el-dropdown-link">下拉菜单<i class="el-icon-arrow-down el-icon--right"></i></span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item>个人中心</el-dropdown-item>
                                <el-dropdown-item><a href="/logout">退出</a></el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </div>
                </el-menu>
            </el-header>
            <el-main class="app-main">
                <el-tabs type="card" closable>
                    <el-tab-pane label="tab1" name="1">
                        tab1 content
                    </el-tab-pane>
                    <el-tab-pane label="tab2" name="2">
                        tab2 content
                    </el-tab-pane>
                </el-tabs>
            </el-main>
        </el-container>
    </el-container>
</div>
<script type="text/javascript" src="/js/vue.min.js"></script>
<script type="text/javascript" src="/element/element-ui.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript">
    var app = new Vue({
        el: '#app',
        data: {

        }
    });
</script>
</body>
</html>