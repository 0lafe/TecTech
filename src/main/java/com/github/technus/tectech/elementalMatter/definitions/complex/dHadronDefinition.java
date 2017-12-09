package com.github.technus.tectech.elementalMatter.definitions.complex;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.elementalMatter.core.cElementalDecay;
import com.github.technus.tectech.elementalMatter.core.cElementalDefinitionStackMap;
import com.github.technus.tectech.elementalMatter.core.containers.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.core.interfaces.iElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.tElementalException;
import com.github.technus.tectech.elementalMatter.core.templates.cElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.transformations.aFluidDequantizationInfo;
import com.github.technus.tectech.elementalMatter.core.transformations.aItemDequantizationInfo;
import com.github.technus.tectech.elementalMatter.core.transformations.aOredictDequantizationInfo;
import com.github.technus.tectech.elementalMatter.definitions.primitive.eBosonDefinition;
import com.github.technus.tectech.elementalMatter.definitions.primitive.eQuarkDefinition;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

import static com.github.technus.tectech.auxiliary.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.elementalMatter.definitions.primitive.eBosonDefinition.boson_Y__;

/**
 * Created by danie_000 on 17.11.2016.
 */
public final class dHadronDefinition extends cElementalDefinition {//TODO Optimize map i/o
    private final int hash;

    private static final byte nbtType = (byte) 'h';
    //Helpers
    public static dHadronDefinition hadron_p, hadron_n, hadron_p_, hadron_n_;
    public static cElementalDefinitionStack hadron_p1, hadron_n1, hadron_p2, hadron_n2;
    private static float protonMass = 0F;
    private static float neutronMass = 0F;

    //float-mass in eV/c^2
    public final float mass;
    //int -electric charge in 1/3rds of electron charge for optimization
    public final int charge;
    public final float rawLifeTime;
    public final byte amount;
    //generation max present inside - minus if contains any antiquark
    public final byte type;
    //private final FluidStack fluidThing;
    //private final ItemStack itemThing;

    private final cElementalDefinitionStackMap quarkStacks;

    @Deprecated
    public dHadronDefinition(eQuarkDefinition... quarks) throws tElementalException {
        this(true, new cElementalDefinitionStackMap(quarks));
    }

    @Deprecated
    private dHadronDefinition(boolean check, eQuarkDefinition... quarks) throws tElementalException {
        this(check, new cElementalDefinitionStackMap(quarks));
    }

    public dHadronDefinition(cElementalDefinitionStack... quarks) throws tElementalException {
        this(true, new cElementalDefinitionStackMap(quarks));
    }

    private dHadronDefinition(boolean check, cElementalDefinitionStack... quarks) throws tElementalException {
        this(check, new cElementalDefinitionStackMap(quarks));
    }

    public dHadronDefinition(cElementalDefinitionStackMap quarks) throws tElementalException {
        this(true, quarks);
    }

    private dHadronDefinition(boolean check, cElementalDefinitionStackMap quarks) throws tElementalException {
        if (check && !canTheyBeTogether(quarks)) throw new tElementalException("Hadron Definition error");
        this.quarkStacks = quarks;

        byte amount = 0;
        int charge = 0;
        int type = 0;
        boolean containsAnti = false;
        float mass = 0;
        for (cElementalDefinitionStack quarkStack : quarkStacks.values()) {
            amount += quarkStack.amount;
            mass += quarkStack.getMass();
            charge += quarkStack.getCharge();
            type = Math.max(Math.abs(quarkStack.definition.getType()), type);
            if (quarkStack.definition.getType() < 0) containsAnti = true;
        }
        this.amount = amount;
        this.charge = charge;
        this.type = containsAnti ? (byte) (-type) : (byte) type;
        int mult = this.amount * this.amount * (this.amount - 1);
        this.mass = mass * 5.543F * (float) mult;//yes it becomes heavier

        if (this.mass == protonMass && this.amount == 3) this.rawLifeTime = STABLE_RAW_LIFE_TIME;
        else if (this.mass == neutronMass && this.amount == 3) this.rawLifeTime = 882F;
        else {
            if (this.amount == 3) {
                this.rawLifeTime = (1.34F / this.mass) * (float) Math.pow(9.81, charge);
            } else if (this.amount == 2) {
                this.rawLifeTime = (1.21F / this.mass) / (float) Math.pow(19.80, charge);
            } else {
                this.rawLifeTime = (1.21F / this.mass) / (float) Math.pow(9.80, charge);
            }
        }

        hash=super.hashCode();
    }

