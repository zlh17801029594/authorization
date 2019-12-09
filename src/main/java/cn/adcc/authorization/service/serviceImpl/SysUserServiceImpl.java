package cn.adcc.authorization.service.serviceImpl;

import cn.adcc.authorization.dao.SysGroupRepository;
import cn.adcc.authorization.dao.SysUserRepository;
import cn.adcc.authorization.model.SysGroup;
import cn.adcc.authorization.model.SysUser;
import cn.adcc.authorization.service.SysUserService;
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
        return userRepository.findByUsername(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public SysUser findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<SysUser> findAll() {
        return userRepository.findAll();
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
