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

//TODO: serialise to JSON properly with XStream
/**
 * Base class for zookeeper proxy objects.
 *
 */
public abstract class ZooKeeperObject {
    private static final Logger LOG = Logger.getLogger(ZooKeeperObject.class.getName());
    static final Gson GSON = new Gson();

    /**
     * Copy (possible stale) of data held in a ZNode
     */
    final Map data;

    /**
     * Path to ZNode
     */
    String path;

    /**
     * An identifier associated with a ZNode
     */
    String zKID;

    /**
     * Associated NodePool + ZK information
     */
    NodePool nodePool;

    /**
     * Initialize local copy of ZNode data
     *
     * @param nodePool associated NodePool cluster
     */
    public ZooKeeperObject(NodePool nodePool) {
        data = new HashMap();
        this.nodePool = nodePool;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getZKID() {
        return zKID;
    }

    public void setZKID(String zKID) {
        this.zKID = zKID;
    }

    public void updateFromMap(Map data) {
        this.data.putAll(data);
    }

    /**
     * Get a copy of this object's data from the ZNode
     *
     * @return Map of the ZNode data
     * @throws Exception on ZooKeeper error
     */
    public Map getFromZK() throws Exception {
        byte[] bytes = nodePool.getConn().getData().forPath(this.path);
        String jsonString = new String(bytes, nodePool.getCharset());
        LOG.log(Level.FINEST, "Read ZNODE: {0}, Data: {1}", new Object[]{path, jsonString});
        return GSON.fromJson(jsonString, HashMap.class);
    }

    /**
     * Update this instance from values in the associated ZNode
     *
     * @throws Exception on ZooKeeper error
     */
    public final void updateFromZK() throws Exception {
        final Map zkData = getFromZK();
        updateFromMap(zkData);
    }

    public String getJson() {
        return GSON.toJson(data);
    }

    public void createZNode() throws Exception {
        nodePool.getConn()
                .create()
                .creatingParentsIfNeeded()
                .forPath(path);
    }

    public void writeToZK() throws Exception {
        if (!exists()) {
            createZNode();
        }
        nodePool.getConn()
                .setData().
                forPath(this.path, getJson().getBytes(nodePool.getCharset()));
    }

    /**
     * Delete the associated ZNode
     */
    public void delete() {
        try {
            nodePool.getConn().delete().forPath(path);
        } catch (Exception e) {
            // not sure what else we can do at this point.
            LOG.log(Level.WARNING, "Failed to delete node at path: " + path + ": " + e.getMessage(), e);
        }
    }

    /**
     * Check if the associated ZNode exists
     *
     * @return true if the node exists
     * @throws Exception  on ZooKeeper error
     */
    public boolean exists() throws Exception {
        return nodePool.getConn().checkExists().forPath(getPath()) != null;
    }
}
