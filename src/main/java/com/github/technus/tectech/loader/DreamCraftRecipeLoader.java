package com.github.technus.tectech.loader;

import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Tec on 06.08.2017.
 */
public class DreamCraftRecipeLoader implements Runnable {
    @Override
    public void run() {
        Object[] o = new Object[0];

        //Quantum Glass
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Containment.get(1, o),
                GT_ModHandler.getIC2Item("reinforcedGlass", 1L)
        }, Materials.Trinium.getMolten(576), new ItemStack(QuantumGlassBlock.INSTANCE, 1), 200, 500000, true);


        //region pipes

        //Data
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Circuit_Parts_GlassFiber.get(8, o),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silver, 8)
        }, Materials.Polytetrafluoroethylene.getMolten(144), CustomItemList.DATApipe.get(1, o), 200, 30720, true);

        //endregion


        //region casing

        //High Power Casing
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Iridium, 6),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Cobalt, 16),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 16),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NiobiumTitanium, 2)
        }, Materials.TungstenSteel.getMolten(576), CustomItemList.eM_Power.get(1, o), 100, 30720, true);

        //Computer Casing
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Power.get(1, o),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 8),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NiobiumTitanium, 2)
        }, Materials.Aluminium.getMolten(1296), CustomItemList.eM_Computer_Casing.get(1, o), 200, 122880, true);
        //Computer Vent Casing
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1),
                ItemList.Electric_Motor_IV.get(2, o),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 2),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.StainlessSteel, 16),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Copper, 16),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 1)
        }, Materials.SolderingAlloy.getMolten(1296), CustomItemList.eM_Computer_Vent.get(1, o), 100, 1920, true);
        //Advanced Computer Casing
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Computer_Casing.get(1, o),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Cobalt, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Superconductor, 4)
        }, Materials.Iridium.getMolten(1296), CustomItemList.eM_Computer_Bus.get(1, o), 200, 122880, true);

        //Molecular Casing
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Power.get(1, o),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 6),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 12),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 24),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.TungstenSteel, 24),
                ItemList.Field_Generator_IV.get(1, o)
        }, Materials.Osmium.getMolten(1296), CustomItemList.eM_Containment.get(1, o), 800, 500000, true);

        //endregion


        //region hatches

        //Dynamo Hatches UV-UIV
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Dynamo_UV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2)}, Materials.Silver.getMolten(1000), CustomItemList.eM_dynamomulti4_UV.get(1, o), 100, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Transformer_MAX_UV.get(1, o), CustomItemList.eM_dynamomulti4_UV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4)}, Materials.Electrum.getMolten(1000), CustomItemList.eM_dynamomulti16_UV.get(1, o), 200, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.WetTransformer_UHV_UV.get(1, o), CustomItemList.eM_dynamomulti16_UV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 6)}, Materials.Tungsten.getMolten(1000), CustomItemList.eM_dynamomulti64_UV.get(1, o), 400, 122880);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Dynamo_MAX.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Superconductor, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2)}, Materials.Silver.getMolten(2000), CustomItemList.eM_dynamomulti4_UHV.get(1, o), 100, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Transformer_UEV_UHV.get(1, o), CustomItemList.eM_dynamomulti4_UHV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Superconductor, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4)}, Materials.Electrum.getMolten(2000), CustomItemList.eM_dynamomulti16_UHV.get(1, o), 200, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.WetTransformer_UEV_UHV.get(1, o), CustomItemList.eM_dynamomulti16_UHV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Superconductor, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6)}, Materials.Tungsten.getMolten(2000), CustomItemList.eM_dynamomulti64_UHV.get(1, o), 400, 500000);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Hatch_Dynamo_UEV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Bedrockium, 2)}, Materials.Silver.getMolten(4000), CustomItemList.eM_dynamomulti4_UEV.get(1, o), 100, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Transformer_UIV_UEV.get(1, o), CustomItemList.eM_dynamomulti4_UEV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Bedrockium, 4)}, Materials.Electrum.getMolten(4000), CustomItemList.eM_dynamomulti16_UEV.get(1, o), 200, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.WetTransformer_UIV_UEV.get(1, o), CustomItemList.eM_dynamomulti16_UEV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Bedrockium, 6)}, Materials.Tungsten.getMolten(4000), CustomItemList.eM_dynamomulti64_UEV.get(1, o), 400, 2000000);

        //GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Hatch_Dynamo_UIV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 2)}, Materials.Silver.getMolten(8000), CustomItemList.eM_dynamomulti4_UIV.get(1, o), 100, 8000000);
        //GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Transformer_UMV_UIV.get(1, o), CustomItemList.eM_dynamomulti4_UIV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 4)}, Materials.Electrum.getMolten(8000), CustomItemList.eM_dynamomulti16_UIV.get(1, o), 200, 8000000);
        //GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.WetTransformer_UMV_UIV.get(1, o), CustomItemList.eM_dynamomulti16_UIV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 6)}, Materials.Tungsten.getMolten(8000), CustomItemList.eM_dynamomulti64_UIV.get(1, o), 400, 8000000);

        //Energy Hatches  UV-UIV
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Energy_UV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2)}, Materials.Silver.getMolten(1000), CustomItemList.eM_energymulti4_UV.get(1, o), 100, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Transformer_MAX_UV.get(1, o), CustomItemList.eM_energymulti4_UV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4)}, Materials.Electrum.getMolten(1000), CustomItemList.eM_energymulti16_UV.get(1, o), 200, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.WetTransformer_UHV_UV.get(1, o), CustomItemList.eM_energymulti16_UV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 6)}, Materials.Tungsten.getMolten(1000), CustomItemList.eM_energymulti64_UV.get(1, o), 400, 122880);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Energy_MAX.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Superconductor, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2)}, Materials.Silver.getMolten(2000), CustomItemList.eM_energymulti4_UHV.get(1, o), 100, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Transformer_UEV_UHV.get(1, o), CustomItemList.eM_energymulti4_UHV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Superconductor, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4)}, Materials.Electrum.getMolten(2000), CustomItemList.eM_energymulti16_UHV.get(1, o), 200, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.WetTransformer_UEV_UHV.get(1, o), CustomItemList.eM_energymulti16_UHV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Superconductor, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6)}, Materials.Tungsten.getMolten(2000), CustomItemList.eM_energymulti64_UHV.get(1, o), 400, 500000);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Hatch_Energy_UEV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Bedrockium, 2)}, Materials.Silver.getMolten(4000), CustomItemList.eM_energymulti4_UEV.get(1, o), 100, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Transformer_UIV_UEV.get(1, o), CustomItemList.eM_energymulti4_UHV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Bedrockium, 4)}, Materials.Electrum.getMolten(4000), CustomItemList.eM_energymulti16_UEV.get(1, o), 200, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.WetTransformer_UIV_UEV.get(1, o), CustomItemList.eM_energymulti16_UHV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Bedrockium, 6)}, Materials.Tungsten.getMolten(4000), CustomItemList.eM_energymulti64_UEV.get(1, o), 400, 2000000);

        //GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Hatch_Energy_UIV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 2)}, Materials.Silver.getMolten(8000), CustomItemList.eM_energymulti4_UIV.get(1, o), 100, 8000000);
        //GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Transformer_UMV_UIV.get(1, o), CustomItemList.eM_energymulti4_UIV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 4)}, Materials.Electrum.getMolten(8000), CustomItemList.eM_energymulti16_UIV.get(1, o), 200, 8000000);
        //GT_Values.RA.addAssemblerRecipe(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.WetTransformer_UMV_UIV.get(1, o), CustomItemList.eM_energymulti16_UIV.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 6)}, Materials.Tungsten.getMolten(8000), CustomItemList.eM_energymulti64_UIV.get(1, o), 400, 8000000);

        //Data Input
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Computer_Casing.get(1, o),
                ItemList.Hatch_Input_Bus_LuV.get(1, o),
                ItemList.Circuit_Crystalcomputer.get(1, o),
                CustomItemList.DATApipe.get(2, o)
        }, Materials.Iridium.getMolten(1296), CustomItemList.dataIn_Hatch.get(1, o), 200, 122880, true);
        //Data Output
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Computer_Casing.get(1, o),
                ItemList.Hatch_Output_Bus_LuV.get(1, o),
                ItemList.Circuit_Crystalcomputer.get(1, o),
                CustomItemList.DATApipe.get(2, o)
        }, Materials.Iridium.getMolten(1296), CustomItemList.dataOut_Hatch.get(1, o), 200, 122880, true);

        //Rack
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Computer_Bus.get(1, o),
                ItemList.Hatch_Input_Bus_ZPM.get(1, o),
                ItemList.Circuit_Crystalcomputer.get(2, o),
                CustomItemList.DATApipe.get(4, o)
        }, Materials.Iridium.getMolten(1296), CustomItemList.rack_Hatch.get(1, o), 800, 122880, true);

        //Object Holder
        GT_Values.RA.addAssemblylineRecipe(ItemList.Hatch_Input_Bus_ZPM.get(1), 10000, new ItemStack[]{
                ItemList.Hatch_Input_Bus_ZPM.get(1),
                CustomItemList.eM_Computer_Bus.get(1, o),
                ItemList.Emitter_ZPM.get(8),
                ItemList.Robot_Arm_ZPM.get(1),
                ItemList.Electric_Motor_ZPM.get(2),
                ItemList.Circuit_Crystalmainframe.get(1),
                GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Naquadah, 2),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 16),
                CustomItemList.DATApipe.get(2, o),
        }, new FluidStack[]{
                Materials.UUMatter.getFluid(500),
                Materials.Iridium.getMolten(1000),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 1000)
        }, CustomItemList.holder_Hatch.get(1), 1200, 100000);

        //Parameterizer
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Computer_Casing.get(1, o),
                ItemList.Circuit_Masterquantumcomputer.get(1, o),
                CustomItemList.DATApipe.get(4, o),
                ItemList.Cover_Screen.get(1, o ),
                new ItemStack(Blocks.stone_button, 16),
        }, Materials.Iridium.getMolten(2592), CustomItemList.Parametrizer_Hatch.get(1, o), 800, 122880, true);
        //Uncertainty
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Computer_Casing.get(1, o),
                ItemList.Circuit_Ultimatecrystalcomputer.get(1, o),
                CustomItemList.DATApipe.get(16, o),
                ItemList.Cover_Screen.get(1, o ),
                new ItemStack(Blocks.stone_button, 16),
        }, Materials.Iridium.getMolten(2592), CustomItemList.Uncertainty_Hatch.get(1, o), 1200, 122880, true);

        //Elemental Input
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Containment.get(1, o),
                ItemList.Hatch_Input_UV.get(1, o),
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 2),
                ItemList.Sensor_UV.get(1, o)
        }, Materials.Osmiridium.getMolten(1296), CustomItemList.eM_in_UV.get(1, o), 800, 500000, true);
        //Elemental Output
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Containment.get(1, o),
                ItemList.Hatch_Output_UV.get(1, o),
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 2),
                ItemList.Emitter_UV.get(1, o)
        }, Materials.Osmiridium.getMolten(1296), CustomItemList.eM_out_UV.get(1, o), 800, 500000, true);
        //Overflow
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Containment.get(1, o),
                ItemList.Hatch_Muffler_UV.get(1, o),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 1),
                ItemList.Field_Generator_UV.get(1, o)
        }, Materials.Osmiridium.getMolten(1296), CustomItemList.eM_muffler_UV.get(1, o), 800, 500000, true);

        //endregion


        //region multiblocks

        //Active Transformer
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                com.dreammaster.gthandler.CustomItemList.WetTransformer_ZPM_LuV.get(1, o),
                com.dreammaster.gthandler.CustomItemList.HighEnergyFlowCircuit.get(1, o),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 16),
                ItemList.Circuit_Chip_UHPIC.get(2, o),
        }, Materials.TungstenSteel.getMolten(576), CustomItemList.Machine_Multi_Transformer.get(1, o), 400, 30720, true);
        //Network Switch
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.Machine_Multi_Transformer.get(1, o),
                ItemList.Circuit_Ultimatecrystalcomputer.get(1, o),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Cobalt, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 64),
                CustomItemList.DATApipe.get(4, o),
        }, Materials.Iridium.getMolten(1296), CustomItemList.Machine_Multi_Switch.get(1, o), 800, 122880, true);

        //Quantum Computer
        GT_Values.RA.addAssemblylineRecipe(ItemList.Tool_DataOrb.get(1), 20000, new ItemStack[]{
                CustomItemList.Machine_Multi_Switch.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Superconductor, 2),
                ItemList.Tool_DataOrb.get(1),
                ItemList.Cover_Screen.get(1, o),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Superconductor, 8),
                CustomItemList.DATApipe.get(8, o),
        }, new FluidStack[]{
                Materials.UUMatter.getFluid(1000),
                Materials.Iridium.getMolten(1296),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                Materials.Hydrogen.getGas(1000),
        }, CustomItemList.Machine_Multi_Computer.get(1), 12000, 100000);

        //Research Station
        GT_Values.RA.addAssemblylineRecipe(com.dreammaster.gthandler.CustomItemList.ScannerZPM.get(1), 80000, new ItemStack[]{
                CustomItemList.Machine_Multi_Switch.get(1),
                ItemList.Sensor_ZPM.get(8),
                ItemList.Circuit_Crystalmainframe.get(4),
                ItemList.Field_Generator_ZPM.get(1),
                ItemList.Electric_Motor_ZPM.get(2),
                GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Naquadah, 4),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 32),
                CustomItemList.DATApipe.get(16, o),
        }, new FluidStack[]{
                Materials.UUMatter.getFluid(1000),
                Materials.Iridium.getMolten(1296),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                Materials.Helium.getGas(1000),
        }, CustomItemList.Machine_Multi_Research.get(1), 12000, 100000);
        //endregion
    }
}
