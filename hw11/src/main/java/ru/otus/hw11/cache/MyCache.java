package ru.otus.hw11.cache;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.function.Consumer;

public class MyCache<K, V> implements HwCache<K, V> {
  private WeakHashMap<K, V> storage = new WeakHashMap<>();
  private List<HwListener<K, V>> listeners = new ArrayList<>();
  private final Logger logger;

  public MyCache(Logger logger) {
    this.logger = logger;
  }

  @Override
  public void put(K key, V value) {
    storage.put(key, value);
    notifyCacheChanged(key, value, "put");
  }

  @Override
  public void remove(K key) {
    notifyCacheChanged(key, get(key), "remove");
    storage.remove(key);
  }

  @Override
  public V get(K key) {
    return storage.get(key);
  }

  @Override
  public void addListener(HwListener listener) {
    executeHandlingException(hwListener -> listeners.add(hwListener), listener);
  }

  @Override
  public void removeListener(HwListener listener) {
    executeHandlingException(hwListener -> listeners.remove(hwListener), listener);
  }

  private void notifyCacheChanged(K key, V value, String action) {
    listeners.forEach(
        listener -> executeHandlingException(
            (hwListener)-> hwListener.notify(key, value, action), listener
        )
    );
  }

  private void executeHandlingException(Consumer<HwListener<K, V>> listenerFunction, HwListener<K, V> listener) {
    try {
      listenerFunction.accept(listener);
    } catch (Exception e) {
      logger.error(
          "Exception " + e.getMessage() + " occured. Trace:\n" + ExceptionUtils.getStackTrace(e));
    }
  }
}
