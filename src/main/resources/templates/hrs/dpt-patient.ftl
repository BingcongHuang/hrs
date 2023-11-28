<!doctype html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>患者列表</title>
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
                            <div class="sub-title-header">患者列表</div>
                            <div class="sub-title-right">
                                医生信息：【${doctor.userName!''}】 【${doctor.firstDptStr!''}_${doctor.secondDptStr!''}】 【<#if doctor.doctorLevel==1>普通<#elseif doctor.doctorLevel==2>专家</#if>】
                            </div>
                        </div>
                        <hr>
                        <form id="dptPatientForm" name="dptPatientForm" class="layui-form" action="" lay-filter="dptPatientForm">
                            <div class="layui-form-item">
                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">患者名称</label>
                                    <div class="layui-input-block">
                                        <input name="patientUserName" id="patientUserName" type="text" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">患者ID</label>
                                    <div class="layui-input-block">
                                        <input name="patientUserId" id="patientUserId" type="text" class="layui-input">
                                    </div>
                                </div>

                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">状态</label>
                                    <div class="layui-input-block">
                                        <select name="status">
                                            <option value="" selected>请选择</option>
                                            <option value="0">未就诊</option>
                                            <option value="1">已就诊</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">日期</label>
                                    <div class="layui-input-block">
                                        <input name="registerDate" id="registerDate" type="text" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">手机号</label>
                                    <div class="layui-input-block">
                                        <input name="patientPhoneNum" id="patientPhoneNum" type="text" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-inline layui-col-md2">
                                    <button class="layui-btn" lay-submit lay-filter="dptPatientForm" id="queryBtn" type="button"><i class="layui-icon">&#xe615;</i>查询</button>
                                    <button class="layui-btn layui-btn-primary" type="reset"><i class="layui-icon">&#xe666;</i>重置</button>
                                </div>

                            </div>

                        </form>


                        <div>
                            <table class="layui-hide" id="dptPatientInfo" lay-filter="dptPatientInfo"></table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 弹出层内容 start-->
<div id="appendRegisterDiv" style="display: none">
    <div class="layui-card">
        <div class="layui-card-body">
            <input type="hidden" id="firstDptId" value="${doctor.firstDptId}">
            <input type="hidden" id="secondDptId" value="${doctor.secondDptId}">
            <input type="hidden" id="doctorUserId" value="${doctor.userId}">
            <input type="hidden" id="doctorLevel" value="${doctor.doctorLevel}">
            <form class="layui-form">
                <div class="layui-form-item">
                    <label class="layui-form-label" >就诊卡号</label>
                    <div class="layui-input-block">
                            <input type="text" id="appendUserId" required  lay-verify="required" placeholder="请输入就诊卡号" autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">时间段</label>
                    <div class="layui-input-block">
                        <input type="radio" name="timeFlag" value="1" title="上午">
                        <input type="radio" name="timeFlag" value="2" title="下午">
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <button class="layui-btn" type="button" id="appendBtn">提交</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="${mainDomain}/cloud/static/layui/layui.all.js"></script>
<script src="${mainDomain}/cloud/static/layui/index.js"></script>
<script type="text/html" id="dptToolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="appendRegister">追加</button>
        <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="consumeRegister">消号</button>
    </div>
</script>

<script>
    var laytable;
    var $ = layui.jquery;
    var rgsDate = '${registerDate}';
    var appentDiv;
    layui.use(['table', 'form','laydate'], function () {
        laytable = layui.table;

        var form = layui.form,
            laydate = layui.laydate;
        //日期范围
        var registerDate = laydate.render({
            elem: '#registerDate'
            ,value:'${registerDate}'
            ,done:function (value){
                rgsDate = value;
            }
        });

        var checkStatus={};
        checkStatus.data=[];
        //方法级渲染
        var dptTab = laytable.render({
            elem: '#dptPatientInfo',
            url: '${mainDomain}/cloud/hrs/register/patients/find',
            contentType:"application/json;charset=UTF-8",
            toolbar: '#dptToolbar',
            method: 'post',
            where: {"registerDate":rgsDate},
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
                , {field: 'patientName', title: '姓名'}
                , {field: 'age', title: '年龄'}
                , {field: 'timeFlag', title: '时间段',templet:function (d){
                        if (d.timeFlag == 1) {
                            return "上午"
                        } else if(d.timeFlag == 2) {
                            return "下午"
                        }
                    }}
                , {field: 'status', title: '状态',templet:function (d){
                        if (d.status == 0) {
                            return "未就诊"
                        } else if(d.status == 1) {
                            return "已就诊"
                        }
                    }}
                , {field: 'createTime', title: '挂号时间',templet:function (d) {
                        return timeStamp2YYYYMMdd(d.createTime);
                    }}
            ]]
        });

        laytable.on('row(dptPatientInfo)', function(obj){
            console.log(obj.tr) //得到当前行元素对象
            $(obj.tr).find('input[type=radio]').prop("checked", true);
            form.render('radio');
            checkStatus.data=[];
            checkStatus.data.push(obj.data);

        });

        form.on('submit(dptPatientForm)',function (data) {
            console.log(JSON.stringify(data.field))
            dptTab.reload({
                contentType:"application/json;charset=UTF-8",
                where:{"patientUserId":data.field.patientUserId,"patientUserName":data.field.patientUserName,"status":data.field.status,"registerDate":rgsDate,"patientPhoneNum":data.field.patientPhoneNum}
            });
            return false;
        });

        laytable.on('toolbar(dptPatientInfo)', function(obj){
            switch(obj.event){
                case 'appendRegister':
                    appentDiv = layer.open({
                        type: 1,
                        title: "追加挂号",
                        content: $("#appendRegisterDiv"),
                        area: ['500px', '250px'],
                        maxmin: true
                    });
                    break;
                case 'consumeRegister':
                    var data = checkStatus.data;  //获取选中行数据
                    if(data.length == 0) {
                        layer.alert("请选择一条患者信息");
                        break;
                    }
                    layer.confirm('是否确定消号?', function(index){
                        $.post("${mainDomain}/cloud/hrs/register/consume/",{"id":data[0].id,},function(e){
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

    $("#appendBtn").on("click",function (){
        var timeFlag = $("input:radio[name='timeFlag']:checked").val();
        $.post("${mainDomain}/cloud/hrs/register/add", {"doctorUserId":$("#doctorUserId").val(),"patientUserId":$("#appendUserId").val(),"timeFlag":timeFlag,"firstDptId":$("#firstDptId").val(),"secondDptId":$("#secondDptId").val(),"registerDate":rgsDate,"sourceType":2,"doctorLevel":$("#doctorLevel").val()} , function (res) {
            if (res.code==0) {
                layer.alert("保存成功", {icon: 6}, function () {
                    layer.close(appentDiv);
                });
                return false;
            } else {
                layer.alert(res.message);
                return false;
            }
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




</script>

</body>

</html>