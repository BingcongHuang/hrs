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
        <div id="searchTab" class="weui-tab__bd-item weui-tab__bd-item--active">
            <div class="weui-search-bar" id="searchBar">
                <form class="weui-search-bar__form" action="#">
                    <div class="weui-search-bar__box">
                        <i class="weui-icon-search"></i>
                        <input type="search" class="weui-search-bar__input" id="searchInput" placeholder="搜索" required="">
                        <a href="javascript:" class="weui-icon-clear" id="searchClear"></a>
                    </div>
                    <label class="weui-search-bar__label" id="searchText" style="transform-origin: 0px 0px 0px; opacity: 1; transform: scale(1, 1);">
                        <i class="weui-icon-search"></i>
                        <span>搜索</span>
                    </label>
                </form>
                <a href="javascript:" class="weui-search-bar__cancel-btn" id="searchCancel">取消</a>
            </div>
            <div class="page__bd" id="resultDiv">
            </div>
        </div>
        <div id="infoTab" class="weui-tab__bd-item">
            <div class="weui-panel weui-panel_access">
                <div class="weui-panel__hd">个人信息</div>
                <div class="weui-panel__bd">
                    <input type="hidden" id="userId" value="<#if user??>${user.userId!''}</#if>">
                    <div class="weui-media-box weui-media-box_text">
                        <div class="weui-flex">
                            <div class="weui-flex__item"><h4 class="weui-media-box__title">账号</h4></div>
                            <div class="weui-flex__item"><h4 class="weui-media-box__title"><#if user??>${user.userName!''}</#if></h4></div>
                        </div>
                    </div>
                    <div class="weui-media-box weui-media-box_text">
                        <div class="weui-flex">
                            <div class="weui-flex__item"><h4 class="weui-media-box__title">所属公司</h4></div>
                            <div class="weui-flex__item"><h4 class="weui-media-box__title"><#if user??>${user.company!''}</#if></h4></div>
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
        <a href="#searchTab" id="searchBtn" class="weui-tabbar__item weui-bar__item--on">
<#--            <span class="weui-badge" style="position: absolute;top: -.4em;right: 1em;">8</span>-->
            <div class="weui-tabbar__icon">
                <img src="${mainDomain}/cloud/static/weui/images/icon_nav_button.png" alt="">
            </div>
            <p class="weui-tabbar__label">查询</p>
        </a>
        <a href="#infoTab" id="infoBtn" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${mainDomain}/cloud/static/weui/images/icon_nav_cell.png" alt="">
            </div>
            <p class="weui-tabbar__label">我</p>
        </a>
    </div>
</div>

<div id="full" class='weui-popup__container'>
    <div class="weui-popup__overlay"></div>
    <div class="weui-popup__modal">

        <header class='demos-header'>
            <h2 class="demos-second-title">用户注册</h2>
            <p class="demos-sub-title">请如实填写</p>
        </header>
        <div class="weui-cells__title">基本信息</div>
        <div class="weui-cells weui-cells_form">
            <div class="weui-cells__title">手机号</div>
            <div class="weui-cells">
                <div class="weui-cell">
                    <div class="weui-cell__bd">
                        <input id="userTelphone" class="weui-input" type="text" placeholder="请输入手机号">
                    </div>
                </div>
            </div>
            <div class="weui-cells__title">登录密码</div>
            <div class="weui-cells">
                <div class="weui-cell">
                    <div class="weui-cell__bd">
                        <input id="userPassword" class="weui-input"  type="text" type="password" placeholder="请输入登录密码">
                    </div>
                </div>
            </div>
            <div class="weui-cells__title">所属公司</div>
            <div class="weui-cells">
                <div class="weui-cell">
                    <div class="weui-cell__bd">
                        <input id="company" class="weui-input" type="text" placeholder="请输入公司名称">
                    </div>
                </div>
            </div>
            <div class="weui-cell weui-cell_vcode">
                <div class="weui-cell__hd"><label class="weui-label">验证码</label></div>
                <div class="weui-cell__bd">
                    <input id="vcode" class="weui-input" type="text" placeholder="请输入验证码">
                </div>
                <div class="weui-cell__ft">
                    <a id="refushImg"><img id="checkImg" title="点击刷新验证码" style="cursor:pointer;width: 100px;height: 36px;margin: 5px 0 0 5px;border-radius: 3px;"
                                           class="weui-vcode-img" src="${mainDomain}/cloud/getVerifiCode"></a>
                </div>
            </div>
        </div>
        <br>
        <div>
            <a href="javascript:;" class="weui-btn weui-btn_primary" id="registerBtn">注册</a>
            <a href="javascript:;" class="weui-btn weui-btn_primary close-popup">关闭</a>
        </div>
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

