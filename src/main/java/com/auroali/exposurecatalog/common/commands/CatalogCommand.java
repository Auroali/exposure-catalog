package com.auroali.exposurecatalog.common.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CatalogCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> register(CommandBuildContext context) {
        return Commands.literal("catalog")
          .requires(ctx -> ctx.hasPermission(2))
          .then(RemoveEntryCommand.register(context))
          .then(AddEntryCommand.register(context))
          .then(AddAllEntriesCommand.register(context));
    }
}
