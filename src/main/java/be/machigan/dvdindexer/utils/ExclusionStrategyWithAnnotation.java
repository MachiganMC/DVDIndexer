package be.machigan.dvdindexer.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class ExclusionStrategyWithAnnotation implements ExclusionStrategy {
    public static final ExclusionStrategyWithAnnotation INSTANCE = new ExclusionStrategyWithAnnotation();

    @Override
    public boolean shouldSkipField(FieldAttributes field) {
        return field.getAnnotation(ExcludeFromGson.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
