package com.github.technus.tectech.thing.metaTileEntity.multi.em_machine;

import com.github.technus.tectech.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.MultiblockControl;

/**
 * Created by danie_000 on 24.12.2017.
 */
public class Behaviour_PrecisionLaser implements GT_MetaTileEntity_EM_machine.Behaviour {
    final int tier;
    public Behaviour_PrecisionLaser(int tier){
        this.tier=tier;
    }

    @Override
    public boolean setAndCheckParametersOutAndStatuses(GT_MetaTileEntity_EM_machine te, double[] parametersToCheckAndFix) {
        return false;
    }

    @Override
    public MultiblockControl<cElementalInstanceStackMap[]> process(cElementalInstanceStackMap[] inputs, double[] checkedAndFixedParameters) {
        return null;
    }
}
