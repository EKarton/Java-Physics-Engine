package com.javaphysicsengine.editor.editor;

import com.javaphysicsengine.api.body.PBody;

import java.util.ArrayList;
import java.util.List;

public class PEditorObservableStore extends PEditorStore {

    public interface EventListener {
        void onAddBody(PBody body);
    }

    List<EventListener> eventListeners = new ArrayList<>();

    public List<EventListener> getEventListeners() {
        return eventListeners;
    }

    public void setEventListeners(List<EventListener> eventListeners) {
        this.eventListeners = eventListeners;
    }


    @Override
    public void addBody(PBody body) {
        super.addBody(body);

        for (EventListener eventListener : eventListeners) {
            eventListener.onAddBody(body);
        }
    }
}
