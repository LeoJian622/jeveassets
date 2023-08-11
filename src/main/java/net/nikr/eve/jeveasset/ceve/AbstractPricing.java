package net.nikr.eve.jeveasset.ceve;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import uk.me.candle.eve.pricing.PriceContainer;
import uk.me.candle.eve.pricing.PricingFetchListener;
import uk.me.candle.eve.pricing.options.PriceType;
import uk.me.candle.eve.pricing.options.PricingOptions;
import uk.me.candle.eve.pricing.options.impl.DefaultPricingOptions;
import uk.me.candle.eve.pricing.util.SplitList;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.ref.WeakReference;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;

/**
 * @author Leojan
 * @date 2023-08-07 16:47
 */

public abstract class AbstractPricing implements Pricing {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractPricing.class);
    private final Map<Integer, AbstractPricing.CachedPrice> cache = Collections.synchronizedMap(new HashMap<>());
    private final List<WeakReference<PricingListener>> pricingListeners = new ArrayList<>();
    private final List<WeakReference<PricingFetchListener>> pricingFetchListeners = new ArrayList<>();
    private final Queue<Integer> waitingQueue = new ConcurrentLinkedQueue<>();
    private final List<Integer> currentlyEvaluating = Collections.synchronizedList(new ArrayList<>());
    private final List<AbstractPricing.PriceFetchingThread> priceFetchingThreads = Collections.synchronizedList(new ArrayList<>());
    private final Map<Integer, AtomicInteger> failedFetchAttempts = Collections.synchronizedMap(new HashMap<>());
    private final Map<Integer, List<String>> failedFetchReasons = Collections.synchronizedMap(new HashMap<>());
    private static final Gson GSON = (new GsonBuilder()).create();
    private static final HttpLoggingInterceptor HTTP_LOGGING_INTERCEPTOR = new HttpLoggingInterceptor(LOG::debug);
    private final OkHttpClient client;
    long cacheTimer = 3600000L;
    PricingOptions options = new DefaultPricingOptions();

    public AbstractPricing(int threads) {
        if (LOG.isDebugEnabled()) {
            this.client = new OkHttpClient().newBuilder().addNetworkInterceptor(HTTP_LOGGING_INTERCEPTOR).build();
            HTTP_LOGGING_INTERCEPTOR.setLevel(HttpLoggingInterceptor.Level.BASIC);
        } else {
            this.client = new OkHttpClient().newBuilder().build();
        }

        for (int i = 1; i <= threads; ++i) {
            PriceFetchingThread priceFetchingThread = new PriceFetchingThread(i);
            this.priceFetchingThreads.add(priceFetchingThread);
            priceFetchingThread.start();
        }

    }

    public Gson getGSON() {
        return GSON;
    }

    public OkHttpClient getClient() {
        return this.client;
    }

    @Override
    public Double getPriceCache(int typeID, PriceType type) {
        CachedPrice cp = this.cache.get(typeID);
        if (cp != null) {
            return cp.getContainer().getPrice(type);
        }
        return null;
    }

    @Override
    public Double getPrice(int typeID, PriceType type) {
        boolean cacheTimout;
        CachedPrice cp = this.cache.get(typeID);
        boolean bl = cacheTimout = cp != null && cp.getTime() + this.cacheTimer < System.currentTimeMillis();
        if (cp == null || this.options.getCacheTimersEnabled() && cacheTimout) {
            this.queuePrice(typeID);
            return null;
        }
        return cp.getContainer().getPrice(type);
    }

    @Override
    public void updatePrices(Set<Integer> typeIDs) {
        this.queuePrices(typeIDs);
    }

    @Override
    public long getNextUpdateTime(int typeID) {
        CachedPrice cp = this.cache.get(typeID);
        if (cp == null) {
            return -1L;
        }
        return cp.getTime();
    }

    protected Element getElement(URL url) throws SocketTimeoutException, IOException, ParserConfigurationException, SAXException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.getInputStream(url)).getDocumentElement();
    }

    protected InputStream getInputStream(URL url) throws SocketTimeoutException, IOException {
        Proxy proxy;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Fetching URL: " + url);
        }
        URLConnection urlCon = (proxy = this.options.getProxy()) != null ? url.openConnection(proxy) : url.openConnection();
        urlCon.setReadTimeout(this.options.getTimeout());
        urlCon.setDoInput(true);
        urlCon.setRequestProperty("Accept-Encoding", "gzip");
        if ("gzip".equals(urlCon.getContentEncoding())) {
            return new GZIPInputStream(urlCon.getInputStream());
        }
        return urlCon.getInputStream();
    }

    @Override
    public void setPricingOptions(PricingOptions options) {
        if (options == null) {
            throw new IllegalArgumentException("Options can not be null");
        }
        this.options = options;
        this.cacheTimer = options.getPriceCacheTimer();
        this.read();
    }

    protected abstract Map<Integer, PriceContainer> fetchPrices(Collection<Integer> var1);

    protected int getBatchSize() {
        return -1;
    }

    public long getCacheTimer() {
        return this.cacheTimer;
    }

    public void setCacheTimer(long cacheTimer) {
        this.cacheTimer = cacheTimer;
    }

    @Override
    public void clearPrice(int typeID) {
        long cacheTime = -1L;
        CachedPrice current = this.cache.get(typeID);
        if (current != null) {
            this.cache.put(typeID, new CachedPrice(cacheTime, current.getContainer().createClone().build()));
        } else {
            this.cache.remove(typeID);
        }

        this.notifyPricingListeners(typeID);
    }

    @Override
    public PricingOptions getPricingOptions() {
        return this.options;
    }

    @Override
    public boolean removePricingListener(PricingListener o) {
        for (int i = this.pricingListeners.size() - 1; i >= 0; --i) {
            WeakReference<PricingListener> wpl = this.pricingListeners.get(i);
            PricingListener pl = wpl.get();
            if (pl == null) {
                this.pricingListeners.remove(i);
                continue;
            }
            if (!pl.equals(o)) continue;
            this.pricingListeners.remove(i);
            return true;
        }
        return false;
    }

    private void notifyPricingListeners(Integer typeID) {
        LOG.debug("notifying " + this.pricingListeners.size() + " pricing listeners with " + typeID);

        for (int i = this.pricingListeners.size() - 1; i >= 0; --i) {
            WeakReference<PricingListener> wpl = this.pricingListeners.get(i);
            PricingListener pl = wpl.get();
            if (pl == null) {
                this.pricingListeners.remove(i);
                continue;
            }
            pl.priceUpdated(typeID, this);
        }
    }

    @Override
    public boolean addPricingListener(PricingListener pl) {
        return this.pricingListeners.add(new WeakReference<>(pl));
    }

    @Override
    public boolean addPricingFetchListener(PricingFetchListener pfl) {
        return this.pricingFetchListeners.add(new WeakReference<>(pfl));
    }

    @Override
    public boolean removePricingFetchListener(PricingFetchListener o) {
        for (int i = this.pricingFetchListeners.size() - 1; i >= 0; --i) {
            WeakReference<PricingFetchListener> wpl = this.pricingFetchListeners.get(i);
            PricingFetchListener pl = wpl.get();
            if (pl == null) {
                this.pricingFetchListeners.remove(i);
                continue;
            }
            if (!pl.equals(o)) continue;
            this.pricingFetchListeners.remove(i);
            return true;
        }
        return false;
    }

    private void notifyPricingFetchListeners(boolean started) {
        LOG.debug("notifying " + this.pricingFetchListeners.size() + " fetch listeners. [" + started + "]");

        for (int i = this.pricingFetchListeners.size() - 1; i >= 0; --i) {
            WeakReference<PricingFetchListener> wpl = this.pricingFetchListeners.get(i);
            PricingFetchListener pl = wpl.get();
            if (pl == null) {
                this.pricingFetchListeners.remove(i);
                continue;
            }
            if (started) {
                pl.fetchStarted();
                continue;
            }
            pl.fetchEnded();
        }
    }

    private void queuePrice(int itemID) {
        this.queuePrices(Collections.singleton(itemID));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void queuePrices(Set<Integer> itemIDs) {
        Iterator<Integer> iterator = itemIDs.iterator();
        while (iterator.hasNext()) {
            int itemID = iterator.next();
            if (this.waitingQueue.contains(itemID) || this.currentlyEvaluating.contains(itemID) || !this.checkFailureCountExceeded(itemID))
                continue;
            if (LOG.isTraceEnabled()) {
                LOG.trace("queued " + itemID + " for price fetching");
            }

            this.waitingQueue.add(itemID);
        }
        Iterator<PriceFetchingThread> iterator1 = this.priceFetchingThreads.iterator();
        while (iterator1.hasNext()) {
            PriceFetchingThread priceFetchingThread;
            PriceFetchingThread priceFetchingThread2 = priceFetchingThread = iterator1.next();
            synchronized (priceFetchingThread2) {
                priceFetchingThread.notifyAll();
            }
        }

    }

    @Override
    public void writeCache() throws IOException {
        this.write();
    }

    private synchronized void read() {
        InputStream input = null;

        try {
            input = this.options.getCacheInputStream();
            if (input != null) {
                ObjectInputStream oos = new ObjectInputStream(input);
                this.cache.putAll((Map) oos.readObject());
            }
        } catch (IOException ioe) {
            LOG.error("Error reading the cache file (Other IO error)", (Throwable) ioe);
        } catch (ClassNotFoundException cnfe) {
            LOG.error("Error reading the cache file (incompatible classes)", (Throwable) cnfe);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ioe) {
                }
            }

        }

    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private synchronized void write() throws IOException {
        block11:
        {
            OutputStream output = null;

            try {
                output = this.options.getCacheOutputStream();
                if (output == null) break block11;
                Map<Integer, CachedPrice> map = this.cache;
                synchronized (map) {
                    ObjectOutputStream oos = new ObjectOutputStream(output);
                    oos.writeObject(this.cache);
                    oos.flush();
                }
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException iOException) {
                    }
                }

            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void cancelAll() {
        while (!this.currentlyEvaluating.isEmpty() || !this.waitingQueue.isEmpty()) {
            while (!this.waitingQueue.isEmpty()) {
                try {
                    this.notifyFailedFetch(this.waitingQueue.remove());
                } catch (NoSuchElementException noSuchElementException) {
                }
            }
            List<Integer> list = this.currentlyEvaluating;
            synchronized (list) {
                try {
                    if (!this.currentlyEvaluating.isEmpty()) {
                        this.currentlyEvaluating.wait();
                    }
                } catch (InterruptedException interruptedException) {
                    // empty catch block
                }
            }
        }
    }

    @Override
    public void cancelSingle(int itemID) {
        this.waitingQueue.remove(itemID);
        this.notifyFailedFetch(itemID);
    }

    @Override
    public void resetAllAttemptCounters() {
        this.failedFetchAttempts.clear();
        this.failedFetchReasons.clear();
    }

    @Override
    public void resetAttemptCounter(int itemID) {
        this.fetchAttemptCount(itemID).set(0);
        this.fetchAttemptReason(itemID).clear();
    }

    private boolean checkFailureCountExceeded(int itemID) {
        int maxFailures = this.options.getAttemptCount();
        if (maxFailures <= 0) {
            return true;
        }
        return this.fetchAttemptCount(itemID).get() < maxFailures;
    }

    private AtomicInteger fetchAttemptCount(int itemID) {
        if (!this.failedFetchAttempts.containsKey(itemID)) {
            this.failedFetchAttempts.put(itemID, new AtomicInteger(0));
        }

        return this.failedFetchAttempts.get(itemID);
    }

    protected void addFailureCount(int itemID) {
        int i = this.fetchAttemptCount(itemID).incrementAndGet();
        if (i >= this.options.getAttemptCount()) {
            this.notifyFailedFetch(itemID);
        }

    }

    protected void notifyFailedFetch(int itemID) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("notifying " + this.pricingListeners.size() + " listeners. [" + itemID + "]");
        }

        for (int i = this.pricingListeners.size() - 1; i >= 0; --i) {
            WeakReference<PricingListener> wpl = this.pricingListeners.get(i);
            PricingListener pl = wpl.get();
            if (pl == null) {
                this.pricingListeners.remove(i);
                continue;
            }
            pl.priceUpdateFailed(itemID, this);
        }
    }

    private List<String> fetchAttemptReason(int itemID) {
        if (!this.failedFetchReasons.containsKey(itemID)) {
            this.failedFetchReasons.put(itemID, new ArrayList<>());
        }

        return this.failedFetchReasons.get(itemID);
    }

    protected void addFetchFailureReason(int itemID, String reason) {
        this.fetchAttemptReason(itemID).add(reason);
    }

    protected void addFailureReasons(Collection<Integer> itemIDs, String reason) {
        for (Integer i : itemIDs) {
            this.addFetchFailureReason(i, reason);
        }

    }

    @Override
    public int getFailedAttempts(int itemID) {
        return this.fetchAttemptCount(itemID).get();
    }

    @Override
    public List<String> getFetchErrors(int typeID) {
        return Collections.unmodifiableList(this.fetchAttemptReason(typeID));
    }

    private class PriceFetchingThread extends Thread {
        private final Set<Integer> evaluate;
        private SplitList failed;

        public PriceFetchingThread(int id) {
            super("Price Fetching " + id);
            this.evaluate = new HashSet<Integer>();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            while (true) {
                block25:
                {
                    try {
                        if (!AbstractPricing.this.waitingQueue.isEmpty()) break block25;
                        PriceFetchingThread priceFetchingThread = this;
                        synchronized (priceFetchingThread) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Pricing fetch thread is waiting.");
                            }

                            this.wait();
                        }
                    } catch (InterruptedException ie) {
                        LOG.warn("Pricing fetch thread interrupted.");
                    }
                }
                AbstractPricing.this.notifyPricingFetchListeners(true);
                if (this.failed != null) {
                    List<Integer> nextList = this.failed.getNextList();
                    if (nextList != null) {
                        this.evaluate.addAll(nextList);
                    } else {
                        this.failed = null;
                    }
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("There are " + AbstractPricing.this.waitingQueue.size() + " items in the queue.");
                }

                while (this.failed == null && AbstractPricing.this.waitingQueue.size() > 0 && (AbstractPricing.this.getBatchSize() <= 0 || this.evaluate.size() < AbstractPricing.this.getBatchSize())) {
                    try {
                        Integer num = (Integer) AbstractPricing.this.waitingQueue.remove();
                        if (!AbstractPricing.this.checkFailureCountExceeded(num)) continue;
                        this.evaluate.add(num);
                        AbstractPricing.this.currentlyEvaluating.add(num);
                    } catch (NoSuchElementException num) {
                    }
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Starting price fetch for " + this.evaluate.size() + " items.");
                }
                Map<Integer, PriceContainer> prices = AbstractPricing.this.fetchPrices(this.evaluate);
                boolean doBinarySearch = false;
                for (Integer itemId : this.evaluate) {
                    if (prices.containsKey(itemId)) {
                        PriceContainer priceContainer = prices.get(itemId);
                        AbstractPricing.this.cache.put(itemId, new CachedPrice(System.currentTimeMillis(), priceContainer));
                        AbstractPricing.this.notifyPricingListeners(itemId);
                        continue;
                    }
                    if (AbstractPricing.this.options.getUseBinaryErrorSearch()) {
                        if (this.evaluate.size() == 1) {
                            AbstractPricing.this.notifyFailedFetch(itemId);
                            continue;
                        }
                        doBinarySearch = true;
                        continue;
                    }
                    AbstractPricing.this.addFailureCount(itemId);
                    AbstractPricing.this.waitingQueue.add(itemId);
                }
                if (doBinarySearch && this.failed == null) {
                    this.failed = new SplitList(this.evaluate);
                }

                if (!doBinarySearch && this.failed != null) {
                    this.failed.removeLast();
                }
                synchronized (AbstractPricing.this.currentlyEvaluating) {
                    AbstractPricing.this.currentlyEvaluating.removeAll(this.evaluate);
                    AbstractPricing.this.currentlyEvaluating.notifyAll();
                }

                this.evaluate.clear();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("finished fetch");
                }

                AbstractPricing.this.notifyPricingFetchListeners(false);
            }
        }
    }

    public static class CachedPrice implements Serializable {
        private static final long serialVersionUID = 76589234632L;
        long time;
        PriceContainer container;

        public CachedPrice(long time, PriceContainer container) {
            this.time = time;
            this.container = container;
        }

        public PriceContainer getContainer() {
            return this.container;
        }

        public long getTime() {
            return this.time;
        }
    }
}

