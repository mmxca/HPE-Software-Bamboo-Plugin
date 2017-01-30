package com.hpe.application.automation.tfs;

/**
 * Created by ybobrik on 1/22/2017.
 */
public abstract class AbstractTask {
    public abstract void parseArgs(String[] args) throws Exception;

    public abstract void execute() throws Throwable;

    private final String PasswordPrefix = "pass:";

    protected String extractPasswordFromParameter(String arg) {
        return extractvalueFromParameter(arg, PasswordPrefix);
    }

    protected String extractvalueFromParameter(String arg, String prefix) {
        int pl = prefix.length();
        if (arg == null
                || arg.isEmpty()
                || arg.length() < pl
                || !arg.substring(0, pl).toLowerCase().equals(prefix)) {
            throw new IllegalArgumentException();
        }
        return arg.substring(pl);
    }

    protected String GetStringParameter(String[] args, String prefix)
    {
        for(String parameter : args)
        {
            if(parameter.contains(prefix))
            {
                return  parameter.substring(prefix.length());
            }
        }
        return  "";
    }

    protected boolean GetBoolParameter(String[] args, String prefix, boolean defaultValue)
    {
        for(String parameter : args)
        {
            if(parameter.contains(prefix))
            {
                return  Boolean.parseBoolean(args[10].toLowerCase());
            }
        }
        return  defaultValue;
    }
}
