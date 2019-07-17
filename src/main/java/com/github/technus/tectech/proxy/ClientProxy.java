package com.github.technus.tectech.proxy;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.compatibility.openmodularturrets.TT_turret_loader;
import com.github.technus.tectech.entity.fx.BlockHint;
import com.github.technus.tectech.entity.fx.WeightlessParticleFX;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.block.QuantumGlassRender;
import com.github.technus.tectech.thing.block.QuantumStuffBlock;
import com.github.technus.tectech.thing.block.QuantumStuffRender;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderInfo() {
        QuantumGlassBlock.renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(QuantumGlassBlock.renderID, new QuantumGlassRender());

        QuantumStuffBlock.renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(QuantumStuffBlock.renderID, new QuantumStuffRender());

        if(Loader.isModLoaded("openmodularturrets")) {
            new TT_turret_loader().run();
        }
    }

    @Override
    public void hint_particle(World w,int x, int y, int z, Block block, int meta) {
        Minecraft.getMinecraft().effectRenderer.addEffect(new BlockHint(w,x,y,z,block,meta));

        EntityFX particle = new WeightlessParticleFX(w, x + TecTech.RANDOM.nextFloat() * 0.5F, y + TecTech.RANDOM.nextFloat() * 0.5F, z + TecTech.RANDOM.nextFloat() * 0.5F, 0, 0, 0);
        particle.setRBGColorF(0, 0.6F * TecTech.RANDOM.nextFloat(), 0.8f);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    @Override
    public void em_particle(IGregTechTileEntity aMuffler, byte facing) {//CUTE!
        ForgeDirection aDir = ForgeDirection.getOrientation(facing);
        float xPos = aDir.offsetX * 0.76F + aMuffler.getXCoord() + 0.25F;
        float yPos = aDir.offsetY * 0.76F + aMuffler.getYCoord() + 0.25F;
        float zPos = aDir.offsetZ * 0.76F + aMuffler.getZCoord() + 0.25F;

        EntityFX particle = new WeightlessParticleFX(aMuffler.getWorld(), xPos + TecTech.RANDOM.nextFloat() * 0.5F, yPos + TecTech.RANDOM.nextFloat() * 0.5F, zPos + TecTech.RANDOM.nextFloat() * 0.5F, 0, 0, 0);
        particle.setRBGColorF(0, 0.6F * TecTech.RANDOM.nextFloat(), 0.8f);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    @Override
    public void pollutor_particle(IGregTechTileEntity aMuffler, byte facing) {
        ForgeDirection aDir = ForgeDirection.getOrientation(facing);
        float xPos = aDir.offsetX * 0.76F + aMuffler.getXCoord() + 0.25F;
        float yPos = aDir.offsetY * 0.76F + aMuffler.getYCoord() + 0.25F;
        float zPos = aDir.offsetZ * 0.76F + aMuffler.getZCoord() + 0.25F;

        float ySpd = aDir.offsetY * 0.1F + 0.2F + 0.1F * (float)TecTech.RANDOM.nextGaussian();
        float xSpd;
        float zSpd;

        if (aDir.offsetY == -1) {
            float temp = TecTech.RANDOM.nextFloat() * 2 * (float) Math.PI;
            xSpd = (float) Math.sin(temp) * 0.1F*(float)TecTech.RANDOM.nextGaussian();
            zSpd = (float) Math.cos(temp) * 0.1F*(float)TecTech.RANDOM.nextGaussian();
        } else {
            xSpd = aDir.offsetX * (0.1F + 0.2F *(float)TecTech.RANDOM.nextGaussian());
            zSpd = aDir.offsetZ * (0.1F + 0.2F *(float)TecTech.RANDOM.nextGaussian());
        }
        aMuffler.getWorld().spawnParticle("largesmoke", xPos + TecTech.RANDOM.nextFloat() * 0.5F, yPos + TecTech.RANDOM.nextFloat() * 0.5F, zPos + TecTech.RANDOM.nextFloat() * 0.5F, xSpd, ySpd, zSpd);
        aMuffler.getWorld().spawnParticle("largesmoke", xPos + TecTech.RANDOM.nextFloat() * 0.5F, yPos + TecTech.RANDOM.nextFloat() * 0.5F, zPos + TecTech.RANDOM.nextFloat() * 0.5F, xSpd, ySpd, zSpd);
        aMuffler.getWorld().spawnParticle("largesmoke", xPos + TecTech.RANDOM.nextFloat() * 0.5F, yPos + TecTech.RANDOM.nextFloat() * 0.5F, zPos + TecTech.RANDOM.nextFloat() * 0.5F, xSpd, ySpd, zSpd);
    }

    @Override
    public void em_particle(World w,double x, double y, double z) {//CUTE!
        EntityFX particle = new WeightlessParticleFX(w,
                x + TecTech.RANDOM.nextFloat() * 0.5F,
                y + TecTech.RANDOM.nextFloat() * 0.5F,
                z + TecTech.RANDOM.nextFloat() * 0.5F,
                0,
                0,
                0);
        particle.setRBGColorF(0, 0.6F * TecTech.RANDOM.nextFloat(), 0.8f);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    @Override
    public void pollutor_particle(World w,double x, double y, double z) {
       w.spawnParticle("largesmoke",
                x + TecTech.RANDOM.nextFloat() * 0.5F,
                y + TecTech.RANDOM.nextFloat() * 0.5F,
                z + TecTech.RANDOM.nextFloat() * 0.5F,
                0,
                0,
                0);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public void renderUnicodeString(String str, int x, int y, int maxWidth, int color) {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fontRenderer = mc.fontRenderer;

        boolean origFont = fontRenderer.getUnicodeFlag();

        if (mc.gameSettings.guiScale == 3) {
            fontRenderer.setUnicodeFlag(true);
            float dist = 0.08F;
            y--;
            for (int cycle = 0; cycle < 2; cycle++) {
                GL11.glTranslatef(-dist, 0F, 0F);
                fontRenderer.drawSplitString(str, x, y, maxWidth, color);
                GL11.glTranslatef(dist, -dist, 0F);
                fontRenderer.drawSplitString(str, x, y, maxWidth, color);
                GL11.glTranslatef(dist, 0F, 0F);
                fontRenderer.drawSplitString(str, x, y, maxWidth, color);
                GL11.glTranslatef(-dist, dist, 0F);

                dist = -dist;
            }
            fontRenderer.setUnicodeFlag(origFont);
        } else {
            fontRenderer.drawSplitString(str, x, y, maxWidth, color);
        }
    }

    @Override
    public void printInchat(String... strings) {
        GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        for (String s : strings) {
            chat.printChatMessage(new ChatComponentText(s));
        }
    }

    @Override
    public void playSound(IGregTechTileEntity base,String name) {
        base.getWorld().playSoundEffect(base.getXCoord(),base.getYCoord(),base.getZCoord(), Reference.MODID+':'+name, 1, 1);
    }

    @Override
    public void renderAABB(World w,AxisAlignedBB box) {
        em_particle(w,box.minX,box.minY,box.minZ);
        em_particle(w,box.minX,box.minY,box.maxZ);
        em_particle(w,box.minX,box.maxY,box.maxZ);
        em_particle(w,box.minX,box.maxY,box.minZ);
        em_particle(w,box.maxX,box.maxY,box.minZ);
        em_particle(w,box.maxX,box.maxY,box.maxZ);
        em_particle(w,box.maxX,box.minY,box.maxZ);
        em_particle(w,box.maxX,box.minY,box.minZ);
    }

    @Override
    public EntityClientPlayerMP getPlayer(){
        return Minecraft.getMinecraft().thePlayer;
    }

    public boolean isThePlayer(EntityPlayer player){
        return getPlayer()==player;
    }
}
