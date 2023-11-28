<!doctype html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title></title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="shortcut icon" href="${mainDomain}/cloud/static/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="${mainDomain}/cloud/static/layui/rc/css/layui.css" media="all">
    <link id="layuicss-layer" rel="stylesheet"
          href="${mainDomain}/cloud/static/layui/rc/css/modules/layer/default/layer.css?v=3.1.1" media="all">
    <link id="layuicss-layuiAdmin" rel="stylesheet" href="${mainDomain}/cloud/static/layui/admin.css" media="all">
    <style>
        .js-search {
            margin-bottom: 10px;
        }

        .sub-menu {
            display: none;
        }

        .sub-title {
            font-size: 15px;
            font-weight: 700;
        }

        .sub-title-header {
            float: left;
        }

        .sub-title-right {
            float: right;
            margin-right: 25px;
            margin-bottom: 5px;
        }
    </style>
    <script>
        var mainDomain = '${mainDomain}';
    </script>
</head>

<body>

<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body">
<#--                    <div class="layui-col-md12 x-so js-search">-->
<#--                        <button data-method="create" data-type="add" data-id="0"-->
<#--                                class="layui-btn layui-btn-normal layui-btn-sm js-method"><i class="layui-icon"></i>添加一级菜单-->
<#--                        </button>-->
<#--                    </div>-->
                    <div class="sub-title">
                        <div class="sub-title-header">PC菜单管理</div>
                        <div class="sub-title-right">
                            <button data-method="create" data-id="0" data-type="add" data-id="" data-category="0"
                                    class="layui-btn layui-btn-normal layui-btn-sm  js-method"><i
                                        class="layui-icon"></i>添加一级菜单
                            </button>
                        </div>
                    </div>
                    <hr>

                    <div style="margin-top: 20px">
                        <table class="layui-table layui-form" style="height:auto">
                            <thead>
                            <tr>
                                <th>菜单名称</th>
                                <th>菜单路径</th>
                                <th width="50">排序</th>
                                <th width="320">操作</th>
                            </thead>
                            <tbody class="x-cate">
                            <#if pcmenus??>
                                <#list pcmenus as menu>
                                    <tr cate-id='${menu.id}' fid='0'>
                                        <td>
                                            <i class="layui-icon x-show js-method" data-method="doopen"
                                               data-pid="${menu.id}" status='true'>&#xe623;</i>
                                            ${menu.description!''}
                                        </td>
                                        <td>${menu.permission}</td>
                                        <td>${menu.menuOrder!'0'}</td>
                                        <td class="td-manage">
                                            <button class="layui-btn layui-btn layui-btn-xs js-method"
                                                    data-method="create" data-levels="1" data-type="edit" data-id="${menu.id!''}" data-category="0"><i
                                                        class="layui-icon">&#xe642;</i>编辑
                                            </button>
                                            <button class="layui-btn layui-btn-warm layui-btn-xs js-method"
                                                    data-levels="2"
                                                    data-method="create" data-type="add" data-id="${menu.id!''}" data-category="0"><i
                                                        class="layui-icon">&#xe642;</i>添加子栏目
                                            </button>
                                            <button class="layui-btn-danger layui-btn layui-btn-xs  js-method"
                                                    data-method="del" data-id="${menu.id!''}"><i class="layui-icon">&#xe640;</i>删除
                                            </button>
                                        </td>
                                    </tr>
                                    <#if menu.subMenu??>
                                        <#list menu.subMenu as sub>
                                            <tr cate-id='${sub.id}' fid='${menu.id}' class="sub-menu">
                                                <td>
                                                    &nbsp;&nbsp;├<i class="layui-icon x-show js-method"
                                                                    data-method="doopen"
                                                                    data-pid="${sub.id}" status='true'>&#xe623;</i>
                                                    ${sub.description!''}
                                                </td>
                                                <td>${sub.permission}</td>
                                                <td>${sub.menuOrder!'1'}</td>
                                                <td class="td-manage">
                                                    <button class="layui-btn layui-btn layui-btn-xs js-method"
                                                            data-method="create" data-type="edit" data-levels="2"
                                                            data-category="0"
                                                            data-id="${sub.id!''}"><i class="layui-icon">&#xe642;</i>编辑
                                                    </button>
