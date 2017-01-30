package com.hpe.application.automation.tfs;

import com.hpe.application.automation.tools.common.model.CdaDetails;
import com.hpe.application.automation.tools.common.rest.RestClient;
import com.hpe.application.automation.tools.common.result.model.junit.Testsuites;
import com.hpe.application.automation.tools.common.sdk.Args;
import com.hpe.application.automation.tools.common.sdk.Logger;
import com.hpe.application.automation.tools.common.sdk.RunManager;

public class AlmLabManagementTTask extends AbstractTask {
    private String AlmServ; //Required
    private String UserName; //Required
    private String Domain; //Required
    private String Project; //Required
    private String TestSet; //Required
    private String TimeSlotDuration; //Required
    private String Pass = "";
    private String RunType = "";
    private String EnvironmentConfigurationID = "";
    private String Description = "";
    private Boolean UseCDA;
    private String DeploymentAction = "";
    private String DeploymentEnvironmentName = "";
    private String DeprovisioningAction = "";

    public void parseArgs(String[] args) throws Exception {

        AlmServ = GetStringParameter(args, "serv:");
        if (AlmServ.length() == 0) {
            throw new Exception("'ALM server' parameter missing");
        }
        UserName = GetStringParameter(args, "user:");
        if (UserName.length() == 0) {
            throw new Exception("'User name' parameter missing");
        }
        Domain = GetStringParameter(args, "domain:");
        if (Domain.length() == 0) {
            throw new Exception("'Domain' parameter missing");
        }
        Project = GetStringParameter(args, "project:");
        if (Project.length() == 0) {
            throw new Exception("'Project' parameter missing");
        }
        TestSet = GetStringParameter(args, "testSet:");
        if (TestSet.length() == 0) {
            throw new Exception("'TestSet' parameter missing");
        }
        TimeSlotDuration = GetStringParameter(args, "timeSlotDuration:");
        if (TimeSlotDuration.length() == 0) {
            throw new Exception("'Time slot duration' parameter missing");
        }
        Pass = GetStringParameter(args, "pass:");
        RunType = GetStringParameter(args, "runType:");
        EnvironmentConfigurationID = GetStringParameter(args, "envconfID:");
        Description = GetStringParameter(args, "desc:");
        UseCDA = GetBoolParameter(args, "useCDA:", false);
        DeploymentAction = GetStringParameter(args, "deploymentAction:");
        DeploymentEnvironmentName = GetStringParameter(args, "depEnvName:");
        DeprovisioningAction = GetStringParameter(args, "deprovisioningAction:");
    }

    public void execute() throws Throwable {

        RunManager runManager = new RunManager();
        CdaDetails cdaDetails = null;
        if(UseCDA)
        {
            cdaDetails = new CdaDetails(DeploymentAction, DeploymentEnvironmentName, DeprovisioningAction);
        }

        Args args = new Args(AlmServ, Domain, Project, UserName, Pass, RunType, TestSet, TimeSlotDuration,
                            Description, null, EnvironmentConfigurationID, cdaDetails);

        RestClient restClient = new RestClient(AlmServ, Domain, Project, UserName);

        RestLogger logger = new RestLogger();

        Testsuites result = runManager.execute(restClient, args, logger);
    }
}
