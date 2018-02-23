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
            border-right: solid 1px #e6e6e6;
        }
        .app-logo {
            border-right: solid 0px #e6e6e6;
        }
        .app-logo-img {
            width: 170px;
            margin: 5px 0px 0px 15px;
        }
        .app-aside-menu
        {
            border-width: 0px;
        }
        .app-header {
            margin: 0px;
            padding: 0px;
            -moz-box-shadow:0px 2px 5px #BBBBBB;
            -webkit-box-shadow:0px 2px 5px #BBBBBB;
            box-shadow:0px 2px 5px #BBBBBB;
        }
        .el-menu--horizontal{
            border-width: 0px;
        }
        .app-header-right {
            float: right;
            margin-top:20px;
            outline: none;
            padding: 0px 15px;
            cursor: pointer;
        }
        .app-main {
            padding: 10px 10px 0 10px;
        }
        .el-tabs__header {
            margin-bottom: 0px;
        }
        .el-tabs__content{
            border: 1px solid #e4e7ed;
            border-top: 0px;
        }

    </style>
</head>
<body>
<div id="app" v-cloak>
    <el-container>
        <el-aside class="app-aside" style="width:202px;">
            <div class="app-logo"><img src="/image/logo.jpg" class="app-logo-img" /></div>
            <el-menu ref="asideMenu" default-active="0" class="app-aside-menu" @select="asideSelect" background-color="#545c64" text-color="#fff" active-text-color="#ffd04b">
                <template v-for="(menu,key,index) in asideMenu">
                    <el-menu-item  :index="key" :key="'asideMenu-'+key">
                        <i class="el-icon-document"></i>
                        <span slot="title">{{menu.title}}</span>
                    </el-menu-item>
                </template>

            </el-menu>
        </el-aside>
        <el-container>
            <el-header class="app-header">
                <el-menu ref="headerMenu" default-active="0" class="" mode="horizontal" @select="initAsideMenu">
                    <el-menu-item v-for="(menu,key) in menus" :index="key" :key="'headerMenu-'+key">{{menu.title}}</el-menu-item>
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
                <el-tabs ref="headerTabs" type="card" value="1" closable  @tab-remove="removeHeaderTab">
                    <el-tab-pane v-for="(tab,key,index) in headerTabs" :key="key" :label="tab.title" :name="key">
                        <iframe :src="tab.url" width="100%" frameborder="0" scrolling="no"></iframe>
                    </el-tab-pane>
                </el-tabs>
            </el-main>
        </el-container>
    </el-container>
</div>
<script type="text/javascript" src="/js/vue.js"></script>
<script type="text/javascript" src="/element/element-ui.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
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
            menus: {
                home: {
                    title: '首页', icon: '', children: {
                        home: {title: '首页', icon: '', url: '//baidu.com'}
                    }
                },
                system: {
                    title: '系统管理', icon: '', children: {
                        user: {title: '用户管理', icon: '', url: '/default'},
                        role: {title: '角色管理', icon: '', url: '//qq.com'},
                        permission: {title: '权限管理', icon: '', url: ''}
                    }
                }
            },
            asideMenu: {},
            headerTabs: {}
        },
        methods: {
            init: function(){
                this.initAsideMenu('home');

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
                this.$delete(this.headerTabs, targetName);
            }
        },
        mounted: function(){
            this.init();
        }
    });
</script>
</body>
</html>