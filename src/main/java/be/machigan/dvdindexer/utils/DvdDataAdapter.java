package be.machigan.dvdindexer.utils;

import be.machigan.dvdindexer.services.dvdinterface.DvdData;
import com.google.gson.*;

import java.lang.reflect.Type;

public class DvdDataAdapter<T extends DvdData> implements JsonSerializer<DvdData>, JsonDeserializer<DvdData> {
    @Override
    public JsonElement serialize(DvdData movieDvdData, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", movieDvdData.getClass().getPackageName() + "." + movieDvdData.getClass().getSimpleName());
        jsonObject.add("data", jsonSerializationContext.serialize(movieDvdData));
        return jsonObject;
    }

    @Override
    public T deserialize(JsonElement jsonElement, Type typeOf, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement data = jsonObject.get("data");
        try {
           return jsonDeserializationContext.deserialize(data, Class.forName( type));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown element type " + type);
        }
    }
}
