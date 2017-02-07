package com.hpe.application.automation.tfs;

import com.hpe.application.automation.tools.common.model.CdaDetails;
import com.hpe.application.automation.tools.common.rest.RestClient;
import com.hpe.application.automation.tools.common.result.model.junit.Testsuites;
import com.hpe.application.automation.tools.common.sdk.Args;
import com.hpe.application.automation.tools.common.sdk.Logger;
import com.hpe.application.automation.tools.common.sdk.RunManager;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private String ReportFileName = "";

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
        ReportFileName = GetStringParameter(args, "repname:");
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

        //RestLogger logger = new RestLogger();
        final StringBuilder sb = new StringBuilder(10000);
        Logger logger = new Logger() {

            public void log(String message) {
                System.out.println(message);
                sb.append(message);
            }
        };
        Testsuites result = runManager.execute(restClient, args, logger);

        createResultFile(sb.toString());
    }

    //link example^ http://mydphdb0140.hpeswlab.net:8080/qcbin/webui/alm/denis/Project_1/lab/index.jsp?processRunId=1013

    private void createResultFile(String log) {
        if (ReportFileName == null || ReportFileName.isEmpty()) {
            return;
        }
        try {
            String workingDirectory = Paths.get(AlmLabManagementTTask.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().getParent().toString();
            workingDirectory = Paths.get(workingDirectory, "res").toString();
            File resultFile = new File(Paths.get(workingDirectory, ReportFileName).toString());
            Pattern p = Pattern.compile("http://.+?processRunId=([0-9]+)");
            Matcher m = p.matcher(log);
            if (!m.find()) {
                return;
            }
            //PrintWriter writer = new PrintWriter(resultFile, "UTF-8");
            //writer.println("[Report " + m.group(1) + "](" + m.group(0) + ")  ");
            //writer.close();

            FileUtils.writeStringToFile(resultFile, String.format("[Report %s](%s)  ", m.group(1), m.group()));
        }
        catch(Throwable th) {
        }
    }
}
