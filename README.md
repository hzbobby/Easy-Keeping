# Easy - Keeping 易记

Android 记账软件

## 引用开源库

    ### 1.skydoves/ColorPickerView [https://github.com/skydoves/ColorPickerView]

## APIs

    ### 1.https://www.exchangerate-api.com/docs/standard-requests

# 开发记录

###### 4.23

21.优化一下homeFragment的item项

1.~~添加账户后，直接记账，丢失category title信息~~，同2

2.~~默认 分类-其他 有问题~~，对billViewModel设置默认已解决

3.~~对category title 设置为 distinct~~ ，算鸟

4.用bottom sheet 显示账单的详情，里面可以删除和编辑
1.~~显示图片~~      carousel success
2.bottom 下滑与 内部的 scrollView 滑动冲突
3.退款、删除、编辑按钮事件
4.~~分类图标颜色~~
5.~~转账界面~~
6.~~账户item显示~~
7.~~金额颜色~~
8.选择账户，该账户没有扣款
9.bill activity -> images use carousel in bottom view
10.chip长按取消设置
11.怎么获取初始Account 和 编辑的 Account
12.设置编辑时，初始Category选中
13.貌似跟初始时的那个category冲突了
14.编辑保存
15.~~payee 改为 收款方~~   
16.~~自动记账相关的一些默认设置，账本、角色、账户(创建默认的 微信、支付宝账户)、类别~~
17.bills表 insert 能不能做一个触发，自动去相应的account里扣款/加款

5.~~更改transfer item layout~~

22.~~homeFrag 上Header的信息~~

23.日历页面

24.图表页面

25.无障碍记账

26.语音输入

27.文字识别

28.截图记账

29.模板记账