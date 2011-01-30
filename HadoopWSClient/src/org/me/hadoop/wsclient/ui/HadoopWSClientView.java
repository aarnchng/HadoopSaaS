/*
 * HadoopWSClientView.java
 */
package org.me.hadoop.wsclient.ui;

import java.awt.EventQueue;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jdesktop.application.Task.InputBlocker;
import org.me.hadoop.service.HadoopService;
import org.me.hadoop.service.HadoopService_Service;
import org.me.hadoop.service.TJobInput;
import org.me.hadoop.service.TJobModel;
import org.me.hadoop.service.TJobModelHeader;
import org.me.hadoop.utils.MiscUtils;
import org.me.hadoop.wsclient.ui.wizards.DataCreatorWizRes;
import org.me.hadoop.wsclient.ui.wizards.JDataCreator1Page;
import org.me.hadoop.wsclient.ui.wizards.JModelCreatorJarPage;
import org.me.hadoop.wsclient.ui.wizards.JModelCreator3Page;
import org.me.hadoop.wsclient.ui.wizards.JModelCreator1Page;
import org.me.hadoop.wsclient.ui.wizards.JModelCreatorStreamPage;
import org.me.hadoop.wsclient.ui.wizards.ModelCreatorWizRes;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardException;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPage.WizardResultProducer;

class ModelCreatorResProd implements WizardResultProducer {

    public Object finish(Map entries) throws WizardException {
        return new ModelCreatorWizRes();
    }

    public boolean cancel(Map settings) {
        return true;
    }
}

class DataCreatorResProd implements WizardResultProducer {

    public Object finish(Map entries) throws WizardException {
        return new DataCreatorWizRes();
    }

    public boolean cancel(Map settings) {
        return true;
    }
}

/**
 * The application's main frame.
 */
public class HadoopWSClientView extends FrameView {

    private static Logger logger = Logger.getLogger(HadoopWSClientView.class.getName());
    private List<TJobModelHeader> models = new ArrayList<TJobModelHeader>();
    private List<TJobInput> inputs = new ArrayList<TJobInput>();

    public HadoopWSClientView(SingleFrameApplication app) {
        super(app);

        initComponents();
        jRunJobButton.setEnabled(false);

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public Task GetModels() {
        return new GetModelsTask(getApplication());
    }

    private class GetModelsTask extends org.jdesktop.application.Task<List<TJobModelHeader>, Void> {

        GetModelsTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to GetModelsTask fields, here.
            super(app);
        }

        @Override
        protected List<TJobModelHeader> doInBackground() {
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.
            List<TJobModelHeader> models = null;
            try { // Call Web Service Operation
                HadoopService_Service service = MiscUtils.getHadoopService();
                HadoopService port = service.getHadoopServicePort();
                // TODO process result here
                models = port.listModels();
            } catch (Exception ex) {
                logger.log(Level.WARNING, null, ex);
            }
            return models;  // return your result
        }

        @Override
        protected void succeeded(List<TJobModelHeader> result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
            jModelComboBox.removeAllItems();
            if (result != null) {
                models = result;
                if (models.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No model is available!",
                            getResourceMap().getString("Application.title", ""),
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    jModelComboBox_AddItems();
                    jModelComboBox.setSelectedIndex(0);
                }
            } else {
                models = new ArrayList<TJobModelHeader>();
                JOptionPane.showMessageDialog(null, "Service is unavailable!",
                        getResourceMap().getString("Application.title", ""),
                        JOptionPane.INFORMATION_MESSAGE);
            }
            jRunJobButton_SetEnabled();
        }
    }

    private void jModelComboBox_AddItems() {
        if ((models != null) && (!models.isEmpty())) {
            for (TJobModelHeader model : models) {
                String item = model.getName();
                if (model.getVersion().trim().length() != 0) {
                    item += " " + model.getVersion();
                }
                if (model.getAuthor().trim().length() != 0) {
                    item += " BY " + model.getAuthor();
                }
                jModelComboBox.addItem(item);
            }
        }
    }

