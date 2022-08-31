package com.hey.mhl.service;

import com.hey.mhl.dao.DiningTableDAO;
import com.hey.mhl.domain.DiningTable;

import java.util.List;

/*
    @author 何恩运
*/
public class DiningTableService {

    //定义一个DiningTableDAO对象
    private DiningTableDAO diningTableDAO = new DiningTableDAO();

    //返回所有餐桌信息
    public List<DiningTable> list() {

        return diningTableDAO.queryMulti("select id, state from diningTable", DiningTable.class);
    }

    //根据id查询对应的餐桌DiningTable对象，如果返回null，表示id编号对应的餐桌不存在
    public DiningTable getDiningTableById(int id) {

        //小技巧: 把sql语句放在查询分析器去测试一下
        return diningTableDAO.querySingle("select * from diningTable where id = ?", DiningTable.class, id);
    }

    //如果餐桌可以预定，则调用方法对其状态进行更新(包括预定人的名字和电话)
    public boolean orderDiningTable(int id, String orderName, String orderTel) {

        int update =
                diningTableDAO.update("update diningTable set state = '已经预定', orderName = ?, orderTel = ? where id = ?", orderName, orderTel, id);

        return  update > 0;
    }

    //提供一个可以更新餐桌状态的方法
    public boolean updateDiningTableState(int id, String state) {

        int update = diningTableDAO.update("update diningTable set state = ? where id = ?", state, id);
        return update > 0;
    }

    //提供方法，将指定的餐桌设置为空闲状态
    public boolean updateDiningTableToFree(int id, String state) {

        int update =
                diningTableDAO.update("update diningTable set state = ?, orderName = '', orderTel = '' where id = ?", state, id);
        return update > 0;
    }
}
