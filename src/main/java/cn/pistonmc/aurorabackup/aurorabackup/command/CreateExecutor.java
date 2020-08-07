package cn.pistonmc.aurorabackup.aurorabackup.command;

import cn.pistonmc.aurorabackup.aurorabackup.AuroraBackup;
import cn.pistonmc.aurorabackup.aurorabackup.utils.Utils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.IOException;

public class CreateExecutor implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        src.sendMessage(Text.of(AuroraBackup.INSTANCE.MessageNode.getNode("Message","Create","Begin").getString()));
        String WorldName = args.<String> getOne("WorldName").orElse(null);
        if(WorldName == null){
            WorldName = Sponge.getServer().getDefaultWorldName();
        }
        Utils.CreateWorldBackup(WorldName);
        return CommandResult.success();
    }

    public  static CommandSpec build(){
        return CommandSpec.builder()
                .permission("aurorabackup.base.create")
                .description(TextSerializers.FORMATTING_CODE.deserialize("&b" + AuroraBackup.INSTANCE.MessageNode.getNode("Message","CommandDescription","Create").getString()))
                .arguments(GenericArguments.seq(GenericArguments.optional(GenericArguments.string(Text.of("WorldName")))))
                .executor(new CreateExecutor())
                .build();
    }
}
