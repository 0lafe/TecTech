package com.github.technus.tectech.elementalMatter.classes;

import com.github.technus.tectech.elementalMatter.interfaces.iElementalDefinition;
import com.github.technus.tectech.elementalMatter.interfaces.iHasElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.github.technus.tectech.elementalMatter.definitions.cPrimitiveDefinition.nbtE__;

/**
 * Created by danie_000 on 22.01.2017.
 */
public final class cElementalInstanceStackMap implements Comparable<cElementalInstanceStackMap> {
    private TreeMap<iElementalDefinition, cElementalInstanceStack> map;

    //Constructors
    public cElementalInstanceStackMap() {
        map = new TreeMap<>();
    }

    @Deprecated
    public cElementalInstanceStackMap(cElementalInstanceStack... inSafe) {
        this(true, inSafe);
    }

    @Deprecated
    public cElementalInstanceStackMap(boolean clone, cElementalInstanceStack... in) {
        map = new TreeMap<>();
        if (clone) {
            cElementalInstanceStack[] stacks=new cElementalInstanceStack[in.length];
            for(int i=0;i<stacks.length;i++)
                stacks[i]=in[i].clone();
            putUnifyAll(stacks);
        } else {
            putUnifyAll(in);
        }
    }

    private cElementalInstanceStackMap(TreeMap<iElementalDefinition, cElementalInstanceStack> inSafe) {
        this(true, inSafe);
    }

    @Deprecated
    private cElementalInstanceStackMap(boolean clone, TreeMap<iElementalDefinition, cElementalInstanceStack> in) {
        if (clone) {
            map = new TreeMap<>();
            for(cElementalInstanceStack stack:in.values())
                putUnify(stack.clone());
        } else {
            map = in;
        }
    }

    public cElementalInstanceStackMap(cElementalInstanceStackMap inSafe) {
        this(true, inSafe.map);
    }

    public cElementalInstanceStackMap(boolean copy, cElementalInstanceStackMap in) {
        this(copy, in.map);
    }

    @Override
    public final cElementalInstanceStackMap clone() {
        return new cElementalInstanceStackMap(map);
    }

    public final cElementalStackMap toDefinitionMapForComparison() {
        cElementalDefinitionStack[] list = new cElementalDefinitionStack[map.size()];
        int i = 0;
        for (cElementalInstanceStack stack : map.values()) {
            list[i++] = new cElementalDefinitionStack(stack.definition, stack.amount);
        }
        return new cElementalMutableDefinitionStackMap(list);
    }

    @Deprecated
    public final cElementalStackMap toDefinitionMap(boolean mutable) {
        TreeMap<iElementalDefinition, cElementalDefinitionStack> newMap = new TreeMap<>();
        for (cElementalInstanceStack stack : map.values()) {
            newMap.put(stack.definition, new cElementalDefinitionStack(stack.definition, stack.amount));
        }
        if (mutable) return new cElementalMutableDefinitionStackMap(newMap);
        return new cElementalDefinitionStackMap(newMap);
    }

    @Deprecated
    public Map<iElementalDefinition, cElementalInstanceStack> getRawMap() {
        return map;
    }

    //Removers
    public void clear() {
        map.clear();
    }

    public cElementalInstanceStack remove(iElementalDefinition def) {
        return map.remove(def);
    }

    @Deprecated
    public cElementalInstanceStack remove(iHasElementalDefinition has) {
        return map.remove(has.getDefinition());
    }

    public void removeAll(iElementalDefinition... definitions) {
        for (iElementalDefinition def : definitions)
            map.remove(def);
    }

    @Deprecated
    private void removeAll(iHasElementalDefinition... hasElementals) {
        for (iHasElementalDefinition has : hasElementals)
            map.remove(has.getDefinition());
    }

    //Remove amounts
    public boolean removeAmount(boolean testOnly, cElementalInstanceStack instance) {
        final cElementalInstanceStack target = map.get(instance.definition);
        if (target == null)
            return false;
        if (testOnly)
            return target.amount >= instance.amount;
        else {
            final int diff = target.amount - instance.amount;
            if (diff > 0) {
                target.amount = diff;
                return true;
            } else if (diff == 0) {
                map.remove(instance.definition);
                return true;
            }
        }
        return false;
    }

