package cn.pistonmc.aurorabackup.aurorabackup.command;

import cn.pistonmc.aurorabackup.aurorabackup.AuroraBackup;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;
import java.util.List;

public class Commands  implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        List<Text> Description = new ArrayList<>();
        Description.add(TextSerializers.FORMATTING_CODE.deserialize("&a/aurorabackup create &e-> &f" + AuroraBackup.INSTANCE.MessageNode.getNode("Message","CommandDescription","Create").getString()));
        Description.add(TextSerializers.FORMATTING_CODE.deserialize("&a/aurorabackup reload &e-> &f" + AuroraBackup.INSTANCE.MessageNode.getNode("Message","CommandDescription","Reload").getString()));
        Description.add(TextSerializers.FORMATTING_CODE.deserialize("&a/aurorabackup enable &e-> &f" + AuroraBackup.INSTANCE.MessageNode.getNode("Message","CommandDescription","Enable").getString()));
        Description.add(TextSerializers.FORMATTING_CODE.deserialize("&a/aurorabackup disable &e-> &f" + AuroraBackup.INSTANCE.MessageNode.getNode("Message","CommandDescription","Disable").getString()));
        Description.add(TextSerializers.FORMATTING_CODE.deserialize("&a/aurorabackup time <Time> &e-> &f" + AuroraBackup.INSTANCE.MessageNode.getNode("Message","CommandDescription","Time").getString()));

        PaginationList.builder()
                .padding(TextSerializers.FORMATTING_CODE.deserialize("&2="))
                .title(TextSerializers.FORMATTING_CODE.deserialize("&2AuroraBackup"))
                .contents(Description)
                .sendTo(src);
        return CommandResult.success();
    }

    public  static CommandSpec build(){
        return CommandSpec.builder()
                .permission("aurorabackup.base")
                .executor(new Commands())
                .child(CreateExecutor.build(),"create","new")
                .child(ReloadExecutor.build(),"reload")
                .child(EnableExecutor.build(),"enable","on")
                .child(DisableExecutor.build(),"disable","off")
                .child(TimeExecutor.build(),"time")
                .build();
    }
}
