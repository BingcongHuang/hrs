<#--<link rel="stylesheet" href="${mainDomain}/cloud/static/weui/weui.min.css">-->
<#--<link rel="stylesheet" href="${mainDomain}/cloud/static/weui/jquery-weui.css">-->
<#--<link rel="stylesheet" href="${mainDomain}/cloud/static/weui/front.css">-->
<#--<script>-->
<#--    var mainDomain = '${mainDomain}';-->
<#--</script>-->
<#if requestLimit?? && requestLimit == '1'>
    <!-- 查询被限制，要求登录 -->
    <div class="weui-flex">
        <div class="weui-flex__item"><div class="placeholder" style="text-align: center"><b>已达最大试用次数，请选择</b></div></div>
    </div>
    <br>
    <br>
    <div class="weui-flex">
        <div class="weui-flex__item">
            <div class="placeholder">
                <div class="button_sp_area"  style="text-align: center">
                    <a id="loginBtn" class="weui-btn weui-btn_mini weui-btn_primary">登录</a>
                </div>
            </div>
        </div>
        <div class="weui-flex__item">
            <div class="placeholder">
                <div class="button_sp_area"  style="text-align: center">
                    <a href="javascript:;" class="weui-btn weui-btn_mini weui-btn_primary open-popup" data-target="#full">注册</a>
                </div>
            </div>
        </div>

    </div>
<#else>
    <#if toolResult?? && (toolResult?size > 0)>
        <#list toolResult as tool>
            <div class="weui-panel">
                <div class="weui-panel__bd">
                    <div class="weui-media-box weui-media-box_text">
                        <h4 class="weui-media-box__title">${tool.toolCode!''}</h4>
                        <p class="weui-media-box__desc">${tool.toolName!''} | ${tool.toolEname!''}</p>
                        <ul class="weui-media-box__info">
                            <li class="weui-media-box__info__meta">适用机型</li>
                            <li class="weui-media-box__info__meta">${tool.aircraftType!''}</li>
                        </ul>
                        <ul class="weui-media-box__info">
                            <li class="weui-media-box__info__meta">出厂日期</li>
                            <li class="weui-media-box__info__meta">${tool.productionDate!''}</li>
                        </ul>
                        <ul class="weui-media-box__info">
                            <li class="weui-media-box__info__meta">生产厂家</li>
                            <li class="weui-media-box__info__meta">${tool.merchant!''}</li>
                        </ul>
                        <ul class="weui-media-box__info">
                            <li class="weui-media-box__info__meta">所属公司</li>
                            <li class="weui-media-box__info__meta">${tool.company!''}</li>
                        </ul>
                        <ul class="weui-media-box__info">
                            <li class="weui-media-box__info__meta">原  价</li>
                            <li class="weui-media-box__info__meta"><span style="color: red">￥${tool.oraginalPrice!''}</span></li>
                            <li class="weui-media-box__info__meta  weui-media-box__info__meta_extra">日租金</li>
                            <li class="weui-media-box__info__meta"><span style="color: red">￥${tool.dailyRent!''}</span></li>
                        </ul>
                        <ul class="weui-media-box__info">
                            <li class="weui-media-box__info__meta">联系人</li>
                            <#if userId??>
                                <li class="weui-media-box__info__meta">${tool.linkman!''}，${tool.phoneNum!''}</li>
                            <#else>
                                <li class="weui-media-box__info__meta">请<a href="${mainDomain}/cloud/user/login" style="color: #0BB20C">登录</a>后查看</li>
                            </#if>
                        </ul>
                    </div>
                </div>
            </div>
        </#list>
        <#if (totalCount > dataCount)>
            <div id="searchMoreLink" class="weui-panel">
                <div class="weui-panel__bd">
                    <div  style="text-align: center" class="weui-media-box weui-media-box_text">
                        <h4 class="weui-media-box__title">
                            <a style="cursor: pointer" id="searchMore">
                                查看更多
                            </a>
                        </h4>
                        <ul class="weui-media-box__info">
                            <li class="weui-media-box__info__meta">&nbsp</li>
                        </ul><ul class="weui-media-box__info">
                            <li class="weui-media-box__info__meta">&nbsp</li>
                        </ul>
                    </div>
                </div>
            </div>
        <#else>
            <div class="weui-panel">
                <div class="weui-panel__bd">
                    <div  style="text-align: center" class="weui-media-box weui-media-box_text">
                        <h4 class="weui-media-box__title">无更多数据</h4>
                        <ul class="weui-media-box__info">
                            <li class="weui-media-box__info__meta">&nbsp</li>
                        </ul><ul class="weui-media-box__info">
                            <li class="weui-media-box__info__meta">&nbsp</li>
                        </ul>
                    </div>
                </div>
            </div>
        </#if>
    <#elseif isApppend == '0'>
        <div class="weui-panel">
            <div class="weui-panel__bd">
                <div  style="text-align: center" class="weui-media-box weui-media-box_text">
                    <h4 class="weui-media-box__title">无匹配工具设备</h4>
                    <ul class="weui-media-box__info">
                        <li class="weui-media-box__info__meta">是否需要平台帮忙寻找，请联系xxxxxxx</li>
                    </ul><ul class="weui-media-box__info">
                        <li class="weui-media-box__info__meta">&nbsp</li>
                    </ul>
                </div>
            </div>
        </div>
    </#if>
</#if>