    //public but u can just try{}catch(){} the constructor it still calls this method
    private static boolean canTheyBeTogether(cElementalDefinitionStackMap stacks) {
        long amount = 0;
        for (cElementalDefinitionStack quarks : stacks.values()) {
            if (!(quarks.definition instanceof eQuarkDefinition)) return false;
            amount += quarks.amount;
        }
        return amount >= 2 && amount <= 12;
    }

    @Override
    public String getName() {
        String name;
        switch (amount) {
            case 2:
                name = "Meson:";
                break;
            case 3:
                name = "Baryon:";
                break;
            case 4:
                name = "Tetraquark:";
                break;
            case 5:
                name = "Pentaquark:";
                break;
            case 6:
                name = "Hexaquark:";
                break;
            default:
                name = "Hadron:";
        }
        for (cElementalDefinitionStack quark : quarkStacks.values()) {
            name += " " + quark.definition.getSymbol() + quark.amount;
        }
        return name;
    }

    @Override
    public String getSymbol() {
        String symbol = "";
        for (cElementalDefinitionStack quark : quarkStacks.values())
            for (int i = 0; i < quark.amount; i++)
                symbol += quark.definition.getSymbol();
        return symbol;
    }

    @Override
    public byte getColor() {
        return -7;
    }

    @Override
    public cElementalDefinitionStackMap getSubParticles() {
        return quarkStacks;
    }

    @Override
    public cElementalDecay[] getNaturalDecayInstant() {
        cElementalDefinitionStack[] quarkStacks = this.quarkStacks.values();
        if (amount == 2 && quarkStacks.length == 2 && quarkStacks[0].definition.getMass() == quarkStacks[1].definition.getMass() && quarkStacks[0].definition.getType() == -quarkStacks[1].definition.getType())
            return cElementalDecay.noProduct;
        ArrayList<cElementalDefinitionStack> decaysInto = new ArrayList<>();
        for (cElementalDefinitionStack quarks : quarkStacks) {
            if (quarks.definition.getType() == 1 || quarks.definition.getType() == -1) {
                //covers both quarks and antiquarks
                decaysInto.add(quarks);
            } else {
                //covers both quarks and antiquarks
                decaysInto.add(new cElementalDefinitionStack(boson_Y__, 2));
            }
        }
        return new cElementalDecay[]{
                new cElementalDecay(0.75F, decaysInto.toArray(new cElementalDefinitionStack[decaysInto.size()])),
                eBosonDefinition.deadEnd
        };
    }

    @Override
    public cElementalDecay[] getEnergyInducedDecay(long energy) {
        cElementalDefinitionStack[] quarkStacks = this.quarkStacks.values();
        if (amount == 2 && quarkStacks.length == 2 && quarkStacks[0].definition.getMass() == quarkStacks[1].definition.getMass() && quarkStacks[0].definition.getType() == -quarkStacks[1].definition.getType())
            return cElementalDecay.noProduct;
        return new cElementalDecay[]{new cElementalDecay(0.75F, quarkStacks), eBosonDefinition.deadEnd}; //decay into quarks
    }

    @Override
    public boolean usesSpecialEnergeticDecayHandling() {
        return false;
    }

    @Override
    public cElementalDecay[] getDecayArray() {
        cElementalDefinitionStack[] quarkStacks = this.quarkStacks.values();
        if (amount == 2 && quarkStacks.length == 2 && quarkStacks[0].definition.getMass() == quarkStacks[1].definition.getMass() && quarkStacks[0].definition.getType() == -quarkStacks[1].definition.getType())
            return cElementalDecay.noProduct;
        else if (amount != 3)
            return new cElementalDecay[]{new cElementalDecay(0.95F, quarkStacks), eBosonDefinition.deadEnd}; //decay into quarks
        else {
            ArrayList<eQuarkDefinition> newBaryon = new ArrayList<eQuarkDefinition>();
            iElementalDefinition[] Particles = new iElementalDefinition[2];
            for (cElementalDefinitionStack quarks : quarkStacks) {
                for (int i = 0; i < quarks.amount; i++)
                    newBaryon.add((eQuarkDefinition) quarks.definition);
            }
            //remove last
            eQuarkDefinition lastQuark = newBaryon.remove(2);

            if (Math.abs(lastQuark.getType()) > 1) {
                cElementalDefinitionStack[] decay = lastQuark.getDecayArray()[1].outputStacks.values();
                newBaryon.add((eQuarkDefinition) decay[0].definition);
                Particles[0] = decay[1].definition;
                Particles[1] = decay[2].definition;
            } else {
                cElementalDefinitionStack[] decay = lastQuark.getDecayArray()[0].outputStacks.values();
                newBaryon.add((eQuarkDefinition) decay[0].definition);
                Particles[0] = decay[1].definition;
                Particles[1] = decay[2].definition;
            }

            eQuarkDefinition[] contentOfBaryon = newBaryon.toArray(new eQuarkDefinition[3]);

            try {
                return new cElementalDecay[]{
                        new cElementalDecay(0.99F, new dHadronDefinition(false, contentOfBaryon), Particles[0], Particles[1]),
                        new cElementalDecay(0.001F, new dHadronDefinition(false, contentOfBaryon), Particles[0], Particles[1], boson_Y__),
                        eBosonDefinition.deadEnd};
            } catch (tElementalException e) {
                if (DEBUG_MODE) e.printStackTrace();
                return new cElementalDecay[]{eBosonDefinition.deadEnd};
            }
        }
    }

