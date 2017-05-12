package com.github.technus.tectech.elementalMatter.classes;

import com.github.technus.tectech.auxiliary.TecTechConfig;
import com.github.technus.tectech.elementalMatter.interfaces.iElementalDefinition;
import com.github.technus.tectech.elementalMatter.interfaces.iHasElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.github.technus.tectech.elementalMatter.definitions.cPrimitiveDefinition.nbtE__;

/**
 * Created by danie_000 on 23.01.2017.
 */
public abstract class cElementalDefinition implements iElementalDefinition {
    //Nothing array
    public static final iElementalDefinition[] nothing = new cElementalPrimitive[0];

    //add text based creators for recipe formula input?
    private static final Map<Byte, Method> nbtCreationBind = new HashMap<>();//creator methods in subclasses
    protected static Method addCreatorFromNBT(byte b, Method m) {
        return nbtCreationBind.put(b, m);
    }

    @Override
    protected final Object clone() {
        return this;//IMMUTABLE
    }

    public static iElementalDefinition fromNBT(NBTTagCompound nbt) {
        try {
            return (iElementalDefinition) nbtCreationBind.get(nbt.getByte("t")).invoke(null, nbt);
        } catch (Exception e) {
            if (TecTechConfig.DEBUG_MODE) e.printStackTrace();
            return nbtE__;
        }
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof iElementalDefinition)
            return compareTo((iElementalDefinition) obj) == 0;
        if (obj instanceof iHasElementalDefinition)
            return compareTo(((iHasElementalDefinition) obj).getDefinition()) == 0;
        return false;
    }

    @Override
    public int compareTo(iElementalDefinition o) {
        if (getClassType() == o.getClassType()) {
            //only of the internal def stacks!!!
            //that allows neat check if the same thing and
            //top hierarchy amount can be used to store amount info
            return compareDefinitionStacksWithAmount(getSubParticles().values(), o.getSubParticles().values());
        }
        return compareClasses(o);
    }

    public final int compareClasses(iElementalDefinition obj) {
        return ((int) getClassType()) - obj.getClassType();
    }

    //use only for nested operations!
    private static int compareDefinitionStacksWithAmount(cElementalDefinitionStack[] tc, cElementalDefinitionStack[] sc) {
        if (tc == null) {
            if (sc == null) return 0;
            else return -1;
        }
        if (sc == null) return 1;

        if (tc.length > sc.length) return 1;
        if (tc.length < sc.length) return -1;

        for (int i = 0; i < tc.length; i++) {
            int cn = tc[i].definition.compareTo(sc[i].definition);
            if (cn != 0) return cn;

            if (tc[i].amount > sc[i].amount) return 1;
            if (tc[i].amount < sc[i].amount) return -1;
        }
        return 0;
    }

    @Override
    public int hashCode() {
        int hash=Integer.MIN_VALUE+getSubParticles().size();
        int i=9;
        for(cElementalDefinitionStack s:getSubParticles().values()){
            hash+=s.hashCode()*(i++);
        }
        return hash;
    }
}
