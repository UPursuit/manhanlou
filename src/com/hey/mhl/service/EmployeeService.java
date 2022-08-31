package com.hey.mhl.service;

import com.hey.mhl.dao.EmployeeDAO;
import com.hey.mhl.domain.Employee;

/*
    @author 何恩运
    该类完成对employee表的各种操作，通过调用EmployeeDAO对象完成
*/
public class EmployeeService {

    //定义一个 EmployeeDAO 属性
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    //编写方法，根据 empId 和 pwd 返回一个 Employee 对象，如果查询不到就返回null
    public Employee getEmployeeByIdAndPwd(String empId, String pwd) {
        Employee employee =
                employeeDAO.querySingle("select * from employee where empId = ? and pwd = md5(?)", Employee.class, empId, pwd);

        return employee;
    }

}
