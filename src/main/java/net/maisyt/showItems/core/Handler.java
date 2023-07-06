package net.maisyt.showItems.core;

import net.maisyt.minecraft.util.resource.ModManagedResource;

public class Handler implements ModManagedResource {
    static public JobPool commonJobPool = new JobPool();

    @Override
    public void shutdown() {
        commonJobPool.shutdown();
    }

    @Override
    public void reload() {
        commonJobPool.reload();
    }
}
