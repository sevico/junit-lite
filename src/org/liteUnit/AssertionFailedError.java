package org.liteUnit;

public class AssertionFailedError extends Error {

    public AssertionFailedError () {
    }
    public AssertionFailedError (String message) {
        super (message);
    }
}
