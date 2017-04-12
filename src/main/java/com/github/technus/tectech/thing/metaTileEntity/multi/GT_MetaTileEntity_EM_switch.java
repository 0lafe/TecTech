package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.dataFramework.quantumDataPacket;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputData;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_OutputData;
import com.github.technus.tectech.vec3pos;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import static com.github.technus.tectech.thing.casing.GT_Container_CasingsTT.sBlockCasingsTT;
import static gregtech.api.enums.GT_Values.V;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_switch extends GT_MetaTileEntity_MultiblockBase_EM {
    public GT_MetaTileEntity_EM_switch(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_switch(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_switch(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[96], new GT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[96]};
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX;
        int yDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetY;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ;
        if (iGregTechTileEntity.getBlockOffset(xDir, yDir, zDir) != sBlockCasingsTT || iGregTechTileEntity.getMetaIDOffset(xDir, yDir, zDir) != 1)
            return false;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int h = -1; h < 2; h++) {
                    if ((i != 0 || j != 0 || h != 0)/*exclude center*/ && (xDir + i != 0 || yDir + h != 0 || zDir + j != 0)/*exclude this*/) {
                        IGregTechTileEntity tTileEntity = iGregTechTileEntity.getIGregTechTileEntityOffset(xDir + i, yDir + h, zDir + j);
                        if (!addEnergyIOToMachineList(tTileEntity, 96) &&
                                (!addDataConnectorToMachineList(tTileEntity,96)) &&
                                (!addMaintenanceToMachineList(tTileEntity,96))) {
                            if (iGregTechTileEntity.getBlockOffset(xDir + i, yDir + h, zDir + j) != sBlockCasingsTT ||
                                    iGregTechTileEntity.getMetaIDOffset(xDir + i, yDir + h, zDir + j) != 3) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean EM_checkRecipe(ItemStack itemStack) {
        short thingsActive=0;
        for (GT_MetaTileEntity_Hatch_InputData di : eInputData)
            if(di.q!=null)
                thingsActive++;

        if(thingsActive>0){
            thingsActive+=eOutputData.size();
            mEUt = -(int) V[7];
            eAmpereFlow = 1 + (thingsActive >> 2);
            mMaxProgresstime = 20;
            mEfficiencyIncrease = 10000;
            return true;
        }
        mMaxProgresstime = 0;
        mEfficiencyIncrease = 0;
        return false;
    }

    @Override
    public void EM_outputFunction() {
        if(eOutputData.size()>0) {
            float total = 0;
            for (int i = 0; i < 10; i++) {//each param pair
                if (eParamsIn[i] > 0 && eParamsIn[i + 10] >= 0)
                    total += eParamsIn[i];//Total weighted div
            }
            total += total / 100F;

            final vec3pos pos = new vec3pos(getBaseMetaTileEntity());
            quantumDataPacket pack = new quantumDataPacket(pos, 0);
            for (GT_MetaTileEntity_Hatch_InputData i : eInputData) {
                if (i.q == null || i.q.contains(pos)) continue;
                pack = pack.unifyPacketWith(i.q);
                if (pack == null) return;
            }

            long remaining=pack.computation;

            for (int i = 0; i < 10; i++) {
                if (eParamsIn[i] > 0) {
                    final int outIndex = (int) (eParamsIn[i + 10]) - 1;
                    if (outIndex < 0 || outIndex > eOutputData.size()) continue;
                    GT_MetaTileEntity_Hatch_OutputData out = eOutputData.get(outIndex);
                    final long part=(long) ((pack.computation * eParamsIn[i]) / total);
                    if(part>0) {
                        remaining-=part;
                        if (remaining > 0)
                            out.q = new quantumDataPacket(pack, part);
                        else if (part + remaining > 0) {
                            out.q = new quantumDataPacket(pack, part + remaining);
                            break;
                        } else break;
                    }
                }
            }
        }
    }

    @Override
    public void EM_checkParams() {
        for (int i = 0; i < 10; i++) {
            if (eParamsIn[i] < 0) eParamsInStatus[i] = PARAM_TOO_LOW;
            else if (eParamsIn[i] == 0) eParamsInStatus[i] = PARAM_UNUSED;
            else if (eParamsIn[i] == Float.POSITIVE_INFINITY) eParamsInStatus[i] = PARAM_TOO_HIGH;
            else eParamsInStatus[i] = PARAM_OK;
        }
        for (int i = 10; i < 20; i++) {
            if (eParamsInStatus[i - 10] == PARAM_OK) {
                if ((int) eParamsIn[i] <= 0) eParamsInStatus[i] = PARAM_LOW;
                else if ((int) eParamsIn[i] > eOutputData.size()) eParamsInStatus[i] = PARAM_TOO_HIGH;
                else eParamsInStatus[i] = PARAM_OK;
            } else {
                eParamsInStatus[i] = PARAM_UNUSED;
            }
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.tecMark,
                "User controlled computation power routing",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Quality of service is a must"
        };
    }
}
