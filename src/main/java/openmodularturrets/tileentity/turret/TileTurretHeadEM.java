package openmodularturrets.tileentity.turret;

import com.github.technus.tectech.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.thing.item.DebugElementalInstanceContainer_EM;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import openmodularturrets.entity.projectiles.TurretProjectile;
import openmodularturrets.entity.projectiles.projectileEM;
import openmodularturrets.handler.ConfigHandler;
import openmodularturrets.tileentity.turretbase.TileTurretBaseEM;
import openmodularturrets.tileentity.turrets.TurretHead;
import openmodularturrets.util.TurretHeadUtil;

/**
 * Created by Bass on 27/07/2017.
 */
public class TileTurretHeadEM extends TurretHead{
    private cElementalInstanceStackMap hatchContentPointer;

    @Override
    public int getTurretRange() {
        return ConfigHandler.getLaserTurretSettings().getRange()<<1;
    }

    @Override
    public int getTurretPowerUsage() {
        return ConfigHandler.getLaserTurretSettings().getPowerUsage()<<4;
    }

    @Override
    public int getTurretFireRate() {
        return (int)Math.ceil(ConfigHandler.getLaserTurretSettings().getFireRate()/2f);
    }

    @Override
    public double getTurretAccuracy() {
        return (int)Math.ceil(ConfigHandler.getLaserTurretSettings().getAccuracy() / 10.0F);
    }

    @Override
    public void updateEntity() {
        if(!worldObj.isRemote && base instanceof TileTurretBaseEM) {
            hatchContentPointer = ((TileTurretBaseEM) base).getContainerHandler();
        }
        super.updateEntity();
    }

    @Override
    public boolean requiresAmmo() {
        return hatchContentPointer == null || !hatchContentPointer.hasStacks();
    }

    @Override
    public boolean requiresSpecificAmmo() {
        return true;//to enable failure in shooting when there is no EM to use
    }

    @Override
    public Item getAmmo() {
        return DebugElementalInstanceContainer_EM.INSTANCE;//Placeholder item that cannot be achieved, yet still usable for debug
    }

    @Override
    public final TurretProjectile createProjectile(World world, Entity target, ItemStack ammo) {
        return new projectileEM(world, TurretHeadUtil.getTurretBase(worldObj, xCoord, yCoord, zCoord), hatchContentPointer);
    }

    @Override
    public String getLaunchSoundEffect() {
        return "laser";
    }
}
