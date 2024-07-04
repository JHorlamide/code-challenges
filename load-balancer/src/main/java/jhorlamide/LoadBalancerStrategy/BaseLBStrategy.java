package jhorlamide.LoadBalancerStrategy;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public abstract class BaseLBStrategy implements ILbStrategy {
   private static final Logger logger = Logger.getLogger(BaseLBStrategy.class.getName());

   protected final List<String> healthyServers;
   protected final List<String> servers;
   protected final List<String> unHealthyServers;

   public BaseLBStrategy(List<String> servers) {
      this.servers = servers;
      this.healthyServers = new ArrayList<>();
      this.unHealthyServers = new ArrayList<>();
   }

   public List<String> getServers() {
      return new ArrayList<>(servers);
   }

   @Override
   public abstract String getNextServer();

   @Override
   public void startServersHealthCheck() {
      ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

      Runnable runnable = (() -> {
         ArrayList<String> serverList = new ArrayList<>(servers);
         for (String serverUrl : serverList) {
            CompletableFuture.runAsync(() -> executeServerHealthcheck(serverUrl));
         }
      });

      executorService.scheduleAtFixedRate(runnable, 0, 2, TimeUnit.SECONDS);
   }

   protected void updateHealthyServers(String serverUrl) {
      unHealthyServers.remove(serverUrl);

      if (!healthyServers.contains(serverUrl)) {
         healthyServers.add(serverUrl);
      }

      logger.info("Updated healthy serverUrl list " + serverUrl);
   }

   protected void updateUnHealthyServers(String serverUrl) {
      healthyServers.remove(serverUrl);

      if (!unHealthyServers.contains(serverUrl)) {
         unHealthyServers.add(serverUrl);
      }

      logger.info("Updated unhealthy serverUrl list " + Arrays.toString(unHealthyServers.toArray()));
   }

   private void executeServerHealthcheck(String serverUrl) {
      if (isServerHealthy(serverUrl)) {
         updateHealthyServers(serverUrl);
      } else {
         updateUnHealthyServers(serverUrl);
      }
   }

   private boolean isServerHealthy(String serverUrl) {
      int connectionTimeOut = 1;
      int connectionReadTimeOut = 3;

      OkHttpClient httpClient = new OkHttpClient.Builder()
              .connectTimeout(connectionTimeOut, TimeUnit.SECONDS)
              .readTimeout(connectionReadTimeOut, TimeUnit.SECONDS)
              .build();

      Request request = new Request.Builder()
              .url(serverUrl + "/health")
              .get()
              .build();

      try {
         int statusCode;
         String responseBody;

         try (Response response = httpClient.newCall(request).execute()) {
            statusCode = response.code();
            responseBody = response.body().string();
         }

         return statusCode == 200 && responseBody.endsWith("healthy");
      } catch (IOException e) {
         return false;
      }
   }
}
