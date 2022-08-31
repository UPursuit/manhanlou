package com.hey.mhl.view;

import com.hey.mhl.domain.*;
import com.hey.mhl.service.BillService;
import com.hey.mhl.service.DiningTableService;
import com.hey.mhl.service.EmployeeService;
import com.hey.mhl.service.MenuService;
import com.hey.mhl.utils.Utility;

import java.util.List;

/*
    @author 何恩运
    主界面
*/
public class MHLView {

    private boolean loop = true;//控制是否退出菜单
    private String key = "";//接收用户的选择
    //定义EmployeeService属性
    private EmployeeService employeeService = new EmployeeService();
    //定义DiningTableService属性
    private DiningTableService diningTableService = new DiningTableService();
    //定义MenuService属性
    private MenuService menuService = new MenuService();
    //定义BillService属性
    private BillService billService = new BillService();

    public static void main(String[] args) {
        new MHLView().mainMenu();
    }

    public void mainMenu() {

        while (loop) {
            System.out.println("================满汉楼================");
            System.out.println("\t\t\t 1 登录满汉楼");
            System.out.println("\t\t\t 2 退出满汉楼");
            System.out.print("请输入你的选择：");
            key = Utility.readString(1);
            switch (key) {
                case "1" :
                    System.out.print("输入员工号：");
                    String empId = Utility.readString(50);
                    System.out.print("输入密  码：");
                    String pwd = Utility.readString(50);
                    Employee employee = employeeService.getEmployeeByIdAndPwd(empId, pwd);
                    if (employee != null) {
                        System.out.println("=============登录成功[" + employee.getName() + "]=============\t");
                        //显示二级菜单
                        while (loop) {
                            System.out.println("\n==============满汉楼二级菜单==============");
                            System.out.println("\t\t\t 1 显示餐桌状态");
                            System.out.println("\t\t\t 2 预定餐桌");
                            System.out.println("\t\t\t 3 显示所有菜品");
                            System.out.println("\t\t\t 4 点餐服务");
                            System.out.println("\t\t\t 5 查看账单");
                            System.out.println("\t\t\t 6 结账");
                            System.out.println("\t\t\t 9 退出满汉楼");
                            System.out.print("请输入你的选择：");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    listDiningTable();
                                    break;
                                case "2":
                                    orderDiningTable();
                                    break;
                                case "3":
                                    listMenu();
                                    break;
                                case "4":
                                    orderMenu();
                                    break;
                                case "5":
                                    listBill();
                                    break;
                                case "6":
                                    payBill();
                                    break;
                                case "9":
                                    loop = false;
                                    break;
                                default:
                                    System.out.println("输入有误，请重新输入");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("===============登录失败===============");
                    }
                    break;
                case "2" :
                    loop = false;
                    break;
                default :
                    System.out.println("输入有误，请重新输入");
            }
        }

        System.out.println("你退出了满汉楼...");
    }

    //显示所有餐桌状态
    public void listDiningTable() {
        List<DiningTable> list = diningTableService.list();
        System.out.println("\n餐桌编号\t\t餐桌状态");
        for (DiningTable diningTable : list) {
            System.out.println(diningTable);
        }
        System.out.println("================显示完毕=================");
    }

    //完成订座
    public void orderDiningTable() {
        System.out.println("================预定餐桌=================");
        System.out.print("请选择要预定的餐桌编号(-1退出)：");
        int orderId = Utility.readInt();
        if (orderId == -1) {
            System.out.println("===============取消预定餐桌===============");
            return;
        }
        char key = Utility.readConfirmSelection();
        if (key == 'Y') {//要预定

            //根据orderId返回对应DiningTable对象，如果为null，说明该对象不存在
            DiningTable diningTable = diningTableService.getDiningTableById(orderId);
            if (diningTable == null) {
                System.out.println("==============预定餐桌不存在==============");
                return;
            }
            //判断改餐桌的状态是否为"空"
            if (!("空".equals(diningTable.getState()))) {
                System.out.println("============该餐桌已预定或就餐中============");
                return;
            }

            //接收预定信息
            System.out.print("预定人的名字：");
            String orderName = Utility.readString(50);
            System.out.print("预定人的电话：");
            String orderTel = Utility.readString(50);

            //更新餐桌状态
            if (diningTableService.orderDiningTable(orderId, orderName, orderTel)) {
                System.out.println("===============预定餐桌成功===============");
            } else {
                System.out.println("===============预定餐桌失败===============");
            }
        } else {
            System.out.println("===============取消预定餐桌===============");
        }
    }

    //显示所有菜品
    public void listMenu() {
        List<Menu> list = menuService.list();
        System.out.println("\n菜品编号\t\t菜品名\t\t类别\t\t价格");
        for (Menu menu : list) {
            System.out.println(menu);
        }
        System.out.println("================显示完毕=================");
    }

    //完成点餐
    public void orderMenu() {
        System.out.println("================点餐服务================");
        System.out.print("请输入点餐的桌号(-1退出)：");
        int orderDiningTableId = Utility.readInt();
        if (orderDiningTableId == -1) {
            System.out.println("================取消点餐================");
            return;
        }
        System.out.print("请输入点餐的菜品号(-1退出)：");
        int orderMenuId = Utility.readInt();
        if (orderMenuId == -1) {
            System.out.println("================取消点餐================");
            return;
        }
        System.out.print("请输入点餐的菜品量(-1退出)：");
        int orderNums = Utility.readInt();
        if (orderNums == -1) {
            System.out.println("================取消点餐================");
            return;
        }

        //验证餐桌号是否存在
        DiningTable diningTable = diningTableService.getDiningTableById(orderDiningTableId);
        if (diningTable == null) {
            System.out.println("===============餐桌号不存在===============");
            return;
        }
        //验证菜品编号是否存在
        Menu menu = menuService.getMenuById(orderMenuId);
        if (menu == null) {
            System.out.println("===============菜品号不存在===============");
            return;
        }

        //点餐
        if (billService.orderMenu(orderMenuId, orderNums, orderDiningTableId)) {
            System.out.println("================点餐成功================");
        } else {
            System.out.println("================点餐失败================");
        }
    }

    //显示账单信息
    public void listBill() {
//        List<Bill> bills = billService.list();
//        System.out.println("\n编号\t\t菜品号\t\t菜品量\t\t金额\t\t桌号\t\t日期\t\t\t\t\t\t\t状态");
//        for (Bill bill : bills) {
//            System.out.println(bill);
//        }
//        System.out.println("================显示完毕=================");

        List<MultiTableBean> multiTableBeans = billService.list2();
        System.out.println("\n编号\t\t菜品号\t\t菜品量\t\t金额\t\t桌号\t\t日期\t\t\t\t\t\t\t状态\t\t菜品名\t\t价格");
        for (MultiTableBean bill : multiTableBeans) {
            System.out.println(bill);
        }
        System.out.println("================显示完毕=================");
    }

    //完成结账
    public void payBill() {
        System.out.println("================结账服务================");
        System.out.print("请选择要结账的餐桌编号(-1退出)：");
        int diningTableId = Utility.readInt();
        if (diningTableId == -1) {
            System.out.println("================取消结账================");
            return;
        }
        //验证餐桌是否存在
        DiningTable diningTable = diningTableService.getDiningTableById(diningTableId);
        if (diningTable == null) {
            System.out.println("==============结账餐桌不存在==============");
            return;
        }
        //验证餐桌是否有需要结账的账单
        if (!billService.hasPayBillByDiningTableId(diningTableId)) {
            System.out.println("===========该餐桌没有未结账账单===========");
            return;
        }
        System.out.print("结账方式(现金/微信/支付宝)回车表示退出：");
        String payMode = Utility.readString(20, "");//如果回车就返回""
        if ("".equals(payMode)) {
            System.out.println("================取消结账================");
            return;
        }
        if (!(payMode.equals("现金") || payMode.equals("微信") || payMode.equals("支付宝"))) {
            System.out.println("===========输入有误，请重新输入===========");
            return;
        }
        char key = Utility.readConfirmSelection();
        if (key == 'Y') {
            if (billService.payBill(diningTableId, payMode)) {
                System.out.println("================完成结账================");
            } else {
                System.out.println("================结账失败================");
            }
        } else {
            System.out.println("================取消结账================");
        }
    }
}
