package epicsquid.gadgetry.core.gui;

import epicsquid.gadgetry.core.lib.container.ContainerModular;
import epicsquid.gadgetry.core.lib.gui.ElementHorizontalProgressBar;
import epicsquid.gadgetry.core.lib.gui.ElementToggleIOButton;
import epicsquid.gadgetry.core.lib.gui.ElementVerticalProgressBar;
import epicsquid.gadgetry.core.lib.gui.GuiModular;
import epicsquid.gadgetry.core.lib.gui.IGuiFactory;
import epicsquid.gadgetry.core.lib.tile.TileBase;
import epicsquid.gadgetry.core.lib.tile.TileModular;
import epicsquid.gadgetry.core.tile.TileAlloyFurnace;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiFactoryAlloyFurnace implements IGuiFactory {

  @SideOnly(Side.CLIENT)
  @Override
  public Gui constructGui(EntityPlayer player, TileEntity tile) {
    TileModular t = (TileModular) tile;
    return new GuiModular(
        new ContainerModular(t).tryAddSlot(0, 10, 42).tryAddSlot(1, 40, 34).tryAddSlot(2, 60, 34).tryAddSlot(3, 80, 34).tryAddSlot(4, 144, 34, true)
            .initPlayerInventory(player.inventory, 0, 0), 176, 166)
        .addElement(new ElementHorizontalProgressBar(108, 34, 176, 16, 24, 16, 176, 0, ((TileAlloyFurnace) t).progress))
        .addElement(new ElementVerticalProgressBar(10, 25, 208, 0, 16, 14, 224, 0, ((TileAlloyFurnace) t).ticks))
        .addElement(new ElementToggleIOButton(152, 60, (TileModular) t, ((TileAlloyFurnace) t).INVENTORY));
  }

  @Override
  public String getName() {
    return TileBase.getTileName(TileAlloyFurnace.class);
  }

  @Override
  public Container constructContainer(EntityPlayer player, TileEntity tile) {
    return new ContainerModular((TileModular) tile).tryAddSlot(0, 10, 42).tryAddSlot(1, 40, 34).tryAddSlot(2, 60, 34).tryAddSlot(3, 80, 34)
        .tryAddSlot(4, 144, 34, true).initPlayerInventory(player.inventory, 0, 0);
  }

}
