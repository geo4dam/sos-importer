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
package org.n52.sos.importer.view.step3;

import javax.swing.JPanel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.table.TableElement;

/**
 * assigns or unassigns columns to Feature of Interests, Observed
 * Properties, Units of Measurement and Sensors
 *
 * @author Raimund
 */
public class ResourceSelectionPanel extends SelectionPanel {

    private static final long serialVersionUID = 1L;

    private Resource resource;

    /**
     * <p>Constructor for ResourceSelectionPanel.</p>
     *
     * @param containerPanel a {@link javax.swing.JPanel} object.
     * @param resource a {@link org.n52.sos.importer.model.resources.Resource} object.
     */
    public ResourceSelectionPanel(JPanel containerPanel, Resource resource) {
        super(containerPanel);
        this.resource = resource;
    }

    @Override
    protected void setSelection(String s) {
    }

    @Override
    protected String getSelection() {
        return "0";
    }

    protected Resource getResource() {
        return resource;
    }

    @Override
    public void setDefaultSelection() {
    }

    @Override
    public void assign(TableElement tableElement) {
        resource.setTableElement(tableElement);
        ModelStore.getInstance().add(resource);
    }

    @Override
    public void unAssign(TableElement tableElement) {
        Resource resourceToRemove = null;
        for (Resource r: resource.getList()) {
            if (tableElement.equals(r.getTableElement())) {
                resourceToRemove = r;
                break;
            }
        }
        ModelStore.getInstance().remove(resourceToRemove);
    }
}
