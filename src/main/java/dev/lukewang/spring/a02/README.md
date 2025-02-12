## BeanFactory的实现

1. beanFactory 不会做的事
   a. 不会主动调用“BeanFactory后处理器”
   b. 不会主动添加“Bean后处理器”
   c. 不会主动预先实例化单例对象
   d. 不会解析 ${} #{}
2. “bean后处理器”有排序
