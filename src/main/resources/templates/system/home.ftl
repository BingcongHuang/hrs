<!doctype html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>医院挂号云平台</title>
    <meta name="Referrer" content="origin" />
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
        #triangle04 {
            width: 0;
            height: 0;
            border: 35px solid transparent;
            border-right: 50px solid #3874EB;
        }

        .adviceBox {
            width: 100%;
            height: 48px;
            background: #EFF3F6;
            overflow: hidden;
            /* padding: 14px auto; */
        }

        .adviceBox .textBox {
            margin: 14px auto;
            width: 480px;
            height: 20px;
            display: flex;
            align-items: center;
        }

        .textBox img {
            width: 16px;
            height: 16px;
            margin-right: 4px;
        }

        .textBox span {
            width: 460px;
            height: 20px;
            font-size: 14px;
            font-weight: 400;
            color: rgba(78, 106, 249, 1);
            line-height: 20px;
        }

        .close {
            width: 16px;
            height: 16px;
            position: absolute;
            right: 26px;
            top: 16px;
        }

    </style>
    <script>
        var mainDomain = '${mainDomain}';
    </script>
</head>
<body layadmin-themealias="ocean" class="layui-layout-body">
<div class="boss" style="overflow: hidden">
    <div class="contentLayui" style="margin-top: 0px;">
        <div id="LAY_app" class="layadmin-tabspage-none ">

            <div class="layui-layout layui-layout-admin">

                <div class="layui-header" style="top: 0px;">
                    <!-- 头部区域 -->
                    <ul class="layui-nav layui-layout-left">
                        <li class="layui-nav-item layadmin-flexible" lay-unselect="">
                            <a href="javascript:;" layadmin-event="flexible" title="侧边伸缩">
                                <i class="layui-icon layui-icon-shrink-right" id="LAY_app_flexible"></i>
                            </a>
                        </li>
                        <li class="layui-nav-item" lay-unselect>
                            <a href="javascript:;" class="js-refresh" title="刷新">
                                <i class="layui-icon layui-icon-refresh-3"></i>
                            </a>
                        </li>
                        <#if notice??>
                        <li class="layui-nav-item" id="notice">
                        <span style="background-image: url('${mainDomain}/cloud/static/img/navBack.png');background-size: 100%;height: 30px;width: 333px; line-height: 30px"
                        class="layui-inline">
                        <span class="layui-inline"
                        style="color: #1EB5D0;padding-left: 50px;padding-right: 15px ;font-size: smaller"
                        onclick="openNotice()">
                        </span>
                        <#--<img src=" ${mainDomain}/cloud/static/img/close.png" onclick="closeNotice()"-->
                        <#--style="height: 15px; ">-->
                        <img src=" ${mainDomain}/cloud/static/img/click.png"
                        style="height: 15px; ">
                        </span>
                        </li>
                        </#if>
                    </ul>
                    <ul class="layui-nav layui-layout-right" lay-filter="layadmin-layout-right">
                        <#if notice??>
                            <li class="layui-nav-item" id="notice" style="margin-right: 15px;">
                    <span style="background-image: url('${mainDomain}/cloud/static/img/navBack.png');background-size: 100%;height: 28px;min-width: 299.69px; line-height: 30px"
                          class="layui-inline">
                        <span class="layui-inline"
                              style="color: #1EB5D0;padding-left: 30px;padding-right: 25px ;font-size: smaller"
                              onclick="openNotice()">
                        </span>
                    <#--<img src=" ${mainDomain}/cloud/static/img/close.png" onclick="closeNotice()"-->
                        <#--style="height: 15px; ">-->
                        <img src=" ${mainDomain}/cloud/static/img/click.png"
                             style="width: 10.39px;height: 13.8px;position: absolute;right: 8px;top: 8px;">
                    </span>
                            </li>
                        </#if>

                        <li class="layui-nav-item" lay-unselect="" id="basicBtn">
                            <div class="layui-inline">
                                <#--<img src="${avatar}" style="width: 30px;height: 30px">-->
                                <a href="javascript:;" style="display:inline"> <cite>
                                        <#--<#if roleName == 1>超级管理员,<#elseif roleName==6>地市管理员,<#else >普通员工,</#if>${name!''}</cite></a>-->
                                        ${userName!''}</cite></a>
                            </div>
                            <dl class="layui-nav-child" id="navBtn">

                                 <#--<dd><a lay-href="set/user/info">基本资料</a></dd>-->
                            <#--                        <dd><a lay-href="set/user/password">修改密码</a></dd>-->
                            <#--                        <hr>-->
                                                    <#--              <dd layadmin-event="logout" style="text-align: center;"><a href="${mainDomain}/cloud/login/loginOut">退出</a></dd>
                                                                    -->
                                        <a href="${mainDomain}/cloud/user/logout"><cite>退出</cite></a>
                                                    </dl>
                                </li>


                                <span class="layui-nav-bar"></span></ul>
                        </div>

                        <!-- 侧边菜单 -->
                <div class="layui-side layui-side-menu" style="top: 0px;">
                    <div class="layui-side-scroll">
                        <div class="layui-logo" lay-href="" style="top: 0px;padding:0px">
                            <span>
                                <img src="${mainDomain}/cloud/static/images/logo.jpg" style="height:50px;width: 230px;">
                            </span>
                        </div>
                        <ul class="layui-nav layui-nav-tree" lay-shrink="all">
                        <#if menuVOList??>
                            <#list menuVOList as menu>
                                <li data-name="${menu.menuName!''}"
                                    class="layui-nav-item">
                                    <a href="javascript:;" lay-tips="${menu.menuName!''}" lay-direction="2">
                                        <i class="layui-icon ${menu.menuIcon!''}"></i>
                                        <cite>${menu.menuName!''}</cite>
                                    </a>
                                    <#if menu.subMenu??>
                                        <#list menu.subMenu as sub>
                                            <dl class="layui-nav-child">
                                                <#if sub.url?length gt 4&&sub.url?substring(0,4)=='http'>
                                                    <dd data-name="${sub.menuName!''}" data-url="${sub.url}"  data-id="${sub.parentId}" data-menu="${sub.ID}"
                                                        class="layui-nav-itemed js-open-page">
                                                        <a href="javascript:;">${sub.menuName!''}</a>
                                                    </dd>
                                                <#else>
                                                    <dd data-name="${sub.menuName!''}" data-url="${mainDomain}/cloud${sub.url}"  data-id="${sub.parentId}" data-menu="${sub.ID}"
                                                        class="layui-nav-itemed js-open-page">
                                                        <a href="javascript:;">${sub.menuName!''}</a>
                                                    </dd>
                                                </#if>
                                            </dl>
                                        </#list>
                                    </#if>
                                </li>
                            </#list>
                        </#if>
                        </ul>
                    </div>
                </div>

                <!-- 主体内容 -->
                <div class="layui-body" id="LAY_app_body" style="top: 45px;">
                    <div class="layadmin-tabsbody-item layui-show">
                        <iframe src='${mainDomain}/cloud/index' frameborder="0" id="j-iframe" class="x-iframe"
                                style="width: 100%;height: 98%"></iframe>
                    </div>
                </div>
                <!-- 辅助元素，一般用于移动设备下遮罩 -->
                <div class="layadmin-body-shade" layadmin-event="shade"></div>

            </div>
        </div>
    </div>
