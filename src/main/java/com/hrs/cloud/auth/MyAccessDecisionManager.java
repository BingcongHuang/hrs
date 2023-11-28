package com.hrs.cloud.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;


@Component
public class MyAccessDecisionManager implements AccessDecisionManager {


	private static final Logger logger = LogManager
			.getLogger();

	@Override
	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {

		if (configAttributes == null) {
			return;
		}
		Iterator<ConfigAttribute> ite = configAttributes.iterator();
		while (ite.hasNext()) {
			ConfigAttribute ca = ite.next();
			
			String needRole = ((SecurityConfig) ca).getAttribute();
			if(logger.isInfoEnabled()){
				logger.info("needRole:"+needRole);
			}

			for (GrantedAuthority ga : authentication.getAuthorities()) {
				if(logger.isInfoEnabled()){
					logger.info("当前:"+ga.getAuthority());
				}
				if (needRole.equals(ga.getAuthority())) { // ga is user's role.
					return;
				}
			}
		}
		if(logger.isInfoEnabled()){
			logger.info("没有找到匹配");
		}
		throw new AccessDeniedException("no right");
	}

	@Override
	public boolean supports(ConfigAttribute arg0) {
		return true;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}
