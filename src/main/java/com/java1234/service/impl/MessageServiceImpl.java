package com.java1234.service.impl;

import com.java1234.entity.Message;
import com.java1234.entity.Message;
import com.java1234.repository.MessageRepository;
import com.java1234.service.MessageService;
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
 * @Date 2020/2/5 17:21
 * @Author JianHui
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Integer getCountByUserId(Integer userId) {
        return messageRepository.getCountByUserId(userId);
    }

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }

    @Override
    public void updateState(Integer userId) {
        messageRepository.updateState(userId);
    }

    @Override
    public List<Message> list(Message message, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable=new PageRequest(page-1, pageSize, direction, properties);
        Page<Message> pageMessage = messageRepository.findAll(new Specification<Message>() {

            @Override
            public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(message!=null){
                    if(message.getUser()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("user").get("id"), message.getUser().getId()));
                    }
                }
                return predicate;
            }
        },pageable);
        return pageMessage.getContent();
    }

    @Override
    public Long getTotal(Message message) {
        long count = messageRepository.count(new Specification<Message>() {
            @Override
            public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (message != null) {
                    if (message.getUser()!=null && StringUtil.isNotEmpty(message.getUser().getUserName())){
                        predicate.getExpressions().add(cb.equal(root.get("user").get("id"), message.getUser().getId()));
                    }
                }
                return predicate;
            }
        });
        return count;
    }
}
