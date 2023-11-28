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
    <div class="layui-form" style="padding-top: 20px" lay-filter="RuleForm">
        <#if RuleInfo??>
            <input id="id" name="id" type="hidden" value="${RuleInfo.id!''}">
        </#if>
        <div class="layui-form-item">
            <label class="layui-form-label">日期范围</label>
            <div class="layui-input-block">
                <input type="text" class="layui-input" id="ruleDate">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">未使用限制数</label>
            <div class="layui-input-block">
                <input type="number" name="unusedLimit" class="layui-input" value="<#if RuleInfo??>${RuleInfo.unusedLimit!''}</#if>">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">追加限制数</label>
            <div class="layui-input-block">
                <input type="number" name="addtionLimit"  class="layui-input" value="<#if RuleInfo??>${RuleInfo.addtionLimit!''}</#if>">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">允许提前天数</label>
            <div class="layui-input-block">
                <input type="number" name="earlyDaysLimit"  class="layui-input" value="<#if RuleInfo??>${RuleInfo.earlyDaysLimit!''}</#if>">
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

        var dateStart = '${ruleDateStr}'.split("~")[0];
        var dateEnd = '${ruleDateStr}'.split("~")[1];

        //日期范围
        var ruleDate = laydate.render({
            elem: '#ruleDate'
            ,range: '~'
            ,value:'${ruleDateStr}'
            ,done: function(value, date){
                dateStart = value.split("~")[0];
                dateEnd = value.split("~")[1];
            }
        });

        var active = {
            save: function () {
                var data = form.val('RuleForm');
                data.dateStart = $.trim(dateStart);
                data.dateEnd = $.trim(dateEnd);
                var idx = layer.load(0);
                $.post("${mainDomain}/cloud/hrs/register/rule/edit", data , function (res) {
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
