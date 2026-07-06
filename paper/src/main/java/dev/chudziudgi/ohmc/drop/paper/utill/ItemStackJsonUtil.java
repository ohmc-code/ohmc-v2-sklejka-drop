package dev.chudziudgi.ohmc.drop.paper.utill;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import de.eldoria.jacksonbukkit.deserializer.PaperItemStackDeserializer;
import de.eldoria.jacksonbukkit.serializer.PaperItemStackSerializer;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemStackJsonUtil {
    private static final ObjectMapper MAPPER;
    static {
        MAPPER = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(ItemStack.class, new PaperItemStackSerializer());
        module.addDeserializer(ItemStack.class, new PaperItemStackDeserializer());
        MAPPER.registerModule(module);
    }

    public static String serializeItem(ItemStack item) {
        try {
            return MAPPER.writeValueAsString(item);
        } catch (Exception e) {
            throw new RuntimeException("Nie można zserializować ItemStack do JSON", e);
        }
    }

    public static String serializeItemList(List<ItemStack> items) {
        try {
            return MAPPER.writeValueAsString(items);
        } catch (Exception e) {
            throw new RuntimeException("Nie można zserializować listy ItemStack do JSON", e);
        }
    }

    public static ItemStack deserializeItem(String json) {
        try {
            return MAPPER.readValue(json, ItemStack.class);
        } catch (Exception e) {
            throw new RuntimeException("Nie można zdeserializować JSON do ItemStack", e);
        }
    }

    public static List<ItemStack> deserializeItemList(String json) {
        try {
            CollectionType type = MAPPER.getTypeFactory().constructCollectionType(List.class, ItemStack.class);
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("Nie można zdeserializować JSON do listy ItemStack", e);
        }
    }
}
