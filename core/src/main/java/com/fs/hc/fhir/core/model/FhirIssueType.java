package com.fs.hc.fhir.core.model;

public enum FhirIssueType {
    /**
     * Content invalid against the specification or a profile.
     */
    INVALID,
    /**
     * A structural issue in the content such as wrong namespace, unable to parse the content completely, invalid syntax, etc.
     */
    STRUCTURE,
    /**
     * A required element is missing.
     */
    REQUIRED,
    /**
     * An element or header value is invalid.
     */
    VALUE,
    /**
     * A content validation rule failed - e.g. a schematron rule.
     */
    INVARIANT,
    /**
     * An authentication/authorization/permissions issue of some kind.
     */
    SECURITY,
    /**
     * The client needs to initiate an authentication process.
     */
    LOGIN,
    /**
     * The user or system was not able to be authenticated (either there is no process, or the proferred token is unacceptable).
     */
    UNKNOWN,
    /**
     * User session expired; a login may be required.
     */
    EXPIRED,
    /**
     * The user does not have the rights to perform this action.
     */
    FORBIDDEN,
    /**
     * Some information was not or might not have been returned due to business rules, consent or privacy rules, or access permission constraints.  This information may be accessible through alternate processes.
     */
    SUPPRESSED,
    /**
     * Processing issues. These are expected to be final e.g. there is no point resubmitting the same content unchanged.
     */
    PROCESSING,
    /**
     * The interaction, operation, resource or profile is not supported.
     */
    NOTSUPPORTED,
    /**
     * An attempt was made to create a duplicate record.
     */
    DUPLICATE,
    /**
     * Multiple matching records were found when the operation required only one match.
     */
    MULTIPLEMATCHES,
    /**
     * The reference provided was not found. In a pure RESTful environment, this would be an HTTP 404 error, but this code may be used where the content is not found further into the application architecture.
     */
    NOTFOUND,
    /**
     * The reference pointed to content (usually a resource) that has been deleted.
     */
    DELETED,
    /**
     * Provided content is too long (typically, this is a denial of service protection type of error).
     */
    TOOLONG,
    /**
     * The code or system could not be understood, or it was not valid in the context of a particular ValueSet.code.
     */
    CODEINVALID,
    /**
     * An extension was found that was not acceptable, could not be resolved, or a modifierExtension was not recognized.
     */
    EXTENSION,
    /**
     * The operation was stopped to protect server resources; e.g. a request for a value set expansion on all of SNOMED CT.
     */
    TOOCOSTLY,
    /**
     * The content/operation failed to pass some business rule and so could not proceed.
     */
    BUSINESSRULE,
    /**
     * Content could not be accepted because of an edit conflict (i.e. version aware updates). (In a pure RESTful environment, this would be an HTTP 409 error, but this code may be used where the conflict is discovered further into the application architecture.).
     */
    CONFLICT,
    /**
     * Transient processing issues. The system receiving the message may be able to resubmit the same content once an underlying issue is resolved.
     */
    TRANSIENT,
    /**
     * A resource/record locking failure (usually in an underlying database).
     */
    LOCKERROR,
    /**
     * The persistent store is unavailable; e.g. the database is down for maintenance or similar action, and the interaction or operation cannot be processed.
     */
    NOSTORE,
    /**
     * y.
     */
    EXCEPTION,
    /**
     * An internal timeout has occurred.
     */
    TIMEOUT,
    /**
     * Not all data sources typically accessed could be reached or responded in time, so the returned information might not be complete (applies to search interactions and some operations).
     */
    INCOMPLETE,
    /**
     * The system is not prepared to handle this request due to load management.
     */
    THROTTLED,
    /**
     * A message unrelated to the processing success of the completed operation (examples of the latter include things like reminders of password expiry, system maintenance times, etc.).
     */
    INFORMATIONAL,
    /**
     * added to help the parsers with the generic types
     */
    NULL;

