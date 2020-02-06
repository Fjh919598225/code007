package com.java1234.service.impl;

import com.java1234.entity.User;
import com.java1234.entity.User;
import com.java1234.entity.User;
import com.java1234.repository.UserRepository;
import com.java1234.service.UserService;
import com.java1234.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * userService实现类
 * @Date 2020/1/26 13:28
 * @Author JianHui
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void save(User user)  {
        userRepository.save(user);
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getById(Integer id) {
        return userRepository.findOne(id);
    }

    @Override
    public List<User> list(User user, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {

        Pageable pageable=new PageRequest(page-1, pageSize, direction, properties);
        Page<User> pageUser = userRepository.findAll(new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(user!=null){
                    if (StringUtil.isNotEmpty(user.getUserName())){
                        predicate.getExpressions().add(cb.like(root.get("userName"),"%"+user.getUserName()+"%"));
                    }
                    if(StringUtil.isNotEmpty(user.getEmail())){
                        predicate.getExpressions().add(cb.like(root.get("email"), "%"+user.getEmail()+"%"));
                    }
                   
                }
                return predicate;
            }
        },pageable);
        return pageUser.getContent();
    }

    @Override
    public Long getTotal(User user) {
        long count = userRepository.count(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(user!=null){
                    if (StringUtil.isNotEmpty(user.getUserName())){
                        predicate.getExpressions().add(cb.like(root.get("userName"),"%"+user.getUserName()+"%"));
                    }
                    if(StringUtil.isNotEmpty(user.getEmail())){
                        predicate.getExpressions().add(cb.like(root.get("email"), "%"+user.getEmail()+"%"));
                    }
                }
                return predicate;
            }
        });
        return count;
    }

    @Override
    public void updateAllSignInfo() {
        userRepository.updateAllSignInfo();
    }


}
