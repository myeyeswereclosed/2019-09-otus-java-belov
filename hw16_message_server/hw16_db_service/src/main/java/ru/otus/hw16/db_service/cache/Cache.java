package ru.otus.hw16.db_service.cache;

public interface Cache<K, V> {

  void put(K key, V value);

  void remove(K key);

  V get(K key);

  void addListener(CacheEventListener listener);

  void removeListener(CacheEventListener listener);
}