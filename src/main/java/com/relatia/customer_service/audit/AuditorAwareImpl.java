package com.relatia.customer_service.audit;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @NotNull
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("system");
    }
}
