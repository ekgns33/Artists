package org.ekgns33.artists.external.event;

public record KopisVenueImportFailedEvent(String performanceId, String venueId, int retryCount) { }

