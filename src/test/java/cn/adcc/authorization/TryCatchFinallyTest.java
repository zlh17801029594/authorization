package cn.adcc.authorization;

import org.junit.jupiter.api.Test;

public class TryCatchFinallyTest {

    @Test
    public void doTryCatchFinally() {
        try {
            int x = 2 / 1;
            System.out.println(String.format("结果为：%d", 1));
            return;
        } catch (Exception e) {
            System.out.println(String.format("结果为：%d", 2));
        } finally {
            //finally语句，就算try或catch已经return了，仍然会执行，这就是finally与最后一条语句的差异
            //try 语句块中有异常未捕获，仍然会执行finally语句块
            System.out.println(String.format("结果为：%d", 3));
        }
        //try或catch未return，才会执行到这里
        System.out.println(String.format("结果为：%d", 4));
    }
}
