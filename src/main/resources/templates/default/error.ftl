<#import "lib/layout.ftl" as layout>
<#assign style>
    <style>

    </style>
</#assign>
<@layout.default title="异常页面" style=style>
path:${result.items.path!""}<br>
error:${result.items.error!""}<br>
exception:${result.items.exception!""}<br>
message:${result.items.message!""}<br>
trace:${result.items.trace!""}<br>
    <@layout.defaultjs />
<script type="text/javascript">
    var main = new Vue({
        el: '#main',
        data: {

        },
        mounted: function () {
            var $this = this;
            this.$nextTick(function () {
            });
        },
        methods: {}
    });
</script>
</@layout.default>