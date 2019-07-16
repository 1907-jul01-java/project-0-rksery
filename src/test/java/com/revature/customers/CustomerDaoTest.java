package com.revature.customers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class CustomerDaoTest {
    CustomerDao customerDao;

    @Test
    public void givenUserReadAccountNumbersReturnsAccountNumber() {
        String user = "testcust";
        int expected = 100000;
        int actual = customerDao.readAccountNumbers(user);
        assertEquals(expected, actual);
    }
}
