<!doctype html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>科室管理</title>
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
                            <div class="sub-title-header">费用管理</div>
                            <div class="sub-title-right">
                            </div>
                        </div>
                        <hr>
                        <div>
                            <table class="layui-hide" id="chargesInfo" lay-filter="chargesInfo"></table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${mainDomain}/cloud/static/layui/layui.all.js"></script>
<script src="${mainDomain}/cloud/static/layui/index.js"></script>
<script type="text/html" id="chargesToolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="addCharges">新增</button>
        <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="modifyCharges">修改</button>
        <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="delCharges">删除</button>
    </div>
</script>

<script>
    var laytable;
    var $ = layui.jquery;

    layui.use(['table', 'form'], function () {
        laytable = layui.table;

        var form = layui.form;

        var checkStatus={};
        checkStatus.data=[];
        //方法级渲染
        var chargesTab = laytable.render({
            elem: '#chargesInfo',
            url: '${mainDomain}/cloud/hrs/register/charges/find',
            contentType:"application/json;charset=UTF-8",
            toolbar: '#chargesToolbar',
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
                {type:'radio'}
                , {field: 'doctorLevel', title: '级别',templet:function (d) {
                        if(d.doctorLevel == 1) {
                            return "普通";
                        } else if(d.doctorLevel == 2) {
                            return "专家"
                        }
                    }}
                , {field: 'registerCharges', title: '挂号费',templet:function (d) {
                        return "￥" + d.registerCharges;
                    }}
                , {field: 'updateBy', title: '更新者'}
                , {field: 'updateTime', title: '更新时间',templet:function (d) {
                        return timeStamp2YYYYMMdd(d.updateTime);
                    }}
            ]]
        });

        laytable.on('row(chargesInfo)', function(obj){
            console.log(obj.tr) //得到当前行元素对象
            $(obj.tr).find('input[type=radio]').prop("checked", true);
            form.render('radio');
            checkStatus.data=[];
            checkStatus.data.push(obj.data);

        });

        laytable.on('toolbar(chargesInfo)', function(obj){
            switch(obj.event){
                case 'addCharges':

                    var index = layer.open({
                        type: 2,
                        title: "新增挂号费信息",
                        content: '${mainDomain}/cloud/hrs/register/charges/save',
                        area: ['800px', '500px'],
                        maxmin: true
                    });
                    break;
                case 'modifyCharges':
                    var data = checkStatus.data;  //获取选中行数据
                    if(data.length == 0) {
                        layer.alert("请选择一条挂号费信息");
                        break;
                    }
                    var index = layer.open({
                        type: 2,
                        title: "修改挂号费信息",
                        content: '${mainDomain}/cloud/hrs/register/charges/modify/'+data[0].id,
                        area: ['800px', '500px'],
                        maxmin: true
                    });
                    break;
                case 'delCharges':
                    var data = checkStatus.data;  //获取选中行数据
                    if(data.length == 0) {
                        layer.alert("请选择一条挂号费信息");
                        break;
                    }
                    layer.confirm('是否确定删除?', function(index){
                        $.post("${mainDomain}/cloud/hrs/register/charges/del/"+data[0].id,{},function(e){
                            layer.close(index);
                            if(e.code==0){
                                chargesTab.reload();
                            }else{
                                layer.alert(e.resultMsg);
                            }
                        });
                    });
                    break;
            };
        });
    });

    function timeStamp2YYYYMMdd (time){
        var year = time.year;
        var month = time.monthValue;
        var date = time.dayOfMonth;
        if(month < 10) {
            month = "0" + month;
        }
        if(date < 10) {
            date = "0" + date;
        }
        var hour = time.hour;
        if(hour<10){
            hour='0'+hour;
        }
        var minute = time.minute;
        if(minute<10){
            minute='0'+minute;
        }
        var second = time.second;
        if(second<10){
            second='0'+second;
        }
        return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
    }

    function date2YYYYMMdd (time){
        var year = time.year;
        var month = time.monthValue;
        var date = time.dayOfMonth;
        if(month < 10) {
            month = "0" + month;
        }
        if(date < 10) {
            date = "0" + date;
        }
        return year + "-" + month + "-" + date;
    }


</script>

</body>

</html>