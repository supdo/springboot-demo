/*
author: kukei
date: 2018-02-09
desc: 共用js库
 */
Vue.component('my-forms', {
    template: '<div>' +
    '   <form-item v-for="(item, index) in items" :key="index" :prop="\'items.\' + index + \'.value\'" :label="item.label" ' +
    '       :rules="{required: item.required, message: item.label + \'不能为空\', trigger: \'blur\'}">' +
    '       <i-select v-if="item.type == \'select\'" v-model="item.value">' +
    '           <i-option v-for="opt in item.options" :value="opt.key" :key="opt.key">{{ opt.name }}</i-option>' +
    '       </i-select>' +
    '       <i-input v-else :type="item.type" v-model="item.value" :placeholder="item.label" clearable>' +
    '       </i-input>' +
    '    </form-item>' +
    '</div>',
    props: ['items']
});

function myGet(url, params, success, error, complete) {
    $.ajax({
        type: "GET",
        url: url,
        data: params,
        dataType: 'json',
        success: success,
        error: error,
        complete: complete
    });
};

function myPost(url, params, success, error, complete) {
    $.ajax({
        type: "POST",
        url: url,
        data: params,
        dataType: 'json',
        success: success,
        error: error,
        complete: complete
    });
};

//删除table的一行数据。
function delRow($this, index, url, title) {
    $this.deleteRow = {index: index, className: 'table-deleting-row'};
    $this.$confirm(title, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        callback: function (action, instance) {
            if (action == 'confirm') {
                myPost(url, {},
                    function (data) {
                        if (data.flag) {
                            $this.deleteRow.className = 'table-deleted-row';
                            delayFun(function () {
                                $this.deleteRow.index = -1;
                            }, 400);
                            $this.listData.splice(index, 1);
                            $this.$message.success(data.msg);
                        } else {
                            $this.$message.error(data.msg);
                            delayFun(function () {
                                $this.deleteRow.index = -1;
                            }, 200);
                        }
                    }
                );
            } else if (action == 'cancel') {
                delayFun(function () {
                    $this.deleteRow.index = -1;
                }, 200);
            }
        }
    });
};

function delayFun(fun, duration) {
    if (duration == undefined) {
        duration = 1000;
    }
    var t;
    clearTimeout(t);
    t = setTimeout(fun, duration);
}