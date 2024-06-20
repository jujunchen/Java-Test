package jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.JobWithDetails;

import java.net.URI;
import java.net.URISyntaxException;

public class JenkinsApiTest {

    public static void main(String[] args) throws Exception {
        JenkinsServer jenkins = new JenkinsServer(new URI("IP:Port"), "user", "password");

        String jobName = "phantom-admin-test"; // 注意，jobName 不要含有空格和中文
        String jobXml = "<?xml version='1.1' encoding='UTF-8'?>\n" +
                "<flow-definition plugin=\"workflow-job@1180.v04c4e75dce43\">\n" +
                "  <actions>\n" +
                "    <org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobAction plugin=\"pipeline-model-definition@2.2077.vc78ec45162f1\"/>\n" +
                "    <org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobPropertyTrackerAction plugin=\"pipeline-model-definition@2.2077.vc78ec45162f1\">\n" +
                "      <jobProperties/>\n" +
                "      <triggers/>\n" +
                "      <parameters/>\n" +
                "      <options/>\n" +
                "    </org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobPropertyTrackerAction>\n" +
                "  </actions>\n" +
                "  <description>[job_description]</description>\n" +
                "  <keepDependencies>false</keepDependencies>\n" +
                "  <properties/>\n" +
                "  <definition class=\"org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition\" plugin=\"workflow-cps@2689.v434009a_31b_f1\">\n" +
                "    <script>#!groovy\n" +
                "\n" +
                "pipeline {\n" +
                "    agent any\n" +
                "    \n" +
                "    environment{\n" +
                "        REPOSITORY=&quot;http://work.gtdreamlife.com:10080/maxrocky/[job_name].git&quot;\n" +
                "        BRANCH=&quot;paas_uat&quot;\n" +
                "        MODULE=&quot;uat-paas-[job_name]&quot;\n" +
                "        SCRIPT_DIR=&quot;/app/uat&quot;\n" +
                "        ENV=&quot;uat&quot;\n" +
                "    }\n" +
                "    \n" +
                "    stages{\n" +
                "        stage(&apos;GitLab拉取代码&apos;) {\n" +
                "            steps{\n" +
                "                 echo&quot;git pull from ${REPOSITORY}&quot;\n" +
                "                 git branch: &quot;${BRANCH}&quot;, credentialsId: &apos;greentown&apos;, url: &quot;${REPOSITORY}&quot;\n" +
                "            }\n" +
                "        }\n" +
                "        stage (&quot;静态代码检查&quot;) {\n" +
                "            steps {\n" +
                "                echo &quot;start code check&quot;\n" +
                "            }\n" +
                "        }\n" +
                "        stage(&apos;Gradle构建&apos;) {\n" +
                "            steps{\n" +
                "                 echo&quot;gradle build&quot;\n" +
                "                 sh &quot;gradle clean build -x test&quot;\n" +
                "            }\n" +
                "        }\n" +
                "        stage(&apos;Docker构建镜像&apos;) {\n" +
                "            steps{\n" +
                "                 echo&quot;docker image&quot;\n" +
                "                 sh &quot;${SCRIPT_DIR}/build.sh ${MODULE} ${ENV} ${BUILD_NUMBER}&quot;\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}</script>\n" +
                "    <sandbox>true</sandbox>\n" +
                "  </definition>\n" +
                "  <triggers/>\n" +
                "  <disabled>false</disabled>\n" +
                "</flow-definition>";

//        jenkins.createJob(jobName, jobXml); // 创建任务
        jenkins.createFolder("测试");
    }
}
