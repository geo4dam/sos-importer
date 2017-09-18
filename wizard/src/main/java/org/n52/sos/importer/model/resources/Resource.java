/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.importer.model.resources;

import java.net.URI;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.n52.sos.importer.model.Combination;
import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In this project, a resource has a URI and a name. This can be
 * a feature of interest, observed property, unit of measurement or
 * a sensor.<br>
 * Since version 2, a resource can be generated by other TableElements
 * including a potential prefix and string used between the TableElements values.
 *
 * @author Raimund
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version 2
 */
public abstract class Resource extends Component {

    private static final Logger LOG = LoggerFactory.getLogger(Resource.class);

    private TableElement tableElement;
    private String name;
    private URI uri;
    private String uriPrefix;
    private TableElement[] relatedCols;
    private String concatString;
    private boolean useNameAfterPrefixAsURI;
    private boolean generated;

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>setURI.</p>
     *
     * @param newUri a {@link java.net.URI} object.
     */
    public void setURI(final URI newUri) {
        uri = newUri;
    }

    /**
     * <p>getURI.</p>
     *
     * @return a {@link java.net.URI} object.
     */
    public URI getURI() {
        return uri;
    }

    /**
     * XML prefix for resources.
     *
     * @return should be xml:ID valid
     */
    public abstract String XML_PREFIX();

    /**
     * <p>getTypeName.</p>
     *
     * @return A String representing this type
     */
    public abstract String getTypeName();

    /**
     * Returns the name or alternatively the URI, when
     * the name is null
     *
     * @return a String representation of this resource's name
     */
    public String getNameString() {
        if (getName() == null || getName().equals("")) {
            return uri.toString();
        } else {
            return getName();
        }
    }

    /**
     * Returns the URI or alternatively the name, when
     * the URI is null
     *
     * @return a String representation of this resource's URI
     */
    public String getURIString() {
        if (uri == null || uri.toString().equals("")) {
            return name;
        } else {
            return uri.toString();
        }
    }

    /**
     * <p>Setter for the field <code>tableElement</code>.</p>
     *
     * @param tableElement a {@link org.n52.sos.importer.model.table.TableElement} object.
     */
    public void setTableElement(final TableElement tableElement) {
        LOG.info("In " + tableElement + " are " + this + "s");
        this.tableElement = tableElement;
    }

    /**
     * <p>Getter for the field <code>tableElement</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.table.TableElement} object.
     */
    public TableElement getTableElement() {
        return tableElement;
    }

    /**
     * assign this resource to a measured value column or row
     *
     * @param mv a {@link org.n52.sos.importer.model.measuredValue.MeasuredValue} object.
     */
    public abstract void assign(MeasuredValue mv);

    /**
     * indicates if the measured value is assigned to a resource of this type
     *
     * @param mv a {@link org.n52.sos.importer.model.measuredValue.MeasuredValue} object.
     * @return a boolean.
     */
    public abstract boolean isAssigned(MeasuredValue mv);

    /**
     * indicates if the measured value is assigned to this resource
     *
     * @param mv a {@link org.n52.sos.importer.model.measuredValue.MeasuredValue} object.
     * @return a boolean.
     */
    public abstract boolean isAssignedTo(MeasuredValue mv);

    /**
     * unassign this resource from a measured value column or row
     *
     * @param mv a {@link org.n52.sos.importer.model.measuredValue.MeasuredValue} object.
     */
    public abstract void unassign(MeasuredValue mv);

    /**
     * get names of the resource stored in the properties file
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public abstract DefaultComboBoxModel<String> getNames();

    /**
     * get URIs of the resource stored in the properties file
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public abstract DefaultComboBoxModel<String> getURIs();

    /**
     * get all resources of this type in the model store
     *
     * @return a {@link java.util.List} object.
     */
    public abstract List<Resource> getList();

    /**
     * needed for iterating within one step
     *
     * @return a {@link org.n52.sos.importer.model.resources.Resource} object.
     */
    public abstract Resource getNextResourceType();

    @Override
    public MissingComponentPanel getMissingComponentPanel(final Combination c) {
        //not used since all resources have the same panel
        return null;
    }

    /**
     * returns the corresponding resource for a measured value cell
     *
     * @param measuredValuePosition a {@link org.n52.sos.importer.model.table.Cell} object.
     * @return a {@link org.n52.sos.importer.model.resources.Resource} object.
     */
    public abstract Resource forThis(Cell measuredValuePosition);

    public String getXMLId() {
        return XML_PREFIX() + hashCode();
    }

    @Override
    public String toString() {
        if (getTableElement() != null) {
            return " " + getTableElement();
        }
        if (getName() != null) {
            return " \"" + getName() + "\"";
        }
        return "";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result
                + (tableElement == null ? 0 : tableElement.hashCode());
        result = prime * result + (uri == null ? 0 : uri.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Resource)) {
            return false;
        }
        final Resource other = (Resource) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (tableElement == null) {
            if (other.tableElement != null) {
                return false;
            }
        } else if (!tableElement.equals(other.tableElement)) {
            return false;
        }
        if (uri == null) {
            if (other.uri != null) {
                return false;
            }
        } else if (!uri.equals(other.uri)) {
            return false;
        }
        return true;
    }

    /**
     * <p>Getter for the field <code>uriPrefix</code>.</p>
     *
     * @return the uriPrefix
     */
    public String getUriPrefix() {
        return uriPrefix;
    }

    /**
     * <p>Setter for the field <code>uriPrefix</code>.</p>
     *
     * @param uriPrefix the uriPrefix to set
     */
    public void setUriPrefix(final String uriPrefix) {
        this.uriPrefix = uriPrefix;
    }

    /**
     * <p>Getter for the field <code>relatedCols</code>.</p>
     *
     * @return the relatedCols
     */
    public TableElement[] getRelatedCols() {
        return relatedCols.clone();
    }

    /**
     * <p>Setter for the field <code>relatedCols</code>.</p>
     *
     * @param relatedCols the relatedCols to set
     */
    public void setRelatedCols(final TableElement[] relatedCols) {
        this.relatedCols = relatedCols == null ? new TableElement[0] : relatedCols.clone();
    }

    /**
     * <p>Getter for the field <code>concatString</code>.</p>
     *
     * @return the concatString
     */
    public String getConcatString() {
        return concatString;
    }

    /**
     * <p>Setter for the field <code>concatString</code>.</p>
     *
     * @param concatString the concatString to set
     */
    public void setConcatString(final String concatString) {
        this.concatString = concatString;
    }

    /**
     * <p>isUseNameAfterPrefixAsURI.</p>
     *
     * @return the useNameAfterPrefixAsURI
     */
    public boolean isUseNameAfterPrefixAsURI() {
        return useNameAfterPrefixAsURI;
    }

    /**
     * <p>Setter for the field <code>useNameAfterPrefixAsURI</code>.</p>
     *
     * @param useNameAfterPrefixAsURI the useNameAfterPrefixAsURI to set
     */
    public void setUseNameAfterPrefixAsURI(final boolean useNameAfterPrefixAsURI) {
        this.useNameAfterPrefixAsURI = useNameAfterPrefixAsURI;
    }

    /**
     * If <code>true</code> this Resource is generated from other elements
     * contained in the data file.
     *
     * @return the generated
     */
    public boolean isGenerated() {
        return generated;
    }

    /**
     * <p>Setter for the field <code>generated</code>.</p>
     *
     * @param generated the generated to set
     */
    public void setGenerated(final boolean generated) {
        this.generated = generated;
    }
}
