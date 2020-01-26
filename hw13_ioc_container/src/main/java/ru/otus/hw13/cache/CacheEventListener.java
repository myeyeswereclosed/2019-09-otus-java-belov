package ru.otus.hw13.cache;

public interface CacheEventListener<K, V> {
  void notify(K key, V value, String action);
}
