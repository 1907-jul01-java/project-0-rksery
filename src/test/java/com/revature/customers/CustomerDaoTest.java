package com.revature.customers;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.Connection;

import com.revature.ConnectionUtil;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class CustomerDaoTest {
    CustomerDao customerDao;
    Connection connection;

    @Before
    public void testInit() {
        ConnectionUtil connectionUtil = new ConnectionUtil();
        customerDao = new CustomerDao(connectionUtil.getConnection());
    }

    @Test
    public void givenUserReadAccountNumbersReturnsAccountNumber() {
        String user = "testcust";
        int expected = 100000;
        int actual = customerDao.readAccountNumbers(user);
        assertEquals(expected, actual);
    }

    @Test
    public void givenUserAndBigDecimalWithdrawBalanceReturnsString() {
        String user = "testcust";
        BigDecimal bd = new BigDecimal("100");
        String amt1 = "$100,000,000.23";
        String amt2 = "$99,999,900.23";
        String expected = amt1 + " - $" + bd + " = " + amt2;
        String actual = customerDao.withdrawBalance(user, bd);
        assertEquals(expected, actual);
    }

    @Test
    public void givenUserAndBigDecimalDepositBalanceReturnsString() {
        String user = "testcust";
        BigDecimal bd = new BigDecimal("100");
        String amt1 = "$99,999,900.23";
        String amt2 = "$100,000,000.23";
        String expected = amt1 + " + $" + bd + " = " + amt2;
        String actual = customerDao.depositBalance(user, bd);
        assertEquals(expected, actual);
    }

    @Test
    public void givenUserAccountNumberAndBigDecimalTransferBalanceReturnsString() {
        String user = "testcust";
        int an1 = 100000;
        int an2 = 100003;
        BigDecimal amt1 = new BigDecimal("100000000.23");
        BigDecimal amt2 = new BigDecimal("10000000.23");
        BigDecimal bd = new BigDecimal("1000");
        amt2 = amt2.add(bd);
        amt1 = amt1.subtract(bd);
        String expected = "New Balance of account number " + an1 + ": " + "99999000.23"
                + "\nNew Balance of account number " + an2 + ": " + "$10,001,000.23";
        String actual = customerDao.transferBalance(user, an2, bd);
        assertEquals(expected, actual);
    }
}
