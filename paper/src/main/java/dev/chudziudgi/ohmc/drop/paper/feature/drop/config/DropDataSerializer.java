package dev.chudziudgi.ohmc.drop.paper.feature.drop.config;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class DropDataSerializer implements ObjectSerializer<DropData> {

    @Override
    public boolean supports(Class<? super DropData> type) {
        return DropData.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(DropData data, SerializationData serializationData, GenericsDeclaration generics) {
        if (data.sourceBlock != null) {
            serializationData.add("sourceBlock", data.sourceBlock, Material.class);
        }
        serializationData.add("dropItem", data.dropItem, Material.class);
        serializationData.add("minAmount", data.minAmount);
        serializationData.add("maxAmount", data.maxAmount);
        serializationData.add("chance", data.chance);
        serializationData.add("minExp", data.minExp);
        serializationData.add("maxExp", data.maxExp);
        serializationData.add("displayName", data.displayName);
        serializationData.addCollection("lore", data.lore, String.class);
        serializationData.addCollection("itemModel", data.itemModel, String.class);
    }

    @Override
    public DropData deserialize(DeserializationData data, GenericsDeclaration generics) {
        Material sourceBlock = data.containsKey("sourceBlock") ? data.get("sourceBlock", Material.class) : null;
        Material dropItem = data.get("dropItem", Material.class);
        int minAmount = data.get("minAmount", Integer.class);
        int maxAmount = data.get("maxAmount", Integer.class);
        double chance = data.get("chance", Double.class);
        int minExp = data.containsKey("minExp") ? data.get("minExp", Integer.class) : 0;
        int maxExp = data.containsKey("maxExp") ? data.get("maxExp", Integer.class) : 0;
        String displayName = data.containsKey("displayName") ? data.get("displayName", String.class) : "";
        List<String> lore = data.containsKey("lore") ? data.getAsList("lore", String.class) : new ArrayList<>();
        List<String> itemModel = data.containsKey("itemModel") ? data.getAsList("itemModel", String.class) : new ArrayList<>();

        return new DropData(sourceBlock, dropItem, minAmount, maxAmount, chance, minExp, maxExp, displayName, lore, itemModel);
    }
}
