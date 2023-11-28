<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>医院挂号云平台</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
    <link rel="stylesheet" href="${mainDomain}/cloud/static/layui/rc/css/layui.css" media="all">
    <link id="layuicss-layer" rel="stylesheet"
          href="${mainDomain}/cloud/static/layui/rc/css/modules/layer/default/layer.css?v=3.1.1" media="all">
    <link id="layuicss-layuiAdmin" rel="stylesheet" href="${mainDomain}/cloud/static/layui/admin.css" media="all">
</head>

<body>
<div class="x-body">
    <form class="layui-form">
        <input type="hidden" id="i_role_id" value="${roleId!''}">
        <div style="margin-left: 20px;">
        <div class="layui-unselect layui-form-checkbox" lay-skin="primary" fid="-1" cate-id="0"id="parent_menu_0">
            <i class="layui-icon">&#xe605;</i>
        </div>全选
        </div>
        <#if pcmenus??>
            <#list pcmenus as menu>
            <table class="layui-table layui-form" style="margin-bottom: 15px;">
                <thead>
                    <tr cate-id='${menu.id}' fid='0' >
                        <th colspan="3">
                            <div class="layui-unselect layui-form-checkbox <#if ids?seq_contains(menu.id)>layui-form-checked</#if>" id="parent_menu_${menu.id}" lay-skin="primary" data-id='${menu.id}' cate-id='${menu.id}' fid='0' >
                                <i class="layui-icon">&#xe605;</i>
                            </div>
                            ${menu.description!''}
                        </th>
                    </tr>
                </thead>
                <tbody class="x-cate">
                        <#if menu.subMenu??>
                        <tr fid='${menu.id}' >
                           <#list menu.subMenu as sub>
                                    <#if sub_index % 3 != 0 || sub_index == 0>
                                        <td cate-id='${sub.id}' style="width: 33%">
                                            <div class="layui-unselect layui-form-checkbox <#if ids?seq_contains(sub.id)>layui-form-checked</#if>" id="sub_menu_${menu.id}"  lay-skin="primary" data-id='${sub.id}' cate-id='${menu.id}' fid='${menu.id}' >
                                                <i class="layui-icon">&#xe605;</i>
                                            </div>
                                            ${sub.name!''}
                                        </td>
                                        <#else>
                                        </tr><tr>
                                            <td cate-id='${sub.menuId}' style="width: 33%">
                                                <div class="layui-unselect layui-form-checkbox <#if ids?seq_contains(sub.id)>layui-form-checked</#if>" id="sub_menu_${menu.id}"  lay-skin="primary" data-id='${sub.id}' cate-id='${menu.id}' fid='${menu.id}' >
                                                    <i class="layui-icon">&#xe605;</i>
                                                </div>
                                                 ${sub.description!''}
                                            </td>
                                    </#if>
                                </#list>
                            <#if menu.subMenu?size % 3 ==1>
                                <td style="width: 33%"></td> <td style="width: 33%"></td>
                            </#if>
                            <#if menu.subMenu?size % 3 ==2>
                                <td style="width: 33%"></td>
                            </#if>
                            </tr>
                        </#if>
                </tbody>
            </table>
            </#list>
        </#if>
        <#if clientmenus??>
            <#list clientmenus as menu>
                <table class="layui-table layui-form" style="margin-bottom: 15px;">
                    <thead>
                    <tr cate-id='${menu.id}' fid='0' >
                        <th colspan="3">
                            <div class="layui-unselect layui-form-checkbox <#if ids?seq_contains(menu.id)>layui-form-checked</#if>" id="parent_menu_${menu.id}" lay-skin="primary" data-id='${menu.id}' cate-id='${menu.id}' fid='0' >
                                <i class="layui-icon">&#xe605;</i>
                            </div>
                            ${menu.description!''}
                        </th>
                    </tr>
                    </thead>
                    <tbody class="x-cate">
                    <#if menu.subMenu??>
                    <tr fid='${menu.id}' >
                        <#list menu.subMenu as sub>
                            <#if sub_index % 3 != 0 || sub_index == 0>
                                <td cate-id='${sub.id}' style="width: 33%">
                                    <div class="layui-unselect layui-form-checkbox <#if ids?seq_contains(sub.id)>layui-form-checked</#if>" id="sub_menu_${menu.id}"  lay-skin="primary" data-id='${sub.id}' cate-id='${menu.id}' fid='${menu.id}' >
                                        <i class="layui-icon">&#xe605;</i>
                                    </div>
                                    ${sub.description!''}
                                </td>
                            <#else>
                                </tr><tr>
                                <td cate-id='${sub.id}' style="width: 33%">
                                    <div class="layui-unselect layui-form-checkbox <#if ids?seq_contains(sub.id)>layui-form-checked</#if>" id="sub_menu_${menu.id}"  lay-skin="primary" data-id='${sub.id}' cate-id='${menu.id}' fid='${menu.id}' >
                                        <i class="layui-icon">&#xe605;</i>
                                    </div>
                                    ${sub.description!''}
                                </td>
                            </#if>
                        </#list>
                        <#if menu.subMenu?size % 3 ==1>
                            <td style="width: 33%"></td> <td style="width: 33%"></td>
                        </#if>
                        <#if menu.subMenu?size % 3 ==2>
                            <td style="width: 33%"></td>
                        </#if>
                        </tr>
                    </#if>
                    </tbody>
                </table>
            </#list>
        </#if>
    </form>
