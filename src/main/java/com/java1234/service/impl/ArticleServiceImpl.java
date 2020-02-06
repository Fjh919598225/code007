package com.java1234.service.impl;

import com.java1234.entity.Article;
import com.java1234.repository.ArticleRepository;
import com.java1234.service.ArticleService;
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
 * 资源service实现类
 * @Date 2020/1/23 12:17
 * @Author JianHui
 */
@Service("articleService")
public class ArticleServiceImpl implements ArticleService{

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<Article> list(Article s_article, Integer page, Integer pageSize, Sort.Direction direction,
                              String... properties) {
        Pageable pageable=new PageRequest(page-1, pageSize, direction, properties);
        Page<Article> pageArticle = articleRepository.findAll(new Specification<Article>() {

            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(s_article!=null){
                    if (s_article.getName()!=null){
                        predicate.getExpressions().add(cb.like(root.get("name"),"%"+s_article.getName()+"%"));
                    }
                    if(s_article.getState()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("state"), s_article.getState()));
                    }
                    if (s_article.isHot()){
                        predicate.getExpressions().add(cb.equal(root.get("isHot"), 1));
                    }
                    if(s_article.getArcType()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("arcType").get("id"), s_article.getArcType().getId()));
                    }
                    if (!s_article.isUseful()){
                        predicate.getExpressions().add(cb.equal(root.get("isUseful"),false));
                    }
                    if (s_article.getUser()!=null && s_article.getUser().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("user").get("id"), s_article.getUser().getId()));
                    }
                    if (s_article.getUser()!=null && StringUtil.isNotEmpty(s_article.getUser().getUserName())){
                        predicate.getExpressions().add(cb.like(root.get("user").get("userName"), "%"+s_article.getUser().getUserName()+"%"));
                    }
                }
                return predicate;
            }
        },pageable);
        return pageArticle.getContent();
    }

    @Override
    public Long getTotal(Article s_article) {
        long count = articleRepository.count(new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (s_article != null) {
                    if (s_article.getName()!=null){
                        predicate.getExpressions().add(cb.like(root.get("name"),"%"+s_article.getName()+"%"));
                    }
                    if (s_article.getState() != null) {
                        predicate.getExpressions().add(cb.equal(root.get("state"), s_article.getState()));
                    }
                    if (s_article.isHot()){
                        predicate.getExpressions().add(cb.equal(root.get("isHot"), 1));
                    }
                    if(s_article.getArcType()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("arcType").get("id"), s_article.getArcType().getId()));
                    }
                    if (!s_article.isUseful()){
                        predicate.getExpressions().add(cb.equal(root.get("isUseful"),false));
                    }
                    if (s_article.getUser()!=null && s_article.getUser().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("user").get("id"), s_article.getUser().getId()));
                    }
                    if (s_article.getUser()!=null && StringUtil.isNotEmpty(s_article.getUser().getUserName())){
                        predicate.getExpressions().add(cb.like(root.get("user").get("userName"), "%"+s_article.getUser().getUserName()+"%"));
                    }
                }
                return predicate;
            }
        });
        return count;
    }

    @Override
    public Article get(Integer id) {
        return articleRepository.findOne(id);
    }

    @Override
    public void save(Article article) {
        this.articleRepository.save(article);
    }

    @Override
    public void delete(Integer id) {
        this.articleRepository.delete(id);
    }

    @Override
    public List<Article> listAll() {
        return articleRepository.findAll();
    }

    @Override
    public List<Article> getArticleNoIndex() {
        return articleRepository.getArticleNoIndex();
    }

    @Override
    public void updateArticleIndex(Integer id) {
        articleRepository.updateArticleIndex(id);
    }


}
