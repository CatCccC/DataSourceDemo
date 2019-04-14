package com.catcccc.datasourcedemo.dao.db1;

import org.apache.ibatis.annotations.Param;

public interface UserDao {

  int addUser(@Param(value = "userName") String userName);

}
