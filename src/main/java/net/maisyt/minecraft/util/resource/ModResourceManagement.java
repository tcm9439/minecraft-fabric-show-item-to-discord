package net.maisyt.minecraft.util.resource;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.slf4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ModResourceManagement {
    Set<ModManagedResource> managedResources = new HashSet<>();

    public void loadJarResource(String modeID, Logger logger, Map<String, InputStream> resources){
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
                return SimpleSynchronousResourceReloadListener.super.reload(synchronizer, manager, prepareProfiler, applyProfiler, prepareExecutor, applyExecutor);
            }

            @Override
            public String getName() {
                return SimpleSynchronousResourceReloadListener.super.getName();
            }

            @Override
            public void reload(ResourceManager manager) {
                for (String resource : resources.keySet()) {
                    try {
                        logger.info("Loading resource: {}", resource);
                        InputStream streamFromModJar = manager.getResource(new Identifier(modeID, resource)).orElseThrow().getInputStream();
                        resources.put(resource, new BufferedInputStream(streamFromModJar));
                    } catch (IOException e) {
                        logger.error("Failed to load resource: {}", resource, e);
                    }
                }
            }

            @Override
            public Identifier getFabricId() {
                return new Identifier(modeID, "reload_listener");
            }

            @Override
            public Collection<Identifier> getFabricDependencies() {
                return SimpleSynchronousResourceReloadListener.super.getFabricDependencies();
            }
        });
    }

    public void register(ModManagedResource resource) {
        managedResources.add(resource);
    }

    public void shutdown() {
        for (ModManagedResource resource : managedResources) {
            resource.shutdown();
        }
//        managedResources.clear();
    }

    public void reload() {
        for (ModManagedResource resource : managedResources) {
            resource.reload();
        }
    }
}
