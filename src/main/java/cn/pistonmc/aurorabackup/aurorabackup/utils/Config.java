package cn.pistonmc.aurorabackup.aurorabackup.utils;

import cn.pistonmc.aurorabackup.aurorabackup.AuroraBackup;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    private static Logger logger = AuroraBackup.INSTANCE.getLogger();
    private static Path ConfigFilePath = Paths.get(AuroraBackup.INSTANCE.ConfigPath.getParent().toString() ,"/aurorabackup.conf");
    private static  ConfigurationLoader<CommentedConfigurationNode> loader;

    public static  void Init(){
        if(!AuroraBackup.INSTANCE.ConfigPath.getParent().toFile().exists()){
            AuroraBackup.INSTANCE.ConfigPath.getParent().toFile().mkdir();
        }
        loader = HoconConfigurationLoader.builder().setPath(ConfigFilePath).build();
        try {
            Sponge.getAssetManager().getAsset(AuroraBackup.INSTANCE.getPluginContainer(),"aurorabackup.conf").get().copyToFile(ConfigFilePath, false, true);
            AuroraBackup.INSTANCE.ConfigNode = loader.load();
        } catch (IOException e) {
            logger.warn("Configuration file error or does not exist, trying to generate default Settings...");
            try {
                Sponge.getAssetManager().getAsset(AuroraBackup.INSTANCE.getPluginContainer(),"aurorabackup.conf").get().copyToFile(ConfigFilePath, true, true);
                AuroraBackup.INSTANCE.ConfigNode = loader.load();
            } catch (IOException ex) {
                logger.error("Unable to load configuration file to ensure server security. The server has been stopped automatically! Error message:\n" + ex.toString());
                Sponge.getServer().shutdown();
            }
        }

        String Lang = AuroraBackup.INSTANCE.ConfigNode.getNode("AuroraBackup","Lang").getString();
        Path MessagePath = Paths.get(AuroraBackup.INSTANCE.ConfigPath.getParent().toString() ,"/message_" + Lang + ".conf");
        loader = HoconConfigurationLoader.builder().setPath(MessagePath).build();
        try {
            Sponge.getAssetManager().getAsset(AuroraBackup.INSTANCE.getPluginContainer(),"message_" + Lang + ".conf").get().copyToFile(MessagePath, false, true);
            AuroraBackup.INSTANCE.MessageNode = loader.load();
        } catch (IOException e) {
            logger.warn("Configuration file error or does not exist, trying to generate default Settings...");
            try {
                Sponge.getAssetManager().getAsset(AuroraBackup.INSTANCE.getPluginContainer(),"message_" + Lang + ".conf").get().copyToFile(MessagePath, true, true);
                AuroraBackup.INSTANCE.MessageNode = loader.load();
            } catch (IOException ex) {
                logger.error("Unable to load configuration file to ensure server security. The server has been stopped automatically! Error message:\n" + ex.toString());
                Sponge.getServer().shutdown();
            }
        }
    }

    public static void Save(){
        try {
            loader = HoconConfigurationLoader.builder().setPath(ConfigFilePath).build();
            loader.save(AuroraBackup.INSTANCE.ConfigNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
