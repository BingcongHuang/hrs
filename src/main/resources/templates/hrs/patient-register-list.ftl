<#if rgsRcords??>
    <#list rgsRcords as rs>
        <div class="weui-panel__bd">
            <div class="weui-media-box weui-media-box_text">
                <h4 class="weui-media-box__title">${rs.doctorName} 【<#if rs.doctorLevel == 2>专家号<#else>普通号</#if>】【${rs.registerCharges}元】</h4>
                <p class="weui-media-box__desc">科室：${rs.dptInfo!''}</p>
                <ul class="weui-media-box__info">
                    <li class="weui-media-box__info__meta">${rs.visitingDateStr} <#if rs.timeFlag == 1>上午<#else>下午</#if></li>
                    <li class="weui-media-box__info__meta">
                        <#if rs.status == 0>待使用<#elseif rs.status == 1>已使用<#else>已取消</#if>
                    </li>
                    <#if rs.status == 0>
                        <li class="weui-media-box__info__meta weui-media-box__info__meta_extra">
                            <a href="javascript:;" data-id = '${rs.id}' class="rgsCnl">取消</a>
                        </li>
                    </#if>
                </ul>
            </div>
        </div>
    </#list>
</#if>

