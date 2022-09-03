package io.github.zemelua.umu_backpack.server;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.Environment;

import static net.fabricmc.api.EnvType.*;

@Environment(SERVER)
public class UMUBackpackServer implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		// NetworkHandler.initializeServer();
	}
}
