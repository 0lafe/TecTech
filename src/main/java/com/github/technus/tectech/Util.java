package com.github.technus.tectech;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Tec on 21.03.2017.
 */
public class Util {
    public static String intToString(int number, int groupSize) {
        StringBuilder result = new StringBuilder();

        for(int i = 31; i >= 0 ; i--) {
            int mask = 1 << i;
            result.append((number & mask) != 0 ? "1" : "0");

            if (i % groupSize == 0)
                result.append(" ");
        }
        result.replace(result.length() - 1, result.length(), "");

        return result.toString();
    }

    //Check Machine Structure based on string[][] (effectively char[][][]), ond offset of the controller
    //This only checks for REGULAR BLOCKS!
    public static boolean StuctureCheck(String[][] structure,//0-9 casing, +- air no air, a... ignore 'a'-CHAR-1 blocks
                                        Block[] blockType,//use numbers 0-9 for casing types
                                        byte[] blockMeta,//use numbers 0-9 for casing types
                                        int horizontalOffset, int verticalOffset, int depthOffset,
                                        IGregTechTileEntity aBaseMetaTileEntity,
                                        boolean forceCheck) {
        //TE Rotation
        byte facing = aBaseMetaTileEntity.getFrontFacing();
        World world=aBaseMetaTileEntity.getWorld();

        int x, y, z, a, b, c, yPos;
        //a,b,c - relative to block face!
        //x,y,z - relative to block position on map!
        //yPos  - absolute height of checked block

        //perform your duties
        c = -depthOffset;
        for (String[] _structure : structure) {//front to back
            b = verticalOffset;
            for (String __structure : _structure) {//top to bottom
                a = -horizontalOffset;
                for (char block : __structure.toCharArray()) {//left to right
                    if (block > '`') {//characters allow to skip check a-1 skip, b-2 skips etc.
                        a += block - '`';
                    } else {
                        //get x y z from rotation
                        switch (facing) {//translation
                            case 4: x = +c; z = +a; y = +b; break;
                            case 3: x = +a; z = -c; y = +b; break;
                            case 5: x = -c; z = -a; y = +b; break;
                            case 2: x = -a; z = +c; y = +b; break;
                            //Things get odd if the block faces up or down...
                            case 1: x = +a; y = -c; z = +b; break;//similar to 3
                            case 0: x = -a; y = +c; z = -b; break;//similar to 2
                            default: return false;
                        }
                        //that must be here since in some cases other axis (b,c) controls y
                        yPos=aBaseMetaTileEntity.getYCoord()+y;
                        if(yPos<0 || yPos>=256) return false;

                        //Check block
                        if (world.blockExists(x,y,z)) {//this actually checks if the chunk is loaded at this pos
                            switch (block) {
                                case '-'://must be air
                                    if (getBlockOffset(aBaseMetaTileEntity, x, y, z, world).getMaterial() != Material.air)
                                        return false;
                                    break;
                                case '+'://must not be air
                                    if (getBlockOffset(aBaseMetaTileEntity, x, y, z, world).getMaterial() == Material.air)
                                        return false;
                                    break;
                                default: {//check for block (countable)
                                    int pointer = block - '0';
                                    //countable air -> net.minecraft.block.BlockAir
                                    if (getBlockOffset(aBaseMetaTileEntity,x,y,z,world) != blockType[pointer]) {
                                        if (TecTech.ModConfig.DEBUG_MODE)
                                            TecTech.Logger.info("Struct-block-error " + x + " " + y + " " + z + "/" + a + " " + c + "/" + getBlockOffset(aBaseMetaTileEntity,x,y,z,world) + " " + blockType[pointer]);
                                        return false;
                                    }
                                    if (getMetaIDOffset(aBaseMetaTileEntity,x,y,z,world) != blockMeta[pointer]) {
                                        if (TecTech.ModConfig.DEBUG_MODE)
                                            TecTech.Logger.info("Struct-meta-id-error " + x + " " + y + " " + z + "/" + a + " " + c + "/" + getMetaIDOffset(aBaseMetaTileEntity,x,y,z,world) + " " + blockMeta[pointer]);
                                        return false;
                                    }
                                }
                            }
                        }else if (forceCheck) return false;
                        a++;//block in horizontal layer
                    }
                }
                b--;//horizontal layer
            }
            c++;//depth
        }
        return true;
    }

