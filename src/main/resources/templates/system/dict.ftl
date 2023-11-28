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
                            <div class="sub-title-header">字典列表</div>
                        </div>
                        <hr>
                        <div class="js-search" style="text-align: left">
                            <form class="layui-form" style="padding-top: 1rem;margin-left:0px">
                                <div class="layui-form-item">
                                    <div class="layui-inline">
                                        <label class="layui-form-label" style="padding: 8px">分组Key：</label>
                                        <div class="layui-input-inline">
                                            <input class="layui-input" placeholder="分组Key" id="s_commonkey"
                                               name="s_commonkey"
                                               value="">
                                        </div>
                                    </div>
                                    <div class="layui-inline">
                                        <a class="layui-btn layui-btn-normal js-method" data-method="query"><i
                                                    class="layui-icon">&#xe615;</i>查询</a>
                                        <a class="layui-btn layui-btn-normal js-method" data-method="create" data-type="add" data-id=""><i
                                                    class="layui-icon"></i>添加分组</a>
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div>
                            <table class="layui-hide" id="userinfo"></table>
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

    layui.use(['table', 'form', 'admin'], function () {
        laytable = layui.table;
        var $ = layui.jquery;
        var form = layui.form;
        var admin = layui.admin;
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
                        commonkey: $('#s_commonkey').val()
                    }
                    , loading: true
                });
            },
            // 创建
            create: function (othis) {
                var id = othis.data('id');
                //admin.openRight(500, '${mainDomain}/cloud/dict/toSave?id=' + id);
                var index = layer.open({
                    type: 2,
                    title: "",
                    content: '${mainDomain}/cloud/dict/toSave?id=' + id,
                    area: ['500px', '500px']
                });
            },
            toSub: function(othis){
                var id = othis.data('id');
                var index =layer.open({
                    type: 2,
                    title:'字典信息列表',
                    maxmin: true,
                    content: '${mainDomain}/cloud/sysDictExt/enterExt?eid='+id
                });
                layer.full(index);
            },
            // 删除
            delById: function (othis) {
                var id = othis.data('id');
                layer.confirm('确认要删除吗？', function (index) {
                    var idList = new Array();
                    idList.push(id);
                    //发异步删除数据
                    layui.jquery.post("${mainDomain}/cloud/dict/delete", "idList="+idList, function (res) {
                        if (res.code == 0 ) {
                            laytable.reload('testReload');
                            layer.msg('已删除!', {icon: 1, time: 1000});
                        } else {
                            layer.alert(res.msg);
                        }
                    });
                    return false;
                });
            }
        };

        //方法级渲染
        laytable.render({
            elem: '#userinfo',
            url: '${mainDomain}/cloud/dict/selectAllPg',
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
                {field: 'id', title: '编号', width: 80, sort: true, fixed: true}
                , {field: 'commonkey', title: '分组key',width:100}
                , {field: 'lvalue', title: '分组value'}
                , {field: 'label', title: '标签'}
                , {field: 'remark', title: '描述'}
                , {
                    field: 'classify', title: '操作',width:250,
                    templet: function (res) {
                            return '<button data-id="' + res.id + '" data-type="edit" data-method="create" class="layui-btn layui-btn layui-btn-xs js-method" ><i class="layui-icon">&#xe642;</i>编辑</button>\n' +
                                ' <button data-id="' + res.id + '" data-method="toSub" class="layui-btn-danger layui-btn layui-btn-xs js-method"><i class="layui-icon">&#xe615;</i>查看字典</button>'+
                                ' <button data-id="' + res.id + '" data-method="delById" class="layui-btn-danger layui-btn layui-btn-xs js-method"><i class="layui-icon">&#xe640;</i>删除</button>'

                    }
                }
            ]]
            , id: 'testReload'
            , done: function (res, curr, count) {
                $('#userinfo').next().find('.layui-table-body').find('.js-method').on('click', function () {
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