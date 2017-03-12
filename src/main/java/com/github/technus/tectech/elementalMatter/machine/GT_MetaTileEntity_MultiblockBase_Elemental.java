package com.github.technus.tectech.elementalMatter.machine;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.elementalMatter.classes.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStack;
import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStackTree;
import com.github.technus.tectech.elementalMatter.classes.tElementalException;
import com.github.technus.tectech.elementalMatter.commonValues;
import com.github.technus.tectech.elementalMatter.gui.GT_Container_MultiMachineEM;
import com.github.technus.tectech.elementalMatter.gui.GT_GUIContainer_MultiMachineEM;
import eu.usrv.yamcore.YAMCore;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Pollution;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

import static com.github.technus.tectech.elementalMatter.commonValues.*;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VN;

/**
 * Created by danie_000 on 27.10.2016.
 */
public abstract class GT_MetaTileEntity_MultiblockBase_Elemental extends GT_MetaTileEntity_MultiBlockBase {
    protected cElementalInstanceStackTree[] outputEM=new cElementalInstanceStackTree[0];

    final static ItemStack[] nothingI=new ItemStack[0];
    final static FluidStack[] nothingF=new FluidStack[0];

    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    public ArrayList<GT_MetaTileEntity_Hatch_InputElemental> eInputHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_OutputElemental> eOutputHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_MufflerElemental> eMufflerHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Param> eParamHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Uncertainty> eUncertainHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_EnergyMulti> eEnergyMulti = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_DynamoMulti> eDynamoMulti = new ArrayList<>();

    public final float[] eParamsIn=new float[20];
    public final float[] eParamsOut=new float[20];
    public final byte[] eParamsInStatus =new byte[20];
    public final byte[] eParamsOutStatus=new byte[20];
    protected final static byte PARAM_UNUSED=0, PARAM_OK=1, PARAM_TOO_LOW=2, PARAM_LOW=3, PARAM_TOO_HIGH=4, PARAM_HIGH=5, PARAM_WRONG=6;

    //TO ENABLE this change value in <init> to false and/or other than 0, can also be added in recipe check or whatever
    public boolean eParameters=true,ePowerPass=false,eSafeVoid=false,eDismatleBoom=false;
    public byte eCertainMode=0,eCertainStatus=0,minRepairStatus=3;

    private int eMaxAmpereFlow =0;
    private long maxEUinputMin=0,maxEUinputMax=0;
    public int eAmpereFlow =1;

    //init param states in constructor, or implement it in checkrecipe/outputfunction

    //METHODS TO OVERRIDE - this 3 below + checkMachine

    //if you want to add checks that run periodically when machine works then make onRunningTick better
    //if you want to add checks that run periodically when machine is built then use check params

    public boolean EM_checkRecipe(ItemStack itemStack){
        return false;
    }
    //My code handles AMPS, if you want overclocking just modify mEUt and mMaxProgressTime, leave amps as usual!

    public void EM_checkParams(){}
    //update status of parameters in guis and "machine state"

    public void EM_outputFunction(){}
    // based on "machine state" do output,
    // this must move to output EM things and can also modify output items/fluids/EM, remaining EM is NOT overflowed.
    //(Well it can be overflowed if machine didn't finished, soft-hammered/disabled/not enough EU)

    //RATHER LEAVE ALONE Section

    public GT_MetaTileEntity_MultiblockBase_Elemental(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_MultiblockBase_Elemental(String aName) {
        super(aName);
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "EMDisplay.png");
    }

