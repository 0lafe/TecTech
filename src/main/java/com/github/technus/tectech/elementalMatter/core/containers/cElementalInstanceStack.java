package com.github.technus.tectech.elementalMatter.core.containers;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.elementalMatter.core.cElementalDecay;
import com.github.technus.tectech.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.elementalMatter.core.interfaces.iElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.interfaces.iHasElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.templates.cElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;

import static com.github.technus.tectech.elementalMatter.definitions.primitive.cPrimitiveDefinition.null__;

/**
 * Created by danie_000 on 22.10.2016.
 */
public final class cElementalInstanceStack implements iHasElementalDefinition {
    public final iElementalDefinition definition;
    //energy - if positive then particle should try to decay
    public int energy;
    //byte color; 0=Red 1=Green 2=Blue 0=Cyan 1=Magenta 2=Yellow, else ignored (-1 - uncolorable)
    private byte color;
    public long age;
    public int amount;
    private float lifeTime;
    private float lifeTimeMult;

    public cElementalInstanceStack(cElementalDefinitionStack stackSafe) {
        this(stackSafe.definition, stackSafe.amount, 1F, 0, 0);
    }

    public cElementalInstanceStack(cElementalDefinitionStack stackSafe, float lifeTimeMult, long age, int energy) {
        this(stackSafe.definition, stackSafe.amount, lifeTimeMult, age, energy);
    }

    public cElementalInstanceStack(iElementalDefinition defSafe, int amount) {
        this(defSafe, amount, 1F, 0, 0);
    }

    public cElementalInstanceStack(iElementalDefinition defSafe, int amount, float lifeTimeMult, long age, int energy) {
        this.definition = defSafe == null ? null__ : defSafe;
        byte color = definition.getColor();
        if (color < 0 || color > 2) {//transforms colorable??? into proper color
            this.color = color;
        } else {
            this.color = (byte) (TecTech.Rnd.nextInt(3));
        }
        this.lifeTimeMult = lifeTimeMult;
        this.lifeTime = definition.getRawLifeTime() * this.lifeTimeMult;
        this.age = age;
        this.energy = energy;
        this.amount = amount;
    }

    //Clone proxy
    private cElementalInstanceStack(cElementalInstanceStack stack) {
        definition = stack.definition;
        energy = stack.energy;
        color = stack.color;
        age = stack.age;
        amount = stack.amount;
        lifeTime = stack.lifeTime;
        lifeTimeMult = stack.lifeTimeMult;
    }

    @Override
    public final cElementalInstanceStack clone() {
        return new cElementalInstanceStack(this);
    }

    @Override
    public int getAmount() {
        return amount;
    }

    public int getCharge() {
        return definition.getCharge() * amount;
    }

    public float getMass() {
        return definition.getMass() * amount;
    }

    public cElementalDefinitionStack getDefinitionStack() {
        return new cElementalDefinitionStack(definition, amount);
    }

    @Override
    public iElementalDefinition getDefinition() {
        return definition;
    }

    public byte getColor() {
        return color;
    }

    public byte setColor(byte color) {//does not allow changing magic element
        if (this.color < 0 || this.color > 2 || color < 0 || color >= 3) return this.color;
        return this.color = color;
    }

    public byte nextColor() {//does not allow changing magic element
        if (this.color < 0 || this.color > 2) return this.color;
        return this.color = (byte) (TecTech.Rnd.nextInt(3));
    }

    public float getLifeTime() {
        return lifeTime;
    }

    public float multLifeTime(float mult) {
        this.lifeTimeMult = mult;
        this.lifeTime = definition.getRawLifeTime() * mult;
        return this.lifeTime;
    }

    public float getLifeTimeMult() {
        return lifeTimeMult;
    }

    public cElementalInstanceStackMap decay() {
        return decay(1F, age, 0);//try to decay without changes
    }

    public cElementalInstanceStackMap decay(long apparentAge, int postEnergize) {
        return decay(1F,apparentAge,postEnergize);
    }

    public cElementalInstanceStackMap decay(Float lifeTimeMult, long apparentAge, int postEnergize) {
        if (this.energy > 0) {
            this.energy--;
            return decayCompute(definition.getEnergeticDecayInstant(), lifeTimeMult, 0, postEnergize + this.energy);
        } else if (definition.getRawLifeTime() < 0) {
            return null;//return null, decay cannot be achieved
        } else if (1F > this.lifeTime) {
            return decayCompute(definition.getNaturalDecayInstant(), lifeTimeMult, 0, postEnergize + this.energy);
        } else if (((float) apparentAge) > this.lifeTime) {
            return decayCompute(definition.getDecayArray(), lifeTimeMult, 0, postEnergize + this.energy);
        }
        return null;//return null since decay cannot be achieved
    }

