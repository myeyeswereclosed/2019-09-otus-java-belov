package ru.otus.hw16.db_service.cache;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.function.Consumer;

@Component
public class MyCache<K, V> implements Cache<K, V> {
  private static final String PUT_ACTION = "put";
  private static final String REMOVE_ACTION = "remove";

  private static final Logger logger = LoggerFactory.getLogger(MyCache.class);

  private WeakHashMap<K, V> storage = new WeakHashMap<>();
  private List<CacheEventListener<K, V>> listeners = new ArrayList<>();

  @Override
  public void put(K key, V value) {
    storage.put(key, value);
    notifyCacheChanged(key, value, PUT_ACTION);
  }

  @Override
  public void remove(K key) {
    notifyCacheChanged(key, get(key), REMOVE_ACTION);
    storage.remove(key);
  }

  @Override
  public V get(K key) {
    return storage.get(key);
  }

  @Override
  public void addListener(CacheEventListener listener) {
    executeHandlingException(hwListener -> listeners.add(hwListener), listener);
  }

  @Override
  public void removeListener(CacheEventListener listener) {
    executeHandlingException(hwListener -> listeners.remove(hwListener), listener);
  }

  private void notifyCacheChanged(K key, V value, String action) {
    listeners.forEach(
        listener -> executeHandlingException(
            (hwListener)-> hwListener.notify(key, value, action), listener
        )
    );
  }

  private void executeHandlingException(Consumer<CacheEventListener<K, V>> listenerFunction, CacheEventListener<K, V> listener) {
    try {
      listenerFunction.accept(listener);
    } catch (Exception e) {
      logger.error(
          "Exception " + e.getMessage() + " occured. Trace:\n" + ExceptionUtils.getStackTrace(e));
    }
  }
}
