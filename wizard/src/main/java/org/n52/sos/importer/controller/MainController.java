/*
 * Copyright (C) 2011-2018 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.importer.controller;

import java.io.File;
import java.io.IOException;

import org.n52.sos.importer.model.BackNextModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.xml.Model;
import org.n52.sos.importer.view.DescriptionPanel;
import org.n52.sos.importer.view.MainFrame;
import org.n52.sos.importer.view.combobox.ComboBoxItems;
import org.n52.sos.importer.wizard.utils.ToolTips;

/**
 * controls the main frame of the application,
 * changes step panels and exits the application
 *
 * @author Raimund
 */
public final class MainController {

    private static MainController instance;

    private final MainFrame mainFrame = new MainFrame(this);

    private final Model xmlModel;

    private MainController() {
        //
        // Load the tooltips
        ToolTips.loadSettings();
        //
        // load the configuration for the ComboBoxItems at startup
        // first call to getInstance() calls ComboBoxItems.load()
        ComboBoxItems.getInstance();
        //
        // init xmlmodel TODO load from configuration
        xmlModel = new Model();
    }

    /**
     * <p>Getter for the field <code>instance</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.controller.MainController} object.
     */
    public static MainController getInstance() {
        if (MainController.instance == null) {
            MainController.instance = new MainController();
        }
        return MainController.instance;
    }

    /**
     * Method is called each time a button ("Next","Back", or "Finish") is clicked
     * in the GUI.
     *
     * @param stepController a {@link org.n52.sos.importer.controller.StepController} object.
     */
    public void setStepController(final StepController stepController) {
        final DescriptionPanel descP = DescriptionPanel.getInstance();
        final BackNextModel bNM = BackNextController.getInstance().getModel();
        //
        descP.setText(stepController.getDescription());
        stepController.loadSettings();
        mainFrame.setStepPanel(stepController.getStepPanel());
        xmlModel.registerProvider(stepController.getModel());
        bNM.setCurrentStepController(stepController);
    }

    /**
     * <p>updateModel.</p>
     */
    public void updateModel() {
        xmlModel.updateModel();
    }

    /**
     * <p>removeProvider.</p>
     *
     * @param sm a {@link org.n52.sos.importer.model.StepModel} object.
     * @return a boolean.
     */
    public boolean removeProvider(final StepModel sm) {
        return xmlModel.removeProvider(sm);
    }

    /**
     * <p>registerProvider.</p>
     *
     * @param sm a {@link org.n52.sos.importer.model.StepModel} object.
     * @return a boolean.
     */
    public boolean registerProvider(final StepModel sm) {
        return xmlModel.registerProvider(sm);
    }

    /**
     * <p>exit.</p>
     */
    public void exit() {
        mainFrame.showExitDialog();
    }

    /**
     * <p>updateTitle.</p>
     *
     * @param csvFilePath a {@link java.lang.String} object.
     */
    public void updateTitle(final String csvFilePath) {
        mainFrame.updateTitle(csvFilePath);
    }

    /**
     * <p>saveModel.</p>
     *
     * @param file a {@link java.io.File} object.
     * @return a boolean.
     * @throws java.io.IOException if any.
     */
    public boolean saveModel(final File file) throws IOException {
        return xmlModel.save(file);
    }

    /**
     * <p>getFilename.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFilename() {
        return xmlModel.getFileName();
    }
}
