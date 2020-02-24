package ru.otus.hw15.cache;

public interface CacheEventListener<K, V> {
  void notify(K key, V value, String action);
}
