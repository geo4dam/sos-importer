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
package org.n52.sos.importer.feeder;


import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

import org.apache.xmlbeans.XmlException;
import org.junit.Test;
import org.n52.sos.importer.feeder.model.FeatureOfInterest;

/**
 * Test for Issue #58: Null Pointer Exception (NPE) when feeder tries to register a sensor
 *
 * https://github.com/52North/sos-importer/issues/58
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class TestIssue58NPEDuringRegisterSensor {


    @Test
    public void testGetFoiForColumn() throws XmlException, IOException, ParseException, URISyntaxException {
        // given
        final Configuration config = new Configuration("src/test/resources/issue-058/data_config.xml");
        DataFile dataFile = new DataFile(config, config.getDataFile());

        // when
        int mvColumnId = 4;
        final String sensor = "TemperaturesensorAdrian";
        double latValue = 48.14935;
        double longValue = 11.567826;
        String[] values = { sensor, sensor, "20.10.2016 11:50",
                latValue + " " + longValue, "Temperature", "CEL", "24", };
        FeatureOfInterest foi = dataFile.getFoiForColumn(mvColumnId, values);

        // then
        assertThat(foi.getPosition(), is(notNullValue()));
        assertThat(foi.getUri().toString(), is(sensor));

        assertThat(foi.getPosition().getValueByAxisAbbreviation("Lat"), is(latValue));
        assertThat(foi.getPosition().getValueByAxisAbbreviation("Long"), is(longValue));
        // the next two values are coming from the configuration
        assertThat(foi.getPosition().getValueByAxisAbbreviation("h"), is(524.0));
        assertThat(foi.getPosition().getUnitByAxisAbbreviation("h"), is("m"));
    }
}
