package ru.otus.hw03.objects_generation;

import java.util.Arrays;

public class GcNameToGenerationMapper {
    // Serial
    private static final String SERIAL_YOUNG_GEN_GC = "Copy";
    private static final String SERIAL_OLD_GEN_GC = "MarkSweepCompact";

    // Parallel
    private static final String PARALLEL_YOUNG_GEN_GC = "PS Scavenge";
    private static final String PARALLEL_OLD_GEN_GC = "PS MarkSweep";

    // CMS
    private static final String CMS_YOUNG_GEN_GC = "ParNew";
    private static final String CMS_OLD_GEN_GC = "ConcurrentMarkSweep";

    // G1
    private static final String G1_YOUNG_GEN_GC = "G1 Young Generation";
    private static final String G1_OLD_GEN_GC = "G1 Old Generation";

    public static String map(String gcName) {
        if (isYoungGenGc(gcName)) {
            return ObjectGeneration.YOUNG.getName();
        }

        if (isOldGenGc(gcName)) {
            return ObjectGeneration.OLD.getName();
        }

        return "Unknown Gen";
    }

    private static boolean isYoungGenGc(String gcName) {
        return
            Arrays.asList(
                SERIAL_YOUNG_GEN_GC,
                PARALLEL_YOUNG_GEN_GC,
                CMS_YOUNG_GEN_GC,
                G1_YOUNG_GEN_GC
            )
                .contains(gcName);
    }

    private static boolean isOldGenGc(String gcName) {
        return
            Arrays.asList(
                SERIAL_OLD_GEN_GC,
                PARALLEL_OLD_GEN_GC,
                CMS_OLD_GEN_GC,
                G1_OLD_GEN_GC
            )
                .contains(gcName);
    }
}
