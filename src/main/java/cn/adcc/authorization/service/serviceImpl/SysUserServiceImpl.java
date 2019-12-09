package cn.adcc.authorization.service.serviceImpl;

import cn.adcc.authorization.dao.SysGroupRepository;
import cn.adcc.authorization.dao.SysUserRepository;
import cn.adcc.authorization.model.SysGroup;
import cn.adcc.authorization.model.SysUser;
import cn.adcc.authorization.service.SysUserService;
import cn.adcc.authorization.utils.BeanPropertiesCopyUtils;
import cn.adcc.authorization.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private SysGroupRepository groupRepository;

    @Override
    public SysUser findByUsername(String username) {
        SysUser sysUser = userRepository.findByUsername(username);
        if (sysUser != null && sysUser.getEnable()) {
            SysUser userDto = new SysUser();
            BeanUtils.copyProperties(sysUser, userDto, "groups");
            userDto.getGroups()
                    .stream()
                    .filter(sysGroup -> sysGroup.getEnable())
                    .forEach(sysGroup -> {
                        SysGroup sysGroup1 = new SysGroup();
                        sysGroup1.setId(sysGroup.getId());
                        sysGroup1.setName(sysGroup.getName());
                        sysGroup1.setDescription(sysGroup.getDescription());
                        userDto.getGroups().add(sysGroup1);
                    });
            return userDto;
        }
        return null;
        //return userRepository.findByUsername(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        String username = SecurityUtils.getUsername();
        SysUser sysUser = userRepository.findByUsername(username);
        if (sysUser != null) {
            if(passwordEncoder.matches(oldPassword, sysUser.getPassword())){
                sysUser.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(sysUser);
                //密码更新成功， 返回json, 前端success()回调处理，跳转到首页 [考虑：或者销毁当前用户token，跳转到登录页]
            }
            //旧密码错误，返回json, 前端异步ajax请求，提示错误消息，不重置表单(用户友好)
        }
    }

    @Override
    public SysUser saveUser(SysUser sysUser) {
        userRepository.findById(sysUser.getId()).ifPresent(sysUser1 -> {
            BeanPropertiesCopyUtils.copyNotNull(sysUser1, sysUser, "password");
            userRepository.save(sysUser1);
        });
        return null;
    }

    @Override
    public SysUser updatePassword(SysUser sysUser) {
        userRepository.findById(sysUser.getId()).ifPresent(sysUser1 -> {
            sysUser1.setPassword(passwordEncoder.encode(sysUser.getPassword()));
            userRepository.save(sysUser1);
        });
        return null;
    }

    @Override
    public void lockUser(SysUser sysUser) {
        Long id = sysUser.getId();
        if (id != null) {
            userRepository.findById(id).ifPresent(sysUser1 -> {
                sysUser1.setAccountNonLocked(sysUser.getAccountNonLocked());
                //测试service业务层异常
                //sysUser1.setAccountNonLocked(null);
                userRepository.save(sysUser1);
            });
        }
    }

    @Override
    public void disableUser(SysUser sysUser) {
        Long id = sysUser.getId();
        if (id != null) {
            userRepository.findById(id).ifPresent(sysUser1 -> {
                sysUser1.setEnable(sysUser.getEnable());
                userRepository.save(sysUser1);
            });
        }
    }

    @Override
    public SysUser findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<SysUser> findAll() {
        List<SysUser> sysUserList = userRepository.findAll();
        return sysUserList.stream().map(sysUser -> {
            SysUser sysUser1 = new SysUser();
            BeanUtils.copyProperties(sysUser, sysUser1, "groups");
            List<SysGroup> sysGroupList = sysUser.getGroups().stream().map(sysGroup -> {
                SysGroup sysGroup1 = new SysGroup();
                BeanUtils.copyProperties(sysGroup, sysGroup1, "permissions");
                return sysGroup1;
            }).collect(Collectors.toList());
            sysUser1.setGroups(sysGroupList);
            return sysUser1;
        }).collect(Collectors.toList());
        //return userRepository.findAll();
    }

    @Override
    public Page<SysUser> findAll(PageRequest pageRequest) {
        return null;
    }

    @Override
    public SysUser save(SysUser user) {
        SysUser sysUser1 = new SysUser();
        //BeanUtils.copyProperties(user, sysUser1, "groups", "createTime", "updateTime");
        BeanUtils.copyProperties(user, sysUser1);
        System.out.println(user);
        System.out.println(sysUser1);
        if (user.getGroups() != null) {
            List<Long> ids = user.getGroups().stream().map(group -> group.getId()).collect(Collectors.toList());
            System.out.println(ids);
            List<SysGroup> groups = new ArrayList<>();
            ids.stream().forEach(id -> {
                groupRepository.findById(id).ifPresent(g -> {
                    groups.add(g);
                });
                //groups.add(groupRepository.findById(id).get());
            });
            sysUser1.setGroups(groups);
        }
        if (!StringUtils.isEmpty(user.getPassword()))
            sysUser1.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(sysUser1);
        //userRepository.save(user);
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void delete(List<Long> ids) {
        for (Long id : ids) {
            userRepository.deleteById(id);
        }
    }
}
