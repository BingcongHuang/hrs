package com.hrs.cloud.auth;


import com.hrs.cloud.constants.RedisRoleCache;
import com.hrs.cloud.entity.vo.RolePermissionVO;
import com.hrs.cloud.helper.RedisHelper;
import com.hrs.cloud.helper.json.JsonHelper;
import com.hrs.cloud.dao.SysPermissionMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class MyInvocationSecurityMetadataSource implements
		FilterInvocationSecurityMetadataSource {

	private final static AntPathMatcher urlMatcher = new AntPathMatcher();

	private static final Logger logger = LogManager
			.getLogger();

	private static Map<String, Collection<ConfigAttribute>> resourceMap=new ConcurrentHashMap<>();


    private SysPermissionMapper sysPermissionMapper;

    private RedisHelper redisUtils;

	public MyInvocationSecurityMetadataSource(SysPermissionMapper sysPermissionMapper,RedisHelper redisUtils) {
		this.sysPermissionMapper=sysPermissionMapper;
		this.redisUtils=redisUtils;
		try {
			loadResourceDefine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadResourceDefine() throws Exception {
		List<RolePermissionVO> permissions = sysPermissionMapper.selectRolePermission();

		Map<String,List<RolePermissionVO>> rolePermissionMap = permissions.stream().collect(Collectors.groupingBy(x->x.getRole()));

		if(logger.isInfoEnabled()){
			logger.info("rolePermissionMap:"+ JsonHelper.toJson(rolePermissionMap));
		}
		redisUtils.set(RedisRoleCache.ROLE_PERMISSION_RELATION.getKey(),JsonHelper.toJson(rolePermissionMap));


		permissions.stream().forEach(x->{
			ConfigAttribute ca = new SecurityConfig(x.getRole());
			String url = x.getPermission();
			if(StringUtils.isNotBlank(url)){
				if(resourceMap.containsKey(url)){
					Collection<ConfigAttribute> value = resourceMap.get(url);
					value.add(ca);
					resourceMap.put(url, value);
				}else{
					Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
					atts.add(ca);
					resourceMap.put(url, atts);
				}
			}
		});

		if(logger.isInfoEnabled()){
			logger.info("resourceMap:"+ JsonHelper.toJson(resourceMap));
		}
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {

		String url = ((FilterInvocation) object).getRequestUrl();

		int firstQuestionMarkIndex = url.indexOf("?");
		if (firstQuestionMarkIndex != -1) {
			url = url.substring(0, firstQuestionMarkIndex);
		}

		Iterator<String> ite = resourceMap.keySet().iterator();
		while (ite.hasNext()) {
			String resURL = ite.next();
			if (urlMatcher.match(resURL, url)) {
				return resourceMap.get(resURL);
			}
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}