</div>


<script src="${mainDomain}/cloud/static/layui/rc/layui.js"></script>
<script src="${mainDomain}/cloud/static/layui/index.js"></script>
<script>
    var admin;
    var $;
    layui.use(['admin', 'laytpl', 'jquery', 'element'], function () {
        admin = layui.admin;
        $ = layui.jquery;
        var element = layui.element;
        // 刷新
        var index;
        $('.js-refresh').on('click', function () {
            index = layer.load(0);
            $('#j-iframe').attr("src", $('#j-iframe').attr('src'));
        });


        var iframe = document.getElementById("j-iframe");
        iframe.onload = function () {
            layer.close(index);
        };
        // 初始化主页
        $('.x-iframe').attr("src", $('#fist').val());
        // 菜单点击事件
        $('.js-open-page').on('click', function () {
            index = layer.load(0);
            var url=$(this).attr('data-url');
            var menuId=$(this).attr('data-menu');
           // $('.x-iframe').attr("src", $(this).attr('data-url'));
            if(url.indexOf("?")>=0){
                url+="&parentId="+$(this).attr('data-id')
                url+="&menuId="+menuId
            }else{
                url+="?parentId="+$(this).attr('data-id')
                url+="&menuId="+menuId
            }
            console.log(url);
            $('.x-iframe').attr("src", url);
            layer.close(index);
        });
        // 自动选中菜单
        $('.js-open-page').each(function (i, item) {
            var url=$(this).attr('data-url');
            var menuId=$(this).attr('data-menu');
            if(url.indexOf("?")>=0){
                url+="&parentId="+$(this).attr('data-id')
                url+="&menuId="+menuId
            }else{
                url+="?parentId="+$(this).attr('data-id')
                url+="&menuId="+menuId
            }
            if (url == $('.x-iframe').attr("src")) {
                $(this).addClass("layui-this");
            }
        })

        $("#basicBtn").click(function () {

            if ($("#navBtn").is(":hidden")) {
                $("#navBtn").show();
            } else {
                $("#navBtn").hide();
            }

        })

        $('.close').click(function () {
            // console.log('66666666666');
            $('.adviceBox').hide();
            $('.layui-header').css('top','0');
            $('.layui-side').css('top','0');
            $('.layui-logo').css('top','0');
            $('.layui-body').css('top','50px');
        })
    });

    function openNotice() {
        //admin.openRight(550, '${mainDomain}/cloud/notice/showNotice');
        var index = layer.open({
            type: 2,
            title: "",
            content: '${mainDomain}/cloud/notice/showNotice',
            area: ['500px', '500px']
        });
    }

    function closeNotice() {
        $("#notice").hide();
    }


</script>


<div class="layui-layer-move"></div>
</body>


</html>
