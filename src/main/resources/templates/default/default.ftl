<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>首页</title>
    <link rel="stylesheet" charset="utf-8" href="/iview/styles/iview.css" />
    <link rel="stylesheet" charset="utf-8" href="/css/style.css" />
    <style scoped>
        #app{padding: 0px; heigth:1000px;}
        .layout{
            border: 0px solid #d7dde4;
            background: #f5f7f9;
            position: relative;
            border-radius: 0px;
            overflow: hidden;
        }
        .layout-header-bar{
            background: #fff;
            box-shadow: 0 2px 1px 1px rgba(100, 100, 100, 0.1);
        }
        .layout-logo-left{
            width: 90%;
            border-radius: 3px;
            margin: 15px 0 15px 15px;
        }
        .collspseIcon{
            cursor: pointer;
            margin: 20px 20px 0;
        }
        .collspseIcon:hover{
            color:#18b566;
        }
        .menu-icon{
            transition: all .3s;
        }
        .rotate-icon{
            transform: rotate(-90deg);
        }
        .menu-item span{
            display: inline-block;
            overflow: hidden;
            width: 69px;
            text-overflow: ellipsis;
            white-space: nowrap;
            vertical-align: bottom;
            transition: width .2s ease .2s;
        }
        .menu-item i{
            transform: translateX(0px);
            transition: font-size .2s ease, transform .2s ease;
            vertical-align: middle;
            font-size: 16px;
        }
        .collapsed-menu span{
            width: 0px;
            transition: width .2s ease;
        }
        .collapsed-menu i{
            transform: translateX(5px);
            transition: font-size .2s ease .2s, transform .2s ease .2s;
            vertical-align: middle;
            font-size: 22px;
        }
        .my-content{
            margin:0px;
            min-height: 800px;
            background-color: #EEEEEE;
        }
    </style>
</head>
<body>
<div id="app" v-cloak>
    <div class="layout">
        <layout>
            <sider ref="side1" hide-trigger collapsible :collapsed-width="78" v-model="isCollapsed" :style="{width: isCollapsed?'60px':'200px', overflow: isCollapsed ? 'visible' : 'auto'}">
                <i-menu active-name="1-2" theme="dark" width="auto" :class="menuitemClasses">
                    <div class="logo-con">
                        <img v-show="!isCollapsed" src="/image/logo.jpg" key="max-logo" class="layout-logo-left" style="width:170px;" />
                        <img v-show="isCollapsed" src="/image/logo-min.jpg" key="min-logo" class="layout-logo-left" style="width:50px;" />
                    </div>
                    <menu-item name="1-1">
                        <icon type="ios-navigate"></icon>
                        <span>Option 1</span>
                    </menu-item>
                    <menu-item name="1-2">
                        <icon type="search"></icon>
                        <span>Option 2</span>
                    </menu-item>
                    <menu-item name="1-3">
                        <icon type="settings"></icon>
                        <span>Option 3</span>
                    </menu-item>
                </i-menu>
            </sider>
            <layout>
                <i-header :style="{padding: 0}" class="layout-header-bar">
                    <icon @click.native="collapsedSider" :class="rotateIcon" class="collspseIcon" type="navicon-round" size="24"></icon>
                    <div style="float:right; padding:2px 10px;">
                        <dropdown style="margin-right:10px;">
                            <a href="javascript:void(0)">kukei<icon type="arrow-down-b"></icon></a>
                            <dropdown-menu slot="list">
                                <dropdown-item>个人中心</dropdown-item>
                                <dropdown-item><a href="/logout">注销</a></dropdown-item>
                            </dropdown-menu>
                        </dropdown>
                        <Avatar style="background-color: #87d068" icon="person" />
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
            isCollapsed: false
        },
        computed: {
            rotateIcon: function() {
                return [
                    'menu-icon',
                    this.isCollapsed ? 'rotate-icon' : ''
                ];
            },
            menuitemClasses: function() {
                return [
                    'menu-item',
                    this.isCollapsed ? 'collapsed-menu' : ''
                ]
            }
        },
        methods: {
            collapsedSider: function() {
                this.$refs.side1.toggleCollapse();
            }
        }
    });
</script>
</body>
</html>