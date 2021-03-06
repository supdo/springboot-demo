<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>首页</title>
    <link rel="stylesheet" charset="utf-8" href="/iview/styles/iview.css"/>
    <link rel="stylesheet" charset="utf-8" href="/css/style.css"/>
    <style scoped>
        #app {
            padding: 0px;
            heigth: 1000px;
        }
        .layout {
            border: 0px solid #d7dde4;
            background: #f5f7f9;
            position: relative;
            border-radius: 0px;
            overflow: hidden;
        }
        .layout-sider-bar {
            width: 200px;
            overflow: auto;
        }
        .layout-header-bar {
            height: 60px;
            padding: 0px;
            background: #fff;
            box-shadow: 0 2px 1px 1px rgba(100, 100, 100, 0.1);
        }
        .layout-logo-left {
            width:170px;
            border-radius: 3px;
            margin: 15px 0 15px 15px;
        }
        .my-content {
            margin: 8px 6px;
            min-height: 800px;
        }
    </style>
</head>
<body>
<div id="app" v-cloak>
    <div class="layout">
        <layout>
            <sider ref="sideMenu" class="layout-sider-bar">
                <div class="logo-con">
                    <img src="/image/logo.jpg" key="max-logo" class="layout-logo-left"/>
                </div>
                <i-menu ref="siderMenu" theme="dark" width="auto" :active-name="0">
                    <menu-item v-for="(item,index) in siderMenu" :name="index" :key="'siderMenu-'+item.name" style="padding: 12px 24px;">
                        <Icon type="document-text"></Icon>
                        <span>{{item.title}}</span>
                    </menu-item>
                </i-menu>
            </sider>
            <layout>
                <i-header class="layout-header-bar">
                    <i-menu ref="headerMenu" mode="horizontal" theme="light" :active-name="0" @on-select="initSiderMenu">
                        <menu-item v-for="(item,index) in menuList" :name="index" :key="'headerMenu-'+item.name" style="font-size: 16px;">
                            <icon type="ios-paper"></icon>
                            {{item.title}}
                        </menu-item>
                        <div style="float:right; padding:0px 10px;">
                            <dropdown transfer style="margin-right:10px;">
                                <a href="javascript:void(0)">kukei
                                    <icon type="arrow-down-b"></icon>
                                </a>
                                <dropdown-menu slot="list">
                                    <dropdown-item>个人中心</dropdown-item>
                                    <dropdown-item><a href="/logout">注销</a></dropdown-item>
                                </dropdown-menu>
                            </dropdown>
                            <Avatar style="background-color: #87d068" icon="person"/>
                        </div>
                    </i-menu>
                </i-header>
                <i-content class="my-content">
                    <template>
                    <tabs ref="headerTabs">
                        <#--<tab-pane v-for="(tab, index) in contentTabs" :key="index" :name="'headerTab-'+index" :label="tab.title" closable>-->
                            <#--<iframe id="tab-ifr-" :src="tab.url" frameborder="0" scrolling="no"></iframe>-->
                        <#--</tab-pane>-->
                        <tab-pane>
                            test
                        </tab-pane>
                    </tabs>
                    </template>
                </i-content>
            </layout>
        </layout>
    </div>
</div>
<script type="text/javascript" src="/js/vue.js"></script>
<script type="text/javascript" src="/iview/iview.min.js"></script>
<script type="text/javascript">
    var app = new Vue({
        el: '#app',
        data: {
            menuList: [
                {name:'home', icon:'', title:'首页', children: [
                        {name: 'home', icon: '', title: '首页'}
                    ]
                },
                {name: 'rightManage', icon: '', title: '权限管理', children: [
                        {name: 'User', icon: '', title: '用户管理'},
                        {name: 'Role', icon: '', title: '角色管理'},
                        {name: 'Power', icon: '', title: '权限管理'}
                    ]
                }
            ],
            siderMenu: [
            ],
            contentTabs: [
                {name: 'User', icon: '', title: '用户管理1', url: '/iview'},
                {name: 'User', icon: '', title: '用户管理2', url: '/login'},
                {name: 'User', icon: '', title: '用户管理3', url: '/logon'}
            ]
        },
        computed: {

        },
        methods: {
            init: function(){
                this.initSiderMenu(0);
            },
            initSiderMenu: function(index){
                var self = this;
                this.siderMenu = this.menuList[index].children;
                this.$refs.siderMenu.currentActiveName = 0;
                this.$nextTick(function(){
                    self.$refs.siderMenu.updateActiveName();
                });
            }
        },
        mounted: function(){
            this.init();
        }
    });
</script>
</body>
</html>