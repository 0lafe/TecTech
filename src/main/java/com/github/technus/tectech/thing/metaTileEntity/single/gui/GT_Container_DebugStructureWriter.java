package com.github.technus.tectech.thing.metaTileEntity.single.gui;

import com.github.technus.tectech.thing.metaTileEntity.single.GT_MetaTileEntity_DebugStructureWriter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.Iterator;

public class GT_Container_DebugStructureWriter
        extends GT_ContainerMetaTile_Machine {
    public boolean size = false;
    public short numbers[] = new short[6];

    public GT_Container_DebugStructureWriter(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 8, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 8, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 8, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 8, 59, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 26, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 26, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 26, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 26, 59, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 152, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 152, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 152, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 152, 59, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 134, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 134, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 134, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 134, 59, false, false, 1));
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex < 0) {
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        }
        Slot tSlot = (Slot) this.inventorySlots.get(aSlotIndex);
        if ((tSlot != null) && (this.mTileEntity.getMetaTileEntity() != null)) {
            GT_MetaTileEntity_DebugStructureWriter dsw = (GT_MetaTileEntity_DebugStructureWriter) mTileEntity.getMetaTileEntity();
            if (dsw.numbers == null) return null;
            switch (aSlotIndex) {
                case 0:
                    dsw.numbers[size ? 3 : 0] -= (aShifthold == 1 ? 512 : 64);
                    return null;
                case 1:
                    dsw.numbers[size ? 4 : 1] -= (aShifthold == 1 ? 512 : 64);
                    return null;
                case 2:
                    dsw.numbers[size ? 5 : 2] -= (aShifthold == 1 ? 512 : 64);
                    return null;
                case 4:
                    dsw.numbers[size ? 3 : 0] -= (aShifthold == 1 ? 16 : 1);
                    return null;
                case 5:
                    dsw.numbers[size ? 4 : 1] -= (aShifthold == 1 ? 16 : 1);
                    return null;
                case 6:
                    dsw.numbers[size ? 5 : 2] -= (aShifthold == 1 ? 16 : 1);
                    return null;
                case 8:
                    dsw.numbers[size ? 3 : 0] += (aShifthold == 1 ? 512 : 64);
                    return null;
                case 9:
                    dsw.numbers[size ? 4 : 1] += (aShifthold == 1 ? 512 : 64);
                    return null;
                case 10:
                    dsw.numbers[size ? 5 : 2] += (aShifthold == 1 ? 512 : 64);
                    return null;
                case 12:
                    dsw.numbers[size ? 3 : 0] += (aShifthold == 1 ? 16 : 1);
                    return null;
                case 13:
                    dsw.numbers[size ? 4 : 1] += (aShifthold == 1 ? 16 : 1);
                    return null;
                case 14:
                    dsw.numbers[size ? 5 : 2] += (aShifthold == 1 ? 16 : 1);
                    return null;
                case 3:
                case 7:
                case 11:
                case 15:
                    dsw.size ^= true;
                    return null;
            }
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if ((this.mTileEntity.isClientSide()) || (this.mTileEntity.getMetaTileEntity() == null)) {
            return;
        }
        GT_MetaTileEntity_DebugStructureWriter dsw = (GT_MetaTileEntity_DebugStructureWriter) mTileEntity.getMetaTileEntity();
        if (numbers != null)
            System.arraycopy(dsw.numbers, 0, this.numbers, 0, dsw.numbers.length);
        this.size = dsw.size;

        Iterator var2 = this.crafters.iterator();
        while (var2.hasNext()) {
            ICrafting var1 = (ICrafting) var2.next();
            if (numbers != null) {
                var1.sendProgressBarUpdate(this, 100, this.numbers[0]);
                var1.sendProgressBarUpdate(this, 101, this.numbers[1]);
                var1.sendProgressBarUpdate(this, 102, this.numbers[2]);
                var1.sendProgressBarUpdate(this, 103, this.numbers[3]);
                var1.sendProgressBarUpdate(this, 104, this.numbers[4]);
                var1.sendProgressBarUpdate(this, 105, this.numbers[5]);
            }
            var1.sendProgressBarUpdate(this, 106, this.size ? 1 : 0);
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 106:
                this.size = par2 == 1;
                break;
            default:
                if (numbers != null && par1 >= 100 && par1 <= 105)
                    this.numbers[par1 - 100] = (short) par2;
                break;
        }
    }
}
