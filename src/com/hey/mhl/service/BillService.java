package com.hey.mhl.service;

import com.hey.mhl.dao.BillDAO;
import com.hey.mhl.dao.MultiTableDAO;
import com.hey.mhl.domain.Bill;
import com.hey.mhl.domain.Menu;
import com.hey.mhl.domain.MultiTableBean;

import java.util.List;
import java.util.UUID;

/*
    @author 何恩运
    处理和账单相关的业务逻辑
*/
public class BillService {
    //定义BillDAO属性
    private BillDAO billDAO = new BillDAO();
    //定义MenuService属性
    private MenuService menuService = new MenuService();
    //定义DiningTableService属性
    private DiningTableService diningTableService = new DiningTableService();
    //定义MultiTableDAO属性
    private MultiTableDAO multiTableDAO = new MultiTableDAO();

    //编写点餐的方法：1.生成账单；2.需要更新对应餐桌的状态；3.如果成功返回true，否则返回false
    public boolean orderMenu(int menuId, int nums, int diningTableId) {
        //生成一个账单号
        String billId = UUID.randomUUID().toString();

        //将账单生成bill表，要求直接计算账单金额
        int update = billDAO.update("insert into bill values(null, ?, ?, ?, ?, ?, now(), '未结账')",
                billId, menuId, nums, menuService.getMenuById(menuId).getPrice() * nums, diningTableId);

        if (update <= 0) {
            return false;
        }

        //更新对应餐桌的状态
        return diningTableService.updateDiningTableState(diningTableId, "就餐中");
    }

    //返回所有账单，提供给View调用
    public List<Bill> list() {
        return billDAO.queryMulti("select * from bill", Bill.class);
    }
    //返回所有账单并带有菜品名，提供给View调用
    public List<MultiTableBean> list2() {
        return multiTableDAO.queryMulti("select bill.*, name, price from bill, menu where bill.menuId = menu.id", MultiTableBean.class);
    }

    //查看某个餐桌是否有未结账的账单
    public boolean hasPayBillByDiningTableId(int diningTableId) {
        Bill bill =
                billDAO.querySingle("select * from bill where diningTableId = ? and state = '未结账' limit 0, 1", Bill.class, diningTableId);
        return bill != null;
    }

    //完成结账[如果餐桌存在，并且该餐桌有未结账的账单]
    public boolean payBill(int diningTableId, String payMode) {

        int update =
                billDAO.update("update bill set state = ? where diningTableId = ? and state = '未结账'", payMode, diningTableId);

        //如果更新没有成功，则表示失败
        if (update <= 0) {
            return false;
        }

        if (!diningTableService.updateDiningTableToFree(diningTableId, "空")) {
            return false;
        }

        return true;
    }
}
