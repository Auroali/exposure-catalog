package com.auroali.exposurecatalog.common.commands;

import com.auroali.exposurecatalog.common.components.CatalogTrackerComponent;
import com.auroali.exposurecatalog.common.components.ECEntityComponents;
import com.auroali.exposurecatalog.common.network.CatalogToastS2C;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;

import java.util.Collection;

public class AddEntryCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> register(CommandBuildContext context) {
        return Commands.literal("add")
          .then(Commands.argument("player", EntityArgument.players())
            .then(Commands.argument("entity", ResourceArgument.resource(context, Registries.ENTITY_TYPE))
              .executes(ctx -> addEntry(
                  EntityArgument.getPlayers(ctx, "player"),
                  ResourceArgument.getEntityType(ctx, "entity")
                )
              )
            )
          );
    }

    public static int addEntry(Collection<ServerPlayer> players, Holder.Reference<EntityType<?>> toAdd) {
        for (ServerPlayer player : players) {
            CatalogTrackerComponent catalog = ECEntityComponents.CATALOG_TRACKER.get(player);
            if (!catalog.hasCataloguedEntity(toAdd.value())) {
                catalog.addEntityToCatalog(toAdd.value());
                ECEntityComponents.CATALOG_TRACKER.sync(player);
                ServerPlayNetworking.send(player, new CatalogToastS2C(1));
            }
        }
        return 0;
    }
}
