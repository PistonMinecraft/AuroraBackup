package cn.pistonmc.aurorabackup.aurorabackup.command;

import cn.pistonmc.aurorabackup.aurorabackup.AuroraBackup;
import cn.pistonmc.aurorabackup.aurorabackup.utils.Config;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.concurrent.TimeUnit;

public class TimeExecutor implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        int Time = args.<Integer> getOne(Text.of("Time")).orElse( 0);
        if(Time < 1){
            src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize("&c" + AuroraBackup.INSTANCE.MessageNode.getNode("Message","Time","Error").getString())));
        }else{
            AuroraBackup.INSTANCE.ConfigNode.getNode("AuroraBackup","Time").setValue(Time);
            Config.Save();
            AuroraBackup.INSTANCE.WorldBackupTask.cancel();
            AuroraBackup.INSTANCE.WorldBackupTask =  AuroraBackup.WorldBackupTaskParameter.submit(AuroraBackup.INSTANCE.getPluginContainer());
            src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize("&a" + AuroraBackup.INSTANCE.MessageNode.getNode("Message","Time","Success").getString())));
        }
        return CommandResult.success();
    }

    public  static CommandSpec build(){
        return CommandSpec.builder()
                .permission("aurorabackup.base.manager")
                .description(TextSerializers.FORMATTING_CODE.deserialize("&b" + AuroraBackup.INSTANCE.MessageNode.getNode("Message","CommandDescription","Time").getString()))
                .arguments(GenericArguments.seq(GenericArguments.integer(Text.of("Time"))))
                .executor(new TimeExecutor())
                .build();
    }
}
