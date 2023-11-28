<link rel="stylesheet" href="${mainDomain}/cloud/static/layui/rc/css/layui.css" media="all">
<link id="layuicss-layer" rel="stylesheet"
      href="${mainDomain}/cloud/static/layui/rc/css/modules/layer/default/layer.css?v=3.1.1" media="all">
<link id="layuicss-layuiAdmin" rel="stylesheet" href="${mainDomain}/cloud/static/layui/admin.css" media="all">
<style>
    html {
        background-color: #FFFFFF;
    }

    .title {
        color: #FF8402;
    }
    .only-read-text{
        background-color: #ebedef;
    }
    /*input.layui-input.layui-unselect {width:120px}*/
    input.layui-input {width:210px}
</style>
<script>
    var mainDomain = '${mainDomain}';
</script>
<body>
<div>
    <div class="layui-form" style="padding-top: 20px" lay-filter="sdptForm">
        <#if sdptInfo??><input id="id" name="id" type="hidden" value="${sdptInfo.id!''}"></#if>
        <input id="id" name="fdptId" type="hidden" value="${fdptId!''}">
        <div class="layui-form-item">
            <label class="layui-form-label">一级科室：</label>
            <div class="layui-input-block">
                <input  class="layui-input only-read-text" type="text" value="${fName!''}" >

            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">科室名</label>
            <div class="layui-input-block">
                <input  class="layui-input" id="dCname" name="dptCname" type="text" value="<#if sdptInfo??>${sdptInfo.dptCname!''}</#if>" >
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">科室名(英文)</label>
            <div class="layui-input-block">
                <input  class="layui-input" id="dEname" name="dptEname" type="text" value="<#if sdptInfo??>${sdptInfo.dptEname!''}</#if>" >
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">排序</label>
            <div class="layui-input-block">
                <input  class="layui-input" id="sortNum" name="sortNum" type="text" value="<#if sdptInfo??>${sdptInfo.sortNum!''}</#if>" >
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">状态</label>
            <div class="layui-input-block">
                <input type="radio" name="status" value="0" title="禁用" <#if sdptInfo??><#if sdptInfo.status == 0>checked</#if><#else>checked</#if>>
                <input type="radio" name="status" value="1" title="开启" <#if sdptInfo?? && sdptInfo.status == 1>checked</#if>>
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">科室简介</label>
            <div class="layui-input-block">
                <textarea placeholder="请输入内容" class="layui-textarea" name="dptRemark" id="dptRemark"><#if fdptInfo??>${fdptInfo.dptRemark!''}</#if></textarea>
            </div>
        </div>
    </div>
    <hr>
    <div class="layui-form-item" style="text-align: right;margin-right: 15px">
        <label class="layui-form-label">
        </label>
        <button class="layui-btn layui-btn-normal js-method" data-method="save">
            保存
        </button>
    </div>
</div>
<script src="${mainDomain}/cloud/static/layui/layui.all.js"></script>
<script src="${mainDomain}/cloud/static/layui/index.js"></script>
<script>
    <#--    checkDataRight(${menuId});-->
</script>
<script>

    layui.use(['form'], function () {
        var form = layui.form;
        var $ = layui.jquery;
        var active = {
            save: function () {
                var data = form.val('sdptForm');
                var dcname = $("#dCname").val();
                if(dcname==''||dcname==null){
                    layer.alert("科室名不能为空");
                    return false;
                }
                var idx = layer.load(0);
                $.post("${mainDomain}/cloud/hrs/secondDpt/edit", data , function (res) {
                    layer.close(idx);
                    if (res.code==0) {
                        layer.alert("保存成功", {icon: 6}, function () {
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                            window.parent.location.reload();
                        });
                        return false;
                    } else {
                        layer.alert(res.msg);
                        return false;
                    }
                });
                return false;
            }
        };

        $('.js-method').on('click', function () {
            var othis = $(this), method = othis.data('method');
            active[method] ? active[method].call(this, othis) : '';
        });

    });


</script>
</body>
