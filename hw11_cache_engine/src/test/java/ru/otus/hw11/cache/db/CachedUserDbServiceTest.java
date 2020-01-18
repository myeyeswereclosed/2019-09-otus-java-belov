package ru.otus.hw11.cache.db;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.hw10.api.entity.AddressDataSet;
import ru.otus.hw10.api.entity.User;
import ru.otus.hw10.api.service.DBServiceUser;
import ru.otus.hw11.cache.HwCache;
import ru.otus.hw11.cache.HwListener;
import ru.otus.hw11.db.CachedUserDbService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CachedUserDbServiceTest {
    @Test
    public void cachedUser() {
        CacheMock cache = new CacheMock();
        User user = new User("Nikolay Ivanovich", new AddressDataSet("Wall Street, 15"));
        cache.put("1", user);

        DbServiceMock dbService = new DbServiceMock(user);

        DBServiceUser service = new CachedUserDbService(dbService, cache);

        Optional<User> userInService = service.getUser(1);

        Assert.assertTrue(userInService.isPresent());
        Assert.assertEquals(user, userInService.get());
        Assert.assertEquals(1, cache.cachingWasCalled());
        Assert.assertEquals(1, cache.getUserWasCalled());
        Assert.assertEquals(0, dbService.getUserWasCalled());
    }

    @Test
    public void nonCachedUser() {
        CacheMock cache = new CacheMock();
        User user = new User("Nikolay Ivanovich", new AddressDataSet("Wall Street, 15"));
        DbServiceMock dbService = new DbServiceMock(user);

        DBServiceUser service = new CachedUserDbService(dbService, cache);

        Assert.assertEquals(0, cache.cachingWasCalled());

        Optional<User> userInService = service.getUser(1);

        Assert.assertTrue(userInService.isPresent());
        Assert.assertEquals(user, userInService.get());
        Assert.assertEquals(1, cache.cachingWasCalled());
        Assert.assertEquals(1, cache.getUserWasCalled());
        Assert.assertEquals(1, dbService.getUserWasCalled());
        Assert.assertEquals(user, cache.get(0));
    }

    private static class DbServiceMock implements DBServiceUser {
        private User user;
        private int callCounter = 0;

        public DbServiceMock(User user) {
            this.user = user;
        }

        @Override
        public long saveUser(User user) {
            return 1;
        }

        @Override
        public Optional<User> getUser(long id) {
            callCounter++;
            return Optional.of(user);
        }

        public int getUserWasCalled() {
            return callCounter;
        }
    }

    private static class CacheMock implements HwCache {
        private Map<String, User> storage = new HashMap<>();
        private int putCallCounter = 0;
        private int getCallCounter = 0;

        @Override
        public void put(Object key, Object value) {
            putCallCounter++;
            storage.put(key.toString(), (User)value);
        }

        @Override
        public void remove(Object key) {

        }

        @Override
        public Object get(Object key) {
            getCallCounter++;

            return storage.get(key.toString());
        }

        @Override
        public void addListener(HwListener listener) {

        }

        @Override
        public void removeListener(HwListener listener) {

        }

        public int cachingWasCalled() {
            return putCallCounter;
        }

        public int getUserWasCalled() {
            return getCallCounter;
        }
    }
}
