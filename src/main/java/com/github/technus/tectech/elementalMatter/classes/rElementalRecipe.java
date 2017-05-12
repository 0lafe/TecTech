package com.github.technus.tectech.elementalMatter.classes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Tec on 02.03.2017.
 */
public class rElementalRecipe implements Comparable<rElementalRecipe> {
    public final cElementalMutableDefinitionStackMap inEM;
    public final cElementalMutableDefinitionStackMap outEM;
    public final ItemStack[] outItems;
    public final FluidStack[] outFluids;
    public Object[] extension = null;
    public final short comparableID;

    public rElementalRecipe(
            cElementalMutableDefinitionStackMap inEMnotNull,
            short comparableID,
            cElementalMutableDefinitionStackMap outEM,
            ItemStack[] outItems,
            FluidStack[] outFluids) {
        this.inEM = inEMnotNull;
        this.comparableID=comparableID;//allows multiple recipes with the same input EM
        this.outEM = outEM;
        this.outItems = outItems;
        this.outFluids = outFluids;
    }

    public rElementalRecipe extend(Object... data) {
        extension = data;
        return this;
    }

    @Override
    public int compareTo(rElementalRecipe o) {
        final int compare=inEM.compareTo(o.inEM);
        if(compare!=0)return compare;
        return (int)comparableID-o.comparableID;
    }

    @Override
    public int hashCode() {
        return comparableID;
    }
}