    private void jInputComboBox_AddItems() {
        if ((inputs != null) && (!inputs.isEmpty())) {
            for (TJobInput input : inputs) {
                jInputComboBox.addItem(input.getName());
            }
        }
    }

    private void jRunJobButton_SetEnabled() {
        if ((jModelComboBox.getSelectedIndex() != -1) &&
                (jInputComboBox.getSelectedIndex() != -1)) {
            jRunJobButton.setEnabled(true);
        } else {
            jRunJobButton.setEnabled(false);
        }
    }

    @Action(block = Task.BlockingScope.APPLICATION)
    public Task RunJob() {
        String modelname = null;
        String inputname = null;
        if (jModelComboBox.getSelectedIndex() != -1) {
            modelname = models.get(jModelComboBox.getSelectedIndex()).getKey();
        }
        if (jInputComboBox.getSelectedIndex() != -1) {
            inputname = inputs.get(jInputComboBox.getSelectedIndex()).getName();
        }

        RunJobTask task = new RunJobTask(getApplication(), modelname, inputname,
                jDescriptionTextField.getText(), jArgumentsTextField.getText());
        task.setInputBlocker(new RunJobInputBlocker(this, task));
        return task;
    }

    private class RunJobInputBlocker extends InputBlocker {

        private JRunJobDialog jRunJobDialog;

        RunJobInputBlocker(FrameView view, RunJobTask task) {
            super(task, Task.BlockingScope.APPLICATION, null);
            jRunJobDialog = new JRunJobDialog(view.getFrame(), true, task);
            task.setProgressableUI(jRunJobDialog);
        }

        @Override
        protected void block() {
            Runnable showDialog = new Runnable() {

                public void run() {
                    jRunJobDialog.setVisible(true);
                }
            };
            EventQueue.invokeLater(showDialog);
        }

        @Override
        protected void unblock() {
            if (jRunJobDialog.getProperty("job.forget")) {
                statusMessageLabel.setText("Job '" + jRunJobDialog.getProperty("job.name",
                        "") + "' is currently running!");
            }
            jRunJobDialog.setVisible(false);
        }
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = HadoopWSClient.getApplication().getMainFrame();
            aboutBox = new HadoopWSClientAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        HadoopWSClient.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jMainTabbedPane = new javax.swing.JTabbedPane();
        jRunJobPanel = new javax.swing.JPanel();
        jInputLabel = new javax.swing.JLabel();
        jModelComboBox = new javax.swing.JComboBox();
        jModelLabel = new javax.swing.JLabel();
        jInputComboBox = new javax.swing.JComboBox();
        jModelButton = new javax.swing.JButton();
        jRunJobButton = new javax.swing.JButton();
        jOptionalParametersPanel = new javax.swing.JPanel();
        jArgumentsLabel = new javax.swing.JLabel();
        jArgumentsTextField = new javax.swing.JTextField();
        jDescriptionLabel = new javax.swing.JLabel();
        jDescriptionTextField = new javax.swing.JTextField();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jModelCreatorJarMenuItem = new javax.swing.JMenuItem();
        jModelCreatorStreamMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jDataCreatorMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        jMainTabbedPane.setName("jMainTabbedPane"); // NOI18N

