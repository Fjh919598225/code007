package com.java1234.repository;

import com.java1234.entity.ArcType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 资源类型repository接口
 * @Date 2020/1/22 17:25
 * @Author JianHui
 */
public interface ArcTypeRepository extends JpaRepository<ArcType,Integer>, JpaSpecificationExecutor<ArcType> {
}
