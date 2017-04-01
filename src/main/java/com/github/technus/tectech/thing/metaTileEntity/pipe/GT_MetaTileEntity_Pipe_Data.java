package com.github.technus.tectech.thing.metaTileEntity.pipe;

import com.github.technus.tectech.elementalMatter.CommonValues;
import com.github.technus.tectech.thing.machineTT;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputData;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IColoredTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Client;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import static gregtech.api.enums.Dyes.MACHINE_METAL;

/**
 * Created by Tec on 26.02.2017.
 */
public class GT_MetaTileEntity_Pipe_Data extends MetaPipeEntity implements iConnectsToDataPipe, machineTT {
    private static Textures.BlockIcons.CustomIcon EMpipe;
    private static Textures.BlockIcons.CustomIcon EMcandy;
    public byte connectionCount = 0;

    public GT_MetaTileEntity_Pipe_Data(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0);
    }

    public GT_MetaTileEntity_Pipe_Data(String aName) {
        super(aName, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_Pipe_Data(mName);
    }

    @Override
    public float getThickNess() {
        return 0.375F;
    }

    @Override
    public void registerIcons(IIconRegister aBlockIconRegister) {
        EMpipe = new Textures.BlockIcons.CustomIcon("iconsets/EM_DATA");
        EMcandy = new Textures.BlockIcons.CustomIcon("iconsets/EM_BAR");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aConnections, byte aColorIndex, boolean aConnected, boolean aRedstone) {
        return new ITexture[]{new GT_RenderedTexture(EMpipe), new GT_RenderedTexture(EMcandy, Dyes.getModulation(aColorIndex, MACHINE_METAL.getRGBA()))};
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public void loadNBTData(NBTTagCompound nbtTagCompound) {
    }

    @Override
    public void saveNBTData(NBTTagCompound nbtTagCompound) {
    }

    @Override
    public boolean renderInside(byte b) {
        return false;
    }

    @Override
    public byte getTileEntityBaseType() {
        return 4;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.tecMark,
                "Advanced data transmission",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Don't stare at the beam!",
                EnumChatFormatting.AQUA + "Must be painted to work",
                EnumChatFormatting.AQUA + "Do not cross or split"
        };
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        return AxisAlignedBB.getBoundingBox(aX + 0.125D, aY + 0.125D, aZ + 0.125D, aX + 0.875D, aY + 0.875D, aZ + 0.875D);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        onPostTick(aBaseMetaTileEntity,31);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if ((aTick & 31) == 31) {
                mConnections = 0;
                connectionCount = 0;
                if (aBaseMetaTileEntity.getColorization() < 0) return;
                for (byte i = 0, j; i < 6; i++) {
                    j = GT_Utility.getOppositeSide(i);
                    //if (!aBaseMetaTileEntity.getCoverBehaviorAtSide(i).alwaysLookConnected(i, aBaseMetaTileEntity.getCoverIDAtSide(i), aBaseMetaTileEntity.getCoverDataAtSide(i), aBaseMetaTileEntity)) {
                        TileEntity tTileEntity = aBaseMetaTileEntity.getTileEntityAtSide(i);
                        if (tTileEntity instanceof IColoredTileEntity) {
                            //if (aBaseMetaTileEntity.getColorization() >= 0) {
                            byte tColor = ((IColoredTileEntity) tTileEntity).getColorization();
                            if (tColor != aBaseMetaTileEntity.getColorization()) continue;
                            //}
                        }
                        if (tTileEntity instanceof iConnectsToDataPipe && (((iConnectsToDataPipe) tTileEntity).canConnect(j))) {
                            mConnections |= (1 << i);
                            connectionCount++;
                        }
                        else if (tTileEntity instanceof IGregTechTileEntity && ((IGregTechTileEntity) tTileEntity).getMetaTileEntity() instanceof iConnectsToDataPipe) {
                            if (//((IGregTechTileEntity) tTileEntity).getCoverBehaviorAtSide(j).alwaysLookConnected(j, ((IGregTechTileEntity) tTileEntity).getCoverIDAtSide(j), ((IGregTechTileEntity) tTileEntity).getCoverDataAtSide(j), ((IGregTechTileEntity) tTileEntity)) ||
                                    ((iConnectsToDataPipe) ((IGregTechTileEntity) tTileEntity).getMetaTileEntity()).canConnect(j)) {
                                mConnections |= (1 << i);
                                connectionCount++;
                            }
                        }
                    //}
                    //else {
                    //    mConnections |= (1 << i);
                    //    if (mOld != mConnections) {
                    //        connectionCount++;
                    //        mOld = mConnections;
                    //    }
                    //}
                }
            }

        } else if (aBaseMetaTileEntity.isClientSide() && GT_Client.changeDetected == 4) {
            aBaseMetaTileEntity.issueTextureUpdate();
        }
    }

    @Override
    public boolean canConnect(byte side) {
        return true;
    }

    @Override
    public iConnectsToDataPipe getNext(iConnectsToDataPipe source) {
        if(connectionCount!=2) return null;
        for(byte s=0;s<6;s++){
            if((mConnections&(1<<s))==0) continue;//if not connected continue
            final IGregTechTileEntity next=getBaseMetaTileEntity().getIGregTechTileEntityAtSide(s);
            if(next==null) continue;
            final IMetaTileEntity meta=next.getMetaTileEntity();
            if(meta instanceof iConnectsToDataPipe && meta!=source){
                if(meta instanceof GT_MetaTileEntity_Hatch_InputData)
                    return (iConnectsToDataPipe)meta;
                if(meta instanceof GT_MetaTileEntity_Pipe_Data &&
                        ((GT_MetaTileEntity_Pipe_Data) meta).connectionCount==2)
                    return (iConnectsToDataPipe)meta;
            }
        }
        return null;
    }
}
