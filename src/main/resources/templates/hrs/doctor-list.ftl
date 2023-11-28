<#if doctors??>
    <#list doctors as dc>
        <div class="weui-panel__bd">
            <div class="weui-media-box weui-media-box_text">
                <h4 class="weui-media-box__title">${dc.doctorName} 【<#if dc.doctorLevel == 2>专家号<#else>普通号</#if>】【${dc.registerCharges}元】</h4>
                <p class="weui-media-box__desc">${dc.doctorIntroduction!''}</p>
                <#if dc.moningFlag == 1>
                    <ul class="weui-media-box__info">
                        <li class="weui-media-box__info__meta">上午</li>
                        <li class="weui-media-box__info__meta">号源剩余：<span style="color: blue">${dc.moningNums}</span>个</li>
                        <li class="weui-media-box__info__meta weui-media-box__info__meta_extra">
                            <a href="javascript:;" style="color: blue" class="rgsLink" data-time = '1' data-docid = '${dc.doctorId}' data-charges = '${dc.registerCharges}'>预约</a>
                        </li>
                    </ul>
                <#elseif dc.moningFlag == 2>
                    <ul class="weui-media-box__info">
                        <li class="weui-media-box__info__meta">上午</li>
                        <li class="weui-media-box__info__meta">号源剩余：<span style="color: blue">0</span>个</li>
                        <li class="weui-media-box__info__meta weui-media-box__info__meta_extra">
                            <span style="color: red">停诊</span>
                        </li>
                    </ul>
                </#if>
                <#if dc.afternoonFlag == 1>
                    <ul class="weui-media-box__info">
                        <li class="weui-media-box__info__meta">下午</li>
                        <li class="weui-media-box__info__meta">号源剩余：<span style="color: blue">${dc.afternoonNums}</span>个</li>
                        <li class="weui-media-box__info__meta weui-media-box__info__meta_extra">
                            <a href="javascript:;" style="color: blue" class="rgsLink" data-time = '2' data-docid = '${dc.doctorId}' data-charges = '${dc.registerCharges}'>预约</a>
                        </li>
                    </ul>
                <#elseif dc.afternoonFlag == 2>
                    <ul class="weui-media-box__info">
                        <li class="weui-media-box__info__meta">下午</li>
                        <li class="weui-media-box__info__meta">号源剩余：<span style="color: blue">0</span>个</li>
                        <li class="weui-media-box__info__meta weui-media-box__info__meta_extra">
                            <span style="color: red">停诊</span>
                        </li>
                    </ul>
                </#if>
            </div>
        </div>
    </#list>
</#if>