    public static String[] ReverseStructureCheck(IGregTechTileEntity aBaseMetaTileEntity,
                                         int horizontalOffset, int verticalOffset, int depthOffset,
                                         int horizontalSize, int verticalSize, int depthSize){
        //TE Rotation
        byte facing = aBaseMetaTileEntity.getFrontFacing();
        World world=aBaseMetaTileEntity.getWorld();

        ItemStack[] array=new ItemStack[10];

        int x, y, z, a, b, c,yPos;
        //a,b,c - relative to block face!
        //x,y,z - relative to block position on map!
        //yPos  - absolute height of checked block

        //perform your duties - #1 - count block types
        c = -depthOffset;
        for (int cz=0;cz<depthSize;cz++) {//front to back
            b = verticalOffset;
            for (int by=0;by<verticalSize;by++) {//top to bottom
                a = -horizontalOffset;
                for (int az=0;az<horizontalSize;az++) {//left to right
                    //get x y z from rotation
                    switch (facing) {//translation
                        case 4: x = +c; z = +a; y = +b; break;
                        case 3: x = +a; z = -c; y = +b; break;
                        case 5: x = -c; z = -a; y = +b; break;
                        case 2: x = -a; z = +c; y = +b; break;
                        //Things get odd if the block faces up or down...
                        case 1: x = +a; y = -c; z = +b; break;//similar to 3
                        case 0: x = -a; y = +c; z = -b; break;//similar to 2
                        default: return new String[]{"Invalid facing"};
                    }
                    //that must be here since in some cases other axis (a,b,c) controls y
                    yPos=aBaseMetaTileEntity.getYCoord()+y;
                    if(yPos<0 || yPos>=256) return new String[]{"Invalid position"};
                    //Check block
                    Block block=getBlockOffset(aBaseMetaTileEntity,x,y,z,world);
                    int meta=getMetaIDOffset(aBaseMetaTileEntity,x,y,z,world);

                    if(!block.hasTileEntity(meta) && block.getMaterial()!=Material.air) {
                        boolean err=true;
                        final ItemStack is=new ItemStack(block, 1, meta);
                        for(int i=0;i<array.length;i++){
                            if(array[i]==null){
                                array[i]=is;
                                err=false;
                                break;
                            } else if(is.getItem()==array[i].getItem() && is.getItemDamage()==array[i].getItemDamage()){
                                err=false;
                                break;
                            }
                        }
                        if (err) return new String[]{"Too much different blocks"};
                    }

                    a++;//block in horizontal layer
                }
                b--;//horizontal layer
            }
            c++;//depth
        }

        List<String> output=new ArrayList<>();

        output.add("Block[] MetaID[]");
        output.add("");
        for(ItemStack is:array){
            if(is!=null) output.add(is.getUnlocalizedName()+" "+is.getItemDamage());
        }
        output.add("");
        output.add("String[][]");
        //perform your duties - #2 - write strings
        c = -depthOffset;
        for (int cz=0;cz<depthSize;cz++) {//front to back
            b = verticalOffset;
            output.add("");
            for (int by=0;by<verticalSize;by++) {//top to bottom
                a = -horizontalOffset;
                String line="";
                for (int az=0;az<horizontalSize;az++) {//left to right
                    //get x y z from rotation
                    switch (facing) {//translation
                        case 4: x = +c; z = +a; y = +b; break;
                        case 3: x = +a; z = -c; y = +b; break;
                        case 5: x = -c; z = -a; y = +b; break;
                        case 2: x = -a; z = +c; y = +b; break;
                        //Things get odd if the block faces up or down...
                        case 1: x = +a; y = -c; z = +b; break;//similar to 3
                        case 0: x = -a; y = +c; z = -b; break;//similar to 2
                        default: return new String[]{"Invalid facing"};
                    }
                    //that must be here since in some cases other axis (a,b,c) controls y
                    //yPos=aBaseMetaTileEntity.getYCoord()+y;
                    //if(yPos<0 || yPos>=256) return new String[]{"Invalid position"};
                    //Check block

                    Block block=getBlockOffset(aBaseMetaTileEntity,x,y,z,world);
                    int meta=getMetaIDOffset(aBaseMetaTileEntity,x,y,z,world);

                    if(a==0 && b==0 && c==0){
                        line+='X';
                    }else if(block.getMaterial()==Material.air){
                        line+='-';
                    }else if(block.hasTileEntity(meta)){
                        line+='+';
                    }else{
                        ItemStack stack=new ItemStack(block,1,meta);
                        String str="?";
                        for(int i=0;i<array.length;i++){
                            if(array[i]!=null && stack.getItem()==array[i].getItem() && stack.getItemDamage()==array[i].getItemDamage()) {
                                str = Integer.toString(i);
                                break;
                            }
                        }
                        line+=str;
                    }

                    a++;//block in horizontal layer
                }
                output.add(line);
                b--;//horizontal layer
            }
            c++;//depth
        }
        return output.toArray(new String[0]);
    }

    private static Block getBlockOffset(IGregTechTileEntity a,int x,int y,int z,World w){
        return w.getBlock(a.getXCoord()+x,a.getYCoord()+y,a.getZCoord()+z);
    }

    private static int getMetaIDOffset(IGregTechTileEntity a,int x,int y,int z,World w){
        return w.getBlockMetadata(a.getXCoord()+x,a.getYCoord()+y,a.getZCoord()+z);
    }

    public static boolean isInputEqual(boolean aDecreaseStacksizeBySuccess, boolean aDontCheckStackSizes, FluidStack[] requiredFluidInputs, ItemStack[] requiredInputs, FluidStack[] givenFluidInputs, ItemStack... givenInputs) {
        if (!GregTech_API.sPostloadFinished) return false;
        if (requiredFluidInputs.length > 0 && givenFluidInputs == null) return false;
        int amt;
        for (FluidStack tFluid : requiredFluidInputs)
            if (tFluid != null) {
                boolean temp = true;
                amt = tFluid.amount;
                for (FluidStack aFluid : givenFluidInputs)
                    if (aFluid != null && aFluid.isFluidEqual(tFluid)) {
                        if (aDontCheckStackSizes) {
                            temp = false;
                            break;
                        }
                        amt -= aFluid.amount;
                        if (amt < 1) {
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
                                if (aDontCheckStackSizes) {
                                    aStack.stackSize -= amt;
                                    break;
                                }
                                if (aStack.stackSize < amt) {
                                    amt -= aStack.stackSize;
                                    aStack.stackSize = 0;
                                } else {
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
}
