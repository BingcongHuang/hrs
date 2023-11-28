<!DOCTYPE html>
<link rel="stylesheet" href="${mainDomain}/cloud/static/layui/rc/css/layui.css" media="all">
<link id="layuicss-layer" rel="stylesheet"
      href="${mainDomain}/cloud/static/layui/rc/css/modules/layer/default/layer.css?v=3.1.1" media="all">
<link id="layuicss-layuiAdmin" rel="stylesheet" href="${mainDomain}/cloud/static/layui/admin.css" media="all">
<style>
    html {
        background-color: #FFFFFF;
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
<body>
<div>
    <div class="layui-form" style="padding-top: 20px" lay-filter="ChargesForm">
        <#if ChargesInfo??>
            <input id="id" name="id" type="hidden" value="${ChargesInfo.id!''}">
        </#if>
        <div class="layui-form-item">
            <label class="layui-form-label">级别</label>
            <div class="layui-input-block">
                <select id="doctorLevel" name="doctorLevel">
                    <option value="">请选择</option>
                    <option value="1" <#if ChargesInfo?? && ChargesInfo.doctorLevel == 1>selected</#if>>普通</option>
                    <option value="2" <#if ChargesInfo?? && ChargesInfo.doctorLevel == 2> selected</#if>>专家</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">挂号费</label>
            <div class="layui-input-block">
                <input type="text" name="registerCharges" class="layui-input" value="<#if ChargesInfo??>${ChargesInfo.registerCharges!''}</#if>">
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
<script src="${mainDomain}/cloud/static/layui/layui.js"></script>
<script src="${mainDomain}/cloud/static/layui/index.js"></script>

<script>

    layui.use(['form','laydate'], function () {
        var form = layui.form,
            laydate = layui.laydate;
        var $ = layui.jquery;

        var active = {
            save: function () {
                var data = form.val('ChargesForm');
                var idx = layer.load(0);
                $.post("${mainDomain}/cloud/hrs/register/charges/edit", data , function (res) {
                    layer.close(idx);
                    if (res.code==0) {
                        layer.alert("保存成功", {icon: 6}, function () {
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                            window.parent.location.reload();
                        });
                        return false;
                    } else {
                        layer.alert(res.message);
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
