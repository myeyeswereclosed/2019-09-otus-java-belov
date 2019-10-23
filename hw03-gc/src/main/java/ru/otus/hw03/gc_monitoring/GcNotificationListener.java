package ru.otus.hw03.gc_monitoring;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

public class GcNotificationListener implements NotificationListener {
    private Statistics statistics;

    public GcNotificationListener(Statistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public void handleNotification(Notification notification, Object handback) {
        if (garbageWasCollected(notification)) {
            GarbageCollectionNotificationInfo info =
                GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

            statistics.add(info);

            String gcName = info.getGcName();
            String gcAction = info.getGcAction();
            String gcCause = info.getGcCause();

            long startTime = info.getGcInfo().getStartTime();
            long duration = info.getGcInfo().getDuration();

            System.out.println(
                "start:" + startTime +
                    " Name:" + gcName +
                    ", action:" + gcAction +
                    ", gcCause:" + gcCause +
                    "(" + duration + " ms)");
        }
    }

    private boolean garbageWasCollected(Notification notification) {
        return notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION);
    }
}
