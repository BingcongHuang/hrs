<link rel="stylesheet" href="${mainDomain}/cloud/static/layui/rc/css/layui.css" media="all">
<link id="layuicss-layer" rel="stylesheet"
      href="${mainDomain}/cloud/static/layui/rc/css/modules/layer/default/layer.css?v=3.1.1" media="all">
<link id="layuicss-layuiAdmin" rel="stylesheet" href="${mainDomain}/cloud/static/layui/admin.css" media="all">
<style>
    html {
        background-color: #FFFFFF;
    }

    .title {
        color: #FF8402;
    }
</style>
<script>
    var mainDomain = '${mainDomain}';
</script>
<body>
<div>
<#--    <div class="layui-card-header title">-->
<#--        菜单配置-->
<#--    </div>-->
    <div class="layui-form" style="padding-top: 20px">
        <input id="i_id" type="hidden" value="${id!''}">
        <input id="i_parentId" type="hidden" value="${parentId!''}">
        <input id="i_levels" type="hidden" value="${levels!''}">
        <div class="layui-form-item">
            <label for="i_menu_name" class="layui-form-label" style="width: 100px">
                <span class="x-red">*</span>菜单名称：
            </label>
            <div class="layui-input-inline" style="width: 300px;">
                <input type="text" class="layui-input" id="i_menu_name" name="i_menu_name" value="${menu.description!''}">
            </div>
        </div>

        <div class="layui-form-item">
            <label for="i_menu_icon" class="layui-form-label" style="width: 100px">
                <span class="x-red">*</span>菜单图标：
            </label>
            <div class="layui-input-inline" style="width: 300px;">
                <input type="text" class="layui-input" id="i_menu_icon" name="i_menu_icon" value="${menu.menuIcon!''}">
            </div>
        </div>
        <div class="layui-form-item">
            <label for="i_menu_url" class="layui-form-label" style="width: 100px">
                <span class="x-red">*</span>菜单地址：
            </label>
            <div class="layui-input-inline" style="width: 300px;">
                <input type="text" class="layui-input" id="i_menu_url" name="i_menu_url" value="${menu.permission!''}">
            </div>
        </div>
<#--        <div class="layui-form-item">-->
<#--            <label for="i_menu_num" class="layui-form-label" style="width: 100px">-->
<#--                <span class="x-red">*</span>权限标识：-->
<#--            </label>-->
<#--            <div class="layui-input-inline" style="width: 300px;">-->
<#--                <input type="text" class="layui-input" id="i_menu_perms" name="i_menu_perms" value="${menu.perms!''}">-->
<#--            </div>-->
<#--        </div>-->
        <div class="layui-form-item">
            <label for="i_menu_num" class="layui-form-label" style="width: 100px">
                <span class="x-red">*</span>菜单排序：
            </label>
            <div class="layui-input-inline" style="width: 50px;">
                <input type="text" class="layui-input" id="i_menu_num" name="i_menu_num" value="${menu.menuOrder!''}">
            </div>
        </div>

<#--        <div class="layui-form-item">-->
<#--            <label for="i_menu_isopen" class="layui-form-label" style="width: 100px">-->
<#--                <span class="x-red">*</span>默认展开：-->
<#--            </label>-->
<#--            <div class="layui-input-inline" style="width: 300px;">-->
<#--                <input type="checkbox" name="switch" lay-text="展开|折叠" lay-filter="switchChange"-->
<#--                       <#if (menu.isopen?exists &&  menu.isopen== 1)>checked</#if> lay-skin="switch" id="i_menu_isopen"-->
<#--                       name="i_menu_isopen">-->
<#--            </div>-->
<#--        </div>-->
    </div>
    <hr>
    <div class="layui-form-item" style="text-align: right;margin-right: 15px">
        <label class="layui-form-label">
        </label>
        <button class="layui-btn layui-btn-normal js-method" data-method="save">
            保存
        </button>
    </div>
</div>
<script src="${mainDomain}/cloud/static/layui/layui.all.js"></script>
<script src="${mainDomain}/cloud/static/layui/index.js"></script>
<script>
    <#--    checkDataRight(${menuId});-->
</script>
<script>

    layui.use('form', function () {
        var form = layui.form;
        var $ = layui.jquery;
        form.on('switch(switchChange)',function (data) {

        });
        var active = {
            save: function () {
                var menuId = $('#i_id').val();
                var parentId = $('#i_parentId').val();
                var name = $('#i_menu_name').val();
                var icon = $('#i_menu_icon').val();
                var url = $('#i_menu_url').val();
                var num = $('#i_menu_num').val();
                // var perms=$('#i_menu_perms').val();
                if (name == '' || name == null) {
                    layer.alert("请输入菜单名称! ");
                    return false;
                }
                if (num == '' || num == null) {
                    layer.alert("请填写菜单排序! ");
                    return false;
                }
                if (parentId != 0 && (url == '' || url == null)) {
                    layer.alert("子菜单必须填写跳转url! ");
                    return false;
                }
                var idx = layer.load(0);
                $.post("${mainDomain}/cloud/menu/saveMenu", {
                    "id": menuId,
                    "parentId": parentId,
                    "description": name,
                    "menuIcon": icon,
                    "permission": url,
                    "menuOrder": num,
                    "resType":3
                    // "perms":perms
                }, function (res) {
                    layer.close(idx);
                    if (res.code == 0) {
                        layer.alert("保存成功", {icon: 6}, function () {
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                            window.parent.location.reload();
                        });
                        return false;
                    } else {
                        layer.alert(res.msg);
                        return false;
                    }
                });
                return false;
            }
        };

        $('.js-method').on('click', function () {
            var othis = $(this), method = othis.data('method');
            active[method] ? active[method].call(this, othis) : '';
        });

    });


</script>
</body>
