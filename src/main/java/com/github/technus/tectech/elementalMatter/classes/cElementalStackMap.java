package com.github.technus.tectech.elementalMatter.classes;

import com.github.technus.tectech.elementalMatter.interfaces.iElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.TreeMap;

/**
 * Created by Tec on 12.05.2017.
 */
abstract class cElementalStackMap implements Comparable<cElementalStackMap> {
    protected TreeMap<iElementalDefinition, cElementalDefinitionStack> map;

    @Override
    public abstract cElementalStackMap clone();

    @Deprecated
    public abstract TreeMap<iElementalDefinition, cElementalDefinitionStack> getRawMap();

    //Getters
    public final cElementalDefinitionStack getFirst(){
        return map.firstEntry().getValue();
    }

    public final cElementalDefinitionStack getLast(){
        return map.lastEntry().getValue();
    }
    public final cElementalDefinitionStack getDefinitionStack(iElementalDefinition def) {
        return map.get(def);
    }

    public final String[] getElementalInfo() {
        final String[] info = new String[map.size() * 3];
        int i = 0;
        for (cElementalDefinitionStack defStack : map.values()) {
            info[i] = EnumChatFormatting.BLUE + defStack.definition.getName();
            info[i + 1] = EnumChatFormatting.AQUA + defStack.definition.getSymbol();
            info[i + 2] = "Amount " + EnumChatFormatting.GREEN + defStack.amount;
            i += 3;
        }
        return info;
    }

    public final cElementalDefinitionStack[] values() {
        return map.values().toArray(new cElementalDefinitionStack[0]);
    }

    public final iElementalDefinition[] keys() {
        return map.keySet().toArray(new iElementalDefinition[0]);
    }

    //Tests
    public final boolean containsDefinition(iElementalDefinition def) {
        return map.containsKey(def);
    }

    public final boolean containsDefinitionStack(cElementalDefinitionStack inst) {
        return map.containsValue(inst);
    }

    public final int size() {
        return map.size();
    }

    public final boolean hasStacks() {
        return map.size() > 0;
    }

    //NBT
    public final NBTTagCompound getInfoNBT() {
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

    public final NBTTagCompound toNBT() {
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("i", map.size());
        int i = 0;
        for (cElementalDefinitionStack defStack : map.values())
            nbt.setTag(Integer.toString(i++), defStack.toNBT());
        return nbt;
    }

    @Override
    public final int compareTo(cElementalStackMap o) {//this actually compares rest
        final int sizeDiff = map.size() - o.map.size();
        if (sizeDiff != 0) return sizeDiff;
        cElementalDefinitionStack[] ofThis = values(), ofO = o.values();
        for (int i = 0; i < ofO.length; i++) {
            int result = ofThis[i].compareTo(ofO[i]);
            if (result != 0) return result;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof cElementalStackMap)
            return compareTo((cElementalStackMap) obj) == 0;
        if (obj instanceof cElementalInstanceStackMap)
            return compareTo(((cElementalInstanceStackMap) obj).toDefinitionMapForComparison()) == 0;
        return false;
    }

    @Override
    public int hashCode() {//Hash only definitions to compare contents not amounts or data
        int hash = -(map.size() << 4);
        for (cElementalDefinitionStack s : map.values()) {
            hash += s.definition.hashCode();
        }
        return hash;
    }
}
