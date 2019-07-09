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

import static com.github.technus.tectech.TecTech.tectechTexturePage1;

/**
 * Created by danie_000 on 03.10.2016.
 */
public class GT_Block_CasingsBA0 extends GT_Block_Casings_Abstract {
    public static final byte texturePage=tectechTexturePage1;
    public static final short textureOffset = texturePage << 7;//Start of PAGE 8 (which is the 9th page)  (8*128)
    private static IIcon tM0, tM1, tM2, tM3, tM4, tM5, tM6, tM7, tM8;
    private static IIcon[] debug = new IIcon[6];

    public GT_Block_CasingsBA0() {
        super(GT_Item_CasingsBA0.class, "gt.blockcasingsBA0", GT_Material_Casings.INSTANCE);
        for (byte b = 0; b < 16; b = (byte) (b + 1)) {
            Textures.BlockIcons.casingTexturePages[texturePage][b] = new GT_CopiedBlockTexture(this, 6, b);
            /*IMPORTANT for block recoloring**/
        }

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "T0 Primary Tesla Windings");//TODO Decide tesla coil winding materials to rename
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "T1 Primary Tesla Windings");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "T2 Primary Tesla Windings");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "T3 Primary Tesla Windings");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "T4 Primary Tesla Windings");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "T5 Primary Tesla Windings");

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Tesla Base Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Tesla Toroid Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Tesla Structural Frame");

        CustomItemList.tM_TeslaPrimary_0.set(new ItemStack(this, 1, 0));
        CustomItemList.tM_TeslaPrimary_1.set(new ItemStack(this, 1, 1));
        CustomItemList.tM_TeslaPrimary_2.set(new ItemStack(this, 1, 2));
        CustomItemList.tM_TeslaPrimary_3.set(new ItemStack(this, 1, 3));
        CustomItemList.tM_TeslaPrimary_4.set(new ItemStack(this, 1, 4));
        CustomItemList.tM_TeslaPrimary_5.set(new ItemStack(this, 1, 5));

        CustomItemList.tM_TeslaBase.set(new ItemStack(this, 1, 6));
        CustomItemList.tM_TeslaToroid.set(new ItemStack(this, 1, 7));
        CustomItemList.tM_TeslaFrame.set(new ItemStack(this, 1, 8));
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        tM0 = aIconRegister.registerIcon("gregtech:iconsets/EM_POWER");
        tM1 = aIconRegister.registerIcon("gregtech:iconsets/EM_POWER");
        tM2 = aIconRegister.registerIcon("gregtech:iconsets/EM_POWER");
        tM3 = aIconRegister.registerIcon("gregtech:iconsets/EM_POWER");
        tM4 = aIconRegister.registerIcon("gregtech:iconsets/EM_POWER");
        tM5 = aIconRegister.registerIcon("gregtech:iconsets/EM_POWER");

        tM6 = aIconRegister.registerIcon("gregtech:iconsets/EM_POWER");
        tM7 = aIconRegister.registerIcon("gregtech:iconsets/EM_POWER");
        tM8 = aIconRegister.registerIcon("gregtech:iconsets/EM_POWER");
    }

    @Override
    public IIcon getIcon(int aSide, int aMeta) {
        switch (aMeta) {
            case 0:
                return tM0;
            case 1:
                return tM1;
            case 2:
                return tM2;
            case 3:
                return tM3;
            case 4:
                return tM4;
            case 5:
                return tM5;
            case 6:
                return tM6;
            case 7:
                return tM7;
            case 8:
                return tM8;
            default:
                return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess aWorld, int xCoord, int yCoord, int zCoord, int aSide) {
        int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
        return getIcon(aSide, tMeta);
    }

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        for (int i = 0; i <= 8; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }
}
