package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.Vec3pos;
import com.github.technus.tectech.mechanics.dataTransport.QuantumDataPacket;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputData;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_OutputData;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Rack;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedTexture;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.Util.StructureBuilderExtreme;
import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.*;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_computer extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    private final ArrayList<GT_MetaTileEntity_Hatch_Rack> eRacks = new ArrayList<>();
    private int maxCurrentTemp = 0;

    //region Structure
    private static final String[][] front = new String[][]{{"A  ", "A  ", "A. ", "A  ",},};
    private static final String[][] terminator = new String[][]{{"A  ", "A  ", "A  ", "A  ",},};
    private static final String[][] cap = new String[][]{{"-01", "A22", "A22", "-01",},};
    private static final String[][] slice = new String[][]{{"-01", "A!2", "A!2", "-01",},};
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{2, 1, 3};
    private static final String[] addingMethods = new String[]{"addToMachineList", "addRackToMachineList"};
    private static final short[] casingTextures = new short[]{textureOffset + 1, textureOffset + 3};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{1, 3};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Classic/Data Hatches or Computer casing",
            "2 - Rack Hatches or Advanced computer casing",
    };
    //endregion

    public GT_MetaTileEntity_EM_computer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        eCertainMode = 5;
        eCertainStatus = -128;//no-brain value
    }

    public GT_MetaTileEntity_EM_computer(String aName) {
        super(aName);
        eCertainMode = 5;
        eCertainStatus = -128;//no-brain value
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected ResourceLocation getActivitySound(){
        return GT_MetaTileEntity_EM_switch.activitySound;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_computer(mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_COMPUTER");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_COMPUTER_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][3], new TT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][3]};
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        eAvailableData = 0;
        maxCurrentTemp = 0;
        double overClockRatio= getParameterIn(0,0);
        double overVoltageRatio= getParameterIn(0,1);
        if(Double.isNaN(overClockRatio) || Double.isNaN(overVoltageRatio)) {
            return false;
        }
        if(overClockRatio>0 && overVoltageRatio>=0.7f && overClockRatio<=3 && overVoltageRatio<=2){
            float eut=V[8] * (float)overVoltageRatio * (float)overClockRatio;
            if(eut<Integer.MAX_VALUE-7) {
                mEUt = -(int) eut;
            } else{
                mEUt = -(int)V[8];
                return false;
            }
            short thingsActive = 0;
            int rackComputation;

            for (GT_MetaTileEntity_Hatch_Rack rack : eRacks) {
                if (!GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(rack)) {
                    continue;
                }
                if (rack.heat > maxCurrentTemp) {
                    maxCurrentTemp = rack.heat;
                }
                rackComputation = rack.tickComponents((float) overClockRatio, (float) overVoltageRatio);
                if (rackComputation > 0) {
                    eAvailableData += rackComputation;
                    thingsActive += 4;
                }
                rack.getBaseMetaTileEntity().setActive(true);
            }

            for (GT_MetaTileEntity_Hatch_InputData di : eInputData) {
                if (di.q != null)//ok for power losses
                {
                    thingsActive++;
                }
            }

            if (thingsActive > 0 && eCertainStatus == 0) {
                thingsActive += eOutputData.size();
                eAmpereFlow = 1 + (thingsActive >> 2);
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                return true;
            } else {
                eAvailableData=0;
                mEUt = -(int)V[8];
                eAmpereFlow = 1;
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                return true;
            }
        }
        return false;
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (!eOutputData.isEmpty()) {
            Vec3pos pos = new Vec3pos(getBaseMetaTileEntity());
            QuantumDataPacket pack = new QuantumDataPacket(eAvailableData / eOutputData.size()).unifyTraceWith(pos);
            if(pack==null){
                return;
            }
            for (GT_MetaTileEntity_Hatch_InputData hatch : eInputData) {
                if (hatch.q == null || hatch.q.contains(pos)) {
                    continue;
                }
                pack = pack.unifyPacketWith(hatch.q);
                if (pack == null) {
                    return;
                }
            }

            for (GT_MetaTileEntity_Hatch_OutputData o : eOutputData) {
                o.q = pack;
            }
        }
    }

    @Override
    protected long getAvailableData_EM() {
        return eAvailableData;
    }

    @Override
    protected void afterRecipeCheckFailed() {
        super.afterRecipeCheckFailed();
        for (GT_MetaTileEntity_Hatch_Rack r : eRacks) {
            r.getBaseMetaTileEntity().setActive(false);
        }
    }

    @Override
    protected void parametersLoadDefault_EM() {
        setParameterPairIn_ClearOut(0, false, 1, 1);
    }

    @Override
    public void parametersOutAndStatusesWrite_EM(boolean machineBusy) {
        double ocRatio = getParameterIn(0, 0);
        if (ocRatio < 0) {
            setStatusOfParameterIn(0, 0, STATUS_TOO_LOW);
        } else if (ocRatio < 1) {
            setStatusOfParameterIn(0, 0, STATUS_LOW);
        } else if (ocRatio == 1) {
            setStatusOfParameterIn(0, 0, STATUS_OK);
        } else if (ocRatio <= 3) {
            setStatusOfParameterIn(0, 0, STATUS_HIGH);
        } else if (Double.isNaN(ocRatio)) {
            setStatusOfParameterIn(0, 0, STATUS_WRONG);
        } else {
            setStatusOfParameterIn(0, 0, STATUS_TOO_HIGH);
        }

        double ovRatio = getParameterIn(0, 1);
        if (ovRatio < 0.7f) {
            setStatusOfParameterIn(0, 1, STATUS_TOO_LOW);
        } else if (ovRatio < 0.8f) {
            setStatusOfParameterIn(0, 1, STATUS_LOW);
        } else if (ovRatio <= 1.2f) {
            setStatusOfParameterIn(0, 1, STATUS_OK);
        } else if (ovRatio <= 2) {
            setStatusOfParameterIn(0, 1, STATUS_HIGH);
        } else if (Double.isNaN(ovRatio)) {
            setStatusOfParameterIn(0, 1, STATUS_WRONG);
        } else {
            setStatusOfParameterIn(0, 1, STATUS_TOO_HIGH);
        }

        setParameterOut(0, 0, maxCurrentTemp);
        setParameterOut(0, 1, eAvailableData);

        if (maxCurrentTemp < -10000) {
            setStatusOfParameterOut(0, 0, STATUS_TOO_LOW);
        } else if (maxCurrentTemp < 0) {
            setStatusOfParameterOut(0, 0, STATUS_LOW);
        } else if (maxCurrentTemp == 0) {
            setStatusOfParameterOut(0, 0, STATUS_OK);
        } else if (maxCurrentTemp <= 5000) {
            setStatusOfParameterOut(0, 0, STATUS_HIGH);
        } else {
            setStatusOfParameterOut(0, 0, STATUS_TOO_HIGH);
        }

        if (!machineBusy) {
            setStatusOfParameterOut(0, 1, STATUS_NEUTRAL);
        } else if (eAvailableData <= 0) {
            setStatusOfParameterOut(0, 1, STATUS_TOO_LOW);
        } else {
            setStatusOfParameterOut(0, 1, STATUS_OK);
        }
    }


    @Override
    public void onRemoval() {
        super.onRemoval();
        for (GT_MetaTileEntity_Hatch_Rack r : eRacks) {
            r.getBaseMetaTileEntity().setActive(false);
        }
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        eAvailableData=0;
        for (GT_MetaTileEntity_Hatch_Rack r : eRacks) {
            r.getBaseMetaTileEntity().setActive(false);
        }
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        for (GT_MetaTileEntity_Hatch_Rack rack : eRacks) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(rack)) {
                rack.getBaseMetaTileEntity().setActive(false);
            }
        }
        eRacks.clear();
        if (!structureCheck_EM(front, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 2, 0)) {
            return false;
        }
        if (!structureCheck_EM(cap, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 2, -1)) {
            return false;
        }
        byte offset = -2, totalLen = 4;
        while (offset > -16) {
            if (!structureCheck_EM(slice, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 2, offset)) {
                break;
            }
            totalLen++;
            offset--;
        }
        if (totalLen > 16) {
            return false;
        }
        if (!structureCheck_EM(cap, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 2, ++offset)) {
            return false;
        }
        if (!structureCheck_EM(terminator, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 2, --offset)) {
            return false;
        }
        eCertainMode = (byte) Math.min(totalLen / 3, 5);
        for (GT_MetaTileEntity_Hatch_Rack rack : eRacks) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(rack)) {
                rack.getBaseMetaTileEntity().setActive(iGregTechTileEntity.isActive());
            }
        }
        return eUncertainHatches.size() == 1;
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        IGregTechTileEntity igt=getBaseMetaTileEntity();
        StructureBuilderExtreme(front, blockType, blockMeta, 1, 2, 0, igt,this,hintsOnly);
        StructureBuilderExtreme(cap, blockType, blockMeta, 1, 2, -1, igt,this,hintsOnly);

        byte offset=-2;
        for (int rackSlices = stackSize >12?12: stackSize; rackSlices>0 ; rackSlices--) {
            StructureBuilderExtreme(slice, blockType, blockMeta, 1, 2, offset--, igt,this,hintsOnly);
        }

        StructureBuilderExtreme(cap, blockType, blockMeta, 1, 2, offset--, igt,this,hintsOnly);
        StructureBuilderExtreme(terminator, blockType, blockMeta, 1, 2, offset,igt,this,hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    protected void extraExplosions_EM() {
        for (MetaTileEntity tTileEntity : eRacks) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                Util.intBitsToString(TecTech.RANDOM.nextInt()),
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "You need it to process the number above"
        };
    }

    //NEW METHOD
    public final boolean addRackToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Rack) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eRacks.add((GT_MetaTileEntity_Hatch_Rack) aMetaTileEntity);
        }
        return false;
    }

    public static void run() {
        try {
            adderMethodMap.put("addRackToMachineList", GT_MetaTileEntity_EM_computer.class.getMethod("addRackToMachineList", IGregTechTileEntity.class, int.class));
        } catch (NoSuchMethodException e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }
    }
}
