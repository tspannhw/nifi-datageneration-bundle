package org.apache.nifi.datageneration.templates

import org.apache.nifi.components.PropertyDescriptor
import org.apache.nifi.processor.AbstractProcessor
import org.apache.nifi.processor.ProcessContext
import org.apache.nifi.processor.ProcessSession
import org.apache.nifi.processor.exception.ProcessException

class PebbleTestProcessor extends AbstractProcessor {
    static final PropertyDescriptor REGISTRY = new PropertyDescriptor.Builder()
        .name("registry")
        .identifiesControllerService(TemplateRegistry.class)
        .required(true)
        .build()

    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return Arrays.asList(REGISTRY)
    }

    @Override
    void onTrigger(ProcessContext processContext, ProcessSession processSession) throws ProcessException {

    }
}
