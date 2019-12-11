package cn.adcc.authorization.service.serviceImpl;

import cn.adcc.authorization.dao.SysUserRepository;
import cn.adcc.authorization.model.SysUser;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Primary
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = userRepository.findByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        //6.也可以自己处理· enable,lock逻辑。lock表示锁定一段时间，时间过后自动恢复
        Set<GrantedAuthority> authoritySet = new HashSet<>();
        authoritySet.addAll(loadUserGroupAuthorities(sysUser));
        //逻辑上添加超级管理员
        if ("admin".equalsIgnoreCase(username)) {
            authoritySet.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        List<GrantedAuthority> authorityList = new ArrayList<>(authoritySet);
        return new User(
                sysUser.getUsername(),
                sysUser.getPassword(),
                sysUser.getEnable(),
                true,
                true,
                true,
                authorityList);
    }

    /*7.是否设计用户-权限表，给用户赋予单独权限*/
    private List<GrantedAuthority> loadUserAuthorities(SysUser sysUser) {
        return new ArrayList<>();
    }

    /*权限组权限*/
    private List<GrantedAuthority> loadUserGroupAuthorities(SysUser sysUser) {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        sysUser.getGroups()
                .stream()
                .filter(sysGroup -> sysGroup.getEnable())
                .flatMap(sysGroup -> sysGroup.getPermissions().stream())
                .filter(sysPermission -> sysPermission.getEnable())
                .forEach(sysPermission -> {
                    StringBuilder permission = new StringBuilder();
                    if (StringUtils.isNotBlank(sysPermission.getServicePrefix())) {
                        permission.append("/").append(sysPermission.getServicePrefix());
                    }
                    if (StringUtils.isNotBlank(sysPermission.getUri())) {
                        permission.append(sysPermission.getUri());
                    } else {
                        permission.append("/");
                    }
                    if (StringUtils.isNotBlank(sysPermission.getMethod())) {
                        //AuthorityUtils.commaSeparatedStringToAuthorityList()
                        //StringUtils.tokenizeToStringArray(authorityString, ",")
                        String[] methods = org.springframework.util.StringUtils.tokenizeToStringArray(sysPermission.getMethod(), ",");
                        String str = permission.toString();
                        for (int i = 0; i < methods.length; i++) {
                            StringBuilder permissionTemp = new StringBuilder(str);
                            permissionTemp.append("::").append(methods[i]);
                            authorityList.add(new SimpleGrantedAuthority(permissionTemp.toString()));
                        }
                    }
                });
        return authorityList;
    }
}
