package femi.core.listeners;


import femi.core.utils.WebDriverUtils;
import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.StepResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("JavadocType")
public class StepsLogger  implements StepLifecycleListener {
    static Logger LOG = LoggerFactory.getLogger(WebDriverUtils.class);

    @Override
    public void beforeStepStop(final StepResult result) {
        LOG.info("Finishing step: {}", result.getName());
    }
}