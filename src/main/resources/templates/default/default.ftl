<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Default</title>
    <link rel="stylesheet" charset="utf-8" href="${request.contextPath}/element/style/style.css"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/iconfont/iconfont.css">
    <link rel="stylesheet" charset="utf-8" href="${request.contextPath}/css/style.css" />
    <style type="text/css">
        body {
        }
        html {
        }
        .app-header-fullicon {
            z-index: 9999;
            position: absolute;
            top: 0px;
            right: 0px;
            width: 55px;
            text-align: center;
            line-height: 48px;
            padding: 0px 0px;
        }
        .app-header-fullicon i {
            font-size: 28px;
            cursor: pointer;
            color: #888888;
            -webkit-transition: font-size 0.25s ease-out 0s;
            -moz-transition: font-size 0.25s ease-out 0s;
            transition: font-size 0.25s ease-out 0s;
        }
        .app-header-fullicon i:hover {
            font-size: 36px;
        }
        .hide {
            display: none;
        }
        #app {
        }
        .app-aside {
            background-color: rgb(84, 92, 100);
            overflow: hidden;
            border-right: solid 1px #e6e6e6;
            width: 202px;
        }
        .app-aside-min {
            width: 62px;
        }
        .app-logo {
            border-right: solid 0px #e6e6e6;
        }
        .app-logo-img {
            width: 170px;
            margin: 5px 0px 0px 15px;
        }
        .app-logo-img-min {
            width: 60px;
            margin: 5px 0px 0px 15px;
        }
        .app-aside-menu
        {
            border-width: 0px;
        }
        .app-header {
            margin: 0px;
            margin-bottom: 4px;
            padding: 0px;
            -moz-box-shadow:0px 2px 5px #BBBBBB;
            -webkit-box-shadow:0px 2px 5px #BBBBBB;
            box-shadow:0px 2px 5px #BBBBBB;
        }
        .el-menu--horizontal{
            border-width: 0px;
        }
        .el-menu--horizontal>.el-menu-item {
            font-size: 16px;
            height: 50px;
            line-height: 50px;
        }
        .el-menu-item, .el-submenu__title {
            height: 46px;
            line-height: 46px;
        }
        .app-header-right {
            float: right;
            line-height: 50px;
            outline: none;
            margin-right: 40px;
            padding: 0px 15px;
            cursor: pointer;
        }
        .app-main {
            padding: 6px 0px 0 6px;
        }
        .el-tabs__header {
            margin-bottom: 0px;
        }
        .el-tabs__item {
            height:36px;
            line-height: 36px;
        }
        .el-tabs__content{
            border: 1px solid #e4e7ed;
            border-top: 0px;
        }

    </style>
</head>
<body>
<div id="app" v-cloak>
    <div class="app-header-fullicon">
        <i class="iconfont" :class="{'icon-fullscreen': !fullScreen, 'icon-exit-fullscreen': fullScreen}" @click="handleFullScreen"></i>
    </div>
    <el-container>
        <el-aside class="app-aside" :width="(fullScreen ? 62 : 202) + 'px'">
            <div class="app-logo"><img src="${request.contextPath}/image/logo.jpg" :class="{'app-logo-img': !fullScreen, 'app-logo-img-min': fullScreen}" /></div>
            <el-menu ref="asideMenu" default-active="0" class="app-aside-menu" :collapse="fullScreen" @select="asideSelect" background-color="#545c64" text-color="#fff" active-text-color="#ffd04b">
                <el-menu-item v-for="(menu,key,index) in asideMenu" :index="key" :key="'asideMenu-'+key">
                    <i class="el-icon-document"></i>
                    <span slot="title">{{menu.title}}</span>
                </el-menu-item>
            </el-menu>
        </el-aside>
        <el-container>
            <el-header class="app-header" :class="{hide: fullScreen}" height="50px">
                <el-menu ref="headerMenu" default-active="home" class="" mode="horizontal" @select="initAsideMenu">
                    <el-menu-item v-for="(menu,key) in menus" :index="key" :key="'headerMenu-'+key">{{menu.title}}</el-menu-item>
                    <div class="app-header-right">
                        <el-dropdown>
                            <span class="el-dropdown-link">下拉菜单<i class="el-icon-arrow-down el-icon--right"></i></span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item>个人中心</el-dropdown-item>
                                <el-dropdown-item><a href="${request.contextPath}/logout">退出</a></el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </div>
                </el-menu>
            </el-header>
            <el-main class="app-main">
                <el-tabs ref="headerTabs" type="card" value="1" @tab-remove="removeHeaderTab">
                    <el-tab-pane v-for="(tab,key,index) in headerTabs" :key="key" :label="tab.title" :name="key" :closable="key != homeIndex">
                        <iframe :src="tab.url" width="100%" :height="ifrHeight" frameborder="0"></iframe>
                    </el-tab-pane>
                </el-tabs>
            </el-main>
        </el-container>
    </el-container>
