package com.github.technus.tectech.loader;

import com.github.technus.tectech.thing.metaTileEntity.hatch.*;
import com.github.technus.tectech.thing.metaTileEntity.multi.*;
import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_Data;
import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_EM;

import static com.github.technus.tectech.thing.CustomItemList.*;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class GT_Loader_Machines implements Runnable {
    public void run() {
        // ===================================================================================================
        // eM IN
        // ===================================================================================================

        eM_in_UV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                12070, "hatch.emin.tier.08", "UV Elemental Input Hatch", 8).getStackForm(1L));

        eM_in_UHV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                12071, "hatch.emin.tier.09", "UHV Elemental Input Hatch", 9).getStackForm(1L));

        eM_in_UEV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                12072, "hatch.emin.tier.10", "UEV Elemental Input Hatch", 10).getStackForm(1L));

        eM_in_UIV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                12073, "hatch.emin.tier.11", "UIV Elemental Input Hatch", 11).getStackForm(1L));

        eM_in_UMV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                12074, "hatch.emin.tier.12", "UMV Elemental Input Hatch", 12).getStackForm(1L));

        eM_in_UXV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                12075, "hatch.emin.tier.13", "UXV Elemental Input Hatch", 13).getStackForm(1L));

        // ===================================================================================================
        // eM OUT
        // ===================================================================================================

        eM_out_UV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                12080, "hatch.emout.tier.08", "UV Elemental Output Hatch", 8).getStackForm(1L));

        eM_out_UHV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                12081, "hatch.emout.tier.09", "UHV Elemental Output Hatch", 9).getStackForm(1L));

        eM_out_UEV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                12082, "hatch.emout.tier.10", "UEV Elemental Output Hatch", 10).getStackForm(1L));

        eM_out_UIV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                12083, "hatch.emout.tier.11", "UIV Elemental Output Hatch", 11).getStackForm(1L));

        eM_out_UMV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                12084, "hatch.emout.tier.12", "UMV Elemental Output Hatch", 12).getStackForm(1L));

        eM_out_UXV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                12085, "hatch.emout.tier.13", "UXV Elemental Output Hatch", 13).getStackForm(1L));

        // ===================================================================================================
        // eM Waste OUT
        // ===================================================================================================

       eM_muffler_UV.set(new GT_MetaTileEntity_Hatch_MufflerElemental(
                12090, "hatch.emmuffler.tier.08", "UV Overflow Output Hatch", 8, 1e10f).getStackForm(1L));

        eM_muffler_UHV.set(new GT_MetaTileEntity_Hatch_MufflerElemental(
                12091, "hatch.emmuffler.tier.09", "UHV Overflow Output Hatch", 9, 5e10f).getStackForm(1L));

        eM_muffler_UEV.set(new GT_MetaTileEntity_Hatch_MufflerElemental(
                12092, "hatch.emmuffler.tier.10", "UEV Overflow Output Hatch", 10, 25e10f).getStackForm(1L));

        eM_muffler_UIV.set(new GT_MetaTileEntity_Hatch_MufflerElemental(
                12093, "hatch.emmuffler.tier.11", "UIV Overflow Output Hatch", 11, 125e10f).getStackForm(1L));

        eM_muffler_UMV.set(new GT_MetaTileEntity_Hatch_MufflerElemental(
                12094, "hatch.emmuffler.tier.12", "UMV Overflow Output Hatch", 12, 125e11f).getStackForm(1L));

        eM_muffler_UXV.set(new GT_MetaTileEntity_Hatch_MufflerElemental(
                12095, "hatch.emmuffler.tier.13", "UXV Overflow Output Hatch", 13, 125e12f).getStackForm(1L));

        // ===================================================================================================
        // Multi AMP Power INPUTS
        // ===================================================================================================

        eM_energymulti4_UV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12100, "hatch.energymulti04.tier.08", "UV 4A Energy Hatch", 8, 4).getStackForm(1L));
        eM_energymulti16_UV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12110, "hatch.energymulti16.tier.08", "UV 16A Energy Hatch", 8, 16).getStackForm(1L));
        eM_energymulti64_UV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12120, "hatch.energymulti64.tier.08", "UV 64A Energy Hatch", 8, 64).getStackForm(1L));

        eM_energymulti4_UHV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12101, "hatch.energymulti04.tier.09", "UHV 4A Energy Hatch", 9, 4).getStackForm(1L));
        eM_energymulti16_UHV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12111, "hatch.energymulti16.tier.09", "UHV 16A Energy Hatch", 9, 16).getStackForm(1L));
        eM_energymulti64_UHV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12121, "hatch.energymulti64.tier.09", "UHV 64A Energy Hatch", 9, 64).getStackForm(1L));

        eM_energymulti4_UEV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12102, "hatch.energymulti04.tier.10", "UEV 4A Energy Hatch", 10, 4).getStackForm(1L));
        eM_energymulti16_UEV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12112, "hatch.energymulti16.tier.10", "UEV 16A Energy Hatch", 10, 16).getStackForm(1L));
        eM_energymulti64_UEV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12122, "hatch.energymulti64.tier.10", "UEV 64A Energy Hatch", 10, 64).getStackForm(1L));

        eM_energymulti4_UIV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12103, "hatch.energymulti04.tier.11", "UIV 4A Energy Hatch", 11, 4).getStackForm(1L));
        eM_energymulti16_UIV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12113, "hatch.energymulti16.tier.11", "UIV 16A Energy Hatch", 11, 16).getStackForm(1L));
        eM_energymulti64_UIV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12123, "hatch.energymulti64.tier.11", "UIV 64A Energy Hatch", 11, 64).getStackForm(1L));

        eM_energymulti4_UMV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12104, "hatch.energymulti04.tier.12", "UMV 4A Energy Hatch", 12, 4).getStackForm(1L));
        eM_energymulti16_UMV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12114, "hatch.energymulti16.tier.12", "UMV 16A Energy Hatch", 12, 16).getStackForm(1L));
        eM_energymulti64_UMV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12124, "hatch.energymulti64.tier.12", "UMV 64A Energy Hatch", 12, 64).getStackForm(1L));

        eM_energymulti4_UXV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12105, "hatch.energymulti04.tier.13", "UXV 4A Energy Hatch", 13, 4).getStackForm(1L));
        eM_energymulti16_UXV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12115, "hatch.energymulti16.tier.13", "UXV 16A Energy Hatch", 13, 16).getStackForm(1L));
        eM_energymulti64_UXV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12125, "hatch.energymulti64.tier.13", "UXV 64A Energy Hatch", 13, 64).getStackForm(1L));


        // ===================================================================================================
        // Multi AMP Power OUTPUTS
        // ===================================================================================================

        eM_dynamomulti4_UV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12130, "hatch.dynamomulti04.tier.08", "UV 4A Dynamo Hatch", 8, 4).getStackForm(1L));
        eM_dynamomulti16_UV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12140, "hatch.dynamomulti16.tier.08", "UV 16A Dynamo Hatch", 8, 16).getStackForm(1L));
        eM_dynamomulti64_UV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12150, "hatch.dynamomulti64.tier.08", "UV 64A Dynamo Hatch", 8, 64).getStackForm(1L));

        eM_dynamomulti4_UHV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12131, "hatch.dynamomulti04.tier.09", "UHV 4A Dynamo Hatch", 9, 4).getStackForm(1L));
        eM_dynamomulti16_UHV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12141, "hatch.dynamomulti16.tier.09", "UHV 16A Dynamo Hatch", 9, 16).getStackForm(1L));
        eM_dynamomulti64_UHV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12151, "hatch.dynamomulti64.tier.09", "UHV 64A Dynamo Hatch", 9, 64).getStackForm(1L));

        eM_dynamomulti4_UEV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12132, "hatch.dynamomulti04.tier.10", "UEV 4A Dynamo Hatch", 10, 4).getStackForm(1L));
        eM_dynamomulti16_UEV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12142, "hatch.dynamomulti16.tier.10", "UEV 16A Dynamo Hatch", 10, 16).getStackForm(1L));
        eM_dynamomulti64_UEV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12152, "hatch.dynamomulti64.tier.10", "UEV 64A Dynamo Hatch", 10, 64).getStackForm(1L));

        eM_dynamomulti4_UIV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12133, "hatch.dynamomulti04.tier.11", "UIV 4A Dynamo Hatch", 11, 4).getStackForm(1L));
        eM_dynamomulti16_UIV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12143, "hatch.dynamomulti16.tier.11", "UIV 16A Dynamo Hatch", 11, 16).getStackForm(1L));
        eM_dynamomulti64_UIV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12153, "hatch.dynamomulti64.tier.11", "UIV 64A Dynamo Hatch", 11, 64).getStackForm(1L));

        eM_dynamomulti4_UMV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12134, "hatch.dynamomulti04.tier.12", "UMV 4A Dynamo Hatch", 12, 4).getStackForm(1L));
        eM_dynamomulti16_UMV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12144, "hatch.dynamomulti16.tier.12", "UMV 16A Dynamo Hatch", 12, 16).getStackForm(1L));
        eM_dynamomulti64_UMV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12154, "hatch.dynamomulti64.tier.12", "UMV 64A Dynamo Hatch", 12, 64).getStackForm(1L));

        eM_dynamomulti4_UXV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12135, "hatch.dynamomulti04.tier.13", "UXV 4A Dynamo Hatch", 13, 4).getStackForm(1L));
        eM_dynamomulti16_UXV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12145, "hatch.dynamomulti16.tier.13", "UXV 16A Dynamo Hatch", 13, 16).getStackForm(1L));
        eM_dynamomulti64_UXV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12155, "hatch.dynamomulti64.tier.13", "UXV 64A Dynamo Hatch", 13, 64).getStackForm(1L));

        // ===================================================================================================
        // MULTIBLOCKS EM
        // ===================================================================================================

        Machine_Multi_Transformer.set(new GT_MetaTileEntity_EM_transformer(12160, "multimachine.em.transformer", "Active Transformer").getStackForm(1L));
        Machine_Multi_Infuser.set(new GT_MetaTileEntity_EM_infuser(12161,"multimachine.em.infuser","Energy Infuser").getStackForm(1));
        Machine_Multi_MatterToEM.set(new GT_MetaTileEntity_EM_quantizer(12162, "multimachine.em.mattertoem", "Matter Quantizer").getStackForm(1L));
        Machine_Multi_EMToMatter.set(new GT_MetaTileEntity_EM_dequantizer(12163, "multimachine.em.emtomatter", "Matter Dequantizer").getStackForm(1L));
        Machine_Multi_EMjunction.set(new GT_MetaTileEntity_EM_junction(12164, "multimachine.em.junction", "Matter Junction").getStackForm(1L));
        Machine_Multi_EMmachine.set(new GT_MetaTileEntity_EM_machine(12165, "multimachine.em.processing", "Quantum Processing Machine").getStackForm(1L));
        Machine_Multi_EMCrafter.set(new GT_MetaTileEntity_EM_crafter(12166, "multimachine.em.crafter", "Matter Assembler").getStackForm(1L));
        Machine_Multi_Collider.set(new GT_MetaTileEntity_EM_collider(12167, "multimachine.em.collider", "Matter Collider").getStackForm(1L));
        Machine_Multi_BHG.set(new GT_MetaTileEntity_EM_bhg(12168, "multimachine.em.blackholegenerator", "Black Hole Generator").getStackForm(1L));
        Machine_Multi_Wormhole.set(new GT_MetaTileEntity_EM_wormhole(12169, "multimachine.em.wormhole", "Wormhole").getStackForm(1L));
        Machine_Multi_Stabilizer.set(new GT_MetaTileEntity_EM_stabilizer(12170, "multimachine.em.stabilizer", "Elemental Stabilizer").getStackForm(1L));
        Machine_Multi_Scanner.set(new GT_MetaTileEntity_EM_scanner(12171, "multimachine.em.scanner", "Elemental Scanner").getStackForm(1L));
        Machine_Multi_Computer.set(new GT_MetaTileEntity_EM_computer(12172, "multimachine.em.computer", "Quantum Computer").getStackForm(1L));
        Machine_Multi_Switch.set(new GT_MetaTileEntity_EM_switch(12173, "multimachine.em.switch", "Network Switch With QoS").getStackForm(1L));

        // ===================================================================================================
        // Hatches EM
        // ===================================================================================================
        Parametrizer_Hatch.set(new GT_MetaTileEntity_Hatch_Param(12180, "hatch.param.tier.08", "Parametrizer", 8).getStackForm(1L));
        Uncertainty_Hatch.set(new GT_MetaTileEntity_Hatch_Uncertainty(12181, "hatch.certain.tier.08", "Uncertainty Resolver", 10).getStackForm(1L));
        UncertaintyX_Hatch.set(new GT_MetaTileEntity_Hatch_Uncertainty(12182, "hatch.certain.tier.10", "Uncertainty Resolver X", 10).getStackForm(1L));
        dataIn_Hatch.set(new GT_MetaTileEntity_Hatch_InputData(12183, "hatch.datain.tier.08", "Optical Slave Connector", 8).getStackForm(1L));
        dataOut_Hatch.set(new GT_MetaTileEntity_Hatch_OutputData(12184, "hatch.dataout.tier.08", "Optical Master Connector", 8).getStackForm(1L));

        // ===================================================================================================
        // EM pipe
        // ===================================================================================================
        EMpipe.set(new GT_MetaTileEntity_Pipe_EM(12200, "pipe.elementalmatter", "Quantum Tunnel").getStackForm(1L));
        DATApipe.set(new GT_MetaTileEntity_Pipe_Data(12201, "pipe.datastream", "Optical Fiber Cable").getStackForm(1L));
    }
}