    @Override
    public float getMass() {
        return mass;
    }

    @Override
    public int getCharge() {
        return charge;
    }

    @Override
    public float getRawTimeSpan() {
        return rawLifeTime;
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return true;
    }

    @Override
    public byte getType() {
        return type;
    }

    @Override
    public iElementalDefinition getAnti() {
        cElementalDefinitionStack[] stacks = this.quarkStacks.values();
        cElementalDefinitionStack[] antiElements = new cElementalDefinitionStack[stacks.length];
        for (int i = 0; i < antiElements.length; i++) {
            antiElements[i] = new cElementalDefinitionStack(stacks[i].definition.getAnti(), stacks[i].amount);
        }
        try {
            return new dHadronDefinition(false, antiElements);
        } catch (tElementalException e) {
            if (DEBUG_MODE) e.printStackTrace();
            return null;
        }
    }

    //@Override
    //public iElementalDefinition getAnti() {
    //    cElementalMutableDefinitionStackMap anti = new cElementalMutableDefinitionStackMap();
    //    for (cElementalDefinitionStack stack : quarkStacks.values())
    //        anti.putReplace(new cElementalDefinitionStack(stack.definition.getAnti(), stack.amount));
    //    try {
    //        return new dHadronDefinition(anti.toImmutable());
    //    } catch (tElementalException e) {
    //        if (TecTechConfig.DEBUG_MODE) e.printStackTrace();
    //        return null;
    //    }
    //}

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

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("t", nbtType);
        cElementalDefinitionStack[] quarkStacksValues = quarkStacks.values();
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
            return new dHadronDefinition(stacks);
        } catch (tElementalException e) {
            if (DEBUG_MODE) e.printStackTrace();
            return null;
        }
    }

    public static void run() {
        try {
            hadron_p = new dHadronDefinition(new cElementalDefinitionStackMap(eQuarkDefinition.quark_u.getStackForm(2), eQuarkDefinition.quark_d.getStackForm(1)));
            protonMass = hadron_p.mass;
            //redefine the proton with proper lifetime (the lifetime is based on mass comparison)
            hadron_p = new dHadronDefinition(new cElementalDefinitionStackMap(eQuarkDefinition.quark_u.getStackForm(2), eQuarkDefinition.quark_d.getStackForm(1)));
            hadron_p_ = (dHadronDefinition) (hadron_p.getAnti());
            hadron_n = new dHadronDefinition(new cElementalDefinitionStackMap(eQuarkDefinition.quark_u.getStackForm(1), eQuarkDefinition.quark_d.getStackForm(2)));
            neutronMass = hadron_n.mass;
            //redefine the neutron with proper lifetime (the lifetime is based on mass comparison)
            hadron_n = new dHadronDefinition(new cElementalDefinitionStackMap(eQuarkDefinition.quark_u.getStackForm(1), eQuarkDefinition.quark_d.getStackForm(2)));
            hadron_n_ = (dHadronDefinition) (hadron_n.getAnti());
        } catch (tElementalException e) {
            if (DEBUG_MODE) e.printStackTrace();
            protonMass = -1;
            neutronMass = -1;
        }
        hadron_p1 = new cElementalDefinitionStack(hadron_p, 1);
        hadron_n1 = new cElementalDefinitionStack(hadron_n, 1);
        hadron_p2 = new cElementalDefinitionStack(hadron_p, 2);
        hadron_n2 = new cElementalDefinitionStack(hadron_n, 2);

        try {
            cElementalDefinition.addCreatorFromNBT(nbtType, dHadronDefinition.class.getMethod("fromNBT", NBTTagCompound.class),(byte)-64);
        } catch (Exception e) {
            if (DEBUG_MODE) e.printStackTrace();
        }
        if(DEBUG_MODE)
            TecTech.Logger.info("Registered Elemental Matter Class: Hadron "+nbtType+" "+(-64));
    }

    @Override
    public byte getClassType() {
        return -64;
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
