package net.tpcraft.minecraft.util;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapList {
    public static Map<?, ?> search(List<Map<?, ?>> mapList, String key, String value) {
        for (Map<?, ?> map : mapList) {
            if (map.containsKey(key) && Objects.equals(map.get(key), value)) {
                return map;
            }
        }
        return null;
    }

    public static List<Map<?, ?>> save(List<Map<?, ?>> mapList, Map<?, ?> map, String key, String value) {
        boolean keyValuePairExists = mapList.stream().anyMatch(m -> value.equals(m.get(key)));

        if (keyValuePairExists) {
            mapList.removeIf(m -> value.equals(m.get(key)));
        }

        mapList.add(map);

        return mapList;
    }
}
