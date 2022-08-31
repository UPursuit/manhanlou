package com.hey.mhl.service;

import com.hey.mhl.dao.MenuDAO;
import com.hey.mhl.domain.Menu;

import java.util.List;

/*
    @author 何恩运
    通过调用MenuDAO完成对menu表的各种操作
*/
public class MenuService {

    //定义MenuDAO属性
    private MenuDAO menuDAO = new MenuDAO();

    //返回所有菜品给界面使用
    public List<Menu> list() {

        return menuDAO.queryMulti("select * from menu", Menu.class);
    }

    //根据id返回Menu对象
    public Menu getMenuById(int id) {

        return menuDAO.querySingle("select * from menu where id = ?", Menu.class, id);
    }
}
