<!doctype html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>二级科室管理</title>
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
                            <div class="sub-title-header">一级科室：<b>${fName}</b>
                                <button id="fid" style="display: none" value="${fid}" />
                                <button id="fName" style="display: none" value="${fName}" />
                                <button id="returnBtn" class="layui-btn layui-btn-sm" value="返回">
                                    返回
                                </button>
                            </div>
                            <div class="sub-title-right">
                            </div>
                        </div>
                        <hr>
                        <form id="departmentForm" name="departmentForm" class="layui-form" action="" lay-filter="departmentForm">
                            <div class="layui-form-item">
                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">二级科室名称</label>
                                    <div class="layui-input-block">
                                        <input name="dcname" id="dcname" type="text" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">状态</label>
                                    <div class="layui-input-block">
                                        <select name="status">
                                            <option value="" selected>请选择</option>
                                            <option value="0">禁用</option>
                                            <option value="1">启用</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="layui-inline layui-col-md2">
                                    <button class="layui-btn" lay-submit lay-filter="departmentForm" id="queryBtn" type="button"><i class="layui-icon">&#xe615;</i>查询</button>
                                    <button class="layui-btn layui-btn-primary" type="reset"><i class="layui-icon">&#xe666;</i>重置</button>
                                </div>

                            </div>

                        </form>


                        <div>
                            <table class="layui-hide" id="departmentInfo" lay-filter="departmentInfo"></table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${mainDomain}/cloud/static/layui/layui.all.js"></script>
<script src="${mainDomain}/cloud/static/layui/index.js"></script>
<script type="text/html" id="dptToolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="addSecondDpt">新增</button>
        <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="modifySecondDpt">修改</button>
        <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="delSecondDpt">删除</button>
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
        var dptTab = laytable.render({
            elem: '#departmentInfo',
            url: '${mainDomain}/cloud/hrs/department/slist/find',
            contentType:"application/json;charset=UTF-8",
            toolbar: '#dptToolbar',
            method: 'post',
            where: {"fdptId":$("#fid").val()},
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
                , {field: 'dptCname', title: '科室名'}
                , {field: 'dptEname', title: '科室名(英文)'}
                , {field: 'status', title: '科室状态',templet:function (d){
                        if (d.status == 1) {
                            return "启用"
                        } else {
                            return "禁用"
                        }
                    }}
                , {field: 'dptRemark', title: '更新者'}
                , {field: 'updateBy', title: '更新者'}
                , {field: 'updateTime', title: '更新时间',templet:function (d) {
                        return timeStamp2YYYYMMdd(d.updateTime);
                    }}
            ]]
        });

        laytable.on('row(departmentInfo)', function(obj){
            console.log(obj.tr) //得到当前行元素对象
            $(obj.tr).find('input[type=radio]').prop("checked", true);
            form.render('radio');
            checkStatus.data=[];
            checkStatus.data.push(obj.data);

        });

        form.on('submit(departmentForm)',function (data) {
            console.log(JSON.stringify(data.field))
            dptTab.reload({
                contentType:"application/json;charset=UTF-8",
                where:{"dptCname":data.field.dptCname,"fdptId":$("#fid").val(),"status":data.field.status}
            });
            return false;
        });

        laytable.on('toolbar(departmentInfo)', function(obj){

            //var checkStatus = laytable.checkStatus(obj.config.id); //获取选中行状态
            switch(obj.event){
                case 'addSecondDpt':

                    var index = layer.open({
                        type: 2,
                        title: "新增二级科室",
                        content: '${mainDomain}/cloud/hrs/secondDpt/save?id='+$("#fid").val()+'&fName='+$("#fName").val(),
                        area: ['700px', '500px'],
                        maxmin: true
                    });
                    break;
                case 'modifySecondDpt':
                    var data = checkStatus.data;  //获取选中行数据
                    if(data.length == 0) {
                        layer.alert("请选择一条科室信息");
                        break;
                    }
                    var index = layer.open({
                        type: 2,
                        title: "修改二级科室",
                        content: '${mainDomain}/cloud/hrs/secondDpt/modify/'+data[0].id+'/?fid='+$("#fid").val()+'&fName='+$("#fName").val(),
                        area: ['700px', '500px'],
                        maxmin: true
                    });
                    break;
                case 'delSecondDpt':
                    var data = checkStatus.data;  //获取选中行数据
                    if(data.length == 0) {
                        layer.alert("请选择一条科室信息");
                        break;
                    }
                    layer.confirm('是否确定删除?', function(index){
                        $.post("${mainDomain}/cloud/hrs/secondDpt/del/"+data[0].id,{},function(e){
                            layer.close(index);
                            if(e.code==0){
                                dptTab.reload();
                            }else{
                                layer.alert(e.resultMsg);
                            }
                        });
                    });
                    break;
            };
        });
    });

    $(document).on("click","#returnBtn",function(){
        location.href = mainDomain+'/cloud/hrs/department/page';
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




</script>

</body>

</html>