    public boolean removeAmount(boolean testOnly, cElementalDefinitionStack stack) {
        final cElementalInstanceStack target = map.get(stack.definition);
        if (target == null)
            return false;
        if (testOnly)
            return target.amount >= stack.amount;
        else {
            final int diff = target.amount - stack.amount;
            if (diff > 0) {
                target.amount = diff;
                return true;
            } else if (diff == 0) {
                map.remove(stack.definition);
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public boolean removeAmount(boolean testOnly, iElementalDefinition def) {
        return removeAmount(testOnly, new cElementalDefinitionStack(def, 1));
    }

    public boolean removeAllAmounts(boolean testOnly, cElementalInstanceStack... instances) {
        boolean test = true;
        for (cElementalInstanceStack stack : instances)
            test &= removeAmount(true, stack);
        if (testOnly || !test) return test;
        for (cElementalInstanceStack stack : instances)
            removeAmount(false, stack);
        return true;
    }

    public boolean removeAllAmounts(boolean testOnly, cElementalDefinitionStack... stacks) {
        boolean test = true;
        for (cElementalDefinitionStack stack : stacks)
            test &= removeAmount(true, stack);
        if (testOnly || !test) return test;
        for (cElementalDefinitionStack stack : stacks)
            removeAmount(false, stack);
        return true;
    }

    @Deprecated
    public boolean removeAllAmounts(boolean testOnly, iElementalDefinition... definitions) {
        final cElementalDefinitionStack[] stacks = new cElementalDefinitionStack[definitions.length];
        for (int i = 0; i < stacks.length; i++)
            stacks[i] = new cElementalDefinitionStack(definitions[i], 1);
        return removeAllAmounts(testOnly, stacks);
    }

    public boolean removeAllAmounts(boolean testOnly, cElementalStackMap container) {
        return removeAllAmounts(testOnly, container.values());
    }

    public boolean removeAllAmounts(boolean testOnly, cElementalInstanceStackMap container) {
        return removeAllAmounts(testOnly, container.values());
    }

    //Remove overflow
    public float removeOverflow(int stacksCount, int stackCapacity) {
        float massRemoved = 0;

        if (map.size() > stacksCount) {
            iElementalDefinition[] keys = this.keys();
            for (int i = stacksCount; i < keys.length; i++) {
                massRemoved += map.get(keys[i]).getDefinitionStack().getMass();
                map.remove(keys[i]);
            }
        }

        for (cElementalInstanceStack instance : this.values())
            if (instance.amount > stackCapacity) {
                massRemoved += instance.definition.getMass() * (instance.amount - stackCapacity);
                instance.amount = stackCapacity;
            }
        return massRemoved;
    }

    //Put replace
    public cElementalInstanceStack putReplace(cElementalInstanceStack instanceUnsafe) {
        return map.put(instanceUnsafe.definition, instanceUnsafe);
    }

    public void putReplaceAll(cElementalInstanceStack... instances) {
        for (cElementalInstanceStack instance : instances)
            this.map.put(instance.definition, instance);
    }

    private void putReplaceAll(Map<iElementalDefinition, cElementalInstanceStack> inTreeUnsafe) {
        this.map.putAll(inTreeUnsafe);
    }

    public void putReplaceAll(cElementalInstanceStackMap inContainerUnsafe) {
        putReplaceAll(inContainerUnsafe.map);
    }

    //Put unify
    public cElementalInstanceStack putUnify(cElementalInstanceStack instance) {
        cElementalInstanceStack stack=map.get(instance.definition);
        if(stack==null) return map.put(instance.definition,instance);
        return map.put(instance.definition, stack.unifyIntoThis(instance));
    }

    public void putUnifyAll(cElementalInstanceStack... instances) {
        for (cElementalInstanceStack instance : instances)
            putUnify(instance);
    }

    private void putUnifyAll(Map<iElementalDefinition, cElementalInstanceStack> inTreeUnsafe) {
        for (cElementalInstanceStack in : inTreeUnsafe.values())
            putUnify(in);
    }

    public void putUnifyAll(cElementalInstanceStackMap containerUnsafe) {
        putUnifyAll(containerUnsafe.map);
    }

    //Getters
    public cElementalInstanceStack getInstance(iElementalDefinition def) {
        return map.get(def);
    }

    public String[] getElementalInfo() {
        final String[] info = new String[map.size() * 3];
        int i = 0;
        for (cElementalInstanceStack instance : map.values()) {
            info[i] = EnumChatFormatting.BLUE + instance.definition.getName();
            info[i + 1] = EnumChatFormatting.AQUA + instance.definition.getSymbol();
            info[i + 2] = "Amount " + EnumChatFormatting.GREEN + instance.amount;
            i += 3;
        }
        return info;
    }

    public cElementalInstanceStack[] values() {
        return map.values().toArray(new cElementalInstanceStack[0]);
    }

    public iElementalDefinition[] keys() {
        return map.keySet().toArray(new iElementalDefinition[0]);
    }

    public float getMass() {
        float mass = 0;
        for (cElementalInstanceStack stack : map.values()) {
            mass += stack.getMass();
        }
        return mass;
    }

    //Tests
    public boolean containsDefinition(iElementalDefinition def) {
        return map.containsKey(def);
    }

    public boolean containsInstance(cElementalInstanceStack inst) {
        return map.containsValue(inst);
    }

    public int size() {
        return map.size();
    }

    public boolean hasStacks() {
        return map.size() > 0;
    }

    //Tick Content
    public void tickContent(float lifeTimeMult, int postEnergize) {
        for (cElementalInstanceStack instance : this.values()) {
            cElementalInstanceStackMap newThings = instance.decay(lifeTimeMult, instance.age += 20, postEnergize);
            if (newThings == null) {
                instance.nextColor();
            } else {
                map.remove(instance.definition);
                for (cElementalInstanceStack newInstance : newThings.values())
                    putUnify(newInstance);
            }
        }

    }

    public void tickContent(int postEnergize) {
        for (cElementalInstanceStack instance : this.values()) {
            cElementalInstanceStackMap newThings = instance.decay(instance.age += 20, postEnergize);
            if (newThings == null) {
                instance.nextColor();
            } else {
                map.remove(instance.definition);
                for (cElementalInstanceStack newInstance : newThings.values())
                    putUnify(newInstance);
            }
        }

    }

    //NBT
    public NBTTagCompound getInfoNBT() {
        final NBTTagCompound nbt = new NBTTagCompound();
        final String[] info = getElementalInfo();
        nbt.setInteger("i", info.length);
        for (int i = 0; i < info.length; i++)
            nbt.setString(Integer.toString(i), info[i]);
        return nbt;
    }

    public static String[] infoFromNBT(NBTTagCompound nbt) {
        final String[] strings = new String[nbt.getInteger("i")];
        for (int i = 0; i < strings.length; i++)
            strings[i] = nbt.getString(Integer.toString(i));
        return strings;
    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("i", map.size());
        int i = 0;
        for (cElementalInstanceStack instance : map.values())
            nbt.setTag(Integer.toString(i++), instance.toNBT());
        return nbt;
    }

    public static cElementalInstanceStackMap fromNBT(NBTTagCompound nbt) throws tElementalException {
        final cElementalInstanceStack[] instances = new cElementalInstanceStack[nbt.getInteger("i")];
        for (int i = 0; i < instances.length; i++) {
            instances[i] = cElementalInstanceStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
            if (instances[i].definition.equals(nbtE__))
                throw new tElementalException("Something went Wrong");
        }
        return new cElementalInstanceStackMap(false, instances);
    }

    //stackUp
    public static cElementalInstanceStack[] stackUp(cElementalInstanceStack... in) {
        final cElementalInstanceStackMap inTree = new cElementalInstanceStackMap();
        inTree.putUnifyAll(in);
        return inTree.values();
    }

    @Override
    public int compareTo(cElementalInstanceStackMap o) {
        final int sizeDiff = map.size() - o.map.size();
        if (sizeDiff != 0) return sizeDiff;
        cElementalInstanceStack[] ofThis = values(), ofThat = o.values();
        for (int i = 0; i < ofThat.length; i++) {
            int result = ofThis[i].compareTo(ofThat[i]);
            if (result != 0) return result;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof cElementalInstanceStackMap)
            return compareTo((cElementalInstanceStackMap) obj) == 0;
        if (obj instanceof cElementalStackMap)
            return toDefinitionMapForComparison().compareTo((cElementalStackMap) obj) == 0;
        return false;
    }

    @Override
    public int hashCode() {//Internal amounts should be also hashed
        int hash = -(map.size() << 4);
        for (cElementalInstanceStack s : map.values()) {
            hash += s.definition.hashCode();
        }
        return hash;
    }
}
