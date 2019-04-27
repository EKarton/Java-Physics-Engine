package com.javaphysicsengine.editor.editor;

import com.javaphysicsengine.api.body.PBody;

import java.util.ArrayList;
import java.util.List;

public class PEditorObservableStore extends PEditorStore {

    public interface OnAddBodyListener {
        void onAddBody(PBody body);
    }

    public interface OnDeleteBodyListener {
        void onDeleteBody(String objectName);
    }

    public interface OnClearBodiesListener {
        void onClearBodies();
    }

    public interface OnSelectedBodyListener {
        void onSelectedBodyChanged(PBody newBody);
    }

    public interface OnChangeBodyNameListener {
        void onChangedBodyName(PBody bodyWithNewName, String oldName);
    }

    private List<OnAddBodyListener> onAddBodyListeners = new ArrayList<>();
    private List<OnDeleteBodyListener> onDeleteBodyListeners = new ArrayList<>();
    private List<OnClearBodiesListener> onClearBodiesListeners = new ArrayList<>();
    private List<OnSelectedBodyListener> onSelectedBodyListeners = new ArrayList<>();
    private List<OnChangeBodyNameListener> onChangeBodyNameListeners = new ArrayList<>();

    public List<OnDeleteBodyListener> getOnDeleteBodyListeners() {
        return onDeleteBodyListeners;
    }

    public void setOnDeleteBodyListeners(List<OnDeleteBodyListener> onDeleteBodyListeners) {
        this.onDeleteBodyListeners = onDeleteBodyListeners;
    }

    public List<OnAddBodyListener> getOnAddBodyListeners() {
        return onAddBodyListeners;
    }

    public void setOnAddBodyListeners(List<OnAddBodyListener> onAddBodyListeners) {
        this.onAddBodyListeners = onAddBodyListeners;
    }

    public List<OnClearBodiesListener> getOnClearBodiesListeners() {
        return onClearBodiesListeners;
    }

    public void setOnClearBodiesListeners(List<OnClearBodiesListener> onClearBodiesListeners) {
        this.onClearBodiesListeners = onClearBodiesListeners;
    }

    public List<OnSelectedBodyListener> getOnSelectedBodyListeners() {
        return onSelectedBodyListeners;
    }

    public void setOnSelectedBodyListeners(List<OnSelectedBodyListener> onSelectedBodyListeners) {
        this.onSelectedBodyListeners = onSelectedBodyListeners;
    }

    public List<OnChangeBodyNameListener> getOnChangeBodyNameListeners() {
        return onChangeBodyNameListeners;
    }

    public void setOnChangeBodyNameListeners(List<OnChangeBodyNameListener> onChangeBodyNameListeners) {
        this.onChangeBodyNameListeners = onChangeBodyNameListeners;
    }

    @Override
    public void addBody(PBody body) {
        super.addBody(body);

        for (OnAddBodyListener onAddBodyListener : onAddBodyListeners) {
            onAddBodyListener.onAddBody(body);
        }
    }

    @Override
    public void deleteBody(String objectName) {
        super.deleteBody(objectName);

        for (OnDeleteBodyListener onDeleteBodyListener : onDeleteBodyListeners) {
            onDeleteBodyListener.onDeleteBody(objectName);
        }
    }

    @Override
    public void clearBodies() {
        super.clearBodies();

        for (OnClearBodiesListener listeners : onClearBodiesListeners) {
            listeners.onClearBodies();
        }
    }

    @Override
    public void setSelectedBody(PBody selectedBody) {
        super.setSelectedBody(selectedBody);

        for (OnSelectedBodyListener listener : onSelectedBodyListeners) {
            listener.onSelectedBodyChanged(selectedBody);
        }
    }

    @Override
    public void changeBodyName(String newName, PBody body) {
        String oldName = body.getName();
        super.changeBodyName(newName, body);

        for (OnChangeBodyNameListener listener : onChangeBodyNameListeners) {
            listener.onChangedBodyName(body, oldName);
        }
    }
}
