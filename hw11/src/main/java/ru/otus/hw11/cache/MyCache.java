package ru.otus.hw11.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
  private WeakHashMap<K, V> storage = new WeakHashMap<>();
  private List<HwListener<K, V>> listeners = new ArrayList<>();

  @Override
  public void put(K key, V value) {
    storage.put(key, value);
    listeners.forEach(listener -> listener.notify(key, value, "put"));
  }

  @Override
  public void remove(K key) {
    listeners.forEach(listener -> listener.notify(key, get(key), "remove"));
    storage.remove(key);
  }

  @Override
  public V get(K key) {
    return storage.get(key);
  }

  @Override
  public void addListener(HwListener listener) {
    listeners.add(listener);
  }

  @Override
  public void removeListener(HwListener listener) {
    listeners.remove(listener);
  }
}
