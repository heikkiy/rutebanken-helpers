package org.entur.pubsub.camel;

import org.apache.camel.Category;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.apache.camel.support.DefaultEndpoint;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;

@UriEndpoint(firstVersion = "0.1", scheme = "entur-google-pubsub", title = "Entur Google Pubsub",
        syntax = "entur-google-pubsub:destinationName", category = Category.MESSAGING)
public class EnturGooglePubSubEndpoint extends DefaultEndpoint {

    @UriPath(description = "Destination Name")
    @Metadata(required = true)
    private String destinationName;

    @UriParam(name = "concurrentConsumers", description = "The number of parallel streams consuming from the subscription", defaultValue = "1")
    private Integer concurrentConsumers = 1;

    @UriParam(defaultValue = "AUTO", enums = "AUTO,NONE",
            description = "AUTO = exchange gets ack'ed/nack'ed on completion. NONE = downstream process has to ack/nack explicitly")
    private EnturGooglePubSubConstants.AckMode ackMode = EnturGooglePubSubConstants.AckMode.AUTO;

    private String projectId;
    private PubSubTemplate pubSubTemplate;

    public EnturGooglePubSubEndpoint(String uri, Component component, PubSubTemplate pubSubTemplate) {
        super(uri, component);
        if (!(component instanceof EnturGooglePubSubComponent)) {
            throw new IllegalArgumentException("The component provided is not EnturGooglePubSubComponent : " + component.getClass().getName());
        }
        this.pubSubTemplate = pubSubTemplate;
    }

    @Override
    public Consumer createConsumer(Processor processor) {
        return new EnturGooglePubSubConsumer(this, processor, pubSubTemplate);
    }

    @Override
    public Producer createProducer() {
        return new EnturGooglePubSubProducer(this, pubSubTemplate);
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }


    public EnturGooglePubSubConstants.AckMode getAckMode() {
        return ackMode;
    }

    public void setAckMode(EnturGooglePubSubConstants.AckMode ackMode) {
        this.ackMode = ackMode;
    }


    public Integer getConcurrentConsumers() {
        return concurrentConsumers;
    }

    public void setConcurrentConsumers(Integer concurrentConsumers) {
        this.concurrentConsumers = concurrentConsumers;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}

