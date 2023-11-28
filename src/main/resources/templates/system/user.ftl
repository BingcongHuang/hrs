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
                            <div class="sub-title-header">用户列表</div>
                            <div class="sub-title-right">
                            </div>
                        </div>



                        <hr>
                        <div class="LAY-app-message-btns" style="margin-bottom: 10px;">
                            <button class="layui-btn layui-btn-green layui-btn-sm " id="adduser">新建用户</button>
                        </div>
                        <div>
                            <table class="layui-hide" id="userinfo"></table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="editUserLayer" style="display: none">
    <div class="layui-card">
        <div class="layui-card-body">
            <form id="editUserForm" class="layui-form" action="" lay-filter="editUserForm">
                <input name="userId" type="hidden" value="">
                <div class="layui-form-item">
                    <label class="layui-form-label"><span style="color: red">*</span>登录名</label>
                    <div class="layui-input-block">
                        <input type="text" name="userName" required  lay-verify="required" placeholder="请输入登录名" autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label"><span style="color: red">*</span>初始密码</label>
                    <div class="layui-input-block">
                        <input type="password" name="userPass" required  lay-verify="required" placeholder="请输入初始密码" autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label"><span style="color: red">*</span>性别</label>
                    <div class="layui-input-block">
                        <select name="userSex">
                            <option value="0">男</option>
                            <option value="1">女</option>
                        </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label"><span style="color: red">*</span>真实姓名</label>
                    <div class="layui-input-block">
                        <input type="text" name="userRealName" required  lay-verify="required" placeholder="请输入真实姓名" autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label"><span style="color: red">*</span>手机号</label>
                    <div class="layui-input-block">
                        <input type="tel" name="userPhone" required  lay-verify="required" placeholder="请输入手机号" autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">邮箱</label>
                    <div class="layui-input-block">
                        <input type="email" name="userEmail"  placeholder="请输入邮箱" autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label"><span style="color: red">*</span>角色</label>
                    <div class="layui-input-block">
                        <select name="roleId" lay-filter="roleSlt">
                            <option value="-1">无角色</option>
                            <#list roleSelect as rs>
                                <option value="${rs.roleId}">${rs.role}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div id="doctorDiv" style="display: none">
                    <div class="layui-form-item">
                        <label class="layui-form-label">科室</label>
                        <div class="layui-input-block">
                            <div class="layui-inline">
                                <select id="fdptSlt" name="firstDptId" lay-filter="fdptSlt">
                                    <option value="-1">请选择</option>
                                    <#list dptInfos as dp>
                                        <option value="${dp.id}">${dp.dptName}</option>
                                    </#list>
                                </select>
                            </div>
                            <div class="layui-inline">
                                <select id="sdptSlt" name="secondDptId" lay-filter="sdptSlt">
                                    <option value="-1">请选择</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">医生级别</label>
                        <div class="layui-input-block">
                            <input type="radio" name="doctorLevel" value="1" title="普通" checked>
                            <input type="radio" name="doctorLevel" value="2" title="专家">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">医生级别</label>
                        <div class="layui-input-block">
                            <textarea name="doctorIntroduction" placeholder="请输入内容" class="layui-textarea"></textarea>
                        </div>
                    </div>
                </div>
                <div id="patientDiv" style="display: none">
                    <div class="layui-form-item">
                        <label class="layui-form-label">出生日期</label>
                        <div class="layui-input-block">
                            <input name="birthday" class="layui-input" id="birthday">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">证件类型</label>
                        <div class="layui-input-block">
                            <select id="idType" name="idType">
                                <option value="">请选择</option>
                                <option value="1">身份证</option>
                                <option value="2">护照</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">证件号</label>
                        <div class="layui-input-block">
                            <input name="idNum" class="layui-input" id="idNum">
                        </div>
                    </div>
                </div>

                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <button class="layui-btn" lay-submit lay-filter="editUserForm">立即提交</button>
                        <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                    </div>
                </div>
            </form>

        </div>
    </div>
