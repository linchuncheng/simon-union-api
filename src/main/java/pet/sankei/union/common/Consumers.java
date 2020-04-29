package pet.sankei.union.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: Consumer条件解耦工具类
 * Created by 林春成 linchuncheng@3vjia.com on 2020/4/9.
 */
public class Consumers<T> {
    static Map<String, Map<String, Consumer>> consumersMap = new ConcurrentHashMap<>();

    public static void register(String field, String key, Consumer consumer) {
        consumersMap.getOrDefault(field, new ConcurrentHashMap<>()).putIfAbsent(key, consumer);
    }

    public static <T,R> R consume(String field, String key, T param) throws Exception {
        return (R)consume(field, key, param, null);
    }

    public static <T,R> R consume(String field, String key, T param, Consumer defaultConsumers) throws Exception {
        Map<String, Consumer> consumers = Consumers.consumersMap.get(field);
        Consumer consumer = consumers.getOrDefault(key, defaultConsumers);
        if (consumer != null) {
            return (R)consumer.accept(param);
        } else if (defaultConsumers != null) {
            return (R) defaultConsumers.accept(param);
        }
        return (R)new Object();
    }
    public interface Consumer<T> {
        Object accept(T t) throws Exception;
    }
}
