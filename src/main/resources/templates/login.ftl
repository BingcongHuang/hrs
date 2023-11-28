<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>医院挂号云平台</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
<#--    <link rel="stylesheet" href="${miscDomain}css/layui.css" media="all">-->
<#--    <link rel="stylesheet" href="${miscDomain}css/modules/admin.css" media="all">-->
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="shortcut icon" href="${mainDomain}/cloud/static/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="${mainDomain}/cloud/static/layui/rc/css/layui.css" media="all">
    <link id="layuicss-layer" rel="stylesheet"
          href="${mainDomain}/cloud/static/layui/rc/css/modules/layer/default/layer.css?v=3.1.1" media="all">
    <link id="layuicss-layuiAdmin" rel="stylesheet" href="${mainDomain}/cloud/static/layui/admin.css" media="all">
    <link rel="stylesheet" href="${mainDomain}/cloud/static/layui/login.css" media="all">
    <script>
        var mainDomain = '${mainDomain}';
    </script>
</head>
<body>

<div class="layadmin-user-login layadmin-user-display-show" id="user-login" style="display: none;">

    <div class="layadmin-user-login-main">
        <div class="layadmin-user-login-box layadmin-user-login-header">
            <h2>医院挂号云平台</h2>
            <p>智能 便捷 安全</p>
        </div>
        <div class="layadmin-user-login-box layadmin-user-login-body layui-form">
            <div class="layui-form-item">
                <label class="layadmin-user-login-icon layui-icon layui-icon-username" for="login-username"></label>
                <input type="text" name="username" value="" id="login-username" lay-verify="required" placeholder="用户名" class="layui-input">
            </div>
            <div class="layui-form-item">
                <label class="layadmin-user-login-icon layui-icon layui-icon-password" for="login-password"></label>
                <input type="password" name="password" value="" id="login-password" lay-verify="required" placeholder="密码" class="layui-input">
            </div>
            <#--<div class="layui-form-item" style="margin-bottom: 20px;">-->
                <#--<input type="checkbox" name="remember" lay-skin="primary" title="记住密码">-->
            <#--</div>-->
            <div class="layui-form-item">
                <button class="layui-btn layui-btn-fluid" lay-submit lay-filter="user-login">登 录</button>
            </div>

            <div class="layui-form-item" style="text-align: right">
                <a href="${mainDomain}/cloud/user/toRegister" data-target="#full">注册</a>
            </div>

        </div>
    </div>

    <div class="layui-trans layadmin-user-login-footer">
        <a  href="${mainDomain}/cloud/register/search/toPage">挂号系统H5版</a>
        <p>© 2023 <a href="####" target="_blank">www.xxxx.com</a></p>
    </div>
</div>
<script src="${mainDomain}/cloud/static/layui/layui.all.js"></script>
<script src="${mainDomain}/cloud/static/layui/index.js"></script>
<script>
    var $;
    layui.use('form', function(){
        var form = layui.form;
        $ = layui.jquery;
        //监听提交
        form.on('submit(user-login)', function(data){
            var data  = "username="+data.field.username+"&password="+data.field.password;
            $.post('${mainDomain}/cloud/user/login',data,function (res) {
                if(res.code == 0){
                    if(res.data == 1) {
                        location.href='${mainDomain}/cloud/hrs/patient/register/toPage';
                    } else {
                        location.href='${mainDomain}/cloud';
                    }
                }else{
                    layer.msg(res.message);
                }
            });
            return false;
        });
    });
</script>
</body>
</html>