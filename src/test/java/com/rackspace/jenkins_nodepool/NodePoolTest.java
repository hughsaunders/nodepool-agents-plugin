/*
 * The MIT License
 *
 * Copyright 2018 Rackspace.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.rackspace.jenkins_nodepool;

import org.apache.curator.framework.CuratorFramework;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

/**
 *
 * @author Rackspace
 */
public class NodePoolTest {

    @ClassRule
    public static NodePoolRule npr = new NodePoolRule();

    private static CuratorFramework conn;
    private static Integer zkPort;



    public NodePoolTest() {

    }

    @BeforeClass
    public static void setUpClass() throws InterruptedException {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of idForPath method, of class NodePoolClient.
     */
//    @Test
//    public void testIdForPath() throws Exception {
//        System.out.println("idForPath");
//        String path = "foo/bah-123";
//        String expResult = "123";
//        String result = NodePool.idForPath(path);
//        assertEquals(expResult, result);
//    }

    @Test
    public void requestTest() throws Exception {
        CuratorFramework conn = npr.getCuratorConnection();
        conn.create().forPath("/testnode");
        conn.delete().forPath("/testnode");
        //   NodePool np = new NodePool(npr.getConnectionString());
        // NodeRequest nr = npc.requestNode("fake-label");
        // nr.waitForFulfillment();

    }

}
