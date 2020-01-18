package ru.otus.hw11.cache;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw10.api.entity.AddressDataSet;
import ru.otus.hw10.api.entity.User;

public class MyCacheTest {
    private static int USER_ID = 23;
    private static final Logger logger = LoggerFactory.getLogger(MyCacheTest.class);


    @Test
    public void cacheUser() {
        HwCache<String, User> cache = new MyCache<>(logger);
        ListenerMock listener = new ListenerMock();
        cache.addListener(listener);

        User user = new User("Nikolay Ivanovich", new AddressDataSet("Wall Street, 15"));
        cache.put(String.valueOf(USER_ID), user);

        User cachedUser = cache.get(String.valueOf(USER_ID));

        Assert.assertEquals(1, listener.notificationWasCalled());
        Assert.assertEquals(user, cachedUser);
    }

    @Test
    public void noCacheAfterGc() {
        HwCache<String, User> cache = new MyCache<>(logger);
        ListenerMock listener = new ListenerMock();
        cache.addListener(listener);

        User user = new User("Nikolay Ivanovich", new AddressDataSet("Wall Street, 15"));
        cache.put(String.valueOf(USER_ID), user);

        User cachedUser = cache.get(String.valueOf(USER_ID));

        Assert.assertEquals(1, listener.notificationWasCalled());
        Assert.assertEquals(user, cachedUser);

        System.gc();

        Assert.assertNull(cache.get(String.valueOf(USER_ID)));
        Assert.assertEquals(1, listener.notificationWasCalled());
    }

    @Test
    public void removeCachedUser() {
        HwCache<String, User> cache = new MyCache<>(logger);
        ListenerMock listener = new ListenerMock();
        cache.addListener(listener);

        User user = new User("Nikolay Ivanovich", new AddressDataSet("Wall Street, 15"));
        cache.put(String.valueOf(USER_ID), user);

        User cachedUser = cache.get(String.valueOf(USER_ID));

        Assert.assertEquals(1, listener.notificationWasCalled());
        Assert.assertEquals(user, cachedUser);

        cache.remove(String.valueOf(USER_ID));

        Assert.assertNull(cache.get(String.valueOf(USER_ID)));
        Assert.assertEquals(2, listener.notificationWasCalled());
    }

    @Test
    public void noListener() {
        HwCache<String, User> cache = new MyCache<>(logger);
        ListenerMock listener = new ListenerMock();

        User user = new User("Nikolay Ivanovich", new AddressDataSet("Wall Street, 15"));
        cache.put(String.valueOf(USER_ID), user);

        User cachedUser = cache.get(String.valueOf(USER_ID));

        Assert.assertEquals(0, listener.notificationWasCalled());
        Assert.assertEquals(user, cachedUser);
    }

    @Test
    public void removeListener() {
        HwCache<String, User> cache = new MyCache<>(logger);
        ListenerMock listener = new ListenerMock();
        cache.addListener(listener);

        User user = new User("Nikolay Ivanovich", new AddressDataSet("Wall Street, 15"));
        cache.put(String.valueOf(USER_ID), user);

        User cachedUser = cache.get(String.valueOf(USER_ID));

        Assert.assertEquals(1, listener.notificationWasCalled());
        Assert.assertEquals(user, cachedUser);

        cache.removeListener(listener);
        cache.remove(String.valueOf(USER_ID));

        Assert.assertNull(cache.get(String.valueOf(USER_ID)));
        Assert.assertEquals(1, listener.notificationWasCalled());
    }

    private class ListenerMock implements HwListener {
        private int callCounter = 0;

        @Override
        public void notify(Object key, Object value, String action) {
            callCounter++;
        }

        public int notificationWasCalled() {
            return callCounter;
        }
    }
}
