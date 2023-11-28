package com.hrs.cloud.boot.config;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

@Component
public class PropertiesUtils implements EmbeddedValueResolverAware {
	
	private static StringValueResolver stringValueResolver;
	
	@Override
	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		stringValueResolver = resolver;
	}
	
	public static String getPropertiesValue(String key){
		key = "${"+key+"}";
		return stringValueResolver.resolveStringValue(key);
	}
	
}
