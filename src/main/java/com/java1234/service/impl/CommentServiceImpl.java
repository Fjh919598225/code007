package com.java1234.service.impl;

import com.java1234.entity.Article;
import com.java1234.entity.Comment;
import com.java1234.entity.Comment;
import com.java1234.entity.Link;
import com.java1234.repository.CommentRepository;
import com.java1234.repository.LinkRepository;
import com.java1234.service.CommentService;
import com.java1234.service.LinkService;
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
 * 评论service实现类
 * @Date 2020/1/23 14:45
 * @Author JianHui
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public List<Comment> list(Comment comment, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable=new PageRequest(page-1, pageSize, direction, properties);
        Page<Comment> pageComment = commentRepository.findAll(new Specification<Comment>() {

            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(comment!=null){
                    if (comment.getState()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("state"),comment.getState()));
                    }
                    if(comment.getArticle()!=null && comment.getArticle().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("article").get("id"), comment.getArticle().getId()));
                    }
                    if(comment.getArticle()!=null && comment.getArticle().getUser()!=null && comment.getArticle().getUser().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("article").get("user").get("id"), comment.getArticle().getUser().getId()));
                    }
                }
                return predicate;
            }
        },pageable);
        return pageComment.getContent();
    }

    @Override
    public Long getTotal(Comment comment) {
        long count = commentRepository.count(new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(comment!=null){
                    if (comment.getState()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("state"),comment.getState()));
                    }
                    if(comment.getArticle()!=null&&comment.getArticle().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("article").get("id"), comment.getArticle().getId()));
                    }
                    if(comment.getArticle()!=null && comment.getArticle().getUser()!=null && comment.getArticle().getUser().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("article").get("user").get("id"), comment.getArticle().getUser().getId()));
                    }
                }
                return predicate;
            }
        });
        return count;
    }

    @Override
    public void delete(Integer id) {
        commentRepository.delete(id);
    }

    @Override
    public Comment get(Integer id) {
        return commentRepository.findOne(id);
    }

    @Override
    public void deleteByArticleId(Integer articleId) {
        commentRepository.deleteByArticleId(articleId);
    }
}
