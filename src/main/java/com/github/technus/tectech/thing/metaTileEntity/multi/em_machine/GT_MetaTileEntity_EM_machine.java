package com.github.technus.tectech.thing.metaTileEntity.multi.em_machine;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.block.QuantumStuffBlock;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.MultiblockControl;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

import static com.github.technus.tectech.Util.StructureBuilderExtreme;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_machine extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {

    public static final String machine = "EM Machinery";

    private Behaviour currentBehaviour;

    //region structure
    private static final String[][] shape = new String[][]{
            {"B0", "A   ", "0 - 0", "A   ", "B0",},
            {"A000", "00000", "00.00", "00000", "A000",},
            {"A121", "1---1", "2---2", "1---1", "A121",},
            {"A131", "1---1", "3-A-3", "1---1", "A131",},
            {"A121", "1---1", "2---2", "1---1", "A121",},
            {"A000", "00000", "00-00", "00000", "A000",},
            {"B0", "A!!!", "0!!!0", "A!!!", "B0",},};
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, QuantumGlassBlock.INSTANCE, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{4, 0, 5, 6};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList", "addElementalToMachineList"};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + "Hint Details:",
            "1 - Classic Hatches or High Power Casing",
            "2 - Elemental Hatches or Molecular Casing",};
    //endregion

    public GT_MetaTileEntity_EM_machine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_machine(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_machine(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 2, 1);
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilderExtreme(shape, blockType, blockMeta, 2, 2, 1, getBaseMetaTileEntity(), this,hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    public String[] getDescription() {
        return new String[]{CommonValues.TEC_MARK_EM, "Processing quantum matter since...", EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "the time u started using it."};
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        if(aBaseMetaTileEntity.isServerSide()) {
            quantumStuff(aBaseMetaTileEntity.isActive());
        }
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if(aBaseMetaTileEntity.isClientSide() && (aTick & 0x2)==0){
            currentBehaviour=GT_MetaTileEntity_EM_machine.map.get(new Util.TT_ItemStack(mInventory[1]));
            if(aBaseMetaTileEntity.isActive()){
                int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX*2+aBaseMetaTileEntity.getXCoord();
                int yDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetY*2+aBaseMetaTileEntity.getYCoord();
                int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ*2+aBaseMetaTileEntity.getZCoord();
                aBaseMetaTileEntity.getWorld().markBlockRangeForRenderUpdate(xDir,yDir,zDir,xDir,yDir,zDir);
            }
        }
    }

    private boolean setCurrentBehaviour(Behaviour newBehaviour){
        boolean changed=currentBehaviour!=newBehaviour;
        if(changed){
            setDefaultParametersAndStatuses();
        }
        currentBehaviour=newBehaviour;
        return changed;
    }

    private void setDefaultParametersAndStatuses() {
        for (int i = 0; i <= 3; i++) {
            setStatusOfParameterIn(i, 0, STATUS_NEUTRAL);
            setStatusOfParameterIn(i, 1, STATUS_NEUTRAL);
        }
        for (int i = 0; i <= 9; i++) {
            setStatusOfParameterOut(i, 0, STATUS_NEUTRAL);
            setStatusOfParameterOut(i, 1, STATUS_NEUTRAL);
            setParameterOut(i, 0, 0);
            setParameterOut(i, 1, 0);
        }
    }

    @Override
    public void onRemoval() {
        quantumStuff(false);
        super.onRemoval();
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        setCurrentBehaviour(GT_MetaTileEntity_EM_machine.map.get(new Util.TT_ItemStack(itemStack)));
        if(currentBehaviour==null){
            return false;
        }
        //mux input
        double[] parameters = new double[]{
                getParameterIn(0, 0),
                getParameterIn(0, 1),
                getParameterIn(1, 0),
                getParameterIn(1, 1),
                getParameterIn(2, 0),
                getParameterIn(2, 1),
                getParameterIn(3, 0),
                getParameterIn(3, 1)};
        if (!currentBehaviour.setAndCheckParametersOutAndStatuses(this, parameters)) {
            return false;
        }

        cElementalInstanceStackMap[] handles = new cElementalInstanceStackMap[6];
        int pointer = getParameterInInt(4, 0) - 1;
        if (pointer >= 0 && pointer < eInputHatches.size()) {
            handles[0] = eInputHatches.get(pointer).getContainerHandler();
        }
        pointer = getParameterInInt(4, 1) - 1;
        if (pointer >= 0 && pointer < eInputHatches.size()) {
            handles[1] = eInputHatches.get(pointer).getContainerHandler();
        }
        pointer = getParameterInInt(5, 0) - 1;
        if (pointer >= 0 && pointer < eInputHatches.size()) {
            handles[2] = eInputHatches.get(pointer).getContainerHandler();
        }
        pointer = getParameterInInt(5, 1) - 1;
        if (pointer >= 0 && pointer < eInputHatches.size()) {
            handles[3] = eInputHatches.get(pointer).getContainerHandler();
        }
        pointer = getParameterInInt(6, 0) - 1;
        if (pointer >= 0 && pointer < eInputHatches.size()) {
            handles[4] = eInputHatches.get(pointer).getContainerHandler();
        }
        pointer = getParameterInInt(6, 1) - 1;
        if (pointer >= 0 && pointer < eInputHatches.size()) {
            handles[5] = eInputHatches.get(pointer).getContainerHandler();
        }

        for (int i = 1; i < 6; i++) {
            if (handles[i] != null) {
                for (int j = 0; j < i; j++) {
                    if (handles[i] == handles[j]) {
                        return false;
                    }
                }
            }
        }

        MultiblockControl<cElementalInstanceStackMap[]> control = currentBehaviour.process(handles, parameters);
        if (control == null) {
            return false;
        }
        cleanMassEM_EM(control.getExcessMass());
        if (control.shouldExplode()) {
            explodeMultiblock();
            return false;
        }
        //update other parameters
        outputEM = control.getValue();
        mEUt = control.getEUT();
        eAmpereFlow = control.getAmperage();
        mMaxProgresstime = control.getMaxProgressTime();
        eRequiredData = control.getRequiredData();
        mEfficiencyIncrease = control.getEffIncrease();
        boolean polluted=polluteEnvironment(control.getPollutionToAdd());
        quantumStuff(polluted);
        return polluted;
    }

    @Override
    protected void afterRecipeCheckFailed() {
        quantumStuff(false);
        super.afterRecipeCheckFailed();
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (setCurrentBehaviour(GT_MetaTileEntity_EM_machine.map.get(new Util.TT_ItemStack(mInventory[1])))) {
            return;
        }
        cElementalInstanceStackMap[] handles = new cElementalInstanceStackMap[6];
        int pointer = getParameterInInt(7, 0) - 1;
        if (pointer >= 0 && pointer < eOutputHatches.size()) {
            handles[0] = eOutputHatches.get(pointer).getContainerHandler();
        }
        pointer = getParameterInInt(7, 1) - 1;
        if (pointer >= 0 && pointer < eOutputHatches.size()) {
            handles[1] = eOutputHatches.get(pointer).getContainerHandler();
        }
        pointer = getParameterInInt(8, 0) - 1;
        if (pointer >= 0 && pointer < eOutputHatches.size()) {
            handles[2] = eOutputHatches.get(pointer).getContainerHandler();
        }
        pointer = getParameterInInt(8, 1) - 1;
        if (pointer >= 0 && pointer < eOutputHatches.size()) {
            handles[3] = eOutputHatches.get(pointer).getContainerHandler();
        }
        pointer = getParameterInInt(9, 0) - 1;
        if (pointer >= 0 && pointer < eOutputHatches.size()) {
            handles[4] = eOutputHatches.get(pointer).getContainerHandler();
        }
        pointer = getParameterInInt(9, 1) - 1;
        if (pointer >= 0 && pointer < eOutputHatches.size()) {
            handles[5] = eOutputHatches.get(pointer).getContainerHandler();
        }
        //output
        for (int i = 0; i < 6 && i < outputEM.length; i++) {
            if (handles[i] != null && outputEM[i] != null && outputEM[i].hasStacks()) {
                handles[i].putUnifyAll(outputEM[i]);
                outputEM[i] = null;
            }
        }
        quantumStuff(false);
        //all other are handled by base multi block code - cleaning is automatic
    }

    @Override
    public void stopMachine() {
        quantumStuff(false);
        super.stopMachine();
    }

    @Override
    protected void parametersLoadDefault_EM() {//default routing table
        setParameterPairIn_ClearOut(4, false, 1, 2);//I
        setParameterPairIn_ClearOut(5, false, 3, 4);//I
        setParameterPairIn_ClearOut(6, false, 5, 6);//I

        setParameterPairIn_ClearOut(7, false, 1, 2);//O
        setParameterPairIn_ClearOut(8, false, 3, 4);//O
        setParameterPairIn_ClearOut(9, false, 5, 6);//O
    }

    @Override
    public void parametersOutAndStatusesWrite_EM(boolean machineBusy) {
        int pointer;
        {
            BitSet checkArray = new BitSet();
            for (int i = 4; i <= 6; i++) {
                pointer = getParameterInInt(i, 0);
                if (Double.isNaN(pointer)) {
                    setStatusOfParameterIn(i, 0, GT_MetaTileEntity_MultiblockBase_EM.STATUS_WRONG);
                } else if (pointer <= 0) {
                    setStatusOfParameterIn(i, 0, GT_MetaTileEntity_MultiblockBase_EM.STATUS_TOO_LOW);
                }//else if(pointer==0)
                //    setStatusOfParameterIn(i,0,STATUS_LOW);
                else if (pointer <= eInputHatches.size()) {
                    if (checkArray.get(pointer)) {
                        setStatusOfParameterIn(i, 0, GT_MetaTileEntity_MultiblockBase_EM.STATUS_WRONG);
                    } else {
                        setStatusOfParameterIn(i, 0, GT_MetaTileEntity_MultiblockBase_EM.STATUS_OK);
                        checkArray.set(pointer);
                    }
                } else {
                    setStatusOfParameterIn(i, 0, GT_MetaTileEntity_MultiblockBase_EM.STATUS_TOO_HIGH);
                }
                pointer = getParameterInInt(i, 1);
                if (Double.isNaN(pointer)) {
                    setStatusOfParameterIn(i, 1, GT_MetaTileEntity_MultiblockBase_EM.STATUS_WRONG);
                } else if (pointer < 0) {
                    setStatusOfParameterIn(i, 1, GT_MetaTileEntity_MultiblockBase_EM.STATUS_TOO_LOW);
                } else if (pointer == 0) {
                    setStatusOfParameterIn(i, 1, GT_MetaTileEntity_MultiblockBase_EM.STATUS_LOW);
                } else if (pointer <= eInputHatches.size()) {
                    if (checkArray.get(pointer)) {
                        setStatusOfParameterIn(i, 1, GT_MetaTileEntity_MultiblockBase_EM.STATUS_WRONG);
                    } else {
                        setStatusOfParameterIn(i, 1, GT_MetaTileEntity_MultiblockBase_EM.STATUS_OK);
                        checkArray.set(pointer);
                    }
                } else {
                    setStatusOfParameterIn(i, 1, GT_MetaTileEntity_MultiblockBase_EM.STATUS_TOO_HIGH);
                }
            }
        }
        {
            for (int i = 7; i <= 9; i++) {
                pointer = getParameterInInt(i, 0);
                if (Double.isNaN(pointer)) {
                    setStatusOfParameterIn(i, 0, GT_MetaTileEntity_MultiblockBase_EM.STATUS_WRONG);
                } else if (pointer < 0) {
                    setStatusOfParameterIn(i, 0, GT_MetaTileEntity_MultiblockBase_EM.STATUS_TOO_LOW);
                } else if (pointer == 0) {
                    setStatusOfParameterIn(i, 0, GT_MetaTileEntity_MultiblockBase_EM.STATUS_LOW);
                } else if (pointer <= eOutputHatches.size()) {
                    setStatusOfParameterIn(i, 0, GT_MetaTileEntity_MultiblockBase_EM.STATUS_OK);
                } else {
                    setStatusOfParameterIn(i, 0, GT_MetaTileEntity_MultiblockBase_EM.STATUS_TOO_HIGH);
                }
                pointer = getParameterInInt(i, 1);
                if (Double.isNaN(pointer)) {
                    setStatusOfParameterIn(i, 1, GT_MetaTileEntity_MultiblockBase_EM.STATUS_WRONG);
                } else if (pointer < 0) {
                    setStatusOfParameterIn(i, 1, GT_MetaTileEntity_MultiblockBase_EM.STATUS_TOO_LOW);
                } else if (pointer == 0) {
                    setStatusOfParameterIn(i, 1, GT_MetaTileEntity_MultiblockBase_EM.STATUS_LOW);
                } else if (pointer <= eOutputHatches.size()) {
                    setStatusOfParameterIn(i, 1, GT_MetaTileEntity_MultiblockBase_EM.STATUS_OK);
                } else {
                    setStatusOfParameterIn(i, 1, GT_MetaTileEntity_MultiblockBase_EM.STATUS_TOO_HIGH);
                }
            }
        }
        setCurrentBehaviour(GT_MetaTileEntity_EM_machine.map.get(new Util.TT_ItemStack(mInventory[1])));
        if (currentBehaviour == null) {
            setDefaultParametersAndStatuses();
        } else {
            double[] parameters = new double[]{
                    getParameterIn(0, 0),
                    getParameterIn(0, 1),
                    getParameterIn(1, 0),
                    getParameterIn(1, 1),
                    getParameterIn(2, 0),
                    getParameterIn(2, 1),
                    getParameterIn(3, 0),
                    getParameterIn(3, 1)};
            currentBehaviour.setAndCheckParametersOutAndStatuses(this,parameters);
        }
    }

    private static final HashMap<Util.TT_ItemStack, Behaviour> map = new HashMap<>();

    public static void registerBehaviour(Behaviour behaviour, ItemStack is) {
        map.put(new Util.TT_ItemStack(is), behaviour);
        TecTech.Logger.info("Registered EM machine behaviour "+behaviour.getClass().getSimpleName()+' '+new Util.TT_ItemStack(is).toString());
    }

    public static abstract class Behaviour {
        public Behaviour(){}

        /**
         * handle parameters pre recipe, and cyclically
         * this shouldn't write to input parameters! only to the provided array and/or output parameters
         * @param te this
         * @param parametersToCheckAndFix array of 6 parameters to pass to the process method (can be modified)
         *                                this allows to pass different numbers if u want to employ automatic parameter correction here
         * @return return true if machine can start with current parameters, false if not
         */
        public abstract boolean setAndCheckParametersOutAndStatuses(GT_MetaTileEntity_EM_machine te, double[] parametersToCheckAndFix);

        /**
         * do recipe handling
         * @param inputs from muxed inputs
         * @param checkedAndFixedParameters array passed from previous method!
         * @return null if recipe should not start, control object to set machine state and start recipe
         */
        public abstract MultiblockControl<cElementalInstanceStackMap[]> process(cElementalInstanceStackMap[] inputs, double[] checkedAndFixedParameters);

        /**
         * get input param description, only for 4 first hatches
         * @param baseDescr
         * @param hatchNo
         * @param paramID
         */
        protected void getFullLedDescriptionIn(ArrayList<String> baseDescr, int hatchNo, int paramID){}

        /**
         * get output param description
         * @param baseDescr
         * @param hatchNo
         * @param paramID
         */
        protected void getFullLedDescriptionOut(ArrayList<String> baseDescr, int hatchNo, int paramID){}
    }

    @Override
    public ArrayList<String> getFullLedDescriptionIn(int hatchNo, int paramID) {
        ArrayList<String> base=super.getFullLedDescriptionIn(hatchNo, paramID);
        if(hatchNo>=7){
            base.add("Output mux "+((hatchNo-7)*2+paramID+1));
        }else if(hatchNo>=4){
            base.add("Input mux "+((hatchNo-4)*2+paramID+1));
        }else if(currentBehaviour!=null){
            currentBehaviour.getFullLedDescriptionIn(base,hatchNo,paramID);
        }
        return base;
    }

    @Override
    public ArrayList<String> getFullLedDescriptionOut(int hatchNo, int paramID) {
        ArrayList<String> base=super.getFullLedDescriptionOut(hatchNo, paramID);
        if(currentBehaviour!=null){
            currentBehaviour.getFullLedDescriptionOut(base,hatchNo,paramID);
        }
        return base;
    }

    private void quantumStuff(boolean shouldExist){
        IGregTechTileEntity base=getBaseMetaTileEntity();
        if(base!=null && base.getWorld()!=null) {
            int xDir = ForgeDirection.getOrientation(base.getBackFacing()).offsetX * 2+base.getXCoord();
            int yDir = ForgeDirection.getOrientation(base.getBackFacing()).offsetY * 2+base.getYCoord();
            int zDir = ForgeDirection.getOrientation(base.getBackFacing()).offsetZ * 2+base.getZCoord();
            Block block = base.getWorld().getBlock(xDir, yDir, zDir);
            if (shouldExist) {
                if(block != null && block.getMaterial()== Material.air) {
                    base.getWorld().setBlock(xDir, yDir, zDir, QuantumStuffBlock.INSTANCE, 0, 2);
                }
            } else {
                if (block instanceof QuantumStuffBlock) {
                    base.getWorld().setBlock(xDir, yDir, zDir, Blocks.air, 0, 2);
                }
            }
        }
    }
}
