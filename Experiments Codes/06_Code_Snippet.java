/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *     Jens Reimann <jreimann@redhat.com> - Provide internal error message
 *******************************************************************************/
package org.eclipse.kura.web.shared;

import java.util.MissingResourceException;

/**
 * Superclass for GWT errors
 * <p>
 * The GwtKuraException class is the superclass of all GWT errors and exceptions
 * in the Kura project. It extends the JDK Exception class by requesting its
 * invokers to provide an error code when building its instances. The code is
 * one value of KuraErrorCode enum; the code is used to document the possible
 * error conditions generated by the platform as well as to identify the
 * localized exception messages to be reported. Exceptions messages are stored
 * in the KuraExceptionMessagesBundle Properties Bundle and they are keyed on
 * the exception code.
 * </p>
 *
 * @author mcarrer
 *
 */
public class GwtKuraException extends Exception {

    private static final long serialVersionUID = 1L;

    protected GwtKuraErrorCode m_errorCode;
    protected String[] m_arguments;

    @SuppressWarnings("unused")
    private GwtKuraException() {
        super();
    }

    public GwtKuraException(String message) {
        super(message);
    }

    @SuppressWarnings("unused")
    private GwtKuraException(String message, Throwable cause) {
        super(message, cause);
    }

    @SuppressWarnings("unused")
    private GwtKuraException(Throwable t) {
        super(t);
    }

    /**
     * Builds a new GwtKuraException instance based on the supplied
     * GwtKuraErrorCode, an optional Throwable cause.
     *
     * @param code
     * @param t
     * @param arguments
     */
    public GwtKuraException(GwtKuraErrorCode errorCode) {
        this.m_errorCode = errorCode;
    }

    /**
     * Builds a new GwtKuraException instance based on the supplied
     * GwtKuraErrorCode, an optional Throwable cause.
     *
     * @param code
     * @param t
     * @param arguments
     */
    public GwtKuraException(GwtKuraErrorCode errorCode, Throwable cause) {
        super(cause);
        this.m_errorCode = errorCode;
    }

    public GwtKuraException(GwtKuraErrorCode errorCode, Throwable cause, String... arguments) {
        super(cause);
        this.m_errorCode = errorCode;
        this.m_arguments = arguments;
    }

    /**
     * Factory method to build an GwtKuraException with the
     * GwtKuraErrorCode.INTERNAL_ERROR code providing a cause and a message.
     *
     * @param cause
     * @param message
     * @return
     */
    public static GwtKuraException internalError(Throwable cause, String message) {
        return new GwtKuraException(GwtKuraErrorCode.INTERNAL_ERROR, cause, message);
    }

    /**
     * Factory method to build an GwtKuraException with the
     * GwtKuraErrorCode.INTERNAL_ERROR code providing only a message.
     *
     * @param cause
     * @param message
     * @return
     */
    public static GwtKuraException internalError(String message) {
        return new GwtKuraException(GwtKuraErrorCode.INTERNAL_ERROR, null);
    }

    public GwtKuraErrorCode getCode() {
        return this.m_errorCode;
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage();
    }

    @Override
    public String getLocalizedMessage() {

        if (GwtKuraErrorCode.INTERNAL_ERROR.equals(this.m_errorCode) && this.m_arguments != null
                && this.m_arguments.length == 1) {
            return this.m_arguments[0];
        }

        String msg = this.m_errorCode.toString();
        try {
            // ValidationMessages MSGS = GWT.create(ValidationMessages.class);
            // String msgPattern = MSGS.getString(m_errorCode.name());
            // if (msgPattern != null) {
            // msg = format(msgPattern, (Object[]) m_arguments);
            // }
        } catch (MissingResourceException e) {
            // ignore
        }
        return msg;
    }

    public String[] getArguments() {
        return this.m_arguments;
    }

///* CODE SNIPPET STARTS HERE *///
    @SuppressWarnings("unused")
    private String format(String s, Object[] arguments) {

        if (arguments == null) {
            return s;
        }

        // A very simple implementation of format
        int i = 0;
        while (i < arguments.length) {
            String delimiter = "{" + i + "}";
            while (s.contains(delimiter)) {
                s = s.replace(delimiter, String.valueOf(arguments[i]));
            }
            i++;
        }
        return s;
    }
///* CODE SNIPPET ENDS HERE *///
}
