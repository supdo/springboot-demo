<#macro default title="default layout" css="" style="">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>${title}</title>
    <link rel="stylesheet" charset="utf-8" href="${request.contextPath}/element/style/style.css"/>
    <link rel="stylesheet" charset="utf-8" href="${request.contextPath}/css/style.css"/>
    ${css}
    ${style}
</head>
<body>
    <#nested>
</body>
</html>
</#macro>

<#macro defaultjs>
<script type="text/javascript" src="${request.contextPath}/js/jquery.ajax.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/vue.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/element/element-ui.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/common.js"></script>
</#macro>