package org.apache.nifi.datageneration.validation;

import org.apache.nifi.annotation.lifecycle.OnDisabled;
import org.apache.nifi.annotation.lifecycle.OnEnabled;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.controller.ConfigurationContext;
import org.apache.nifi.processor.util.StandardValidators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ValidationRouter extends DynamicValidator implements TemplateOutputValidator {
    public static final PropertyDescriptor VALIDATOR_ATTRIBUTE = new PropertyDescriptor.Builder()
        .name("validator-attribute")
        .displayName("Validator Attribute")
        .description("The name of the flowfile attribute that contains the validator to use.")
        .defaultValue("${output.validator}")
        .addValidator(StandardValidators.NON_BLANK_VALIDATOR)
        .build();

    public static final List<PropertyDescriptor> DESCRIPTORS;

    static {
        List<PropertyDescriptor> _temp = new ArrayList<>();
        _temp.add(VALIDATOR_ATTRIBUTE);
        DESCRIPTORS = Collections.unmodifiableList(_temp);
    }

    @Override
    public List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return DESCRIPTORS;
    }

    private volatile Map<String, TemplateOutputValidator> validators;
    private volatile String flowFileAttribute;

    @OnEnabled
    public void onEnabled(ConfigurationContext context) {
        Map<String, TemplateOutputValidator> _temp = new HashMap<>();
        for (PropertyDescriptor desc : context.getProperties().keySet()) {
            if (desc.isDynamic()) {
                TemplateOutputValidator validator = context.getProperty(desc).asControllerService(TemplateOutputValidator.class);
                _temp.put(desc.getName(), validator);
            }
        }
        validators = new ConcurrentHashMap<>(_temp);
        flowFileAttribute = context.getProperty(VALIDATOR_ATTRIBUTE).evaluateAttributeExpressions().getValue();
    }

    @OnDisabled
    public void onDisabled() {
        validators = null;
    }

    @Override
    public void validate(String input, Map<String, String> attributes) {
        if (!attributes.containsKey(flowFileAttribute)) {
            throw new ValidationException(String.format("Flowfile attributes did not contain \"%s\"", flowFileAttribute));
        }
    }
}
