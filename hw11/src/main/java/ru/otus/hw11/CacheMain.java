package ru.otus.hw11;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw10.api.entity.User;
import ru.otus.hw10.api.service.DBServiceUser;
import ru.otus.hw11.cache.HwCache;
import ru.otus.hw11.cache.HwListener;
import ru.otus.hw11.cache.MyCache;
import ru.otus.hw11.db.CachedUserDbService;

import static ru.otus.hw10.HibernateMain.createUser;
import static ru.otus.hw10.HibernateMain.dbService;
import static ru.otus.hw10.HibernateMain.outputUserOptional;

public class CacheMain {
  private static final Logger logger = LoggerFactory.getLogger(CacheMain.class);

  public static void main(String[] args) {
    new CacheMain().demo();
  }

  private void demo() {
    HwCache<String, User> cache = new MyCache<>();
    HwListener<String, User> listener =
        (key, value, action) -> logger.info("key:{}, value:{}, action: {}", key, value, action);
    cache.addListener(listener);

    DBServiceUser userService = new CachedUserDbService(dbService(), cache);

    outputUserOptional(
        "Trying to find user using cache",
        userService.getUser(userService.saveUser(createUser()))
    );
  }
}
