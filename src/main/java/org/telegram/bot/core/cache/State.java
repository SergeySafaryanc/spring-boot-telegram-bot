package org.telegram.bot.core.cache;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class State {

    private static final ConcurrentHashMap<Long, Item> map = new ConcurrentHashMap<>();
    private static final int ttl = 1;
    private static final TimeUnit timeUnit = TimeUnit.DAYS;

    private State() {
        Thread th = new Thread(new ClearRunnable(), "Clear-Thread");
        th.setDaemon(true);
        th.setPriority(Thread.MIN_PRIORITY);
        th.start();
    }

    public static void setState(Long userId, String state) {
        long expireTime = System.currentTimeMillis() + timeUnit.toMillis(ttl);
        map.put(userId, new State.Item(state, expireTime));
    }

    public static String getState(Long userId) {
        Item item = null;
        item = map.get(userId);
        if (item == null || !item.isValid()) {
            return "";
        } else {
            return item.value;
        }
    }

    public static void clear(Long userId) {
        map.remove(userId);
    }

    private static class ClearRunnable implements Runnable {

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    Set<Long> keys = map.keySet();
                    for (Long key : keys) {
                        State.Item item = map.get(key);
                        if (!item.isValid()) {
                            map.remove(key);
                        }
                    }
                    if (Thread.interrupted()) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private static class Item {
        private final String value;
        private final long expireTime;

        Item(String value, long expireTime) {
            super();
            this.value = value;
            this.expireTime = expireTime;
        }

        private boolean isValid() {
            return System.currentTimeMillis() <= expireTime;
        }
    }

}
