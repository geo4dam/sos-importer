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
package org.n52.sos.importer.view;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.Step8Controller;
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * shows progress while assembling data, registering sensors
 * and inserting observations, provides a link to the log file
 *
 * @author Raimund
 */
public class Step8Panel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(Step8Panel.class);

    private JButton logFileButton;
    private JButton directImportExecuteButton;

    private JButton configFileButton;
    private JTextArea configurationFileInstructions;
    private JTextArea directImportOutputTextArea;

    private final Step8Controller controller;

    /**
     * <p>Constructor for Step8Panel.</p>
     *
     * @param s7M a {@link org.n52.sos.importer.model.Step7Model} object.
     * @param controller a {@link org.n52.sos.importer.controller.Step8Controller} object.
     */
    public Step8Panel(final Step7Model s7M, final Step8Controller controller) {
        this.controller = controller;
        final GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{450, 0};
        gridBagLayout.rowHeights = new int[]{50, 50, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        final JPanel logfilePanel = initLogFilePanel();

        final GridBagConstraints gbc_logfilePanel = new GridBagConstraints();
        gbc_logfilePanel.fill = GridBagConstraints.BOTH;
        gbc_logfilePanel.insets = new Insets(0, 0, 5, 0);
        gbc_logfilePanel.gridx = 0;
        gbc_logfilePanel.gridy = 0;
        add(logfilePanel, gbc_logfilePanel);

        final JPanel configurationFilePanel = initConfigurationFilePanel(s7M);

        final GridBagConstraints gbc_configurationFilePanel = new GridBagConstraints();
        gbc_configurationFilePanel.insets = new Insets(0, 0, 5, 0);
        gbc_configurationFilePanel.fill = GridBagConstraints.BOTH;
        gbc_configurationFilePanel.gridx = 0;
        gbc_configurationFilePanel.gridy = 1;
        add(configurationFilePanel, gbc_configurationFilePanel);

        final JPanel directImportPanel = initDirectImportPanel();

        final GridBagConstraints gbc_directImportPanel = new GridBagConstraints();
        gbc_directImportPanel.fill = GridBagConstraints.BOTH;
        gbc_directImportPanel.gridx = 0;
        gbc_directImportPanel.gridy = 2;
        add(directImportPanel, gbc_directImportPanel);
    }

    private JPanel initDirectImportPanel() {
        final JPanel directImportPanel = new JPanel();
        directImportPanel.setBorder(new TitledBorder(null,
                Lang.l().step8DirectImportStartButton(),
                TitledBorder.LEADING,
                TitledBorder.TOP,
                null,
                null));

        final GridBagLayout gbl_directImportPanel = new GridBagLayout();
        gbl_directImportPanel.columnWidths = new int[] {700, 0, 0};
        gbl_directImportPanel.rowHeights = new int[]{23, 0, 0, 0};
        gbl_directImportPanel.columnWeights = new double[]{1.0, 0.0};
        gbl_directImportPanel.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        directImportPanel.setLayout(gbl_directImportPanel);

        final JTextArea directImportInstructions = new JTextArea(Lang.l().step8DirectImportInstructions());
        directImportInstructions.setBackground(Constants.DEFAULT_COLOR_BACKGROUND);
        directImportInstructions.setFocusable(false);
        directImportInstructions.setEditable(false);
        directImportInstructions.setWrapStyleWord(true);
        directImportInstructions.setLineWrap(true);
        directImportInstructions.setFont(Constants.DEFAULT_LABEL_FONT);

        final GridBagConstraints gbc_directImportInstructions = new GridBagConstraints();
        gbc_directImportInstructions.fill = GridBagConstraints.HORIZONTAL;
        gbc_directImportInstructions.anchor = GridBagConstraints.NORTH;
        gbc_directImportInstructions.insets = new Insets(0, 0, 5, 5);
        gbc_directImportInstructions.gridx = 0;
        gbc_directImportInstructions.gridy = 0;
        directImportPanel.add(directImportInstructions, gbc_directImportInstructions);

        directImportExecuteButton = new JButton(Lang.l().step8StartImportButton());
        directImportExecuteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                controller.directImport();
            }
        });
        final GridBagConstraints gbc_directImportExecuteButton = new GridBagConstraints();
        gbc_directImportExecuteButton.anchor = GridBagConstraints.NORTHWEST;
        gbc_directImportExecuteButton.insets = new Insets(0, 0, 5, 5);
        gbc_directImportExecuteButton.gridx = 0;
        gbc_directImportExecuteButton.gridy = 1;
        directImportPanel.add(directImportExecuteButton, gbc_directImportExecuteButton);

        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        final GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 2;
        directImportPanel.add(scrollPane, gbc_scrollPane);

        directImportOutputTextArea = new JTextArea();
        directImportOutputTextArea.setEditable(false);
        directImportOutputTextArea.setFont(Constants.DEFAULT_LABEL_FONT);
        scrollPane.setViewportView(directImportOutputTextArea);

        return directImportPanel;
    }

    private JPanel initConfigurationFilePanel(final Step7Model s7M) {
        final JPanel configurationFilePanel = new JPanel();
        configurationFilePanel.setBorder(new TitledBorder(null,
                Lang.l().step7ConfigurationFile(),
                TitledBorder.LEADING,
                TitledBorder.TOP,
                null,
                null));

        final GridBagLayout gbl_configurationFilePanel = new GridBagLayout();
        gbl_configurationFilePanel.columnWidths = new int[]{450, 0, 0};
        gbl_configurationFilePanel.rowHeights = new int[]{0, 23, 0};
        gbl_configurationFilePanel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        gbl_configurationFilePanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        configurationFilePanel.setLayout(gbl_configurationFilePanel);

        configurationFileInstructions = new JTextArea(Lang.l().step8ConfigurationFileInstructions());
        configurationFileInstructions.setBackground(Constants.DEFAULT_COLOR_BACKGROUND);
        configurationFileInstructions.setFocusable(false);
        configurationFileInstructions.setEditable(false);
        configurationFileInstructions.setWrapStyleWord(true);
        configurationFileInstructions.setLineWrap(true);
        configurationFileInstructions.setFont(Constants.DEFAULT_LABEL_FONT);

        final GridBagConstraints gbc_configurationFileInstructions = new GridBagConstraints();
        gbc_configurationFileInstructions.insets = new Insets(0, 0, 5, 5);
        gbc_configurationFileInstructions.fill = GridBagConstraints.BOTH;
        gbc_configurationFileInstructions.gridx = 0;
        gbc_configurationFileInstructions.gridy = 0;
        configurationFilePanel.add(configurationFileInstructions, gbc_configurationFileInstructions);

        configFileButton = new JButton();
        configFileButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        configFileButton.setText(Lang.l().step8ConfigFileButton());
        configFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                openFile(s7M.getConfigFile());
            }

        });
        configFileButton.setEnabled(true);
        configFileButton.setVisible(true);

        final GridBagConstraints gbc_configFileButton = new GridBagConstraints();
        gbc_configFileButton.anchor = GridBagConstraints.WEST;
        gbc_configFileButton.insets = new Insets(0, 0, 0, 5);
        gbc_configFileButton.gridx = 0;
        gbc_configFileButton.gridy = 1;
        configurationFilePanel.add(configFileButton, gbc_configFileButton);

        return configurationFilePanel;
    }

    private void openFile(File f) throws HeadlessException {
        final Desktop desktop = Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.OPEN)) {
            try {
                desktop.open(f);
            } catch (final IOException ioe) {
                logger.error("Unable to open file: " + ioe.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    Lang.l().step8ErrorDesktopNotSupportedMesage(
                            f.getAbsolutePath()),
                    Lang.l().step8ErrorDesktopNotSupportedTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel initLogFilePanel() {
        final JPanel logfilePanel = new JPanel();
        logfilePanel.setBorder(new TitledBorder(null,
                Lang.l().step8LogFile(),
                TitledBorder.LEADING,
                TitledBorder.TOP,
                null,
                null));

        final GridBagLayout gbl_logfilePanel = new GridBagLayout();
        gbl_logfilePanel.columnWidths = new int[]{177, 95, 0};
        gbl_logfilePanel.rowHeights = new int[] {25, 25, 0};
        gbl_logfilePanel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        gbl_logfilePanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        logfilePanel.setLayout(gbl_logfilePanel);

        final JTextArea logfileInstructionsJT = new JTextArea(Lang.l().step8LogFileInstructions());
        logfileInstructionsJT.setFocusable(false);
        logfileInstructionsJT.setLineWrap(true);
        logfileInstructionsJT.setWrapStyleWord(true);
        logfileInstructionsJT.setEditable(false);
        logfileInstructionsJT.setBackground(Constants.DEFAULT_COLOR_BACKGROUND);
        logfileInstructionsJT.setFont(Constants.DEFAULT_LABEL_FONT);

        final GridBagConstraints gbc_logfileInstructionsJT = new GridBagConstraints();
        gbc_logfileInstructionsJT.fill = GridBagConstraints.BOTH;
        gbc_logfileInstructionsJT.insets = new Insets(0, 0, 5, 5);
        gbc_logfileInstructionsJT.gridx = 0;
        gbc_logfileInstructionsJT.gridy = 0;
        logfilePanel.add(logfileInstructionsJT, gbc_logfileInstructionsJT);

        final GridBagConstraints gbc_logFileButton = new GridBagConstraints();
        gbc_logFileButton.insets = new Insets(0, 0, 0, 5);
        gbc_logFileButton.anchor = GridBagConstraints.NORTHWEST;
        gbc_logFileButton.gridx = 0;
        gbc_logFileButton.gridy = 1;

        logFileButton = new JButton();
        logFileButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        logFileButton.setText(Lang.l().step8LogFileButton());

        logfilePanel.add(logFileButton, gbc_logFileButton);

        return logfilePanel;
    }

    /**
     * <p>setLogFileURI.</p>
     *
     * @param uri a {@link java.net.URI} object.
     */
    public void setLogFileURI(final URI uri) {
        if (logger.isTraceEnabled()) {
            logger.trace("setLogFileURI(" + uri + ")");
        }
        logFileButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                openFile(new File(uri));
            }
        });
    }

    /**
     * <p>setDirectImportExecuteButtonEnabled.</p>
     *
     * @param enabled a boolean.
     */
    public void setDirectImportExecuteButtonEnabled(final boolean enabled) {
        directImportExecuteButton.setEnabled(enabled);
    }

    /**
     * <p>Getter for the field <code>directImportOutputTextArea</code>.</p>
     *
     * @return a {@link javax.swing.JTextArea} object.
     */
    public JTextArea getDirectImportOutputTextArea() {
        return directImportOutputTextArea;
    }

}
