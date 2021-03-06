/*
    GNU GENERAL LICENSE
    Copyright (C) 2006 The Lobo Project. Copyright (C) 2014 - 2016 Lobo Evolution

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    verion 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General License for more details.

    You should have received a copy of the GNU General Public
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    

    Contact info: lobochief@users.sourceforge.net; ivan.difrancesco@yahoo.it
 */
/*
 * Created on Mar 12, 2005
 */
package org.lobobrowser.store;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.lobobrowser.security.GenericLocalPermission;
import org.lobobrowser.security.LocalSecurityPolicy;
import org.lobobrowser.util.gui.StorageManagerCommon;

/**
 * * @author J. H. S.
 */
public class StorageManager extends StorageManagerCommon implements Runnable {

    /** The Constant logger. */
    private static final Logger logger = LogManager.getLogger(StorageManager.class
            .getName());

    /** The Constant HOST_STORE_QUOTA. */
    private static final long HOST_STORE_QUOTA = 200 * 1024;
    // Note that the installer makes assumptions about these names.
    /** The Constant HOST_STORE_DIR. */
    private static final String HOST_STORE_DIR = "HostStore";

    /** The Constant CACHE_DIR. */
    private static final String CACHE_DIR = "cache";

    /** The Constant CONTENT_DIR. */
    private static final String CONTENT_DIR = "content";

    /** The Constant SETTINGS_DIR. */
    private static final String SETTINGS_DIR = "settings";

    /** The Constant instance. */
    private static final StorageManager instance = new StorageManager();

    /** The store directory. */
    private final File storeDirectory;

    /** Gets the Constant instance.
	 *
	 * @return the Constant instance
	 */
    public static StorageManager getInstance() throws IOException {
        return instance;
    }

    /**
     * Instantiates a new storage manager.
     */
    private StorageManager() {
        this.storeDirectory = LocalSecurityPolicy.STORE_DIRECTORY;
        if (!this.storeDirectory.exists()) {
            this.storeDirectory.mkdirs();
        }
    }

    /** The thread started. */
    private boolean threadStarted = false;

    /**
     * Ensure thread started.
     */
    private void ensureThreadStarted() {
        if (!this.threadStarted) {
            synchronized (this) {
                if (!this.threadStarted) {
                    Thread t = new Thread(this, "StorageManager");
                    t.setDaemon(true);
                    t.setPriority(Thread.MIN_PRIORITY);
                    t.start();
                    this.threadStarted = true;
                }
            }
        }
    }

    /** Gets the app home.
	 *
	 * @return the app home
	 */
    public File getAppHome() {
        return this.storeDirectory;
    }

    /** The Constant NO_HOST. */
    private static final String NO_HOST = "$NO_HOST$";

    /**
     * Gets the cache host directory.
     *
     * @param hostName
     *            the host name
     * @return the cache host directory
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public File getCacheHostDirectory(String hostName) throws IOException {
        CacheManager.getInstance();
        File cacheDir = this.getCacheRoot();
        if ((hostName == null) || "".equals(hostName)) {
            hostName = NO_HOST;
        }
        return new File(cacheDir, normalizedFileName(hostName));
    }

    /**
     * Gets the content cache file.
     *
     * @param hostName
     *            the host name
     * @param fileName
     *            the file name
     * @return the content cache file
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public File getContentCacheFile(String hostName, String fileName)
            throws IOException {
        File domainDir = this.getCacheHostDirectory(hostName);
        File xamjDir = new File(domainDir, CONTENT_DIR);
        return new File(xamjDir, fileName);
    }

    /** Gets the cache root.
	 *
	 * @return the cache root
	 */
    public File getCacheRoot() {
        return new File(this.storeDirectory, CACHE_DIR);
    }

    /** The restricted store cache. */
    private final Map<String, RestrictedStore> restrictedStoreCache = new HashMap<String, RestrictedStore>();

    /**
     * Gets the restricted store.
     *
     * @param hostName
     *            the host name
     * @param createIfNotExists
     *            the create if not exists
     * @return the restricted store
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public RestrictedStore getRestrictedStore(String hostName,
            final boolean createIfNotExists) throws IOException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(GenericLocalPermission.EXT_GENERIC);
        }
        if ((hostName == null) || "".equals(hostName)) {
            hostName = NO_HOST;
        }
        final String normHost = hostName;
        RestrictedStore store;
        synchronized (this) {
            store = this.restrictedStoreCache.get(normHost);
            if (store == null) {
                store = AccessController
                        .doPrivileged(new PrivilegedAction<RestrictedStore>() {
                            // Reason: Since we are checking StoreHostPermission
                            // previously,
                            // this is fine.
                            @Override
                            public RestrictedStore run() {
                                File hostStoreDir = new File(storeDirectory,
                                        HOST_STORE_DIR);
                                File domainDir = new File(hostStoreDir,
                                        normalizedFileName(normHost));
                                if (!createIfNotExists && !domainDir.exists()) {
                                    return null;
                                }
                                try {
                                    return new RestrictedStore(domainDir,
                                            HOST_STORE_QUOTA);
                                } catch (IOException ioe) {
                                    throw new IllegalStateException(ioe);
                                }
                            }
                        });
                if (store != null) {
                    this.restrictedStoreCache.put(normHost, store);
                }
            }
        }
        if (store != null) {
            this.ensureThreadStarted();
        }
        return store;
    }

    /** Gets the settings directory.
	 *
	 * @return the settings directory
	 */
    public File getSettingsDirectory() {
        return new File(this.storeDirectory, SETTINGS_DIR);
    }

    /**
     * Normalized file name.
     *
     * @param hostName
     *            the host name
     * @return the string
     */
    static String normalizedFileName(String hostName) {
        return hostName;
    }

    /**
     * Gets the host name.
     *
     * @param fileName
     *            the file name
     * @return the host name
     */
    static String getHostName(String fileName) {
        return fileName;
    }

    /** The Constant MANAGED_STORE_UPDATE_DELAY. */
    private static final int MANAGED_STORE_UPDATE_DELAY = 1000 * 60 * 5;

    /*
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        for (;;) {
            try {
                Thread.sleep(MANAGED_STORE_UPDATE_DELAY);
                RestrictedStore[] stores;
                synchronized (this) {
                    stores = this.restrictedStoreCache.values().toArray(
                            new RestrictedStore[0]);
                }
                for (int i = 0; i < stores.length; i++) {
                    Thread.yield();
                    stores[i].updateSizeFile();
                }
            } catch (Throwable err) {
                logger.log(Level.ERROR, "run()", err);
                try {
                    Thread.sleep(MANAGED_STORE_UPDATE_DELAY);
                } catch (InterruptedException ie) {
                    // Ignore this time.
                }
            }
        }
    }
}
