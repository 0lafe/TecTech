package com.github.technus.tectech.proxy;

import cpw.mods.fml.common.network.IGuiHandler;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommonProxy implements IGuiHandler {
    public void registerRenderInfo() {}

    public void hint_particle(World world, int x, int y, int z, Block block, int meta){}
    public void em_particle(IGregTechTileEntity aMuffler, byte facing) {}//CUTE!
    public void em_particle(World w,double x,double y,double z){}

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    public World getClientWorld() {
        return null;
    }

    public void renderUnicodeString(String str, int x, int y, int maxWidth, int color) {
    }

    public void setCustomRenderer() {}

    public void setCustomRenderers() {}

    public void broadcast(String str) {
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(str));
    }

    public void printInchat(String... strings){}

    public void playSound(IGregTechTileEntity base,String name){}

    public void renderAABB(AxisAlignedBB box){}
    public void renderAABB(World w,AxisAlignedBB box){}

    public String getUUID(String name) {
        for(WorldServer worldServer:MinecraftServer.getServer().worldServers){
            for(Object o:worldServer.playerEntities){
                if(o instanceof EntityPlayer && ((EntityPlayer) o).getGameProfile().getName().equals(name)){
                    return ((EntityPlayer) o).getGameProfile().getId().toString();
                }
            }
        }
        return null;
    }

    public boolean isOnlineName(String name) {
        for(WorldServer worldServer:MinecraftServer.getServer().worldServers){
            for(Object o:worldServer.playerEntities){
                if(o instanceof EntityPlayer && ((EntityPlayer) o).getGameProfile().getName().equals(name)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOnlineUUID(String uuid) {
        for(WorldServer worldServer:MinecraftServer.getServer().worldServers){
            for(Object o:worldServer.playerEntities){
                if(o instanceof EntityPlayer && ((EntityPlayer) o).getGameProfile().getId().toString().equals(uuid)){
                    return true;
                }
            }
        }
        return false;
    }

    public EntityPlayer getPlayer(){
        return null;
    }
}
