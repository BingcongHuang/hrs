<!doctype html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>医院挂号云平台</title>
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

        .layui-form-onswitch {
            border-color: #5FB878 !important;
            background-color: #5FB878 !important;
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
                    <div style="margin-top: 10px">
                        <div class="sub-title">
                            <div class="sub-title-header">角色列表</div>
                            <div class="sub-title-right">
                                <button data-method="create" data-type="add" data-id=""
                                        class="layui-btn layui-btn-normal layui-btn-sm  js-method"><i
                                            class="layui-icon"></i>添加角色
                                </button>
                            </div>
                        </div>
                        <hr>
                        <div>
                            <table class="layui-hide" id="roleinfo"></table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${mainDomain}/cloud/static/layui/layui.all.js"></script>
<script src="${mainDomain}/cloud/static/layui/index.js"></script>

<script>
    var laytable;
    var active = {};

    layui.use(['table', 'form'], function () {
        laytable = layui.table;
        var $ = layui.jquery;
        var form = layui.form;
        // 事件
        active = {
            // 查询
            query: function (othis) {
                //执行重载
                laytable.reload('testReload', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    , where: {
                        // name: $('#s_role_name').val()
                    }
                    , loading: true
                });
            },
            // 设置
            create: function (othis) {
                var id = othis.data('id');
                var type = othis.data('type');
                layer.open({
                    type: 2
                    , title: '角色维护'
                    , id: 'layerDemo'
                    , content: '${mainDomain}/cloud/role/toSave?type=' + type + '&id=' + id
                    , area: ['600px','300px']
                    , btn: ['保存', '取消']
                    , closeBtn: false
                    , shade: 0.2 //不显示遮罩
                    , yes: function (index, layero) {
                        var body = layer.getChildFrame('body', index);
                        var id = body.find('#i_id').val();
                        var code = body.find('#i_role_code').val();
                        var name = body.find('#i_role_name').val();
                        var remark = body.find('#remark').val();
                        if (name == '' || name == null) {
                            layer.alert("请输入角色名称! ");
                            return false;
                        }
                        var idx = layer.load(0);
                        $.post("${mainDomain}/cloud/role/saveRole", {
                            "id": id,
                            "role": code,
                            "name": name,
                            "description": remark
                        }, function (res) {
                            layer.close(idx);
                            if (res.code == 0 ) {
                                layer.alert("保存成功", {icon: 6}, function (i) {
                                    layer.close(index);
                                    laytable.reload('testReload');
                                    layer.closeAll();
                                });
                            } else {
                                layer.alert(res.alertMsg);
                                return false;
                            }
                        });
                        return false;

                    }
                    , btn2: function (index, layero) {
                        layer.closeAll();
                    }
                });
            },
            // 删除
            delById: function (othis) {
                var id = othis.data('id');
                layer.confirm('确认要删除吗？', function (index) {
                    var idList = new Array();
                    idList.push(id);
                    //发异步删除数据
                    layui.jquery.post("${mainDomain}/cloud/role/removeRole", "id="+id, function (res) {
                        if (res.code == 0 ) {
                            laytable.reload('testReload');
                            layer.msg('已删除!', {icon: 1, time: 1000});
                        } else {
                            layer.alert(res.alertMsg);
                        }
                    });
                    return false;
                });
            },
            //权限
            auth: function (othis) {
                var id = othis.data('id');
                var idx;
                layer.open({
                    type: 2
                    , title: '权限设置'
                    , id: 'auth_' + id
                    , content: '${mainDomain}/cloud/role/toRoleMenus?id=' + id
                    , area: ['850px', '600px']
                    , btn: ['保存', '取消']
                    , closeBtn: false
                    , shade: 0.2 //不显示遮罩
                    , success: function (layero, index) {

                    }
                    , yes: function (index, layero) {
                        var w = $(layero).find("iframe")[0].contentWindow;
                        var falg = w.save();
                    }
                    , btn2: function (index, layero) {
                        layer.closeAll();
                    }
                });
            },
            refashTable: function () {
                laytable.reload('testReload');
            }
        };

        //方法级渲染
        laytable.render({
            elem: '#roleinfo',
            url: '${mainDomain}/cloud/role/findRolePg',
            method: 'post',
            where: {},
            loading: true,
            cellMinWidth: 80,
            page: {
                layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'] //自定义分页布局
                , groups: 5 //只显示 1 个连续页码
                , first: false //不显示首页
                , last: false //不显示尾页
            },
            cols: [[
                {field: 'id', title: 'ID', width: 80, sort: true, fixed: true}
                , {field: 'role', title: '角色CODE', width: 160}
                , {field: 'name', title: '角色名称', width: 160}
                , {field: 'description', title: '职能描述'}
                , {field: 'opt', title: '操作',
                    templet: function (res) {
                        return '<button data-id="' + res.id + '" data-method="auth" class="layui-btn-normal layui-btn layui-btn-xs js-method"><i class="layui-icon">&#xe6fc;</i>权限设置</button>\n' +
                            '<button data-id="' + res.id + '" data-type="edit" data-method="create" class="layui-btn layui-btn layui-btn-xs js-method" ><i class="layui-icon">&#xe642;</i>编辑</button>\n' +
                            ' <button data-id="' + res.id + '" data-method="delById" class="layui-btn-danger layui-btn layui-btn-xs js-method"><i class="layui-icon">&#xe640;</i>删除</button>'
                    }
                }
            ]]
            , id: 'testReload'
            , done: function (res, curr, count) {
                $('#roleinfo').next().find('.layui-table-body').find('.js-method').on('click', function () {
                    var othis = $(this), method = othis.data('method');
                    active[method] ? active[method].call(this, othis) : '';
                });
            }
        });



        //事件绑定
        $('.js-method').on('click', function () {
            var othis = $(this), method = othis.data('method');
            active[method] ? active[method].call(this, othis) : '';
        });

    });


</script>

</body>

</html>