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

        .layout-header-bar {
            background: #fff;
            box-shadow: 0 2px 1px 1px rgba(100, 100, 100, 0.1);
        }

        .layout-logo-left {
            width: 90%;
            border-radius: 3px;
            margin: 15px 0 15px 15px;
        }

        .collspseIcon {
            cursor: pointer;
            margin: 20px 20px 0;
        }

        .collspseIcon:hover {
            color: #18b566;
        }

        .menu-icon {
            transition: all .3s;
        }

        .rotate-icon {
            transform: rotate(-90deg);
        }

        .menu-item span {
            display: inline-block;
            overflow: hidden;
            width: 69px;
            text-overflow: ellipsis;
            white-space: nowrap;
            vertical-align: bottom;
            transition: width .2s ease .2s;
        }
        .menu-item i {
            transform: translateX(0px);
            transition: font-size .2s ease, transform .2s ease;
            vertical-align: middle;
            font-size: 16px;
        }

        .poptipMenu .ivu-poptip-popper {
            margin-top:10px;
        }
        .poptipMenu .poptipMenuItem {
            color: rgba(255,255,255,.7);
        }
        .poptipMenu .selectedMenu {
            color: #2d8cf0;
            background: #363e4f;
            border-right: 2px solid #2d8cf0;
        }
        .poptipMenu .ivu-icon{
            font-size: 24px;
            cursor: pointer;
            padding: 0 30px;
            margin: 10px 0;
            margin-right: -2px;
        }

        .my-content {
            margin: 0px;
            min-height: 800px;
            background-color: #EEEEEE;
        }
    </style>
</head>
<body>
<div id="app" v-cloak>
    <div class="layout">
        <layout>
            <sider ref="sideMenu" hide-trigger collapsible :collapsed-width="78" v-model="isCollapsed"
                   :style="{width: isCollapsed?'60px':'200px', overflow: isCollapsed ? 'visible' : 'auto'}">
                <div class="logo-con">
                    <img v-show="!isCollapsed" src="/image/logo.jpg" key="max-logo" class="layout-logo-left" style="width:170px;"/>
                    <img v-show="isCollapsed" src="/image/logo-min.jpg" key="min-logo" class="layout-logo-left" style="width:50px;"/>
                </div>
                <i-menu v-show="!isCollapsed" active-name="siderMenu" theme="dark" width="auto" :class="menuitemClasses">
                    <template v-for="item in menuList">
                        <menu-item v-if="item.children.length<1" :name="item.name" :key="'menuitem'+item.name">
                            <icon type="search"></icon>
                            <span>{{item.title}}</span>
                        </menu-item>
                        <submenu v-if="item.children.length>=1" :name="item.name" :key="'menuitem'+item.name">
                            <template slot="title">
                                <icon type="ios-paper"></icon>
                                <span>{{item.title}}</span>
                            </template>
                            <menu-item v-for="child in item.children" :name="child.name" :key="'menuitem'+child.name">{{child.title}}</menu-item>
                        </submenu>
                    </template>
                </i-menu>
                <div ref="poptipMenu" v-show="isCollapsed" class="poptipMenu"  v-for="(item,index) in menuList">
                    <poptip placement="right-start" :name="'menuitem1'+item.name" :key="'menuitem1'+item.name"
                            :title="item.children.length>=1 ? item.title : ''">
                        <div class="poptipMenuItem" :class="{'selectedMenu': currentActiveName==item.name}" @click="currentActiveName=item.name">
                            <icon type="search"></icon>
                        </div>
                        <div slot="content">
                            <span v-if="item.children.length<1">{{item.title}}</span>
                            <span v-else-if="item.children.length>=1" v-for="child in item.children">{{child.title}}</span>
                        </div>
                    </poptip>
                </div>
            </sider>
            <layout>
                <i-header :style="{padding: 0}" class="layout-header-bar">
                    <icon @click.native="collapsedSider" :class="rotateIcon" class="collspseIcon" type="navicon-round"
                          size="24"></icon>
                    <div style="float:right; padding:2px 10px;">
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
                </i-header>
                <i-content class="my-content">
                    Content
                </i-content>
            </layout>
        </layout>
    </div>
</div>
<script type="text/javascript" src="/js/vue.js"></script>
<script type="text/javascript" src="/iview/iview.js"></script>
<script type="text/javascript">
    var app = new Vue({
        el: '#app',
        data: {
            isCollapsed: false,
            currentActiveName: '',
            menuList: [
                {
                    name: 'home', icon: '', title: '首页', children: []
                },
                {
                    name: 'rightManage', icon: '', title: '权限管理', children: [
                        {name: 'rightManage-User', icon: '', title: '用户管理'},
                        {name: 'rightManage-Role', icon: '', title: '角色管理'},
                        {name: 'rightManage-Power', icon: '', title: '权限管理'}
                    ]
                }
            ]
        },
        computed: {
            rotateIcon: function () {
                return [
                    'menu-icon',
                    this.isCollapsed ? 'rotate-icon' : ''
                ];
            },
            menuitemClasses: function () {
                return [
                    'menu-item',
                    this.isCollapsed ? 'collapsed-menu' : ''
                ]
            }
        },
        methods: {
            collapsedSider: function () {
                if(this.isCollapsed)
                this.$refs.sideMenu.toggleCollapse();
            }
        }
    });
</script>
</body>
</html>