<#--                                                    <button class="layui-btn layui-btn-warm layui-btn-xs js-method"-->
<#--                                                            data-levels="3"-->
<#--                                                            data-method="create" data-type="add" data-id="${sub.menuId!''}">-->
<#--                                                        <i class="layui-icon">&#xe642;</i>添加页面受控组件-->
<#--                                                    </button>-->
                                                    <button class="layui-btn-danger layui-btn layui-btn-xs js-method"
                                                            data-method="del" data-id="${sub.id!''}"><i
                                                                class="layui-icon">&#xe640;</i>删除
                                                    </button>
                                                </td>
                                            </tr>
                                            <#if sub.subMenu??>
                                                <#list sub.subMenu as ssub>
                                                    <tr cate-id='${ssub.id}' fid='${sub.id}' class="sub-menu">
                                                        <td>${ssub.id}</td>
                                                        <td>
                                                            &nbsp;&nbsp;&nbsp;&nbsp;
                                                            ├${ssub.description!''}
                                                        </td>
                                                        <td>${ssub.permission}</td>
                                                        <td>${ssub.menuOrder!'1'}</td>
                                                        <td class="td-manage">
                                                            <button class="layui-btn layui-btn layui-btn-xs js-method"
                                                                    data-method="create" data-type="edit"
                                                                    data-levels="3" data-category="0"
                                                                    data-id="${ssub.id!''}"><i class="layui-icon">&#xe642;</i>编辑
                                                            </button>
                                                            <button class="layui-btn-danger layui-btn layui-btn-xs js-method"
                                                                    data-method="del" data-id="${ssub.id!''}"><i
                                                                        class="layui-icon">&#xe640;</i>删除
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </#list>
                                            </#if>
                                        </#list>
                                    </#if>
                                </#list>
                            </#if>
                            </tbody>
                        </table>

                    </div>
                </div>
            </div>
        </div>

<#--        <div class="layui-col-md12">-->
<#--            <div class="layui-card">-->
<#--                <div class="layui-card-body">-->
<#--                &lt;#&ndash;                    <div class="layui-col-md12 x-so js-search">&ndash;&gt;-->
<#--                &lt;#&ndash;                        <button data-method="create" data-type="add" data-id="0"&ndash;&gt;-->
<#--                &lt;#&ndash;                                class="layui-btn layui-btn-normal layui-btn-sm js-method"><i class="layui-icon"></i>添加一级菜单&ndash;&gt;-->
<#--                &lt;#&ndash;                        </button>&ndash;&gt;-->
<#--                &lt;#&ndash;                    </div>&ndash;&gt;-->
<#--                    <div class="sub-title">-->
<#--                        <div class="sub-title-header">客户端菜单管理</div>-->
<#--                        <div class="sub-title-right">-->
<#--                            <button data-method="create" data-id="0" data-type="add" data-id="" data-category="1"-->
<#--                                    class="layui-btn layui-btn-normal layui-btn-sm  js-method"><i-->
<#--                                    class="layui-icon"></i>添加一级菜单-->
<#--                            </button>-->
<#--                        </div>-->
<#--                    </div>-->
<#--                    <hr>-->

