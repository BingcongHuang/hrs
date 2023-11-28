<!doctype html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>用户注册</title>
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

<div>
        <header class='demos-header'>
            <h2 class="demos-second-title">用户注册</h2>
            <p class="demos-sub-title">请如实填写</p>
        </header>
        <div class="weui-cells__title">基本信息</div>
    <div class="weui-cells__title">姓名</div>
    <div class="weui-cells">
        <div class="weui-cell">
            <div class="weui-cell__bd">
                <input id="userRealname" name="userRealname" class="weui-input" type="text" placeholder="姓名">
            </div>
        </div>
    </div>
        <div class="weui-cells weui-cells_form">
            <div class="weui-cells__title">证件类型</div>
            <div class="weui-cells">
                <div class="weui-cell">
                    <div class="weui-cell__bd">
                        <select id="idType" class="weui-select">
                            <option value="">请选择</option>
                            <option value="1">身份证</option>
                            <option value="2">护照</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="weui-cells__title">证件号</div>
            <div class="weui-cells">
                <div class="weui-cell">
                    <div class="weui-cell__bd">
                        <input id="idNum" name="idNum" class="weui-input" type="text" placeholder="证件号">
                    </div>
                </div>
            </div>
             <div class="weui-cells__title">出生日期</div>
            <div class="weui-cells">
                <div class="weui-cell">
                    <div class="weui-cell__bd">
                        <input id="birthday" name="birthday" class="weui-input" type="date">
                    </div>
                </div>
            </div>
            <div class="weui-cells__title">手机号（登录账号）</div>
            <div class="weui-cells">
                <div class="weui-cell">
                    <div class="weui-cell__bd">
                        <input id="userTelphone" name="userTelphone" class="weui-input" type="text" placeholder="请输入手机号">
                    </div>
                </div>
            </div>
            <div class="weui-cells__title">登录密码</div>
            <div class="weui-cells">
                <div class="weui-cell">
                    <div class="weui-cell__bd">
                        <input id="userPassword" name="userPassword" class="weui-input"  type="text" type="password" placeholder="请输入登录密码">
                    </div>
                </div>
            </div>
            <div class="weui-cell weui-cell_vcode">
                <div class="weui-cell__hd"><label class="weui-label">验证码</label></div>
                <div class="weui-cell__bd">
                    <input id="vcode" name="vcode" class="weui-input" type="text" placeholder="请输入验证码">
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
    $(document).on("click", "#refushImg", function(){
        $("#checkImg").prop("src","${mainDomain}/cloud/getVerifiCode?a="+new Date().getTime());
    });

    $(document).on("click", "#registerBtn", function(){
        if('' === $("#userRealname").val()) {
            $.toptip("请填写姓名", 'warning');
            return false;
        }
        if('' === $("#userTelphone").val()) {
            $.toptip("请填写手机号", 'warning');
            return false;
        }
        if('' === $("#idType").val()) {
            $.toptip("请填写证件类型", 'warning');
            return false;
        }
        if('' === $("#idNum").val()) {
            $.toptip("请填写证件号码", 'warning');
            return false;
        }
        if('' === $("#birthday").val()) {
            $.toptip("请填写出生日期", 'warning');
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
            {'userRealname':$("#userRealname").val()
                ,'userTelphone':$("#userTelphone").val()
                ,'userPassword':$("#userPassword").val()
                ,'idType':$("#idType").val()
                ,'idNum':$("#idNum").val()
                ,'birthday':$("#birthday").val()
                ,'vcode':$("#vcode").val()}
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