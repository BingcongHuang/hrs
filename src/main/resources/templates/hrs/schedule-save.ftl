<!DOCTYPE html>
<link rel="stylesheet" href="${mainDomain}/cloud/static/layui/rc/css/layui.css" media="all">
<link id="layuicss-layer" rel="stylesheet"
      href="${mainDomain}/cloud/static/layui/rc/css/modules/layer/default/layer.css?v=3.1.1" media="all">
<link id="layuicss-layuiAdmin" rel="stylesheet" href="${mainDomain}/cloud/static/layui/admin.css" media="all">
<style>
    html {
        background-color: #FFFFFF;
    }
</style>
<script>
    var mainDomain = '${mainDomain}';
</script>
<body>
<div>
    <div class="layui-form" style="padding-top: 20px" lay-filter="ScdForm">
        <#if ScdInfo??>
            <input id="id" name="id" type="hidden" value="${ScdInfo.id!''}">
            <input id="doctorUserId" name="doctorUserId" type="hidden" value="${ScdInfo.userId!''}">
        </#if>
        <input id="scheduleType" name="scheduleType" type="hidden" value="${scheduleType}">
        <#if ScdInfo??>
            <div class="layui-form-item">
                <label class="layui-form-label">科室信息</label>
                <div class="layui-input-block">
                    <input type="text" class="layui-input" value="${ScdInfo.dptInfo!''}" disabled>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">医生</label>
                <div class="layui-input-block">
                    <input type="text" class="layui-input" value="${ScdInfo.userName!''}" disabled>
                </div>
            </div>
        <#else>
            <div class="layui-form-item">
                <label class="layui-form-label">一级科室</label>
                <div class="layui-input-block">
                    <select id="fdptSlt" name="firstDptId" lay-filter="fdptSlt">
                        <option value="-1">请选择</option>
                        <#list dptInfos as dp>
                            <option value="${dp.id}">${dp.dptName}</option>
                        </#list>
                    </select>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">二级科室</label>
                <div class="layui-input-block">
                    <select id="sdptSlt" name="secondDptId" lay-filter="sdptSlt">
                        <option value="-1">请选择</option>
                    </select>
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">医生</label>
                <div class="layui-input-block">
                    <select id="doctorSlt" name="doctorUserId"  lay-filter="doctorSlt" lay-search>
                        <option value="-1">请选择</option>
                    </select>
                </div>
            </div>
        </#if>
        <div class="layui-form-item">
            <label class="layui-form-label">日期范围</label>
            <div class="layui-input-block">
                <input type="text" class="layui-input" id="scdDate">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">周期</label>
            <div class="layui-input-block">
                <input type="checkbox" lay-filter="days" class="days" name="days" value="1" title="周一" <#if ScdInfo?? && ScdInfo.daysOfWeek?contains('1')>checked</#if>>
                <input type="checkbox" lay-filter="days" class="days" name="days" value="2" title="周二" <#if ScdInfo?? && ScdInfo.daysOfWeek?contains('2')>checked</#if>>
                <input type="checkbox" lay-filter="days" class="days" name="days" value="3" title="周三" <#if ScdInfo?? && ScdInfo.daysOfWeek?contains('3')>checked</#if>>
                <input type="checkbox" lay-filter="days" class="days" name="days" value="4" title="周四" <#if ScdInfo?? && ScdInfo.daysOfWeek?contains('4')>checked</#if>>
                <input type="checkbox" lay-filter="days" class="days" name="days" value="5" title="周五" <#if ScdInfo?? && ScdInfo.daysOfWeek?contains('5')>checked</#if>>
                <input type="checkbox" lay-filter="days" class="days" name="days" value="6" title="周六" <#if ScdInfo?? && ScdInfo.daysOfWeek?contains('6')>checked</#if>>
                <input type="checkbox" lay-filter="days" class="days" name="days" value="7" title="周日" <#if ScdInfo?? && ScdInfo.daysOfWeek?contains('7')>checked</#if>>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">时间段</label>
            <div class="layui-input-block">
                <input type="checkbox" id="moningFlag" value="1" title="上午" <#if ScdInfo?? && ScdInfo.moningFlag == 1>checked</#if>>
                <input type="checkbox" id="afternoonFlag" value="1" title="下午" <#if ScdInfo?? && ScdInfo.afternoonFlag == 1>checked</#if>>
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

        var dateStart = '${scdDateStr}'.split("~")[0];
        var dateEnd = '${scdDateStr}'.split("~")[1];

        //日期范围
        var scdDate = laydate.render({
            elem: '#scdDate'
            ,range: '~'
            ,value:'${scdDateStr}'
            ,done: function(value, date){
                dateStart = value.split("~")[0];
                dateEnd = value.split("~")[1];
            }
        });

        var active = {
            save: function () {
                var data = form.val('ScdForm');
                var daysofweek = '';
                $("input[name='days']:checked").each(function(){
                    daysofweek += $(this).val();
                });

                if(daysofweek==''||daysofweek==null){
                    layer.alert("周期不能为空");
                    return false;
                }
                data.daysOfWeek = daysofweek;
                data.moningFlag = $("#moningFlag").prop('checked')? 1:0;
                data.afternoonFlag = $("#afternoonFlag").prop('checked')? 1:0;
                data.dateStart = $.trim(dateStart);
                data.dateEnd = $.trim(dateEnd);
                data.scheduleType = $("#scheduleType").val();
                var idx = layer.load(0);
                $.post("${mainDomain}/cloud/hrs/schedule/edit", data , function (res) {
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

        form.on('select(fdptSlt)',function(data){
            var fdptId = data.value;
            $("#sdptSlt").empty();
            $("#sdptSlt").append(new Option("请选择","-1"));
            form.render("select");
            $.post(mainDomain+'/cloud/hrs/department/slist/'+ fdptId +'/findAll',{},function(res){
                if(res.code == "0"){
                    $.each(res.data,function (index,item){
                        $("#sdptSlt").append(new Option(item.dptName,item.id));
                    });
                    form.render("select");
                }
            });
        });

        form.on('checkbox', function(data){
            form.render();
        })

        form.on('select(sdptSlt)',function(data){
            $("#doctorSlt").empty();
            $("#doctorSlt").append(new Option("请选择",""));
            form.render("select");
            $.post(mainDomain+'/cloud/user/doctor/list/findAll',{"firstDptId":$("#fdptSlt").val(),"secondDptId":$("#sdptSlt").val()},function(res){
                if(res.code == "0"){
                    $.each(res.data,function (index,item){
                        $("#doctorSlt").append(new Option(item.userRealName,item.userId));
                    });
                    form.render("select");
                }
            });
        });


        $('.js-method').on('click', function () {
            var othis = $(this), method = othis.data('method');
            active[method] ? active[method].call(this, othis) : '';
        });



    });


</script>
</body>
