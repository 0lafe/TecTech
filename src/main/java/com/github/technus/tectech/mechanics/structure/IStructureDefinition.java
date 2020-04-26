package com.github.technus.tectech.mechanics.structure;

import com.github.technus.tectech.mechanics.alignment.enumerable.ExtendedFacing;
import net.minecraft.world.World;

public interface IStructureDefinition<T> {
    /**
     * Used internally
     * @param name same name as for other methods here
     * @return the array of elements to process
     */
    IStructureElement<T>[] getElementsFor(String name);

    default boolean check(T object,String piece, World world, ExtendedFacing extendedFacing,
                          int basePositionX, int basePositionY, int basePositionZ,
                          int basePositionA, int basePositionB, int basePositionC,
                          boolean forceCheckAllBlocks){
        return iterate(object,getElementsFor(piece), world, extendedFacing, basePositionX, basePositionY, basePositionZ,
                basePositionA, basePositionB, basePositionC,false,forceCheckAllBlocks);
    }

    default boolean hints(T object,String piece, World world, ExtendedFacing extendedFacing,
                          int basePositionX, int basePositionY, int basePositionZ,
                          int basePositionA, int basePositionB, int basePositionC) {
        return iterate(object,getElementsFor(piece), world, extendedFacing, basePositionX, basePositionY, basePositionZ,
                basePositionA, basePositionB, basePositionC,true,null);
    }

    default boolean build(T object,String piece, World world, ExtendedFacing extendedFacing,
                          int basePositionX, int basePositionY, int basePositionZ,
                          int basePositionA, int basePositionB, int basePositionC) {
        return iterate(object,getElementsFor(piece), world, extendedFacing, basePositionX, basePositionY, basePositionZ,
                basePositionA, basePositionB, basePositionC,false,null);
    }

    default boolean buildOrHints(T object,String piece, World world, ExtendedFacing extendedFacing,
                                 int basePositionX, int basePositionY, int basePositionZ,
                                 int basePositionA, int basePositionB, int basePositionC,
                                 boolean hintsOnly){
        return iterate(object,getElementsFor(piece), world, extendedFacing, basePositionX, basePositionY, basePositionZ,
                basePositionA, basePositionB, basePositionC,hintsOnly,null);
    }

    static <T> boolean iterate(T object, IStructureElement<T>[] elements, World world, ExtendedFacing extendedFacing,
                               int basePositionX, int basePositionY, int basePositionZ,
                               int basePositionA, int basePositionB, int basePositionC,
                               boolean hintsOnly, Boolean checkBlocksIfNotNullForceCheckAllIfTrue){
        if(world.isRemote ^ hintsOnly){
            return false;
        }

        //change base position to base offset
        basePositionA=-basePositionA;
        basePositionB=-basePositionB;
        basePositionC=-basePositionC;

        int[] abc = new int[]{basePositionA,basePositionB,basePositionC};
        int[] xyz = new int[3];

        if(checkBlocksIfNotNullForceCheckAllIfTrue!=null){
            if(checkBlocksIfNotNullForceCheckAllIfTrue){
                for (IStructureElement<T> element : elements) {
                    extendedFacing.getWorldOffset(abc, xyz);
                    xyz[0] += basePositionX;
                    xyz[1] += basePositionY;
                    xyz[2] += basePositionZ;

                    if(element instanceof IStructureNavigate) {
                        IStructureNavigate<T> navigate=(IStructureNavigate<T>)element;
                        abc[0] = (navigate.resetA() ? basePositionA : abc[0]) + navigate.getStepA();
                        abc[1] = (navigate.resetB() ? basePositionA : abc[1]) + navigate.getStepB();
                        abc[2] = (navigate.resetC() ? basePositionA : abc[2]) + navigate.getStepC();
                    }else {
                        if (world.blockExists(xyz[0], xyz[1], xyz[2])) {
                            if(!element.check(object, world, xyz[0], xyz[1], xyz[2])){
                                return false;
                            }
                        }else {
                            return false;
                        }
                        abc[0]+=1;
                    }
                }
            } else {
                for (IStructureElement<T> element : elements) {
                    extendedFacing.getWorldOffset(abc, xyz);
                    xyz[0] += basePositionX;
                    xyz[1] += basePositionY;
                    xyz[2] += basePositionZ;

                    if(element instanceof IStructureNavigate) {
                        IStructureNavigate<T> navigate=(IStructureNavigate<T>)element;
                        abc[0] = (navigate.resetA() ? basePositionA : abc[0]) + navigate.getStepA();
                        abc[1] = (navigate.resetB() ? basePositionA : abc[1]) + navigate.getStepB();
                        abc[2] = (navigate.resetC() ? basePositionA : abc[2]) + navigate.getStepC();
                    }else {
                        if (world.blockExists(xyz[0], xyz[1], xyz[2])) {
                            if(!element.check(object, world, xyz[0], xyz[1], xyz[2])){
                                return false;
                            }
                        }
                        abc[0]+=1;
                    }
                }
            }
        }else {
            if(hintsOnly) {
                for (IStructureElement<T> element : elements) {
                    extendedFacing.getWorldOffset(abc, xyz);
                    xyz[0] += basePositionX;
                    xyz[1] += basePositionY;
                    xyz[2] += basePositionZ;

                    if(element instanceof IStructureNavigate) {
                        IStructureNavigate<T> navigate=(IStructureNavigate<T>)element;
                        abc[0] = (navigate.resetA() ? basePositionA : abc[0]) + navigate.getStepA();
                        abc[1] = (navigate.resetB() ? basePositionA : abc[1]) + navigate.getStepB();
                        abc[2] = (navigate.resetC() ? basePositionA : abc[2]) + navigate.getStepC();
                    }else {
                        if (world.blockExists(xyz[0], xyz[1], xyz[2])) {
                            element.spawnHint(object, world, xyz[0], xyz[1], xyz[2]);
                        }
                        abc[0]+=1;
                    }
                }
            } else {
                for (IStructureElement<T> element : elements) {
                    extendedFacing.getWorldOffset(abc, xyz);
                    xyz[0] += basePositionX;
                    xyz[1] += basePositionY;
                    xyz[2] += basePositionZ;

                    if(element instanceof IStructureNavigate) {
                        IStructureNavigate<T> navigate=(IStructureNavigate<T>)element;
                        abc[0] = (navigate.resetA() ? basePositionA : abc[0]) + navigate.getStepA();
                        abc[1] = (navigate.resetB() ? basePositionA : abc[1]) + navigate.getStepB();
                        abc[2] = (navigate.resetC() ? basePositionA : abc[2]) + navigate.getStepC();
                    }else {
                        if (world.blockExists(xyz[0], xyz[1], xyz[2])) {
                            element.placeBlock(object, world, xyz[0], xyz[1], xyz[2]);
                        }
                        abc[0]+=1;
                    }
                }
            }
        }
        return true;
    }
}
