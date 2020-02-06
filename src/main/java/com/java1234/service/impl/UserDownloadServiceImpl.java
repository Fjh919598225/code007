package com.java1234.service.impl;

import com.java1234.entity.Message;
import com.java1234.entity.UserDownload;
import com.java1234.repository.UserDownloadRepository;
import com.java1234.service.UserDownloadService;
import com.java1234.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * 用户下载实现类
 * @Date 2020/2/3 15:46
 * @Author JianHui
 */
@Service("UserDownloadService")
@Transactional
public class UserDownloadServiceImpl implements UserDownloadService {

    @Autowired
    private UserDownloadRepository userDownloadRepository;

    @Override
    public Integer getCountByUserIdAndArticleId(Integer userId, Integer articleId) {
        return userDownloadRepository.getCountByUserIdAndArticleId(userId,articleId);
    }

    @Override
    public void save(UserDownload userDownload) {
        userDownloadRepository.save(userDownload);
    }

    @Override
    public void deleteByArticleId(Integer articleId) {
        userDownloadRepository.deleteByArticleId(articleId);
    }

    @Override
    public List<UserDownload> list(UserDownload userDownload, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable=new PageRequest(page-1, pageSize, direction, properties);
        Page<UserDownload> pageMessage = userDownloadRepository.findAll(new Specification<UserDownload>() {

            @Override
            public Predicate toPredicate(Root<UserDownload> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(userDownload!=null){
                    if(userDownload.getUser()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("user").get("id"), userDownload.getUser().getId()));
                    }
                }
                return predicate;
            }
        },pageable);
        return pageMessage.getContent();
    }

    @Override
    public Long getTotal(UserDownload userDownload) {
        long count = userDownloadRepository.count(new Specification<UserDownload>() {
            @Override
            public Predicate toPredicate(Root<UserDownload> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (userDownload != null) {
                    if (userDownload.getUser()!=null && StringUtil.isNotEmpty(userDownload.getUser().getUserName())){
                        predicate.getExpressions().add(cb.equal(root.get("user").get("id"), userDownload.getUser().getId()));
                    }
                }
                return predicate;
            }
        });
        return count;
    }
}
