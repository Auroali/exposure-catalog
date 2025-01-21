package com.auroali.exposurecatalog.common.commands;

import com.auroali.exposurecatalog.common.components.CatalogTrackerComponent;
import com.auroali.exposurecatalog.common.components.ECEntityComponents;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
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
import java.util.Iterator;

public class RemoveEntryCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> register(CommandBuildContext context) {
        return Commands.literal("remove")
          .then(Commands.argument("player", EntityArgument.players())
            .then(Commands.argument("entity", ResourceArgument.resource(context, Registries.ENTITY_TYPE))
              .executes(ctx -> removeEntry(
                  EntityArgument.getPlayers(ctx, "player"),
                  ResourceArgument.getEntityType(ctx, "entity")
                )
              )
            )
            .executes(ctx -> removeAllEntries(EntityArgument.getPlayers(ctx, "player")))
          );
    }

    public static int removeAllEntries(Collection<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            CatalogTrackerComponent catalog = ECEntityComponents.CATALOG_TRACKER.get(player);
            for (Iterator<EntityType<?>> it = catalog.iterator(); it.hasNext(); ) {
                it.next();
                it.remove();
            }
            ECEntityComponents.CATALOG_TRACKER.sync(player);
        }
        return 0;
    }

    public static int removeEntry(Collection<ServerPlayer> players, Holder.Reference<EntityType<?>> toRemove) {
        for (ServerPlayer player : players) {
            CatalogTrackerComponent catalog = ECEntityComponents.CATALOG_TRACKER.get(player);
            if (catalog.removeCataloguedEntity(toRemove.value())) {
                ECEntityComponents.CATALOG_TRACKER.sync(player);
            }
        }
        return 0;
    }
}
