<!doctype html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>工具设备管理</title>
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
                            <div class="sub-title-header">工具设备管理</div>
                            <div class="sub-title-right">
                            </div>
                        </div>
                        <hr>
                        <form id="catQueryForm" name="catQueryForm" class="layui-form" action="" lay-filter="catQueryForm">
                            <div class="layui-form-item">
                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">件号</label>
                                    <div class="layui-input-block">
                                        <input name="toolCode" id="toolCode" type="text" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">名称</label>
                                    <div class="layui-input-block">
                                        <input name="toolName" id="toolName" type="text" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-inline  layui-col-md3">
                                    <label class="layui-form-label">英文名称</label>
                                    <div class="layui-input-block">
                                        <input name="toolEname"  id="toolEname" type="text" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-inline layui-col-md2">
                                    <button class="layui-btn" lay-submit lay-filter="catQueryForm" id="queryBtn" type="button"><i class="layui-icon">&#xe615;</i>查询</button>
                                    <button class="layui-btn layui-btn-primary" type="reset"><i class="layui-icon">&#xe666;</i>重置</button>
                                </div>

                            </div>

                        </form>


                        <div>
                            <table class="layui-hide" id="mistakeinfo" lay-filter="mistakeinfo"></table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="fileDiv" style="display: none">
    <div class="layui-card">
        <div class="layui-card-header"><a href="${mainDomain}/cloud/static/template/工具设备模板.xlsx" target="_blank">模板下载</a></div>
        <div class="layui-card-body">
            <div class="layui-upload">
                <button type="button" class="layui-btn layui-btn-normal" id="test8">选择文件</button>
                <button type="button" class="layui-btn" id="test9">开始上传</button>
            </div>
        </div>
    </div>
</div>
<script src="${mainDomain}/cloud/static/layui/layui.all.js"></script>
<script src="${mainDomain}/cloud/static/layui/index.js"></script>
<script type="text/html" id="catToolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="addMistake">新增</button>
        <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="modifyMistake">修改</button>
        <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="delMistake">删除</button>
        <button class="layui-btn layui-btn-normal layui-btn-sm" id="test1">数据导入</button>
        <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="uploadExcel">模板下载</button>
    </div>
</script>

<script>
    var laytable;

    layui.use(['table', 'form', 'laydate','upload'], function () {
        laytable = layui.table;
        var $ = layui.jquery;
        var form = layui.form;
        var laydate = layui.laydate;
        var upload = layui.upload;

        var flightDate = laydate.render({
            elem: '#flightDate'
        });

        var checkStatus={};
        checkStatus.data=[];
        //方法级渲染
        var catTab = laytable.render({
            elem: '#mistakeinfo',
            url: '${mainDomain}/cloud/tool/list',
            contentType:"application/json;charset=UTF-8",
            toolbar: '#catToolbar',
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
                //代理企业名称、企业备案编号、登记时间
                {type:'radio'}
                , {field: 'toolCode', title: '件号'}
                , {field: 'seqNo', title: '序号'}
                , {field: 'toolName', title: '名称'}
                , {field: 'toolEname', title: '英文名称'}
                , {field: 'aircraftType', title: '适用机型'}
                , {field: 'technicalParam', title: '主要技术参数'}
                , {field: 'manualChapters', title: '手册章节'}
                , {field: 'toolStatus', title: '工具设备状态'}
                , {field: 'productionDate', title: '出厂日期'}
                , {field: 'merchant', title: '厂家'}
                , {field: 'stock', title: '数量'}
                , {field: 'oraginalPrice', title: '原价'}
                , {field: 'dailyRent', title: '日租金'}
                , {field: 'company', title: '所属公司'}
                , {field: 'linkman', title: '联系人'}
                , {field: 'oraginalPrice', title: '特别说明'}
                , {field: 'remark', title: '备注'}
                , {field: 'createTime', title: '创建时间',templet:function (d) {
                        return timeStamp2YYYYMMdd(d.createTime);
                    }}
            ]]
        });

        laytable.on('row(mistakeinfo)', function(obj){
            console.log(obj.tr) //得到当前行元素对象
            $(obj.tr).find('input[type=radio]').prop("checked", true);
            form.render('radio');
            checkStatus.data=[];
            checkStatus.data.push(obj.data);

        });

        form.on('submit(catQueryForm)',function (data) {
            console.log(JSON.stringify(data.field))
            catTab.reload({
                contentType:"application/json;charset=UTF-8",
                where:{"toolCode":data.field.toolCode,"toolName":data.field.toolName,"toolEname":data.field.toolEname}
            });
            return false;
        });

        laytable.on('toolbar(mistakeinfo)', function(obj){

            //var checkStatus = laytable.checkStatus(obj.config.id); //获取选中行状态
            switch(obj.event){
                case 'addMistake':

                    var index = layer.open({
                        type: 2,
                        title: "新增工具设备",
                        content: '${mainDomain}/cloud/tool/save',
                        area: ['1024px', '980px'],
                        maxmin: true
                    });


                    break;
                case 'modifyMistake':
                    var data = checkStatus.data;  //获取选中行数据
                    if(data.length == 0) {
                        layer.alert("请选择一条工具设备信息");
                        break;
                    }
                    var index = layer.open({
                        type: 2,
                        title: "修改工具设备",
                        content: '${mainDomain}/cloud/tool/modify/'+data[0].id,
                        area: ['1024px', '980px'],
                        maxmin: true
                    });
                    break;
                case 'delMistake':
                    var data = checkStatus.data;  //获取选中行数据
                    if(data.length == 0) {
                        layer.alert("请选择一条工具设备信息");
                        break;
                    }
                    layer.confirm('是否确定删除?', function(index){


                        $.post("${mainDomain}/station/cat/mistake/del/"+data[0].id,{},function(e){

                            layer.close(index);
                            if(e.code==0){
                                catTab.reload();
                            }else{
                                layer.alert(e.resultMsg);
                            }

                        });


                    });
                    break;
                case 'uploadExcel':
                    const elt = document.createElement('a');
                    elt.setAttribute('href', '${mainDomain}/cloud/static/template/工具设备模板.xlsx');
                    // elt.setAttribute('download', 'file.png');
                    elt.style.display = 'none';
                    document.body.appendChild(elt);
                    elt.click();
                    document.body.removeChild(elt);
                    break;
            };
        });
        //执行实例
        var uploadInst = upload.render({
            elem: '#test1' //绑定元素
            ,url: '${mainDomain}/cloud/tool/uploadExcel' //上传接
            ,accept: 'file'
            ,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
                layer.load(); //上传loading
            }
            ,done: function(res){
                //上传完毕回调
                layer.closeAll('loading'); //关闭loading
                layer.msg('上传成功');
                catTab.reload({
                    contentType:"application/json;charset=UTF-8",
                    where:{"toolCode":$("#toolCode").val(),"toolName":$("#toolName").val(),"toolEname":$("#toolEname").val()}
                });
            }
            ,error: function(){
                layer.closeAll('loading'); //关闭loading
                layer.msg(res.message);
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