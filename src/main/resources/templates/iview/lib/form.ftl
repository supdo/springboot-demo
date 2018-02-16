<#macro Hform items=[]>
    <#list items?values as item>
        <#rt><form-item prop="${item.name}" label="${item.label}"
                        <#t> <#if item.required>:rules="{required: true, message: '${item.label}不能为空', trigger: 'blur'}"</#if>
                        <#lt> <#if item_index == 0>autofocus</#if> <#list item.attrs?keys as key> ${key}="${item.attrs[key]}"</#list>>
        <i-input type="${item.type}" v-model="formData.${item.name}" placeholder="${item.label}"></i-input>
    </form-item>
    </#list>
</#macro>