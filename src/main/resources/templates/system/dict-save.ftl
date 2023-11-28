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
</style>
<script>
    var mainDomain = '${mainDomain}';
</script>
<body>
<div>
    <div class="layui-card-header title">
        字典配置
    </div>
    <div class="layui-form" style="padding-top: 20px">
        <input id="i_id" type="hidden" value="${dt.id!''}">
        <div class="layui-form-item">
            <label for="i_menu_name" class="layui-form-label" style="width: 120px">
                <span class="x-red">*</span>key：
            </label>
            <div class="layui-input-inline" style="width: 300px;">
                <input type="text" class="layui-input" id="i_commonkey" value="${dt.commonkey!''}">
            </div>
        </div>

        <div class="layui-form-item">
            <label for="i_menu_icon" class="layui-form-label" style="width: 120px">
                <span class="x-red">*</span>字典标签：
            </label>
            <div class="layui-input-inline" style="width: 300px;">
                <input type="text" class="layui-input" id="i_label" value="${dt.label!''}">
            </div>
        </div>
        <div class="layui-form-item">
            <label for="i_menu_icon" class="layui-form-label" style="width: 120px">
                <span class="x-red">*</span>value：
            </label>
            <div class="layui-input-inline" style="width: 300px;">
                <input type="text" class="layui-input" id="i_lvalue" value="${dt.lvalue!''}">
            </div>
        </div>
        <div class="layui-form-item">
            <label for="i_menu_icon" class="layui-form-label" style="width: 120px">
                字典描述：
            </label>
            <div class="layui-input-inline" style="width: 300px;">
                <input type="text" class="layui-input" id="i_remark" value="${dt.remark!''}">
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
    var $;
    layui.use('form', function () {
        var form = layui.form;
        $ = layui.jquery;
        var active = {
            save: function () {
                var id = $('#i_id').val();
                var commonkey = $('#i_commonkey').val();
                var label = $('#i_label').val();
                var lvalue = $('#i_lvalue').val();
                var remark = $('#i_remark').val();
                if (commonkey == '' || commonkey == null) {
                    layer.alert("请输入字典key! ");
                    return false;
                }
                if (label == '' || label == null) {
                    layer.alert("请填写字典标签! ");
                    return false;
                }
                if (lvalue =='' || lvalue == null) {
                    layer.alert("请填写字典value! ");
                    return false;
                }
                var idx = layer.load(0);
                $.post("${mainDomain}/cloud/dict/saveDict", {
                    "id": id,
                    "commonkey": commonkey,
                    "label": label,
                    "lvalue": lvalue,
                    "remark": remark
                }, function (res) {
                    layer.close(idx);
                    if (res.code == 0) {
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

    function priceFormat(_this){
        var p = $(_this).val();
        //非数字和小数点去掉
        p=p.replace(/[^0123456789.]/,"")//之前没注意写错了！！！！
        //防止无输入无限个“.”
        p=p.replace(/\.+/ ,".")
        //在不是“0.”开头的字符进行修改：“01”=>1
        if(p.charAt(0)=="0"&&p.charAt(1)!="."&&p.length>=2){
            p=p.replace(/0/ ,"")
        }
        //获取第一个小数点的索引值
        var index=p.indexOf('.')
        //获取最后一个小数点的索引值
        var lastIndex=p.lastIndexOf('.')
        //判断小数点是不是开头，如果是开头，则去除
        if(index==0){
            p=p.replace(/\./ ,"")
        }
        //只允许小数点后面有2位字符
        if(index>=1){
            p=p.substring(0,index+3)
        }
        //防止小数点后面又出现小数点
        if(index!=lastIndex){
            p=p.substring(0,index+2)
        }
        $(_this).val(p);
    }


</script>
</body>
