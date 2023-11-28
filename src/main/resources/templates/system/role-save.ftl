<link rel="stylesheet" href="${mainDomain}/cloud/static/layui/rc/css/layui.css" media="all">
<link id="layuicss-layer" rel="stylesheet"
      href="${mainDomain}/cloud/static/layui/rc/css/modules/layer/default/layer.css?v=3.1.1" media="all">
<link id="layuicss-layuiAdmin" rel="stylesheet" href="${mainDomain}/cloud/static/layui/admin.css" media="all">
<script>
    var mainDomain='${mainDomain}';
</script>
<div style="padding:10px;background-color: #FFFFFF;">
    <div class="layui-form">
        <input id="i_id" type="hidden" value="${id!''}">
        <div class="layui-form-item">
            <label for="i_role_name" class="layui-form-label" style="width: 100px">
                <span style="color: #cc0000">*</span>角色CODE：
            </label>
            <div class="layui-input-inline" style="width: 300px;">
                <input type="text" class="layui-input" id="i_role_code" name="i_role_code" value="${role.role!''}">
            </div>
        </div>
        <div class="layui-form-item">
            <label for="i_role_name" class="layui-form-label" style="width: 100px">
                <span style="color: #cc0000">*</span>角色名称：
            </label>
            <div class="layui-input-inline" style="width: 300px;">
                <input type="text" class="layui-input" id="i_role_name" name="i_role_name" value="${role.name!''}">
            </div>
        </div>

        <div class="layui-form-item">
            <label for="remark" class="layui-form-label" style="width: 100px">
                职能描述：
            </label>
            <div class="layui-input-inline" style="width: 300px;">
                <textarea type="text" class="layui-input" id="remark" name="remark" maxlength="100" style="height: 80px;width: 100%">${role.description!''}</textarea>
            </div>
        </div>
    </div>
</div>
<hr>


