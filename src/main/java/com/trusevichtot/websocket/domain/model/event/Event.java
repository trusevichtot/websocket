package com.trusevichtot.websocket.domain.model.event;

import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.ZonedDateTime;

import static java.util.Objects.nonNull;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Event implements Serializable {

    @Include
    private String id;
    private String type;
    private String communicationId;
    private ZonedDateTime created;
    private String creator;
    private String payload;

    public boolean hasCommunicationId() {
        return nonNull(communicationId) && !communicationId.isEmpty();
    }
}
