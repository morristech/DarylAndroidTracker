package com.appprova.daryl;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Map;

public class GoogleAnalyticsTracker implements TrackerAdapter {

    private final Tracker tracker;

    public GoogleAnalyticsTracker (Tracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public void logPageView(String name) {
        tracker.setScreenName(name);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void logEvent(Map<String, Object> eventData) {
        final String category = (String)eventData.get(Constants.EVENT_CATEGORY);
        final String action = (String)eventData.get(Constants.EVENT_ACTION);
        final String label = (String)eventData.get(Constants.EVENT_LABEL);
        final String campaignUrl = (String)eventData.get(Constants.EVENT_CAMPAIGN_URL);
        final Long value = (Long)eventData.get(Constants.EVENT_VALUE);
        this.logEvent(category, action, label, value == null ? 0 : value, campaignUrl);
    }

    @Override
    public void setUserProperty(String key, Object value) {
    }

    @Override
    public void logException(Throwable e) {
        tracker.send(new HitBuilders.ExceptionBuilder()
                .setDescription(e.toString()).build());
    }

    private void logEvent(String category, String action, String label, long value, String campaignUrl) {
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(value);
        if (campaignUrl != null) {
            builder.setCampaignParamsFromUrl(campaignUrl);
        }
        tracker.send(builder.build());
    }
}
