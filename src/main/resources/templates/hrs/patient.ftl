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
    <link rel="stylesheet" href="${mainDomain}/cloud/static/css/custom.css" media="all">
    <link rel="stylesheet" href="${mainDomain}/cloud/static/weui/weui.min.css">
    <link rel="stylesheet" href="${mainDomain}/cloud/static/weui/jquery-weui.css">
    <link rel="stylesheet" href="${mainDomain}/cloud/static/weui/front.css">
    <script>
        var mainDomain = '${mainDomain}';
    </script>
</head>
<body ontouchstart>

<div class="weui-tab">
    <div class="weui-tab__bd">
        <div id="registerTab" class="weui-tab__bd-item weui-tab__bd-item--active">
            <div class="weui-cells__title">日期</div>
            <div class="weui-cells">
                <div class="weui-cell">
                    <div class="weui-cell__bd">
                        <div class="button-sp-area">
                            <#if earlyDates??>
                                <#list earlyDates as ed>
                                    <a href="javascript:;" data-date="${ed}" class="weui-btn weui-btn_mini <#if ed_index == 0>weui-btn_primary<#else>weui-btn_default</#if> dateBtn">${ed?substring(5)}</a>
                                </#list>
                            </#if>
                        </div>
                    </div>
                </div>
            </div>
            <div class="weui-cells__title">科室</div>
            <div class="weui-cells">
                <div class="weui-cell">
                    <div class="weui-cell__bd">
                        <select id="fdptSlt" class="weui-select" name="firstDptId" lay-filter="fdptSlt">
                            <option value="">请选择</option>
                            <#list dptInfos as dp>
                                <option value="${dp.id}">${dp.dptName}</option>
                            </#list>
                        </select>
                        <select id="sdptSlt" class="weui-select" name="secondDptId" lay-filter="sdptSlt">
                            <option value="">请选择</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="weui-panel">
                <div class="weui-panel__hd">医生列表</div>
                <div id="doctorList">

                </div>
            </div>
            <div class="weui-panel">
            </div>

        </div>
        <div id="scheduleTab" class="weui-tab__bd-item">
            <div class="weui-panel">
                <div class="weui-panel__hd">挂号列表</div>
                <div id="registerList">

                </div>
            </div>
            <div class="weui-panel">
            </div>

        </div>
        <div id="infoTab" class="weui-tab__bd-item">
            <div class="weui-panel weui-panel_access">
                <div class="weui-panel__hd">个人信息</div>
                <div class="weui-panel__bd">
                    <input type="hidden" id="userId" value="<#if user??>${user.userId!''}</#if>">
                    <div class="weui-media-box weui-media-box_text">
                        <div class="weui-flex">
                            <div class="weui-flex__item"><h4 class="weui-media-box__title">账号/手机号</h4></div>
                            <div class="weui-flex__item"><h4 class="weui-media-box__title"><#if user??>${user.userName!''}</#if></h4></div>
                        </div>
                    </div>
                    <div class="weui-media-box weui-media-box_text">
                        <div class="weui-flex">
                            <div class="weui-flex__item"><h4 class="weui-media-box__title">就诊卡号</h4></div>
                            <div class="weui-flex__item"><h4 class="weui-media-box__title"><#if user??>${user.userId!''}</#if></h4></div>
                        </div>
                    </div>
                    <div class="weui-media-box weui-media-box_text">
                        <div class="weui-flex">
                            <div class="weui-flex__item"><h4 class="weui-media-box__title">姓名</h4></div>
                            <div class="weui-flex__item"><h4 class="weui-media-box__title"><#if user??>${user.realName!''}</#if></h4></div>
                        </div>
                    </div>
                    <div class="weui-media-box weui-media-box_text">
                        <div class="weui-flex">
                            <div class="weui-flex__item"><h4 class="weui-media-box__title">出生日期</h4></div>
                            <div class="weui-flex__item"><h4 class="weui-media-box__title"><#if user??>${user.birthday!''}</#if></h4></div>
                        </div>
                    </div>
                    <div class="weui-media-box weui-media-box_text">
                        <div class="weui-flex">
                            <div class="weui-flex__item"><h4 class="weui-media-box__title">就诊二维码</h4></div>
                            <div class="weui-flex__item"><div id="code"></div></div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="weui-panel weui-panel_access">
                <a href="${mainDomain}/cloud/user/logout" class="weui-btn weui-btn_warn">退出登录</a>
            </div>
        </div>
    </div>

    <div class="weui-tabbar">
        <a href="#registerTab" id="registerBtn" class="weui-tabbar__item weui-bar__item--on">
            <div class="weui-tabbar__icon">
                <img src="${mainDomain}/cloud/static/weui/images/icon_nav_button.png" alt="">
            </div>
            <p class="weui-tabbar__label">挂号</p>
        </a>
        <a href="#scheduleTab" id="scheduleBtn" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${mainDomain}/cloud/static/weui/images/icon_nav_calendar.png" alt="">
            </div>
            <p class="weui-tabbar__label">就诊</p>
        </a>
        <a href="#infoTab" id="infoBtn" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${mainDomain}/cloud/static/weui/images/icon_nav_cell.png" alt="">
            </div>
            <p class="weui-tabbar__label">我</p>
        </a>
    </div>
