package org.example.frontend.gallery.imageutil;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.Plugin;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.plugins.gallery.imageutil.ImageUtils;
import org.hippoecm.frontend.plugins.gallery.model.GalleryProcessor;
import org.hippoecm.frontend.plugins.gallery.processor.ScalingParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ScalingCroppingGalleryProcessorPlugin extends Plugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScalingCroppingGalleryProcessor.class);

    private static final long serialVersionUID = 1L;

    protected static final String CONFIG_PARAM_WIDTH = "width";
    protected static final String CONFIG_PARAM_HEIGHT = "height";
    protected static final String CONFIG_PARAM_UPSCALING = "upscaling";
    protected static final String CONFIG_PARAM_OPTIMIZE = "optimize";
    protected static final String CONFIG_PARAM_COMPRESSION = "compression";

    protected static final int DEFAULT_WIDTH = 0;
    protected static final int DEFAULT_HEIGHT = 0;
    protected static final boolean DEFAULT_UPSCALING = false;
    protected static final String DEFAULT_OPTIMIZE = "quality";
    protected static final double DEFAULT_COMPRESSION = 1.0;

    /**
     * Necessarilly copied from ScalingGalleryProcessorPlugin
     */
    private static final Map<String, ImageUtils.ScalingStrategy> SCALING_STRATEGY_MAP = new LinkedHashMap<>();
    static {
        SCALING_STRATEGY_MAP.put("auto", ImageUtils.ScalingStrategy.AUTO);
        SCALING_STRATEGY_MAP.put("speed", ImageUtils.ScalingStrategy.SPEED);
        SCALING_STRATEGY_MAP.put("speed.and.quality", ImageUtils.ScalingStrategy.SPEED_AND_QUALITY);
        SCALING_STRATEGY_MAP.put("quality", ImageUtils.ScalingStrategy.QUALITY);
        SCALING_STRATEGY_MAP.put("best.quality", ImageUtils.ScalingStrategy.BEST_QUALITY);
    }


    public ScalingCroppingGalleryProcessorPlugin(final IPluginContext context, final IPluginConfig config) {
        super(context, config);
        final GalleryProcessor processor = createScalingCroppingGalleryProcessor(config);
        final String id = config.getString("gallery.processor.id", "gallery.processor.service");
        context.registerService(processor, id);
    }

    protected ScalingCroppingGalleryProcessor createScalingCroppingGalleryProcessor(IPluginConfig config) {
        final ScalingCroppingGalleryProcessor processor = new ScalingCroppingGalleryProcessor();

        for (IPluginConfig scaleConfig: config.getPluginConfigSet()) {
            final String nodeName = StringUtils.substringAfterLast(scaleConfig.getName(), ".");

            if (!StringUtils.isEmpty(nodeName)) {
                final int width = scaleConfig.getAsInteger(CONFIG_PARAM_WIDTH, DEFAULT_WIDTH);
                final int height = scaleConfig.getAsInteger(CONFIG_PARAM_HEIGHT, DEFAULT_HEIGHT);
                final boolean upscaling = scaleConfig.getAsBoolean(CONFIG_PARAM_UPSCALING, DEFAULT_UPSCALING);
                final float compressionQuality = (float) scaleConfig.getAsDouble(CONFIG_PARAM_COMPRESSION, DEFAULT_COMPRESSION);

                final String strategyName = scaleConfig.getString(CONFIG_PARAM_OPTIMIZE, DEFAULT_OPTIMIZE);
                ImageUtils.ScalingStrategy strategy = SCALING_STRATEGY_MAP.get(strategyName);
                if (strategy == null) {
                    LOGGER.warn("Image variant '{}' specifies an unknown scaling optimization strategy '{}'. Possible values are {}. Falling back to '{}' instead.",
                            new Object[]{nodeName, strategyName, SCALING_STRATEGY_MAP.keySet(), DEFAULT_OPTIMIZE});
                    strategy = SCALING_STRATEGY_MAP.get(DEFAULT_OPTIMIZE);
                }

                final ScalingParameters parameters = new ScalingParameters(width, height, upscaling, strategy, compressionQuality);
                LOGGER.debug("Scaling parameters for {}: {}", nodeName, parameters);
                processor.addScalingParameters(nodeName, parameters);
            }
        }

        return processor;
    }
}

