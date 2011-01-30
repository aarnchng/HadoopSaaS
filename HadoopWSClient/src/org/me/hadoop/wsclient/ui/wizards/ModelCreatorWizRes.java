/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.wsclient.ui.wizards;

import java.util.Map;
import org.me.hadoop.wsclient.ModelCreator;
import org.netbeans.spi.wizard.DeferredWizardResult;
import org.netbeans.spi.wizard.ResultProgressHandle;

/**
 *
 * @author Aaron
 */
public class ModelCreatorWizRes extends DeferredWizardResult {

    public ModelCreatorWizRes() {
        super(false);
    }

    public void start(Map entries, ResultProgressHandle progress) {
        ModelCreator.createModel(entries, progress);
    }
}
