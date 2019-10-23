package ru.otus.hw03;

import ru.otus.hw03.gc_monitoring.Monitoring;
import ru.otus.hw03.gc_monitoring.Statistics;
import ru.otus.hw03.leaking_list.LeakingList;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class GcMain {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());

        // Object collecting gc statistics
        Statistics statistics = new Statistics();

        // Object handling gc notification info (pass it to statistics)
        Monitoring monitoring = new Monitoring(statistics);

        monitoring.start();

        long beginTime = System.currentTimeMillis();

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        ObjectName name = new ObjectName("ru.otus:type=LeakingList");
        LeakingList leakingList = new LeakingList();

        mbs.registerMBean(leakingList, name);

        try {
            leakingList.addElements();
        } catch (OutOfMemoryError e) {
            System.out.println(
                "Out of memory occured! Time of work: " +
                (System.currentTimeMillis() - beginTime) / 1000 + " seconds"
            );

            monitoring.stop();

            System.out.println(statistics.results());
            System.out.println("Elements collected in list: " + leakingList.size());
        }
    }
}
