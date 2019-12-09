package pl.jarosyjarosy.yougetin.rating.endpoint.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RatingMessage {
    private Long id;
    private Long givingId;
    private Long receiverId;
    private Boolean approval;
    private String comment;
}
