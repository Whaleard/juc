package future;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 案例说明：电商比价需求，模拟如下情况
 *
 * 需求：
 *  1、同一款产品，同时搜索出同款产品在各大电商平台的售价
 *  2、同一款产品，同时搜索出本产品在同一个电商平台，各个入驻卖家售价是多少
 *
 * 输出：结果希望是同款产品在不同地方的价格清单列表，返回一个List<String>
 *  《mysql》 in jd price is 88.05
 *  《mysql》 in dangdang price is 86.11
 *  《mysql》 in taobao price is 90.43
 *
 * 技术要求：
 *  1、函数式变成
 *  2、链式编程
 *  3、Stream流式计算
 */
public class CompletableFutureECDemo {

    static List<NetMall> list = Arrays.asList(
            new NetMall("jd"),
            new NetMall("dangdang"),
            new NetMall("taobao")

    );

    /**
     * step by step 一家家搜索
     *
     * @param list
     * @param productName
     * @return
     */
    public static List<String> getPrice(List<NetMall> list, String productName) {
        return list.stream()
                .map(netMall ->
                        String.format(productName + "in %s price is %.2f",
                                netMall.getName(),
                                netMall.calcPrice(productName)))
                .collect(Collectors.toList());
    }

    /**
     *
     *
     * @param list
     * @param productName
     * @return
     */
    public static List<String> getPriceByCompletableFuture(List<NetMall> list, String productName) {
        return list.stream()
                .map(netMall ->
                        CompletableFuture.supplyAsync(new Supplier<String>() {
                            @Override
                            public String get() {
                                return String.format(productName + "in %s price is %.2f",
                                        netMall.getName(),
                                        netMall.calcPrice(productName));
                            }
                        }))
                .collect(Collectors.toList())
                .stream()
                .map(s -> s.join())
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        List<String> result = getPrice(list, "《mysql》");
        for (String str : result) {
            System.out.println(str);
        }
        long endTime = System.currentTimeMillis();
        List<String> result2 = getPriceByCompletableFuture(list, "《mysql》");
        for (String str : result2) {
            System.out.println(str);
        }
        System.out.println("======costTime：" + (endTime - startTime) + "毫秒");
    }
}

class NetMall {

    private String name;

    public NetMall(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double calcPrice(String productName) {
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return ThreadLocalRandom.current().nextDouble() * 2 + productName.charAt(1);
    }
}