<#--                    <div style="margin-top: 20px">-->
<#--                        <table class="layui-table layui-form" style="height:auto">-->
<#--                            <thead>-->
<#--                            <tr>-->
<#--                                <th>菜单名称</th>-->
<#--                                <th>菜单路径</th>-->
<#--                                <th width="50">排序</th>-->
<#--                                <th width="320">操作</th>-->
<#--                            </thead>-->
<#--                            <tbody class="x-cate">-->
<#--                            <#if clientmenus??>-->
<#--                                <#list clientmenus as menu>-->
<#--                                <tr cate-id='${menu.menuId}' fid='0'>-->
<#--                                    <td>-->
<#--                                        <i class="layui-icon x-show js-method" data-method="doopen"-->
<#--                                           data-pid="${menu.menuId}" status='true'>&#xe623;</i>-->
<#--                                    ${menu.name!''}-->
<#--                                    </td>-->
<#--                                    <td>${menu.url}</td>-->
<#--                                    <td>${menu.orderNum!'0'}</td>-->
<#--                                    <td class="td-manage">-->
<#--                                        <button class="layui-btn layui-btn layui-btn-xs js-method"-->
<#--                                                data-method="create" data-levels="1" data-type="edit" data-id="${menu.menuId!''}" data-category="1"><i-->
<#--                                                class="layui-icon">&#xe642;</i>编辑-->
<#--                                        </button>-->
<#--                                        <button class="layui-btn layui-btn-warm layui-btn-xs js-method"-->
<#--                                                data-levels="2"-->
<#--                                                data-method="create" data-type="add" data-id="${menu.menuId!''}" data-category="1"><i-->
<#--                                                class="layui-icon">&#xe642;</i>添加子栏目-->
<#--                                        </button>-->
<#--                                        <button class="layui-btn-danger layui-btn layui-btn-xs  js-method"-->
<#--                                                data-method="del" data-id="${menu.menuId!''}"><i class="layui-icon">&#xe640;</i>删除-->
<#--                                        </button>-->
<#--                                    </td>-->
<#--                                </tr>-->
<#--                                    <#if menu.subMenus??>-->
<#--                                        <#list menu.subMenus as sub>-->
<#--                                        <tr cate-id='${sub.menuId}' fid='${menu.menuId}' class="sub-menu">-->
<#--                                            <td>-->
<#--                                                &nbsp;&nbsp;├<i class="layui-icon x-show js-method"-->
<#--                                                                data-method="doopen"-->
<#--                                                                data-pid="${sub.menuId}" status='true'>&#xe623;</i>-->
<#--                                            ${sub.name!''}-->
<#--                                            </td>-->
<#--                                            <td>${sub.url}</td>-->
<#--                                            <td>${sub.orderNum!'1'}</td>-->
<#--                                            <td class="td-manage">-->
<#--                                                <button class="layui-btn layui-btn layui-btn-xs js-method"-->
<#--                                                        data-method="create" data-type="edit" data-levels="2"-->
<#--                                                        data-category="1"-->
<#--                                                        data-id="${sub.menuId!''}"><i class="layui-icon">&#xe642;</i>编辑-->
<#--                                                </button>-->
<#--                                            &lt;#&ndash;                                                    <button class="layui-btn layui-btn-warm layui-btn-xs js-method"&ndash;&gt;-->
<#--                                            &lt;#&ndash;                                                            data-levels="3"&ndash;&gt;-->
<#--                                            &lt;#&ndash;                                                            data-method="create" data-type="add" data-id="${sub.menuId!''}">&ndash;&gt;-->
<#--                                            &lt;#&ndash;                                                        <i class="layui-icon">&#xe642;</i>添加页面受控组件&ndash;&gt;-->
<#--                                            &lt;#&ndash;                                                    </button>&ndash;&gt;-->
<#--                                                <button class="layui-btn-danger layui-btn layui-btn-xs js-method"-->
<#--                                                        data-method="del" data-id="${sub.menuId!''}"><i-->
<#--                                                        class="layui-icon">&#xe640;</i>删除-->
<#--                                                </button>-->
<#--                                            </td>-->
<#--                                        </tr>-->
<#--                                            <#if sub.subMenus??>-->
<#--                                                <#list sub.subMenus as ssub>-->
<#--                                                <tr cate-id='${ssub.menuId}' fid='${sub.menuId}' class="sub-menu">-->
<#--                                                    <td>${ssub.menuId}</td>-->
<#--                                                    <td>-->
<#--                                                        &nbsp;&nbsp;&nbsp;&nbsp;-->
<#--                                                        ├${ssub.name!''}-->
<#--                                                    </td>-->
<#--                                                    <td>${ssub.url}</td>-->
<#--                                                    <td>${ssub.orderNum!'1'}</td>-->
<#--                                                    <td class="td-manage">-->
<#--                                                        <button class="layui-btn layui-btn layui-btn-xs js-method"-->
<#--                                                                data-method="create" data-type="edit"-->
<#--                                                                data-levels="3" data-category="1"-->
<#--                                                                data-id="${ssub.menuId!''}"><i class="layui-icon">&#xe642;</i>编辑-->
<#--                                                        </button>-->
<#--                                                        <button class="layui-btn-danger layui-btn layui-btn-xs js-method"-->
<#--                                                                data-method="del" data-id="${ssub.menuId!''}"><i-->
<#--                                                                class="layui-icon">&#xe640;</i>删除-->
<#--                                                        </button>-->
<#--                                                    </td>-->
<#--                                                </tr>-->
<#--                                                </#list>-->
<#--                                            </#if>-->
<#--                                        </#list>-->
<#--                                    </#if>-->
<#--                                </#list>-->
<#--                            </#if>-->
<#--                            </tbody>-->
<#--                        </table>-->

