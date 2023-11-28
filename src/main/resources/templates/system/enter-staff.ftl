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
                        <input type="hidden" id="e_id" value="${enter.id}">
                        <table lay-filter="demo">
                            <thead>
                            <tr>
                                <th lay-data="{field:'name', width:100}">企业名称</th>
                                <th lay-data="{field:'customno'}">企业海关编码</th>
                                <th lay-data="{field:'uniformcreditno'}">统一信用代码</th>
                                <th lay-data="{field:'entertype', width:120}">企业性质</th>
                                <th lay-data="{field:'address'}">企业注册地址</th>
                                <th lay-data="{field:'contact', width:80}">联系人</th>
                                <th lay-data="{field:'contacttel'}">联系电话</th>
                                <th lay-data="{field:'uniformcreditimage'}">营业执照图片</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>${enter.name!''}</td>
                                <td>${enter.customno!''}</td>
                                <td>${enter.uniformcreditno!''}</td>
                                <td><#if enter.entertype == 1>国营企业<#elseif enter.entertype == 2 >私营企业<#else>-</#if></td>
                                <td>${enter.address!''}</td>
                                <td>${enter.contact!''}</td>
                                <td>${enter.contacttel!''}</td>
                                <td><a align="center" class="js-method" data-method="openImg" data-ig="${enter.uniformcreditimage!''}" style="color: #01AAED;cursor:pointer;">查看</a></td>
                            </tr>
                            </tbody>
                        </table>
                        <div class="js-search" style="text-align: left">
                            <form class="layui-form" style="padding-top: 1rem;margin-left:0px">
                                <div class="layui-form-item">
                                    <div class="layui-inline">
                                        <label class="layui-form-label" style="padding: 8px">姓名：</label>
                                        <div class="layui-input-inline">
                                            <input class="layui-input" placeholder="姓名" id="s_name"
                                               name="s_name"
                                               value="">
                                        </div>
                                    </div>
                                    <div class="layui-inline">
                                        <a class="layui-btn layui-btn-normal js-method" data-method="query"><i
                                                    class="layui-icon">&#xe615;</i>查询</a>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div>
                            <table class="layui-hide" id="userinfo"></table>
                        </div>
                        <div align="center" style="margin-top: 20px;margin-bottom: 20px">
                            <button type="button" class="layui-btn js-method" data-method="close"><i class="layui-icon">&#xe65c;</i>返回</button>
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


        laytable.init('demo', {
          limit: 1 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
        });
        var eid = '${enter.id}';
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
                        name: $('#s_name').val(),
                        enterpriseid:eid
                    }
                    , loading: true
                });
            },
            openImg:function(othis){
                var src = othis.data('ig');
                //配置一个透明的询问框
                layer.msg('<img src="'+src+'" />', {
                    time: 20000, //20s后自动关闭
                    btn: ['关闭']
                });
            },
            close:function () {
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                parent.layer.close(index);
            }
        };

        //方法级渲染
        laytable.render({
            elem: '#userinfo',
            url: '${mainDomain}/cloud/enterStaff/selectAllPg',
            method: 'post',
            where: {
                name: $('#s_name').val(),
                enterpriseid:eid
            },
            loading: true,
            cellMinWidth: 80,
            page: {
                layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'] //自定义分页布局
                , groups: 5 //只显示 1 个连续页码
                , first: false //不显示首页
                , last: false //不显示尾页
            },
            cols: [[
                {field: 'name', title: '姓名',width:120}
                , {field: 'sex', title: '性别'}
                , {field: 'personneltype', title: '类型',
                    templet:function (res) {
                        if (res.personneltype == 1){
                            return "身份证";
                        }else if (res.personneltype == 0) {
                            return "驾驶证";
                        }else{
                            return "-";
                        }
                    }
                }
                , {field: 'pid', title: '证件号',
                    templet:function (res) {
                        if (res.personneltype == 1){
                            return res.idnumber;
                        }else if (res.personneltype == 0) {
                            return res.drivinglicenceno;
                        }else{
                            return "-";
                        }
                    }
                }
                , {field: 'drivinglicenceimage', title: '证件正面照片',
                    templet:function (res) {
                        return '<a align="center" class="js-method" data-method="openImg" data-ig="'+res.drivinglicenceimage+'" style="color: #01AAED;cursor:pointer;">查看</a>';
                    }
                }
                , {field: 'drivinglicenceimagevice', title: '证件反面照片',
                    templet:function (res) {
                        return '<a align="center" class="js-method" data-method="openImg" data-ig="'+res.drivinglicenceimagevice+'" style="color: #01AAED;cursor:pointer;">查看</a>';
                    }
                }
                , {field: 'createtime', title: '创建时间'}

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