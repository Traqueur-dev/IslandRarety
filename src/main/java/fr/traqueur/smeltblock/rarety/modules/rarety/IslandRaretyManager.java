package fr.traqueur.smeltblock.rarety.modules.rarety;

import com.google.common.collect.Lists;
import fr.traqueur.smeltblock.rarety.api.Plugin;
import fr.traqueur.smeltblock.rarety.api.jsons.DiscUtil;
import fr.traqueur.smeltblock.rarety.api.modules.Saveable;
import fr.traqueur.smeltblock.rarety.api.utils.InventoryUtils;
import fr.traqueur.smeltblock.rarety.api.utils.ItemBuilder;
import fr.traqueur.smeltblock.rarety.modules.profiles.ProfileManager;
import fr.traqueur.smeltblock.rarety.modules.profiles.clazz.Profile;
import fr.traqueur.smeltblock.rarety.modules.rarety.clazz.Config;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IslandRaretyManager extends Saveable {

    private static IslandRaretyManager instance;
    private HeadDatabaseAPI headApi;

    private Config config;
    private HashMap<Player, Integer> pages;
    private HashMap<Player, Profile> viewed;
    private HashMap<String, List<Inventory>> inventories;

    public IslandRaretyManager(Plugin plugin) {
        super(plugin, "IslandRarety");
        instance = this;
        headApi = new HeadDatabaseAPI();
        pages = new HashMap<>();
        viewed = new HashMap<>();
        inventories = new HashMap<>();
        this.config = new Config();
        this.registerCommand(new RaretyCommand());
        this.registerListener(new InventoryListener());
        this.registerListener(new ShopListener());
    }

    public static IslandRaretyManager getInstance() {
        return instance;
    }

    public boolean isRaretyItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR || !item.getItemMeta().hasLore())
            return false;
        for (String s : item.getItemMeta().getLore()) {
            for (String r : config.getRaretyItems()) {
                if (s.contains(r)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isRaretyInventory(InventoryView view) {
        return view.getTitle().contains(config.getRaretyInventoryName());
    }

    public int getPlayerPage(Player player) {
        return pages.get(player);
    }

    public void nextPage(Player player) {
        this.openInv(player, viewed.get(player), this.getPlayerPage(player) + 1);
    }

    public void previousPage(Player player) {
        this.openInv(player, viewed.get(player), this.getPlayerPage(player) - 1);
    }

    public void openInv(Player player, int page) {
        ProfileManager manager = ProfileManager.getInstance();
        this.openInv(player, manager.getProfile(player), page);
    }

    public void setupInv(Profile profile) {
        if (this.inventories.containsKey(profile.getName())) {
            return;
        }

        List<Inventory> inventories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Inventory inv = Bukkit.createInventory(Bukkit.getPlayer(profile.getName()), 54,
                    config.getRaretyInventoryName() + " §e" + (i + 1) + "/5");

            // Line 1
            inv.setItem(0, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).displayname("§1")
                    .flag(ItemFlag.HIDE_ATTRIBUTES).build());
            addBlueGlassPane(inv, 1);
            addBlueGlassPane(inv, 7);

            ItemStack item;
            ItemMeta itemM;
            item = headApi.getItemHead("5650");
            itemM = item.getItemMeta();
            itemM.setDisplayName("§7Informations");
            itemM.setLore(config.getLoreInventory());
            item.setItemMeta(itemM);
            inv.setItem(8, item);

            // Line 2
            addBlueGlassPane(inv, 9);
            addBlueGlassPane(inv, 10);
            addBlueGlassPane(inv, 16);
            addBlueGlassPane(inv, 17);

            // Line 3
            addBlueGlassPane(inv, 18);
            addBlueGlassPane(inv, 26);

            // Line 4
            addBlueGlassPane(inv, 27);
            addBlueGlassPane(inv, 35);

            // Line 5
            addBlueGlassPane(inv, 36);
            addBlueGlassPane(inv, 37);
            addBlueGlassPane(inv, 43);
            addBlueGlassPane(inv, 44);

            // Line 6
            addBlueGlassPane(inv, 46);
            addBlueGlassPane(inv, 47);
            addBlueGlassPane(inv, 51);
            addBlueGlassPane(inv, 52);

            // Pages
            if ((i + 1) != 1) {
                item = headApi.getItemHead("37794");
                itemM = item.getItemMeta();
                itemM.setDisplayName("§7Page précédente");
                item.setItemMeta(itemM);
            } else {
                item = new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).displayname("§1")
                        .flag(ItemFlag.HIDE_ATTRIBUTES).build();
            }
            inv.setItem(45, item);

            if ((i + 1) < 5) {
                item = headApi.getItemHead("37795");
                itemM = item.getItemMeta();
                itemM.setDisplayName("§7Page suivante");
                item.setItemMeta(itemM);
            } else {
                item = new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).displayname("§1")
                        .flag(ItemFlag.HIDE_ATTRIBUTES).build();
            }
            inv.setItem(53, item);

            List<byte[]> itemsBytes = profile.getRaretyItems();
            for (int j = (0 + 32 * ((i + 1) - 1)); j < 160 - (5 - (i + 1)) * 32; j++) {
                if (j < itemsBytes.size()) {
                    item = ItemStack.deserializeBytes(itemsBytes.get(j));
                    InventoryUtils.addItem(inv, item, item.getAmount());
                }
            }


            inventories.add(inv);
        }
        this.inventories.put(profile.getName(), inventories);
    }

    public void saveItems(Profile profile) {
        if (!inventories.containsKey(profile.getName())) {
            return;
        }

        List<byte[]> items = new ArrayList<>();
        List<Inventory> invs = inventories.get(profile.getName());

        for (Inventory inv : invs) {
            List<ItemStack> content = Lists.newArrayList(inv.getContents());
            content.removeIf(i -> !this.isRaretyItem(i));

            for (ItemStack item : content) {
                items.add(item.serializeAsBytes());
            }
        }
        profile.setRaretyItems(items);
    }

    public void openInv(Player player, Profile profile, int page) {
        pages.put(player, page);
        viewed.put(player, profile);
        this.setupInv(profile);
        player.openInventory(this.inventories.get(profile.getName()).get(page - 1));
    }

    public void disconnect(Player player) {
        this.saveItems(ProfileManager.getInstance().getProfile(player));
    }

    private void addBlueGlassPane(Inventory inv, int slot) {
        ItemStack item = new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).displayname("§1")
                .flag(ItemFlag.HIDE_ATTRIBUTES).build();
        inv.setItem(slot, item);
    }

    public HashMap<String, List<Inventory>> getInventories() {
        return inventories;
    }

    public List<Inventory> getInventories(Profile profile) {
        return this.getInventories().getOrDefault(profile.getName(), null);
    }

    public void put(Profile profile, List<Inventory> invs) {
        inventories.put(profile.getName(), invs);
    }

    @Override
    public File getFile() {
        return new File(this.getPlugin().getDataFolder(), "config.json");
    }

    @Override
    public void loadData() {
        String content = DiscUtil.readCatch(getFile());
        if (content != null) {
            this.config = this.getGson().fromJson(content, Config.class);
        }
    }

    @Override
    public void saveData() {
        DiscUtil.writeCatch(getFile(), this.getGson().toJson(config));
    }

}