<script>
    var laytable;

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

    var page = 1;
    var limit = 2;
    var dataCount = 0;
    var condition = "";

    $("#searchInput").on('blur',function () {
        // $("#searchInput").blur();
        var str = $(this).val();
        if(str === ''){
            $("#resultDiv").html("");
            return false;
        }
        if(condition === str) {
            return false;
        }
        var idx = layer.load(0);
        $.post("${mainDomain}/cloud/tool/search", {'str':str,'isAppend':0} , function (res) {
            layer.close(idx);
            $("#resultDiv").html(res);
            condition = str;
        });
    });

    $(document).on("click", "#searchMore", function(e){
        var str = $("#searchInput").val();
        page += 1;
        var idx = layer.load(0);
        $.post("${mainDomain}/cloud/tool/search", {'str':str,'isAppend':1,'page':page} , function (res) {
            layer.close(idx);
            $("#searchMoreLink").remove();
            $("#resultDiv").append(res);
        });
    });

    $(document).on("click", ".weui-search-bar__cancel-btn", function(){
        $(".weui-search-bar").removeClass("weui-search-bar_focusing");
        $("#searchInput").val("");
        $("#resultDiv").html("");
        condition = "";
    });

    $(document).on("click", ".weui-icon-clear", function(){
        $("#searchInput").val("").focus();
        $("#resultDiv").html("");
        condition = "";
    });
    $(document).on("click", "#searchBtn", function(){
        $("#searchInput").val("").focus();
        $("#resultDiv").html("");
        condition = "";
    });

    $(document).on("click", "#refushImg", function(){
        $("#checkImg").prop("src","${mainDomain}/cloud/getVerifiCode?a="+new Date().getTime());
    });

    $(document).on("click", "#loginBtn", function(){
        location.href='${mainDomain}/cloud/user/login';
    });

    $(document).on("click", "#infoBtn", function(){
        var userId = $("#userId").val();
        condition = "";
        if(userId == '') {
            location.href='${mainDomain}/cloud/user/login';
        }
    });
    $(document).on("click", "#registerBtn", function(){
        if('' === $("#userTelphone").val()) {
            $.toptip("请填写手机号", 'warning');
            return false;
        }
        if('' === $("#userPassword").val()) {
            $.toptip("请填写密码", 'warning');
            return false;
        }
        if('' === $("#vcode").val()) {
            $.toptip("请填写验证码", 'warning');
            return false;
        }
        var regx = /^1[0-9]{10}$/;
        if(!regx.test($("#userTelphone").val())) {
            $.toptip("请输入正确的手机号", 'warning');
            return false;
        }

        var idx = layer.load(0);
        $.post("${mainDomain}/cloud/user/register",
            {'userTelphone':$("#userTelphone").val(),
                'userPassword':$("#userPassword").val(),
                'company':$("#company").val(),
                'vcode':$("#vcode").val()}
            , function (res) {
            layer.close(idx);
            if(res.code == '0'){
                $.alert("注册成功！","恭喜").click(function () {
                    location.href='${mainDomain}/cloud/user/login';
                });
            } else {
                $.toptip(res.message, 'warning');
            }
        });
    });
</script>

</body>

</html>