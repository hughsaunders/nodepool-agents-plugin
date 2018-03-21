package com.rackspace.jenkins_nodepool;

import org.junit.Rule;
import org.jvnet.hudson.test.RestartableJenkinsRule;

public class NodePoolCloudTest {

    @Rule
    public RestartableJenkinsRule rr = new RestartableJenkinsRule();

    /**
     * Tries to exercise enough code paths to catch common mistakes:
     * <ul>
     * <li>missing {@code load}
     * <li>missing {@code save}
     * <li>misnamed or absent getter/setter
     * <li>misnamed {@code textbox}
     * </ul>
     */
//    @Test
//    public void testConfigWithRestart() {
//        rr.then(r -> {
//	    NodePoolCloud npc = new NodePoolCloud("test", "host1:port1,host2:port2");
//	    r.jenkins.clouds.add(npc);
//	    HtmlForm config = r.createWebClient().goTo("configure").getFormByName("config");
//            HtmlTextInput nameStringBox = config.getInputByName("_.name");
//	    assertEquals(nameStringBox.getText(),"test");
//	    nameStringBox.setText("test2");
//	    r.submit(config);
//
//
//        });
//        rr.then(r -> {
//            HtmlForm config = r.createWebClient().goTo("configure").getFormByName("config");
//            HtmlTextInput nameStringBox = config.getInputByName("_.name");
//            HtmlTextInput connectionStringBox = config.getInputByName("_.connectionString");
//	    assertEquals(nameStringBox.getText(), "test2");
//	    assertEquals(connectionStringBox.getText(), "host1:port1,host2:port2");
//        });
//    }

}
