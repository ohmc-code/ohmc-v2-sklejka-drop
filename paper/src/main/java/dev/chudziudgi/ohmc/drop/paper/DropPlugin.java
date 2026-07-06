package dev.chudziudgi.ohmc.drop.paper;

import com.eternalcode.commons.adventure.AdventureLegacyColorPostProcessor;
import com.eternalcode.commons.adventure.AdventureLegacyColorPreProcessor;
import com.eternalcode.commons.bukkit.scheduler.BukkitSchedulerImpl;
import com.eternalcode.commons.scheduler.Scheduler;
import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.resolver.NoticeResolverRegistry;
import com.eternalcode.multification.notice.resolver.actionbar.ActionbarResolver;
import com.eternalcode.multification.notice.resolver.bossbar.BossBarResolver;
import com.eternalcode.multification.notice.resolver.bossbar.BossBarService;
import com.eternalcode.multification.notice.resolver.chat.ChatResolver;
import com.eternalcode.multification.notice.resolver.sound.SoundAdventureResolver;
import com.eternalcode.multification.notice.resolver.title.*;
import com.j256.ormlite.table.TableUtils;
import dev.chudziudgi.ohmc.drop.paper.config.ConfigService;
import dev.chudziudgi.ohmc.drop.paper.config.impl.PluginMessages;
import dev.chudziudgi.ohmc.drop.paper.database.DatabaseConfig;
import dev.chudziudgi.ohmc.drop.paper.database.DatabaseManager;
import dev.chudziudgi.ohmc.drop.paper.feature.crafting.CraftingService;
import dev.chudziudgi.ohmc.drop.paper.feature.crafting.config.CraftingConfig;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.DropCommand;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.DropService;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.config.DropConfig;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.config.DropInventoryConfiguration;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.database.DropRepository;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.database.DropTable;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.inventory.DropInventory;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.listener.DropBlockBreakListener;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.listener.DropConnectionListener;
import dev.chudziudgi.ohmc.drop.paper.litecommands.NoticeHandler;
import dev.chudziudgi.ohmc.drop.paper.litecommands.PermissionHandler;
import dev.chudziudgi.ohmc.drop.paper.litecommands.UsageHandler;
import dev.chudziudgi.ohmc.drop.paper.multification.LobbyMultification;
import dev.chudziudgi.ohmc.drop.paper.reload.ReloadCommand;
import dev.chudziudgi.ohmc.drop.paper.reload.ReloadManager;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.adventure.LiteAdventureExtension;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class DropPlugin extends JavaPlugin {

    public static final MiniMessage MINI_MESSAGE = MiniMessage.builder().postProcessor(new AdventureLegacyColorPostProcessor()).preProcessor(new AdventureLegacyColorPreProcessor()).build();
    public static boolean HAS_PLACEHOLDER_API = false;

    private ConfigService configService;
    private ReloadManager reloadManager;
    private DatabaseManager databaseManager;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        Logger logger = this.getLogger();
        Server server = this.getServer();
        File dataFolder = this.getDataFolder();

        HAS_PLACEHOLDER_API = server.getPluginManager().getPlugin("PlaceholderAPI") != null;

        this.configService = new ConfigService(new NoticeResolverRegistry().registerResolver(new ChatResolver()).registerResolver(new TitleResolver()).registerResolver(new SubtitleResolver()).registerResolver(new TitleWithEmptySubtitleResolver()).registerResolver(new SubtitleWithEmptyTitleResolver()).registerResolver(new TimesResolver()).registerResolver(new TitleHideResolver()).registerResolver(new SoundAdventureResolver()).registerResolver(new ActionbarResolver()).registerResolver(new BossBarResolver(new BossBarService())));

        PluginMessages messages = this.configService.create(PluginMessages.class, new File(dataFolder, "messages.yml"));
        DropConfig dropConfig = this.configService.create(DropConfig.class, new File(dataFolder, "drop.yml"));
        DropInventoryConfiguration dropInventoryConfiguration = this.configService.create(DropInventoryConfiguration.class, new File(dataFolder, "drop-menu.yml"));
        CraftingConfig craftingConfig = this.configService.create(CraftingConfig.class, new File(dataFolder, "crafting.yml"));
        DatabaseConfig databaseConfig = this.configService.create(DatabaseConfig.class, new File(dataFolder, "database.yml"));

        LobbyMultification multification = new LobbyMultification(messages);

        Scheduler scheduler = new BukkitSchedulerImpl(this);

        this.databaseManager = new DatabaseManager(logger, dataFolder, databaseConfig);
        try {
            this.databaseManager.connect();
            TableUtils.createTableIfNotExists(this.databaseManager.connectionSource(), DropTable.class);
            logger.info("Successfully connected to database: " + databaseConfig.databaseType());
        }
        catch (Exception exception) {
            logger.severe("Failed to connect to the database: " + exception.getMessage());
            server.getPluginManager().disablePlugin(this);
            return;
        }

        DropRepository dropRepository = new DropRepository(this.databaseManager, scheduler);
        DropService dropService = new DropService(dropConfig, dropRepository, multification);
        DropInventory dropInventory = new DropInventory(dropInventoryConfiguration, dropService, multification);

        CraftingService craftingService = new CraftingService(craftingConfig, this, logger);
        craftingService.registerRecipes();

        server.getOnlinePlayers().forEach(player -> dropService.loadPlayer(player.getUniqueId()));

        this.reloadManager = new ReloadManager(logger);
        this.reloadManager.register(this.configService);
        this.reloadManager.register(craftingService);

        this.liteCommands = LiteBukkitFactory.builder("ohmc-drop", this, server).extension(new LiteAdventureExtension<>(), extension -> extension.serializer(MINI_MESSAGE))
                .missingPermission(new PermissionHandler(multification)).invalidUsage(new UsageHandler(multification)).result(Notice.class, new NoticeHandler(multification))
                .message(LiteBukkitMessages.PLAYER_ONLY, input -> messages.playerOnly)
                .message(LiteBukkitMessages.OFFLINE_PLAYER_NOT_FOUND, input -> messages.playerNotFound)
                .message(LiteBukkitMessages.PLAYER_NOT_FOUND, input -> messages.playerNotFound)
                .commands(
                        new DropCommand(dropInventory),
                        new ReloadCommand(this.reloadManager, messages)
                )
                .build();

        this.registerListeners(
                new DropBlockBreakListener(dropService),
                new DropConnectionListener(dropService)
        );
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = this.getServer().getPluginManager();
        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

    @Override
    public void onDisable() {
        if (this.liteCommands != null) {
            this.liteCommands.getPlatform().unregisterAll();
            this.liteCommands = null;
        }
        if (this.databaseManager != null) {
            this.databaseManager.close();
            this.databaseManager = null;
        }
    }
}
