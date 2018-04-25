<#macro Hform items=[], formData='formData' refName="myForm", itemOptions='itemOptions'>
    <#list items?values as item>
        <#rt><el-form-item ref="${refName}_${item.name}" prop="${item.name}" label="${item.label}"
                        <#t> <#if item.required>:rules="{required: true, message: '${item.label}不能为空', trigger: 'blur'}"</#if>
                        <#lt> <#if item_index == 0>autofocus</#if> <#list item.attrs?keys as key> ${key}="${item.attrs[key]}"</#list>>
        <#if item.type == 'select'>
            <el-select v-model="${formData}.${item.name}" filterable placeholder="${item.label}">
                <el-option  v-for="(label, val) in ${itemOptions}.${item.name}" :key="val" :label="label" :value="val"></el-option>
            </el-select>
        <#elseif item.type == 'redio'>
            <el-switch v-model="${formData}.${item.name}" active-color="#13ce66"></el-switch>
        <#else>
            <el-input type="${item.type}" v-model="${formData}.${item.name}" placeholder="${item.label}"></el-input>
        </#if>
        </el-form-item>
    </#list>
</#macro>