package io.github.zemelua.umu_backpack;

import io.github.zemelua.umu_config.config.IConfigProvider;
import io.github.zemelua.umu_config.config.container.ConfigContainer;
import io.github.zemelua.umu_config.config.container.IConfigContainer;
import io.github.zemelua.umu_config.config.value.BooleanConfigValue;
import io.github.zemelua.umu_config.config.value.IConfigValue;

import java.util.List;

public final class ModConfigs implements IConfigProvider {
	public static final IConfigValue<Boolean> FREELY_DETACH;
	public static final IConfigContainer CONFIG;

	@Override
	public List<IConfigContainer> getConfigs() {
		return List.of(CONFIG);
	}

	static {
		FREELY_DETACH = new BooleanConfigValue(UMUBackpack.identifier("freely_detach"), false);
		CONFIG = new ConfigContainer(UMUBackpack.identifier("umu_backpack"), FREELY_DETACH);
	}
}
