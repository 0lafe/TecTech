package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.GT_Container_CasingsTT;
import com.github.technus.tectech.thing.metaTileEntity.constructableTT;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.Util.StructureBuilder;
import static com.github.technus.tectech.thing.casing.GT_Container_CasingsTT.sBlockCasingsTT;
import static gregtech.api.enums.GT_Values.E;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_bhg extends GT_MetaTileEntity_MultiblockBase_EM implements constructableTT {
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    //Time dillatation - to slow down the explosion thing but REALLY REDUCE POWER OUTPUT
    //Startcodes to startup
    //per dim disable thingies

    //region Structure
    private static final String[][] shape = new String[][]{
            {"\u000B","M0000000","L00     00","L0       0","L0  !!!  0","L0  !.!  0","L0  !!!  0","L0       0","L00     00","M0000000",},
            {"\u0008","O0A0","O0A0","O0A0","O0A0","N11111","M1101011","I000010010010000","M1111111","I000010010010000","M1101011","N11111","O0A0","O0A0","O0A0","O0A0",},
            {"\u0006","O0A0","O0A0","O0A0","P1","P1","M1111111","L11E11","L1B222B1","G000B1A23332A1B000","J111A23332A111","G000B1A23332A1B000","L1B222B1","L11E11","M1111111","P1","P1","O0A0","O0A0","O0A0",},
            {"\u0005","O0A0","O0A0","P1","P1","\u0004","F00Q00","H11M11","F00Q00","\u0003","P4","P1","P1","O0A0","O0A0",},
            {"\u0004","O0A0","N00000","P1","P4","P4","\u0003","F0S0","E00S00","F0144M4410","E00S00","F0S0","\u0003","P4","P4","P1","N00000","O0A0",},
            {"\u0003","O0A0","O0A0","P1","M2224222","\u0004","G2Q2","G2Q2","D00A2Q2A00","F14Q41","D00A2Q2A00","G2Q2","G2Q2","\u0004","M2224222","P1","O0A0","O0A0",},
            {"\u0002","O0A0","N00000","P1","P4","\u0006","D0W0","C00W00","D014S410","C00W00","D0W0","\u0006","P4","P1","N00000","O0A0",},
            {"\u0001","O0A0","O0A0","P1","M2224222","\u0006","E2U2","E2U2","B00A2U2A00","D14U41","B00A2U2A00","E2U2","E2U2","\u0006","M2224222","P1","O0A0","O0A0",},
            {"\u0001","O0A0","P1","P4","\u0009","B0[0","C14W41","B0[0","\u0009","P4","P1","O0A0",},
            {E,"O0A0","O0A0","P1","P4","\u0009","A00[00","C14W41","A00[00","\u0009","P4","P1","O0A0","O0A0",},
            {E,"O0A0","P1","\u000B","A0]0","B1[1","A0]0","\u000B","P1","O0A0",},
            {E,"O0A0","P1","\u000B","A0]0","B1[1","A0]0","\u000B","P1","O0A0",},
            {"O0A0","O0A0","M1111111","\u0009","B1[1","B1[1","001[100","B1[1","001[100","B1[1","B1[1","\u0009","M1111111","O0A0","O0A0",},
            {"O0A0","N11111","L11E11","\u0001","G2Q2",E,"E2U2","\u0003","B1[1","B1[1","A1]1","01]10","A1]1","01]10","A1]1","B1[1","B1[1","\u0003","E2U2",E,"G2Q2","\u0001","L11E11","N11111","O0A0",},
            {"O0A0","M1101011","L1B222B1",E,"F0S0","G2Q2","D0W0","E2U2","\u0003","B1[1","A1]1","A1]1","002[200","A12[21","002[200","A1]1","A1]1","B1[1","\u0003","E2U2","D0W0","G2Q2","F0S0",E,"L1B222B1","M1101011","O0A0",},
            {"L000000000","I000010010010000","G000B1A23332A1B000","F00Q00","E00S00","D00A2Q2A00","C00W00","B00A2U2A00","B0[0","A00[00","A0]0","A0]0","001[100","01]10","002[200","003[300","013[310","003[300","002[200","01]10","001[100","A0]0","A0]0","A00[00","B0[0","B00A2U2A00","C00W00","D00A2Q2A00","E00S00","F00Q00","G000B1A23332A1B000","I000010010010000","L000000000",},
            {"O0A0","M1111111","J111A23332A111","H11M11","F0144M4410","F14Q41","D014S410","D14U41","C14W41","C14W41","B1[1","B1[1","B1[1","A1]1","A12[21","013[310","A13[31","013[310","A12[21","A1]1","B1[1","B1[1","B14Y41","C14W41","C14W41","D14U41","D014S410","F14Q41","F0144M4410","H11M11","J111A23332A111","M1111111","O0A0",},
            {"L000000000","I000010010010000","G000B1A23332A1B000","F00Q00","E00S00","D00A2Q2A00","C00W00","B00A2U2A00","B0[0","A00[00","A0]0","A0]0","001[100","01]10","002[200","003[300","013[310","003[300","002[200","01]10","001[100","A0]0","A0]0","A00[00","B0[0","B00A2U2A00","C00W00","D00A2Q2A00","E00S00","F00Q00","G000B1A23332A1B000","I000010010010000","L000000000",},
            {"O0A0","M1101011","L1B222B1",E,"F0S0","G2Q2","D0W0","E2U2","\u0003","B1[1","A1]1","A1]1","002[200","A12[21","002[200","A1]1","A1]1","B1[1","\u0003","E2U2","D0W0","G2Q2","F0S0",E,"L1B222B1","M1101011","O0A0",},
            {"O0A0","N11111","L11E11","\u0001","G2Q2",E,"E2U2","\u0003","B1[1","B1[1","A1]1","01]10","A1]1","01]10","A1]1","B1[1","B1[1","\u0003","E2U2",E,"G2Q2","\u0001","L11E11","N11111","O0A0",},
            {"O0A0","O0A0","M1111111","\u0009","B1[1","B1[1","001[100","B1[1","001[100","B1[1","B1[1","\u0009","M1111111","O0A0","O0A0",},
            {E,"O0A0","P1","\u000B","A0]0","B1[1","A0]0","\u000B","P1","O0A0",},
            {E,"O0A0","P1","\u000B","A0]0","B1[1","A0]0","\u000B","P1","O0A0",},
            {E,"O0A0","O0A0","P1","P4","\u0009","A00[00","C14W41","A00[00","\u0009","P4","P1","O0A0","O0A0",},
            {"\u0001","O0A0","P1","P4","\u0009","B0[0","C14W41","B0[0","\u0009","P4","P1","O0A0",},
            {"\u0001","O0A0","O0A0","P1","M2224222","\u0006","E2U2","E2U2","B00A2U2A00","D14U41","B00A2U2A00","E2U2","E2U2","\u0006","M2224222","P1","O0A0","O0A0",},
            {"\u0002","O0A0","N00000","P1","P4","\u0006","D0W0","C00W00","D014S410","C00W00","D0W0","\u0006","P4","P1","N00000","O0A0",},
            {"\u0003","O0A0","O0A0","P1","M2224222","\u0004","G2Q2","G2Q2","D00A2Q2A00","F14Q41","D00A2Q2A00","G2Q2","G2Q2","\u0004","M2224222","P1","O0A0","O0A0",},
            {"\u0004","O0A0","N00000","P1","P4","P4","\u0003","F0S0","E00S00","F0144M4410","E00S00","F0S0","\u0003","P4","P4","P1","N00000","O0A0",},
            {"\u0005","O0A0","O0A0","P1","P1","\u0004","F00Q00","H11M11","F00Q00","\u0003","P4","P1","P1","O0A0","O0A0",},
            {"\u0006","O0A0","O0A0","O0A0","P1","P1","M1111111","L11E11","L1B222B1","G000B1A23332A1B000","J111A23332A111","G000B1A23332A1B000","L1B222B1","L11E11","M1111111","P1","P1","O0A0","O0A0","O0A0",},
            {"\u0008","O0A0","O0A0","O0A0","O0A0","N11111","M1101011","I000010010010000","M1111111","I000010010010000","M1101011","N11111","O0A0","O0A0","O0A0","O0A0",},
            {"\u000B","O0A0","O0A0","O0A0","L000000000","O0A0","L000000000","O0A0","O0A0","O0A0",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT,sBlockCasingsTT,sBlockCasingsTT,sBlockCasingsTT,sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{12, 13, 14, 10, 11};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList", "addElementalToMachineList"};
    private static final byte[] casingTextures = new byte[]{textureOffset, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4};
    //endregion

    public GT_MetaTileEntity_EM_bhg(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_bhg(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_bhg(this.mName);
    }

    @Override
    public boolean EM_checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return EM_StructureCheckAdvanced(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 16, 16, 0);
    }

    @Override
    public void construct(int qty) {
        StructureBuilder(shape, blockType, blockMeta,16, 16, 0, getBaseMetaTileEntity());
    }

    @Override
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_BHG");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_BHG_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[textureOffset + 12], new GT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[textureOffset + 12]};
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.tecMark,
                "Singularity based power generation.",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Super unstable!!!"
        };
    }
}