</div>
<script src="${mainDomain}/cloud/static/layui/layui.all.js"></script>
<script src="${mainDomain}/cloud/static/layui/index.js"></script>
<script>
    var $ = layui.jquery;
    var laytable;
    var active = {};
    // var fdptMap = [];
    //
    // $(document).ready(function (){
    //     $.post(mainDomain+'/cloud/hrs/department/flist/findAll',{},function(res){
    //         if(res.code == "0"){
    //             $.each(res.data,function (index,item){
    //                 fdptMap.push(item.id,item.dptName);
    //             });
    //         }
    //     });
    //     console.log(fdptMap);
    // });



    layui.use(['table', 'form', 'laydate','admin'], function () {
        laytable = layui.table;
        var form = layui.form;
        var laydate = layui.laydate;
        var userMap = new Map();
        // 事件
        active = {
            // 查询
            query: function (othis) {
                //执行重载
                laytable.reload('testReload', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    , where: {
                        // name: $('#s_role_name').val()
                    }
                    , loading: true
                });
            },
            //权限
            addRole: function (othis) {
                var id = othis.data('id');
                var idx;
                layer.open({
                    type: 2
                    , title: '用户赋权'
                    , id: 'auth_0'
                    , content: '${mainDomain}/cloud/role/roleCheck'
                    , area: ['650px', '400px']
                    , btn: ['保存', '取消']
                    , closeBtn: false
                    , shade: 0.2 //不显示遮罩
                    , success: function (layero, index) {

                    }
                    , yes: function (index, layero) {
                        var body = layer.getChildFrame('body', index);
                        var roleid = body.find('input:radio[name="role"]:checked').val();
                        if(roleid!=null&&roleid!=''){

                            $.post('${mainDomain}/cloud/sysUser/addRole',{roleid:roleid,userid:1},function(res){

                                if(res.code==0){
                                    layer.closeAll();
                                }
                            })

                        }else{
                            layer.msg('请选择角色', {icon: 1, time: 1000});
                        }

                    }
                    , btn2: function (index, layero) {
                        layer.closeAll();
                    }
                });
            },
            refashTable: function () {
                laytable.reload('testReload');
            }
        };

        //方法级渲染
        var userTab = laytable.render({
            elem: '#userinfo',
            url: '${mainDomain}/cloud/setting/user/list',
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
                {field: 'userId', title: 'ID', width: 80, sort: true, fixed: true}
                , {field: 'userName', title: '登录名', width: 160}
                , {field: 'userRealName', title: '真实姓名'}
                , {field: 'roleName', title: '角色'}
                ,{field: 'userSex', title: '性别',templet:function(d){

                        if(d.userSex==0){
                            return '男';
                        }else if(d.userSex==1){
                            return '女';
                        } else {
                            return "-";
                        }
                    }}
                ,{field: 'userPhone', title: '用户手机'}
                ,{field:'userEmail',title:'用户邮箱'}
                ,{field: 'locked', title: '锁定',templet:function(d){

                        if(d.locked==0){
                            return '<span class="layui-badge layui-bg-green">未锁定</span>';
                        }else if(d.locked==1){
                            return '<span class="layui-badge">锁定</span>';;
                        }

                    }}
                , {
                    field: 'classify', title: '操作',
                    templet: function (d) {
                        userMap.set(d.userId,d)
                        var a ='<button class="layui-btn layui-btn-xs layui-btn-normal editUser" edit-id="'+d.userId+'">编辑</button>';
                        if(d.locked){
                            a+='<button class="layui-btn layui-btn-xs layui-btn-normal unlockUser" lock-id="'+d.userId+'">解锁</button>';
                        } else {
                            a+='<button class="layui-btn layui-btn-xs layui-btn-normal lockUser" lock-id="'+d.userId+'">锁定</button>';
                        }
                        a+='<button class="layui-btn layui-btn-xs layui-btn-normal delUser" del-id="'+d.userId+'">删除</button>';
                        return  a;
                    }
                }
            ]]
            , id: 'testReload'
            , done: function (res, curr, count) {
                $('#userinfo').next().find('.layui-table-body').find('.js-method').on('click', function () {
                    var othis = $(this), method = othis.data('method');
                    active[method] ? active[method].call(this, othis) : '';
                });
            }
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

        laydate.render({
            elem: '#birthday' //指定元素
        });

        form.on('select(roleSlt)',function(data){
            var role = data.value;
            if(role == 2) {
                $("#doctorDiv").show();
                $("#patientDiv").hide();
                $("#birthday").val("");
            } else if(role == 3) {
                $("#doctorDiv").hide();
                $("#patientDiv").show();
                $("input[name='doctorLevel'][value='1']").prop("checked",true);
                $("#sdptSlt").val(-1);
                $("#sdptSlt").empty();
                $("#sdptSlt").append(new Option("请选择",""));
                form.render();
            } else {
                $("#doctorDiv").hide();
                $("#patientDiv").hide();
                $("#birthday").val("");
                $("input[name='doctorLevel'][value='1']").prop("checked",true);
                $("#fdptSlt").val(-1);
                $("#sdptSlt").empty();
                $("#sdptSlt").append(new Option("请选择",""));
                form.render();
            }
            form.render();

        });

        //事件绑定
        $('.js-method').on('click', function () {
            var othis = $(this), method = othis.data('method');
            active[method] ? active[method].call(this, othis) : '';
        });

        var userDiv
        $(document).on("click","#adduser",function(){
            $("#editUserForm")[0].reset();
            $("#doctorDiv").hide();
            $("#patientDiv").hide();
            form.render();
            userDiv =layer.open({
                type: 1,
                title:'新增用户',
                area: ['600px', '800px'],
                content: $("#editUserLayer")
            });
        });

        $(document).on("click",".editUser",function(){
            var userId =$(this).attr("edit-id");
            var userInfo = userMap.get(userId);
            userInfo.roleId = userInfo.role
            if(userInfo.role == 2) {
                // $("input[name='doctorLevel'][value="+ userInfo.doctorLevel +"]").prop("checked",true);
                // $("#fdptSlt").val(userInfo.firstDptId)
                $.post(mainDomain+'/cloud/hrs/department/slist/'+ userInfo.firstDptId +'/findAll',{},function(res){
                    if(res.code == "0"){
                        $.each(res.data,function (index,item){
                            $("#sdptSlt").append(new Option(item.dptName,item.id));
                        });
                        $("#sdptSlt").val(userInfo.secondDptId)
                        form.render("select");
                    }
                });
            }
            form.val("editUserForm",userInfo);
            if(userInfo.role == 1){
                $("#doctorDiv").hide();
                $("#patientDiv").hide();
            } else if(userInfo.role == 2){
                $("#doctorDiv").show();
                $("#patientDiv").hide();
            } else if(userInfo.role == 3){
                $("#doctorDiv").hide();
                $("#patientDiv").show();
            }
            userDiv =layer.open({
                type: 1,
                title:'编辑用户',
                area: ['600px', '800px'],
                content: $("#editUserLayer")
            });
        });

        //监听提交
        form.on('submit(editUserForm)', function(data){
            $.ajax({
                type: 'POST',
                url: mainDomain+'/cloud/setting/user/edit',
                data: JSON.stringify(data.field),
                success: function(res){
                    if(res.code==0){
                        layer.close(userDiv);
                        layer.alert("操作成功！");
                        userTab.reload();
                    }else {
                        layer.msg(res.message);
                    }
                },
                contentType: 'application/json'
            });

            return false;
        });

        $(document).on("click",".lockUser",function(){
            var userId = $(this).attr("lock-id");
            $.post(mainDomain+'/cloud/setting/user/disable',{'userId':userId},function(res){
                layer.alert("锁定成功！");
                userTab.reload();
            })
        });
        $(document).on("click",".unlockUser",function(){
            var userId = $(this).attr("lock-id");
            $.post(mainDomain+'/cloud/setting/user/enable',{'userId':userId},function(res){
                layer.alert("解锁成功！");
                userTab.reload();
            })
        });
        $(document).on("click",".delUser",function(){
            var _this = $(this);
            layer.confirm('确认删除该用户', {
                btn: ['确定', '取消']

            }, function(index, layero){
                var userId = _this.attr("del-id");
                $.post(mainDomain+'/cloud/setting/user/del',{'userId':userId},function(res){
                    layer.alert("删除成功！");
                    userTab.reload();
                    layer.close(index);
                })
            }, function(index){
                layer.close(index);
            });
        });

    });


</script>

</body>

</html>