package com.brutus.shared;

public interface Constants {
public static final int BAD_QUALITY = 0;
public static final int MEDIUM_QUALITY = 90;
public static final int BEST_QUALITY = 180;
public static final int LIMIT_CLIENT = 1;//simultaneus client ask of values
public static final int SERVER_WAIT = 2000;//max waiting in ms of answer from single server
public static final int LIMIT_MULTIPLE_REQUEST = 1;//poller limit multiple request at time
public static final String parameters = "parameters";
public static final long longWait = 5000;
public static final long shortWait = 1000;
public static final long maxWait = 2000;
public static final int longWaitStatus = 1;
public static final int shortWaitStatus = 2;
}
