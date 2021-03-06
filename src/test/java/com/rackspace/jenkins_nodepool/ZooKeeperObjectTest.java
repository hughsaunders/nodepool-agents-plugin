/*
 * The MIT License
 *
 * Copyright 2018 hughsaunders.
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

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author hughsaunders
 */
public class ZooKeeperObjectTest {

    Mocks m;
    ZooKeeperObjectImpl zko;
    String key;
    String value;
    String path;
    Map map;

    public ZooKeeperObjectTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        path = "/tgfzk";

        m = new Mocks();
        zko = new ZooKeeperObjectImpl(m.np);
        zko.setPath(path);
        assert m.np.getConn() != null;
        map = new HashMap();
        map.put(key, value);
    }

    @After
    public void tearDown() {
        m.cleanup();
    }

    /**
     * Test of updateFromMap method, of class ZooKeeperObject.
     */
    @Test
    public void testUpdateFromMap() {

        zko.updateFromMap(map);
        String rvalue = (String) zko.data.get(key);
        assertEquals(value, rvalue);
    }

    /**
     * Test of getFromZK method, of class ZooKeeperObject.
     */
    @Test
    public void testGetFromZK() throws Exception {
        m.writeNodeData(path, map);
        Map rdata = zko.getFromZK();
        String rvalue = (String) rdata.get(key);
        assertEquals(value, rvalue);
    }

    /**
     * Test of updateFromZK method, of class ZooKeeperObject.
     */
    @Test
    public void testUpdateFromZK() throws Exception {
        m.writeNodeData(path, map);
        zko.updateFromZK();
        String rvalue = (String) zko.data.get(key);
        assertEquals(value, rvalue);
    }

    /**
     * Test of getJson method, of class ZooKeeperObject.
     */
    @Test
    public void testGetJson() {
        zko.data.clear();
        zko.data.put(key, value);
        Map rdata = new Gson().fromJson(zko.getJson(), Map.class);
        assertEquals(value, rdata.get(key));
    }

    /**
     * Test of writeToZK method, of class ZooKeeperObject.
     */
    @Test
    public void testWriteToZK() throws Exception {
        zko.data.clear();
        zko.data.put(key, value);
        zko.writeToZK();
        Map rdata = m.getNodeData(path);
        assertEquals(value, rdata.get(key));

    }

    /**
     * Test of delete method, of class ZooKeeperObject.
     */
    @Test
    public void testDelete() {
        Stat results = null;
        try {
            zko.createZNode();
            results = m.conn.checkExists().forPath(path);
        } catch (Exception ex) {
            Logger.getLogger(ZooKeeperObjectTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertNotNull(results);
        zko.delete();
        try {
            results = m.conn.checkExists().forPath(path);
        } catch (Exception ex) {
            Logger.getLogger(ZooKeeperObjectTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertNull(results);
    }

    /**
     * Test of exists method, of class ZooKeeperObject.
     */
    @Test
    public void testExists() throws Exception {
        Boolean result = zko.exists();
        assertFalse(result);
        zko.createZNode();
        result = zko.exists();
        assertTrue(result);
    }

    public class ZooKeeperObjectImpl extends ZooKeeperObject {

        public ZooKeeperObjectImpl(NodePool np) {
            super(np);
        }
    }

}