    public static FhirIssueType fromCode(String codeString) {
        if (codeString == null || "".equals(codeString))
            return null;
        if ("invalid".equals(codeString))
            return INVALID;
        if ("structure".equals(codeString))
            return STRUCTURE;
        if ("required".equals(codeString))
            return REQUIRED;
        if ("value".equals(codeString))
            return VALUE;
        if ("invariant".equals(codeString))
            return INVARIANT;
        if ("security".equals(codeString))
            return SECURITY;
        if ("login".equals(codeString))
            return LOGIN;
        if ("unknown".equals(codeString))
            return UNKNOWN;
        if ("expired".equals(codeString))
            return EXPIRED;
        if ("forbidden".equals(codeString))
            return FORBIDDEN;
        if ("suppressed".equals(codeString))
            return SUPPRESSED;
        if ("processing".equals(codeString))
            return PROCESSING;
        if ("not-supported".equals(codeString))
            return NOTSUPPORTED;
        if ("duplicate".equals(codeString))
            return DUPLICATE;
        if ("multiple-matches".equals(codeString))
            return MULTIPLEMATCHES;
        if ("not-found".equals(codeString))
            return NOTFOUND;
        if ("deleted".equals(codeString))
            return DELETED;
        if ("too-long".equals(codeString))
            return TOOLONG;
        if ("code-invalid".equals(codeString))
            return CODEINVALID;
        if ("extension".equals(codeString))
            return EXTENSION;
        if ("too-costly".equals(codeString))
            return TOOCOSTLY;
        if ("business-rule".equals(codeString))
            return BUSINESSRULE;
        if ("conflict".equals(codeString))
            return CONFLICT;
        if ("transient".equals(codeString))
            return TRANSIENT;
        if ("lock-error".equals(codeString))
            return LOCKERROR;
        if ("no-store".equals(codeString))
            return NOSTORE;
        if ("exception".equals(codeString))
            return EXCEPTION;
        if ("timeout".equals(codeString))
            return TIMEOUT;
        if ("incomplete".equals(codeString))
            return INCOMPLETE;
        if ("throttled".equals(codeString))
            return THROTTLED;
        if ("informational".equals(codeString))
            return INFORMATIONAL;
        //if (Configuration.isAcceptInvalidEnums())
        //    return null;
        else
            return null;
    }

    public String toCode() {
        switch (this) {
            case INVALID:
                return "invalid";
            case STRUCTURE:
                return "structure";
            case REQUIRED:
                return "required";
            case VALUE:
                return "value";
            case INVARIANT:
                return "invariant";
            case SECURITY:
                return "security";
            case LOGIN:
                return "login";
            case UNKNOWN:
                return "unknown";
            case EXPIRED:
                return "expired";
            case FORBIDDEN:
                return "forbidden";
            case SUPPRESSED:
                return "suppressed";
            case PROCESSING:
                return "processing";
            case NOTSUPPORTED:
                return "not-supported";
            case DUPLICATE:
                return "duplicate";
            case MULTIPLEMATCHES:
                return "multiple-matches";
            case NOTFOUND:
                return "not-found";
            case DELETED:
                return "deleted";
            case TOOLONG:
                return "too-long";
            case CODEINVALID:
                return "code-invalid";
            case EXTENSION:
                return "extension";
            case TOOCOSTLY:
                return "too-costly";
            case BUSINESSRULE:
                return "business-rule";
            case CONFLICT:
                return "conflict";
            case TRANSIENT:
                return "transient";
            case LOCKERROR:
                return "lock-error";
            case NOSTORE:
                return "no-store";
            case EXCEPTION:
                return "exception";
            case TIMEOUT:
                return "timeout";
            case INCOMPLETE:
                return "incomplete";
            case THROTTLED:
                return "throttled";
            case INFORMATIONAL:
                return "informational";
            default:
                return "?";
        }
    }

