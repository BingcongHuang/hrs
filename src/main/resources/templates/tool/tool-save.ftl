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
    <div class="layui-form" style="padding-top: 20px" lay-filter="mistake">
        <#if toolInst??><input id="id" name="id" type="hidden" value="${toolInst.id!''}"></#if>

        <div class="layui-form-item">
            <div class="layui-inline  ">
                <label class="layui-form-label">件号</label>
                <div class="layui-input-block">
                    <input  class="layui-input" id="toolCode" name="toolCode" type="text" value="<#if toolInst??>${toolInst.toolCode!''}</#if>" >
                </div>
            </div>
            <div class="layui-inline  ">
                <label class="layui-form-label">序号</label>
                <div class="layui-input-block">
                    <input  class="layui-input"  name="seqNo" type="text" value="<#if toolInst??>${toolInst.seqNo!''}</#if>" >
                </div>
            </div>
            <div class="layui-inline  ">
                <label class="layui-form-label">工具状态</label>
                <div class="layui-input-block">
                    <input  class="layui-input" name="toolStatus" type="text" value="<#if toolInst??>${toolInst.toolStatus!''}</#if>" >
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline  ">
                <label class="layui-form-label">名称</label>
                <div class="layui-input-block">
                    <input  class="layui-input" id="toolName"  name="toolName" type="text" value="<#if toolInst??>${toolInst.toolName!''}</#if>" >
                </div>
            </div>
            <div class="layui-inline  ">
                <label class="layui-form-label">英文名称</label>
                <div class="layui-input-block">
                    <input  class="layui-input" name="toolEname" type="text" value="<#if toolInst??>${toolInst.toolEname!''}</#if>" >
                </div>
            </div>
            <div class="layui-inline  ">
                <label class="layui-form-label">出厂日期</label>
                <div class="layui-input-block">
                    <input  class="layui-input" id="productionDate"  name="productionDate" type="text" value="<#if toolInst??>${toolInst.productionDate!''}</#if>" >
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline  ">
                <label class="layui-form-label">数量</label>
                <div class="layui-input-block">
                    <input  class="layui-input" name="stock" type="number" value="<#if toolInst??>${toolInst.stock!''}</#if>" >
                </div>
            </div>
            <div class="layui-inline  ">
                <label class="layui-form-label">原价</label>
                <div class="layui-input-block">
                    <input  class="layui-input" name="oraginalPrice" type="number" value="<#if toolInst??>${toolInst.oraginalPrice!''}</#if>" >
                </div>
            </div>
            <div class="layui-inline  ">
                <label class="layui-form-label">日租金</label>
                <div class="layui-input-block">
                    <input  class="layui-input" name="dailyRent" type="number" value="<#if toolInst??>${toolInst.dailyRent!''}</#if>" >
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline  ">
                <label class="layui-form-label">厂家</label>
                <div class="layui-input-block">
                    <input  class="layui-input" name="merchant" type="text" value="<#if toolInst??>${toolInst.merchant!''}</#if>" >
                </div>
            </div>
            <div class="layui-inline  ">
                <label class="layui-form-label">所属公司</label>
                <div class="layui-input-block">
                    <input  class="layui-input" name="company" type="text" value="<#if toolInst??>${toolInst.company!''}</#if>" >
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline  ">
                <label class="layui-form-label">联系人</label>
                <div class="layui-input-block">
                    <input  class="layui-input" name="linkman" type="text" value="<#if toolInst??>${toolInst.linkman!''}</#if>" >
                </div>
            </div>
            <div class="layui-inline  ">
                <label class="layui-form-label">联系电话</label>
                <div class="layui-input-block">
                    <input  class="layui-input" name="phoneNum" type="text" value="<#if toolInst??>${toolInst.phoneNum!''}</#if>" >
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline  ">
                <label class="layui-form-label">设备照片</label>
                <div class="layui-input-block">
                    <button type="button" class="layui-btn" id="picUpload">
                        <i class="layui-icon">&#xe67c;</i>上传图片
                    </button>
                    <input type="hidden" name="pic">
                </div>
            </div>
            <div class="layui-inline  ">
                <label class="layui-form-label">设备说明书</label>
                <div class="layui-input-block">
                    <button type="button" class="layui-btn" id="guideUpload">
                        <i class="layui-icon">&#xe67c;</i>上传文件
                    </button>
                </div>
            </div>
        </div>

        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">适用机型</label>
            <div class="layui-input-block">
                <textarea placeholder="请输入内容" class="layui-textarea" name="aircraftType" id="aircraftType"><#if toolInst??>${toolInst.aircraftType!''}</#if></textarea>
            </div>
        </div>

        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">主要技术参数</label>
            <div class="layui-input-block">
                <textarea placeholder="请输入内容" class="layui-textarea"  name="technicalParam" id="technicalParam"><#if toolInst??>${toolInst.technicalParam!''}</#if></textarea>
            </div>
        </div>

        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">手册章节</label>
            <div class="layui-input-block">
                <textarea placeholder="请输入内容" class="layui-textarea"  name="manualChapters" id="manualChapters"><#if toolInst??>${toolInst.manualChapters!''}</#if></textarea>
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">特别说明</label>
            <div class="layui-input-block">
                <textarea placeholder="请输入内容" class="layui-textarea" name="specialDescription" id="aircraftType"><#if toolInst??>${toolInst.specialDescription!''}</#if></textarea>
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
                <textarea placeholder="请输入内容" class="layui-textarea" name="remark" id="aircraftType"><#if toolInst??>${toolInst.remark!''}</#if></textarea>
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

    layui.use(['form','upload','laydate'], function () {
        var form = layui.form;
        var $ = layui.jquery;
        var upload = layui.upload;
        var laydate = layui.laydate;

        //常规用法
        laydate.render({
            elem: '#productionDate'
        });

        //执行实例
        var uploadInst = upload.render({
            elem: '#picUpload' //绑定元素
            ,url: '/upload/' //上传接口
            ,done: function(res){
                //上传完毕回调
            }
            ,error: function(){
                //请求异常回调
            }
        });

        //执行实例
        var uploadInst = upload.render({
            elem: '#guideUpload' //绑定元素
            ,url: '/upload/' //上传接口
            ,done: function(res){
                //上传完毕回调
            }
            ,error: function(){
                //请求异常回调
            }
        });


        var active = {
            save: function () {
                var data = form.val('mistake');


                var toolCode = $("#toolCode").val();
                if(toolCode==''||toolCode==null){
                    layer.alert("件号不能为空");
                    return false;
                }
                var toolName = $("#toolName").val();
                if(toolName==''||toolName==null){
                    layer.alert("名称不能为空");
                    return false;
                }

                var idx = layer.load(0);
                $.post("${mainDomain}/cloud/tool/edit", data , function (res) {
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
