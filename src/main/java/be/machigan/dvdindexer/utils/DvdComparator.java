package be.machigan.dvdindexer.utils;

import be.machigan.dvdindexer.services.dvdinterface.DvdData;

import java.util.Arrays;
import java.util.Comparator;

public class DvdComparator implements Comparator<DvdData> {
    public static final DvdComparator INSTANCE = new DvdComparator();

    @Override
    public int compare(DvdData o1, DvdData o2) {
        if (o1.isAvailable() && o2.isAvailable()) {
            return Long.compare(
                    o1.toFile().lastModified(),
                    o2.toFile().lastModified()
            );
        }
        return o1.toString().compareTo(o2.toString());
    }

    private static int extractDigit(DvdData data) {
        String s = removeExtension(data);
        String[] split = s.split(" ");
        String lastPart = split[split.length - 1];
        try {
            return Integer.parseInt(lastPart);
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }

    private static String removeExtension(DvdData data) {
        final String[] s = {null};
        Arrays.stream(DvdInformations.VALID_EXTENSION).forEach(extension -> {
            if (!extension.startsWith("."))
                extension = "." + extension;
            if (s[0] == null && data.toString().endsWith(extension))
                s[0] = data.toString().split(extension)[0];
        });
        return s[0] == null ? data.toString() : s[0];
    }

    private DvdComparator() {}
}