    public String getSystem() {
        switch (this) {
            case INVALID:
                return "http://hl7.org/fhir/issue-type";
            case STRUCTURE:
                return "http://hl7.org/fhir/issue-type";
            case REQUIRED:
                return "http://hl7.org/fhir/issue-type";
            case VALUE:
                return "http://hl7.org/fhir/issue-type";
            case INVARIANT:
                return "http://hl7.org/fhir/issue-type";
            case SECURITY:
                return "http://hl7.org/fhir/issue-type";
            case LOGIN:
                return "http://hl7.org/fhir/issue-type";
            case UNKNOWN:
                return "http://hl7.org/fhir/issue-type";
            case EXPIRED:
                return "http://hl7.org/fhir/issue-type";
            case FORBIDDEN:
                return "http://hl7.org/fhir/issue-type";
            case SUPPRESSED:
                return "http://hl7.org/fhir/issue-type";
            case PROCESSING:
                return "http://hl7.org/fhir/issue-type";
            case NOTSUPPORTED:
                return "http://hl7.org/fhir/issue-type";
            case DUPLICATE:
                return "http://hl7.org/fhir/issue-type";
            case MULTIPLEMATCHES:
                return "http://hl7.org/fhir/issue-type";
            case NOTFOUND:
                return "http://hl7.org/fhir/issue-type";
            case DELETED:
                return "http://hl7.org/fhir/issue-type";
            case TOOLONG:
                return "http://hl7.org/fhir/issue-type";
            case CODEINVALID:
                return "http://hl7.org/fhir/issue-type";
            case EXTENSION:
                return "http://hl7.org/fhir/issue-type";
            case TOOCOSTLY:
                return "http://hl7.org/fhir/issue-type";
            case BUSINESSRULE:
                return "http://hl7.org/fhir/issue-type";
            case CONFLICT:
                return "http://hl7.org/fhir/issue-type";
            case TRANSIENT:
                return "http://hl7.org/fhir/issue-type";
            case LOCKERROR:
                return "http://hl7.org/fhir/issue-type";
            case NOSTORE:
                return "http://hl7.org/fhir/issue-type";
            case EXCEPTION:
                return "http://hl7.org/fhir/issue-type";
            case TIMEOUT:
                return "http://hl7.org/fhir/issue-type";
            case INCOMPLETE:
                return "http://hl7.org/fhir/issue-type";
            case THROTTLED:
                return "http://hl7.org/fhir/issue-type";
            case INFORMATIONAL:
                return "http://hl7.org/fhir/issue-type";
            default:
                return "?";
        }
    }

