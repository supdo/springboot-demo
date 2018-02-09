<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>test-01 Title</title>
    <link rel="stylesheet" charset="utf-8" href="/iview/styles/iview.css" />
    <link rel="stylesheet" href="/css/style.css">
    <style scoped>
        body {
            background-color: #DDDDDD;
        }
        #my-comp {
            width: 400px;
            margin: 150px auto 0 auto;
        }
    </style>
</head>
<body>
<div id="my-comp" v-cloak>
    <card>
        <div slot="title" ><span class="card-title-text">测试</span> <span style="float:right;"></div>
        <i-form ref="testForm" :model="forms" :label-width="80">
            <my-forms :items="forms.items"></my-forms>
        </i-form>
    </card>
</div>
<script type="text/javascript" src="/js/vue.min.js"></script>
<script type="text/javascript" src="/iview/iview.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript">
    var myComp = new Vue({
        el: '#my-comp',
        data: {
            forms: {
                items: [
                    {
                        name: 'name',
                        value: 'test',
                        label: '姓名',
                        type: 'text',
                        required: true
                    },
                    {
                        name: 'password',
                        value: '',
                        label: '密码',
                        type: 'password',
                        required: true
                    },
                    {
                        name: 'sex',
                        value: '1',
                        label: '性别',
                        type: 'select',
                        options: [
                            {key: '1', name: '男'},
                            {key: '0', name: '女'}
                        ],
                        required: true
                    },
                    {
                        key: 'remark',
                        value: '',
                        label: '说明',
                        type: 'textarea'
                    }
                ]
            }
        }
    });

    $.get("/logon_json", { name: "John", time: "2pm" },
        function(data){
            var users = data.items.user.fields;
            for(var key in users){
                myComp.forms.items.push(users[key]);
            }
        }
    );
</script>
</body>
</html>