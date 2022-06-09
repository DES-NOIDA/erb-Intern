package com.thales.ERB.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.thales.ERB.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsernameAndPassword(String username, String password);
	
	@Query("select u from User u where u.username = :userName")
	public User findUserByName(@Param("userName") String userName);
	
	@Transactional
	@Modifying
	@Query("update User u set u.password =:passWord where u.username =:userName")
	public void update(@Param("userName") String userName, @Param("passWord") String passWord);
	
	@Modifying
	@Transactional
	@Query("delete from User u where u.username = :userName")
	 public void deleteByUsername(@Param("userName") String userName);
}
