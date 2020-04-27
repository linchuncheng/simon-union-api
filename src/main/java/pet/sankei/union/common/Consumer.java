package pet.sankei.union.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: Consumer条件解耦工具类
 * Created by 林春成 linchuncheng@3vjia.com on 2020/4/9.
 */
public interface Consumer<T> {

    Object accept(T t) throws Exception;

    Map<String, Map<String, Consumer>> consumersMap = new ConcurrentHashMap<>();

    static void register(String field, String key, Consumer consumer) {
        consumersMap.getOrDefault(field, new ConcurrentHashMap<>()).putIfAbsent(key, consumer);
    }

    static <T,R> R consume(String field, String key, T param) throws Exception {
        return (R)consume(field, key, param, null);
    }

    static <T,R> R consume(String field, String key, T param, Consumer defaultConsumer) throws Exception {
        Map<String, Consumer> consumers = Consumer.consumersMap.get(field);
        Consumer consumer = consumers.getOrDefault(key, defaultConsumer);
        if (consumer != null) {
            return (R)consumer.accept(param);
        } else if (defaultConsumer != null) {
            return (R)defaultConsumer.accept(param);
        }
        return (R)new Object();
    }
}
