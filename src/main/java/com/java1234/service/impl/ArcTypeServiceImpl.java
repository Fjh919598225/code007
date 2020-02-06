package com.java1234.service.impl;

import com.java1234.entity.ArcType;
import com.java1234.repository.ArcTypeRepository;
import com.java1234.service.ArcTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Date 2020/1/22 17:29
 * @Author JianHui
 */
@Service("arcTypeService")
public class ArcTypeServiceImpl implements ArcTypeService {

    @Autowired
    private ArcTypeRepository arcTypeRepository;

    @Override
    public List<ArcType> listAll(Sort.Direction direction, String... properties) {
        Sort sort = new Sort(direction,properties);
        List<ArcType> arcTypeList = arcTypeRepository.findAll(sort);
        return arcTypeList;
    }

    @Override
    public List<ArcType> list(Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable=new PageRequest(page-1, pageSize, direction, properties);
        Page<ArcType> arcTypePage = arcTypeRepository.findAll(pageable);
        return arcTypePage.getContent();
    }

    @Override
    public Long getTotal() {
        return arcTypeRepository.count();
    }

    @Override
    public ArcType get(Integer id) {
        return arcTypeRepository.findOne(id);
    }

    @Override
    public void save(ArcType arcType) {
        arcTypeRepository.save(arcType);
    }

    @Override
    public void delete(Integer id) {
        arcTypeRepository.delete(id);
    }

}
