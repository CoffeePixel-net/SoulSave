package me.cheese.customdisc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

public class CustomDiscPlugin extends JavaPlugin {

    private static CustomDiscPlugin instance;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        config = getConfig();

        // Listeners
        Bukkit.getPluginManager().registerEvents(new SwordAbilityListener(), this);
        Bukkit.getPluginManager().registerEvents(new WitherKillDropListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ReachDiscJukeboxListener(this), this);

        // Command
        if (getCommand("givedisc") != null) {
            Objects.requireNonNull(getCommand("givedisc"))
                    .setExecutor(new GiveDiscCommand(this));
        }

        // Recipe
        registerSoulSaveRecipe();

        getLogger().info("CustomDiscPlugin enabled with config-based model data!");
    }

    public static CustomDiscPlugin getInstance() {
        return instance;
    }

    /**
     * Creates the Reach music disc item using config:
     * discs.reach.name
     * discs.reach.base-material
     * discs.reach.model-data
     * discs.reach.lore
     */
    public ItemStack createReachDisc() {
        String path = "discs.reach.";
        String matName = config.getString(path + "base-material", "MUSIC_DISC_CAT");
        Material mat = Material.matchMaterial(matName);
        if (mat == null) {
            mat = Material.MUSIC_DISC_CAT;
        }

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String name = config.getString(path + "name", "&bMusic Disc - Reach");
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    Objects.requireNonNull(name)));

            // model-data in config is stored as 3001.0 – getInt() safely handles it
            meta.setCustomModelData(config.getInt(path + "model-data"));

            List<String> lore = config.getStringList(path + "lore");
            lore.replaceAll(line -> ChatColor.translateAlternateColorCodes('&', line));
            meta.setLore(lore);

            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Creates the SoulSave sword item using config:
     * swords.soulsave.*
     */
    public ItemStack createSoulSaveSword() {
        String path = "swords.soulsave.";
        String matName = config.getString(path + "base-material", "GOLDEN_SWORD");
        Material mat = Material.matchMaterial(matName);
        if (mat == null) {
            mat = Material.GOLDEN_SWORD;
        }

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String name = config.getString(path + "name", "&5SoulSave");
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    Objects.requireNonNull(name)));

            meta.setCustomModelData(config.getInt(path + "model-data"));

            List<String> lore = config.getStringList(path + "lore");
            int cooldown = config.getInt(path + "cooldown-seconds", 15);
            lore.replaceAll(line -> ChatColor.translateAlternateColorCodes(
                    '&',
                    line.replace("%cooldown%", String.valueOf(cooldown))
            ));
            meta.setLore(lore);
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);

            item.setItemMeta(meta);
        }
        return item;
    }

    public int getSoulSaveCooldown() {
        return getConfig().getInt("swords.soulsave.cooldown-seconds", 15);
    }

    /**
     * Crafting:
     *  D
     * N J N
     *  S
     * D = NETHER_STAR
     * J = JUKEBOX
     * N = NETHERITE_INGOT
     * S = DIAMOND_SWORD
     */
    private void registerSoulSaveRecipe() {
        ItemStack result = createSoulSaveSword();
        NamespacedKey key = new NamespacedKey(this, "soulsave_sword");

        // Use exact Reach disc as ingredient
        ItemStack reachDisc = createReachDisc(); // this gives us the custom CMD 3001 disc

        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(" D ", "NJN", "FSF");

        recipe.setIngredient('D', new RecipeChoice.ExactChoice(reachDisc));
        recipe.setIngredient('F', Material.NETHER_STAR);// <-- FIXED
        recipe.setIngredient('J', Material.JUKEBOX);
        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        recipe.setIngredient('S', Material.DIAMOND_SWORD);

        Bukkit.addRecipe(recipe);
    }
}
