package com.github.technus.tectech.things.metaTileEntity;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.things.machineTT;
import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStackTree;
import com.github.technus.tectech.elementalMatter.classes.tElementalException;
import com.github.technus.tectech.elementalMatter.commonValues;
import com.github.technus.tectech.elementalMatter.interfaces.iConnectsToEMpipe;
import com.github.technus.tectech.elementalMatter.interfaces.iElementalInstanceContainer;
import com.github.technus.tectech.things.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_MufflerElemental;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import static com.github.technus.tectech.elementalMatter.commonValues.*;
import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity;

/**
 * Created by danie_000 on 11.12.2016.
 */
public abstract class GT_MetaTileEntity_Hatch_ElementalContainer extends GT_MetaTileEntity_Hatch implements iElementalInstanceContainer, iConnectsToEMpipe, machineTT {
    private static Textures.BlockIcons.CustomIcon EM_T_SIDES;
    private static Textures.BlockIcons.CustomIcon EM_T_ACTIVE;

    protected cElementalInstanceStackTree content = new cElementalInstanceStackTree();
    //float lifeTimeMult=1f;
    int postEnergize = 0;
    float overflowMatter = 0f;
    public short id = -1;
    private byte deathDelay = 2;

    public GT_MetaTileEntity_Hatch_ElementalContainer(int aID, String aName, String aNameRegional, int aTier, String descr) {
        super(aID, aName, aNameRegional, aTier, 0, descr);
    }

    public GT_MetaTileEntity_Hatch_ElementalContainer(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        EM_T_ACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_EM_T_ACTIVE");
        EM_T_SIDES = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_EM_T_SIDES");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(EM_T_ACTIVE, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(EM_T_SIDES, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT)};
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("postEnergize", postEnergize);
        //aNBT.setFloat("lifeTimeMult",lifeTimeMult);
        aNBT.setFloat("overflowMatter", overflowMatter);
        aNBT.setTag("eM_Stacks", content.toNBT());
        aNBT.setShort("eID", id);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        postEnergize = aNBT.getInteger("postEnergize");
        //lifeTimeMult=aNBT.getFloat("lifeTimeMult");
        overflowMatter = aNBT.getFloat("overflowMatter");
        id = aNBT.getShort("eID");
        try {
            content = cElementalInstanceStackTree.fromNBT(aNBT.getCompoundTag("eM_Stacks"));
        } catch (tElementalException e) {
            if (TecTech.ModConfig.DEBUG_MODE) e.printStackTrace();
            if (content == null) content = new cElementalInstanceStackTree();
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            byte Tick = (byte) (aTick % 20);
            if (decayAt == Tick) {
                purgeOverflow();
                content.tickContent(postEnergize);//Hatches don't life time mult things
                purgeOverflow();
            } else if (overflowAt == Tick) {
                if (overflowMatter <= 0) {
                    deathDelay = 3;
                } else {
                    if (deathDelay == 2) {
                        if (TecTech.ModConfig.BOOM_ENABLE && TecTech.Rnd.nextInt(10) == 0)
                            aBaseMetaTileEntity.setOnFire();
                        else
                            TecTech.proxy.broadcast("Container0 FIRE! " + getBaseMetaTileEntity().getXCoord() + " " + getBaseMetaTileEntity().getYCoord() + " " + getBaseMetaTileEntity().getZCoord());
                    } else if (deathDelay == 1) {
                        IGregTechTileEntity tGTTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityAtSide(aBaseMetaTileEntity.getBackFacing());
                        if (tGTTileEntity == null || !(tGTTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_MufflerElemental))
                            tGTTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityAtSide((byte) 0);
                        if (tGTTileEntity == null || !(tGTTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_MufflerElemental))
                            tGTTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityAtSide((byte) 1);
                        if (tGTTileEntity != null && (tGTTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_MufflerElemental)) {
                            GT_MetaTileEntity_Hatch_MufflerElemental aMetaTileEntity = (GT_MetaTileEntity_Hatch_MufflerElemental) tGTTileEntity.getMetaTileEntity();
                            aMetaTileEntity.overflowMatter += overflowMatter;
                            if (aMetaTileEntity.overflowMatter > aMetaTileEntity.overflowMax) {
                                if (TecTech.ModConfig.BOOM_ENABLE) tGTTileEntity.doExplosion(V[14]);
                                else
                                    TecTech.proxy.broadcast("Container1 BOOM! " + getBaseMetaTileEntity().getXCoord() + " " + getBaseMetaTileEntity().getYCoord() + " " + getBaseMetaTileEntity().getZCoord());
                            } else overflowMatter = 0F;
                        }
                    } else if (deathDelay < 1) {
                        if (TecTech.ModConfig.BOOM_ENABLE) getBaseMetaTileEntity().doExplosion(V[14]);
                        else
                            TecTech.proxy.broadcast("Container2 BOOM! " + getBaseMetaTileEntity().getXCoord() + " " + getBaseMetaTileEntity().getYCoord() + " " + getBaseMetaTileEntity().getZCoord());
                    }
                    deathDelay--;
                }
            } else if (moveAt == Tick) {
                moveAround(aBaseMetaTileEntity);
                getBaseMetaTileEntity().setActive(content.hasStacks());
            }
        }
    }

    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
    }

    @Override
    public cElementalInstanceStackTree getContainerHandler() {
        return content;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isLiquidInput(byte aSide) {
        return false;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    public int getMaxStacksCount() {
        return mTier * 2;
    }

    public int getMaxStackSize() {
        return mTier * (mTier - 7) * 1000;
    }

    @Override
    public float purgeOverflow() {
        return overflowMatter += content.removeOverflow(getMaxStacksCount(), getMaxStackSize());
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        if (id > 0) {
            if (content == null || content.size() == 0)
                return new String[]{"ID: " + EnumChatFormatting.AQUA + id, "No Stacks"};
            else {
                final String[] lines = content.getElementalInfo();
                final String[] output = new String[lines.length + 1];
                output[0] = "ID: " + EnumChatFormatting.AQUA + id;
                System.arraycopy(lines, 0, output, 1, lines.length);
                return output;
            }
        }
        if (content == null || content.size() == 0) return new String[]{"No Stacks"};
        return content.getElementalInfo();
    }

    public float updateSlots() {
        return purgeOverflow();
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                commonValues.tecMark,
                mDescription,
                "Max stacks amount: " + EnumChatFormatting.AQUA + getMaxStacksCount(),
                "Stack capacity: " + EnumChatFormatting.AQUA + getMaxStackSize(),
                "Place Overflow Hatch behind,on top or below",
                "to provide overflow protection while this block",
                "is not attached to multi block.",
                "Transport range can be extended in straight",
                "line up to 15 blocks with quantum tunnels.",
                EnumChatFormatting.AQUA + "Must be painted to work"
        };
    }

    @Override
    public void onRemoval() {
        if (isValidMetaTileEntity(this) && getBaseMetaTileEntity().isActive())
            if (TecTech.ModConfig.BOOM_ENABLE) getBaseMetaTileEntity().doExplosion(V[15]);
            else
                TecTech.proxy.broadcast("BOOM! " + getBaseMetaTileEntity().getXCoord() + " " + getBaseMetaTileEntity().getYCoord() + " " + getBaseMetaTileEntity().getZCoord());
    }
}