    //Use to get direct decay output providing correct decay array
    public cElementalInstanceStackMap decayCompute(cElementalDecay[] decays, float lifeTimeMult, long newProductsAge, int energy) {
        if (decays == null) return null;//Can not decay so it wont
        else if (decays.length == 0)
            return new cElementalInstanceStackMap();//provide non null 0 length array for annihilation
        else if (decays.length == 1) {//only one type of decay :D, doesn't need dead end
            return decays[0].getResults(lifeTimeMult, newProductsAge, energy, this.amount);
        } else {
            cElementalInstanceStackMap output = new cElementalInstanceStackMap();
            final int differentDecays = decays.length;
            int[] qttyOfDecay = new int[differentDecays];
            int amountRemaining = this.amount, amount = this.amount;
            float remainingProbability = 1F;

            for (int i = 0; i < differentDecays; i++) {
                if (decays[i].probability > 1F) {
                    int thisDecayAmount = (int) (Math.floor(remainingProbability * (float) amount));
                    if (thisDecayAmount == 0) {
                        //remainingProbability=something;
                        break;
                    } else if (thisDecayAmount <= amountRemaining) {
                        amountRemaining -= thisDecayAmount;
                        qttyOfDecay[i] += thisDecayAmount;
                        break;
                    }
                    //in case too much was made
                    qttyOfDecay[i] += amountRemaining;
                    amountRemaining = 0;
                    //remainingProbability=0;
                    break;
                }
                int thisDecayAmount = (int) (Math.floor(decays[i].probability * (float) amount));
                if (thisDecayAmount <= amountRemaining && thisDecayAmount > 0) {//some was made
                    remainingProbability -= (decays[i].probability);
                    amountRemaining -= thisDecayAmount;
                    qttyOfDecay[i] += thisDecayAmount;
                } else if (thisDecayAmount > amountRemaining) {//too much was made
                    qttyOfDecay[i] += amountRemaining;
                    amountRemaining = 0;
                    //remainingProbability=0;
                    break;
                }//if 0
            }

            for (int i = 0; i < amountRemaining; i++) {
                double rand = (double) (TecTech.Rnd.nextFloat());
                for (int j = 0; j < differentDecays; j++) {//looking for the thing it decayed into
                    rand -= (double) (decays[j].probability);
                    if (rand <= 0D) {
                        qttyOfDecay[j]++;
                        break;
                    }
                }
            }

            for (int i = 0; i < differentDecays; i++) {
                if (qttyOfDecay[i] > 0)
                    output.putUnifyAll(decays[i].getResults(lifeTimeMult, newProductsAge, energy, qttyOfDecay[i]));
            }
            return output;
        }
    }

    public cElementalInstanceStack unifyIntoThis(cElementalInstanceStack... instances) {
        if (instances == null) return this;
        //returns with the definition from the first object passed
        int energy = this.energy * this.amount;
        float lifeTimeMul = this.lifeTimeMult;

        for (cElementalInstanceStack instance : instances) {
            if (instance != null && this.compareTo(instance) == 0) {
                this.amount += instance.amount;
                energy += instance.energy * instance.amount;
                lifeTimeMul = Math.min(lifeTimeMul, instance.lifeTimeMult);
                this.age = Math.max(this.age, instance.age);
            }
        }

        if (amount != 0) energy /= Math.abs(amount);

        this.energy = energy;
        this.multLifeTime(lifeTimeMul);
        return this;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("d", definition.toNBT());
        nbt.setInteger("q", amount);
        nbt.setInteger("e", energy);
        nbt.setByte("c", color);
        nbt.setLong("a", age);
        nbt.setFloat("m", lifeTimeMult);
        return nbt;
    }

    public static cElementalInstanceStack fromNBT(NBTTagCompound nbt) {
        NBTTagCompound definition = nbt.getCompoundTag("d");
        cElementalInstanceStack instance = new cElementalInstanceStack(
                cElementalDefinition.fromNBT(definition),
                nbt.getInteger("q"),
                nbt.getFloat("m"),
                nbt.getLong("a"),
                nbt.getInteger("e"));
        instance.setColor(nbt.getByte("c"));
        return instance;
    }

    @Override
    public int compareTo(iHasElementalDefinition o) {//use for unification
        return definition.compareTo(o.getDefinition());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof iElementalDefinition)
            return definition.compareTo((iElementalDefinition) obj) == 0;
        if (obj instanceof iHasElementalDefinition)
            return definition.compareTo(((iHasElementalDefinition) obj).getDefinition()) == 0;
        return false;
    }

    //Amount shouldn't be hashed if this is just indicating amount and not structure, DOES NOT CARE ABOUT INSTANCE INFO
    @Override
    public int hashCode() {
        return definition.hashCode();
    }

    @Override
    public String toString() {
        return definition.getName()+ '\n' + definition.getSymbol() + '\n' + amount + '\n' + getMass();
    }
}
