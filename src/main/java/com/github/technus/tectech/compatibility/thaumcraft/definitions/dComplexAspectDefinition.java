package com.github.technus.tectech.compatibility.thaumcraft.definitions;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.elementalMatter.core.*;
import com.github.technus.tectech.elementalMatter.core.templates.cElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.containers.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.core.transformations.aFluidDequantizationInfo;
import com.github.technus.tectech.elementalMatter.core.transformations.aItemDequantizationInfo;
import com.github.technus.tectech.elementalMatter.core.transformations.aOredictDequantizationInfo;
import com.github.technus.tectech.elementalMatter.definitions.primitive.eBosonDefinition;
import com.github.technus.tectech.elementalMatter.core.interfaces.iElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;

import static com.github.technus.tectech.auxiliary.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.elementalMatter.core.cElementalDecay.noDecay;
import static com.github.technus.tectech.compatibility.thaumcraft.definitions.AspectDefinitionCompat.aspectDefinitionCompat;

/**
 * Created by Tec on 06.05.2017.
 */
public final class dComplexAspectDefinition extends cElementalDefinition implements iElementalAspect {
    private final int hash;
    public final float mass;

    private static final byte nbtType = (byte) 'c';

    private final cElementalDefinitionStackMap aspectStacks;

    @Deprecated
    public dComplexAspectDefinition(cElementalDefinition... aspects) throws tElementalException {
        this(true, new cElementalDefinitionStackMap(aspects));
    }

    @Deprecated
    private dComplexAspectDefinition(boolean check, cElementalDefinition... aspects) throws tElementalException {
        this(check, new cElementalDefinitionStackMap(aspects));
    }

    public dComplexAspectDefinition(cElementalDefinitionStack... aspects) throws tElementalException {
        this(true, new cElementalDefinitionStackMap(aspects));
    }

    private dComplexAspectDefinition(boolean check, cElementalDefinitionStack... aspects) throws tElementalException {
        this(check, new cElementalDefinitionStackMap(aspects));
    }

    public dComplexAspectDefinition(cElementalDefinitionStackMap aspects) throws tElementalException {
        this(true, aspects);
    }

    private dComplexAspectDefinition(boolean check, cElementalDefinitionStackMap aspects) throws tElementalException {
        if (check && !canTheyBeTogether(aspects)) throw new tElementalException("Hadron Definition error");
        this.aspectStacks = aspects;
        float mass=0;
        for(cElementalDefinitionStack stack:aspects.values()){
            mass+=stack.getMass();
        }
        this.mass=mass;
        hash=super.hashCode();
    }

    //public but u can just try{}catch(){} the constructor it still calls this method
    private static boolean canTheyBeTogether(cElementalDefinitionStackMap stacks) {
        int amount = 0;
        for (cElementalDefinitionStack aspects : stacks.values()) {
            if (aspects.definition instanceof dComplexAspectDefinition || aspects.definition instanceof ePrimalAspectDefinition)
                amount += aspects.amount;
            else return false;
        }
        return amount==2;
    }

    @Override
    public String getName() {
        String name= aspectDefinitionCompat.getAspectTag(this);
        if(name!=null){
            name=name.substring(0,1).toUpperCase()+name.substring(1);
        }else{
            name=getSymbol();
        }
        return "Aspect: "+name;
    }

    @Override
    public String getSymbol() {
        String symbol = "";
        for (cElementalDefinitionStack aspect : aspectStacks.values()) {
            if (aspect.definition instanceof ePrimalAspectDefinition) {
                for (int i = 0; i < aspect.amount; i++) {
                    symbol += aspect.definition.getSymbol();
                }
            } else {
                symbol+="(";
                for (int i = 0; i < aspect.amount; i++) {
                    symbol += aspect.definition.getSymbol();
                }
                symbol+=")";
            }
        }
        return symbol;
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("t", nbtType);
        cElementalDefinitionStack[] quarkStacksValues = aspectStacks.values();
        nbt.setInteger("i", quarkStacksValues.length);
        for (int i = 0; i < quarkStacksValues.length; i++)
            nbt.setTag(Integer.toString(i), quarkStacksValues[i].toNBT());
        return nbt;
    }

    public static iElementalDefinition fromNBT(NBTTagCompound nbt) {
        cElementalDefinitionStack[] stacks = new cElementalDefinitionStack[nbt.getInteger("i")];
        for (int i = 0; i < stacks.length; i++)
            stacks[i] = cElementalDefinitionStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
        try {
            return new dComplexAspectDefinition(stacks);
        } catch (tElementalException e) {
            if (DEBUG_MODE) e.printStackTrace();
            return null;
        }
    }

    @Override
    public float getRawLifeTime() {
        return -1;
    }

    @Override
    public int getCharge() {
        return 0;
    }

    @Override
    public byte getType() {
        return 0;
    }

    @Override
    public byte getColor() {
        return -1;
    }

    @Override
    public cElementalDefinitionStackMap getSubParticles() {
        return aspectStacks;
    }

    @Override
    public cElementalDecay[] getEnergeticDecayInstant() {
        return new cElementalDecay[]{new cElementalDecay(0.75F, aspectStacks), eBosonDefinition.deadEnd};
    }

    @Override
    public cElementalDecay[] getNaturalDecayInstant() {
        return noDecay;
    }

    @Override
    public cElementalDecay[] getDecayArray() {
        return noDecay;
    }

    @Override
    public float getMass() {
        return mass;
    }

    @Override
    public aFluidDequantizationInfo someAmountIntoFluidStack() {
        return null;
    }

    @Override
    public aItemDequantizationInfo someAmountIntoItemsStack() {
        return null;
    }

    @Override
    public aOredictDequantizationInfo someAmountIntoOredictStack() {
        return null;
    }

    public Object materializeIntoAspect() {
        return aspectDefinitionCompat.getAspect(this);
    }

    @Override
    public iElementalDefinition getAnti() {
        return null;
    }

    public static void run() {
        try {
            cElementalDefinition.addCreatorFromNBT(nbtType, dComplexAspectDefinition.class.getMethod("fromNBT", NBTTagCompound.class),(byte)-96);
        } catch (Exception e) {
            if (DEBUG_MODE) e.printStackTrace();
        }
        if(DEBUG_MODE)
            TecTech.Logger.info("Registered Elemental Matter Class: ComplexAspect "+nbtType+" "+(-96));
    }

    @Override
    public byte getClassType() {
        return -96;
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