    @Override
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_CONTROLLER");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_CONTROLLER_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[99], new GT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[99]};
    }

    @Override
    public final byte getTileEntityBaseType() {
        return 3;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        //Fix supermethod shit.
        if(mOutputItems!=null)
            aNBT.setInteger("eItemsOut",mOutputItems.length);
        if(mOutputFluids!=null)
            aNBT.setInteger("eFluidsOut",mOutputFluids.length);

        aNBT.setLong("eMaxEUmin", maxEUinputMin);
        aNBT.setLong("eMaxEUmax", maxEUinputMax);
        aNBT.setInteger("eRating", eAmpereFlow);
        aNBT.setInteger("eMaxA", eMaxAmpereFlow);
        aNBT.setByte("eCertainM",eCertainMode);
        aNBT.setByte("eCertainS",eCertainStatus);
        aNBT.setByte("eMinRepair",minRepairStatus);
        aNBT.setBoolean("eParam",eParameters);
        aNBT.setBoolean("ePass",ePowerPass);
        aNBT.setBoolean("eVoid",eSafeVoid);
        aNBT.setBoolean("eBoom", eDismatleBoom);

        if (outputEM!=null) {
            aNBT.setInteger("outputStackCount", outputEM.length);
            NBTTagCompound output = new NBTTagCompound();
            for (int i = 0; i < outputEM.length; i++)
                output.setTag(Integer.toString(i), outputEM[i].toNBT());
            aNBT.setTag("outputEM", output);
        } else {
            aNBT.setInteger("outputStackCount", 0);
            aNBT.removeTag("outputEM");
        }

        NBTTagCompound paramI=new NBTTagCompound();
        for(int i=0;i<20;i++)
            paramI.setFloat(Integer.toString(i),eParamsIn[i]);
        aNBT.setTag("eParamsIn",paramI);

        NBTTagCompound paramO=new NBTTagCompound();
        for(int i=0;i<20;i++)
            paramO.setFloat(Integer.toString(i),eParamsOut[i]);
        aNBT.setTag("eParamsOut",paramO);

        NBTTagCompound paramIs=new NBTTagCompound();
        for(int i=0;i<20;i++)
            paramIs.setByte(Integer.toString(i),eParamsInStatus[i]);
        aNBT.setTag("eParamsInS",paramIs);

        NBTTagCompound paramOs=new NBTTagCompound();
        for(int i=0;i<20;i++)
            paramOs.setByte(Integer.toString(i),eParamsOutStatus[i]);
        aNBT.setTag("eParamsOutS",paramOs);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        maxEUinputMin =aNBT.getLong("eMaxEUmin");
        maxEUinputMax =aNBT.getLong("eMaxEUmax");
        eAmpereFlow =aNBT.getInteger("eRating");
        eMaxAmpereFlow =aNBT.getInteger("eMaxA");
        eCertainMode=aNBT.getByte("eCertainM");
        eCertainStatus=aNBT.getByte("eCertainS");
        minRepairStatus=aNBT.getByte("eMinRepair");
        eParameters=aNBT.getBoolean("eParam");
        ePowerPass=aNBT.getBoolean("ePass");
        eSafeVoid=aNBT.getBoolean("eVoid");
        eDismatleBoom =aNBT.getBoolean("eBoom");

        //Fix supermethod shit.
        mOutputItems = new ItemStack[aNBT.getInteger("eItemsOut")];
        for (int i = 0; i < mOutputItems.length; i++)
            mOutputItems[i] = GT_Utility.loadItem(aNBT, "mOutputItem" + i);
        mOutputFluids = new FluidStack[aNBT.getInteger("eFluidsOut")];
        for (int i = 0; i < mOutputFluids.length; i++)
            mOutputFluids[i] = GT_Utility.loadFluid(aNBT, "mOutputFluids" + i);

        final int outputLen=aNBT.getInteger("outputStackCount");
        if(outputLen>0){
            outputEM=new cElementalInstanceStackTree[outputLen];
            for(int i=0;i<outputEM.length;i++)
                try {
                    outputEM[i] = cElementalInstanceStackTree.fromNBT(
                            aNBT.getCompoundTag("outputEM").getCompoundTag(Integer.toString(i)));
                }catch (tElementalException e){
                    if(TecTech.ModConfig.DEBUG_MODE)e.printStackTrace();
                    outputEM[i] = new cElementalInstanceStackTree();
                }
        }else outputEM=new cElementalInstanceStackTree[0];

        NBTTagCompound paramI=aNBT.getCompoundTag("eParamsIn");
        for(int i=0;i<eParamsIn.length;i++)
            eParamsIn[i]=paramI.getFloat(Integer.toString(i));

        NBTTagCompound paramO=aNBT.getCompoundTag("eParamsOut");
        for(int i=0;i<eParamsOut.length;i++)
            eParamsOut[i]=paramO.getFloat(Integer.toString(i));

        NBTTagCompound paramIs=aNBT.getCompoundTag("eParamsInS");
        for(int i=0;i<eParamsInStatus.length;i++)
            eParamsInStatus[i]=paramIs.getByte(Integer.toString(i));

        NBTTagCompound paramOs=aNBT.getCompoundTag("eParamsOutS");
        for(int i=0;i<eParamsOutStatus.length;i++)
            eParamsOutStatus[i]=paramOs.getByte(Integer.toString(i));
    }

    @Override
    public final long maxEUStore() {
        return (maxEUinputMin* eMaxAmpereFlow)<<3;
    }

    @Override
    public final long getMinimumStoredEU() {
        return maxEUStore()>>1;
    }

    @Override
    public final long maxAmperesIn() {
        return 0L;
    }

    @Override
    public final long maxAmperesOut() {
        return 0L;
    }

    @Override
    public int getPollutionPerTick(ItemStack itemStack) {
        return 0;
    }

    @Override
    public final void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (mEfficiency < 0) mEfficiency = 0;
            if (--mUpdate == 0 || --mStartUpCheck == 0) {

                mInputHatches.clear();
                mInputBusses.clear();
                mOutputHatches.clear();
                mOutputBusses.clear();
                mDynamoHatches.clear();
                mEnergyHatches.clear();
                mMufflerHatches.clear();
                mMaintenanceHatches.clear();

                for(GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental:eOutputHatches)
                    if(isValidMetaTileEntity(hatch_elemental))hatch_elemental.id=-1;
                for(GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental:eInputHatches)
                    if(isValidMetaTileEntity(hatch_elemental))hatch_elemental.id=-1;
                for(GT_MetaTileEntity_Hatch_Uncertainty hatch:eUncertainHatches)
                    if(isValidMetaTileEntity(hatch))hatch.getBaseMetaTileEntity().setActive(false);
                for(GT_MetaTileEntity_Hatch_Param hatch:eParamHatches)
                    if(isValidMetaTileEntity(hatch))hatch.getBaseMetaTileEntity().setActive(false);

                eUncertainHatches.clear();
                eEnergyMulti.clear();
                eInputHatches.clear();
                eOutputHatches.clear();
                eParamHatches.clear();
                eMufflerHatches.clear();
                eDynamoMulti.clear();

                mMachine = checkMachine(aBaseMetaTileEntity, mInventory[1]);

                if(!mMachine)
                    if(eDismatleBoom && mMaxProgresstime>0) explodeMultiblock();
                    else if(outputEM!=null)
                        for(cElementalInstanceStackTree tree:outputEM)
                            if(tree.hasStacks()) explodeMultiblock();

                if(eUncertainHatches.size()>1) mMachine=false;

                if(mMachine) {
                    short id=1;
                    for(GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental:eOutputHatches)
                        if(isValidMetaTileEntity(hatch_elemental))hatch_elemental.id=id++;
                    id=1;
                    for(GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental:eInputHatches)
                        if(isValidMetaTileEntity(hatch_elemental))hatch_elemental.id=id++;

                    if(mEnergyHatches.size()>0 || eEnergyMulti.size()>0) {
                        maxEUinputMin =V[15];
                        maxEUinputMax=V[0];
                        for(GT_MetaTileEntity_Hatch_Energy hatch:mEnergyHatches)
                            if(isValidMetaTileEntity(hatch)){
                                if (hatch.maxEUInput() < maxEUinputMin) maxEUinputMin = hatch.maxEUInput();
                                if (hatch.maxEUInput() > maxEUinputMax) maxEUinputMax = hatch.maxEUInput();
                            }
                        for(GT_MetaTileEntity_Hatch_EnergyMulti hatch:eEnergyMulti)
                            if(isValidMetaTileEntity(hatch)){
                                if (hatch.maxEUInput() < maxEUinputMin) maxEUinputMin = hatch.maxEUInput();
                                if (hatch.maxEUInput() > maxEUinputMax) maxEUinputMax = hatch.maxEUInput();
                            }
                        eMaxAmpereFlow =0;
                        //counts only full amps
                        for(GT_MetaTileEntity_Hatch_Energy hatch:mEnergyHatches)
                            if(isValidMetaTileEntity(hatch)) eMaxAmpereFlow +=hatch.maxEUInput()/ maxEUinputMin;
                        for(GT_MetaTileEntity_Hatch_EnergyMulti hatch:eEnergyMulti)
                            if(isValidMetaTileEntity(hatch)) eMaxAmpereFlow +=(hatch.maxEUInput()/ maxEUinputMin)*hatch.Amperes;
                        if(this.getEUVar()>maxEUStore())this.setEUVar(this.maxEUStore());
                    } else {
                        maxEUinputMin=0;
                        maxEUinputMax=0;
                        eMaxAmpereFlow =0;
                        this.setEUVar(0);
                    }

                    for (GT_MetaTileEntity_Hatch_Uncertainty hatch : eUncertainHatches)
                        if(isValidMetaTileEntity(hatch))hatch.getBaseMetaTileEntity().setActive(true);
                    for (GT_MetaTileEntity_Hatch_Param hatch : eParamHatches)
                        if(isValidMetaTileEntity(hatch))hatch.getBaseMetaTileEntity().setActive(true);
                }else{
                    maxEUinputMin=0;
                    maxEUinputMax=0;
                    eMaxAmpereFlow =0;
                    this.setEUVar(0);
                }
            }

            if (mStartUpCheck < 0 ) {//E
                if (mMachine) {//S
                    final byte Tick=(byte)(aTick%20);
                    if (multiPurge1At==Tick || multiPurge2At==Tick)
                        purgeAll();
                    else if (multiCheckAt==Tick)
                        for (GT_MetaTileEntity_Hatch_Maintenance tHatch : mMaintenanceHatches) {
                            if (isValidMetaTileEntity(tHatch)) {
                                if (disableMaintenance){
                                    mWrench = true;
                                    mScrewdriver = true;
                                    mSoftHammer = true;
                                    mHardHammer = true;
                                    mSolderingTool = true;
                                    mCrowbar = true;
                                } else {
                                    if (tHatch.mAuto && !(mWrench&&mScrewdriver&&mSoftHammer&&mHardHammer&&mSolderingTool&&mCrowbar))tHatch.isRecipeInputEqual(true);
                                    if (tHatch.mWrench) mWrench = true;
                                    if (tHatch.mScrewdriver) mScrewdriver = true;
                                    if (tHatch.mSoftHammer) mSoftHammer = true;
                                    if (tHatch.mHardHammer) mHardHammer = true;
                                    if (tHatch.mSolderingTool) mSolderingTool = true;
                                    if (tHatch.mCrowbar) mCrowbar = true;

                                    tHatch.mWrench = false;
                                    tHatch.mScrewdriver = false;
                                    tHatch.mSoftHammer = false;
                                    tHatch.mHardHammer = false;
                                    tHatch.mSolderingTool = false;
                                    tHatch.mCrowbar = false;
                                }
                            }
                        }
                    else if(moveAt==Tick && eSafeVoid) {
                        for(GT_MetaTileEntity_Hatch_MufflerElemental voider:eMufflerHatches) {
                            if(voider.overflowMax<voider.overflowMatter) continue;
                            float remaining=voider.overflowMax-voider.overflowMatter;
                            for(GT_MetaTileEntity_Hatch_InputElemental in:eInputHatches){
                                for(cElementalInstanceStack instance:in.getContainerHandler().values()){
                                    int qty=(int)Math.floor(remaining/instance.definition.getMass());
                                    if(qty>0){
                                        qty=Math.min(qty,instance.amount);
                                        voider.overflowMatter+=instance.definition.getMass()*qty;
                                        in.getContainerHandler().removeAmount(false,new cElementalDefinitionStack(instance.definition,qty));
                                    }
                                }
                            }
                            for(GT_MetaTileEntity_Hatch_OutputElemental out:eOutputHatches){
                                for(cElementalInstanceStack instance:out.getContainerHandler().values()){
                                    int qty=(int)Math.floor(remaining/instance.definition.getMass());
                                    if(qty>0){
                                        qty=Math.min(qty,instance.amount);
                                        voider.overflowMatter+=instance.definition.getMass()*qty;
                                        out.getContainerHandler().removeAmount(false,new cElementalDefinitionStack(instance.definition,qty));
                                    }
                                }
                            }
                            //in case some weird shit happened here, it will still be safe
                            if(voider.overflowMatter>voider.overflowMax)voider.overflowMatter=voider.overflowMax;
                        }
                    }

                    if (getRepairStatus() >= minRepairStatus) {//S
                        if (multiCheckAt==Tick)
                            paramsUpdate();

                        if (mMaxProgresstime > 0 && doRandomMaintenanceDamage()) {//Start
                            if (onRunningTick(mInventory[1])) {//Compute EU
                                if (!polluteEnvironment(getPollutionPerTick(mInventory[1])))
                                    stopMachine();

                                if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime && recipeAt==Tick ) {//progress increase and done
                                    paramsUpdate();
                                    EM_outputFunction();
                                    if (mOutputItems != null) for (ItemStack tStack : mOutputItems)
                                        if (tStack != null)
                                            addOutput(tStack);

                                    if (mOutputFluids != null && mOutputFluids.length == 1)
                                        for (FluidStack tStack : mOutputFluids)
                                            if (tStack != null)
                                                addOutput(tStack);
                                    else if (mOutputFluids != null && mOutputFluids.length > 1)
                                        addFluidOutputs(mOutputFluids);
                                    updateSlots();
                                    mOutputItems = null;
                                    mOutputFluids = null;
                                    outputEM=new cElementalInstanceStackTree[0];
                                    mProgresstime = 0;
                                    mMaxProgresstime = 0;
                                    mEfficiencyIncrease = 0;
                                    if (aBaseMetaTileEntity.isAllowedToWork()) {
                                        if(checkRecipe(mInventory[1])) {
                                            mEfficiency = Math.max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
                                        }
                                        updateSlots();
                                    }
                                }
                            }
                        } else {
                            if (recipeAt==Tick || aBaseMetaTileEntity.hasWorkJustBeenEnabled()) {
                                if (aBaseMetaTileEntity.isAllowedToWork()) {
                                    if(checkRecipe(mInventory[1])) {
                                        mEfficiency = Math.max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
                                    }
                                    updateSlots();
                                }
                            }
                        }

                        {//DO ONCE
                            long euVar;
                            for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
                                if (this.getEUVar() > this.getMinimumStoredEU()) break;
                                if (isValidMetaTileEntity(tHatch)){
                                    euVar=tHatch.maxEUInput();
                                    if(tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(euVar, false))
                                        this.setEUVar(this.getEUVar() + euVar);
                                }
                            }
                            for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti) {
                                if (this.getEUVar() > this.getMinimumStoredEU()) break;
                                if (isValidMetaTileEntity(tHatch)){
                                    euVar=tHatch.maxEUInput()*tHatch.Amperes;
                                    if(tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(euVar, false))
                                        this.setEUVar(this.getEUVar() + euVar);
                                }
                            }
                            if (ePowerPass) {
                                for (GT_MetaTileEntity_Hatch_Dynamo tHatch : mDynamoHatches) {
                                    if (isValidMetaTileEntity(tHatch)) {
                                        euVar = tHatch.maxEUOutput();
                                        if(     tHatch.getBaseMetaTileEntity().getStoredEU() <= (tHatch.maxEUStore() - euVar) &&
                                                aBaseMetaTileEntity.decreaseStoredEnergyUnits(euVar + (euVar >> 7), false))
                                            tHatch.setEUVar(tHatch.getBaseMetaTileEntity().getStoredEU() + euVar);
                                    }
                                }
                                for (GT_MetaTileEntity_Hatch_DynamoMulti tHatch : eDynamoMulti) {
                                    if (isValidMetaTileEntity(tHatch)){
                                        euVar = tHatch.maxEUOutput()*tHatch.Amperes;
                                        if(     tHatch.getBaseMetaTileEntity().getStoredEU() <= tHatch.maxEUStore() - euVar &&
                                                aBaseMetaTileEntity.decreaseStoredEnergyUnits(euVar + (euVar >> 7), false))
                                            tHatch.setEUVar(tHatch.getBaseMetaTileEntity().getStoredEU() + euVar);
                                    }
                                }
                            }
                        }
                    } else {//not repaired
                        stopMachine();
                    }
                } else {//not machine
                    stopMachine();
                }
            }
            aBaseMetaTileEntity.setErrorDisplayID((aBaseMetaTileEntity.getErrorDisplayID() & -512) | (mWrench ? 0 : 1) | (mScrewdriver ? 0 : 2) | (mSoftHammer ? 0 : 4) | (mHardHammer ? 0 : 8) | (mSolderingTool ? 0 : 16) | (mCrowbar ? 0 : 32) | (mMachine ? 0 : 64) | ((eCertainStatus == 0) ? 0 : 128) | (eParameters ? 0 : 256));
            aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
            boolean active = aBaseMetaTileEntity.isActive() && mPollution > 0;
            for (GT_MetaTileEntity_Hatch_Muffler aMuffler : mMufflerHatches)
                aMuffler.getBaseMetaTileEntity().setActive(active);
        }
    }

    @Deprecated
    @Override
    public final int getAmountOfOutputs() {
        return 0;
    }

    private void addFluidOutputs(FluidStack[] mOutputFluids2) {
        for(int i = 0; i < mOutputFluids2.length; ++i) {
            if(this.mOutputHatches.size() > i && this.mOutputHatches.get(i) != null && mOutputFluids2[i] != null && isValidMetaTileEntity((MetaTileEntity)this.mOutputHatches.get(i))) {
                this.mOutputHatches.get(i).fill(mOutputFluids2[i], true);
            }
        }
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getIdealStatus() {
        return super.getIdealStatus()+2;
    }

    @Override
    public int getRepairStatus() {
        return super.getRepairStatus() + ((eCertainStatus==0)?1:0) + (this.eParameters?1:0);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if(this.mEUt > 0) {
            this.EMaddEnergyOutput((long)mEUt *  (long)mEfficiency / getMaxEfficiency(aStack), eAmpereFlow);
            return true;
        } else if(this.mEUt < 0 && !this.EMdrainEnergyInput((long)(-this.mEUt) * getMaxEfficiency(aStack) / (long)Math.max(1000, this.mEfficiency), eAmpereFlow)) {
            this.stopMachine();
            return false;
        } else return true;
    }

    @Deprecated
    @Override
    public final boolean addEnergyOutput(long EU) {
        if(EU <= 0L) return true;
        for(GT_MetaTileEntity_Hatch tHatch:eDynamoMulti)
            if(isValidMetaTileEntity(tHatch) && tHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(EU,false))
                return true;
        for(GT_MetaTileEntity_Hatch tHatch:mDynamoHatches)
            if(isValidMetaTileEntity(tHatch) && tHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(EU,false))
                return true;
        return false;
    }

    //new method
    public final boolean EMaddEnergyOutput(long EU, int Amperes) {
        if(EU <= 0L || Amperes<=0) return true;
        long euVar=EU*Amperes;
        long diff;
        for (GT_MetaTileEntity_Hatch_Dynamo tHatch : mDynamoHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                if(tHatch.maxEUOutput()<EU) explodeMultiblock();
                diff = tHatch.maxEUStore() - tHatch.getBaseMetaTileEntity().getStoredEU();
                if (diff > 0) {
                    if (euVar > diff) {
                        tHatch.setEUVar(tHatch.maxEUStore());
                        euVar -= diff;
                    } else if (euVar <= diff) {
                        tHatch.setEUVar(tHatch.getBaseMetaTileEntity().getStoredEU() + euVar);
                        return true;
                    }
                }
            }
        }
        for (GT_MetaTileEntity_Hatch_DynamoMulti tHatch : eDynamoMulti) {
            if (isValidMetaTileEntity(tHatch)) {
                if(tHatch.maxEUOutput()<EU) explodeMultiblock();
                diff = tHatch.maxEUStore() - tHatch.getBaseMetaTileEntity().getStoredEU();
                if (diff > 0) {
                    if (euVar > diff) {
                        tHatch.setEUVar(tHatch.maxEUStore());
                        euVar -= diff;
                    } else if (euVar <= diff) {
                        tHatch.setEUVar(tHatch.getBaseMetaTileEntity().getStoredEU() + euVar);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Deprecated
    @Override
    public final boolean drainEnergyInput(long EU) {
        if(EU <= 0L) return true;
        for(GT_MetaTileEntity_Hatch tHatch:eEnergyMulti)
            if(isValidMetaTileEntity(tHatch) && tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(EU,false))
                return true;
        for(GT_MetaTileEntity_Hatch tHatch:mEnergyHatches)
            if(isValidMetaTileEntity(tHatch) && tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(EU,false))
                return true;
        return false;
    }

    //new method
    public final boolean EMdrainEnergyInput(long EU, int Amperes) {
        if(EU <= 0L || Amperes<=0) return true;
        long euVar=EU*Amperes;
        if(     getEUVar() < euVar ||
                EU>maxEUinputMax ||
                (euVar+maxEUinputMin-1)/maxEUinputMin> eMaxAmpereFlow)return false;
        //sub eu
        setEUVar(getEUVar()-euVar);
        return true;
    }

    //new method
    public final boolean EMoverclockAndPutValuesIn(long EU,int time){
        if(EU==0){
            mEUt=0;
            mMaxProgresstime=time;
            return true;
        }
        long tempEUt = EU<V[1] ? V[1] : EU;
        long tempTier=maxEUinputMax>>2;
        while(tempEUt<tempTier){
            tempEUt<<=2;
            time>>=1;
            EU = time==0 ? EU>>1 : EU<<2;//U know, if the time is less than 1 tick make the machine use less power
        }
        if(EU> Integer.MAX_VALUE || EU< Integer.MIN_VALUE){
            mEUt= Integer.MAX_VALUE-1;
            mMaxProgresstime= Integer.MAX_VALUE-1;
            return false;
        }
        mEUt=(int)EU;
        mMaxProgresstime=time==0?1:time;
        return true;
    }//Use in EM check recipe return statement if you want overclocking

    @Override
    public final long getMaxInputVoltage() {
        long rVoltage = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches)
            if (isValidMetaTileEntity(tHatch)) rVoltage += tHatch.maxEUInput();
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti)
            if (isValidMetaTileEntity(tHatch)) rVoltage += tHatch.maxEUInput();
        return rVoltage;
    }

    //new Method
    public final int getMaxEnergyInputTier(){
        return GT_Utility.getTier(maxEUinputMax);
    }

    //new Method
    public final int getMinEnergyInputTier(){
        return GT_Utility.getTier(maxEUinputMin);
    }

    @Override
    public final void stopMachine() {
        mOutputItems = null;
        mOutputFluids = null;
        //mEUt = 0;
        mEfficiency = 0;
        mProgresstime = 0;
        mMaxProgresstime = 0;
        mEfficiencyIncrease = 0;
        getBaseMetaTileEntity().disableWorking();

        float mass=0;
        if(outputEM==null) return;
        for(cElementalInstanceStackTree tree:outputEM)
            mass+=tree.getMass();
        if(mass>0) {
            if (eMufflerHatches.size()<1) explodeMultiblock();
            mass /= eMufflerHatches.size();
            for (GT_MetaTileEntity_Hatch_MufflerElemental dump : eMufflerHatches) {
                dump.overflowMatter += mass;
                if (dump.overflowMatter > dump.overflowMax) explodeMultiblock();
            }
        }
        outputEM = null;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return null;
    }

    @Override
    public void updateSlots() {
        super.updateSlots();
        purgeAll();
    }

    private void purgeAll(){
        float mass=0;
        for(GT_MetaTileEntity_Hatch_InputElemental tHatch: eInputHatches) {
            if (isValidMetaTileEntity(tHatch)) tHatch.updateSlots();
            mass+=tHatch.overflowMatter;
            tHatch.overflowMatter=0;
        }
        for(GT_MetaTileEntity_Hatch_OutputElemental tHatch: eOutputHatches) {
            if (isValidMetaTileEntity(tHatch)) tHatch.updateSlots();
            mass+=tHatch.overflowMatter;
            tHatch.overflowMatter=0;
        }
        if(mass>0) {
            if (eMufflerHatches.size()<1) {
                explodeMultiblock();
                return;
            }
            mass /= eMufflerHatches.size();
            for (GT_MetaTileEntity_Hatch_MufflerElemental dump : eMufflerHatches) {
                dump.overflowMatter += mass;
                if (dump.overflowMatter > dump.overflowMax) explodeMultiblock();
            }
        }
    }

    public void cleanHatchContent(GT_MetaTileEntity_Hatch_ElementalContainer target){
        float mass=target.getContainerHandler().getMass();
        if(mass>0) {
            if (eMufflerHatches.size()<1) explodeMultiblock();
            mass /= eMufflerHatches.size();
            for (GT_MetaTileEntity_Hatch_MufflerElemental dump : eMufflerHatches) {
                dump.overflowMatter += mass;
                if (dump.overflowMatter > dump.overflowMax) explodeMultiblock();
            }
        }
    }

    @Override
    public final boolean checkRecipe(ItemStack itemStack){//do recipe checks, based on "machine content and state"
        paramsUpdate();
        return EM_checkRecipe(itemStack);
    }

    private void paramsUpdate(){
        for(GT_MetaTileEntity_Hatch_Param param:eParamHatches){
            int paramID=param.param;
            if(paramID<0)continue;
            eParamsIn[paramID]=param.value1f;
            eParamsIn[paramID+10]=param.value2f;
            param.input1f=eParamsOut[paramID];
            param.input2f=eParamsOut[paramID+10];
        }
        EM_checkParams();
        for(GT_MetaTileEntity_Hatch_Uncertainty uncertainty:eUncertainHatches){
            eCertainStatus=uncertainty.update(eCertainMode);
        }
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getDamageToComponent(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return false;
    }

    @Override
    public void explodeMultiblock() {//BEST METHOD EVER!!!
        if(!TecTech.ModConfig.BOOM_ENABLE) {
            TecTech.proxy.broadcast("BOOM! "+getBaseMetaTileEntity().getXCoord()+" "+getBaseMetaTileEntity().getYCoord()+" "+getBaseMetaTileEntity().getZCoord());
            return;
        }
        GT_Pollution.addPollution(new ChunkPosition(this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getYCoord(), this.getBaseMetaTileEntity().getZCoord()), 600000);
        mInventory[1] = null;
        for (MetaTileEntity tTileEntity : mInputBusses)         tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : mOutputBusses)        tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : mInputHatches)        tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : mOutputHatches)       tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : mDynamoHatches)       tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : mMufflerHatches)      tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : mEnergyHatches)       tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : mMaintenanceHatches)  tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : eParamHatches)        tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : eInputHatches)        tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : eOutputHatches)       tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : eMufflerHatches)      tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : eEnergyMulti)         tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : eUncertainHatches)    tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : eDynamoMulti)         tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        getBaseMetaTileEntity().doExplosion(V[15]);
    }

    @Override
    public final boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch)
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input)
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus)
            return mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output)
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus)
            return mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy)
            return mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo)
            return mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance)
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler)
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputElemental)
            return eInputHatches.add((GT_MetaTileEntity_Hatch_InputElemental) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputElemental)
            return eOutputHatches.add((GT_MetaTileEntity_Hatch_OutputElemental) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Param)
            return eParamHatches.add((GT_MetaTileEntity_Hatch_Param) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Uncertainty)
            return eUncertainHatches.add((GT_MetaTileEntity_Hatch_Uncertainty) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_MufflerElemental)
            return eMufflerHatches.add((GT_MetaTileEntity_Hatch_MufflerElemental) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti)
            return eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
        return false;
    }

    @Override
    public final boolean addMufflerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_MufflerElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return eMufflerHatches.add((GT_MetaTileEntity_Hatch_MufflerElemental) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public final boolean addInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            ((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            //((GT_MetaTileEntity_Hatch_Elemental) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return eInputHatches.add((GT_MetaTileEntity_Hatch_InputElemental) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public final boolean addOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return eOutputHatches.add((GT_MetaTileEntity_Hatch_OutputElemental) aMetaTileEntity);
        }
        return false;
    }

    @Deprecated
    @Override
    public final boolean addEnergyInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
        }
        return false;
    }

    @Deprecated
    @Override
    public final boolean addDynamoToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
            ((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti) {
            ((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return eDynamoMulti.add((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity);
        }
        return false;
    }

    //New Method
    public final boolean addEnergyIOToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
            ((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti) {
            ((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return eDynamoMulti.add((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addElementalInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            //((GT_MetaTileEntity_Hatch_Elemental) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return eInputHatches.add((GT_MetaTileEntity_Hatch_InputElemental) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addElementalOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return eOutputHatches.add((GT_MetaTileEntity_Hatch_OutputElemental) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addClassicInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            ((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addClassicOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addParametrizerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Param) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return eParamHatches.add((GT_MetaTileEntity_Hatch_Param) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addUncertainToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Uncertainty) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return eUncertainHatches.add((GT_MetaTileEntity_Hatch_Uncertainty) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public final boolean addMaintenanceToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex){
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Param) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return eParamHatches.add((GT_MetaTileEntity_Hatch_Param) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Uncertainty) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return eUncertainHatches.add((GT_MetaTileEntity_Hatch_Uncertainty) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addClassicMaintenanceToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex){
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public String[] getInfoData() {//TODO Do it
        long storedEnergy=0;
        long maxEnergy=0;
        for(GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy+=tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy+=tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }
        for(GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy+=tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy+=tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }

        return new String[]{
                "Progress:",
                EnumChatFormatting.GREEN + Integer.toString(mProgresstime/20) + EnumChatFormatting.RESET +" s / "+
                        EnumChatFormatting.YELLOW + Integer.toString(mMaxProgresstime/20) + EnumChatFormatting.RESET +" s",
                "Stored Energy:",
                EnumChatFormatting.GREEN + Long.toString(storedEnergy) + EnumChatFormatting.RESET +" EU / "+
                        EnumChatFormatting.YELLOW + Long.toString(maxEnergy) + EnumChatFormatting.RESET +" EU",
                (mEUt<=0?"Probably uses: ":"Probably makes: ")+
                        EnumChatFormatting.RED + Integer.toString(Math.abs(mEUt)) + EnumChatFormatting.RESET + " EU/t at "+
                        EnumChatFormatting.RED + Integer.toString(eAmpereFlow)+ EnumChatFormatting.RESET +" A",
                "Tier Rating: "+EnumChatFormatting.YELLOW+VN[getMaxEnergyInputTier()]+EnumChatFormatting.RESET+" / "+EnumChatFormatting.GREEN+VN[getMinEnergyInputTier()]+EnumChatFormatting.RESET+
                        " Amp Rating: "+EnumChatFormatting.GREEN+ eMaxAmpereFlow +EnumChatFormatting.RESET + " A",
                "Problems: "+EnumChatFormatting.RED+ (getIdealStatus() - getRepairStatus())+EnumChatFormatting.RESET+
                        " Efficiency: "+EnumChatFormatting.YELLOW+Float.toString(mEfficiency / 100.0F)+EnumChatFormatting.RESET + " %",
                "PowerPass: "+EnumChatFormatting.BLUE+ePowerPass+EnumChatFormatting.RESET+
                        " SafeVoid: "+EnumChatFormatting.BLUE+eSafeVoid
        };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    //Check Machine Structure based on string array array, ond offset of the controller
    public static boolean stuctureCheck(String[][] structure,//0-9 casing, +- air no air, a-z ignore
                                        Block[] blockType,//use numbers 0-9 for casing types
                                        byte[] blockMeta,//use numbers 0-9 for casing types
                                        int horizontalOffset, int verticalOffset, int depthOffset,
                                        IGregTechTileEntity aBaseMetaTileEntity){
        //TE Rotation
        byte facing=aBaseMetaTileEntity.getFrontFacing();

        int x,y,z,a,c;//b is y no matter what

        //perform your duties
        c=-depthOffset;
        for (String[] _structure:structure) {//front to back
            y=verticalOffset;
            for (String __structure : _structure) {//top to bottom
                a=-horizontalOffset;
                for (char block : __structure.toCharArray()) {//left to right
                    if(block>'`'){//small characters allow to skip check a-1 skip, b-2 skips etc.
                        a+=block-'`';
                    } else {
                        //get x y z from rotation
                        switch (facing) {//translation
                            case 4: x =  c; z =  a; break;
                            case 3: x =  a; z = -c; break;
                            case 5: x = -c; z = -a; break;
                            case 2: x = -a; z =  c; break;
                            default: return false;
                        }
                        //Check block
                        switch (block) {
                            case '-'://must be air
                                if (!aBaseMetaTileEntity.getAirOffset(x, y, z)) return false;
                                break;
                            case '+'://must not be air
                                if (aBaseMetaTileEntity.getAirOffset(x, y, z)) return false;
                                break;
                            default: {//check for block (countable)
                                int pointer = block - '0';
                                //countable air -> net.minecraft.block.BlockAir
                                if (aBaseMetaTileEntity.getBlockOffset(x, y, z) != blockType[pointer]){
                                    if(TecTech.ModConfig.DEBUG_MODE)
                                        TecTech.Logger.info("Struct-block-error "+x+" "+y+" "+z+"/"+a+" "+c+"/"+aBaseMetaTileEntity.getBlockOffset (x,y,z)+" "+blockType[pointer]);
                                    return false;
                                }
                                if (aBaseMetaTileEntity.getMetaIDOffset(x, y, z) != blockMeta[pointer]){
                                    if(TecTech.ModConfig.DEBUG_MODE)
                                        TecTech.Logger.info("Struct-meta-id-error "+x+" "+y+" "+z+"/"+a+" "+c+"/"+aBaseMetaTileEntity.getMetaIDOffset(x,y,z)+" "+blockMeta[pointer]);
                                    return false;
                                }
                            }
                        }
                        a++;//block in horizontal layer
                    }
                }
                y--;//horizontal layer
            }
            c++;//depth
        }
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                commonValues.tecMark,
                "Nothing special just override me."
        };
    }

    @Override
    public void onRemoval() {
        try {
            if(eOutputHatches!=null) {
                for (GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental : eOutputHatches)
                    hatch_elemental.id = -1;
                for (GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental : eInputHatches)
                    hatch_elemental.id = -1;
                for (GT_MetaTileEntity_Hatch_Uncertainty hatch : eUncertainHatches)
                    hatch.getBaseMetaTileEntity().setActive(false);
                for (GT_MetaTileEntity_Hatch_Param hatch : eParamHatches)
                    hatch.getBaseMetaTileEntity().setActive(false);
            }
            if(outputEM!=null) {
                for (cElementalInstanceStackTree output : outputEM) {
                    if (output.hasStacks()) {
                        explodeMultiblock();
                        return;
                    }
                }
            }
        }catch (Exception e){
            if(TecTech.ModConfig.DEBUG_MODE) e.printStackTrace();
        }
    }

    protected boolean isInputEqual(boolean aDecreaseStacksizeBySuccess, boolean aDontCheckStackSizes, FluidStack[] requiredFluidInputs, ItemStack[] requiredInputs, FluidStack[] givenFluidInputs, ItemStack... givenInputs) {
        if(!GregTech_API.sPostloadFinished)return false;
        if (requiredFluidInputs.length > 0 && givenFluidInputs == null) return false;
        int amt;
        for (FluidStack tFluid : requiredFluidInputs)
            if (tFluid != null) {
                boolean temp = true;
                amt = tFluid.amount;
                for (FluidStack aFluid : givenFluidInputs)
                    if (aFluid != null && aFluid.isFluidEqual(tFluid)){
                        if (aDontCheckStackSizes ){
                            temp = false;
                            break;
                        }
                        amt -= aFluid.amount;
                        if (amt<1){
                            temp = false;
                            break;
                        }
                    }
                if (temp) return false;
            }

        if (requiredInputs.length > 0 && givenInputs == null) return false;
        for (ItemStack tStack : requiredInputs) {
            if (tStack != null) {
                amt = tStack.stackSize;
                boolean temp = true;
                for (ItemStack aStack : givenInputs) {
                    if ((GT_Utility.areUnificationsEqual(aStack, tStack, true) || GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tStack, true))) {
                        if (aDontCheckStackSizes) {
                            temp = false;
                            break;
                        }
                        amt -= aStack.stackSize;
                        if (amt < 1) {
                            temp = false;
                            break;
                        }
                    }
                }
                if (temp) return false;
            }
        }

        if (aDecreaseStacksizeBySuccess) {
            if (givenFluidInputs != null) {
                for (FluidStack tFluid : requiredFluidInputs) {
                    if (tFluid != null) {
                        amt = tFluid.amount;
                        for (FluidStack aFluid : givenFluidInputs) {
                            if (aFluid != null && aFluid.isFluidEqual(tFluid)) {
                                if (aDontCheckStackSizes) {
                                    aFluid.amount -= amt;
                                    break;
                                }
                                if (aFluid.amount < amt) {
                                    amt -= aFluid.amount;
                                    aFluid.amount = 0;
                                } else {
                                    aFluid.amount -= amt;
                                    amt = 0;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if (givenInputs != null) {
                for (ItemStack tStack : requiredInputs) {
                    if (tStack != null) {
                        amt = tStack.stackSize;
                        for (ItemStack aStack : givenInputs) {
                            if ((GT_Utility.areUnificationsEqual(aStack, tStack, true) || GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tStack, true))) {
                                if (aDontCheckStackSizes){
                                    aStack.stackSize -= amt;
                                    break;
                                }
                                if (aStack.stackSize < amt){
                                    amt -= aStack.stackSize;
                                    aStack.stackSize = 0;
                                }else{
                                    aStack.stackSize -= amt;
                                    amt = 0;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * finds a Recipe matching the aFluid and ItemStack Inputs.
     *
     * @param aNotUnificated if this is T the Recipe searcher will unificate the ItemStack Inputs
     * @param aVoltage       Voltage of the Machine or Long.MAX_VALUE if it has no Voltage
     * @param aFluids        the Fluid Inputs
     * @param aSpecialSlot   the content of the Special Slot, the regular Manager doesn't do anything with this, but some custom ones do.
     * @param aInputs        the Item Inputs
     * @return the Recipe it has found or null for no matching Recipe
     */

    //protected static GT_Recipe findRecipe(boolean aNotUnificated, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot, ItemStack... aInputs) {
    //    // Some Recipe Classes require a certain amount of Inputs of certain kinds. Like "at least 1 Fluid + 1 Stack" or "at least 2 Stacks" before they start searching for Recipes.
    //    // This improves Performance massively, especially if people leave things like Circuits, Molds or Shapes in their Machines to select Sub Recipes.
    //    if (GregTech_API.sPostloadFinished) {
    //        if (mMinimalInputFluids > 0) {
    //            if (aFluids == null) return null;
    //            int tAmount = 0;
    //            for (FluidStack aFluid : aFluids) if (aFluid != null) tAmount++;
    //            if (tAmount < mMinimalInputFluids) return null;
    //        }
    //        if (mMinimalInputItems > 0) {
    //            if (aInputs == null) return null;
    //            int tAmount = 0;
    //            for (ItemStack aInput : aInputs) if (aInput != null) tAmount++;
    //            if (tAmount < mMinimalInputItems) return null;
    //        }
    //    }
    //    // Unification happens here in case the Input isn't already unificated.
    //    if (aNotUnificated) aInputs = GT_OreDictUnificator.getStackArray(true, (Object[]) aInputs);
    //    // Check the Recipe which has been used last time in order to not have to search for it again, if possible.
    //    if (aRecipe != null)
    //        if (!aRecipe.mFakeRecipe && aRecipe.mCanBeBuffered && aRecipe.isRecipeInputEqual(false, true, aFluids, aInputs))
    //            return aRecipe.mEnabled && aVoltage * mAmperage >= aRecipe.mEUt ? aRecipe : null;
    //    // Now look for the Recipes inside the Item HashMaps, but only when the Recipes usually have Items.
    //    if (mUsualInputCount > 0 && aInputs != null) for (ItemStack tStack : aInputs)
    //        if (tStack != null) {
    //            Collection<GT_Recipe>
    //                    tRecipes = mRecipeItemMap.get(new GT_ItemStack(tStack));
    //            if (tRecipes != null) for (GT_Recipe tRecipe : tRecipes)
    //                if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs))
    //                    return tRecipe.mEnabled && aVoltage * mAmperage >= tRecipe.mEUt ? tRecipe : null;
    //            tRecipes = mRecipeItemMap.get(new GT_ItemStack(GT_Utility.copyMetaData(W, tStack)));
    //            if (tRecipes != null) for (GT_Recipe tRecipe : tRecipes)
    //                if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs))
    //                    return tRecipe.mEnabled && aVoltage * mAmperage >= tRecipe.mEUt ? tRecipe : null;
    //        }
    //    // If the minimal Amount of Items for the Recipe is 0, then it could be a Fluid-Only Recipe, so check that Map too.
    //    if (mMinimalInputItems == 0 && aFluids != null) for (FluidStack aFluid : aFluids)
    //        if (aFluid != null) {
    //            Collection<GT_Recipe>
    //                    tRecipes = mRecipeFluidMap.get(aFluid.getFluid());
    //            if (tRecipes != null) for (GT_Recipe tRecipe : tRecipes)
    //                if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs))
    //                    return tRecipe.mEnabled && aVoltage * mAmperage >= tRecipe.mEUt ? tRecipe : null;
    //        }
    //    // And nothing has been found.
    //    return null;
    //}
}
