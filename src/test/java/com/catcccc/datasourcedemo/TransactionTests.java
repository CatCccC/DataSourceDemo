package com.catcccc.datasourcedemo;

import com.catcccc.datasourcedemo.dao.db1.UserDao;
import com.catcccc.datasourcedemo.dao.db2.LogDao;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionTests {

  @Resource(name = "db1TransactionManager")
  PlatformTransactionManager db1TransactionManager;
  @Resource(name = "db2TransactionManager")
  PlatformTransactionManager db2TransactionManager;

  @Autowired
  UserDao userDao;
  @Autowired
  LogDao logDao;


  //cashierDataSource 事务测试  报错是否能够回滚
  @Test
  public void testDB1Transaction() {
    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    TransactionStatus status = db1TransactionManager.getTransaction(def);
    try {
      userDao.addUser("测试");
      throw new NullPointerException("test null for rollback");
      //cashierTransactionManager.commit(status);
    } catch (Exception e) {
      e.printStackTrace();
      db1TransactionManager.rollback(status);
    }
  }


  @Test
  public void testDB2Transaction() {
    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    TransactionStatus status = db2TransactionManager.getTransaction(def);
    try {
      logDao.addLog("测试日志");
      throw new NullPointerException("test null for rollback");
      //uqpaydbTransactionManager.commit(status);
    } catch (Exception e) {
      e.printStackTrace();
      db2TransactionManager.rollback(status);
    }
  }

  /**
   * 错误示例 addUser 将无法回滚 且提示java.lang.IllegalStateException: Transaction synchronization is not
   * active
   *
   * 异常是由于spring的事务处理是按照LIFO/stack behavior的方式进行的，所以在多个事务进行提交时必须按照上述规则进行，否则就会报下面的异常。
   */
  @Test
  public void errorTransactionTest() {
    DefaultTransactionDefinition db1 = new DefaultTransactionDefinition();
    db1.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    TransactionStatus db1Status = db1TransactionManager.getTransaction(db1);

    DefaultTransactionDefinition db2 = new DefaultTransactionDefinition();
    db2.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    TransactionStatus db2Status = db2TransactionManager.getTransaction(db2);
    try {
      userDao.addUser("测试2");
      db1TransactionManager.commit(db1Status);
      logDao.addLog("ad");
      db2TransactionManager.commit(db2Status);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 正确示例 多个事务执行 顺序 需要符合LIFO/stack behavior的方式
   */
  @Test
  public void TransactionTest() {
    DefaultTransactionDefinition db1 = new DefaultTransactionDefinition();
    db1.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    TransactionStatus db1Status = db1TransactionManager.getTransaction(db1);

    try {
      userDao.addUser("123");
      //  throw new NullPointerException("test null for rollback");
      db1TransactionManager.commit(db1Status);
    } catch (Exception e) {
      db1TransactionManager.rollback(db1Status);
    }
    DefaultTransactionDefinition db2 = new DefaultTransactionDefinition();
    db2.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    TransactionStatus db2Status = db2TransactionManager.getTransaction(db2);
    try {
      logDao.addLog("asd");
      //  throw new NullPointerException("test null for rollback");
      db2TransactionManager.commit(db2Status);
    } catch (Exception e) {
      db2TransactionManager.rollback(db2Status);
    }
  }
}

