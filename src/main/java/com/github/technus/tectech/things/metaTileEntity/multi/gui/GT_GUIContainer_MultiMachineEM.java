package com.github.technus.tectech.things.metaTileEntity.multi.gui;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

/**
 * Created by Tec on 21.02.2017.
 */

public class GT_GUIContainer_MultiMachineEM extends GT_GUIContainerMetaTile_Machine {
    String mName = "";
    private static byte cntr = 0;

    public GT_GUIContainer_MultiMachineEM(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aTextureFile) {
        super(new GT_Container_MultiMachineEM(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "multimachines/" + (aTextureFile == null ? "MultiblockDisplay" : aTextureFile));
        mName = aName;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString(mName, 10, -18, 16448255);

        if (mContainer != null) {
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 1) != 0)
                fontRendererObj.drawString("Pipe is loose.", 10, -10, 16448255);
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 2) != 0)
                fontRendererObj.drawString("Screws are missing.", 10, -2, 16448255);
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 4) != 0)
                fontRendererObj.drawString("Something is stuck.", 10, 6, 16448255);
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 8) != 0)
                fontRendererObj.drawString("Platings are dented.", 10, 14, 16448255);
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 16) != 0)
                fontRendererObj.drawString("Circuitry burned out.", 10, 22, 16448255);
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 32) != 0)
                fontRendererObj.drawString("That doesn't belong there.", 10, 30, 16448255);
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 64) != 0)
                fontRendererObj.drawString("Incomplete Structure.", 10, 38, 16448255);
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 128) != 0)
                fontRendererObj.drawString("Too Uncertain.", 10, 46, 16448255);
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 256) != 0)
                fontRendererObj.drawString("Invalid Parameters.", 10, 54, 16448255);

            if (((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode == 0) {
                if (((GT_Container_MultiMachineEM) mContainer).mActive == 0) {
                    fontRendererObj.drawString("Hit with Soft Hammer", 10, -10, 16448255);
                    fontRendererObj.drawString("to (re-)start the Machine", 10, -2, 16448255);
                    fontRendererObj.drawString("if it doesn't start.", 10, 6, 16448255);
                } else {
                    fontRendererObj.drawString("Running perfectly.", 10, -10, 16448255);
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        y -= 26;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize + 26);
        if (this.mContainer != null && ((GT_Container_MultiMachineEM) this.mContainer).eParamsInStatus != null) {
            cntr = (byte) ((1 + cntr) % 6);
            GL11.glColor4f(1f, 1f, 1f, 1f);

            if (((GT_Container_MultiMachineEM) mContainer).ePowerPass)
                drawTexturedModalRect(x + 151, y + 25, 183, 25, 18, 18);

            if (((GT_Container_MultiMachineEM) mContainer).eSafeVoid)
                drawTexturedModalRect(x + 151, y + 46, 183, 46, 18, 18);

            x += 11;
            y += 96;
            for (int i = 0; i < 20; ) {
                byte j = (byte) (i >>> 1);
                LEDdrawP(x, y, i, 0, ((GT_Container_MultiMachineEM) this.mContainer).eParamsInStatus[j]);
                LEDdrawP(x, y, i++, 1, ((GT_Container_MultiMachineEM) this.mContainer).eParamsOutStatus[j]);
                LEDdrawP(x, y, i, 0, ((GT_Container_MultiMachineEM) this.mContainer).eParamsInStatus[j + 10]);
                LEDdrawP(x, y, i++, 1, ((GT_Container_MultiMachineEM) this.mContainer).eParamsOutStatus[j + 10]);
            }

            final short rU = 183, Vs = 77;
            x += 140;
            y -= 19;
            final byte state = ((GT_Container_MultiMachineEM) this.mContainer).eCertainStatus;
            switch (((GT_Container_MultiMachineEM) this.mContainer).eCertainMode) {
                case 1://ooo oxo ooo
                    drawTexturedModalRect(x + 6, y + 6,
                            rU + (state == 0 ? 38 : 6),
                            Vs + 6, 6, 6);
                    break;
                case 2://ooo xox ooo
                    drawTexturedModalRect(x, y + 6,
                            rU + ((state & 1) == 0 ? 32 : 0),
                            Vs + 6, 6, 6);
                    drawTexturedModalRect(x + 12, y + 6,
                            rU + ((state & 2) == 0 ? 44 : 12),
                            Vs + 6, 6, 6);
                    break;
                case 3://oxo xox oxo
                    drawTexturedModalRect(x + 6, y,
                            rU + ((state & 1) == 0 ? 38 : 6),
                            Vs, 6, 6);
                    drawTexturedModalRect(x, y + 6,
                            rU + ((state & 2) == 0 ? 32 : 0),
                            Vs + 6, 6, 6);
                    drawTexturedModalRect(x + 12, y + 6,
                            rU + ((state & 4) == 0 ? 44 : 12),
                            Vs + 6, 6, 6);
                    drawTexturedModalRect(x + 6, y + 12,
                            rU + ((state & 8) == 0 ? 38 : 6),
                            Vs + 12, 6, 6);
                    break;
                case 4://xox ooo xox
                    drawTexturedModalRect(x, y,
                            rU + ((state & 1) == 0 ? 32 : 0),
                            Vs, 6, 6);
                    drawTexturedModalRect(x + 12, y,
                            rU + ((state & 2) == 0 ? 44 : 12),
                            Vs, 6, 6);
                    drawTexturedModalRect(x, y + 12,
                            rU + ((state & 4) == 0 ? 32 : 0),
                            Vs + 12, 6, 6);
                    drawTexturedModalRect(x + 12, y + 12,
                            rU + ((state & 8) == 0 ? 44 : 12),
                            Vs + 12, 6, 6);
                    break;
                case 5://xox ooo xox
                    drawTexturedModalRect(x, y,
                            rU + ((state & 1) == 0 ? 32 : 0),
                            Vs, 6, 6);
                    drawTexturedModalRect(x + 12, y,
                            rU + ((state & 2) == 0 ? 44 : 12),
                            Vs, 6, 6);
                    drawTexturedModalRect(x + 6, y + 6,
                            rU + ((state & 4) == 0 ? 38 : 6),
                            Vs + 6, 6, 6);
                    drawTexturedModalRect(x, y + 12,
                            rU + ((state & 8) == 0 ? 32 : 0),
                            Vs + 12, 6, 6);
                    drawTexturedModalRect(x + 12, y + 12,
                            rU + ((state & 16) == 0 ? 44 : 12),
                            Vs + 12, 6, 6);
                    break;
            }
        }
    }

    private void LEDdrawP(int x, int y, int i, int j, byte status) {
        final int v = 192, su = 8, sv = 6, u = 11;
        switch (status) {
            case 6:
                if (cntr < 2) {
                    drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * j, su, sv);
                    break;
                } else if (cntr < 4) {
                    drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (8 + j), su, sv);
                    break;
                }
            case 1://ok
                drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (4 + j), su, sv);
                break;
            case 2://too low blink
                if (cntr < 3) {
                    drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * j, su, sv);
                    break;
                }
            case 3:// too low
                drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (2 + j), su, sv);
                break;
            case 4://too high blink
                if (cntr < 3) {
                    drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (8 + j), su, sv);
                    break;
                }
            case 5:// too high
                drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (6 + j), su, sv);
                break;
        }
    }
}