</div>
<script src="${mainDomain}/cloud/static/layui/rc/layui.js"></script>
<#--<script src="${mainDomain}/cloud/static/layui/index.js"></script>-->
<script>
    var $;
    layui.use(['form','table'], function(){
        var form = layui.form;
        $ = layui.jquery;

        //选中事件 -----  勾选父级，子级自动勾选
        $(".layui-form-checkbox").click(function() {
            //判断是否是父级
            var pid = $(this).attr('fid') ;
            var cateid = $(this).attr('cate-id');
            var optype ;
            if ($(this).hasClass('layui-form-checked')) {
                optype = 'remove';
                $(this).removeClass('layui-form-checked');
                if ($(this).hasClass('header')) {
                    $(".layui-form-checkbox").removeClass('layui-form-checked');
                }
                //取消上级全选
                // $("#parent_menu_"+cateid).removeClass('layui-form-checked');
                $("#parent_menu_0").removeClass('layui-form-checked')
            } else {
                optype = 'add';
                $(this).addClass('layui-form-checked');
                if ($(this).hasClass('header')) {
                    $(".layui-form-checkbox").addClass('layui-form-checked');
                }
                //子级勾选，则父级必定勾选
                $("#parent_menu_"+cateid).removeClass('layui-form-checked').addClass('layui-form-checked');
            }
            if(pid == 0){
                // 子级跟随父级的状态变更
                $('.layui-table').find(".layui-form-checkbox").each(function(i , v){
                    var tmp = $(this).attr('cate-id');
                    if(tmp == cateid){
                        if(optype == 'add'){
                            $(this).removeClass('layui-form-checked').addClass('layui-form-checked');
                        }else{
                            $(this).removeClass('layui-form-checked');
                        }
                    }
                })
            }else if(pid == -1){
                //全选  或 全取消
                if(optype == 'add'){
                    $('.layui-form-checkbox').removeClass('layui-form-checked').addClass('layui-form-checked');
                }else{
                    $('.layui-form-checkbox').removeClass('layui-form-checked');
                }
            }
        });


    });



    function save() {
        var roleId = $('#i_role_id').val();
        var menuIds = [];
        $('table.layui-table .layui-form-checked').each(function () {
            var menuId = $(this).attr('data-id');
            menuIds.push(menuId);
        })
        if (menuIds.length == 0){
            layer.confirm('当前角色没有选择任何菜单权限，确认保存吗'  , function (index) {
                saveMenus(roleId , menuIds.join(','));
            })
        }else{
            saveMenus(roleId , menuIds.join(','));
        }
        return false;
    }

    function saveMenus(roleId , menuIds){
        var idx = layer.load(0);
        $.post("${mainDomain}/cloud/role/saveRoleMenus", {
            "roleId": roleId,
            "menuIds": menuIds
        }, function (res) {
            layer.closeAll();
            if (res.code == 0) {
                layer.alert("保存成功", {icon: 6}, function () {
                    //关闭当前frame
                    var index = parent.layer.getFrameIndex(window.name);
                    //关闭当前frame
                    parent.layer.close(index);
                    parent.active.refashTable();
                    return true;
                });
                return false;
            } else {
                layer.alert(res.msg);
                return false;
            }
        });
    }

</script>
</body>

</html>