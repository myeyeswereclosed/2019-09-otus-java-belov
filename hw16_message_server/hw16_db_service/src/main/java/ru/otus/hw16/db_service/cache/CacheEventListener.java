package ru.otus.hw16.db_service.cache;

public interface CacheEventListener<K, V> {
  void notify(K key, V value, String action);
}
