package com.catcccc.datasourcedemo;

import com.catcccc.datasourcedemo.dao.db1.UserDao;
import com.catcccc.datasourcedemo.dao.db2.LogDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatasourcedemoApplicationTests {

  @Autowired
  UserDao userDao;
  @Autowired
  LogDao logDao;

  @Test
  public void contextLoads() {
    logDao.addLog("123");
    userDao.addUser("12");
  }

}
