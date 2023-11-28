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
        .layui-table-cell {
            padding:0 5px;
            height:auto;
            overflow:visible;
            text-overflow:inherit;
            white-space:normal;
            word-break: break-all;
            text-align: center;
            font-size: small;
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
                            <div class="sub-title-header">企业列表</div>
                        </div>
                        <hr>
                        <div class="js-search" style="text-align: left">
                            <form class="layui-form" style="padding-top: 1rem;margin-left:0px">
                                <div class="layui-form-item">
                                    <div class="layui-inline">
                                        <label class="layui-form-label" style="padding: 8px">企业名称：</label>
                                        <div class="layui-input-inline">
                                            <input class="layui-input" placeholder="企业名称" id="s_name"
                                               name="s_name"
                                               value="">
                                        </div>
                                    </div>
                                    <div class="layui-inline">
                                        <a class="layui-btn layui-btn-normal js-method" data-method="query"><i
                                                    class="layui-icon">&#xe615;</i>查询</a>
                                        <a class="layui-btn layui-btn-normal js-method" data-method="export"><i
                                                    class="layui-icon">&#xe601;</i>导出</a>
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
                        name: $('#s_name').val()

                    }
                    , loading: true
                });
            },
            // 创建
            create: function (othis) {
                var id = othis.data('id');
                var index =layer.open({
                    type: 2,
                    title:'企业与员工信息',
                    maxmin: true,
                    content: '${mainDomain}/cloud/enterStaff?eid='+id //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
                });
                layer.full(index);
            },
            openImg:function(othis){
                var src = othis.data('ig');
                //配置一个透明的询问框
                layer.msg('<img src="'+src+'" />', {
                    time: 20000, //20s后自动关闭
                    btn: ['关闭']
                });
            },
            export:function () {
                window.open('${mainDomain}/cloud/enter/excelExport');
            }
        };

        //方法级渲染
        laytable.render({
            elem: '#userinfo',
            url: '${mainDomain}/cloud/enter/selectAllPg',
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
                {field: 'name', title: '企业名称',width:120}
                , {field: 'customno', title: '企业海关编码'}
                , {field: 'uniformcreditno', title: '企业信用代码',width:150}
                , {field: 'billingtype', title: '结算方式',
                    templet:function (res) {
                        if (res.billingtype == 1){
                            return '月结';
                        } else if(res.billingtype == 2){
                            return '散结';
                        }else{
                            return '-';
                        }
                    }
                }
                , {field: 'isagree', title: '是否协议',
                    templet:function (res) {
                        if (res.isagree == 1){
                            return '是';
                        } else if(res.isagree == 0){
                            return '否';
                        }else{
                            return '-';
                        }
                    }
                }
                , {field: 'entertype', title: '企业性质',
                    templet:function (res) {
                        if (res.entertype == 1){
                            return '国营企业';
                        } else if(res.entertype == 2){
                            return '私营企业';
                        }else{
                            return '-';
                        }
                    }
                }
                , {field: 'address', title: '注册地址'}
                , {field: 'contacttel', title: '联系电话'}
                , {field: 'uniformcreditimage', title: '营业执照照片',
                    templet:function (res) {
                        return '<a align="center" class="js-method" data-method="openImg" data-ig="'+res.uniformcreditimage+'" style="color: #01AAED;cursor:pointer;">查看</a>';
                    }
                }
                , {field: 'createtime', title: '创建时间'}
                , {field: 'updatetime', title: '修改时间'}
                , {
                    field: 'classify', title: '操作',width:130,
                    templet: function (res) {
                            return '<a data-id="' + res.id + '" data-method="create" class="layui-btn layui-btn-xs js-method" ><i class="layui-icon">&#xe615;</i>查看员工</a>\n';

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