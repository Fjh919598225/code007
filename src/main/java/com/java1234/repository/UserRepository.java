package com.java1234.repository;

import com.java1234.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户Repository接口
 * @Date 2020/1/26 13:25
 * @Author JianHui
 */
public interface UserRepository extends JpaRepository<User,Integer> , JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查找实体
     * @param userName
     * @return
     */
    @Query(value = "select * from t_user where user_name=?1",nativeQuery = true)
    public User findByUserName(String userName);

    /**
     * 根据用户名查找实体
     * @param userName
     * @return
     */
    @Query(value = "select * from t_user where email=?1",nativeQuery = true)
    public User findByEmail(String email);


    /**
     * 重置所有签到信息
     */
    @Transactional
    @Modifying
    @Query(value = "update t_user set is_sign = false ,sign_sort=null ,sign_time = null ",nativeQuery = true)
    public void updateAllSignInfo();
}
