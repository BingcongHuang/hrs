<!doctype html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>医生列表</title>
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
    <link rel="stylesheet" href="${mainDomain}/cloud/static/css/custom.css" media="all">
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
        .layui-form-label{
            padding: 0px 0px 0px 0px !important;
            text-align: right;
            width: 100px;
            line-height: 32px;
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
                            <div class="sub-title-header">医生列表</div>
                            <div class="sub-title-right">
                            </div>
                        </div>
                        <hr>
                        <form id="doctorForm" name="doctorForm" class="layui-form" action="" lay-filter="doctorForm">
                            <div class="layui-form-item">
                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">姓名</label>
                                    <div class="layui-input-block">
                                        <input name="dcname" id="dcname" type="text" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">联系方式</label>
                                    <div class="layui-input-block">
                                        <input name="userTelphone" type="text" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">级别</label>
                                    <div class="layui-input-block">
                                        <select name="status">
                                            <option value="" selected>请选择</option>
                                            <option value="1">普通</option>
                                            <option value="2">专家</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">科室</label>
                                    <div class="layui-input-block">
                                        <select id="fdptSlt" name="firstDptId" lay-filter="fdptSlt">
                                            <option value="-1">请选择</option>
                                            <#list dptInfos as dp>
                                                <option value="${dp.id}">${dp.dptName}</option>
                                            </#list>
                                        </select>
                                    </div>
                                </div>
                                <div class="layui-inline  layui-col-md3">
                                    <div class="layui-input-block">
                                        <select id="sdptSlt" name="secondDptId" lay-filter="sdptSlt">
                                            <option value="">请选择</option>
                                        </select>
                                    </div>

                                </div>
                                <div class="layui-inline layui-col-md2">
                                    <button class="layui-btn" lay-submit lay-filter="doctorForm" id="queryBtn" type="button"><i class="layui-icon">&#xe615;</i>查询</button>
                                    <button class="layui-btn layui-btn-primary" type="reset"><i class="layui-icon">&#xe666;</i>重置</button>
                                </div>
                            </div>
                        </form>
                        <div>
                            <table class="layui-hide" id="doctorInfo" lay-filter="doctorInfo"></table>
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
    var $ = layui.jquery;
    layui.use(['table', 'form'], function () {
        laytable = layui.table;
        var form = layui.form;

        var checkStatus={};
        checkStatus.data=[];
        //方法级渲染
        var dptTab = laytable.render({
            elem: '#doctorInfo',
            url: '${mainDomain}/cloud/user/doctor/list/find',
            contentType:"application/json;charset=UTF-8",
            toolbar: '#dptToolbar',
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
                {field: 'userRealName', title: '姓名'}
                , {field: 'userSex', title: '性别',templet:function (d){
                        if (d.userSex == 0) {
                            return "男"
                        } else {
                            return "女"
                        }
                }}
                , {field: 'deptName', title: '科室',templet:function (d){
                        return d.firstDptStr + "_" + d.secondDptStr
                }}
                , {field: 'userPhone', title: '联系方式'}
                , {field: 'doctorLevel', title: '级别',templet:function (d){
                        if (d.doctorLevel == 1) {
                            return "普通"
                        } else if(d.doctorLevel == 2) {
                            return "专家"
                        } else {
                            return "未知"
                        }
                    }}
            ]]
        });

        form.on('submit(doctorForm)',function (data) {
            console.log(JSON.stringify(data.field))
            dptTab.reload({
                contentType:"application/json;charset=UTF-8",
                where:{"userTelphone":data.field.userTelphone,"userRealname":data.field.userRealname,"firstDptId":data.field.firstDptId,"secondDptId":data.field.secondDptId,"doctorLevel":data.field.doctorLevel}
            });
            return false;
        });

        form.on('select(fdptSlt)',function(data){
            var fdptId = data.value;
            $("#sdptSlt").empty();
            $("#sdptSlt").append(new Option("请选择",""));
            $.post(mainDomain+'/cloud/hrs/department/slist/'+ fdptId +'/findAll',{},function(res){
                if(res.code == "0"){
                    $.each(res.data,function (index,item){
                        $("#sdptSlt").append(new Option(item.dptName,item.id));
                    });
                    form.render("select");
                }
            });
            form.render("select");
        });
    });
</script>

</body>

</html>