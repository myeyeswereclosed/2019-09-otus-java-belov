package ru.otus.hw03.gc_monitoring;

import com.sun.management.GarbageCollectionNotificationInfo;
import ru.otus.hw03.objects_generation.GcNameToGenerationMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Statistics {
    private List<GarbageCollectionNotificationInfo> garbageCollectionInfoList = new ArrayList<>();

    public void add(GarbageCollectionNotificationInfo garbageCollectionInfo) {
        garbageCollectionInfoList.add(garbageCollectionInfo);
    }

    public String results() {
        Map<String, List<GarbageCollectionNotificationInfo>> garbageCollectionsByName =
            garbageCollectionsByName();

        Map<String, String> result = new HashMap<>();

        garbageCollectionsByName.forEach(
            (name, infoList) ->
                result.put(
                    name, garbageCollectionsByMinutesAsString(garbageCollectionsByMinutes(infoList))
                )
        );

        return
            result
                .entrySet()
                .stream()
                .map(entry -> gcNameWithGeneration(entry.getKey()) + " -->\r\n" + entry.getValue())
                .collect(Collectors.joining("\r\n"));
    }

    private String gcNameWithGeneration(String gcName) {
        return gcName + '(' + GcNameToGenerationMapper.map(gcName) + ')';
    }

    private Map<String, List<GarbageCollectionNotificationInfo>> garbageCollectionsByName() {
        List<GarbageCollectionNotificationInfo> info = garbageCollectionInfoList;

        return
            info
                .stream()
                .collect(
                    Collectors.groupingBy(GarbageCollectionNotificationInfo::getGcName)
                );
    }

    private String garbageCollectionsByMinutesAsString(
        Map<Long, List<GarbageCollectionNotificationInfo>> gcInfoByMinutes
    ) {
        return
            gcInfoByMinutes
                .entrySet()
                .stream()
                .map(entry -> "\t " + entry.getKey() + " minute -> " + gcInfoAsString(entry.getValue()))
                .collect(Collectors.joining("\r\n"))
            ;
    }

    private Map<Long, List<GarbageCollectionNotificationInfo>> garbageCollectionsByMinutes(
        List<GarbageCollectionNotificationInfo> garbageCollectionInfoList
    ) {
        return
            garbageCollectionInfoList
                .stream()
                .collect(
                    Collectors.groupingBy(
                        info ->
                            TimeUnit.MILLISECONDS.toMinutes(info.getGcInfo().getStartTime()) + 1,
                        Collectors.toList()
                    )
                );
    }

    private String gcInfoAsString(List<GarbageCollectionNotificationInfo> garbageCollectionInfoList) {
        return
            "Collections count: " + garbageCollectionInfoList.size() + ", total duration: " +
                garbageCollectionInfoList
                    .stream()
                    .mapToLong(info -> info.getGcInfo().getDuration())
                    .sum() + " ms"
            ;
    }
}
