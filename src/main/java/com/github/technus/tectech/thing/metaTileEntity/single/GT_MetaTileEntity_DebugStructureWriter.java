package com.github.technus.tectech.thing.metaTileEntity.single;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.elementalMatter.CommonValues;
import com.github.technus.tectech.thing.metaTileEntity.single.gui.GT_Container_DebugStructureWriter;
import com.github.technus.tectech.thing.metaTileEntity.single.gui.GT_GUIContainer_DebugStructureWriter;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static com.github.technus.tectech.Util.ReverseStructureCheck;

/**
 * Created by Tec on 23.03.2017.
 */
public class GT_MetaTileEntity_DebugStructureWriter extends GT_MetaTileEntity_TieredMachineBlock {
    public short numbers[]=new short[6];
    public boolean size=false;
    public String[] result=new String[]{"Undefined"};

    public GT_MetaTileEntity_DebugStructureWriter(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Scans Blocks Around");
    }

    public GT_MetaTileEntity_DebugStructureWriter(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DebugStructureWriter(mName,mTier,mDescription,mTextures);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], (aSide != this.getBaseMetaTileEntity().getFrontFacing()) ? new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TELEPORTER_SIDES) : aActive ? new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE) : new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TELEPORTER)};
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_DebugStructureWriter(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_DebugStructureWriter(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        for(int i=0;i<numbers.length;i++){
            aNBT.setShort("eData"+i,numbers[i]);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        for(int i=0;i<numbers.length;i++){
            numbers[i]=aNBT.getShort("eData"+i);
        }
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if(aBaseMetaTileEntity.isAllowedToWork()){
            result=ReverseStructureCheck(this.getBaseMetaTileEntity(),numbers[0],numbers[1],numbers[2],numbers[3],numbers[4],numbers[5]);
            if(TecTech.ModConfig.DEBUG_MODE)
                for(String s:result)
                    TecTech.Logger.info(s);
            aBaseMetaTileEntity.disableWorking();
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
            aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isElectric() {
        return false;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.tecMark,
                this.mDescription,
                "Prints Multiblock NonTE structure check code",
                "ABC axises aligned to machine front"
        };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return result;
    }
}