    public String getDefinition() {
        switch (this) {
            case INVALID:
                return "Content invalid against the specification or a profile.";
            case STRUCTURE:
                return "A structural issue in the content such as wrong namespace, unable to parse the content completely, invalid syntax, etc.";
            case REQUIRED:
                return "A required element is missing.";
            case VALUE:
                return "An element or header value is invalid.";
            case INVARIANT:
                return "A content validation rule failed - e.g. a schematron rule.";
            case SECURITY:
                return "An authentication/authorization/permissions issue of some kind.";
            case LOGIN:
                return "The client needs to initiate an authentication process.";
            case UNKNOWN:
                return "The user or system was not able to be authenticated (either there is no process, or the proferred token is unacceptable).";
            case EXPIRED:
                return "User session expired; a login may be required.";
            case FORBIDDEN:
                return "The user does not have the rights to perform this action.";
            case SUPPRESSED:
                return "Some information was not or might not have been returned due to business rules, consent or privacy rules, or access permission constraints.  This information may be accessible through alternate processes.";
            case PROCESSING:
                return "Processing issues. These are expected to be final e.g. there is no point resubmitting the same content unchanged.";
            case NOTSUPPORTED:
                return "The interaction, operation, resource or profile is not supported.";
            case DUPLICATE:
                return "An attempt was made to create a duplicate record.";
            case MULTIPLEMATCHES:
                return "Multiple matching records were found when the operation required only one match.";
            case NOTFOUND:
                return "The reference provided was not found. In a pure RESTful environment, this would be an HTTP 404 error, but this code may be used where the content is not found further into the application architecture.";
            case DELETED:
                return "The reference pointed to content (usually a resource) that has been deleted.";
            case TOOLONG:
                return "Provided content is too long (typically, this is a denial of service protection type of error).";
            case CODEINVALID:
                return "The code or system could not be understood, or it was not valid in the context of a particular ValueSet.code.";
            case EXTENSION:
                return "An extension was found that was not acceptable, could not be resolved, or a modifierExtension was not recognized.";
            case TOOCOSTLY:
                return "The operation was stopped to protect server resources; e.g. a request for a value set expansion on all of SNOMED CT.";
            case BUSINESSRULE:
                return "The content/operation failed to pass some business rule and so could not proceed.";
            case CONFLICT:
                return "Content could not be accepted because of an edit conflict (i.e. version aware updates). (In a pure RESTful environment, this would be an HTTP 409 error, but this code may be used where the conflict is discovered further into the application architecture.).";
            case TRANSIENT:
                return "Transient processing issues. The system receiving the message may be able to resubmit the same content once an underlying issue is resolved.";
            case LOCKERROR:
                return "A resource/record locking failure (usually in an underlying database).";
            case NOSTORE:
                return "The persistent store is unavailable; e.g. the database is down for maintenance or similar action, and the interaction or operation cannot be processed.";
            case EXCEPTION:
                return "y.";
            case TIMEOUT:
                return "An internal timeout has occurred.";
            case INCOMPLETE:
                return "Not all data sources typically accessed could be reached or responded in time, so the returned information might not be complete (applies to search interactions and some operations).";
            case THROTTLED:
                return "The system is not prepared to handle this request due to load management.";
            case INFORMATIONAL:
                return "A message unrelated to the processing success of the completed operation (examples of the latter include things like reminders of password expiry, system maintenance times, etc.).";
            default:
                return "?";
        }
    }

    public String getDisplay() {
        switch (this) {
            case INVALID:
                return "Invalid Content";
            case STRUCTURE:
                return "Structural Issue";
            case REQUIRED:
                return "Required element missing";
            case VALUE:
                return "Element value invalid";
            case INVARIANT:
                return "Validation rule failed";
            case SECURITY:
                return "Security Problem";
            case LOGIN:
                return "Login Required";
            case UNKNOWN:
                return "Unknown User";
            case EXPIRED:
                return "Session Expired";
            case FORBIDDEN:
                return "Forbidden";
            case SUPPRESSED:
                return "Information  Suppressed";
            case PROCESSING:
                return "Processing Failure";
            case NOTSUPPORTED:
                return "Content not supported";
            case DUPLICATE:
                return "Duplicate";
            case MULTIPLEMATCHES:
                return "Multiple Matches";
            case NOTFOUND:
                return "Not Found";
            case DELETED:
                return "Deleted";
            case TOOLONG:
                return "Content Too Long";
            case CODEINVALID:
                return "Invalid Code";
            case EXTENSION:
                return "Unacceptable Extension";
            case TOOCOSTLY:
                return "Operation Too Costly";
            case BUSINESSRULE:
                return "Business Rule Violation";
            case CONFLICT:
                return "Edit Version Conflict";
            case TRANSIENT:
                return "Transient Issue";
            case LOCKERROR:
                return "Lock Error";
            case NOSTORE:
                return "No Store Available";
            case EXCEPTION:
                return "Exception";
            case TIMEOUT:
                return "Timeout";
            case INCOMPLETE:
                return "Incomplete Results";
            case THROTTLED:
                return "Throttled";
            case INFORMATIONAL:
                return "Informational Note";
            default:
                return "?";
        }
    }

}
