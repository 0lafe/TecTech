package openmodularturrets.tileentity.turretbase;

import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStackMap;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputElemental;
import cpw.mods.fml.common.Optional;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import openmodularturrets.tileentity.turretbase.TurretBaseTierFiveTileEntity;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;

/**
 * Created by Bass on 27/07/2017.
 */

public class TileTurretBaseEM extends TurretBaseTierFiveTileEntity {
    public TileTurretBaseEM(int MaxEnergyStorage, int MaxIO) {
        super(MaxEnergyStorage, MaxIO);
    }

    @Optional.Method(
            modid = "OpenComputers"
    )
    public String getComponentName() {
        return "turretBaseEM";
    }

    public final cElementalInstanceStackMap getContainerHandler() {
        World worldIn = getWorldObj();
        TileEntity te;
        if ((te = worldIn.getTileEntity(xCoord + 1, yCoord, zCoord)) instanceof IGregTechTileEntity &&
                ((IGregTechTileEntity) te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental)
            return getFromHatch((GT_MetaTileEntity_Hatch_InputElemental) ((IGregTechTileEntity) te).getMetaTileEntity());

        if ((te = worldIn.getTileEntity(xCoord - 1, yCoord, zCoord)) instanceof IGregTechTileEntity &&
                ((IGregTechTileEntity) te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental)
            return getFromHatch((GT_MetaTileEntity_Hatch_InputElemental) ((IGregTechTileEntity) te).getMetaTileEntity());

        if ((te = worldIn.getTileEntity(xCoord, yCoord + 1, zCoord)) instanceof IGregTechTileEntity &&
                ((IGregTechTileEntity) te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental)
            return getFromHatch((GT_MetaTileEntity_Hatch_InputElemental) ((IGregTechTileEntity) te).getMetaTileEntity());

        if ((te = worldIn.getTileEntity(xCoord, yCoord - 1, zCoord)) instanceof IGregTechTileEntity &&
                ((IGregTechTileEntity) te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental)
            return getFromHatch((GT_MetaTileEntity_Hatch_InputElemental) ((IGregTechTileEntity) te).getMetaTileEntity());

        if ((te = worldIn.getTileEntity(xCoord, yCoord, zCoord + 1)) instanceof IGregTechTileEntity &&
                ((IGregTechTileEntity) te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental)
            return getFromHatch((GT_MetaTileEntity_Hatch_InputElemental) ((IGregTechTileEntity) te).getMetaTileEntity());

        if ((te = worldIn.getTileEntity(xCoord, yCoord, zCoord - 1)) instanceof IGregTechTileEntity &&
                ((IGregTechTileEntity) te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental)
            return getFromHatch((GT_MetaTileEntity_Hatch_InputElemental) ((IGregTechTileEntity) te).getMetaTileEntity());

        return null;
    }

    private cElementalInstanceStackMap getFromHatch(GT_MetaTileEntity_Hatch_InputElemental hatch) {
        hatch.mMachineBlock = textureOffset + 4;
        return hatch.getContainerHandler();
    }
}
