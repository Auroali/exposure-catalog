package com.auroali.exposurecatalog.common.commands;

import com.auroali.exposurecatalog.common.catalog.CatalogEntry;
import com.auroali.exposurecatalog.common.components.CatalogTrackerComponent;
import com.auroali.exposurecatalog.common.components.ECEntityComponents;
import com.auroali.exposurecatalog.common.network.CatalogToastS2C;
import com.auroali.exposurecatalog.common.registry.ECRegistries;
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

public class AddAllEntriesCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> register(CommandBuildContext context) {
        return Commands.literal("addall")
          .then(Commands.argument("player", EntityArgument.players())
            .executes(ctx -> addEntries(EntityArgument.getPlayers(ctx, "player")))
          );
    }

    public static int addEntries(Collection<ServerPlayer> players) {
        Collection<CatalogEntry> entries = ECRegistries.CATALOG.getEntries();
        for (ServerPlayer player : players) {
            CatalogTrackerComponent catalog = ECEntityComponents.CATALOG_TRACKER.get(player);
            int added = 0;
            for (CatalogEntry entry : entries) {
                if (!catalog.hasCataloguedEntity(entry.entity())) {
                    catalog.addEntityToCatalog(entry.entity());
                    added++;
                }
            }
            if (added > 0) {
                ECEntityComponents.CATALOG_TRACKER.sync(player);
                ServerPlayNetworking.send(player, new CatalogToastS2C(added));
            }
        }
        return 0;
    }
}
