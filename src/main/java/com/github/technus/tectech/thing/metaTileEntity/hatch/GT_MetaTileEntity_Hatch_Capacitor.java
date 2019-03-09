package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.Reference;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_Container_Capacitor;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_GUIContainer_Capacitor;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.Map;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.Util.getUniqueIdentifier;
import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;

/**
 * Created by Tec on 03.04.2017.
 */
public class GT_MetaTileEntity_Hatch_Capacitor extends GT_MetaTileEntity_Hatch {
    private static Textures.BlockIcons.CustomIcon EM_H;
    private static Textures.BlockIcons.CustomIcon EM_H_ACTIVE;
    private static Map<String, GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent> componentBinds = new HashMap<>();
    private float energyStoredFrac = 0;

    public GT_MetaTileEntity_Hatch_Capacitor(int aID, String aName, String aNameRegional, int aTier, String descr) {
        super(aID, aName, aNameRegional, aTier, 1, descr);
        Util.setTier(aTier,this);
    }

    public GT_MetaTileEntity_Hatch_Capacitor(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        EM_H_ACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/EM_HOLDER_ACTIVE");
        EM_H = new Textures.BlockIcons.CustomIcon("iconsets/EM_HOLDER");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(EM_H_ACTIVE)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(EM_H)};
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Capacitor(mName, mTier, mDescription, mTextures);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return aFacing >= 2;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aSide == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aSide == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_Capacitor(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_Capacitor(aPlayerInventory, aBaseMetaTileEntity, "Capacitor Hatch");
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public int getSizeInventory() {
        return energyStoredFrac >= 0.25 || getBaseMetaTileEntity().isActive() ? 0 : mInventory.length;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.BASS_MARK,
                mDescription,
                EnumChatFormatting.AQUA + "Stores 'nergy! (for a while)"
        };
    }

    public static void run() {
        new GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent(Reference.MODID+":tm.teslaCoilCapacitor.0", 0, 1, 1024, 1);//LV Capacitor
        new GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent(Reference.MODID+":tm.teslaCoilCapacitor.1", 1, 1, 1024, 1);//MV Capacitor
        new GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent(Reference.MODID+":tm.teslaCoilCapacitor.2", 2, 1, 1024, 1);//HV Capacitor
        new GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent(Reference.MODID+":tm.teslaCoilCapacitor.3", 3, 1, 1024, 1);//EV Capacitor
        new GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent(Reference.MODID+":tm.teslaCoilCapacitor.4", 4, 1, 1024, 1);//IV Capacitor
        new GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent(Reference.MODID+":tm.teslaCoilCapacitor.5", 5, 1, 1024, 1);//LuV Capacitor
        new GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent(Reference.MODID+":tm.teslaCoilCapacitor.6", 6, 1, 1024, 1);//ZPM Capacitor
        new GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent(Reference.MODID+":tm.teslaCoilCapacitor.7", 7, 1, 1024, 1);//UV Capacitor
    }

    public static class CapacitorComponent implements Comparable<GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent> {
        private final String unlocalizedName;
        private final long tier, current, energyMax;
        private final float efficiency;

        CapacitorComponent(ItemStack is, long tier, long current, long energyMax, float efficiency) {
            this(getUniqueIdentifier(is), tier, current, energyMax, efficiency);
        }

        CapacitorComponent(String is, long tier, long current, long energyMax, float efficiency) {
            unlocalizedName = is;
            this.tier = tier;
            this.current = current;
            this.energyMax = energyMax;
            this.efficiency = efficiency;
            componentBinds.put(unlocalizedName, this);
            if (DEBUG_MODE) {
                TecTech.LOGGER.info("Tesla Capacitor registered: " + unlocalizedName);
            }
        }

        @Override
        public int compareTo(GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent o) {
            return unlocalizedName.compareTo(o.unlocalizedName);
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent) {
                return compareTo((GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent) obj) == 0;
            }
            return false;
        }
    }
}


