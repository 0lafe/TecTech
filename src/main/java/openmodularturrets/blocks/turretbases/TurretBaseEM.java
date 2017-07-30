package openmodularturrets.blocks.turretbases;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.auxiliary.Reference;
import openmodularturrets.handler.ConfigHandler;
import openmodularturrets.tileentity.turretbase.TileTurretBaseEM;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Bass on 27/07/2017.
 */
public class TurretBaseEM extends BlockAbstractTurretBase {
    private final int MaxCharge = ConfigHandler.getBaseTierFiveMaxCharge();
    private final int MaxIO = ConfigHandler.getBaseTierFiveMaxIo();
    public static TurretBaseEM INSTANCE;

    public TurretBaseEM(){
        super();
        setCreativeTab(TecTech.mainTab);
        this.setResistance(16);
        this.setBlockName("turretBaseEM");
        this.setStepSound(Block.soundTypeMetal);
        this.setBlockTextureName(Reference.MODID+":turretBaseEM");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileTurretBaseEM(MaxCharge,MaxIO);
    }

    public void registerBlockIcons(IIconRegister p_149651_1_) {
        super.registerBlockIcons(p_149651_1_);
        this.blockIcon = p_149651_1_.registerIcon(Reference.MODID+":turretBaseEM");
    }

    public static void run() {
        INSTANCE = new TurretBaseEM();
        GameRegistry.registerBlock(INSTANCE, TurretBaseItemEM.class, INSTANCE.getUnlocalizedName());
        GameRegistry.registerTileEntity(TileTurretBaseEM.class,"TileTurretBaseEM");
    }
}
