package cn.pistonmc.aurorabackup.aurorabackup.command;

import cn.pistonmc.aurorabackup.aurorabackup.AuroraBackup;
import cn.pistonmc.aurorabackup.aurorabackup.utils.Config;
import org.slf4j.Logger;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.concurrent.TimeUnit;

public class EnableExecutor implements CommandExecutor {
    private static Logger logger = AuroraBackup.INSTANCE.getLogger();

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof ConsoleSource){
            if(!AuroraBackup.INSTANCE.ConfigNode.getNode("AuroraBackup","AutoBackup").getBoolean()){
                AuroraBackup.INSTANCE.WorldBackupTask =  AuroraBackup.WorldBackupTaskParameter.submit(AuroraBackup.INSTANCE.getPluginContainer());
                AuroraBackup.INSTANCE.ConfigNode.getNode("AuroraBackup","AutoBackup").setValue(true);
                Config.Save();
                src.sendMessage(Text.of(AuroraBackup.INSTANCE.MessageNode.getNode("Message","Enable","True").getString()));
            }else{
                src.sendMessage(Text.of(AuroraBackup.INSTANCE.MessageNode.getNode("Message","Enable","False").getString()));
            }
        }else if(src instanceof Player){
            if(!AuroraBackup.INSTANCE.ConfigNode.getNode("AuroraBackup","AutoBackup").getBoolean()){
                AuroraBackup.INSTANCE.WorldBackupTask =  AuroraBackup.WorldBackupTaskParameter.submit(AuroraBackup.INSTANCE.getPluginContainer());
                AuroraBackup.INSTANCE.ConfigNode.getNode("AuroraBackup","AutoBackup").setValue(true);
                Config.Save();
                logger.info(AuroraBackup.INSTANCE.MessageNode.getNode("Message","Enable","Logger").getString() + src.getName());
                src.sendMessage(Text.of(AuroraBackup.INSTANCE.MessageNode.getNode("Message","Enable","True").getString()));
            }else{
                src.sendMessage(Text.of(AuroraBackup.INSTANCE.MessageNode.getNode("Message","Enable","False").getString()));
            }
        }
        return CommandResult.success();
    }

    public  static CommandSpec build(){
        return CommandSpec.builder()
                .permission("aurorabackup.base.enable")
                .description(TextSerializers.FORMATTING_CODE.deserialize("&b" + AuroraBackup.INSTANCE.MessageNode.getNode("Message","CommandDescription","Enable").getString()))
                .executor(new EnableExecutor())
                .build();
    }
}
