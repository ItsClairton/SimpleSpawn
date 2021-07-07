package com.github.itsclairton.simplespawn.command;

import com.github.itsclairton.simplespawn.SimpleSpawn;
import com.github.itsclairton.simplespawn.data.Location;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.storage.FolderName;

public class SetSpawnCommand {

    public SetSpawnCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("setspawn").requires((commandSource -> commandSource.hasPermission(1))).executes(this::onCommand));
    }

    public int onCommand(CommandContext<CommandSource> ctx) {
        if (ctx.getSource().getEntity() == null) {
            return 0;
        }

        Entity entity = ctx.getSource().getEntity();
        Location loc = new Location(entity.level.dimension().location().toString(), entity.getX(), entity.getY(), entity.getZ(), entity.yRot, entity.xRot);

        SimpleSpawn.getInstance().saveLocation(ctx.getSource().getServer().getWorldPath(new FolderName("spdata")).toFile(), loc);

        entity.sendMessage(new StringTextComponent(SimpleSpawn.getInstance().getConfig().getSetSpawnMessage().replaceAll("&", "§")), entity.getUUID());
        return 0;
    }

}
