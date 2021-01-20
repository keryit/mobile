package femi.core.utils.element;

import net.serenitybdd.core.annotations.ImplementedBy;
import net.serenitybdd.core.pages.WebElementFacade;

import java.util.concurrent.TimeUnit;


@ImplementedBy(WebElementFacadeImplementation.class)

public interface IWebElementFacade extends WebElementFacade {
    public <T extends WebElementFacade> T setExplicitTimeout(final int timeout, final TimeUnit unit);
}
