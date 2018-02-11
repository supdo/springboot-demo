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
            box-shadow: 0 1px 1px rgba(0,0,0,.1);
        }
        .layout-logo-left{
            width: 90%;
            height: 30px;
            background: #5b6270;
            border-radius: 3px;
            margin: 15px auto;
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
    </style>
</head>
<body>
<div id="app" v-cloak>
    <div class="layout">
        <layout>
            <i-header :style="{padding: 0}" class="layout-header-bar">
                <icon @click.native="collapsedSider" :class="rotateIcon" :style="{margin: '20px 20px 0',pointer:'cursor'}" type="navicon-round" size="24"></icon>
            </i-header>
            <layout>
                <sider ref="side1" hide-trigger collapsible :collapsed-width="78" v-model="isCollapsed" :style="{width: isCollapsed?'60px':'200px', overflow: isCollapsed ? 'visible' : 'auto'}">
                    <i-menu active-name="1-2" theme="dark" width="auto" :class="menuitemClasses">
                        <div class="logo-con">
                            <img v-show="!isCollapsed" src="/image/logo.jpg" key="max-logo" style="width:190px;" />
                            <img v-show="isCollapsed" src="/image/logo-min.jpg" key="min-logo" style="width:50px;" />
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
                <i-content :style="{margin: '0px', background: '#fff', minHeight: '860px'}">
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