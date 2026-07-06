package dev.chudziudgi.ohmc.drop.paper.feature.drop.config;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class DropData {

    public Material dropItem;
    public int minAmount;
    public int maxAmount;
    public double chance;
    public int minExp;
    public int maxExp;
    public String displayName;
    public List<String> lore;
    public List<String> itemModel;

    public DropData() {
        this.lore = new ArrayList<>();
        this.itemModel = new ArrayList<>();
    }

    public DropData(Material dropItem, int minAmount, int maxAmount, double chance, int minExp, int maxExp,
                    String displayName, List<String> lore, List<String> itemModel) {
        this.dropItem = dropItem;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.chance = chance;
        this.minExp = minExp;
        this.maxExp = maxExp;
        this.displayName = displayName;
        this.lore = new ArrayList<>(lore);
        this.itemModel = new ArrayList<>(itemModel);
    }

    public String displayName() {
        return (this.displayName == null || this.displayName.isBlank())
                ? this.dropItem.name()
                : this.displayName;
    }
}