<#--                    </div>-->
<#--                </div>-->
<#--            </div>-->
<#--        </div>-->
    </div>
</div>
<script src="${mainDomain}/cloud/static/layui/layui.all.js"></script>
<script src="${mainDomain}/cloud/static/layui/index.js"></script>
<script>

    layui.use(['table', 'admin'], function () {
        var $ = layui.jquery;
        var admin = layui.admin;

        var active = {
            doopen: function (othis) {
                if (othis.attr('status') == 'true') {
                    othis.attr('status', 'false');
                    othis.html('&#xe625;')
                } else {
                    othis.attr('status', 'true');
                    othis.html('&#xe623;')
                }
                var fid = othis.data('pid');
                $('tr[fid="' + fid + '"]').each(function () {
                    $(this).toggleClass("sub-menu");
                    if (othis.attr('status') == 'true') {
                        var pid = $(this).attr('cate-id');
                        $('tr[fid="' + pid + '"]').each(function () {
                            $('i[data-pid="' + pid + '"]').attr('status', 'true');
                            $('i[data-pid="' + pid + '"]').html('&#xe623;');
                            $(this).addClass("sub-menu");
                        });
                    }
                })
            },
            create: function (othis) {
                var id = othis.data('id');
                var type = othis.data('type');
                var levels = othis.data('levels');
                // var category = othis.data('category');

                var index = layer.open({
                    type: 2,
                    title: "菜单配置",
                    content: '${mainDomain}/cloud/menu/toSave?levels=' + levels + '&type=' + type + '&id=' + id,
                    area: ['500px', '500px'],

                });
                //admin.openRight(500, '${mainDomain}/cloud/menu/toSave?levels=' + levels + '&type=' + type + '&id=' + id+'&category='+category);
            },
            del: function (othis) {
                var id = othis.data('id');
                var idlist = new Array();
                idlist.push(id);
                layer.confirm('确认要删除吗？', function (index) {
                    //捉到所有被选中的，发异步进行删除
                    $.post("${mainDomain}/cloud/menu/delete", "idList="+idlist, function (res) {
                        if (res.code == 0) {
                            layer.msg('删除成功', {icon: 1});
                            $(".layui-form-checked").not('.header').parents('tr').remove();
                            location.reload();
                            return false;
                        } else {
                            layer.alert(res.msg);
                            return false;
                        }
                    });
                    return false;

                });
            }
        };

        //事件绑定
        $('.js-method').on('click', function () {
            var othis = $(this), method = othis.data('method');
            active[method] ? active[method].call(this, othis) : '';
        });
    });

</script>
</body>

</html>