        jRunJobPanel.setName("jRunJobPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.me.hadoop.wsclient.ui.HadoopWSClient.class).getContext().getResourceMap(HadoopWSClientView.class);
        jInputLabel.setText(resourceMap.getString("jInputLabel.text")); // NOI18N
        jInputLabel.setName("jInputLabel"); // NOI18N

        jModelComboBox.setName("jModelComboBox"); // NOI18N

        jModelLabel.setText(resourceMap.getString("jModelLabel.text")); // NOI18N
        jModelLabel.setName("jModelLabel"); // NOI18N

        jInputComboBox.setName("jInputComboBox"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.me.hadoop.wsclient.ui.HadoopWSClient.class).getContext().getActionMap(HadoopWSClientView.class, this);
        jModelButton.setAction(actionMap.get("GetModels")); // NOI18N
        jModelButton.setText(resourceMap.getString("jModelButton.text")); // NOI18N
        jModelButton.setName("jModelButton"); // NOI18N

        jRunJobButton.setAction(actionMap.get("RunJob")); // NOI18N
        jRunJobButton.setText(resourceMap.getString("jRunJobButton.text")); // NOI18N
        jRunJobButton.setName("jRunJobButton"); // NOI18N

        jOptionalParametersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jOptionalParametersPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), resourceMap.getColor("jOptionalParametersPanel.border.titleColor"))); // NOI18N
        jOptionalParametersPanel.setName("jOptionalParametersPanel"); // NOI18N

        jArgumentsLabel.setText(resourceMap.getString("jArgumentsLabel.text")); // NOI18N
        jArgumentsLabel.setName("jArgumentsLabel"); // NOI18N

        jArgumentsTextField.setText(resourceMap.getString("jArgumentsTextField.text")); // NOI18N
        jArgumentsTextField.setName("jArgumentsTextField"); // NOI18N

        jDescriptionLabel.setText(resourceMap.getString("jDescriptionLabel.text")); // NOI18N
        jDescriptionLabel.setName("jDescriptionLabel"); // NOI18N

        jDescriptionTextField.setText(resourceMap.getString("jDescriptionTextField.text")); // NOI18N
        jDescriptionTextField.setName("jDescriptionTextField"); // NOI18N

        javax.swing.GroupLayout jOptionalParametersPanelLayout = new javax.swing.GroupLayout(jOptionalParametersPanel);
        jOptionalParametersPanel.setLayout(jOptionalParametersPanelLayout);
        jOptionalParametersPanelLayout.setHorizontalGroup(
            jOptionalParametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jOptionalParametersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jOptionalParametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jArgumentsLabel)
                    .addComponent(jDescriptionLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jOptionalParametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jArgumentsTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                    .addComponent(jDescriptionTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
                .addContainerGap())
        );
        jOptionalParametersPanelLayout.setVerticalGroup(
            jOptionalParametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jOptionalParametersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jOptionalParametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jArgumentsLabel)
                    .addComponent(jArgumentsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jOptionalParametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jDescriptionLabel)
                    .addComponent(jDescriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jRunJobPanelLayout = new javax.swing.GroupLayout(jRunJobPanel);
        jRunJobPanel.setLayout(jRunJobPanelLayout);
        jRunJobPanelLayout.setHorizontalGroup(
            jRunJobPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jRunJobPanelLayout.createSequentialGroup()
                .addGroup(jRunJobPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jRunJobPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jRunJobPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jModelLabel)
                            .addComponent(jInputLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jRunJobPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jInputComboBox, 0, 353, Short.MAX_VALUE)
                            .addComponent(jModelComboBox, 0, 353, Short.MAX_VALUE)))
                    .addGroup(jRunJobPanelLayout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(jModelButton))
                    .addGroup(jRunJobPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jOptionalParametersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jRunJobPanelLayout.createSequentialGroup()
                        .addContainerGap(358, Short.MAX_VALUE)
                        .addComponent(jRunJobButton)))
                .addContainerGap())
        );
        jRunJobPanelLayout.setVerticalGroup(
            jRunJobPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jRunJobPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jRunJobPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jModelLabel)
                    .addComponent(jModelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jModelButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jRunJobPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jInputLabel)
                    .addComponent(jInputComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jOptionalParametersPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jRunJobButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMainTabbedPane.addTab(resourceMap.getString("jRunJobPanel.TabConstraints.tabTitle"), jRunJobPanel); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jMainTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jMainTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jModelCreatorJarMenuItem.setText(resourceMap.getString("jModelCreatorJarMenuItem.text")); // NOI18N
        jModelCreatorJarMenuItem.setName("jModelCreatorJarMenuItem"); // NOI18N
        jModelCreatorJarMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jModelCreatorJarMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(jModelCreatorJarMenuItem);

        jModelCreatorStreamMenuItem.setText(resourceMap.getString("jModelCreatorStreamMenuItem.text")); // NOI18N
        jModelCreatorStreamMenuItem.setName("jModelCreatorStreamMenuItem"); // NOI18N
        jModelCreatorStreamMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jModelCreatorStreamMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(jModelCreatorStreamMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        jDataCreatorMenuItem.setText(resourceMap.getString("jDataCreatorMenuItem.text")); // NOI18N
        jDataCreatorMenuItem.setName("jDataCreatorMenuItem"); // NOI18N
        jDataCreatorMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDataCreatorMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(jDataCreatorMenuItem);

        jSeparator2.setName("jSeparator2"); // NOI18N
        fileMenu.add(jSeparator2);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 294, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void jModelCreatorJarMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jModelCreatorJarMenuItemActionPerformed
        // TODO add your handling code here:
        Wizard wiz = WizardPage.createWizard("Creating Jar Model...", new Class[]{
                    JModelCreator1Page.class, JModelCreatorJarPage.class, JModelCreator3Page.class},
                new ModelCreatorResProd());
        TJobModel model = (TJobModel) WizardDisplayer.showWizard(wiz);
        if (model != null) {
            TJobModelHeader header = new TJobModelHeader();
            header.setKey(model.getKey());
            header.setName(model.getName());
            header.setVersion(model.getVersion());
            header.setAuthor(model.getAuthor());
            models.add(0, header);

            jModelComboBox.removeAllItems();
            jModelComboBox_AddItems();
            jModelComboBox.setSelectedIndex(0);
            statusMessageLabel.setText("Model '" + model.getName() + "' has been added!");
        }
        jRunJobButton_SetEnabled();
}//GEN-LAST:event_jModelCreatorJarMenuItemActionPerformed

    private void jModelCreatorStreamMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jModelCreatorStreamMenuItemActionPerformed
        // TODO add your handling code here:
        Wizard wiz = WizardPage.createWizard("Creating Streaming Model...", new Class[]{
                    JModelCreator1Page.class, JModelCreatorStreamPage.class, JModelCreator3Page.class},
                new ModelCreatorResProd());
        TJobModel model = (TJobModel) WizardDisplayer.showWizard(wiz);
        if (model != null) {
            TJobModelHeader header = new TJobModelHeader();
            header.setKey(model.getKey());
            header.setName(model.getName());
            header.setVersion(model.getVersion());
            header.setAuthor(model.getAuthor());
            models.add(0, header);

            jModelComboBox.removeAllItems();
            jModelComboBox_AddItems();
            jModelComboBox.setSelectedIndex(0);
            statusMessageLabel.setText("Model '" + model.getName() + "' has been added!");
        }
        jRunJobButton_SetEnabled();
}//GEN-LAST:event_jModelCreatorStreamMenuItemActionPerformed

    private void jDataCreatorMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDataCreatorMenuItemActionPerformed
        // TODO add your handling code here:
        Wizard wiz = WizardPage.createWizard("Creating Input Data...", new Class[]{
                    JDataCreator1Page.class}, new DataCreatorResProd());
        TJobInput input = (TJobInput) WizardDisplayer.showWizard(wiz);
        if (input != null) {
            inputs.add(0, input);

            jInputComboBox.removeAllItems();
            jInputComboBox_AddItems();
            jInputComboBox.setSelectedIndex(0);
            statusMessageLabel.setText("'" + input.getName() + "' has been added!");
        }
        jRunJobButton_SetEnabled();
}//GEN-LAST:event_jDataCreatorMenuItemActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jArgumentsLabel;
    private javax.swing.JTextField jArgumentsTextField;
    private javax.swing.JMenuItem jDataCreatorMenuItem;
    private javax.swing.JLabel jDescriptionLabel;
    private javax.swing.JTextField jDescriptionTextField;
    private javax.swing.JComboBox jInputComboBox;
    private javax.swing.JLabel jInputLabel;
    private javax.swing.JTabbedPane jMainTabbedPane;
    private javax.swing.JButton jModelButton;
    private javax.swing.JComboBox jModelComboBox;
    private javax.swing.JMenuItem jModelCreatorJarMenuItem;
    private javax.swing.JMenuItem jModelCreatorStreamMenuItem;
    private javax.swing.JLabel jModelLabel;
    private javax.swing.JPanel jOptionalParametersPanel;
    private javax.swing.JButton jRunJobButton;
    private javax.swing.JPanel jRunJobPanel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
}
