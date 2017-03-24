package com.github.technus.tectech.thing.casing;

import com.github.technus.tectech.thing.CustomItemList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import gregtech.common.blocks.GT_Material_Casings;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import java.util.List;

/**
 * Created by danie_000 on 03.10.2016.
 */
public class GT_Block_CasingsTT
        extends GT_Block_Casings_Abstract {

    private static IIcon eM0s,eM1s,eM2s,eM0,eM1,eM2,eM3, eM4, eM5, eM6s, eM6, eM7, eM8, eM9;
    private static IIcon debug[] = new IIcon[6];

    public GT_Block_CasingsTT() {

        super(GT_Item_CasingsTT.class, "gt.blockcasingsTT", GT_Material_Casings.INSTANCE);
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            Textures.BlockIcons.CASING_BLOCKS[(i + 96)] = new GT_CopiedBlockTexture(this, 6, i);
            /*IMPORTANT for block recoloring*/
        }
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Computer Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Advanced Computer Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Computer Heat Vent");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Molecular Containment Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Containment Field Generator");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Containment Field Generator Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Molecular Containment Coil");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Teleportation Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Spacetime Altering Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Collider Hollow Casing");//adding

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Debug Sides");//adding


        CustomItemList.eM_computer.set(new ItemStack(this, 1, 0));//adding
        CustomItemList.eM_computerAdv.set(new ItemStack(this, 1, 1));//adding
        CustomItemList.eM_computerVent.set(new ItemStack(this, 1, 2));//adding
        CustomItemList.eM_Casing.set(new ItemStack(this, 1, 3));//adding
        CustomItemList.eM_Field.set(new ItemStack(this, 1, 4));//adding
        CustomItemList.eM_Field_Casing.set(new ItemStack(this, 1, 5));//adding
        CustomItemList.eM_Coil.set(new ItemStack(this, 1, 6));//adding
        CustomItemList.eM_Tele.set(new ItemStack(this, 1, 7));//adding
        CustomItemList.eM_TimeSpaceWarp.set(new ItemStack(this, 1, 8));
        CustomItemList.eM_Hollow.set(new ItemStack(this, 1, 9));

        CustomItemList.debugBlock.set(new ItemStack(this, 1, 10));
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        //super.registerBlockIcons(aIconRegister);
        eM0 = aIconRegister.registerIcon("gregtech:iconsets/EM_PC_NONSIDE");
        eM0s = aIconRegister.registerIcon("gregtech:iconsets/EM_PC");
        eM1 = aIconRegister.registerIcon("gregtech:iconsets/EM_PC_ADV_NONSIDE");
        eM1s = aIconRegister.registerIcon("gregtech:iconsets/EM_PC_ADV");
        eM2 = aIconRegister.registerIcon("gregtech:iconsets/EM_PC_VENT_NONSIDE");
        eM2s = aIconRegister.registerIcon("gregtech:iconsets/EM_PC_VENT");
        eM3 = aIconRegister.registerIcon("gregtech:iconsets/EM_CASING");
        eM4 = aIconRegister.registerIcon("gregtech:iconsets/EM_FIELD");
        eM5 = aIconRegister.registerIcon("gregtech:iconsets/EM_FIELD_CASING");
        eM6 = aIconRegister.registerIcon("gregtech:iconsets/EM_COIL_NONSIDE");
        eM6s = aIconRegister.registerIcon("gregtech:iconsets/EM_COIL");
        eM7 = aIconRegister.registerIcon("gregtech:iconsets/EM_TELE");
        eM8 = aIconRegister.registerIcon("gregtech:iconsets/EM_TIMESPACE");
        eM9 = aIconRegister.registerIcon("gregtech:iconsets/EM_HOLLOW");

        debug[0] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_0");
        debug[1] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_1");
        debug[2] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_2");
        debug[3] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_3");
        debug[4] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_4");
        debug[5] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_5");
    }

    public IIcon getIcon(int aSide, int aMeta) {
        switch (aMeta) {
            case 0:
                if (aSide < 2) return eM0;
                return eM0s;
            case 1:
                if (aSide < 2) return eM1;
                return eM1s;
            case 2:
                if (aSide < 2) return eM2;
                return eM2s;
            case 3:
                return eM3;
            case 4:
                return eM4;
            case 5:
                return eM5;
            case 6:
                if (aSide < 2) return eM6;
                return eM6s;
            case 7:
                return eM7;
            case 8:
                return eM8;
            case 9:
                return eM9;
            case 10:
                return debug[aSide];
            default:
                return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess aWorld, int xCoord, int yCoord, int zCoord, int aSide) {
        int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
        return getIcon(aSide, tMeta);
    }

    public int colorMultiplier(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return super.colorMultiplier(aWorld, aX, aY, aZ);
    }

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        for (int i = 0; i <= 10; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }
}
