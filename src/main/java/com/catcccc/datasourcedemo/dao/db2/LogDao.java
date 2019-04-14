package com.catcccc.datasourcedemo.dao.db2;

import org.apache.ibatis.annotations.Param;


public interface LogDao {

  int addLog(@Param(value = "logDesc") String logDesc);

}