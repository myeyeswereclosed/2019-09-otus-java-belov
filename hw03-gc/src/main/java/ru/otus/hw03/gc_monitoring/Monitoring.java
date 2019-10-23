package ru.otus.hw03.gc_monitoring;

import javax.management.ListenerNotFoundException;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Monitoring {
    private Statistics statistics;
    private Map<NotificationEmitter, NotificationListener> emitterAndListenerMap = new HashMap<>();

    public Monitoring(Statistics statistics) {
        this.statistics = statistics;
    }

    public void start() {
        List<GarbageCollectorMXBean> gcbeans = ManagementFactory.getGarbageCollectorMXBeans();

        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());

            NotificationEmitter emitter = (NotificationEmitter) gcbean;

            NotificationListener listener = new GcNotificationListener(statistics);

            emitterAndListenerMap.put(emitter, listener);

            emitter.addNotificationListener(listener, null, null);
        }
    }

    public void stop() {
        emitterAndListenerMap.forEach(
            (emitter, listener) -> {
                try {
                    emitter.removeNotificationListener(listener);
                } catch (ListenerNotFoundException e) {

                }
            }
        );
    }
}
