package com.github.technus.tectech.elementalMatter.classes;

import com.github.technus.tectech.elementalMatter.interfaces.iElementalDefinition;
import com.github.technus.tectech.elementalMatter.interfaces.iHasElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;
import java.util.TreeMap;

import static com.github.technus.tectech.elementalMatter.definitions.cPrimitiveDefinition.nbtE__;

/**
 * Created by danie_000 on 22.01.2017.
 */
public final class cElementalMutableDefinitionStackMap extends cElementalStackMap {//Transient class for construction of definitions/recipes

    //Constructors + Clone, all make a whole new OBJ.
    public cElementalMutableDefinitionStackMap() {
        map = new TreeMap<>();
    }

    @Deprecated
    public cElementalMutableDefinitionStackMap(iElementalDefinition... in) {
        map=stackUpMap(in).map;
    }

    @Deprecated
    public cElementalMutableDefinitionStackMap(cElementalDefinitionStack... in) {
        map=stackUpMap(in).map;
    }

    public cElementalMutableDefinitionStackMap(TreeMap<iElementalDefinition, cElementalDefinitionStack> in) {
        this(true, in);
    }

    public cElementalMutableDefinitionStackMap(boolean clone, TreeMap<iElementalDefinition, cElementalDefinitionStack> in) {
        if (clone) {
            map = new TreeMap<>(in);
        } else {
            map = in;
        }
    }

    @Override
    public cElementalMutableDefinitionStackMap clone() {
        return new cElementalMutableDefinitionStackMap(map);
    }

    public cElementalDefinitionStackMap toImmutable() {
        return new cElementalDefinitionStackMap(map);
    }

    @Override
    @Deprecated
    public TreeMap<iElementalDefinition, cElementalDefinitionStack> getRawMap() {
        return map;
    }


    //Removers
    public void clear() {
        map.clear();
    }

    public cElementalDefinitionStack remove(iElementalDefinition def) {
        return map.remove(def);
    }

    @Deprecated
    public cElementalDefinitionStack remove(iHasElementalDefinition has) {
        return map.remove(has.getDefinition());
    }

    public void removeAll(iElementalDefinition... definitions) {
        for (iElementalDefinition def : definitions)
            map.remove(def);
    }

    @Deprecated
    public void removeAll(iHasElementalDefinition... hasElementals) {
        for (iHasElementalDefinition has : hasElementals)
            map.remove(has.getDefinition());
    }

    //Remove amounts
    public boolean removeAmount(boolean testOnly, cElementalInstanceStack instance) {
        final cElementalDefinitionStack target = map.get(instance.definition);
        if (target == null)
            return false;
        if (testOnly)
            return target.amount >= instance.amount;
        else {
            final int diff = target.amount - instance.amount;
            if (diff > 0) {
                map.put(target.definition, new cElementalDefinitionStack(target.definition, diff));
                return true;
            } else if (diff == 0) {
                map.remove(instance.definition);
                return true;
            }
        }
        return false;
    }

    public boolean removeAmount(boolean testOnly, cElementalDefinitionStack stack) {
        final cElementalDefinitionStack target = map.get(stack.definition);
        if (target == null)
            return false;
        if (testOnly)
            return target.amount >= stack.amount;
        else {
            final int diff = target.amount - stack.amount;
            if (diff > 0) {
                map.put(target.definition, new cElementalDefinitionStack(target.definition, diff));
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

    //Put replace
    public cElementalDefinitionStack putReplace(cElementalDefinitionStack defStackUnsafe) {
        return map.put(defStackUnsafe.definition, defStackUnsafe);
    }

    public void putReplaceAll(cElementalDefinitionStack... defStacks) {
        for (cElementalDefinitionStack defStack : defStacks)
            this.map.put(defStack.definition, defStack);
    }

    public void putReplaceAll(cElementalStackMap inContainerUnsafe) {
        this.map.putAll(inContainerUnsafe.map);
    }

    //Put unify
    public cElementalDefinitionStack putUnify(cElementalDefinitionStack def) {
        final cElementalDefinitionStack stack=map.get(def.definition);
        if(stack==null) return map.put(def.definition,def);
        return map.put(def.definition, stack.addAmountIntoNewInstance(def.amount));
    }

    @Deprecated
    public cElementalDefinitionStack putUnify(iElementalDefinition def) {
        return putUnify(new cElementalDefinitionStack(def, 1));
    }

    public void putUnifyAll(cElementalDefinitionStack... defs) {
        for (cElementalDefinitionStack def : defs)
            putUnify(def);
    }

    @Deprecated
    public void putUnifyAll(iElementalDefinition... defs) {
        for (iElementalDefinition def : defs)
            putUnify(def);
    }

    private void putUnifyAll(Map<iElementalDefinition, cElementalDefinitionStack> inTreeUnsafe) {
        for (cElementalDefinitionStack in : inTreeUnsafe.values())
            putUnify(in);
    }

    public void putUnifyAll(cElementalStackMap containerUnsafe) {
        for (cElementalDefinitionStack in : containerUnsafe.map.values())
            putUnify(in);
    }

    public static cElementalMutableDefinitionStackMap fromNBT(NBTTagCompound nbt) throws tElementalException {
        final cElementalDefinitionStack[] defStacks = new cElementalDefinitionStack[nbt.getInteger("i")];
        for (int i = 0; i < defStacks.length; i++) {
            defStacks[i] = cElementalDefinitionStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
            if (defStacks[i].definition.equals(nbtE__))
                throw new tElementalException("Something went Wrong");
        }
        return new cElementalMutableDefinitionStackMap(defStacks);
    }

    //stackUp
    @Deprecated
    public static cElementalMutableDefinitionStackMap stackUpMap(iElementalDefinition... in) {
        final cElementalMutableDefinitionStackMap inTree = new cElementalMutableDefinitionStackMap();
        for (iElementalDefinition def : in) {
            inTree.putUnify(new cElementalDefinitionStack(def, 1));
        }
        return inTree;
    }

    public static cElementalMutableDefinitionStackMap stackUpMap(cElementalDefinitionStack... in) {
        final cElementalMutableDefinitionStackMap inTree = new cElementalMutableDefinitionStackMap();
        inTree.putUnifyAll(in);
        return inTree;
    }
}
