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
                            <div class="sub-title-header">排班管理</div>
                            <div class="sub-title-right">
                            </div>
                        </div>
                        <hr>
                        <form id="scheduleForm" name="scheduleForm" class="layui-form" action="" lay-filter="scheduleForm">
                            <div class="layui-form-item">
                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">医生姓名</label>
                                    <div class="layui-input-block">
                                        <input name="userName" id="userName" type="text" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">医生级别</label>
                                    <div class="layui-input-block">
                                        <select id="doctorLevel" name="doctorLevel">
                                            <option value="" selected>请选择</option>
                                            <option value="1">普通</option>
                                            <option value="2">专家</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">联系方式</label>
                                    <div class="layui-input-block">
                                        <input name="userTelphone" id="userTelphone" type="text" class="layui-input">
                                    </div>
                                </div>

                            </div>
                            <div class="layui-form-item">
                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">科室</label>
                                    <div class="layui-input-block">
                                        <select id="fdptSlt" name="firstDptId" lay-filter="fdptSlt">
                                            <option value="">请选择</option>
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
                                    <button class="layui-btn" lay-submit lay-filter="scheduleForm" id="queryBtn" type="button"><i class="layui-icon">&#xe615;</i>查询</button>
                                    <button class="layui-btn layui-btn-primary" type="reset"><i class="layui-icon">&#xe666;</i>重置</button>
                                </div>

                            </div>

                        </form>
                        <hr class="layui-bg-green">

                        <div class="layui-tab layui-tab-brief" lay-filter="orderTab">
                            <ul class="layui-tab-title">
                                <li lay-id = '1' class="layui-this">出诊</li>
                                <li lay-id = '2'>停诊</li>
                            </ul>
                        </div>
                        <div>
                            <table class="layui-hide" id="scheduleInfo" lay-filter="scheduleInfo"></table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${mainDomain}/cloud/static/layui/layui.all.js"></script>
<script src="${mainDomain}/cloud/static/layui/index.js"></script>
<script type="text/html" id="scdToolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="addScd">新增</button>
        <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="modifyScd">修改</button>
        <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="delScd">删除</button>
    </div>
</script>

<script>
    var laytable;
    var $ = layui.jquery;

    layui.use(['table', 'form','layer','laydate','element'], function () {
        laytable = layui.table;

        var form = layui.form,
            layer = layui.layer,
            laydate = layui.laydate,
            element = layui.element;
        var checkStatus={};
        checkStatus.data=[];

        var scheduleType = 1;// 默认查询出诊记录
        //方法级渲染
        var scdTab = laytable.render({
            elem: '#scheduleInfo',
            url: '${mainDomain}/cloud/hrs/schedule/find',
            contentType:"application/json;charset=UTF-8",
            toolbar: '#scdToolbar',
            method: 'post',
            where: {"scheduleType":scheduleType},
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
                , {field: 'userName', title: '姓名'}
                , {field: 'dptInfo', title: '科室信息'}
                , {field: 'dateStart', title: '开始日期',templet:function (d){
                        return date2YYYYMMdd(d.dateStart);
                }}
                , {field: 'dateEnd', title: '结束日期',templet:function (d){
                        return date2YYYYMMdd(d.dateEnd);
                    }}
                , {field: 'daysOfWeek', title: '周期'}
                , {field: 'timePart', title: '时间段',templet:function (d){
                    var timePart = "";
                        if (d.moningFlag == 1) {
                            timePart += "上午";
                        }
                        if (d.moningFlag == 1 && d.afternoonFlag == 1) {
                            timePart += "，";
                        }
                        if (d.afternoonFlag == 1) {
                            timePart += "下午";
                        }
                        return timePart;
                    }}
                , {field: 'userTelphone', title: '联系方式'}
                , {field: 'updateBy', title: '更新者'}
                , {field: 'updateTime', title: '更新时间',templet:function (d) {
                        return timeStamp2YYYYMMdd(d.updateTime);
                    }}
            ]]
        });

        laytable.on('row(scheduleInfo)', function(obj){
            console.log(obj.tr) //得到当前行元素对象
            $(obj.tr).find('input[type=radio]').prop("checked", true);
            form.render('radio');
            checkStatus.data=[];
            checkStatus.data.push(obj.data);

        });

        element.on('tab(orderTab)', function(){
            scheduleType = $(this).attr('lay-id');
            scdTab.reload({
                where:{"scheduleType":scheduleType,"firstDptId":$("#fdptSlt").val(),"secondDptId":$("#sdptSlt").val(),"userTelphone":$("#userTelphone").val(),"doctorLevel":$("#doctorLevel").val(),"userName":$("#userName").val()}
            });
        });

        form.on('submit(scheduleForm)',function (data) {
            scdTab.reload({
                where:{"scheduleType":scheduleType,"firstDptId":$("#fdptSlt").val(),"secondDptId":$("#sdptSlt").val(),"userTelphone":$("#userTelphone").val(),"doctorLevel":$("#doctorLevel").val(),"userName":$("#userName").val()}
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

        laytable.on('toolbar(scheduleInfo)', function(obj){
            var scdType = "";
            if(scheduleType == 1){
                scdType = "出诊";
            } else if(scheduleType == 2){
                scdType = "停诊";
            }

            //var checkStatus = laytable.checkStatus(obj.config.id); //获取选中行状态
            switch(obj.event){
                case 'addScd':
                    var index = layer.open({
                        type: 2,
                        title: "新增"+ scdType +"排班",
                        content: '${mainDomain}/cloud/hrs/schedule/save?scheduleType='+scheduleType,
                        area: ['800px', '500px'],
                        maxmin: true
                    });
                    form.render();
                    break;
                case 'modifyScd':
                    var data = checkStatus.data;  //获取选中行数据
                    if(data.length == 0) {
                        layer.alert("请选择一条排班信息");
                        break;
                    }
                    var index = layer.open({
                        type: 2,
                        title: "修改"+ scdType +"排班",
                        content: '${mainDomain}/cloud/hrs/schedule/modify/'+data[0].id,
                        area: ['800px', '500px'],
                        maxmin: true
                    });
                    break;
                case 'delScd':
                    var data = checkStatus.data;  //获取选中行数据
                    if(data.length == 0) {
                        layer.alert("请选择一条排班信息");
                        break;
                    }
                    layer.confirm('是否确定删除?', function(index){
                        $.post("${mainDomain}/cloud/hrs/fisrtScd/del/"+data[0].id,{},function(e){
                            layer.close(index);
                            if(e.code==0){
                                scdTab.reload();
                            }else{
                                layer.alert(e.resultMsg);
                            }
                        });
                    });
                    break;
            };
        });
    });

    $(document).on("click",".propertyIcon",function(){
        location.href = mainDomain+'/cloud/hrs/secondScd/page?id='+$(this).attr("data-id")+"&fName="+$(this).attr("data-name");
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