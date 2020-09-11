package com.dasheightmate.flaminhot.compat.rei.itemmod;

import com.dasheightmate.flaminhot.FlaminHot;
import com.dasheightmate.flaminhot.compat.rei.FlaminHotReiPlugin;
import com.dasheightmate.flaminhot.components.FlammabilityInfo;
import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Tooltip;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemModRecipeCategory implements RecipeCategory<ItemModRecipeDisplay> {
    @Override
    public @NotNull Identifier getIdentifier() {
        return FlaminHotReiPlugin.REI_ITEMMOD_IDENTIFIER;
    }

    @Override
    public @NotNull String getCategoryName() {
        return I18n.translate("compat.rei.flaminhot.item_mod_name");
    }

    @Override
    public @NotNull EntryStack getLogo() {
        return EntryStack.create(Items.BUCKET);
    }

    @Override
    public @NotNull List<Widget> setupDisplay(ItemModRecipeDisplay recipeDisplay, Rectangle bounds) {
        CompoundTag tag = recipeDisplay.getResultingNBT();
        FlammabilityInfo info = new FlammabilityInfo(tag.getBoolean("flamin_infiniburn"), tag.getBoolean("flamin_explosive"),
                tag.contains("flamin_fireproofing") ? tag.getInt("flamin_fireproofing") : 3);
        Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 27);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 18)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y + 19)));

        int i;
        for(i = 0; i < 3; i++) {
            for (int x = 0; x < 3; x++) {
                if (i == 1 && x == 1){
                    widgets.add(Widgets.createSlot(new Point(startPoint.x + 1 + x * 18, startPoint.y + 1 + i * 18))
                            .markInput().entries(recipeDisplay.getInputEntries().get(0)));
                } else {
                    widgets.add(Widgets.createSlotBackground(new Point(startPoint.x + 1 + x * 18, startPoint.y + 1 + i * 18)));
                    int finalX = x;
                    int finalI = i;
                    widgets.add(Widgets.createDrawableWidget((drawableHelper, matrixStack, mouseX, mouseY, delta) -> {
                        Rectangle spriteBounds = new Rectangle(startPoint.x + 1 + finalX * 18,
                                startPoint.y + 1 + finalI * 18, 16, 16);
                        MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier(FlaminHot.MOD_ID, "textures/gui/any-block.png"));
                        DrawableHelper.drawTexture(matrixStack, spriteBounds.x, spriteBounds.y, 0, 0,
                                16, 16, 16, 16);
                        if (spriteBounds.contains(mouseX, mouseY)){
                            Tooltip.create(info.addTooltip(new ArrayList<>(Arrays.asList(new LiteralText("[Any block!]")
                                    .formatted(Formatting.ITALIC))), true)).queue();
                        }
                    }));
                }
            }
        }
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 19))
                .entries(EntryStack.ofItems(Collections.singleton(Items.BUCKET))).disableBackground().markOutput());
        return widgets;
    }
}
