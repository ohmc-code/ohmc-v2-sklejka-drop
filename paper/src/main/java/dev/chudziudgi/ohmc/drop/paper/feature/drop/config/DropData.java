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

    // Blok źródłowy do którego przypisany jest ten drop.
    // null = drop może wypaść z dowolnego bloku z listy 'blocks'.
    // ustawiony = drop leci wyłącznie z tego konkretnego bloku (np. cobblestone tylko ze STONE).
    public Material sourceBlock;

    public DropData() {
        this.lore = new ArrayList<>();
        this.itemModel = new ArrayList<>();
        this.sourceBlock = null;
    }

    public DropData(Material dropItem, int minAmount, int maxAmount, double chance, int minExp, int maxExp,
                    String displayName, List<String> lore, List<String> itemModel) {
        this(null, dropItem, minAmount, maxAmount, chance, minExp, maxExp, displayName, lore, itemModel);
    }

    public DropData(Material sourceBlock, Material dropItem, int minAmount, int maxAmount, double chance,
                    int minExp, int maxExp, String displayName, List<String> lore, List<String> itemModel) {
        this.sourceBlock = sourceBlock;
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

    public boolean appliesTo(Material block) {
        return this.sourceBlock == null || this.sourceBlock == block;
    }

    public String displayName() {
        return (this.displayName == null || this.displayName.isBlank())
                ? this.dropItem.name()
                : this.displayName;
    }
}
