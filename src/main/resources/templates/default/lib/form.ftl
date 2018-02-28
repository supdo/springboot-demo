<#macro Hform items=[], formData='formData' formError="formError">
    <#list items?values as item>
        <#rt><el-form-item ref="${formError}_${item.name}" prop="${item.name}" label="${item.label}"
                        <#t> <#if item.required>:rules="{required: true, message: '${item.label}不能为空', trigger: 'blur'}"</#if>
                        <#lt> <#if item_index == 0>autofocus</#if> <#list item.attrs?keys as key> ${key}="${item.attrs[key]}"</#list>>
            <el-input type="${item.type}" v-model="${formData}.${item.name}" placeholder="${item.label}"></el-input>
        </el-form-item>
    </#list>
</#macro>