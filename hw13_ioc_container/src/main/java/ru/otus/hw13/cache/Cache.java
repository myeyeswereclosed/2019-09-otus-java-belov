package ru.otus.hw13.cache;

import org.springframework.stereotype.Component;


public interface Cache<K, V> {

  void put(K key, V value);

  void remove(K key);

  V get(K key);

  void addListener(CacheEventListener listener);

  void removeListener(CacheEventListener listener);
}
