package com.github.itsclairton.simplespawn.command;


import com.github.itsclairton.simplespawn.SimpleSpawn;
import com.github.itsclairton.simplespawn.data.Location;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

import java.util.Objects;

public class SpawnCommand {

    public SpawnCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("spawn").executes(this::onCommand));
    }

    public int onCommand(CommandContext<CommandSource> ctx) {

        if (ctx.getSource().getEntity() == null) {
            return 0;
        }

        ServerWorld world = SimpleSpawn.getInstance().getWorld(ctx.getSource().getServer());
        Location loc = SimpleSpawn.getInstance().getLocation(ctx.getSource().getServer());
        Entity entity = ctx.getSource().getEntity();

        if (world == null || loc == null) {
            entity.sendMessage(new StringTextComponent(SimpleSpawn.getInstance().getConfig().getNoSpawnMessage().replaceAll("&", "§")), entity.getUUID());
            return 0;
        }

        Objects.requireNonNull(ctx.getSource().getServer().getPlayerList().getPlayer(entity.getUUID())).teleportTo(world, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        entity.sendMessage(new StringTextComponent(SimpleSpawn.getInstance().getConfig().getSpawnMessage().replaceAll("&", "§")), ctx.getSource().getEntity().getUUID());
        return 0;
    }

}