</div>
<script type="text/javascript" src="${request.contextPath}/js/vue.js"></script>
<script type="text/javascript" src="${request.contextPath}/element/element-ui.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/common.js"></script>
<script type="text/javascript">
    var app = new Vue({
        el: '#app',
        data: {
            // menuList: [
            //     {
            //         name:'home', icon:'', title:'首页', children:
            //                 [
            //                     { name: 'home', icon: '', title: '首页', url:'' }
            //                 ]
            //     },
            //     {
            //         name: 'system', icon: '', title: '权限管理', children:
            //                 [
            //                     {name: 'User', icon: '', title: '用户管理', url:''},
            //                     {name: 'Role', icon: '', title: '角色管理', url:''},
            //                     {name: 'Power', icon: '', title: '权限管理', url:''}
            //                 ]
            //     }
            // ],
            fullScreen: false,
            homeIndex: 'home',
            menus: {
                home: {
                    title: '首页', icon: '', children: {
                        home: {title: '首页', icon: '', url: '${request.contextPath}/test-02.html'}
                    }
                },
                system: {
                    title: '系统管理', icon: '', children: {
                        user: {title: '用户管理', icon: '', url: '${request.contextPath}/user/list'},
                        role: {title: '角色管理', icon: '', url: '${request.contextPath}/role/list'},
                        permission: {title: '权限管理', icon: '', url: '${request.contextPath}/permission/list'}
                    }
                },
                spider: {
                    title: '爬虫', icon: '', children: {
                        list: {title: '页面列表', icon: '', url: '${request.contextPath}/spider/list'},
                        rule: {title: '规则管理', icon: '', url: '${request.contextPath}/spiderRule/list'},
                        zootopia: {title: 'zootopia', icon: '', url: '${request.contextPath}/spider/zootopia'}
                    }
                },
                websocket: {
                    title: 'WebSocket', icon: '', children: {
                        hello: {title: 'Hello', icon: '', url: '${request.contextPath}/websocket/hello'}
                    }
                }
            },
            asideMenu: {},
            headerTabs: {},
            ifrHeight: 600
        },
        methods: {
            init: function(){
                this.initAsideMenu('home');
                this.initIfrHeight();
            },
            handleFullScreen: function(){
                this.fullScreen = !this.fullScreen;
                this.initIfrHeight();
            },
            initIfrHeight: function(){
                //this.ifrHeight = this.fullScreen ? document.documentElement.clientHeight - 56 : document.documentElement.clientHeight - 116;
                this.ifrHeight = document.documentElement.clientHeight - (this.fullScreen ? 48 : 103);
            },
            initAsideMenu: function(index){
                var $this = this, asideIndex;
                this.$set(this, 'asideMenu', this.menus[index].children);
                asideIndex = Object.keys(this.asideMenu)[0];
                this.asideSelect(asideIndex);
                this.$nextTick(function(){
                    $this.$refs.asideMenu.activeIndex = asideIndex;
                });
            },
            asideSelect: function(index){
                var $this = this;
                this.$set(this.headerTabs, index, this.asideMenu[index]);
                this.$nextTick(function(){
                    $this.$refs.headerTabs.currentName = index;
                });
            },
            removeHeaderTab: function(targetName){
                var $this = this;
                if(targetName != this.homeIndex){
                    this.$delete(this.headerTabs, targetName);
                    this.$nextTick(function(){
                        $this.$refs.headerTabs.currentName = $this.homeIndex;
                    });
                }
            }
        },
        mounted: function(){
            this.init();
        }
    });
</script>
</body>
</html>