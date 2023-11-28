layui.config({
    base: mainDomain+'/cloud/static/layui/'
}).extend({
    treeTable: 'treeTable'
});

layui.use(['laypage','treeTable','table', 'form','laydate'], function () {
    ManifestArrived.treeTable = layui.treeTable;
    ManifestArrived.$ = layui.jquery;
    ManifestArrived.page = layui.laypage;
    var $= layui.jquery;
    var laydate = layui.laydate;
    //日期时间范围
    laydate.render({
        elem: '#flightDate'
        ,type: 'date'
    });

    //方法级渲染
    ManifestArrived.treeTable.render({
        elem: ManifestArrived.id,
        toolbar: true,
        url:'/cloud/manifestArrived/list',
        where: ManifestArrived.formParams(),
        loading: true,
        cellMinWidth: 80,
        request: {pidName: 'pid'},
        tree: {
            iconIndex: 1,
            haveChildName: 'haveChild',  // 自定义标识是否还有子节点的字段名称,
            isPidData: false,        // 是否是id、pid形式数据
            idName: 'id',  // id字段名称
            pidName: 'mawbno'     // pid字段名称
        }
        ,  page: true
        , limit: 10   //默认十条数据一页
        , limits: [10, 20, 30, 50]    //数据分页条
        , cols: ManifestArrived.initColumn(),
        reqData: function (data, callback) {  // 懒加载也可以用url方式，这里用reqData方式演示
                var url = '/cloud/manifestArrived/list';
                if(data){
                   url = '/cloud/manifestArrived/hawbList?mawbno='+data.mawbno;

                }
                $.get(url, function (res) {
                    callback(res.data);
                });
        }
    });

    //总页数大于页码总数
    ManifestArrived.page.render({
        elem: 'page'
        ,count: 0       //数据总数
        ,jump: function(obj){
            console.log(obj)
        }
    });

});



/**
 * 管理初始化
 */
var ManifestArrived = {
    id: "#arrivedInfoList",	//表格id
    seItem: null,		//选中的条目
    treeTable: null,
    $: null,
    layerIndex: -1,
    page:null
};

/**
 * 初始化表格的列
 */
ManifestArrived.initColumn = function () {
    return [[
        {type:'radio'},
        {title: '运单'  ,width:'150px',field: 'mawbno',align: 'center',singleLine: false,unresize:true,templet: function (row) {
            if(row.type ===0){
                return row.mawbno+"_"+row.hawbno;
            }else{
                return row.mawbno;
            }
            }},
        {title: '舱单件数',field: 'mawbno',align: 'center',singleLine: false,unresize:true},
        {title: '舱单重量',field: 'mawbno',align: 'center',singleLine: false,unresize:true},
        {title: '理货件数',field: 'mawbno',align: 'center',singleLine: false,unresize:true},
        {title: '理货重量',field: 'mawbno',align: 'center',singleLine: false,unresize:true},
        {title: '卸货时间',field: 'id',align: 'center',singleLine: false,unresize:true},
        {title: '状态'   ,field: 'id',align: 'center',singleLine: false,unresize:true},
        {title: '回执信息',field: 'id',align: 'center',singleLine: false,unresize:true},
        {title: '操作'   ,field: 'operation',width:190,fixed:'center',singleLine: false,unresize:true,
            templet: function (row) {
                var srt = '';
                    srt +=' <button  onclick="ManifestArrived.edit('+row.id+')" type="button" class="layui-btn layui-btn-xs layui-btn-radius list-op op">回执明细</button>';
                    srt +=' <button  onclick="ManifestArrived.edit('+row.id+')" type="button" class="layui-btn layui-btn-xs layui-btn-radius list-op op">编辑主单</button>';
                    srt +=' <button  onclick="ManifestArrived.edit('+row.id+')" type="button" class="layui-btn layui-btn-xs layui-btn-radius list-op op">发送运抵报</button>';
                    srt +=' <button  onclick="ManifestArrived.edit('+row.id+')" type="button" class="layui-btn layui-btn-xs layui-btn-radius list-op op">发送运抵删除报</button>';
                return srt;
            }
        }
    ]];
};

function changeDateFormat(val) {
    if (val) {
        var mills = new Date(Date.parse(val.replace(/-/g, "/"))).getTime();//获得毫秒数
        var date = new Date(mills);
        var year = date.getFullYear();
        var month = date.getMonth() + 1;//月份是从0开始的
        var day = date.getDate();
        return year + '-' + (month < 10 ? '0' + month : month) + '-' + (day < 10 ? '0' + day : day);
    } else {
        return "";
    }
}

ManifestArrived.add = function (id) {
    alert("add"+id)
}


ManifestArrived.edit = function (id) {
    alert("edit"+id)
}




ManifestArrived.formParams = function(){
    debugger
    var queryData = {};
    queryData['mawbNo'] = ManifestArrived.$("#mawbNo").val();
    queryData['flightNo'] = ManifestArrived.$("#flightNo").val();
    queryData['flightDate'] = ManifestArrived.$("#flightDate").val();
    return queryData;
}



ManifestArrived.search= function(){
    debugger
    ManifestArrived.treeTable.reload('arrivedReload', {
        page: {
            curr: 1 //重新从第 1 页开始
        }
        , where: ManifestArrived.formParams()
        , loading: true
    });
}


ManifestArrived.reset = function () {
    $("#flightNo").val("");
    $("#flightDate").val("");
    $("#mawbNo").val("");
}



