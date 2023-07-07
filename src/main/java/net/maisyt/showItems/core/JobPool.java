package net.maisyt.showItems.core;

import net.maisyt.minecraft.util.resource.ModManagedResource;
import net.maisyt.showItems.ShowItemsMod;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobPool implements ModManagedResource {
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public void shutdown(){
        ShowItemsMod.LOGGER.info("Shutdown JobPool");
        if (executor != null && !executor.isShutdown()){
            executor.shutdown();
            if (!executor.isShutdown()){
                try {
                    Thread.sleep(1500);
                    executor.shutdownNow();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void reload(){
        ShowItemsMod.LOGGER.info("Reloading JobPool");
        shutdown();
        executor = Executors.newSingleThreadExecutor();
    }

    public void submit(Runnable runnable){
        executor.submit(runnable);
    }

    public boolean isShutdown(){
        return executor.isShutdown();
    }
}
