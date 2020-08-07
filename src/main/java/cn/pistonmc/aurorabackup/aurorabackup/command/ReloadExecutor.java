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

public class ReloadExecutor implements CommandExecutor {
    private static Logger logger = AuroraBackup.INSTANCE.getLogger();

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof ConsoleSource){
            src.sendMessage(Text.of(AuroraBackup.INSTANCE.MessageNode.getNode("Message","Reload","Inform","a").getString()));
            Config.Init();
            src.sendMessage(Text.of(AuroraBackup.INSTANCE.MessageNode.getNode("Message","Reload","Inform","b").getString()));
        }else if(src instanceof Player){
            src.sendMessage(Text.of(AuroraBackup.INSTANCE.MessageNode.getNode("Message","Reload","Inform","a").getString()));
            logger.info(AuroraBackup.INSTANCE.MessageNode.getNode("Message","Reload","Inform","a").getString());
            Config.Init();
            src.sendMessage(Text.of(AuroraBackup.INSTANCE.MessageNode.getNode("Message","Reload","Inform","b").getString()));
            logger.info(AuroraBackup.INSTANCE.MessageNode.getNode("Message","Reload","Inform","b").getString());
        }
        AuroraBackup.INSTANCE.WorldBackupTask.cancel();
        AuroraBackup.INSTANCE.WorldBackupTask =  AuroraBackup.WorldBackupTaskParameter.submit(AuroraBackup.INSTANCE.getPluginContainer());
        return CommandResult.success();
    }

    public  static CommandSpec build(){
        return CommandSpec.builder()
                .permission("aurorabackup.base.reload")
                .description(TextSerializers.FORMATTING_CODE.deserialize("&b" + AuroraBackup.INSTANCE.MessageNode.getNode("Message","CommandDescription","Reload").getString()))
                .executor(new ReloadExecutor())
                .build();
    }
}
