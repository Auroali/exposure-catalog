package com.auroali.exposurecatalog.client.widgets;

import com.auroali.exposurecatalog.client.screens.CatalogScreen;
import com.auroali.exposurecatalog.common.catalog.CatalogEntry;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class CatalogWidget extends AbstractWidget {
    private final CatalogEntry entry;
    private final Entity entity;
    private int ticks;
    private boolean unlocked;
    private final int initialX;
    private final int initialY;

    public CatalogWidget(CatalogEntry entry, Entity entity, int initialX, int initialY) {
        super(initialX, initialY, 32, 32, Component.empty());
        this.entry = entry;
        this.entity = entity;
        this.initialX = initialX;
        this.initialY = initialY;
    }

    public static CatalogWidget fromEntry(CatalogEntry entry, int x, int y, boolean unlocked) {
        Entity entity = entry.entity().create(Minecraft.getInstance().level);
        CatalogWidget widget = new CatalogWidget(entry, entity, x, y);
        widget.setX(x);
        widget.setY(y);
        String translationKey = entry.entity().getDescriptionId();
        widget.setUnlocked(unlocked);
        if (widget.isUnlocked())
            widget.setTooltip(Tooltip.create(Component.translatable(translationKey)));
        else
            widget.setTooltip(Tooltip.create(Component.translatable("gui.exposurecatalog.locked")));
        return widget;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public boolean isUnlocked() {
        return this.unlocked;
    }

    public int getInitialX() {
        return this.initialX;
    }

    public int getInitialY() {
        return this.initialY;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    protected Vec3 getScaleForWidget() {
        Vec3 scaleFactor = this.entry.guiScale();
        float height = this.entity.getBbHeight();
        float width = this.entity.getBbWidth();
        double newScale = Math.min(1.d / height, 1.d / width);
        return scaleFactor.multiply(newScale, newScale, newScale);
    }

    protected Vec3 getOffsetForWidget(int width, int height) {
        Vec3 offset = this.entry.guiOffset();
        return offset.add(width / 2.d, height, 0);
    }

    public Component getDescription() {
        return this.entry.text();
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        guiGraphics.blit(CatalogScreen.TEXTURES, this.getX(), this.getY(), this.isUnlocked() ? 0 : 32, 154, 32, 32);
        if (!this.isUnlocked())
            return;

        float rotation = this.getRotation(this.ticks + delta);
        guiGraphics.enableScissor(this.getX() + 5, this.getY() + 5, this.getX() + this.width - 5, this.getY() + this.height - 5);
        this.renderCatalogEntity(guiGraphics, this.getX(), this.getY(), this.width, this.height - 5, 22.f, rotation);
        guiGraphics.disableScissor();
    }

    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent event) {
        if (!this.active) {
            return null;
        } else {
            return !this.isFocused() ? ComponentPath.leaf(this) : null;
        }
    }

    public void tick() {
        this.ticks++;
    }

    public float getRotation(float time) {
        return time / 20.f;
    }

    public void renderCatalogEntity(GuiGraphics guiGraphics, int x, int y, int width, int height, float scale, float angle) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();

        guiGraphics.pose().pushPose();

        Quaternionf rotation = new Quaternionf()
          .rotateZ((float) Math.PI)
          .rotateY(angle);

        Vec3 guiOffset = this.getOffsetForWidget(width, height);
        Vec3 guiScale = this.getScaleForWidget();

        guiGraphics.pose().translate(x + guiOffset.x(), y + guiOffset.y(), guiOffset.z() + 50);
        guiGraphics.pose().mulPoseMatrix(new Matrix4f().scaling(scale, scale, -scale));
        guiGraphics.pose().mulPose(rotation);
        guiGraphics.pose().scale((float) guiScale.x(), (float) guiScale.y(), (float) guiScale.z());
        Lighting.setupForEntityInInventory();

        dispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() ->
          dispatcher.render(this.entity, 0.d, 0.d, 0.d, 0.f, 1.f, guiGraphics.pose(), guiGraphics.bufferSource(), LightTexture.FULL_BRIGHT)
        );
        guiGraphics.flush();
        dispatcher.setRenderShadow(true);

        guiGraphics.pose().popPose();

        Lighting.setupFor3DItems();
    }
}
