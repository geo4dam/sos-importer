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
package org.n52.sos.importer.test;

import org.n52.sos.importer.controller.DateAndTimeController;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step5aController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.table.Column;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @since 0.5.0
 */
public class Step5aTest {

    //CHECKSTYLE:OFF
    public static void main(final String[] args) {
        //CHECKSTYLE:ON
        final MainController f = MainController.getInstance();
        TableController.getInstance().setContent(TestData.EXAMPLE_TABLE_MINI_ONLY_STRINGS);
        final int firstLineWithData = 0;

        final DateAndTime dtm1 = new DateAndTime();
        DateAndTimeController dtc = new DateAndTimeController(dtm1);
        dtc.assignPattern("HH-mm-ss", new Column(0, firstLineWithData));
        ModelStore.getInstance().add(dtm1);

        final DateAndTime dtm2 = new DateAndTime();
        dtc = new DateAndTimeController(dtm2);
        dtc.assignPattern("dd-MM-yyyy", new Column(1, firstLineWithData));
        ModelStore.getInstance().add(dtm2);

        final Step5aController controller = new Step5aController(firstLineWithData);
        controller.isNecessary();
        f.setStepController(controller);
    }
}