</div>

<script src="${mainDomain}/cloud/static/layui/layui.all.js"></script>
<script src="${mainDomain}/cloud/static/layui/index.js"></script>

<script src="${mainDomain}/cloud/static/weui/jquery-2.1.4.js"></script>
<script src="${mainDomain}/cloud/static/weui/fastclick.js"></script>
<script>
    $(function() {
        FastClick.attach(document.body);
    });
</script>
<script src="${mainDomain}/cloud/static/weui/jquery-weui.js"></script>
<script type="text/javascript" src="${mainDomain}/cloud/static/js/qrcode.min.js"></script>

<script>
    var laytable;

    var qrocode = new QRCode(document.getElementById("code"),{
        width:120,
        height:120
    })
    qrocode.makeCode(${user.userId!''});
    layui.use(['table', 'form', 'element'], function () {
        laytable = layui.table;
        var $ = layui.jquery;
        var form = layui.form;
        var element = layui.element;
        //一些事件监听
        element.on('tab(demo)', function(data){
            console.log(data);
        });
    });

    $("#fdptSlt").on('change',function () {
        var fdptId = $("#fdptSlt").val();
        $("#sdptSlt").empty();
        $("#sdptSlt").append(new Option("请选择",""));
        $.post(mainDomain+'/cloud/hrs/department/slist/'+ fdptId +'/findAll',{},function(res){
            if(res.code == "0"){
                $.each(res.data,function (index,item){
                    $("#sdptSlt").append(new Option(item.dptName,item.id));
                });
            }
        });
    });


    $("#sdptSlt").on('change',function () {
        // 获取选中日期
        var  sltDate = $(".weui-btn_primary").attr("data-date");
        $.get(mainDomain+'/cloud/hrs/patient/register/search',{"firstDptId":$("#fdptSlt").val(),"secondDptId":$("#sdptSlt").val(),"registerDate":sltDate},function(res){
            $("#doctorList").html(res);
        });
    });

    $(".dateBtn").on('click',function (){
        //weui-btn_primary weui-btn_default
        $(".weui-btn_primary").removeClass("weui-btn_primary").addClass("weui-btn_default");
        $(this).removeClass("weui-btn_default");
        $(this).addClass("weui-btn_primary");
        if($("#fdptSlt").val() != '' && $("#sdptSlt").val() != '') {
            var  sltDate = $(".weui-btn_primary").attr("data-date")
            $.get(mainDomain+'/cloud/hrs/patient/register/search',{"firstDptId":$("#fdptSlt").val(),"secondDptId":$("#sdptSlt").val(),"registerDate":sltDate},function(res){
                $("#doctorList").html(res);
            });
        }
    });


    $(document).on("click", "#infoBtn", function(){
        var userId = $("#userId").val();
        condition = "";
        if(userId == '') {
            location.href='${mainDomain}/cloud/user/login';
        }
    });

    $(document).on("click", "#scheduleBtn", function(){
        var userId = $("#userId").val();
        if(userId == '') {
            location.href='${mainDomain}/cloud/user/login';
        }
// 获取选中日期
        $.get(mainDomain+'/cloud/hrs/patient/register/find',{"patientUserId":userId},function(res){
            $("#registerList").html(res);
        });
    });

    $(document).on("click", ".rgsLink", function(){
        // 获取选中日期
        var  sltDate = $(".weui-btn_primary").attr("data-date");
        var userId = $("#userId").val();
        if(userId == '') {
            location.href='${mainDomain}/cloud/user/login';
        }
        var doctorUserId = $(this).attr("data-docid");
        var timeFlag = $(this).attr("data-time");
        var registerCharges = $(this).attr("data-charges");
        $.post(mainDomain+'/cloud/hrs/register/add',{"doctorUserId":doctorUserId,"firstDptId":$("#fdptSlt").val(),"secondDptId":$("#sdptSlt").val(),"registerDate":sltDate,"patientUserId":userId,"timeFlag":timeFlag,"registerCharges":registerCharges,"sourceType":1},function(res){
            if(res.code === "0"){
                $.alert("挂号成功！");
                var  sltDate = $(".weui-btn_primary").attr("data-date")
                $.get(mainDomain+'/cloud/hrs/patient/register/search',{"firstDptId":$("#fdptSlt").val(),"secondDptId":$("#sdptSlt").val(),"registerDate":sltDate},function(res){
                    $("#doctorList").html(res);
                });
            } else {
                $.toptip(res.message, 'warning');
            }
        });
    });

    $(document).on("click", ".rgsCnl", function(){
        // 获取选中日期
        var userId = $("#userId").val();
        if(userId == '') {
            location.href='${mainDomain}/cloud/user/login';
        }
        var id = $(this).attr("data-id");
        $.post(mainDomain+'/cloud/hrs/register/cnl',{"id":id},function(res){
            if(res.code === "0"){
                $.alert("取消成功！");
            } else {
                $.toptip(res.message, 'warning');
            }
        });
    });
</script>

</body>

</html>