package com.hrs.cloud.auth;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hrs.cloud.core.utils.DateUtil;
import com.hrs.cloud.dao.SysRoleMapper;
import com.hrs.cloud.dao.SysUserMapper;
import com.hrs.cloud.dao.SysUserRoleRelationMapper;
import com.hrs.cloud.entity.SysRole;
import com.hrs.cloud.entity.SysUser;
import com.hrs.cloud.entity.SysUserRoleRelation;
import com.hrs.cloud.entity.vo.UserVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


@Service("MyuserDetailsService")
public class MyUserDetailService implements UserDetailsService {

	private static final Logger logger = LogManager
			.getLogger();

	@Resource
	private SysUserMapper sysUserMapper;
	@Resource
	private SysRoleMapper sysRoleMapper;
	@Resource
	private SysUserRoleRelationMapper sysUserRoleRelationMapper;
//	@Autowired
//	private SysPermissionMapper sysPermissionMapper;
	private static String CUSTOMER = "PATIENT";

	public MyUserDetailService() {

	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();

		String password = null;
		List<SysRole> roles = null;

		SysUser user = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("USER_NAME", username));
		if (null == user) {

			String message = "用户不存在";
			throw new UsernameNotFoundException(message);

		} else {
			//是否锁定
			boolean locked = !user.getLocked();
			//是否删除
			boolean enable = !user.getIsDeleted();
			//获取用户密码
			password = user.getUserPassword();

			List<SysUserRoleRelation> sysUserRoleRelations = sysUserRoleRelationMapper.selectList(
					new QueryWrapper<SysUserRoleRelation>()
//							.eq("USERID", user.getId())
//							.or()
							.eq("USERID", user.getUserId())
			);

			List<Long> roleIds = new ArrayList<Long>();
			if(CollectionUtils.isNotEmpty(sysUserRoleRelations)) {
				roleIds = sysUserRoleRelations.stream().map(SysUserRoleRelation::getRoleid).collect(Collectors.toList());
			}

			List<SysRole> sysRoles = sysRoleMapper.selectList(new QueryWrapper<SysRole>().in("ID", roleIds));

			Iterator<SysRole> sysroles = sysRoles.iterator();
			Integer customerFlag = 0;
			while (sysroles.hasNext()) {
				String role = sysroles.next().getRole();
				if (logger.isInfoEnabled()) {
					logger.info("username:" + username + "-role:" + role);
				}
				SimpleGrantedAuthority grantedAuthorityImpl = new SimpleGrantedAuthority(
						role);
				auths.add(grantedAuthorityImpl);
				if(CUSTOMER.equals(role)) {
					customerFlag = 1;
				}
			}


			UserVO userVO = new UserVO(username, password, enable, true, true, locked, auths);
			userVO.setUserId(user.getUserId());
			userVO.setRealName(user.getUserRealname());
			userVO.setUserName(user.getUserName());
			userVO.setTelPhone(user.getUserTelphone());
			userVO.setRoles(roles);
			if(null != user.getBirthday()){
				userVO.setBirthday(DateUtil.localDateToString(user.getBirthday()));
			}
			userVO.setCustomerFlag(customerFlag);
			return userVO;
		}
	}
}