package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.GT_Container_CasingsTT;
import com.github.technus.tectech.thing.metaTileEntity.constructableTT;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputElemental;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_OutputElemental;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import static com.github.technus.tectech.Util.StructureBuilder;
import static com.github.technus.tectech.thing.casing.GT_Container_CasingsTT.sBlockCasingsTT;
import static gregtech.api.enums.GT_Values.V;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_junction extends GT_MetaTileEntity_MultiblockBase_EM implements constructableTT {
    //region Structure
    //use multi A energy inputs, use less power the longer it runs
    private static final String[][] shape = new String[][]{
            {"   ", " . ", "   ",},
            {"000", "000", "000",},
            {"!!!", "!0!", "!!!",},
            {"!!!", "!!!", "!!!",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{4};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList", "addElementalToMachineList"};
    private static final byte[] casingTextures = new byte[]{textureOffset, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4};
    //endregion

    public GT_MetaTileEntity_EM_junction(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_junction(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_junction(this.mName);
    }

    @Override
    public boolean EM_checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return EM_StructureCheckAdvanced(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 1, 0);
    }

    @Override
    public void construct(int qty) {
        StructureBuilder(shape, blockType, blockMeta,1, 1, 0, getBaseMetaTileEntity());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.tecMark,
                "Reroutes Matter",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Axis aligned movement!"
        };
    }

    @Override
    public void EM_checkParams() {
        for (int i = 0; i < 10; i++) {
            if ((int) eParamsIn[i] < 0) eParamsInStatus[i] = PARAM_TOO_LOW;
            else if ((int) eParamsIn[i] == 0) eParamsInStatus[i] = PARAM_UNUSED;
            else if ((int) eParamsIn[i] > eInputHatches.size()) eParamsInStatus[i] = PARAM_TOO_HIGH;
            else eParamsInStatus[i] = PARAM_OK;
        }
        for (int i = 10; i < 20; i++) {
            if (eParamsInStatus[i - 10] == PARAM_OK) {
                if ((int) eParamsIn[i] < 0) eParamsInStatus[i] = PARAM_TOO_LOW;
                else if ((int) eParamsIn[i] == 0) eParamsInStatus[i] = PARAM_LOW;
                else if ((int) eParamsIn[i] > eOutputHatches.size()) eParamsInStatus[i] = PARAM_TOO_HIGH;
                else eParamsInStatus[i] = PARAM_OK;
            } else {
                eParamsInStatus[i] = PARAM_UNUSED;
            }
        }
    }

    @Override
    public boolean EM_checkRecipe(ItemStack itemStack) {
        for (GT_MetaTileEntity_Hatch_InputElemental in : eInputHatches)
            if (in.getContainerHandler().hasStacks()) {
                mEUt = -(int) V[8];
                eAmpereFlow = 1 + ((eInputHatches.size() + eOutputHatches.size()) >> 1);
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
        for (int i = 0; i < 10; i++) {
            final int inIndex = (int) (eParamsIn[i]) - 1;
            if (inIndex < 0 || inIndex > eInputHatches.size()) continue;
            final int outIndex = (int) (eParamsIn[i + 10]) - 1;
            GT_MetaTileEntity_Hatch_InputElemental in = eInputHatches.get(inIndex);
            if (outIndex == -1) {//param==0 -> null the content
                cleanHatchContent(in);
            } else {
                if (outIndex < 0 || outIndex > eOutputHatches.size()) continue;
                GT_MetaTileEntity_Hatch_OutputElemental out = eOutputHatches.get(outIndex);
                out.getContainerHandler().putUnifyAll(in.getContainerHandler());
                in.getContainerHandler().clear();
            }
        }
    }
}
