package com.wukong.logisticsproject.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
public class ShiroConfig {
	/*创建ShiroFilterFactoryBean*/
	@Bean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

		//设置安全管理器
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		//添加Shiro内置过滤器
		/**
		 * Shiro内置过滤器，可以实现权限相关的拦截器
		 * 常用的过滤器：
		 *      anon：无需认证(登录)可以访问
		 *      authc：必须认证才可以访问
		 *      user：如果使用rememberMe的功能可以直接访问
		 *      perms：该资源必须得到资源权限才可以访问
		 *      role：该资源必须得到角色权限才可以访问
		 */
		Map<String, String> filterMap = new LinkedHashMap<String, String>();
		//修改调整的登录页面
		shiroFilterFactoryBean.setLoginUrl("/index");

		filterMap.put("/getVerifyCode", "anon");//验证码
//		filterMap.put("/", "anon");

		filterMap.put("/static/**", "anon");
		filterMap.put("/js/**", "anon");
		filterMap.put("/fonts/**", "anon");
		filterMap.put("/img/**", "anon");
		filterMap.put("/favicon.ico", "anon");
		filterMap.put("/css/**", "anon");
		filterMap.put("/bower_components/**", "anon");

		filterMap.put("/login", "anon");
		filterMap.put("/registered", "anon");
		filterMap.put("/region", "anon");
		filterMap.put("/logout", "logout");
		filterMap.put("/**", "authc");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
		shiroFilterFactoryBean.setUnauthorizedUrl("/error");//未授权
		return shiroFilterFactoryBean;
	}

	/*创建DefaultWebSecurity*/
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") ShiroRealm shiroRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		//管理realm
		securityManager.setRealm(shiroRealm);
		return securityManager;
	}

	/*创建Realm*/
	@Bean(name = "userRealm")
	public ShiroRealm getRealm() {
		return new ShiroRealm();
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager){
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}
	/**
	 * 页面上使用shiro标签
	 * @return
	 */
	@Bean(name = "shiroDialect")
	public ShiroDialect shiroDialect(){
		return new ShiroDialect();
	}

}
