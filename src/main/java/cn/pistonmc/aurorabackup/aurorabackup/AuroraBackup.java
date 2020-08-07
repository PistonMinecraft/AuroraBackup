package cn.pistonmc.aurorabackup.aurorabackup;

import cn.pistonmc.aurorabackup.aurorabackup.command.Commands;
import cn.pistonmc.aurorabackup.aurorabackup.utils.Config;
import cn.pistonmc.aurorabackup.aurorabackup.utils.Utils;
import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Plugin(
        id = "aurorabackup",
        name = "AuroraBackup",
        description = "This plugin can backup the server file and world",
        authors = {
                "PistonMC"
        }
)
public class AuroraBackup {
    @Inject
    private Logger logger;
    @Inject
    private PluginContainer AuroraBackupPlugin;
    @Inject
    @DefaultConfig(sharedRoot = false)
    public Path ConfigPath;

    private long StartTime;
    public ConfigurationNode ConfigNode,MessageNode;
    public static  AuroraBackup INSTANCE;
    public Task WorldBackupTask;
    public static Task.Builder  WorldBackupTaskParameter;

    @Listener
    public void onPluginInitialization(GameInitializationEvent event){
        StartTime = System.currentTimeMillis();
        //初始化实例
        INSTANCE = this;
        //初始化设置文件以及本地化文件
        logger.info("Try loading configuration files and localization files...");
        Config.Init();
        logger.info(MessageNode.getNode("Message","Initialization","Begin","a").getString() + AuroraBackupPlugin.getVersion().get() + MessageNode.getNode("Message","Initialization","Begin","b").getString());
        //注册命令
        Sponge.getCommandManager().register(AuroraBackupPlugin, Commands.build(),"aurorabackup","backup");
    }

    @Listener
    public  void onLoadComplete(GameLoadCompleteEvent event) {
        logger.info(MessageNode.getNode("Message","Initialization","Finish").getString() + (System.currentTimeMillis() - StartTime) + "ms");
    }

    @Listener
    public void onServerStaring(GameStartingServerEvent event){
        WorldBackupTaskParameter = Task.builder()
                .execute(() -> {
                    Utils.CreateWorldBackup(Sponge.getServer().getDefaultWorldName());
                })
                .async()
                .delay(500, TimeUnit.MILLISECONDS)
                .interval(ConfigNode.getNode("AuroraBackup","Time").getInt(),TimeUnit.SECONDS)
                .name("AuroraBackup-WorldBackupTask");
    }

    @Listener
    public void onServerStarted(GameStartedServerEvent event){
        if(ConfigNode.getNode("AuroraBackup","AutoBackup").getBoolean()){
            //世界备份
            WorldBackupTask = WorldBackupTaskParameter.submit(AuroraBackupPlugin);
        }
    }

    @Listener
    public void onServerStopped(GameStoppedServerEvent event){
        if(ConfigNode.getNode("AuroraBackup","AutoBackup").getBoolean()){
            WorldBackupTask.cancel();
        }
    }


    public Logger getLogger(){
        return logger;
    }
    public  PluginContainer getPluginContainer(){
        return AuroraBackupPlugin;
    }
}
